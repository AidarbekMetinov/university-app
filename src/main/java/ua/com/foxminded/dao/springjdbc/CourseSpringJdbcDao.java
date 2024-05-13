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
import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class CourseSpringJdbcDao implements CourseDao {

	private static final String SAVE = "INSERT INTO public.course (name, description) VALUES (?, ?)";
	private static final String UPDATE = "UPDATE public.course SET (name, description) = (?, ?) WHERE id = ?";
	private static final String FIND_BY_ID = "SELECT * FROM public.course WHERE id = ?";
	private static final String EXISTS_BY_ID = "SELECT count(*) FROM public.course WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM public.course";
	private static final String COUNT = "SELECT COUNT(*) FROM public.course";
	private static final String UPDATE_LECTURES = "UPDATE public.lecture SET course_id = NULL WHERE course_id = ?";
	private static final String DELETE = "DELETE FROM public.course_teacher WHERE course_id = ?; DELETE FROM public.course WHERE id = ?";
	private static final String DELETE_ALL = "UPDATE public.lecture SET course_id = NULL; DELETE FROM public.course_teacher; DELETE FROM public.course";
	private static final String SEARCH = "SELECT * FROM public.course WHERE course.name iLIKE ?";
	private static final String FIND_BY_TEACHER_ID = "SELECT * FROM public.course JOIN course_teacher "
			+ "ON course.id = course_teacher.course_id WHERE course_teacher.teacher_id = ?";

	private final JdbcTemplate jdbcTemplate;

	private RowMapper<Course> courseMapper = new RowMapper<Course>() {
		public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
			Course course = new Course();
			course.setId(rs.getInt("id"));
			course.setName(rs.getString("name"));
			course.setDescription(rs.getString("description"));
			return course;
		}
	};

	@Override
	public Course save(Course entity) {
		String name = null;
		try {
			name = entity.getName();
			log.debug("Save course {}", name);
			Integer id = jdbcTemplate.update(SAVE, name, entity.getDescription());
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save course {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save course {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Course entity) {
		String name = null;
		try {
			name = entity.getName();
			log.debug("Update course {}", name);
			jdbcTemplate.update(UPDATE, name, entity.getDescription(), entity.getId());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update course {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update course {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Course> findById(Integer id) {
		try {
			log.debug("Find course by id = {}", id);
			return ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, courseMapper, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find course by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find course by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		try {
			Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Integer.class, id);
			boolean exists = ofNullable(count).filter(number -> number > 0).isPresent();
			log.debug("Exists course by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check course by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check course by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Course> findAll() {
		try {
			log.debug("Find all courses");
			return jdbcTemplate.query(FIND_ALL, courseMapper);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all courses", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all courses", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		log.debug("Count courses");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			log.debug("Delete course by id = {}", id);
			jdbcTemplate.update(UPDATE_LECTURES, id);
			jdbcTemplate.update(DELETE, id, id);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete course by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete course by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			log.debug("Delete all courses");
			jdbcTemplate.update(DELETE_ALL);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all courses", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all courses", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Course>> findByNameLike(String name) {
		try {
			log.debug("Find courses by name {}", name);
			return ofNullable(jdbcTemplate.query(SEARCH, courseMapper, "%" + name + "%"));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find courses by name {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find courses by name {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Course>> findByTeacherId(Integer id) {
		try {
			log.debug("Find courses by teacher id {}", id);
			return ofNullable(jdbcTemplate.query(FIND_BY_TEACHER_ID, courseMapper, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find courses by teacher id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find courses by teacher id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}
}