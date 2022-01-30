package ua.com.foxminded.controller;

import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.DELETE_ALL;
import static ua.com.foxminded.Constants.DETAILS;
import static ua.com.foxminded.Constants.FACULTY;
import static ua.com.foxminded.Constants.FIRST_NAME;
import static ua.com.foxminded.Constants.FORM;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.INCOMMING_ERROR;
import static ua.com.foxminded.Constants.LAST_NAME;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.REDIRECT;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.SAVE_TEACHER;
import static ua.com.foxminded.Constants.TEACHER;
import static ua.com.foxminded.Constants.UPDATE_FORM;
import static ua.com.foxminded.Constants._COURSE_ID;
import static ua.com.foxminded.Constants._FACULTY_ID;
import static ua.com.foxminded.Constants._TEACHER;
import static ua.com.foxminded.Constants._TEACHERS;
import static ua.com.foxminded.Constants._TEACHER_ID;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.crm.CrmObject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@Controller
@RequestMapping(TEACHER)
@RequiredArgsConstructor
public class TeacherController {

	private final TeacherService teacherService;
	private final CourseService courseService;

	@GetMapping(LIST)
	public String findAll(Model model) {

		log.debug(GET_MAP + TEACHER + LIST);
		model.addAttribute(_TEACHERS, teacherService.findAll());

		return TEACHER + LIST;
	}

	@GetMapping(SAVE_FORM)
	public String save(Model model) {

		log.debug(GET_MAP + TEACHER + SAVE_FORM);
		Teacher teacher = new Teacher();
		teacher.setId(null);

		model.addAttribute(_TEACHER, teacher);

		return TEACHER + FORM;
	}

	@GetMapping(UPDATE_FORM)
	public String updateTeacher(@RequestParam(_TEACHER_ID) Integer id, Model model) {

		log.debug(GET_MAP + TEACHER + UPDATE_FORM);
		if (id != null) {
			model.addAttribute(_TEACHER, teacherService.findById(id));
			return TEACHER + FORM;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DETAILS)
	public String showDetails(@RequestParam(_TEACHER_ID) Integer id, Model model) {

		log.debug(GET_MAP + TEACHER + DETAILS);
		if (id != null) {
			Teacher teacher = teacherService.findById(id);
			model.addAttribute(_TEACHER, teacher);
			model.addAttribute("myCourses", teacher.getCourses());
			model.addAttribute("allCourses", courseService.findAll());
			model.addAttribute("crmObject", new CrmObject());
			return TEACHER + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping(SAVE_TEACHER)
	public String saveTeacher(@Valid @ModelAttribute(_TEACHER) Teacher teacher, BindingResult theBindingResult,
			Model model) {
		log.debug(POST_MAP + TEACHER + SAVE_TEACHER);

		if (theBindingResult.hasErrors()) {
			model.addAttribute(_TEACHER, teacher);
			return TEACHER + FORM;
		}
		if (teacher != null) {
			teacherService.saveOrUpdate(teacher);
			return REDIRECT + TEACHER + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping("/addCourse")
	public String addCourseToTeacher(@RequestParam(_TEACHER_ID) Integer teacherId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, Model model, RedirectAttributes redirectAttributes,
			BindingResult theBindingResult) {
		log.debug(GET_MAP + TEACHER + "/addCourse");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute(_TEACHER_ID, teacherId);
			return REDIRECT + TEACHER + DETAILS;
		}
		if (teacherId != null && crmObject != null) {
			Integer courseId = crmObject.getId();

			teacherService.addCourseToTeacher(teacherId, courseId);

			redirectAttributes.addAttribute(_TEACHER_ID, teacherId);
			return REDIRECT + TEACHER + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DELETE)
	public String deleteTeacher(@RequestParam(_TEACHER_ID) Integer id) {

		log.debug(GET_MAP + TEACHER + DELETE);
		if (id != null) {
			teacherService.deleteById(id);
			return REDIRECT + TEACHER + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/deleteCourse")
	public String deleteCourseFromTeacher(@RequestParam(_TEACHER_ID) Integer teacherId,
			@RequestParam(_COURSE_ID) Integer courseId, RedirectAttributes redirectAttributes) {

		log.debug(GET_MAP + TEACHER + "/deleteCourse");
		if (teacherId != null && courseId != null) {
			teacherService.deleteCourseFromTeacher(teacherId, courseId);

			redirectAttributes.addAttribute(_TEACHER_ID, teacherId);
			return REDIRECT + TEACHER + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/deleteFromFaculty")
	public String deleteFromFaculty(@RequestParam(_TEACHER_ID) Integer teacherId,
			@RequestParam(_FACULTY_ID) Integer facultyId, RedirectAttributes redirectAttributes) {

		log.debug(GET_MAP + TEACHER + "/deleteFromFaculty");
		if (teacherId != null && facultyId != null) {
			teacherService.deleteTeacherFromFaculty(teacherId, facultyId);
			redirectAttributes.addAttribute(_FACULTY_ID, facultyId);
			return REDIRECT + FACULTY + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DELETE_ALL)
	public String deleteAll() {

		log.debug(GET_MAP + TEACHER + DELETE_ALL);
		teacherService.deleteAll();

		return REDIRECT + TEACHER + LIST;
	}

	@GetMapping("/searchByFirstName")
	public String searchByFirstName(@RequestParam(FIRST_NAME) String name, Model model) {

		log.debug(GET_MAP + TEACHER + "/searchByFirstName");
		if (name != null && !name.trim().isEmpty()) {
			List<Teacher> teachers = teacherService.searchByFirstName(name);
			if (teachers.isEmpty()) {
				return REDIRECT + TEACHER + LIST;
			} else {
				model.addAttribute(_TEACHERS, teachers);
				return TEACHER + LIST;
			}
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/searchByLastName")
	public String searchByLastName(@RequestParam(LAST_NAME) String name, Model model) {

		log.debug(GET_MAP + TEACHER + "/searchByLastName");
		if (name != null && !name.trim().isEmpty()) {
			List<Teacher> teachers = teacherService.searchByLastName(name);
			if (teachers.isEmpty()) {
				return REDIRECT + TEACHER + LIST;
			} else {
				model.addAttribute(_TEACHERS, teachers);
				return TEACHER + LIST;
			}
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}
}
