package ua.com.foxminded.dao.springjdbc;

import static java.util.Optional.ofNullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import ua.com.foxminded.dao.LectureDao;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;
import ua.com.foxminded.service.AudienceService;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class LectureSpringJdbcDao implements LectureDao {

	private static final String SAVE = "INSERT INTO public.lecture (audience_id, teacher_id, course_id, date_time) VALUES (SELECT id FROM audience WHERE id = ?, SELECT id FROM teacher WHERE id = ?, SELECT id FROM course WHERE id = ?, ?)";
	private static final String UPDATE = "UPDATE public.lecture SET (audience_id, teacher_id, course_id, date_time) = (?, ?, ?, ?) WHERE id = ?";
	private static final String FIND_BY_ID = "SELECT * FROM lecture WHERE lecture.id = ?";
	private static final String FIND_BY_DATE = "SELECT * FROM lecture WHERE date_time = ?";
	private static final String EXISTS_BY_ID = "SELECT count(*) FROM public.lecture WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM lecture";
	private static final String COUNT = "SELECT COUNT(*) FROM public.lecture";
	private static final String UPDATE_LECTURE_GROUPS = "DELETE FROM public.group_lecture WHERE lecture_id = ?";
	private static final String DELETE = "DELETE FROM public.lecture WHERE id = ?";
	private static final String DELETE_ALL = "DELETE FROM public.group_lecture; DELETE FROM public.lecture";
	private static final String ADD_GROUP_TO_LECTURE = "INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (?, ?)";

	private final JdbcTemplate jdbcTemplate;
	private final CourseService courseService;
	private final TeacherService teacherService;
	private final AudienceService audienceService;

	private RowMapper<Lecture> lectureMapper = new RowMapper<Lecture>() {
		public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
			Lecture lecture = new Lecture();
			lecture.setId(rs.getInt("id"));
			lecture.setCourse(courseService.findById(rs.getInt("course_id")));
			lecture.setAudience(audienceService.findById(rs.getInt("audience_id")));
			lecture.setTeacher(teacherService.findById(rs.getInt("teacher_id")));
			lecture.setDateTime(
					LocalDateTime.parse(rs.getString("date_time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			return lecture;
		}
	};

	@Override
	public Lecture save(Lecture entity) {
		try {
			String dateTime = entity.getDateTime().toString();
			log.debug("Save lecture starts = {}", dateTime);
			Integer id = jdbcTemplate.update(SAVE, entity.getAudience().getId(), entity.getTeacher().getId(),
					entity.getCourse().getId(), entity.getDateTime());
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save lecture", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save lecture", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Lecture entity) {
		Integer id = null;
		try {
			String dateTime = entity.getDateTime().toString();
			id = entity.getId();
			log.debug("Save lecture starts = {}", dateTime);
			jdbcTemplate.update(UPDATE, entity.getAudience().getId(), entity.getTeacher().getId(),
					entity.getCourse().getId(), entity.getDateTime(), id);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update lecture by id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update lecture by id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Lecture> findById(Integer id) {
		try {
			log.debug("Find lecture by id = {}", id);
			return ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, lectureMapper, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find lecture by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find lecture by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Lecture>> findByDateTime(LocalDateTime dateTime) {
		String dateString = null;
		try {
			dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
			log.debug("Find lectures by date = {}", dateString);
			return ofNullable(jdbcTemplate.query(FIND_BY_DATE, lectureMapper, dateTime));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find lectures by date = {}", dateString, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find lectures by date = {}", dateString, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		try {
			Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Integer.class, id);
			boolean exists = ofNullable(count).filter(number -> number > 0).isPresent();
			log.debug("Exists lecture by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check lecture by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check lecture by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Lecture> findAll() {
		try {
			log.debug("Find all lectures");
			return jdbcTemplate.query(FIND_ALL, lectureMapper);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all lectures", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all lectures", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		log.debug("Count lectures");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			log.debug("Delete lecture by id = {}", id);
			jdbcTemplate.update(UPDATE_LECTURE_GROUPS, id);
			jdbcTemplate.update(DELETE, id);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete lecture by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete lecture by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			log.debug("Delete all lectures");
			jdbcTemplate.update(DELETE_ALL);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all lectures", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all lectures", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addGroupToLecture(Integer lectureId, Integer groupId) {
		try {
			log.debug("Add group by id {} to lecture by id {}", groupId, lectureId);
			jdbcTemplate.update(ADD_GROUP_TO_LECTURE, groupId, lectureId);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add group by id {} to lecture by id {}", groupId, lectureId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add group by id {} to lecture by id {}", groupId, lectureId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}
