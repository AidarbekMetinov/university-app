package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;

public interface GroupService extends UniversityService<Group, Integer> {

	List<Group> findByFaculty(Faculty faculty);

	List<Group> findByLecture(Lecture lecture);

	List<Group> searchByName(String name);

	void deleteGroupFromFaculty(Integer groupId);

	void deleteGroupFromLecture(Integer groupId, Integer lectureId);

	void addStudent(Integer groupId, Integer studentId);
}
