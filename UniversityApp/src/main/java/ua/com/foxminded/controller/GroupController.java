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
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.FacultyService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@Slf4j
@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

	private final GroupService groupService;
	private final FacultyService facultyService;
	private final StudentService studentService;

	@GetMapping("/list")
	public String findAll(Model model) {

		log.debug("Get mapping:/group/list");
		model.addAttribute("groups", groupService.findAll());

		return "group/list-groups";
	}

	@GetMapping("/formForSave")
	public String save(Model model) {

		log.debug("Get mapping:/group/formForSave");
		Group group = new Group();
		group.setId(null);

		model.addAttribute("group", group);
		model.addAttribute("faculties", facultyService.findAll());

		return "group/group-form";
	}

	@GetMapping("/formForUpdate")
	public String updateGroup(@RequestParam("groupId") Integer id, Model model) {

		log.debug("Get mapping:/group/formForUpdate");

		if (id != null) {
			model.addAttribute("group", groupService.findById(id));
			model.addAttribute("faculties", facultyService.findAll());
			return "group/group-form";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/addStudent")
	public String addStudent(@RequestParam("groupId") Integer groupId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, BindingResult theBindingResult,
			RedirectAttributes redirectAttributes) {
		log.debug("Post mapping:/group/addStudent");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute("groupId", groupId);
			return "redirect:/group/details";
		}
		if (groupId != null && crmObject != null) {
			Integer studentId = crmObject.getId();
			groupService.addStudent(groupId, studentId);
			redirectAttributes.addAttribute("groupId", groupId);
			return "redirect:/group/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/details")
	public String showDetails(@RequestParam("groupId") Integer id, Model model) {

		log.debug("Get mapping:/group/details");
		if (id != null) {
			Group group = groupService.findById(id);
			model.addAttribute("group", group);
			model.addAttribute("myFaculty", facultyService.findById(group.getFacultyId()));
			model.addAttribute("myStudents", studentService.findByGroup(group));
			model.addAttribute("allStudents", studentService.findAll());
			model.addAttribute("crmObject", new CrmObject());
			return "group/group-details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/saveGroup")
	public String saveGroup(@Valid @ModelAttribute("group") Group group, BindingResult theBindingResult, Model model) {
		log.debug("Post mapping:/group/saveGroup");

		if (theBindingResult.hasErrors()) {
			model.addAttribute("group", group);
			model.addAttribute("faculties", facultyService.findAll());
			return "group/group-form";
		}
		if (group != null) {
			groupService.saveOrUpdate(group);
			return "redirect:/group/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}

	}

	@GetMapping("/delete")
	public String deleteGroup(@RequestParam("groupId") Integer id) {

		log.debug("Get mapping:/group/delete");
		if (id != null) {
			groupService.deleteById(id);
			return "redirect:/group/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteFromFaculty")
	public String deleteFromFaculty(@RequestParam("groupId") Integer groupId,
			@RequestParam("facultyId") Integer facultyId, RedirectAttributes redirectAttributes) {

		log.debug("Get mapping:/group/deleteFromFaculty");
		if (groupId != null && facultyId != null) {
			groupService.deleteGroupFromFaculty(groupId);
			redirectAttributes.addAttribute("facultyId", facultyId);
			return "redirect:/faculty/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteFromLecture")
	public String deleteFromLecture(@RequestParam("groupId") Integer groupId,
			@RequestParam("lectureId") Integer lectureId, RedirectAttributes redirectAttributes) {

		log.debug("Get mapping:/group/deleteFromLecture");
		if (groupId != null && lectureId != null) {
			groupService.deleteGroupFromLecture(groupId, lectureId);
			redirectAttributes.addAttribute("lectureId", lectureId);
			return "redirect:/lecture/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/deleteAll")
	public String deleteAll() {

		log.debug("Get mapping:/group/deleteAll");
		groupService.deleteAll();

		return "redirect:/group/list";
	}

	@GetMapping("/search")
	public String search(@RequestParam("name") String name, Model model) {

		log.debug("Get mapping:/group/search");
		if (name != null && !name.trim().isEmpty()) {
			List<Group> groups = groupService.searchByName(name);
			if (groups.isEmpty()) {
				return "redirect:/group/list";
			} else {
				model.addAttribute("groups", groups);
				return "group/list-groups";
			}
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}
}