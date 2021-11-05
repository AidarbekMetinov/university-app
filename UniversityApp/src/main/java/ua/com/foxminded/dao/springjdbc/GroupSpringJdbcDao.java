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
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class GroupSpringJdbcDao implements GroupDao {

	private static final String SAVE = "INSERT INTO public.ugroup (name, faculty_id) VALUES (?, ?)";
	private static final String UPDATE = "UPDATE public.ugroup SET (name, faculty_id) = (?, ?) WHERE id = ?";
	private static final String FIND_BY_ID = "SELECT * FROM ugroup WHERE ugroup.id = ?";
	private static final String FIND_BY_FACULTY = "SELECT * FROM ugroup WHERE ugroup.faculty_id = ?";
	private static final String FIND_BY_LECTURE = "SELECT * FROM ugroup JOIN group_lecture ON group_lecture.group_id = ugroup.id WHERE group_lecture.lecture_id = ?";
	private static final String EXISTS_BY_ID = "SELECT count(*) FROM ugroup WHERE ugroup.id = ?";
	private static final String FIND_ALL = "SELECT * FROM public.ugroup";
	private static final String COUNT = "SELECT COUNT(*) FROM public.ugroup";
	private static final String UPDATE_STUDENTS = "UPDATE public.student SET group_id = NULL WHERE group_id = ?";
	private static final String UPDATE_LECTURE_GROUPS = "DELETE FROM public.group_lecture WHERE group_id = ?";
	private static final String DELETE = "DELETE FROM public.ugroup WHERE id = ?";
	private static final String DELETE_ALL = "UPDATE public.student SET group_id = NULL; DELETE FROM public.group_lecture; DELETE FROM public.ugroup";
	private static final String SEARCH = "SELECT * FROM public.ugroup WHERE ugroup.name iLIKE ?";
	private static final String DELETE_GROUP_FROM_LECTURE = "DELETE FROM public.group_lecture WHERE group_id = ? AND lecture_id = ?";
	private static final String ADD_STUDENT = "UPDATE public.student SET (group_id) = (SELECT id FROM ugroup WHERE id = ?) WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;

	private RowMapper<Group> groupMapper = new RowMapper<Group>() {
		public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
			Group group = new Group();
			group.setId(rs.getInt("id"));
			group.setName(rs.getString("name"));
			group.setFacultyId(rs.getInt("faculty_id"));
			return group;
		}
	};

	@Override
	public Group save(Group entity) {
		String name = null;
		try {
			name = entity.getName();
			log.debug("Save group {}", name);
			entity.setId(jdbcTemplate.update(SAVE, name, entity.getFacultyId()));
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save group {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save group {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Group entity) {
		String name = null;
		try {
			name = entity.getName();
			log.debug("Update group {}", name);
			jdbcTemplate.update(UPDATE, name, entity.getFacultyId(), entity.getId());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update group {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update group {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Group> findById(Integer id) {
		try {
			log.debug("Find group by id = {}", id);
			return ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, groupMapper, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find group by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find group by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Group>> findByFacultyId(Integer facultyId) {
		try {
			log.debug("Find group by facultyId = {}", facultyId);
			return ofNullable(jdbcTemplate.query(FIND_BY_FACULTY, groupMapper, facultyId));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find group by facultyId = {}", facultyId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find group by facultyId = {}", facultyId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Group>> findByLectureId(Integer lectureId) {
		try {
			log.debug("Find group by lectureId = {}", lectureId);
			return ofNullable(jdbcTemplate.query(FIND_BY_LECTURE, groupMapper, lectureId));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find group by lectureId = {}", lectureId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find group by lectureId = {}", lectureId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		try {
			Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Integer.class, id);
			boolean exists = ofNullable(count).filter(number -> number > 0).isPresent();
			log.debug("Exists group by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check group by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check group by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Group> findAll() {
		try {
			log.debug("Find all groups");
			return jdbcTemplate.query(FIND_ALL, groupMapper);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all groups", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all groups", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		log.debug("Count groups");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			log.debug("Delete group by id = {}", id);
			jdbcTemplate.update(UPDATE_STUDENTS, id);
			jdbcTemplate.update(UPDATE_LECTURE_GROUPS, id);
			jdbcTemplate.update(DELETE, id);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete group by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete group by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			log.debug("Delete all groups");
			jdbcTemplate.update(DELETE_ALL);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all groups", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all groups", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Group>> findByNameLike(String name) {
		try {
			log.debug("Find groups by name {}", name);
			return ofNullable(jdbcTemplate.query(SEARCH, groupMapper, "%" + name + "%"));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find groups by name {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find groups by name {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteGroupFromLecture(Integer groupId, Integer lectureId) {
		try {
			log.debug("Delete group id {} from lecture id {}", groupId, lectureId);
			jdbcTemplate.update(DELETE_GROUP_FROM_LECTURE, groupId, lectureId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete group id {} from lecture id {}", groupId, lectureId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete group id {} from lecture id {}", groupId, lectureId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addStudent(Integer groupId, Integer studentId) {
		try {
			log.debug("Add student id {} to group id {}", studentId, groupId);
			jdbcTemplate.update(ADD_STUDENT, groupId, studentId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add student id {} to group id {}", studentId, groupId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add student id {} to group id {}", studentId, groupId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}