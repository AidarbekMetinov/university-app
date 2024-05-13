package ua.com.foxminded.dao.hibernate;

import static java.util.Optional.ofNullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import ua.com.foxminded.dao.LectureDao;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Repository
@Profile("hibernate")
@RequiredArgsConstructor
public class LectureHibernateDao implements LectureDao {

	private final EntityManager entityManager;

	@Override
	public Lecture save(Lecture entity) {
		Session session = entityManager.unwrap(Session.class);
		try {
			String dateTime = entity.getDateTime().toString();
			log.debug("Save lecture starts = {}", dateTime);
			Integer id = (Integer) session.save(entity);
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save lecture", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save lecture", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Lecture entity) {
		Session session = entityManager.unwrap(Session.class);
		Integer id = null;
		try {
			String dateTime = entity.getDateTime().toString();
			id = entity.getId();
			log.debug("Save lecture starts = {}", dateTime);
			session.update(entity);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update lecture by id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update lecture by id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Lecture> findById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find lecture by id = {}", id);
			return ofNullable(session.get(Lecture.class, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find lecture by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find lecture by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			boolean exists = session.createQuery("from Lecture where id = :id").setParameter("id", id).setMaxResults(1)
					.uniqueResult() != null;
			log.debug("Exists lecture by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check lecture by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check lecture by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Lecture> findAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find all lectures");
			Query<Lecture> query = session.createQuery("from Lecture", Lecture.class);
			return query.getResultList();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all lectures", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all lectures", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		Session session = entityManager.unwrap(Session.class);
		log.debug("Count lectures");
		return session.createQuery("select count(*) from Lecture").getMaxResults();
	}

	@Override
	public void deleteById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete lecture by id = {}", id);
			session.createNativeQuery("DELETE FROM public.group_lecture WHERE lecture_id = :id").setParameter("id", id)
					.executeUpdate();
			session.createQuery("delete from Lecture where id = :id").setParameter("id", id).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete lecture by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete lecture by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete all lectures");
			session.createNativeQuery("DELETE FROM public.group_lecture").executeUpdate();
			session.createQuery("delete from Lecture").executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all lectures", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all lectures", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Lecture>> findByDateTime(LocalDateTime dateTime) {
		Session session = entityManager.unwrap(Session.class);
		String dateString = null;
		try {
			dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
			return ofNullable(session.createQuery("from Lecture where dateTime = :dateTime", Lecture.class)
					.setParameter("dateTime", dateTime).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find lectures by date = {}", dateString, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find lectures by date = {}", dateString, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addGroupToLecture(Integer lectureId, Integer groupId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Add group by id {} to lecture by id {}", groupId, lectureId);
			session.createNativeQuery(
					"INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (:lectureId, :groupId)")
					.setParameter("groupId", groupId).setParameter("lectureId", lectureId).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add group by id {} to lecture by id {}", groupId, lectureId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add group by id {} to lecture by id {}", groupId, lectureId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}
