package ua.com.foxminded.service.impl;

import static java.lang.String.format;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.exception.NotFoundException;
import ua.com.foxminded.service.StudentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final StudentDao studentDao;

	@Override
	@Transactional
	public Student save(Student entity) {
		log.debug("Saving new student {}", entity);
		return studentDao.save(entity);
	}

	@Override
	@Transactional
	public void update(Student entity) {
		Integer id = entity.getId();

		log.debug("Updating student {}", entity);
		if (existsById(id)) {
			studentDao.update(entity);
		} else {
			log.debug("Updating student {} FAILED", entity);
			throw new NotFoundException(format("Could'nt update, student by id '%s' doesn't exists", id));
		}
	}

	@Override
	@Transactional
	public Student findById(Integer id) {
		log.debug("Find student by id {}", id);
		return studentDao.findById(id)
				.orElseThrow(() -> new NotFoundException(format("Could'nt find student by id = '%s'", id)));
	}

	@Override
	@Transactional
	public List<Student> findByGroup(Group group) {
		log.debug("Find student by group {}", group);
		Integer groupId = group.getId();
		return studentDao.findByGroupId(groupId)
				.orElseThrow(() -> new NotFoundException(format("Students not found by group '%s'", group.getName())));
	}

	@Override
	@Transactional
	public boolean existsById(Integer id) {
		return studentDao.existsById(id);
	}

	@Override
	@Transactional
	public List<Student> findAll() {
		log.debug("Find all students");
		return studentDao.findAll();
	}

	@Override
	@Transactional
	public Integer count() {
		log.debug("Count students");
		return Integer.valueOf((int) studentDao.count());
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		log.debug("Delete student by id {}", id);
		if (existsById(id)) {
			studentDao.deleteById(id);
		} else {
			log.debug("Delete student by id {} FAILED", id);
			throw new NotFoundException(format("Could'nt delete, student by id '%s' doesn't exists", id));
		}
	}

	@Override
	public void delete(Student entity) {
		Integer id = entity.getId();

		deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		log.debug("Delete all students");
		studentDao.deleteAll();
	}

	@Override
	@Transactional
	public List<Student> searchByFirstName(String firstName) {
		log.debug("Find students by firstName {}", firstName);
		return studentDao.findByFirstNameLike(firstName)
				.orElseThrow(() -> new NotFoundException("Students not found, table is empty"));
	}

	@Override
	@Transactional
	public List<Student> searchByLastName(String lastName) {
		log.debug("Find students by lastName {}", lastName);
		return studentDao.findByLastNameLike(lastName)
				.orElseThrow(() -> new NotFoundException("Students not found, table is empty"));
	}

	@Override
	public void saveOrUpdate(Student student) {

		Integer id = student.getId();

		if (id == null || id == 0) {
			save(student);
		} else {
			update(student);
		}
	}

	@Override
	@Transactional
	public void deleteStudentFromGroup(Integer studentId) {
		log.debug("Delete student by id {} from group", studentId);
		if (existsById(studentId)) {
			studentDao.deleteStudentFromGroup(studentId);
		} else {
			log.debug("Delete student by id {} from group FAILED", studentId);
			throw new NotFoundException(format("Could'nt delete, student by id '%s' from group", studentId));
		}
	}
}
