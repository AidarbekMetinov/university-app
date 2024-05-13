package ua.com.foxminded.rest;

import static java.lang.String.format;
import static ua.com.foxminded.Constants.API;
import static ua.com.foxminded.Constants.BY_ID;
import static ua.com.foxminded.Constants.DELETES;
import static ua.com.foxminded.Constants.DELETE_MAP;
import static ua.com.foxminded.Constants.FACULTY;
import static ua.com.foxminded.Constants.FINDS;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.ID;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.PUT_MAP;
import static ua.com.foxminded.Constants.SAVES_NEW;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.SLF4J_PH;
import static ua.com.foxminded.Constants.UPDATES_EXISTING;
import static ua.com.foxminded.Constants._FACULTIES;
import static ua.com.foxminded.Constants._FACULTY;

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
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

@Slf4j
@RestController
@Tag(name = "Faculty", description = "the faculty API")
@RequestMapping(API + FACULTY)
@RequiredArgsConstructor
public class FacultyRestController {

	private final FacultyService service;

	@GetMapping()
	@Operation(summary = FINDS + _FACULTIES)
	public List<Faculty> all() {

		log.debug(GET_MAP + API + FACULTY);
		return service.findAll();
	}

	@GetMapping(ID)
	@Operation(summary = FINDS + _FACULTY + BY_ID)
	public Faculty one(@PathVariable Integer id) {

		log.debug(GET_MAP + API + FACULTY + SLF4J_PH, id);
		return service.findById(id);
	}

	@GetMapping(SEARCH + "/{name}")
	@Operation(summary = FINDS + _FACULTIES + " by name")
	public List<Faculty> searchByName(@PathVariable String name) {

		log.debug(GET_MAP + API + FACULTY + SEARCH + SLF4J_PH, name);
		return service.searchByName(name);
	}

	@DeleteMapping(ID)
	@Operation(summary = DELETES + _FACULTY + BY_ID)
	public String deleteOne(@PathVariable Integer id) {

		log.debug(DELETE_MAP + API + FACULTY + SLF4J_PH, id);
		service.delete(service.findById(id));

		return format("Deleted faculty id - '%s'", id);
	}

	@DeleteMapping()
	@Operation(summary = DELETES + _FACULTIES)
	public String deleteAll() {

		log.debug(DELETE_MAP + API + FACULTY);
		service.deleteAll();

		return "Deleted faculties";
	}

	@PostMapping()
	@Operation(summary = SAVES_NEW + _FACULTY)
	public Faculty save(@RequestBody Faculty faculty) {

		log.debug(POST_MAP + API + FACULTY);
		faculty.setId(0);
		service.save(faculty);

		return faculty;
	}

	@PutMapping()
	@Operation(summary = UPDATES_EXISTING + _FACULTY)
	public Faculty update(@RequestBody Faculty faculty) {

		log.debug(PUT_MAP + API + FACULTY);
		service.update(faculty);

		return faculty;
	}

	@PutMapping("/{facultyId}/addTeacher/{teacherId}")
	@Operation(summary = "Adds teacher by id = {teacherId} to faculty by id = {facultyId}")
	public String addTeacher(@PathVariable Integer facultyId, @PathVariable Integer teacherId) {

		log.debug(PUT_MAP + API + FACULTY + SLF4J_PH + "/addTeacher" + SLF4J_PH, facultyId, teacherId);
		service.addTeacher(facultyId, teacherId);

		return format("Added teacher id '%s' to faculty id '%s'", teacherId, facultyId);
	}

	@PutMapping("/{facultyId}/addGroup/{groupId}")
	@Operation(summary = "Adds group by id = {groupId} to faculty by id = {facultyId}")
	public String addGroup(@PathVariable Integer facultyId, @PathVariable Integer groupId) {

		log.debug(PUT_MAP + API + FACULTY + SLF4J_PH + "/addGroup" + SLF4J_PH, facultyId, groupId);
		service.addGroup(facultyId, groupId);

		return format("Added group id '%s' to faculty id '%s'", groupId, facultyId);
	}
}
