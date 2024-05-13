package ua.com.foxminded.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.entity.Faculty;

public interface FacultyDao extends UniversityDao<Faculty, Integer> {

	Optional<List<Faculty>> findByNameLike(String name);

	@Query(value = "UPDATE public.ugroup SET (faculty_id) = (SELECT id FROM faculty WHERE faculty.id = ?1) WHERE ugroup.id = ?2", nativeQuery = true)
	void addGroup(Integer facultyId, Integer groupId);

	@Query(value = "INSERT INTO public.faculty_teacher (faculty_id, teacher_id) VALUES (?1, ?2)", nativeQuery = true)
	void addTeacher(Integer facultyId, Integer teacherId);
}
