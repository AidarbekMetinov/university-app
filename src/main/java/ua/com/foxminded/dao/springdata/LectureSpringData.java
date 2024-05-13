package ua.com.foxminded.dao.springdata;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.dao.LectureDao;
import ua.com.foxminded.entity.Lecture;

@Profile("springdata")
public interface LectureSpringData extends JpaRepository<Lecture, Integer>, LectureDao {

	@Override
	default void update(Lecture entity) {
		save(entity);
	}
}
