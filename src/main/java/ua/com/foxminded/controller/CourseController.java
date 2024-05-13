package ua.com.foxminded.controller;

import static ua.com.foxminded.Constants.COURSE;
import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.DELETE_ALL;
import static ua.com.foxminded.Constants.FORM;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.INCOMMING_ERROR;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.NAME;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.REDIRECT;
import static ua.com.foxminded.Constants.SAVE_COURSE;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.UPDATE_FORM;
import static ua.com.foxminded.Constants._COURSE;
import static ua.com.foxminded.Constants._COURSES;
import static ua.com.foxminded.Constants._COURSE_ID;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.service.CourseService;

@Slf4j
@Controller
@RequestMapping(COURSE)
@RequiredArgsConstructor
public class CourseController {

	private final CourseService courseService;

	@GetMapping(LIST)
	public String findAll(Model model) {

		log.debug(GET_MAP + COURSE + LIST);
		model.addAttribute(_COURSES, courseService.findAll());

		return COURSE + LIST;
	}

	@GetMapping(SAVE_FORM)
	public String save(Model model) {

		log.debug(GET_MAP + COURSE + SAVE_FORM);
		Course course = new Course();
		course.setId(null);

		model.addAttribute(_COURSE, course);

		return COURSE + FORM;
	}

	@GetMapping(UPDATE_FORM)
	public String updateCourse(@RequestParam(_COURSE_ID) Integer id, Model model) {

		log.debug(GET_MAP + COURSE + UPDATE_FORM);
		if (id != null) {
			model.addAttribute(_COURSE, courseService.findById(id));
			return COURSE + FORM;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping(SAVE_COURSE)
	public String saveCourse(@Valid @ModelAttribute(_COURSE) Course course, BindingResult theBindingResult,
			Model model) {
		log.debug(POST_MAP + COURSE + SAVE_COURSE);

		if (theBindingResult.hasErrors()) {
			model.addAttribute(_COURSE, course);
			return COURSE + FORM;
		}
		if (course != null) {
			courseService.saveOrUpdate(course);
			return REDIRECT + COURSE + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DELETE)
	public String deleteCourse(@RequestParam(_COURSE_ID) Integer id) {

		log.debug(GET_MAP + COURSE + DELETE);
		if (id != null) {
			courseService.deleteById(id);
			return REDIRECT + COURSE + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DELETE_ALL)
	public String deleteAll() {

		log.debug(GET_MAP + COURSE + DELETE_ALL);
		courseService.deleteAll();

		return REDIRECT + COURSE + LIST;
	}

	@GetMapping(SEARCH)
	public String search(@RequestParam(NAME) String name, Model model) {

		log.debug(GET_MAP + COURSE + SEARCH);
		if (name != null && !name.trim().isEmpty()) {
			List<Course> courses = courseService.searchByName(name);
			if (courses.isEmpty()) {
				return REDIRECT + COURSE + LIST;
			} else {
				model.addAttribute("courses", courses);
				return COURSE + LIST;
			}
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}
}
