package ua.com.foxminded.rest;

import static java.lang.String.format;
import static ua.com.foxminded.Constants.API;
import static ua.com.foxminded.Constants.AUDIENCE;
import static ua.com.foxminded.Constants.BY_ID;
import static ua.com.foxminded.Constants.DELETES;
import static ua.com.foxminded.Constants.DELETE_MAP;
import static ua.com.foxminded.Constants.FINDS;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.ID;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.PUT_MAP;
import static ua.com.foxminded.Constants.SAVES_NEW;
import static ua.com.foxminded.Constants.SLF4J_PH;
import static ua.com.foxminded.Constants.UPDATES_EXISTING;
import static ua.com.foxminded.Constants._AUDIENCE;
import static ua.com.foxminded.Constants._AUDIENCES;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.entity.Audience;
import ua.com.foxminded.service.AudienceService;

@Slf4j
@RestController
@Tag(name = "Audience", description = "the audience API")
@RequestMapping(API + AUDIENCE)
@RequiredArgsConstructor
public class AudienceRestController {

	private final AudienceService service;

	@GetMapping()
	@Operation(summary = FINDS + _AUDIENCES)
	public List<Audience> all() {

		log.debug(GET_MAP + API + AUDIENCE);
		return service.findAll();
	}

	@GetMapping(ID)
	@Operation(summary = FINDS + _AUDIENCE + BY_ID)
	public Audience one(@PathVariable Integer id) {

		log.debug(GET_MAP + API + AUDIENCE + SLF4J_PH, id);
		return service.findById(id);
	}

	@DeleteMapping(ID)
	@Operation(summary = DELETES + _AUDIENCE + BY_ID)
	public String deleteOne(@PathVariable Integer id) {

		log.debug(DELETE_MAP + API + AUDIENCE + SLF4J_PH, id);
		service.delete(service.findById(id));

		return format("Deleted audience id - '%s'", id);
	}

	@DeleteMapping()
	@Operation(summary = DELETES + _AUDIENCES)
	public String deleteAll() {

		log.debug(DELETE_MAP + API + AUDIENCE);
		service.deleteAll();

		return "Deleted audiences";
	}

	@PostMapping()
	@Operation(summary = SAVES_NEW + _AUDIENCE)
	public Audience save(@RequestBody Audience audience) {

		log.debug(POST_MAP + API + AUDIENCE);
		audience.setId(0);
		service.save(audience);

		return audience;
	}

	@PutMapping()
	@Operation(summary = UPDATES_EXISTING + _AUDIENCE)
	public Audience update(@RequestBody Audience audience) {

		log.debug(PUT_MAP + API + AUDIENCE);
		service.update(audience);

		return audience;
	}
}