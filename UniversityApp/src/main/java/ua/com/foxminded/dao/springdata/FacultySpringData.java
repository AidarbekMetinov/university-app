package ua.com.foxminded.dao.springdata;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.dao.FacultyDao;
import ua.com.foxminded.entity.Faculty;

@Profile("springdata")
public interface FacultySpringData extends JpaRepository<Faculty, Integer>, FacultyDao {

	@Override
	default void update(Faculty entity) {
		save(entity);
	}
}
