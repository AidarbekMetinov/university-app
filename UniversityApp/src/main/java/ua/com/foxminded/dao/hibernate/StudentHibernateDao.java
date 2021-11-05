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
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Repository
@Profile("hibernate")
@RequiredArgsConstructor
public class StudentHibernateDao implements StudentDao {

	private final EntityManager entityManager;

	@Override
	public Student save(Student entity) {
		Session session = entityManager.unwrap(Session.class);
		String firstName = null;
		String lastName = null;
		try {
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			log.debug("Save student {} {}", firstName, lastName);
			Integer id = (Integer) session.save(entity);
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save student {} {}", firstName, lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save student {} {}", firstName, lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Student entity) {
		Session session = entityManager.unwrap(Session.class);
		String firstName = null;
		String lastName = null;
		try {
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			log.debug("Update student {} {}", firstName, lastName);
			session.update(entity);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update student {} {}", firstName, lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update student {} {}", firstName, lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Student> findById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find student by id = {}", id);
			return ofNullable(session.get(Student.class, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find student by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find student by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			boolean exists = session.createQuery("from Student where id = :id").setParameter("id", id).setMaxResults(1)
					.uniqueResult() != null;
			log.debug("Exists student by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check student by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check student by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Student> findAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find all students");
			Query<Student> query = session.createQuery("from Student", Student.class);
			return query.getResultList();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all students", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all students", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		Session session = entityManager.unwrap(Session.class);
		log.debug("Count students");
		return session.createQuery("select count(*) from Student").getMaxResults();

	}

	@Override
	public void deleteById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete student by id = {}", id);
			session.createQuery("delete from Student where id = :id").setParameter("id", id).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete student by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete student by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete all students");
			session.createQuery("delete from Student").executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all students", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all students", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Student>> findByGroupId(Integer groupId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find student by groupId = {}", groupId);
			return ofNullable(session.createQuery("from Student where groupId = :groupId", Student.class)
					.setParameter("groupId", groupId).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find student by groupId = {}", groupId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find student by groupId = {}", groupId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Student>> findByFirstNameLike(String firstName) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find students by firstName {}", firstName);
			return ofNullable(session.createQuery("from Student where firstName = :firstName", Student.class)
					.setParameter("firstName", firstName).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find students by firstName {}", firstName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find students by firstName {}", firstName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Student>> findByLastNameLike(String lastName) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find students by lastName {}", lastName);
			return ofNullable(session.createQuery("from Student where lastName = :lastName", Student.class)
					.setParameter("lastName", lastName).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find students by lastName {}", lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find students by lastName {}", lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteStudentFromGroup(Integer studentId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete student id {} from group", studentId);
			session.createQuery("update Student set groupId = null where id = :id").setParameter("id", studentId)
					.executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete student id {} from group", studentId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete student id {} from group", studentId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}
