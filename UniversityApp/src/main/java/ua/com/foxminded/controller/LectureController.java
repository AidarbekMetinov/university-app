package ua.com.foxminded.controller;

import java.time.LocalDateTime;
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
import ua.com.foxminded.crm.CrmLecture;
import ua.com.foxminded.crm.CrmObject;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.AudienceService;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.TeacherService;

@Slf4j
@Controller
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

	private final LectureService lectureService;
	private final AudienceService audienceService;
	private final TeacherService teacherService;
	private final CourseService courseService;
	private final GroupService groupService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/list")
	public String findAll(Model model) {

		log.debug("Get mapping:/lecture/list");
		model.addAttribute("dateTime", LocalDateTime.now());
		model.addAttribute("lectures", lectureService.findAll());

		return "lecture/list-lectures";
	}

	@GetMapping("/formForSave")
	public String save(Model model) {

		log.debug("Get mapping:/lecture/formForSave");
		CrmLecture lecture = new CrmLecture();
		lecture.setDateTime(LocalDateTime.now().toString());

		model.addAttribute("lecture", lecture);
		model.addAttribute("audiences", audienceService.findAll());
		model.addAttribute("teachers", teacherService.findAll());
		model.addAttribute("courses", courseService.findAll());

		return "lecture/lecture-form";
	}

	@GetMapping("/formForUpdate")
	public String updateLecture(@RequestParam("lectureId") Integer id, Model model) {

		log.debug("Get mapping:/lecture/formForUpdate");
		if (id != null) {
			Lecture persist = lectureService.findById(id);
			CrmLecture lecture = new CrmLecture();
			lecture.setId(id);
			lecture.setAudienceId(persist.getAudience().getId());
			lecture.setTeacherId(persist.getTeacher().getId());
			lecture.setCourseId(persist.getCourse().getId());
			lecture.setDateTime(persist.getDateTime().toString());

			model.addAttribute("lecture", lecture);
			model.addAttribute("audiences", audienceService.findAll());
			model.addAttribute("teachers", teacherService.findAll());
			model.addAttribute("courses", courseService.findAll());

			return "lecture/lecture-form";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/addGroup")
	public String addGroupToLecture(@RequestParam("lectureId") Integer lectureId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, BindingResult theBindingResult,
			RedirectAttributes redirectAttributes) {
		log.debug("Get mapping:/lecture/formForUpdate");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute("lectureId", lectureId);

			return "redirect:/lecture/details";
		}
		if (lectureId != null && crmObject != null) {
			Integer groupId = crmObject.getId();

			lectureService.addGroupToLecture(lectureId, groupId);
			redirectAttributes.addAttribute("lectureId", lectureId);

			return "redirect:/lecture/details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@GetMapping("/details")
	public String showDetails(@RequestParam("lectureId") Integer id, Model model) {

		log.debug("Get mapping:/group/details");
		if (id != null) {
			Lecture lecture = lectureService.findById(id);
			model.addAttribute("lecture", lecture);
			model.addAttribute("myGroups", groupService.findByLecture(lecture));
			model.addAttribute("allGroups", groupService.findAll());
			model.addAttribute("crmObject", new CrmObject());
			return "lecture/lecture-details";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}

	@PostMapping("/saveLecture")
	public String saveLecture(@Valid @ModelAttribute("lecture") CrmLecture lecture, BindingResult theBindingResult,
			Model model) {
		log.debug("Post mapping:/lecture/saveLecture");

		if (theBindingResult.hasErrors()) {
			model.addAttribute("lecture", lecture);
			model.addAttribute("audiences", audienceService.findAll());
			model.addAttribute("teachers", teacherService.findAll());
			model.addAttribute("courses", courseService.findAll());

			return "lecture/lecture-form";
		}
		if (lecture != null) {
			lectureService.saveOrUpdate(lecture);
			return "redirect:/lecture/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}

	}

	@GetMapping("/delete")
	public String deleteLecture(@RequestParam("lectureId") Integer id) {

		log.debug("Get mapping:/lecture/delete");
		if (id != null) {
			lectureService.deleteById(id);
			return "redirect:/lecture/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}

	}

	@GetMapping("/deleteAll")
	public String deleteAll() {

		log.debug("Get mapping:/lecture/deleteAll");
		lectureService.deleteAll();

		return "redirect:/lecture/list";
	}

	@GetMapping("/search")
	public String search(@RequestParam("dateTime") String dateTime, Model model) {

		LocalDateTime dt = LocalDateTime.parse(dateTime);

		log.debug("Get mapping:/lecture/search");
		if (dateTime != null) {
			List<Lecture> lectures = lectureService.findByDate(dt);
			if (lectures.isEmpty()) {
				return "redirect:/lecture/list";
			} else {
				model.addAttribute("lectures", lectures);
				return "lecture/list-lectures";
			}
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}
	}
}
