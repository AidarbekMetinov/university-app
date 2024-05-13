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
import static ua.com.foxminded.Constants.SLF4J_PH;
import static ua.com.foxminded.Constants.TEACHER;
import static ua.com.foxminded.Constants.UPDATES_EXISTING;
import static ua.com.foxminded.Constants._COURSE;
import static ua.com.foxminded.Constants._TEACHER;
import static ua.com.foxminded.Constants._TEACHERS;

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
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@RestController
@Tag(name = "Teacher", description = "the teacher API")
@RequestMapping(API + TEACHER)
@RequiredArgsConstructor
public class TeacherRestController {

	private final TeacherService service;

	@GetMapping()
	@Operation(summary = FINDS + _TEACHERS)
	public List<Teacher> all() {

		log.debug(GET_MAP + API + TEACHER);
		return service.findAll();
	}

	@GetMapping(ID)
	@Operation(summary = FINDS + _TEACHER + BY_ID)
	public Teacher one(@PathVariable Integer id) {

		log.debug(GET_MAP + API + TEACHER + SLF4J_PH, id);
		return service.findById(id);
	}

	@GetMapping("/searchByFirstName/{firstName}")
	@Operation(summary = FINDS + _TEACHERS + " by first name")
	public List<Teacher> searchByFirstName(@PathVariable String firstName) {

		log.debug(GET_MAP + API + TEACHER + "/searchByFirstName" + SLF4J_PH, firstName);
		return service.searchByFirstName(firstName);
	}

	@GetMapping("/searchByLastName/{lastName}")
	@Operation(summary = FINDS + _TEACHERS + " by last name")
	public List<Teacher> searchByLastName(@PathVariable String lastName) {

		log.debug(GET_MAP + API + TEACHER + "/searchByLastName" + SLF4J_PH, lastName);
		return service.searchByLastName(lastName);
	}

	@DeleteMapping(ID)
	@Operation(summary = DELETES + _TEACHER + BY_ID)
	public String deleteOne(@PathVariable Integer id) {

		log.debug(DELETE_MAP + API + TEACHER + SLF4J_PH, id);
		service.delete(service.findById(id));

		return format("Deleted teacher id - '%s'", id);
	}

	@DeleteMapping()
	@Operation(summary = DELETES + _TEACHERS)
	public String deleteAll() {

		log.debug(DELETE_MAP + API + TEACHER);
		service.deleteAll();

		return "Deleted teachers";
	}

	@PostMapping()
	@Operation(summary = SAVES_NEW + _TEACHER)
	public Teacher save(@RequestBody Teacher teacher) {

		log.debug(POST_MAP + API + TEACHER);
		teacher.setId(0);
		service.save(teacher);

		return teacher;
	}

	@PutMapping()
	@Operation(summary = UPDATES_EXISTING + _TEACHER)
	public Teacher update(@RequestBody Teacher teacher) {

		log.debug(PUT_MAP + API + TEACHER);
		service.update(teacher);

		return teacher;
	}

	@PutMapping("/{teacherId}/addCourse/{courseId}")
	@Operation(summary = "Adds course by = {courseId} to teacher by id = {teacherId}")
	public String addCourse(@PathVariable Integer teacherId, @PathVariable Integer courseId) {

		log.debug(PUT_MAP + API + TEACHER + SLF4J_PH + "/addCourse" + SLF4J_PH, teacherId, courseId);
		service.addCourseToTeacher(teacherId, courseId);

		return format("Added course id '%s' to teacher id '%s'", courseId, teacherId);
	}

	@DeleteMapping("/{teacherId}/fromFaculty/{facultyId}")
	@Operation(summary = DELETES + _TEACHER + BY_ID + " = {teacherId} from faculty by id = {facultyId}")
	public String deleteFromFaculty(@PathVariable Integer teacherId, @PathVariable Integer facultyId) {

		log.debug(DELETE_MAP + API + TEACHER + SLF4J_PH + "/fromFaculty" + SLF4J_PH, teacherId, facultyId);
		service.deleteTeacherFromFaculty(teacherId, facultyId);

		return format("Deleted teacher id '%s' from faculty id '%s'", teacherId, facultyId);
	}

	@DeleteMapping("/{teacherId}/course/{courseId}")
	@Operation(summary = DELETES + _COURSE + BY_ID + " = {courseId} from teacher by id = {teacherId}")
	public String deleteCourseFromTeacher(@PathVariable Integer teacherId, @PathVariable Integer courseId) {

		log.debug(DELETE_MAP + API + TEACHER + SLF4J_PH + COURSE + SLF4J_PH, teacherId, courseId);
		service.deleteCourseFromTeacher(teacherId, courseId);

		return format("Deleted course id '%s' from teacher id '%s'", courseId, teacherId);
	}
}
