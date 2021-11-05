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
import ua.com.foxminded.dao.TeacherDao;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;
import ua.com.foxminded.service.CourseService;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class TeacherSpringJdbcDao implements TeacherDao {

	private static final String SAVE = "INSERT INTO public.teacher (first_name, last_name) VALUES (?, ?)";
	private static final String UPDATE = "UPDATE public.teacher SET (first_name, last_name) = (?, ?) WHERE id = ?";
	private static final String FIND_BY_ID = "SELECT * FROM public.teacher WHERE id = ?";
	private static final String EXISTS_BY_ID = "SELECT count(*) FROM public.teacher WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM public.teacher";
	private static final String COUNT = "SELECT COUNT(*) FROM public.teacher";
	private static final String DELETE_BY_ID = "UPDATE public.lecture SET teacher_id = NULL WHERE teacher_id = ?;"
			+ "DELETE FROM public.course_teacher WHERE teacher_id = ?; DELETE FROM public.faculty_teacher WHERE teacher_id = ?;"
			+ "DELETE FROM public.teacher WHERE id = ?";
	private static final String DELETE_ALL = "UPDATE public.lecture SET teacher_id = NULL; DELETE FROM public.faculty_teacher;"
			+ "DELETE FROM public.course_teacher; DELETE FROM public.teacher";
	private static final String SEARCH_BY_FIRST_NAME = "SELECT * FROM public.teacher WHERE teacher.first_name iLIKE ?";
	private static final String SEARCH_BY_LAST_NAME = "SELECT * FROM public.teacher WHERE teacher.last_name iLIKE ?";
	private static final String DELETE_COURSE_FROM_TEACHER = "DELETE FROM public.course_teacher WHERE course_id = ? AND teacher_id = ?";
	private static final String ADD_COURSE_TO_TEACHER = "INSERT INTO public.course_teacher (course_id, teacher_id) VALUES (?, ?)";
	private static final String FIND_BY_FACULTY_ID = "SELECT * FROM public.teacher JOIN faculty_teacher "
			+ "ON teacher.id = faculty_teacher.teacher_id WHERE faculty_teacher.faculty_id = ?";
	private static final String DELETE_TEACHER_FROM_FACULTY = "DELETE FROM public.faculty_teacher WHERE teacher_id = ? AND faculty_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private final CourseService courseService;

	private RowMapper<Teacher> teacherMapper = new RowMapper<Teacher>() {
		public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
			Teacher teacher = new Teacher();
			Integer id = rs.getInt("id");
			teacher.setId(id);
			teacher.setFirstName(rs.getString("first_name"));
			teacher.setLastName(rs.getString("last_name"));
			teacher.setCourses(courseService.findByTeacherId(id));
			return teacher;
		}
	};

	@Override
	public Teacher save(Teacher entity) {
		String firstName = null;
		String lastName = null;
		try {
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			log.debug("Save teacher {} {}", firstName, lastName);
			Integer id = jdbcTemplate.update(SAVE, firstName, lastName);
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save teacher {} {}", firstName, lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save teacher {} {}", firstName, lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Teacher entity) {
		String firstName = null;
		String lastName = null;
		try {
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			log.debug("Update teacher {} {}", firstName, lastName);
			jdbcTemplate.update(UPDATE, firstName, lastName, entity.getId());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update teacher {} {}", firstName, lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update teacher {} {}", firstName, lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Teacher> findById(Integer id) {
		try {
			log.debug("Find teacher by id = {}", id);
			return ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, teacherMapper, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find teacher by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find teacher by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		try {
			Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Integer.class, id);
			boolean exists = ofNullable(count).filter(number -> number > 0).isPresent();
			log.debug("Exists teacher by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check teacher by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check teacher by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Teacher> findAll() {
		try {
			log.debug("Find all teachers");
			return jdbcTemplate.query(FIND_ALL, teacherMapper);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all teachers", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all teachers", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		log.debug("Count teachers");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			log.debug("Delete teacher by id = {}", id);
			jdbcTemplate.update(DELETE_BY_ID, id, id, id, id);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete teacher by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete teacher by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			log.debug("Delete all teachers");
			jdbcTemplate.update(DELETE_ALL);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all teachers", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all teachers", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Teacher>> findByFirstNameLike(String firstName) {
		try {
			log.debug("Find teachers by firstName {}", firstName);
			return ofNullable(jdbcTemplate.query(SEARCH_BY_FIRST_NAME, teacherMapper, "%" + firstName + "%"));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find teachers by firstName {}", firstName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find teachers by firstName {}", firstName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Teacher>> findByLastNameLike(String lastName) {
		try {
			log.debug("Find teachers by lastName {}", lastName);
			return ofNullable(jdbcTemplate.query(SEARCH_BY_LAST_NAME, teacherMapper, "%" + lastName + "%"));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find teachers by lastName {}", lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find teachers by lastName {}", lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteCourseFromTeacher(Integer teacherId, Integer courseId) {
		try {
			log.debug("Delete course by id {} from teacher by id {}", courseId, teacherId);
			jdbcTemplate.update(DELETE_COURSE_FROM_TEACHER, courseId, teacherId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete course by id {} from teacher by id {}", courseId, teacherId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete course by id {} from teacher by id {}", courseId, teacherId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addCourseToTeacher(Integer teacherId, Integer courseId) {
		try {
			log.debug("Add course by id {} to teacher by id {}", courseId, teacherId);
			jdbcTemplate.update(ADD_COURSE_TO_TEACHER, courseId, teacherId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add course by id {} to teacher by id {}", courseId, teacherId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add course by id {} to teacher by id {}", courseId, teacherId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Teacher>> findByFacultyId(Integer id) {
		try {
			log.debug("Find teachers by faculty id {}", id);
			return ofNullable(jdbcTemplate.query(FIND_BY_FACULTY_ID, teacherMapper, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find teachers by faculty id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find teachers by faculty id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteTeacherFromFaculty(Integer teacherId, Integer facultyId) {
		try {
			log.debug("Delete teacher id {} from faculty id {}", teacherId, facultyId);
			jdbcTemplate.update(DELETE_TEACHER_FROM_FACULTY, teacherId, facultyId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete teacher id {} from faculty id {}", teacherId, facultyId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete teacher id {} from faculty id {}", teacherId, facultyId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}