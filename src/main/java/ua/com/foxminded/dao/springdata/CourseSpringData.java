package ua.com.foxminded.dao.springdata;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.entity.Course;

@Profile("springdata")
public interface CourseSpringData extends JpaRepository<Course, Integer>, CourseDao {

	@Override
	default void update(Course entity) {
		save(entity);
	}
}
