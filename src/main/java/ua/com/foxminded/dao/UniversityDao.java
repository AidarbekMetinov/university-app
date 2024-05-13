package ua.com.foxminded.dao;

import java.util.List;
import java.util.Optional;

public interface UniversityDao<T, I> {
	T save(T entity);

	void update(T entity);

	Optional<T> findById(I id);

	boolean existsById(I id);

	List<T> findAll();

	long count();

	void deleteById(I id);

	void deleteAll();
}