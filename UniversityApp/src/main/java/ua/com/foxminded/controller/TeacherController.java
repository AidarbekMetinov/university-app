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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.crm.CrmObject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

	private final TeacherService teacherService;
	private final CourseService courseService;

	@GetMapping("/list")
	public String findAll(Model model) {

		log.debug("Get mapping:/teacher/list");
		model.addAttribute("teachers", teacherService.findAll());

		return "teacher/list-teachers";
	}

	@GetMapping("/formForSave")
	public String save(Model model) {

		log.debug("Get mapping:/teacher/formForSave");
		Teacher teacher = new Teacher();
		teacher.setId(null);

		model.addAttribute("teacher", teacher);

		return "teacher/teacher-form";
	}

	@GetMapping("/formForUpdate")
	public String updateTeacher(@RequestParam("teacherId") Integer id, Model model) {

		log.debug("Get mapping:/teacher/formForUpdate");
		if (id != null) {
			model.addAttribute("teacher", teacherService.findById(id));
			return "teacher/teacher-form";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/details")
	public String showDetails(@RequestParam("teacherId") Integer id, Model model) {

		log.debug("Get mapping:/teacher/details");
		if (id != null) {
			Teacher teacher = teacherService.findById(id);
			model.addAttribute("teacher", teacher);
			model.addAttribute("myCourses", teacher.getCourses());
			model.addAttribute("allCourses", courseService.findAll());
			model.addAttribute("crmObject", new CrmObject());
			return "teacher/teacher-details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/saveTeacher")
	public String saveTeacher(@Valid @ModelAttribute("teacher") Teacher teacher, BindingResult theBindingResult,
			Model model) {
		log.debug("Post mapping:/teacher/saveTeacher");

		if (theBindingResult.hasErrors()) {
			model.addAttribute("teacher", teacher);
			return "teacher/teacher-form";
		}
		if (teacher != null) {
			teacherService.saveOrUpdate(teacher);
			return "redirect:/teacher/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/addCourse")
	public String addCourseToTeacher(@RequestParam("teacherId") Integer teacherId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, Model model, RedirectAttributes redirectAttributes,
			BindingResult theBindingResult) {
		log.debug("Get mapping:/teacher/addCourse");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute("teacherId", teacherId);
			return "redirect:/teacher/details";
		}
		if (teacherId != null && crmObject != null) {
			Integer courseId = crmObject.getId();

			teacherService.addCourseToTeacher(teacherId, courseId);

			redirectAttributes.addAttribute("teacherId", teacherId);
			return "redirect:/teacher/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/delete")
	public String deleteTeacher(@RequestParam("teacherId") Integer id) {

		log.debug("Get mapping:/teacher/delete");
		if (id != null) {
			teacherService.deleteById(id);
			return "redirect:/teacher/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteCourse")
	public String deleteFromLesson(@RequestParam("teacherId") Integer teacherId,
			@RequestParam("courseId") Integer courseId, RedirectAttributes redirectAttributes) {

		log.debug("Get mapping:/teacher/deleteCourse");
		if (teacherId != null && courseId != null) {
			teacherService.deleteCourseFromTeacher(teacherId, courseId);

			redirectAttributes.addAttribute("teacherId", teacherId);
			return "redirect:/teacher/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteFromFaculty")
	public String deleteFromFaculty(@RequestParam("teacherId") Integer teacherId,
			@RequestParam("facultyId") Integer facultyId, RedirectAttributes redirectAttributes) {

		log.debug("Get mapping:/teacher/deleteFromFaculty");
		if (teacherId != null && facultyId != null) {
			teacherService.deleteTeacherFromFaculty(teacherId, facultyId);
			redirectAttributes.addAttribute("facultyId", facultyId);
			return "redirect:/faculty/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteAll")
	public String deleteAll() {

		log.debug("Get mapping:/teacher/deleteAll");
		teacherService.deleteAll();

		return "redirect:/teacher/list";
	}

	@GetMapping("/searchByFirstName")
	public String searchByFirstName(@RequestParam("firstName") String name, Model model) {

		log.debug("Get mapping:/teacher/searchByFirstName");
		if (name != null && !name.trim().isEmpty()) {
			List<Teacher> teachers = teacherService.searchByFirstName(name);
			if (teachers.isEmpty()) {
				return "redirect:/teacher/list";
			} else {
				model.addAttribute("teachers", teachers);
				return "teacher/list-teachers";
			}
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/searchByLastName")
	public String searchByLastName(@RequestParam("lastName") String name, Model model) {

		log.debug("Get mapping:/student/searchByLastName");
		if (name != null && !name.trim().isEmpty()) {
			List<Teacher> teachers = teacherService.searchByLastName(name);
			if (teachers.isEmpty()) {
				return "redirect:/teacher/list";
			} else {
				model.addAttribute("teachers", teachers);
				return "teacher/list-teachers";
			}
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}
}
