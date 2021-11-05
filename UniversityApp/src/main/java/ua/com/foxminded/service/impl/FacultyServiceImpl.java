package ua.com.foxminded.service.impl;

import static java.lang.String.format;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.FacultyDao;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.NotFoundException;
import ua.com.foxminded.service.FacultyService;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

	private final FacultyDao facultyDao;

	@Override
	@Transactional
	public Faculty save(Faculty entity) {
		log.debug("Saving new faculty {}", entity);
		return facultyDao.save(entity);
	}

	@Override
	@Transactional
	public void update(Faculty entity) {
		Integer id = entity.getId();

		log.debug("Updating faculty {}", entity);
		if (existsById(id)) {
			facultyDao.update(entity);
		} else {
			log.debug("Updating faculty {} FAILED", entity);
			throw new NotFoundException(format("Could'nt update, faculty by id '%s' doestn't exists", id));
		}
	}

	@Override
	@Transactional
	public Faculty findById(Integer id) {
		log.debug("Find faculty by id {}", id);
		return facultyDao.findById(id)
				.orElseThrow(() -> new NotFoundException(format("Could'nt find faculty by id = '%s'", id)));
	}

	@Override
	@Transactional
	public boolean existsById(Integer id) {
		return facultyDao.existsById(id);
	}

	@Override
	@Transactional
	public List<Faculty> findAll() {
		log.debug("Find all faculties");
		return facultyDao.findAll();
	}

	@Override
	@Transactional
	public Integer count() {
		log.debug("Count faculties");
		return Integer.valueOf((int) facultyDao.count());
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		log.debug("Delete faculty by id {}", id);
		if (existsById(id)) {
			facultyDao.deleteById(id);
		} else {
			log.debug("Delete faculty by id {} FAILED", id);
			throw new NotFoundException(format("Could'nt delete, faculty by id '%s' doesn't exists", id));
		}
	}

	@Override
	public void delete(Faculty entity) {
		Integer id = entity.getId();

		deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		log.debug("Delete all faculties");
		facultyDao.deleteAll();
	}

	@Override
	@Transactional
	public List<Faculty> searchByName(String name) {
		log.debug("Find faculties by name {}", name);
		return facultyDao.findByNameLike(name)
				.orElseThrow(() -> new NotFoundException("Faculties not found, table is empty"));
	}

	@Override
	public void saveOrUpdate(Faculty faculty) {

		Integer id = faculty.getId();

		if (id == null || id == 0) {
			save(faculty);
		} else {
			update(faculty);
		}
	}

	@Override
	@Transactional
	public void addGroup(Integer facultyId, Integer groupId) {
		log.debug("Add group id {} to faculty id {}", groupId, facultyId);
		if (existsById(facultyId)) {
			facultyDao.addGroup(facultyId, groupId);
		} else {
			log.debug("Add group id {} to faculty id {} FAILED", groupId, facultyId);
			throw new NotFoundException(
					format("Add group id '%s' to faculty id '%s' FAILED, faculty doesn't exists", groupId, facultyId));
		}
	}

	@Override
	@Transactional
	public void addTeacher(Integer facultyId, Integer teacherId) {
		log.debug("Add teacher id {} to faculty id {}", teacherId, facultyId);
		if (existsById(facultyId)) {
			facultyDao.addTeacher(facultyId, teacherId);
		} else {
			log.debug("Add teacher id {} to faculty id {} FAILED", teacherId, facultyId);
			throw new NotFoundException(format("Add teacher id '%s' to faculty id '%s' FAILED, faculty doesn't exists",
					teacherId, facultyId));
		}
	}
}
