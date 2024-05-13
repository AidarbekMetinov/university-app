package ua.com.foxminded.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.entity.Teacher;

public interface TeacherDao extends UniversityDao<Teacher, Integer> {

	Optional<List<Teacher>> findByFirstNameLike(String firstName);

	Optional<List<Teacher>> findByLastNameLike(String lastName);

	@Query(value = "DELETE FROM public.course_teacher WHERE course_id = ?2 AND teacher_id = ?1", nativeQuery = true)
	void deleteCourseFromTeacher(Integer teacherId, Integer courseId);

	@Query(value = "INSERT INTO public.course_teacher (course_id, teacher_id) VALUES (?2, ?1)", nativeQuery = true)
	void addCourseToTeacher(Integer teacherId, Integer courseId);

	@Query("select t from Faculty f join f.teachers t where f.id = ?1")
	Optional<List<Teacher>> findByFacultyId(Integer id);

	@Query(value = "DELETE FROM public.faculty_teacher WHERE teacher_id = ?1 AND faculty_id = ?2", nativeQuery = true)
	void deleteTeacherFromFaculty(Integer teacherId, Integer facultyId);
}
