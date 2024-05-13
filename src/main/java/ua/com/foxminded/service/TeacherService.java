package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Teacher;

public interface TeacherService extends UniversityService<Teacher, Integer> {

	List<Teacher> searchByFirstName(String firstName);

	List<Teacher> searchByLastName(String lastName);

	void deleteCourseFromTeacher(Integer teacherId, Integer courseId);

	void addCourseToTeacher(Integer teacherId, Integer courseId);

	List<Teacher> findByFaculty(Faculty faculty);

	void deleteTeacherFromFaculty(Integer teacherId, Integer facultyId);
}
