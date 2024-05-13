package ua.com.foxminded.service.impl;

import static java.lang.String.format;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.AudienceDao;
import ua.com.foxminded.entity.Audience;
import ua.com.foxminded.exception.NotFoundException;
import ua.com.foxminded.service.AudienceService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudienceServiceImpl implements AudienceService {

	private final AudienceDao audienceDao;

	@Override
	@Transactional
	public Audience save(Audience entity) {
		Integer id = entity.getId();

		log.debug("Saving new audience {}", entity);
		if (!existsById(id)) {
			return audienceDao.save(entity);
		} else {
			log.debug("Saving new audience {} FAILED", entity);
			throw new IllegalArgumentException(format("Audience id '%s' already exists", id));
		}
	}

	@Override
	@Transactional
	public void update(Audience entity) {

		Integer id = entity.getId();

		log.debug("Update audience {}", entity);
		if (existsById(id)) {
			audienceDao.update(entity);
		} else {
			log.debug("Update audience {} FAILED", entity);
			throw new NotFoundException(format("Audience id '%s' doesn't exists", id));
		}
	}

	@Override
	@Transactional
	public Audience findById(Integer id) {
		log.debug("Find audience by id {}", id);
		return audienceDao.findById(id)
				.orElseThrow(() -> new NotFoundException(format("Could'nt find audience id '%s'", id)));
	}

	@Override
	@Transactional
	public boolean existsById(Integer id) {
		return audienceDao.existsById(id);
	}

	@Override
	@Transactional
	public List<Audience> findAll() {
		log.debug("Find all audiences");
		return audienceDao.findAll();
	}

	@Override
	@Transactional
	public Integer count() {
		log.debug("Count audiences");
		return Integer.valueOf((int) audienceDao.count());
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		log.debug("Delete audience by id {}", id);
		if (existsById(id)) {
			audienceDao.deleteById(id);
		} else {
			log.debug("Delete audience by id {} FAILED", id);
			throw new NotFoundException(format("Could'nt delete, audience id '%s' doesn't exists", id));
		}
	}

	@Override
	public void delete(Audience entity) {
		Integer id = entity.getId();

		deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		log.debug("Delete all audiences");
		audienceDao.deleteAll();
	}

	@Override
	public void saveOrUpdate(Audience audience) {

		Integer id = audience.getId();

		if (id == null || id == 0) {
			save(audience);
		} else {
			update(audience);
		}
	}
}