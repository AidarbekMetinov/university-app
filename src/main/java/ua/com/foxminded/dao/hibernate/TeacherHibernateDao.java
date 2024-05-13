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
import ua.com.foxminded.dao.TeacherDao;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Repository
@Profile("hibernate")
@RequiredArgsConstructor
public class TeacherHibernateDao implements TeacherDao {

	private final EntityManager entityManager;

	@Override
	public Teacher save(Teacher entity) {
		Session session = entityManager.unwrap(Session.class);
		String firstName = null;
		String lastName = null;
		try {
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			log.debug("Save teacher {} {}", firstName, lastName);
			Integer id = (Integer) session.save(entity);
			entity.setId(id);
			return entity;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt save teacher {} {}", firstName, lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt save teacher {} {}", firstName, lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void update(Teacher entity) {
		Session session = entityManager.unwrap(Session.class);
		String firstName = null;
		String lastName = null;
		try {
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			log.debug("Update teacher {} {}", firstName, lastName);
			session.update(entity);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt update teacher {} {}", firstName, lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt update teacher {} {}", firstName, lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<Teacher> findById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find teacher by id = {}", id);
			return ofNullable(session.get(Teacher.class, id));
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find teacher by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find teacher by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public boolean existsById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			boolean exists = session.createQuery("from Teacher where id = :id").setParameter("id", id).setMaxResults(1)
					.uniqueResult() != null;
			log.debug("Exists teacher by id = {}: {}", id, exists);
			return exists;
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt check teacher by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt check teacher by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public List<Teacher> findAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find all teachers");
			Query<Teacher> query = session.createQuery("from Teacher", Teacher.class);
			return query.getResultList();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find all teachers", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find all teachers", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public long count() {
		Session session = entityManager.unwrap(Session.class);
		log.debug("Count teachers");
		return session.createQuery("select count(*) from Teacher").getMaxResults();
	}

	@Override
	public void deleteById(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete teacher by id = {}", id);
			session.createQuery("update Lecture a set a.teacher=null where a.teacher.id = :id").setParameter("id", id)
					.executeUpdate();
			session.createNativeQuery("DELETE FROM public.course_teacher WHERE teacher_id = :id").setParameter("id", id)
					.executeUpdate();
			session.createNativeQuery("DELETE FROM public.faculty_teacher WHERE teacher_id = :id")
					.setParameter("id", id).executeUpdate();
			session.createQuery("delete from Teacher where id = :id").setParameter("id", id).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete teacher by id = {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete teacher by id = {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete all teachers");
			session.createQuery("update Lecture a set a.teacher=null").executeUpdate();
			session.createNativeQuery("DELETE FROM public.course_teacher").executeUpdate();
			session.createNativeQuery("DELETE FROM public.faculty_teacher").executeUpdate();
			session.createQuery("delete from Teacher").executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete all teachers", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete all teachers", e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Teacher>> findByFirstNameLike(String firstName) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find teachers by firstName {}", firstName);
			return ofNullable(session.createQuery("from Teacher where firstName = :firstName", Teacher.class)
					.setParameter("firstName", firstName).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find teachers by firstName {}", firstName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find teachers by firstName {}", firstName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Teacher>> findByLastNameLike(String lastName) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find teachers by lastName {}", lastName);
			return ofNullable(session.createQuery("from Teacher where lastName = :lastName", Teacher.class)
					.setParameter("lastName", lastName).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find teachers by lastName {}", lastName, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find teachers by lastName {}", lastName, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteCourseFromTeacher(Integer teacherId, Integer courseId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete course by id {} from teacher by id {}", courseId, teacherId);
			session.createNativeQuery(
					"DELETE FROM public.course_teacher WHERE course_id = :courseId AND teacher_id = :teacherId")
					.setParameter("teacherId", teacherId).setParameter("courseId", courseId).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete course by id {} from teacher by id {}", courseId, teacherId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete course by id {} from teacher by id {}", courseId, teacherId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void addCourseToTeacher(Integer teacherId, Integer courseId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Add course by id {} to teacher by id {}", courseId, teacherId);
			session.createNativeQuery(
					"INSERT INTO public.course_teacher (course_id, teacher_id) VALUES (:courseId, :teacherId)")
					.setParameter("teacherId", teacherId).setParameter("courseId", courseId).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt add course by id {} to teacher by id {}", courseId, teacherId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt add course by id {} to teacher by id {}", courseId, teacherId, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public Optional<List<Teacher>> findByFacultyId(Integer id) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Find teachers by faculty id {}", id);
			return ofNullable(
					session.createQuery("select t from Faculty f join f.teachers t where f.id = :id", Teacher.class)
							.setParameter("id", id).getResultList());
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt find teachers by faculty id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt find teachers by faculty id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteTeacherFromFaculty(Integer teacherId, Integer facultyId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			log.debug("Delete teacher id {} from faculty id {}", teacherId, facultyId);
			session.createNativeQuery(
					"DELETE FROM public.faculty_teacher WHERE teacher_id = :teacherId AND faculty_id = :facultyId")
					.setParameter("teacherId", teacherId).setParameter("facultyId", facultyId).executeUpdate();
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete teacher id {} from faculty id {}", teacherId, facultyId, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete teacher id {} from faculty id {}", teacherId, facultyId, e);
			throw new UnknownDataAccessException(e);
		}
	}
}
