package ua.com.foxminded.rest;

import static java.lang.String.format;
import static ua.com.foxminded.Constants.API;
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
import static ua.com.foxminded.Constants.STUDENT;
import static ua.com.foxminded.Constants.UPDATES_EXISTING;
import static ua.com.foxminded.Constants._STUDENT;
import static ua.com.foxminded.Constants._STUDENTS;

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
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.StudentService;

@Slf4j
@RestController
@Tag(name = "Student", description = "the student API")
@RequestMapping(API + STUDENT)
@RequiredArgsConstructor
public class StudentRestController {

	private final StudentService service;

	@GetMapping()
	@Operation(summary = FINDS + _STUDENTS)
	public List<Student> all() {

		log.debug(GET_MAP + API + STUDENT);
		return service.findAll();
	}

	@GetMapping(ID)
	@Operation(summary = FINDS + _STUDENT + BY_ID)
	public Student one(@PathVariable Integer id) {

		log.debug(GET_MAP + API + STUDENT + SLF4J_PH, id);
		return service.findById(id);
	}

	@GetMapping("/searchByFirstName/{firstName}")
	@Operation(summary = FINDS + _STUDENTS + " by first name")
	public List<Student> searchByFirstName(@PathVariable String firstName) {

		log.debug(GET_MAP + API + STUDENT + "/searchByFirstName" + SLF4J_PH, firstName);
		return service.searchByFirstName(firstName);
	}

	@GetMapping("/searchByLastName/{lastName}")
	@Operation(summary = FINDS + _STUDENTS + " by last name")
	public List<Student> searchByLastName(@PathVariable String lastName) {

		log.debug(GET_MAP + API + STUDENT + "/searchByLastName" + SLF4J_PH, lastName);
		return service.searchByLastName(lastName);
	}

	@DeleteMapping(ID)
	@Operation(summary = DELETES + _STUDENT + BY_ID)
	public String deleteOne(@PathVariable Integer id) {

		log.debug(DELETE_MAP + API + STUDENT + SLF4J_PH, id);
		service.delete(service.findById(id));

		return format("Deleted student id - '%s'", id);
	}

	@DeleteMapping()
	@Operation(summary = DELETES + _STUDENTS)
	public String deleteAll() {

		log.debug(DELETE_MAP + API + STUDENT);
		service.deleteAll();

		return "Deleted students";
	}

	@PostMapping()
	@Operation(summary = SAVES_NEW + _STUDENT)
	public Student save(@RequestBody Student student) {

		log.debug(POST_MAP + API + STUDENT);
		student.setId(0);
		service.save(student);

		return student;
	}

	@PutMapping()
	@Operation(summary = UPDATES_EXISTING + _STUDENT)
	public Student update(@RequestBody Student student) {

		log.debug(PUT_MAP + API + STUDENT);
		service.update(student);

		return student;
	}

	@DeleteMapping("/{studentId}/deleteFromGroup")
	@Operation(summary = DELETES + _STUDENT + " from its faculty")
	public String deleteFromGroup(@PathVariable Integer studentId) {

		log.debug(DELETE_MAP + API + STUDENT + SLF4J_PH + "/deleteFromGroup", studentId);
		service.deleteStudentFromGroup(studentId);

		return format("Deleted student id '%s' from it's group", studentId);
	}
}
