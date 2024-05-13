package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.entity.Course;

public interface CourseService extends UniversityService<Course, Integer> {

	List<Course> findByTeacherId(Integer id);

	List<Course> searchByName(String name);
}
