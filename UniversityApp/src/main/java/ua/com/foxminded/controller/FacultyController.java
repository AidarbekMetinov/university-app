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
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.FacultyService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@Controller
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

	private final FacultyService facultyService;
	private final GroupService groupService;
	private final TeacherService teacherService;

	@GetMapping("/list")
	public String findAll(Model model) {

		log.debug("Get mapping:/faculty/list");
		model.addAttribute("faculties", facultyService.findAll());

		return "faculty/list-faculties";
	}

	@GetMapping("/formForSave")
	public String save(Model model) {

		log.debug("Get mapping:/faculty/listformForSave");
		Faculty faculty = new Faculty();
		faculty.setId(null);

		model.addAttribute("faculty", faculty);

		return "faculty/faculty-form";
	}

	@GetMapping("/formForUpdate")
	public String updateFaculty(@RequestParam("facultyId") Integer id, Model model) {

		log.debug("Get mapping:/faculty/formForUpdate");
		if (id != null) {
			model.addAttribute("faculty", facultyService.findById(id));
			return "faculty/faculty-form";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/details")
	public String showDetails(@RequestParam("facultyId") Integer id, Model model) {

		log.debug("Get mapping:/faculty/details");
		if (id != null) {
			Faculty faculty = facultyService.findById(id);
			model.addAttribute("faculty", faculty);
			model.addAttribute("myGroups", groupService.findByFaculty(faculty));
			model.addAttribute("myTeachers", teacherService.findByFaculty(faculty));
			model.addAttribute("allTeachers", teacherService.findAll());
			model.addAttribute("allGroups", groupService.findAll());
			model.addAttribute("crmObject", new CrmObject());
			return "faculty/faculty-details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/groupDetails")
	public String showGroupDetails(@RequestParam("groupId") Integer id, @RequestParam("facultyId") Integer facultyId,
			Model model) {

		log.debug("Get mapping:/faculty/groupDetails");
		if (id != null && facultyId != null) {
			Group group = groupService.findById(id);
			model.addAttribute("group", group);
			model.addAttribute("myFaculty", facultyService.findById(group.getFacultyId()));
			model.addAttribute("myStudents", group.getStudents());
			model.addAttribute("facultyId", facultyId);
			return "faculty/group-details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/teacherDetails")
	public String showTeacherDetails(@RequestParam("teacherId") Integer id,
			@RequestParam("facultyId") Integer facultyId, Model model) {

		log.debug("Get mapping:/faculty/teacherDetails");
		if (id != null && facultyId != null) {
			Teacher teacher = teacherService.findById(id);
			model.addAttribute("teacher", teacher);
			model.addAttribute("myCourses", teacher.getCourses());
			model.addAttribute("facultyId", facultyId);
			return "faculty/teacher-details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/saveFaculty")
	public String saveFaculty(@Valid @ModelAttribute("faculty") Faculty faculty, BindingResult theBindingResult,
			Model model) {
		log.debug("Post mapping:/faculty/saveFaculty");

		if (theBindingResult.hasErrors()) {
			model.addAttribute("faculty", faculty);
			return "faculty/faculty-form";
		}
		if (faculty != null) {
			facultyService.saveOrUpdate(faculty);
			return "redirect:/faculty/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/addGroup")
	public String addGroup(@RequestParam("facultyId") Integer facultyId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, BindingResult theBindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		log.debug("Post mapping:/group/addGroup");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute("facultyId", facultyId);
			return "redirect:/faculty/details";
		}
		if (facultyId != null && crmObject != null) {
			Integer groupId = crmObject.getId();
			facultyService.addGroup(facultyId, groupId);
			redirectAttributes.addAttribute("facultyId", facultyId);
			return "redirect:/faculty/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/addTeacher")
	public String addTeacher(@RequestParam("facultyId") Integer facultyId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, BindingResult theBindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		log.debug("Post mapping:/group/addTeacher");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute("facultyId", facultyId);
			return "redirect:/faculty/details";
		}
		if (facultyId != null && crmObject != null) {
			Integer teacherId = crmObject.getId();
			facultyService.addTeacher(facultyId, teacherId);
			redirectAttributes.addAttribute("facultyId", facultyId);
			return "redirect:/faculty/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/delete")
	public String deleteFaculty(@RequestParam("facultyId") Integer id) {

		log.debug("Get mapping:/faculty/delete");
		if (id != null) {
			facultyService.deleteById(id);
			return "redirect:/faculty/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteAll")
	public String deleteAll() {

		log.debug("Get mapping:/faculty/deleteAll");
		facultyService.deleteAll();

		return "redirect:/faculty/list";
	}

	@GetMapping("/search")
	public String search(@RequestParam("name") String name, Model model) {

		log.debug("Get mapping:/faculty/search");
		if (name != null && !name.trim().isEmpty()) {
			List<Faculty> faculties = facultyService.searchByName(name);
			if (faculties.isEmpty()) {
				return "redirect:/faculty/list";
			} else {
				model.addAttribute("faculties", faculties);
				return "faculty/list-faculties";
			}
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}
}