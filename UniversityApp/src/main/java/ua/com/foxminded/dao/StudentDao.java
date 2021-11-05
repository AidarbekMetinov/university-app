package ua.com.foxminded.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.entity.Student;

public interface StudentDao extends UniversityDao<Student, Integer> {

	Optional<List<Student>> findByGroupId(Integer groupId);

	Optional<List<Student>> findByFirstNameLike(String firstName);

	Optional<List<Student>> findByLastNameLike(String lastName);

	@Query("update Student set groupId = null where id = ?1")
	void deleteStudentFromGroup(Integer studentId);
}
