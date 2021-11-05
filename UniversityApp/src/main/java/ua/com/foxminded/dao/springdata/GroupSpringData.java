package ua.com.foxminded.dao.springdata;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.entity.Group;

@Profile("springdata")
public interface GroupSpringData extends JpaRepository<Group, Integer>, GroupDao {

	@Override
	default void update(Group entity) {
		save(entity);
	}
}
