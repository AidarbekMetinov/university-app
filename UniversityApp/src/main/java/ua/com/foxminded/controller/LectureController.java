package ua.com.foxminded.controller;

import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.DELETE_ALL;
import static ua.com.foxminded.Constants.DETAILS;
import static ua.com.foxminded.Constants.FORM;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.GROUP;
import static ua.com.foxminded.Constants.INCOMMING_ERROR;
import static ua.com.foxminded.Constants.LECTURE;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.REDIRECT;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.SAVE_LECTURE;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.UPDATE_FORM;
import static ua.com.foxminded.Constants._AUDIENCES;
import static ua.com.foxminded.Constants._COURSES;
import static ua.com.foxminded.Constants._LECTURE;
import static ua.com.foxminded.Constants._LECTURES;
import static ua.com.foxminded.Constants._LECTURE_ID;
import static ua.com.foxminded.Constants._TEACHERS;

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
@RequestMapping(LECTURE)
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

	@GetMapping(LIST)
	public String findAll(Model model) {

		log.debug(GET_MAP + LECTURE + LIST);
		model.addAttribute("dateTime", LocalDateTime.now());
		model.addAttribute(_LECTURES, lectureService.findAll());

		return LECTURE + LIST;
	}

	@GetMapping(SAVE_FORM)
	public String save(Model model) {

		log.debug(GET_MAP + LECTURE + SAVE_FORM);
		CrmLecture lecture = new CrmLecture();
		lecture.setDateTime(LocalDateTime.now().toString());

		model.addAttribute(_LECTURE, lecture);
		model.addAttribute(_AUDIENCES, audienceService.findAll());
		model.addAttribute(_TEACHERS, teacherService.findAll());
		model.addAttribute(_COURSES, courseService.findAll());

		return LECTURE + FORM;
	}

	@GetMapping(UPDATE_FORM)
	public String updateLecture(@RequestParam(_LECTURE_ID) Integer id, Model model) {

		log.debug(GET_MAP + LECTURE + UPDATE_FORM);
		if (id != null) {
			Lecture persist = lectureService.findById(id);
			CrmLecture lecture = new CrmLecture();
			lecture.setId(id);
			lecture.setAudienceId(persist.getAudience().getId());
			lecture.setTeacherId(persist.getTeacher().getId());
			lecture.setCourseId(persist.getCourse().getId());
			lecture.setDateTime(persist.getDateTime().toString());

			model.addAttribute(_LECTURE, lecture);
			model.addAttribute(_AUDIENCES, audienceService.findAll());
			model.addAttribute(_TEACHERS, teacherService.findAll());
			model.addAttribute(_COURSES, courseService.findAll());

			return LECTURE + FORM;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping("/addGroup")
	public String addGroupToLecture(@RequestParam(_LECTURE_ID) Integer lectureId,
			@Valid @ModelAttribute("crmObject") CrmObject crmObject, BindingResult theBindingResult,
			RedirectAttributes redirectAttributes) {
		log.debug(GET_MAP + LECTURE + "/addGroup");

		if (theBindingResult.hasErrors()) {
			redirectAttributes.addAttribute(_LECTURE_ID, lectureId);

			return REDIRECT + LECTURE + DETAILS;
		}
		if (lectureId != null && crmObject != null) {
			Integer groupId = crmObject.getId();

			lectureService.addGroupToLecture(lectureId, groupId);
			redirectAttributes.addAttribute(_LECTURE_ID, lectureId);

			return REDIRECT + LECTURE + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@GetMapping(DETAILS)
	public String showDetails(@RequestParam(_LECTURE_ID) Integer id, Model model) {

		log.debug(GET_MAP + GROUP + DETAILS);
		if (id != null) {
			Lecture lecture = lectureService.findById(id);
			model.addAttribute(_LECTURE, lecture);
			model.addAttribute("myGroups", lecture.getGroups());
			model.addAttribute("allGroups", groupService.findAll());
			model.addAttribute("crmObject", new CrmObject());
			return LECTURE + DETAILS;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}

	@PostMapping(SAVE_LECTURE)
	public String saveLecture(@Valid @ModelAttribute(_LECTURE) CrmLecture lecture, BindingResult theBindingResult,
			Model model) {
		log.debug(POST_MAP + LECTURE + SAVE_LECTURE);

		if (theBindingResult.hasErrors()) {
			model.addAttribute(_LECTURE, lecture);
			model.addAttribute(_AUDIENCES, audienceService.findAll());
			model.addAttribute(_TEACHERS, teacherService.findAll());
			model.addAttribute(_COURSES, courseService.findAll());

			return LECTURE + FORM;
		}
		if (lecture != null) {
			lectureService.saveOrUpdate(lecture);
			return REDIRECT + LECTURE + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}

	}

	@GetMapping(DELETE)
	public String deleteLecture(@RequestParam(_LECTURE_ID) Integer id) {

		log.debug(GET_MAP + LECTURE + DELETE);
		if (id != null) {
			lectureService.deleteById(id);
			return REDIRECT + LECTURE + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}

	}

	@GetMapping(DELETE_ALL)
	public String deleteAll() {

		log.debug(GET_MAP + LECTURE + DELETE_ALL);
		lectureService.deleteAll();

		return REDIRECT + LECTURE + LIST;
	}

	@GetMapping(SEARCH)
	public String search(@RequestParam("dateTime") String dateTime, Model model) {

		LocalDateTime dt = LocalDateTime.parse(dateTime);

		log.debug(GET_MAP + LECTURE + SEARCH);
		if (dateTime != null) {
			List<Lecture> lectures = lectureService.findByDate(dt);
			if (lectures.isEmpty()) {
				return REDIRECT + LECTURE + LIST;
			} else {
				model.addAttribute(_LECTURES, lectures);
				return LECTURE + LIST;
			}
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}
	}
}
