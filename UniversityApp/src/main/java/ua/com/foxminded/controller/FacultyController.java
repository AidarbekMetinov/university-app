package ua.com.foxminded.controller;

import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.DELETE_ALL;
import static ua.com.foxminded.Constants.DETAILS;
import static ua.com.foxminded.Constants.FACULTY;
import static ua.com.foxminded.Constants.FORM;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.GROUP;
import static ua.com.foxminded.Constants.INCOMMING_ERROR;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.NAME;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.REDIRECT;
import static ua.com.foxminded.Constants.SAVE_FACULTY;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.UPDATE_FORM;
import static ua.com.foxminded.Constants._FACULTIES;
import static ua.com.foxminded.Constants._FACULTY;
import static ua.com.foxminded.Constants._FACULTY_ID;
import static ua.com.foxminded.Constants._GROUP;
import static ua.com.foxminded.Constants._GROUP_ID;
import static ua.com.foxminded.Constants._TEACHER;
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
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.FacultyService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@Controller
@RequestMapping(FACULTY)
@RequiredArgsConstructor
public class FacultyController {

	private final FacultyService facultyService;
	private final GroupService groupService;
	private final TeacherService teacherService;

	@GetMapping(LIST)
	public String findAll(Model model) {

		log.debug(GET_MAP + FACULTY + LIST);
		model.addAttribute(_FACULTIES, facultyService.findAll());

		return FACULTY + LIST;
	}

	@GetMapping(SAVE_FORM)
	public String save(Model model) {

		log.debug(GET_MAP + FACULTY + SAVE_FORM);
		Faculty faculty = new Faculty();
		faculty.setId(null);

		model.addAttribute(_FACULTY, faculty);

		return FACULTY + FORM;
	}

	@GetMapping(UPDATE_FORM)
	public String updateFaculty(@RequestParam(_FACULTY_ID) Integer id, Model model) {

		log.debug(GET_MAP + FACULTY + UPDATE_FORM);
		if (id != null) {
			model.addAttribute(_FACULTY, facultyService.findById(id));
			return FACULTY + FORM;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DETAILS)
	public String showDetails(@RequestParam(_FACULTY_ID) Integer id, Model model) {

		log.debug(GET_MAP + FACULTY + DETAILS);
		if (id != null) {
			Faculty faculty = facultyService.findById(id);
			model.addAttribute(_FACULTY, faculty);
			model.addAttribute("myGroups", groupService.findByFaculty(faculty));
			model.addAttribute("myTeachers", teacherService.findByFaculty(faculty));
			model.addAttribute("allTeachers", teacherService.findAll());
			model.addAttribute("allGroups", groupService.findAll());
			model.addAttribute("crmObject", new CrmObject());
			return FACULTY + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/groupDetails")
	public String showGroupDetails(@RequestParam(_GROUP_ID) Integer id, @RequestParam(_FACULTY_ID) Integer facultyId,
			Model model) {

		log.debug(GET_MAP + FACULTY + "/groupDetails");
		if (id != null && facultyId != null) {
			Group group = groupService.findById(id);
			model.addAttribute(_GROUP, group);
			model.addAttribute("myFaculty", facultyService.findById(group.getFacultyId()));
			model.addAttribute("myStudents", group.getStudents());
			model.addAttribute(_FACULTY_ID, facultyId);
			return FACULTY + "/group-details";
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/teacherDetails")
	public String showTeacherDetails(@RequestParam(_TEACHER_ID) Integer id,
			@RequestParam(_FACULTY_ID) Integer facultyId, Model model) {

		log.debug(GET_MAP + FACULTY + "/teacherDetails");
		if (id != null && facultyId != null) {
			Teacher teacher = teacherService.findById(id);
			model.addAttribute(_TEACHER, teacher);
			model.addAttribute("myCourses", teacher.getCourses());
			model.addAttribute(_FACULTY_ID, facultyId);
			return FACULTY + "/teacher-details";
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping(SAVE_FACULTY)
	public String saveFaculty(@Valid @ModelAttribute(_FACULTY) Faculty faculty, BindingResult theBindingResult,
			Model model) {
		log.debug(POST_MAP + FACULTY + SAVE_FACULTY);

		if (theBindingResult.hasErrors()) {
			model.addAttribute(_FACULTY, faculty);
			return FACULTY + FORM;
		}
		if (faculty != null) {
			facultyService.saveOrUpdate(faculty);
			return REDIRECT + FACULTY + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping("/addGroup")
	public String addGroup(@RequestParam(_FACULTY_ID) Integer facultyId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, BindingResult theBindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		log.debug(POST_MAP + GROUP + "/addGroup");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute(_FACULTY_ID, facultyId);
			return REDIRECT + FACULTY + DETAILS;
		}
		if (facultyId != null && crmObject != null) {
			Integer groupId = crmObject.getId();
			facultyService.addGroup(facultyId, groupId);
			redirectAttributes.addAttribute(_FACULTY_ID, facultyId);
			return REDIRECT + FACULTY + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping("/addTeacher")
	public String addTeacher(@RequestParam(_FACULTY_ID) Integer facultyId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, BindingResult theBindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		log.debug(POST_MAP + GROUP + "/addTeacher");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute(_FACULTY_ID, facultyId);
			return REDIRECT + FACULTY + DETAILS;
		}
		if (facultyId != null && crmObject != null) {
			Integer teacherId = crmObject.getId();
			facultyService.addTeacher(facultyId, teacherId);
			redirectAttributes.addAttribute(_FACULTY_ID, facultyId);
			return REDIRECT + FACULTY + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DELETE)
	public String deleteFaculty(@RequestParam(_FACULTY_ID) Integer id) {

		log.debug(GET_MAP + FACULTY + DELETE);
		if (id != null) {
			facultyService.deleteById(id);
			return REDIRECT + FACULTY + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DELETE_ALL)
	public String deleteAll() {

		log.debug(GET_MAP + FACULTY + DELETE_ALL);
		facultyService.deleteAll();

		return REDIRECT + FACULTY + LIST;
	}

	@GetMapping(SEARCH)
	public String search(@RequestParam(NAME) String name, Model model) {

		log.debug(GET_MAP + FACULTY + SEARCH);
		if (name != null && !name.trim().isEmpty()) {
			List<Faculty> faculties = facultyService.searchByName(name);
			if (faculties.isEmpty()) {
				return REDIRECT + FACULTY + LIST;
			} else {
				model.addAttribute(_FACULTIES, faculties);
				return FACULTY + LIST;
			}
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}
}