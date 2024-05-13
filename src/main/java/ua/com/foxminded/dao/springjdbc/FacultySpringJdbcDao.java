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
import ua.com.foxminded.dao.FacultyDao;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class FacultySpringJdbcDao implements FacultyDao {

	private static final String SAVE = "INSERT INTO public.faculty (name, description) VALUES (?, ?)";
	private static final String UPDATE = "UPDATE public.faculty SET (name, description) = (?, ?) WHERE id = ?";
	private static final String FIND_BY_ID = "SELECT * FROM public.faculty WHERE id = ?";
	private static final String EXISTS_BY_ID = "SELECT count(*) FROM public.faculty WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM public.faculty";
	private static final String COUNT = "SELECT COUNT(*) FROM public.faculty";
	private static final String UPDATE_TEACHERS = "DELETE FROM public.faculty_teacher WHERE faculty_id = ?";
	private static final String UPDATE_GROUPS = "UPDATE public.ugroup SET faculty_id = NULL WHERE faculty_id = ?";
	private static final String DELETE = "DELETE FROM public.faculty WHERE id = ?";
	private static final String DELETE_ALL = "DELETE FROM public.faculty_teacher; UPDATE public.ugroup SET faculty_id = NULL; DELETE FROM public.faculty";
	private static final String SEARCH = "SELECT * FROM public.faculty WHERE faculty.name iLIKE ?";
	private static final String ADD_TEACHER = "INSERT INTO public.faculty_teacher (faculty_id, teacher_id) VALUES (?, ?)";
	private static final String ADD_GROUP = "UPDATE public.ugroup SET (faculty_id) = (SELECT id FROM faculty WHERE faculty.id = ?) WHERE ugroup.id = ?";

	private final JdbcTemplate jdbcTemplate;

	private RowMapper<Faculty> facultyMapper = new RowMapper<Faculty>() {
		public Faculty mapRow(ResultSet rs, int rowNum) throws SQLException {
			Faculty faculty = new Faculty();
			faculty.setId(rs.getInt("id"));
			faculty.setName(rs.getString("name"));
			faculty.setDescription(rs.getString("description"));
			return faculty;
		}
	};

	@Override
	public Faculty save(Faculty entity) {
		String name = null;
		try {
			name = entity.getName();
			log.debug("Save faculty {}", name);
			entity.setId(jdbcTemplate.update(SAVE, name, entity.getDescription()));
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save faculty {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save faculty {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Faculty entity) {
		String name = null;
		try {
			name = entity.getName();
			log.debug("Update faculty {}", name);
			jdbcTemplate.update(UPDATE, name, entity.getDescription(), entity.getId());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update faculty {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update faculty {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Faculty> findById(Integer id) {
		try {
			log.debug("Find faculty by id = {}", id);
			return ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, facultyMapper, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find faculty by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find faculty by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		try {
			Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Integer.class, id);
			boolean exists = ofNullable(count).filter(number -> number > 0).isPresent();
			log.debug("Exists faculty by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check faculty by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check faculty by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Faculty> findAll() {
		try {
			log.debug("Find all faculties");
			return jdbcTemplate.query(FIND_ALL, facultyMapper);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all faculties", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all faculties", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		log.debug("Count faculties");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			log.debug("Delete faculty by id = {}", id);
			jdbcTemplate.update(UPDATE_TEACHERS, id);
			jdbcTemplate.update(UPDATE_GROUPS, id);
			jdbcTemplate.update(DELETE, id);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete faculty by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete faculty by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			log.debug("Delete all faculties");
			jdbcTemplate.update(DELETE_ALL);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all faculties", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all faculties", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Faculty>> findByNameLike(String name) {
		try {
			log.debug("Find faculties by name {}", name);
			return ofNullable(jdbcTemplate.query(SEARCH, facultyMapper, "%" + name + "%"));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find faculties by name {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find faculties by name {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addGroup(Integer facultyId, Integer groupId) {
		try {
			log.debug("Add group id {} to faculty id {}", groupId, facultyId);
			jdbcTemplate.update(ADD_GROUP, facultyId, groupId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add group id {} to faculty id {}", groupId, facultyId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add group id {} to faculty id {}", groupId, facultyId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addTeacher(Integer facultyId, Integer teacherId) {
		try {
			log.debug("Add teacher id {} to faculty id {}", teacherId, facultyId);
			jdbcTemplate.update(ADD_TEACHER, facultyId, teacherId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add teacher id {} to faculty id {}", teacherId, facultyId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add teacher id {} to faculty id {}", teacherId, facultyId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}
