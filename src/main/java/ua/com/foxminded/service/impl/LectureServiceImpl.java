package ua.com.foxminded.service.impl;

import static java.lang.String.format;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.crm.CrmLecture;
import ua.com.foxminded.dao.LectureDao;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.exception.NotFoundException;
import ua.com.foxminded.service.AudienceService;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

	private final LectureDao lectureDao;
	private final CourseService courseService;
	private final TeacherService teacherService;
	private final AudienceService audienceService;

	@Override
	@Transactional
	public Lecture save(Lecture entity) {
		log.debug("Saving new lecture {}", entity);
		return lectureDao.save(entity);
	}

	@Override
	@Transactional
	public void update(Lecture entity) {
		Integer id = entity.getId();

		log.debug("Updating lecture {}", entity);
		if (existsById(id)) {
			lectureDao.update(entity);
		} else {
			log.debug("Updating lecture {} FAILED", entity);
			throw new NotFoundException(format("Could'nt update, lecture by id '%s' doesn't exists", id));
		}
	}

	@Override
	@Transactional
	public Lecture findById(Integer id) {
		log.debug("Find lecture by id {}", id);
		return lectureDao.findById(id)
				.orElseThrow(() -> new NotFoundException(format("Could'nt find lecture by id = '%s'", id)));
	}

	@Override
	@Transactional
	public List<Lecture> findByDate(LocalDateTime dateTime) {
		log.debug("Find lectures by date {}", dateTime);
		return lectureDao.findByDateTime(dateTime)
				.orElseThrow(() -> new NotFoundException(format("Lectures not found by date '%s'", dateTime)));
	}

	@Override
	public boolean existsById(Integer id) {
		return lectureDao.existsById(id);
	}

	@Override
	@Transactional
	public List<Lecture> findAll() {
		log.debug("Find all lectures");
		return lectureDao.findAll();
	}

	@Override
	@Transactional
	public Integer count() {
		log.debug("Count lectures");
		return Integer.valueOf((int) lectureDao.count());
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		log.debug("Delete lecture by id {}", id);
		if (existsById(id)) {
			lectureDao.deleteById(id);
		} else {
			log.debug("Delete lecture by id {} FAILED", id);
			throw new NotFoundException(format("Could'nt delete, lecture by id '%s' doesn't exists", id));
		}
	}

	@Override
	public void delete(Lecture entity) {
		Integer id = entity.getId();

		deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		log.debug("Delete all lectures");
		lectureDao.deleteAll();
	}

	@Override
	public void saveOrUpdate(Lecture lecture) {

		Integer id = lecture.getId();

		if (id == null || id == 0) {
			save(lecture);
		} else {
			update(lecture);
		}
	}

	@Override
	@Transactional
	public void addGroupToLecture(Integer lectureId, Integer groupId) {
		log.debug("Add group id {} to lecture id {}", groupId, lectureId);
		if (existsById(lectureId)) {
			lectureDao.addGroupToLecture(lectureId, groupId);
		} else {
			log.debug("Add group id {} to lecture id {} FAILED", groupId, lectureId);
			throw new NotFoundException(
					format("Add group id '%s' to lecture id '%s' FAILED, lecture doesn't exists", groupId, lectureId));
		}
	}

	public void saveOrUpdate(CrmLecture lecture) {
		Integer id = lecture.getId();

		Lecture newLecture = new Lecture();
		newLecture.setId(id);
		newLecture.setCourse(courseService.findById(lecture.getCourseId()));
		newLecture.setAudience(audienceService.findById(lecture.getAudienceId()));
		newLecture.setTeacher(teacherService.findById(lecture.getTeacherId()));
		newLecture.setDateTime(LocalDateTime.parse(lecture.getDateTime()));

		if (id == 0 || id == null) {
			save(newLecture);
		} else {
			update(newLecture);
		}
	}
}
