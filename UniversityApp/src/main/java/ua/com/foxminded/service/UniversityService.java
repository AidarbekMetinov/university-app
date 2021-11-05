package ua.com.foxminded.service;

import java.util.List;

public interface UniversityService<T, I> {
	T save(T entity);

	void update(T entity);

	T findById(I id);

	boolean existsById(I id);

	List<T> findAll();

	Integer count();

	void deleteById(I id);

	void delete(T entity);

	void deleteAll();

	void saveOrUpdate(T entity);
}