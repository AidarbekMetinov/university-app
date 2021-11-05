package ua.com.foxminded.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.entity.Lecture;

public interface LectureDao extends UniversityDao<Lecture, Integer> {

	Optional<List<Lecture>> findByDateTime(LocalDateTime dateTime);

	@Query(value = "INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (?2, ?1)", nativeQuery = true)
	void addGroupToLecture(Integer lectureId, Integer groupId);
}
