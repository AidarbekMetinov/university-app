package ua.com.foxminded.service.impl;

import static java.lang.String.format;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.exception.NotFoundException;
import ua.com.foxminded.service.CourseService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

	private final CourseDao courseDao;

	@Override
	@Transactional
	public Course save(Course entity) {
		log.debug("Saving new course {}", entity);
		return courseDao.save(entity);
	}

	@Override
	@Transactional
	public void update(Course entity) {
		Integer id = entity.getId();

		log.debug("Updating course {}", entity);
		if (existsById(id)) {
			courseDao.update(entity);
		} else {
			log.debug("Updating course {} FAILED", entity);
			throw new NotFoundException(format("Could'nt update, course by id '%s' doesn't exists", id));
		}
	}

	@Override
	@Transactional
	public Course findById(Integer id) {
		log.debug("Find course by id {}", id);
		return courseDao.findById(id)
				.orElseThrow(() -> new NotFoundException(format("Could'nt get course by id = '%s'", id)));
	}

	@Override
	@Transactional
	public List<Course> findByTeacherId(Integer id) {
		log.debug("Find courses by teacher id");
		return courseDao.findByTeacherId(id)
				.orElseThrow(() -> new NotFoundException(format("Courses not found by teacher id '%s'", id)));
	}

	@Override
	@Transactional
	public boolean existsById(Integer id) {
		return courseDao.existsById(id);
	}

	@Override
	@Transactional
	public List<Course> findAll() {
		log.debug("Find all courses");
		return courseDao.findAll();
	}

	@Override
	@Transactional
	public Integer count() {
		log.debug("Count courses");
		return Integer.valueOf((int) courseDao.count());
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		log.debug("Delete course by id {}", id);
		if (existsById(id)) {
			courseDao.deleteById(id);
		} else {
			log.debug("Delete course by id {} FAILED", id);
			throw new NotFoundException(format("Could'nt delete, course by id '%s' doesn't exists", id));
		}
	}

	@Override
	public void delete(Course entity) {
		Integer id = entity.getId();

		deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		log.debug("Delete all courses");
		courseDao.deleteAll();
	}

	@Override
	@Transactional
	public List<Course> searchByName(String name) {
		log.debug("Find courses by name {}", name);
		return courseDao.findByNameLike(name)
				.orElseThrow(() -> new NotFoundException("Courses not found, table is empty"));
	}

	@Override
	public void saveOrUpdate(Course course) {

		Integer id = course.getId();

		if (id == null || id == 0) {
			save(course);
		} else {
			update(course);
		}
	}
}
