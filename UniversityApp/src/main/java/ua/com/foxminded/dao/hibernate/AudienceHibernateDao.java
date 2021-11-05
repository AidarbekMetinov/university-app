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
import ua.com.foxminded.dao.AudienceDao;
import ua.com.foxminded.entity.Audience;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Repository
@Profile("hibernate")
@RequiredArgsConstructor
public class AudienceHibernateDao implements AudienceDao {

	private final EntityManager entityManager;

	@Override
	public Audience save(Audience entity) {
		Session session = entityManager.unwrap(Session.class);
		Integer audienceN = null;
		try {
			audienceN = entity.getAudienceNumber();
			log.debug("Save audience N{}", audienceN);
			Integer id = (Integer) session.save(entity);
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save audience N{}", audienceN, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save audience N{}", audienceN, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Audience entity) {
		Session session = entityManager.unwrap(Session.class);
		Integer id = null;
		try {
			id = entity.getId();
			log.debug("Update audience id {}", id);
			session.update(entity);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update audience {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update audience {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Audience> findById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find audience id {}", id);
			return ofNullable(session.get(Audience.class, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find audience id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find audience id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			boolean exists = session.createQuery("from Audience where id = " + id).setMaxResults(1)
					.uniqueResult() != null;
			log.debug("Exists audience id {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check audience id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check audience id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Audience> findAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find all audiences");
			Query<Audience> query = session.createQuery("from Audience", Audience.class);
			return query.getResultList();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find audiences", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find audiences", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		Session session = entityManager.unwrap(Session.class);
		log.debug("Count audiences");
		return session.createQuery("select count(*) from Audience").getMaxResults();
	}

	@Override
	public void deleteById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete audience id {}", id);
			session.createQuery("update Lecture a set a.audience=null where a.audience.id = :id").setParameter("id", id)
					.executeUpdate();
			session.createQuery("delete from Audience where id = :id").setParameter("id", id).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	public void delete(Audience entity) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete {}", entity);
			session.createQuery("update Lecture a set a.audience=null where a.audience.id = :id")
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
			log.debug("Delete audiences");
			session.createQuery("update Lecture set audience=null").executeUpdate();
			session.createQuery("delete from Audience").executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete audiences", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete audiences", e);
			throw new UnknownDataAccessException(e);
		}
	}
}
