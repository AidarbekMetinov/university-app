package ua.com.foxminded.dao.hibernate;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Repository
@Profile("hibernate")
@RequiredArgsConstructor
public class CourseHibernateDao implements CourseDao {

	private final EntityManager entityManager;

	@Override
	public Course save(Course entity) {
		Session session = entityManager.unwrap(Session.class);
		String name = null;
		try {
			name = entity.getName();
			log.debug("Save course {}", name);
			Integer id = (Integer) session.save(entity);
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save course {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save course {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Course entity) {
		Session session = entityManager.unwrap(Session.class);
		String name = null;
		try {
			name = entity.getName();
			log.debug("Update course {}", name);
			session.update(entity);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update course {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update course {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Course> findById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find course by id = {}", id);
			return ofNullable(session.get(Course.class, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find course by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find course by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			boolean exists = session.createQuery("from Course where id = :id").setParameter("id", id).setMaxResults(1)
					.uniqueResult() != null;
			log.debug("Exists course by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check course by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check course by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Course> findAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find all courses");
			Query<Course> query = session.createQuery("from Course", Course.class);
			return query.getResultList();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all courses", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all courses", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		Session session = entityManager.unwrap(Session.class);
		log.debug("Count courses");
		return session.createQuery("select count(*) from Course").getMaxResults();
	}

	@Override
	public void deleteById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete course by id = {}", id);
			session.createQuery("update Lecture a set a.course=null where a.course.id = :id").setParameter("id", id)
					.executeUpdate();
			session.createQuery("delete from Course where id = :id").setParameter("id", id).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete course by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete course by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	public void delete(Course entity) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete {}", entity);
			session.createQuery("update Lecture a set a.course=null where a.course.id = :id")
					.setParameter("id", entity.getId()).executeUpdate();
			session.delete(entity);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete {}", entity, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete {}", entity, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete all courses");
			session.createQuery("update Lecture set audience=null").executeUpdate();
			session.createQuery("delete from Audience").executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all courses", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all courses", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Course>> findByNameLike(String name) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find courses by name {}", name);
			return ofNullable(session.createQuery("from Course where name = :name", Course.class)
					.setParameter("name", name).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find courses by name {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find courses by name {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Course>> findByTeacherId(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find courses by teacher id {}", id);
			return ofNullable(
					session.createQuery("select c from Teacher t join t.courses c where t.id = :id", Course.class)
							.setParameter("id", id).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find courses by teacher id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find courses by teacher id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}
}
