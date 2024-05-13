package ua.com.foxminded.dao.springjdbc;

import static java.util.Optional.ofNullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.dao.AudienceDao;
import ua.com.foxminded.entity.Audience;
import ua.com.foxminded.exception.ConnectionException;
import ua.com.foxminded.exception.UnknownDataAccessException;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class AudienceSpringJdbcDao implements AudienceDao {

	private static final String SAVE = "INSERT INTO public.audience (audience_n, seat, has_projector) VALUES (?, ?, ?)";
	private static final String UPDATE = "UPDATE public.audience SET (audience_n, seat, has_projector) = (?, ?, ?) WHERE id = ?";
	private static final String FIND_BY_ID = "SELECT * FROM public.audience WHERE id = ?";
	private static final String EXIST_BY_ID = "SELECT count(*) FROM public.audience WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM public.audience";
	private static final String COUNT = "SELECT COUNT(*) FROM public.audience";
	private static final String UPDATE_LECTURES = "UPDATE public.lecture SET audience_id = NULL WHERE audience_id = ?";
	private static final String DELETE = "DELETE FROM public.audience WHERE id = ?";
	private static final String DELETE_ALL = "UPDATE public.lecture SET audience_id = NULL; DELETE FROM public.audience";

	private final JdbcTemplate jdbcTemplate;

	private RowMapper<Audience> audienceMapper = new RowMapper<Audience>() {
		public Audience mapRow(ResultSet rs, int rowNum) throws SQLException {
			Audience audience = new Audience();
			audience.setId(rs.getInt("id"));
			audience.setAudienceNumber(rs.getInt("audience_n"));
			audience.setSeats(rs.getInt("seat"));
			audience.setHasProjector(rs.getBoolean("has_projector"));
			return audience;
		}
	};

	@Override
	public Audience save(Audience entity) {
		Integer audienceN = null;
		try {
			audienceN = entity.getAudienceNumber();
			log.debug("Save audience N{}", audienceN);
			jdbcTemplate.update(SAVE, audienceN, entity.getSeats(), entity.isHasProjector());
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
		Integer id = null;
		try {
			id = entity.getId();
			log.debug("Update audience id {}", id);
			jdbcTemplate.update(UPDATE, entity.getAudienceNumber(), entity.getSeats(), entity.isHasProjector(), id);
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
		try {
			log.debug("Find audience id {}", id);
			return ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, audienceMapper, id));
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
		try {
			Integer count = jdbcTemplate.queryForObject(EXIST_BY_ID, Integer.class, id);
			boolean exists = ofNullable(count).filter(number -> number > 0).isPresent();
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
		try {
			log.debug("Find all audiences");
			return jdbcTemplate.query(FIND_ALL, audienceMapper);
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
		log.debug("Count audiences");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			log.debug("Delete audience id {}", id);
			jdbcTemplate.update(UPDATE_LECTURES, id);
			jdbcTemplate.update(DELETE, id);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete id {}", id, e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete id {}", id, e);
			throw new UnknownDataAccessException(e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			log.debug("Delete audiences");
			jdbcTemplate.update(DELETE_ALL);
		} catch (NonTransientDataAccessException e) {
			log.error("Could'nt delete audiences", e);
			throw new ConnectionException(e);
		} catch (DataAccessException e) {
			log.error("Could'nt delete audiences", e);
			throw new UnknownDataAccessException(e);
		}
	}
}