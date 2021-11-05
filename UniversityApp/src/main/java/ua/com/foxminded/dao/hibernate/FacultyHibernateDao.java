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
import ua.com.foxminded.dao.FacultyDao;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Repository
@Profile("hibernate")
@RequiredArgsConstructor
public class FacultyHibernateDao implements FacultyDao {

	private final EntityManager entityManager;

	@Override
	public Faculty save(Faculty entity) {
		Session session = entityManager.unwrap(Session.class);
		String name = null;
		try {
			name = entity.getName();
			log.debug("Save faculty {}", name);
			Integer id = (Integer) session.save(entity);
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save faculty {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save faculty {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Faculty entity) {
		Session session = entityManager.unwrap(Session.class);
		String name = null;
		try {
			name = entity.getName();
			log.debug("Update faculty {}", name);
			session.update(entity);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update faculty {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update faculty {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Faculty> findById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find faculty by id = {}", id);
			return ofNullable(session.get(Faculty.class, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find faculty by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find faculty by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			boolean exists = session.createQuery("from Faculty where id = :id").setParameter("id", id).setMaxResults(1)
					.uniqueResult() != null;
			log.debug("Exists faculty by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check faculty by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check faculty by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Faculty> findAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find all faculties");
			Query<Faculty> query = session.createQuery("from Faculty", Faculty.class);
			return query.getResultList();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all faculties", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all faculties", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		Session session = entityManager.unwrap(Session.class);
		log.debug("Count faculties");
		return session.createQuery("select count(*) from Faculty").getMaxResults();
	}

	@Override
	public void deleteById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete faculty by id = {}", id);
			session.createNativeQuery("DELETE FROM public.faculty_teacher WHERE faculty_id = :id")
					.setParameter("id", id).executeUpdate();
			session.createQuery("update Group a set a.facultyId=null where a.facultyId = :id").setParameter("id", id)
					.executeUpdate();
			session.createQuery("delete from Faculty where id = :id").setParameter("id", id).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete faculty by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete faculty by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete all faculties");
			session.createNativeQuery("DELETE FROM public.faculty_teacher").executeUpdate();
			session.createQuery("update Group a set a.facultyId=null").executeUpdate();
			session.createQuery("delete from Faculty").executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all faculties", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all faculties", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Faculty>> findByNameLike(String name) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find faculties by name {}", name);
			return ofNullable(session.createQuery("from Faculty where name = :name", Faculty.class)
					.setParameter("name", name).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find faculties by name {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find faculties by name {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addGroup(Integer facultyId, Integer groupId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Add group id {} to faculty id {}", groupId, facultyId);
			session.createNativeQuery(
					"UPDATE public.ugroup SET (faculty_id) = (SELECT id FROM faculty WHERE faculty.id = :facultyId) WHERE ugroup.id = :groupId")
					.setParameter("facultyId", facultyId).setParameter("groupId", groupId).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add group id {} to faculty id {}", groupId, facultyId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add group id {} to faculty id {}", groupId, facultyId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addTeacher(Integer facultyId, Integer teacherId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Add teacher id {} to faculty id {}", teacherId, facultyId);
			session.createNativeQuery(
					"INSERT INTO public.faculty_teacher (faculty_id, teacher_id) VALUES (:facultyId, :teacherId)")
					.setParameter("facultyId", facultyId).setParameter("teacherId", teacherId).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add teacher id {} to faculty id {}", teacherId, facultyId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add teacher id {} to faculty id {}", teacherId, facultyId, e);
			throw new UnknownDataAccessException(e);
		}
	}

}
