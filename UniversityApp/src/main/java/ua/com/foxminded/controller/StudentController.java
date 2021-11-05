package ua.com.foxminded.controller;

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
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;
	private final GroupService groupService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/list")
	public String findAll(Model model) {

		log.debug("Get mapping:/student/list");
		model.addAttribute("students", studentService.findAll());

		return "student/list-students";
	}

	@GetMapping("/formForSave")
	public String save(Model model) {

		log.debug("Get mapping:/student/formForSave");
		Student student = new Student();
		student.setId(null);

		model.addAttribute("student", student);
		model.addAttribute("groups", groupService.findAll());

		return "student/student-form";
	}

	@GetMapping("/formForUpdate")
	public String updateStudent(@RequestParam("studentId") Integer id, Model model) {

		log.debug("Get mapping:/student/formForUpdate");
		if (id != null) {
			model.addAttribute("student", studentService.findById(id));
			model.addAttribute("groups", groupService.findAll());
			return "student/student-form";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}

	}

	@PostMapping("/saveStudent")
	public String saveStudent(@Valid @ModelAttribute("student") Student student, BindingResult theBindingResult,
			Model model) {
		log.debug("Post mapping:/student/saveStudent");

		if (theBindingResult.hasErrors()) {
			model.addAttribute("groups", groupService.findAll());
			model.addAttribute("student", student);
			return "student/student-form";
		}
		if (student != null) {
			studentService.saveOrUpdate(student);
			return "redirect:/student/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}

	}

	@GetMapping("/delete")
	public String deleteStudent(@RequestParam("studentId") Integer id) {

		log.debug("Get mapping:/student/delete");
		if (id != null) {
			studentService.deleteById(id);
			return "redirect:/student/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteFromGroup")
	public String deleteFromGroup(@RequestParam("studentId") Integer studentId,
			@RequestParam("groupId") Integer groupId, RedirectAttributes redirectAttributes) {

		log.debug("Get mapping:/student/deleteFromGroup");
		if (studentId != null && groupId != null) {
			studentService.deleteStudentFromGroup(studentId);
			redirectAttributes.addAttribute("groupId", groupId);
			return "redirect:/group/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteAll")
	public String deleteAll() {

		log.debug("Get mapping:/student/deleteAll");
		studentService.deleteAll();

		return "redirect:/student/list";
	}

	@GetMapping("/searchByFirstName")
	public String searchByFirstName(@RequestParam("firstName") String name, Model model) {

		log.debug("Get mapping:/student/search");
		if (name != null && !name.trim().isEmpty()) {
			model.addAttribute("students", studentService.searchByFirstName(name));
			return "student/list-students";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/searchByLastName")
	public String searchByLastName(@RequestParam("lastName") String name, Model model) {

		log.debug("Get mapping:/student/searchByLastName");
		if (name != null && !name.trim().isEmpty()) {
			List<Student> students = studentService.searchByLastName(name);
			if (students.isEmpty()) {
				return "redirect:/student/list";
			} else {
				model.addAttribute("students", students);
				return "student/list-students";
			}
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}
}
