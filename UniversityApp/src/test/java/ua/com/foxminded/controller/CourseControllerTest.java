package ua.com.foxminded.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.Constants.COURSE;
import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.SEARCH;
import static ua.com.foxminded.Constants.UPDATE_FORM;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.TestUtil;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.service.CourseService;

@WebMvcTest(CourseController.class)
class CourseControllerTest extends TestUtil {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CourseService service;

	@BeforeAll
	static void init() {
		createCourses();
	}

	@Test
	void methodFindAllShouldPass() throws Exception {

		when(service.findAll()).thenReturn(courses);

		getRequestContains(mockMvc, COURSE + LIST, COURSES);

		verify(service, times(ONE)).findAll();
	}

	@Test
	void methodSaveFormShouldPass() throws Exception {
		String[] contents = { "Save Course", "id=\"id\" name=\"id\" value=\"\"",
				"id=\"name\" name=\"name\" value=\"\"" };

		getRequestContains(mockMvc, COURSE + SAVE_FORM, contents);
	}

	@Test
	void methodUpdateFormShouldPass() throws Exception {
		int id = ONE;
		String[] contents = { "Save Course", "id=\"id\" name=\"id\" value=\"" + id + "\"",
				"id=\"name\" name=\"name\" value=\"Mathematics\"" };

		when(service.findById(id)).thenReturn(courses.get(FIRST));

		getRequestContains(mockMvc, COURSE + UPDATE_FORM + BY_COURSE_ID + id, contents);

		verify(service, times(ONE)).findById(id);
	}

	@Test
	void methodDeleteShouldPass() throws Exception {
		int id = ONE;

		getRequestRedirection(mockMvc, COURSE + DELETE + BY_COURSE_ID + id);

		verify(service, times(ONE)).deleteById(id);
	}

	@Test
	void methodSearchShouldPass() throws Exception {

		String courseName = "Mathematics";

		String[] contents = { courseName, "<td>Mathematics (knowledge, study, learning)", "courseId=1" };

		List<Course> mathInList = courses.stream().filter(course -> course.getName() == courseName)
				.collect(Collectors.toList());

		when(service.searchByName(courseName)).thenReturn(mathInList);

		getRequestContains(mockMvc, COURSE + SEARCH + BY_NAME + courseName, contents);

		verify(service, times(ONE)).searchByName(courseName);
	}
}
