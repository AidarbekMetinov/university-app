package ua.com.foxminded.rest;

import static java.lang.String.format;
import static ua.com.foxminded.Constants.API;
import static ua.com.foxminded.Constants.BY_ID;
import static ua.com.foxminded.Constants.DELETES;
import static ua.com.foxminded.Constants.DELETE_MAP;
import static ua.com.foxminded.Constants.FINDS;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.ID;
import static ua.com.foxminded.Constants.LECTURE;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.PUT_MAP;
import static ua.com.foxminded.Constants.SAVES_NEW;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.SLF4J_PH;
import static ua.com.foxminded.Constants.UPDATES_EXISTING;
import static ua.com.foxminded.Constants._LECTURE;
import static ua.com.foxminded.Constants._LECTURES;

import java.time.LocalDateTime;
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
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.LectureService;

@Slf4j
@RestController
@Tag(name = "Lecture", description = "the lecture API")
@RequestMapping(API + LECTURE)
@RequiredArgsConstructor
public class LectureRestController {

	private final LectureService service;

	@GetMapping()
	@Operation(summary = FINDS + _LECTURES)
	public List<Lecture> all() {

		log.debug(GET_MAP + API + LECTURE);
		return service.findAll();
	}

	@GetMapping(ID)
	@Operation(summary = FINDS + _LECTURE + BY_ID)
	public Lecture one(@PathVariable Integer id) {

		log.debug(GET_MAP + API + LECTURE + SLF4J_PH, id);
		return service.findById(id);
	}

	@GetMapping(SEARCH + "/{dateTime}")
	@Operation(summary = FINDS + _LECTURES + " by date")
	public List<Lecture> searchByName(@PathVariable String dateTime) {

		log.debug(GET_MAP + API + LECTURE + SEARCH + SLF4J_PH, dateTime);
		return service.findByDate(LocalDateTime.parse(dateTime));
	}

	@DeleteMapping(ID)
	@Operation(summary = DELETES + _LECTURE + BY_ID)
	public String deleteOne(@PathVariable Integer id) {

		log.debug(DELETE_MAP + API + LECTURE + SLF4J_PH, id);
		service.delete(service.findById(id));

		return format("Deleted lecture id - '%s'", id);
	}

	@DeleteMapping()
	@Operation(summary = DELETES + _LECTURES)
	public String deleteAll() {

		log.debug(DELETE_MAP + API + LECTURE);
		service.deleteAll();

		return "Deleted lectures";
	}

	@PostMapping()
	@Operation(summary = SAVES_NEW + _LECTURE)
	public Lecture save(@RequestBody Lecture lecture) {

		log.debug(POST_MAP + API + LECTURE);
		lecture.setId(0);
		service.save(lecture);

		return lecture;
	}

	@PutMapping()
	@Operation(summary = UPDATES_EXISTING + _LECTURE)
	public Lecture update(@RequestBody Lecture lecture) {

		log.debug(PUT_MAP + API + LECTURE);
		service.update(lecture);

		return lecture;
	}

	@PutMapping("/{lectureId}/addGroup/{groupId}")
	@Operation(summary = "Adds group by = {groupId} to lecture by id = {lectureId}")
	public String addGroup(@PathVariable Integer lectureId, @PathVariable Integer groupId) {

		log.debug(PUT_MAP + API + LECTURE + SLF4J_PH + "/addGroup" + SLF4J_PH, lectureId, groupId);
		service.addGroupToLecture(lectureId, groupId);

		return format("Added group id '%s' to lecture id '%s'", groupId, lectureId);
	}
}
