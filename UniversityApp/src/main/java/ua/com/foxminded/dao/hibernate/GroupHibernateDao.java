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
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Repository
@Profile("hibernate")
@RequiredArgsConstructor
public class GroupHibernateDao implements GroupDao {

	private final EntityManager entityManager;

	@Override
	public Group save(Group entity) {
		Session session = entityManager.unwrap(Session.class);
		String name = null;
		try {
			name = entity.getName();
			log.debug("Save group {}", name);
			Integer id = (Integer) session.save(entity);
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save group {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save group {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Group entity) {
		Session session = entityManager.unwrap(Session.class);
		String name = null;
		try {
			name = entity.getName();
			log.debug("Update group {}", name);
			session.update(entity);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update group {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update group {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Group> findById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find group by id = {}", id);
			return ofNullable(session.get(Group.class, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find group by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find group by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			boolean exists = session.createQuery("from Group where id = :id").setParameter("id", id).setMaxResults(1)
					.uniqueResult() != null;
			log.debug("Exists group by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check group by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check group by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Group> findAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find all groups");
			Query<Group> query = session.createQuery("from Group", Group.class);
			return query.getResultList();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all groups", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all groups", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		Session session = entityManager.unwrap(Session.class);
		log.debug("Count groups");
		return session.createQuery("select count(*) from Group").getMaxResults();
	}

	@Override
	public void deleteById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete group by id = {}", id);
			session.createNativeQuery("DELETE FROM public.group_lecture WHERE group_id = :id").setParameter("id", id)
					.executeUpdate();
			session.createQuery("update Student a set a.groupId=null where a.groupId = :id").setParameter("id", id)
					.executeUpdate();
			session.createQuery("delete from Group where id = :id").setParameter("id", id).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete group by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete group by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete all groups");
			session.createNativeQuery("DELETE FROM public.group_lecture").executeUpdate();
			session.createQuery("update Student a set a.groupId=null").executeUpdate();
			session.createQuery("delete from Group").executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all groups", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all groups", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Group>> findByFacultyId(Integer facultyId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find group by facultyId = {}", facultyId);
			return ofNullable(session.createQuery("from Group where facultyId = :facultyId", Group.class)
					.setParameter("facultyId", facultyId).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find group by facultyId = {}", facultyId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find group by facultyId = {}", facultyId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Group>> findByLectureId(Integer lectureId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find group by lectureId = {}", lectureId);
			return ofNullable(
					session.createQuery("select g from Lecture l join l.groups g where l.id = :id", Group.class)
							.setParameter("id", lectureId).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find group by lectureId = {}", lectureId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find group by lectureId = {}", lectureId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Group>> findByNameLike(String name) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find groups by name {}", name);
			return ofNullable(session.createQuery("from Group where name = :name", Group.class)
					.setParameter("name", name).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find groups by name {}", name, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find groups by name {}", name, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteGroupFromLecture(Integer groupId, Integer lectureId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete group id {} from lecture id {}", groupId, lectureId);
			session.createNativeQuery(
					"DELETE FROM public.group_lecture WHERE group_id = :groupId AND lecture_id = :lectureId")
					.setParameter("groupId", groupId).setParameter("lectureId", lectureId).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete group id {} from lecture id {}", groupId, lectureId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete group id {} from lecture id {}", groupId, lectureId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addStudent(Integer groupId, Integer studentId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Add student id {} to group id {}", studentId, groupId);
			session.createQuery("update Student s set s.groupId = :groupId where s.id = :studentId")
					.setParameter("groupId", groupId).setParameter("studentId", studentId).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add student id {} to group id {}", studentId, groupId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add student id {} to group id {}", studentId, groupId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}
