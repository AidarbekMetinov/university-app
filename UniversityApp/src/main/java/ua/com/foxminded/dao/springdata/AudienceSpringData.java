package ua.com.foxminded.dao.springdata;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.dao.AudienceDao;
import ua.com.foxminded.entity.Audience;

@Profile("springdata")
public interface AudienceSpringData extends JpaRepository<Audience, Integer>, AudienceDao {

	@Override
	default void update(Audience entity) {
		save(entity);
	}
}
