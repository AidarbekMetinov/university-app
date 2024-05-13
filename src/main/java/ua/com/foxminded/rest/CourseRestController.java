package ua.com.foxminded.rest;

import static java.lang.String.format;
import static ua.com.foxminded.Constants.API;
import static ua.com.foxminded.Constants.BY_ID;
import static ua.com.foxminded.Constants.COURSE;
import static ua.com.foxminded.Constants.DELETES;
import static ua.com.foxminded.Constants.DELETE_MAP;
import static ua.com.foxminded.Constants.FINDS;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.ID;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.PUT_MAP;
import static ua.com.foxminded.Constants.SAVES_NEW;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.SLF4J_PH;
import static ua.com.foxminded.Constants.UPDATES_EXISTING;
import static ua.com.foxminded.Constants._COURSE;
import static ua.com.foxminded.Constants._COURSES;

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
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.service.CourseService;

@Slf4j
@RestController
@Tag(name = "Course", description = "the course API")
@RequestMapping(API + COURSE)
@RequiredArgsConstructor
public class CourseRestController {

	private final CourseService service;

	@GetMapping()
	@Operation(summary = FINDS + _COURSES)
	public List<Course> all() {

		log.debug(GET_MAP + API + COURSE);
		return service.findAll();
	}

	@GetMapping(ID)
	@Operation(summary = FINDS + _COURSE + BY_ID)
	public Course one(@PathVariable Integer id) {

		log.debug(GET_MAP + API + COURSE + SLF4J_PH, id);
		return service.findById(id);
	}

	@GetMapping(SEARCH + "/{name}")
	@Operation(summary = FINDS + _COURSES + " by name")
	public List<Course> searchByName(@PathVariable String name) {

		log.debug(GET_MAP + API + COURSE + SEARCH + SLF4J_PH, name);
		return service.searchByName(name);
	}

	@DeleteMapping(ID)
	@Operation(summary = DELETES + _COURSE + BY_ID)
	public String deleteOne(@PathVariable Integer id) {

		log.debug(DELETE_MAP + API + COURSE + SLF4J_PH, id);
		service.delete(service.findById(id));

		return format("Deleted course id - '%s'", id);
	}

	@DeleteMapping()
	@Operation(summary = DELETES + _COURSES)
	public String deleteAll() {

		log.debug(DELETE_MAP + API + COURSE);
		service.deleteAll();

		return "Deleted courses";
	}

	@PostMapping()
	@Operation(summary = SAVES_NEW + _COURSE)
	public Course save(@RequestBody Course course) {

		log.debug(POST_MAP + API + COURSE);
		course.setId(0);
		service.save(course);

		return course;
	}

	@PutMapping()
	@Operation(summary = UPDATES_EXISTING + _COURSE)
	public Course update(@RequestBody Course course) {

		log.debug(PUT_MAP + API + COURSE);
		service.update(course);

		return course;
	}
}
