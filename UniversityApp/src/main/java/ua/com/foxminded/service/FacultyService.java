package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.entity.Faculty;

public interface FacultyService extends UniversityService<Faculty, Integer> {

	void saveOrUpdate(Faculty faculty);

	List<Faculty> searchByName(String name);

	void addGroup(Integer facultyId, Integer groupId);

	void addTeacher(Integer facultyId, Integer teacherId);
}
