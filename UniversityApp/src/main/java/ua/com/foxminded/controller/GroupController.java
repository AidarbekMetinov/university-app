package ua.com.foxminded.controller;

import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.DELETE_ALL;
import static ua.com.foxminded.Constants.DETAILS;
import static ua.com.foxminded.Constants.FACULTY;
import static ua.com.foxminded.Constants.FORM;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.GROUP;
import static ua.com.foxminded.Constants.INCOMMING_ERROR;
import static ua.com.foxminded.Constants.LECTURE;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.NAME;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.REDIRECT;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.SAVE_GROUP;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.UPDATE_FORM;
import static ua.com.foxminded.Constants._FACULTIES;
import static ua.com.foxminded.Constants._FACULTY_ID;
import static ua.com.foxminded.Constants._GROUP;
import static ua.com.foxminded.Constants._GROUPS;
import static ua.com.foxminded.Constants._GROUP_ID;
import static ua.com.foxminded.Constants._LECTURE_ID;

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
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.FacultyService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@Slf4j
@Controller
@RequestMapping(GROUP)
@RequiredArgsConstructor
public class GroupController {

	private final GroupService groupService;
	private final FacultyService facultyService;
	private final StudentService studentService;

	@GetMapping(LIST)
	public String findAll(Model model) {

		log.debug(GET_MAP + GROUP + LIST);
		model.addAttribute(_GROUPS, groupService.findAll());

		return GROUP + LIST;
	}

	@GetMapping(SAVE_FORM)
	public String save(Model model) {

		log.debug(GET_MAP + GROUP + SAVE_FORM);
		Group group = new Group();
		group.setId(null);

		model.addAttribute(_GROUP, group);
		model.addAttribute(_FACULTIES, facultyService.findAll());

		return GROUP + FORM;
	}

	@GetMapping(UPDATE_FORM)
	public String updateGroup(@RequestParam(_GROUP_ID) Integer id, Model model) {

		log.debug(GET_MAP + GROUP + UPDATE_FORM);

		if (id != null) {
			model.addAttribute(_GROUP, groupService.findById(id));
			model.addAttribute(_FACULTIES, facultyService.findAll());
			return GROUP + FORM;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping("/addStudent")
	public String addStudent(@RequestParam(_GROUP_ID) Integer groupId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, BindingResult theBindingResult,
			RedirectAttributes redirectAttributes) {
		log.debug(POST_MAP + GROUP + "/addStudent");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute(_GROUP_ID, groupId);
			return REDIRECT + GROUP + DETAILS;
		}
		if (groupId != null && crmObject != null) {
			Integer studentId = crmObject.getId();
			groupService.addStudent(groupId, studentId);
			redirectAttributes.addAttribute(_GROUP_ID, groupId);
			return REDIRECT + GROUP + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DETAILS)
	public String showDetails(@RequestParam(_GROUP_ID) Integer id, Model model) {

		log.debug(GET_MAP + GROUP + DETAILS);
		if (id != null) {
			Group group = groupService.findById(id);
			model.addAttribute(_GROUP, group);
			model.addAttribute("myFaculty", facultyService.findById(group.getFacultyId()));
			model.addAttribute("myStudents", studentService.findByGroup(group));
			model.addAttribute("allStudents", studentService.findAll());
			model.addAttribute("crmObject", new CrmObject());
			return GROUP + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping(SAVE_GROUP)
	public String saveGroup(@Valid @ModelAttribute(_GROUP) Group group, BindingResult theBindingResult, Model model) {
		log.debug(POST_MAP + GROUP + SAVE_GROUP);

		if (theBindingResult.hasErrors()) {
			model.addAttribute(_GROUP, group);
			model.addAttribute(_FACULTIES, facultyService.findAll());
			return GROUP + FORM;
		}
		if (group != null) {
			groupService.saveOrUpdate(group);
			return REDIRECT + GROUP + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}

	}

	@GetMapping(DELETE)
	public String deleteGroup(@RequestParam(_GROUP_ID) Integer id) {

		log.debug(GET_MAP + GROUP + DELETE);
		if (id != null) {
			groupService.deleteById(id);
			return REDIRECT + GROUP + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/deleteFromFaculty")
	public String deleteFromFaculty(@RequestParam(_GROUP_ID) Integer groupId,
			@RequestParam(_FACULTY_ID) Integer facultyId, RedirectAttributes redirectAttributes) {

		log.debug(GET_MAP + GROUP + "/deleteFromFaculty");
		if (groupId != null && facultyId != null) {
			groupService.deleteGroupFromFaculty(groupId);
			redirectAttributes.addAttribute(_FACULTY_ID, facultyId);
			return REDIRECT + FACULTY + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping("/deleteFromLecture")
	public String deleteFromLecture(@RequestParam(_GROUP_ID) Integer groupId,
			@RequestParam(_LECTURE_ID) Integer lectureId, RedirectAttributes redirectAttributes) {

		log.debug(GET_MAP + GROUP + "/deleteFromLecture");
		if (groupId != null && lectureId != null) {
			groupService.deleteGroupFromLecture(groupId, lectureId);
			redirectAttributes.addAttribute(_LECTURE_ID, lectureId);
			return REDIRECT + LECTURE + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DELETE_ALL)
	public String deleteAll() {

		log.debug(GET_MAP + GROUP + DELETE_ALL);
		groupService.deleteAll();

		return REDIRECT + GROUP + LIST;
	}

	@GetMapping(SEARCH)
	public String search(@RequestParam(NAME) String name, Model model) {

		log.debug(GET_MAP + GROUP + SEARCH);
		if (name != null && !name.trim().isEmpty()) {
			List<Group> groups = groupService.searchByName(name);
			if (groups.isEmpty()) {
				return REDIRECT + GROUP + LIST;
			} else {
				model.addAttribute(_GROUPS, groups);
				return GROUP + LIST;
			}
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}
}