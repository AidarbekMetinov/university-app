package ua.com.foxminded.dao.springjdbc;

import static java.util.Optional.ofNullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class StudentSpringJdbcDao implements StudentDao {

	private static final String SAVE = "INSERT INTO public.student (first_name, last_name, email, group_id) VALUES (?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE public.student SET (first_name, last_name, email, group_id) = (?, ?, ?, ?) WHERE id = ?";
	private static final String FIND_BY_ID = "SELECT * FROM public.student WHERE id = ?";
	private static final String FIND_BY_GROUP = "SELECT * FROM public.student WHERE group_id = ?";
	private static final String EXISTS_BY_ID = "SELECT count(*) FROM public.student WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM public.student";
	private static final String COUNT = "SELECT COUNT(*) FROM public.student";
	private static final String DELETE = "DELETE FROM public.student WHERE id = ?";
	private static final String DELETE_ALL = "DELETE FROM public.student";
	private static final String SEARCH_BY_FIRST_NAME = "SELECT * FROM public.student WHERE student.first_name iLIKE ?";
	private static final String SEARCH_BY_LAST_NAME = "SELECT * FROM public.student WHERE student.last_name iLIKE ?";
	private static final String DELETE_FROM_GROUP = "UPDATE public.student SET group_id = NULL WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;

	private RowMapper<Student> studentMapper = new RowMapper<Student>() {
		public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
			Student student = new Student();
			student.setId(rs.getInt("id"));
			student.setFirstName(rs.getString("first_name"));
			student.setLastName(rs.getString("last_name"));
			student.setEmail(rs.getString("email"));
			student.setGroupId(rs.getInt("group_id"));
			return student;
		}
	};

	@Override
	public Student save(Student entity) {
		String firstName = null;
		String lastName = null;
		try {
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			log.debug("Save student {} {}", firstName, lastName);
			Integer id = jdbcTemplate.update(SAVE, firstName, lastName, entity.getEmail(), entity.getGroupId());
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save student {} {}", firstName, lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save student {} {}", firstName, lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Student entity) {
		String firstName = null;
		String lastName = null;
		try {
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			log.debug("Update student {} {}", firstName, lastName);
			jdbcTemplate.update(UPDATE, firstName, lastName, entity.getEmail(), entity.getGroupId(), entity.getId());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update student {} {}", firstName, lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update student {} {}", firstName, lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Student> findById(Integer id) {
		try {
			log.debug("Find student by id = {}", id);
			return ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, studentMapper, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find student by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find student by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Student>> findByGroupId(Integer groupId) {
		try {
			log.debug("Find student by groupId = {}", groupId);
			return ofNullable(jdbcTemplate.query(FIND_BY_GROUP, studentMapper, groupId));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find student by groupId = {}", groupId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find student by groupId = {}", groupId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		try {
			Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Integer.class, id);
			boolean exists = ofNullable(count).filter(number -> number > 0).isPresent();
			log.debug("Exists student by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check student by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check student by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Student> findAll() {
		try {
			log.debug("Find all students");
			return jdbcTemplate.query(FIND_ALL, studentMapper);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all students", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all students", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		log.debug("Count students");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			log.debug("Delete student by id = {}", id);
			jdbcTemplate.update(DELETE, id);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete student by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete student by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			log.debug("Delete all students");
			jdbcTemplate.update(DELETE_ALL);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all students", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all students", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Student>> findByFirstNameLike(String firstName) {
		try {
			log.debug("Find students by firstName {}", firstName);
			return ofNullable(jdbcTemplate.query(SEARCH_BY_FIRST_NAME, studentMapper, "%" + firstName + "%"));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find students by firstName {}", firstName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find students by firstName {}", firstName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Student>> findByLastNameLike(String lastName) {
		try {
			log.debug("Find students by lastName {}", lastName);
			return ofNullable(jdbcTemplate.query(SEARCH_BY_LAST_NAME, studentMapper, "%" + lastName + "%"));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find students by lastName {}", lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find students by lastName {}", lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteStudentFromGroup(Integer studentId) {
		try {
			log.debug("Delete student id {} from group", studentId);
			jdbcTemplate.update(DELETE_FROM_GROUP, studentId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete student id {} from group", studentId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete student id {} from group", studentId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}