package ua.com.foxminded.rest;

import static java.lang.String.format;
import static ua.com.foxminded.Constants.API;
import static ua.com.foxminded.Constants.BY_ID;
import static ua.com.foxminded.Constants.DELETES;
import static ua.com.foxminded.Constants.DELETE_MAP;
import static ua.com.foxminded.Constants.FINDS;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.GROUP;
import static ua.com.foxminded.Constants.ID;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.PUT_MAP;
import static ua.com.foxminded.Constants.SAVES_NEW;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.SLF4J_PH;
import static ua.com.foxminded.Constants.UPDATES_EXISTING;
import static ua.com.foxminded.Constants._GROUP;
import static ua.com.foxminded.Constants._GROUPS;

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
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

@Slf4j
@RestController
@Tag(name = "Group", description = "the group API")
@RequestMapping(API + GROUP)
@RequiredArgsConstructor
public class GroupRestController {

	private final GroupService service;

	@GetMapping()
	@Operation(summary = FINDS + _GROUPS)
	public List<Group> all() {

		log.debug(GET_MAP + API + GROUP);
		return service.findAll();
	}

	@GetMapping(ID)
	@Operation(summary = FINDS + _GROUP + BY_ID)
	public Group one(@PathVariable Integer id) {

		log.debug(GET_MAP + API + GROUP + SLF4J_PH, id);
		return service.findById(id);
	}

	@GetMapping(SEARCH + "/{name}")
	@Operation(summary = FINDS + _GROUPS + " by name")
	public List<Group> searchByName(@PathVariable String name) {

		log.debug(GET_MAP + API + GROUP + SEARCH + SLF4J_PH, name);
		return service.searchByName(name);
	}

	@DeleteMapping(ID)
	@Operation(summary = DELETES + _GROUP + BY_ID)
	public String deleteOne(@PathVariable Integer id) {

		log.debug(DELETE_MAP + API + GROUP + SLF4J_PH, id);
		service.delete(service.findById(id));

		return format("Deleted group id - '%s'", id);
	}

	@DeleteMapping()
	@Operation(summary = DELETES + _GROUPS)
	public String deleteAll() {

		log.debug(DELETE_MAP + API + GROUP);
		service.deleteAll();

		return "Deleted groups";
	}

	@PostMapping()
	@Operation(summary = SAVES_NEW + _GROUP)
	public Group save(@RequestBody Group group) {

		log.debug(POST_MAP + API + GROUP);
		group.setId(0);
		service.save(group);

		return group;
	}

	@PutMapping()
	@Operation(summary = UPDATES_EXISTING + _GROUP)
	public Group update(@RequestBody Group group) {

		log.debug(PUT_MAP + API + GROUP);
		service.update(group);

		return group;
	}

	@PutMapping("/{groupId}/addStudent/{studentId}")
	@Operation(summary = "Adds student by = {studentId} to group by id = {groupId}")
	public String addStudent(@PathVariable Integer groupId, @PathVariable Integer studentId) {

		log.debug(PUT_MAP + API + GROUP + SLF4J_PH + "/addStudent" + SLF4J_PH, groupId, studentId);
		service.addStudent(groupId, studentId);

		return format("Added student id '%s' to group id '%s'", studentId, groupId);
	}

	@DeleteMapping("/{groupId}/deleteFromFaculty")
	@Operation(summary = DELETES + _GROUP + " from its faculty")
	public String deleteFromFaculty(@PathVariable Integer groupId) {

		log.debug(DELETE_MAP + API + GROUP + SLF4J_PH + "/deleteFromFaculty", groupId);
		service.deleteGroupFromFaculty(groupId);

		return format("Deleted group id '%s' from it's faculty", groupId);
	}

	@DeleteMapping("/{groupId}/fromLecture/{lectureId}")
	@Operation(summary = DELETES + _GROUP + BY_ID + " = {groupId} from lecture by id = {lectureId}")
	public String deleteFromLecture(@PathVariable Integer groupId, @PathVariable Integer lectureId) {

		log.debug(DELETE_MAP + API + GROUP + SLF4J_PH + "/fromLecture" + SLF4J_PH, groupId, lectureId);
		service.deleteGroupFromLecture(groupId, lectureId);

		return format("Deleted group id '%s' from lecture id '%s'", groupId, lectureId);
	}
}
