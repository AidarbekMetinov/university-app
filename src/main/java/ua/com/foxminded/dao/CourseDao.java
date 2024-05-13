package ua.com.foxminded.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.entity.Course;

public interface CourseDao extends UniversityDao<Course, Integer> {

	Optional<List<Course>> findByNameLike(String name);

	@Query("select c from Teacher t join t.courses c where t.id = ?1")
	Optional<List<Course>> findByTeacherId(Integer id);
}
