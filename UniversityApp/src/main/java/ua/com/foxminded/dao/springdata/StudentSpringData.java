package ua.com.foxminded.dao.springdata;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.entity.Student;

@Profile("springdata")
public interface StudentSpringData extends JpaRepository<Student, Integer>, StudentDao {

	@Override
	default void update(Student entity) {
		save(entity);
	}
}
