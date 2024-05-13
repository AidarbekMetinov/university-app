package ua.com.foxminded.controller;

import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.DELETE_ALL;
import static ua.com.foxminded.Constants.DETAILS;
import static ua.com.foxminded.Constants.FIRST_NAME;
import static ua.com.foxminded.Constants.FORM;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.GROUP;
import static ua.com.foxminded.Constants.INCOMMING_ERROR;
import static ua.com.foxminded.Constants.LAST_NAME;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.REDIRECT;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.SAVE_STUDENT;
import static ua.com.foxminded.Constants.STUDENT;
import static ua.com.foxminded.Constants.UPDATE_FORM;
import static ua.com.foxminded.Constants._GROUPS;
import static ua.com.foxminded.Constants._GROUP_ID;
import static ua.com.foxminded.Constants._STUDENT;
import static ua.com.foxminded.Constants._STUDENTS;
import static ua.com.foxminded.Constants._STUDENT_ID;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@Slf4j
@Controller
@RequestMapping(STUDENT)
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;
	private final GroupService groupService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping(LIST)
	public String findAll(Model model) {

		log.debug(GET_MAP + STUDENT + LIST);
		model.addAttribute(_STUDENTS, studentService.findAll());

		return STUDENT + LIST;
	}

	@GetMapping(SAVE_FORM)
	public String save(Model model) {

		log.debug(GET_MAP + STUDENT + SAVE_FORM);
		Student student = new Student();
		student.setId(null);

		model.addAttribute(_STUDENT, student);
		model.addAttribute(_GROUPS, groupService.findAll());

		return STUDENT + FORM;
	}

	@GetMapping(UPDATE_FORM)
	public String updateStudent(@RequestParam(_STUDENT_ID) Integer id, Model model) {

		log.debug(GET_MAP + STUDENT + UPDATE_FORM);
		if (id != null) {
			model.addAttribute(_STUDENT, studentService.findById(id));
			model.addAttribute(_GROUPS, groupService.findAll());
			return STUDENT + FORM;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}

	}

	@PostMapping(SAVE_STUDENT)
	public String saveStudent(@Valid @ModelAttribute(_STUDENT) Student student, BindingResult theBindingResult,
			Model model) {
		log.debug(POST_MAP + STUDENT + SAVE_STUDENT);

		if (theBindingResult.hasErrors()) {
			model.addAttribute(_GROUPS, groupService.findAll());
			model.addAttribute(_STUDENT, student);
			return STUDENT + FORM;
		}
		if (student != null) {
			studentService.saveOrUpdate(student);
			return REDIRECT + STUDENT + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}

	}

	@GetMapping(DELETE)
	public String deleteStudent(@RequestParam(_STUDENT_ID) Integer id) {

		log.debug(GET_MAP + STUDENT + DELETE);
		if (id != null) {
			studentService.deleteById(id);
			return REDIRECT + STUDENT + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/deleteFromGroup")
	public String deleteFromGroup(@RequestParam(_STUDENT_ID) Integer studentId,
			@RequestParam(_GROUP_ID) Integer groupId, RedirectAttributes redirectAttributes) {

		log.debug(GET_MAP + STUDENT + "/deleteFromGroup");
		if (studentId != null && groupId != null) {
			studentService.deleteStudentFromGroup(studentId);
			redirectAttributes.addAttribute(_GROUP_ID, groupId);
			return REDIRECT + GROUP + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DELETE_ALL)
	public String deleteAll() {

		log.debug(GET_MAP + STUDENT + DELETE_ALL);
		studentService.deleteAll();

		return REDIRECT + STUDENT + LIST;
	}

	@GetMapping("/searchByFirstName")
	public String searchByFirstName(@RequestParam(FIRST_NAME) String name, Model model) {

		log.debug(GET_MAP + STUDENT + "/searchByFirstName");
		if (name != null && !name.trim().isEmpty()) {
			model.addAttribute(_STUDENTS, studentService.searchByFirstName(name));
			return STUDENT + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/searchByLastName")
	public String searchByLastName(@RequestParam(LAST_NAME) String name, Model model) {

		log.debug(GET_MAP + STUDENT + "/searchByLastName");
		if (name != null && !name.trim().isEmpty()) {
			List<Student> students = studentService.searchByLastName(name);
			if (students.isEmpty()) {
				return REDIRECT + STUDENT + LIST;
			} else {
				model.addAttribute(_STUDENTS, students);
				return STUDENT + LIST;
			}
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}
}
