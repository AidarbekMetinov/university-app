package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;

public interface StudentService extends UniversityService<Student, Integer> {

	List<Student> findByGroup(Group group);

	List<Student> searchByFirstName(String firstName);

	List<Student> searchByLastName(String lastName);

	void deleteStudentFromGroup(Integer studentId);
}
