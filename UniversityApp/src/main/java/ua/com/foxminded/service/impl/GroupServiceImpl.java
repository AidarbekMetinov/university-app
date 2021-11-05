package ua.com.foxminded.service.impl;

import static java.lang.String.format;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.exception.NotFoundException;
import ua.com.foxminded.service.GroupService;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupDao groupDao;

	@Override
	@Transactional
	public Group save(Group entity) {
		log.debug("Saving new group {}", entity);
		return groupDao.save(entity);
	}

	@Override
	@Transactional
	public void update(Group entity) {
		Integer id = entity.getId();

		log.debug("Updating group {}", entity);
		if (existsById(id)) {
			groupDao.update(entity);
		} else {
			log.debug("Updating group {} FAILED", entity);
			throw new NotFoundException(format("Could'nt update, group by id '%s' doesn't exists", id));
		}
	}

	@Override
	@Transactional
	public Group findById(Integer id) {
		log.debug("Find group by id {}", id);
		return groupDao.findById(id)
				.orElseThrow(() -> new NotFoundException(format("Could'nt find group by id = '%s'", id)));
	}

	@Override
	@Transactional
	public List<Group> findByFaculty(Faculty faculty) {
		log.debug("Find groups by faculty {}", faculty);
		Integer facultyId = faculty.getId();
		return groupDao.findByFacultyId(facultyId).orElseThrow(
				() -> new NotFoundException(format("Groups not found by faculty '%s'", faculty.getName())));
	}

	@Override
	@Transactional
	public List<Group> findByLecture(Lecture lecture) {
		log.debug("Find groups by lecture {}", lecture);
		Integer lectureId = lecture.getId();
		return groupDao.findByLectureId(lectureId)
				.orElseThrow(() -> new NotFoundException(format("Groups not found by lecture id '%s'", lectureId)));
	}

	@Override
	@Transactional
	public boolean existsById(Integer id) {
		return groupDao.existsById(id);
	}

	@Override
	@Transactional
	public List<Group> findAll() {
		log.debug("Find all  groups");
		return groupDao.findAll();
	}

	@Override
	@Transactional
	public Integer count() {
		log.debug("Count  groups");
		return Integer.valueOf((int) groupDao.count());
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		log.debug("Delete  group by id {}", id);
		if (existsById(id)) {
			groupDao.deleteById(id);
		} else {
			log.debug("Delete  group by id {} FAILED", id);
			throw new NotFoundException(format("Could'nt delete, group by id '%s' doesn't exists", id));
		}
	}

	@Override
	public void delete(Group entity) {
		Integer id = entity.getId();

		deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		log.debug("Delete all  groups");
		groupDao.deleteAll();
	}

	@Override
	@Transactional
	public List<Group> searchByName(String name) {
		log.debug("Find groups by name {}", name);
		return groupDao.findByNameLike(name)
				.orElseThrow(() -> new NotFoundException(format("Groups not found by name '%s'", name)));
	}

	@Override
	public void saveOrUpdate(Group group) {

		Integer id = group.getId();

		if (id == null || id == 0) {
			save(group);
		} else {
			update(group);
		}
	}

	@Override
	@Transactional
	public void deleteGroupFromFaculty(Integer groupId) {
		log.debug("Delete group by id {} from faculty", groupId);
		Group group = findById(groupId);
		group.setFacultyId(null);
		update(group);
	}

	@Override
	@Transactional
	public void deleteGroupFromLecture(Integer groupId, Integer lectureId) {
		log.debug("Delete  group by id {} from lecture id {}", groupId, lectureId);
		if (existsById(groupId)) {
			groupDao.deleteGroupFromLecture(groupId, lectureId);
		} else {
			log.debug("Delete group by id {} from lecture id {} FAILED", groupId, lectureId);
			throw new NotFoundException(format("Could'nt delete, group by id '%s' from lecture id '%s' doesn't exists",
					groupId, lectureId));
		}
	}

	@Override
	@Transactional
	public void addStudent(Integer groupId, Integer studentId) {
		log.debug("Add student id {} to group id {}", studentId, groupId);
		if (existsById(groupId)) {
			groupDao.addStudent(groupId, studentId);
		} else {
			log.debug("Add student id {} to group id {} FAILED", studentId, groupId);
			throw new NotFoundException(
					format("Add student id '%s' to group id '%s' FAILED, group doesn't exists", studentId, groupId));
		}
	}
}
