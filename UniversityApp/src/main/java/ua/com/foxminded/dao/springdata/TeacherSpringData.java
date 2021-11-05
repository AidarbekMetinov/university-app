package ua.com.foxminded.dao.springdata;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.dao.TeacherDao;
import ua.com.foxminded.entity.Teacher;

@Profile("springdata")
public interface TeacherSpringData extends JpaRepository<Teacher, Integer>, TeacherDao {

	@Override
	default void update(Teacher entity) {
		save(entity);
	}
}
