package ua.com.foxminded.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.entity.Group;

public interface GroupDao extends UniversityDao<Group, Integer> {

	Optional<List<Group>> findByFacultyId(Integer facultyId);

	@Query("select g from Lecture l join l.groups g where l.id = ?1")
	Optional<List<Group>> findByLectureId(Integer lectureId);

	Optional<List<Group>> findByNameLike(String name);

	@Query(value = "DELETE FROM public.group_lecture WHERE group_id = ?1 AND lecture_id = ?2", nativeQuery = true)
	void deleteGroupFromLecture(Integer groupId, Integer lectureId);

	@Query("update Student s set s.groupId = ?1 where s.id = ?2")
	void addStudent(Integer groupId, Integer studentId);
}
