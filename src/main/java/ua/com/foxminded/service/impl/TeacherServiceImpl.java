package ua.com.foxminded.service.impl;

import static java.lang.String.format;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.TeacherDao;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.exception.NotFoundException;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

	private final TeacherDao teacherDao;

	@Override
	@Transactional
	public Teacher save(Teacher entity) {
		log.debug("Saving new teacher {}", entity);
		return teacherDao.save(entity);
	}

	@Override
	@Transactional
	public void update(Teacher entity) {
		Integer id = entity.getId();

		log.debug("Updating teacher {}", entity);
		if (existsById(id)) {
			teacherDao.update(entity);
		} else {
			log.debug("Updating teacher {} FAILED", entity);
			throw new NotFoundException(format("Could'nt update, teacher by id '%s' doesn't exists", id));
		}
	}

	@Override
	@Transactional
	public Teacher findById(Integer id) {
		log.debug("Find teacher by id {}", id);
		return teacherDao.findById(id)
				.orElseThrow(() -> new NotFoundException(format("Could'nt find teacher by id = '%s'", id)));
	}

	@Override
	public boolean existsById(Integer id) {
		return teacherDao.existsById(id);
	}

	@Override
	@Transactional
	public List<Teacher> findAll() {
		log.debug("Find all teachers");
		return teacherDao.findAll();
	}

	@Override
	@Transactional
	public Integer count() {
		log.debug("Count teachers");
		return Integer.valueOf((int) teacherDao.count());
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		log.debug("Delete teacher by id {}", id);
		if (existsById(id)) {
			teacherDao.deleteById(id);
		} else {
			log.debug("Delete teacher by id {} FAILED", id);
			throw new NotFoundException(format("Could'nt delete, teacher by id '%s' doesn't exists", id));
		}
	}

	@Override
	@Transactional
	public void delete(Teacher entity) {
		Integer id = entity.getId();

		deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		log.debug("Delete all teachers");
		teacherDao.deleteAll();
	}

	@Override
	@Transactional
	public List<Teacher> searchByFirstName(String firstName) {
		log.debug("Find teachers by firstName {}", firstName);
		return teacherDao.findByFirstNameLike(firstName)
				.orElseThrow(() -> new NotFoundException("Teachers not found, table is empty"));
	}

	@Override
	@Transactional
	public List<Teacher> searchByLastName(String lastName) {
		log.debug("Find teachers by lastName {}", lastName);
		return teacherDao.findByLastNameLike(lastName)
				.orElseThrow(() -> new NotFoundException("Teachers not found, table is empty"));
	}

	@Override
	public void saveOrUpdate(Teacher teacher) {

		Integer id = teacher.getId();

		if (id == null || id == 0) {
			save(teacher);
		} else {
			update(teacher);
		}
	}

	@Override
	@Transactional
	public void deleteCourseFromTeacher(Integer teacherId, Integer courseId) {
		log.debug("Delete course by id {} from teacher by id {}", courseId, teacherId);
		if (existsById(teacherId)) {
			teacherDao.deleteCourseFromTeacher(teacherId, courseId);
		} else {
			log.debug("Delete course by id {} from teacher by id {} FAILED", courseId, teacherId);
			throw new NotFoundException(
					format("Could'nt delete course by id '%s' from teacher by id '%s'", courseId, teacherId));
		}
	}

	@Override
	@Transactional
	public void addCourseToTeacher(Integer teacherId, Integer courseId) {
		log.debug("Add course id {} to teacher id {}", courseId, teacherId);
		if (existsById(teacherId)) {
			teacherDao.addCourseToTeacher(teacherId, courseId);
		} else {
			log.debug("Add course id {} to teacher id {} FAILED", courseId, teacherId);
			throw new NotFoundException(
					format("Could'nt add course by id '%s' to teacher by id '%s'", courseId, teacherId));
		}
	}

	@Override
	@Transactional
	public List<Teacher> findByFaculty(Faculty faculty) {
		Integer id = faculty.getId();
		log.debug("Find teachers by faculty id");
		return teacherDao.findByFacultyId(id)
				.orElseThrow(() -> new NotFoundException(format("Teaachers not found by faculty id '%s'", id)));
	}

	@Override
	@Transactional
	public void deleteTeacherFromFaculty(Integer teacherId, Integer facultyId) {
		log.debug("Delete teacher by id {} from faculty", teacherId);
		if (existsById(teacherId)) {
			teacherDao.deleteTeacherFromFaculty(teacherId, facultyId);
		} else {
			log.debug("Delete teacher id {} from faculty id {} FAILED", teacherId, facultyId);
			throw new NotFoundException(
					format("Delete teacher id '%s' from faculty id '%s' FAILED", teacherId, facultyId));
		}
	}
}
