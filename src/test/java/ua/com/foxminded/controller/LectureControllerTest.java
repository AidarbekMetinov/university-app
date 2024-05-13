package ua.com.foxminded.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.LECTURE;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.UPDATE_FORM;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.TestUtil;
import ua.com.foxminded.service.AudienceService;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.TeacherService;

@WebMvcTest(LectureController.class)
class LectureControllerTest extends TestUtil {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LectureService service;
	@MockBean
	private AudienceService audienceService;
	@MockBean
	private TeacherService teacherService;
	@MockBean
	private CourseService courseService;
	@MockBean
	private GroupService groupService;

	@BeforeAll
	static void init() {
		createLectures();
	}

	@Test
	void methodFindAllShouldPass() throws Exception {

		when(service.findAll()).thenReturn(lectures);

		getRequestContains(mockMvc, LECTURE + LIST, LECTURES);

		verify(service, times(ONE)).findAll();
	}

	@Test
	void methodSaveFormShouldPass() throws Exception {
		String[] contents = { "<h3>Save Lecture</h3>", "id=\"id\" name=\"id\" value=\"\"",
				"<option value=\"0\">Select audience</option>" };

		getRequestContains(mockMvc, LECTURE + SAVE_FORM, contents);
	}

	@Test
	void methodUpdateFormShouldPass() throws Exception {
		int id = ONE;
		String[] contents = { "<h3>Save Lecture</h3>", "id=\"id\" name=\"id\" value=\"" + id + "\"",
				"<option value=\"0\">Select audience</option>" };

		when(service.findById(id)).thenReturn(lectures.get(FIRST));

		getRequestContains(mockMvc, LECTURE + UPDATE_FORM + BY_LECTURE_ID + id, contents);

		verify(service, times(ONE)).findById(id);
	}

	@Test
	void methodDeleteShouldPass() throws Exception {
		int id = ONE;

		getRequestRedirection(mockMvc, LECTURE + DELETE + BY_LECTURE_ID + id);

		verify(service, times(ONE)).deleteById(id);
	}
}
