package ua.com.foxminded.controller;

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
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

	private final CourseService courseService;

	@GetMapping("/list")
	public String findAll(Model model) {

		log.debug("Get mapping:/course/list");
		model.addAttribute("courses", courseService.findAll());

		return "course/list-courses";
	}

	@GetMapping("/formForSave")
	public String save(Model model) {

		log.debug("Get mapping:/course/formForSave");
		Course course = new Course();
		course.setId(null);

		model.addAttribute("course", course);

		return "course/course-form";
	}

	@GetMapping("/formForUpdate")
	public String updateCourse(@RequestParam("courseId") Integer id, Model model) {

		log.debug("Get mapping:/course/formForUpdate");
		if (id != null) {
			model.addAttribute("course", courseService.findById(id));
			return "course/course-form";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/saveCourse")
	public String saveCourse(@Valid @ModelAttribute("course") Course course, BindingResult theBindingResult,
			Model model) {
		log.debug("Post mapping:/course/saveCourse");

		if (theBindingResult.hasErrors()) {
			model.addAttribute("course", course);
			return "course/course-form";
		}
		if (course != null) {
			courseService.saveOrUpdate(course);
			return "redirect:/course/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/delete")
	public String deleteCourse(@RequestParam("courseId") Integer id) {

		log.debug("Get mapping:/course/delete");
		if (id != null) {
			courseService.deleteById(id);
			return "redirect:/course/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteAll")
	public String deleteAll() {

		log.debug("Get mapping:/course/deleteAll");
		courseService.deleteAll();

		return "redirect:/course/list";
	}

	@GetMapping("/search")
	public String search(@RequestParam("name") String name, Model model) {

		log.debug("Get mapping:/course/search");
		if (name != null && !name.trim().isEmpty()) {
			List<Course> courses = courseService.searchByName(name);
			if (courses.isEmpty()) {
				return "redirect:/course/list";
			} else {
				model.addAttribute("courses", courses);
				return "course/list-courses";
			}
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}
}
