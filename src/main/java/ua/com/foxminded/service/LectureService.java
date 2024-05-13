package ua.com.foxminded.service;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.crm.CrmLecture;
import ua.com.foxminded.entity.Lecture;

public interface LectureService extends UniversityService<Lecture, Integer> {

	List<Lecture> findByDate(LocalDateTime dateTime);

	void addGroupToLecture(Integer lectureId, Integer groupId);

	void saveOrUpdate(CrmLecture lecture);
}
