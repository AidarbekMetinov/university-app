package ua.com.foxminded.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.TEACHER;
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
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.TeacherService;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest extends TestUtil {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TeacherService service;
	@MockBean
	private CourseService courseService;

	@BeforeAll
	static void init() {
		createTeachers();
	}

	@Test
	void methodFindAllShouldPass() throws Exception {

		when(service.findAll()).thenReturn(teachers);

		getRequestContains(mockMvc, TEACHER + LIST, TEACHERS);

		verify(service, times(ONE)).findAll();
	}

	@Test
	void methodSaveFormShouldPass() throws Exception {
		String[] contents = { "<h3>Save Teacher</h3>",
				"placeholder=\"First name\" id=\"firstName\" name=\"firstName\" value=\"\"",
				"placeholder=\"Last name\" id=\"lastName\" name=\"lastName\" value=\"\"" };

		getRequestContains(mockMvc, TEACHER + SAVE_FORM, contents);
	}

	@Test
	void methodUpdateFormShouldPass() throws Exception {
		int id = ONE;
		String[] contents = { "<h3>Save Teacher</h3>",
				"placeholder=\"First name\" id=\"firstName\" name=\"firstName\" value=\"Orel\"",
				"placeholder=\"Last name\" id=\"lastName\" name=\"lastName\" value=\"Wollen\"" };

		when(service.findById(id)).thenReturn(teachers.get(FIRST));

		getRequestContains(mockMvc, TEACHER + UPDATE_FORM + BY_TEACHER_ID + id, contents);

		verify(service, times(ONE)).findById(id);
	}

	@Test
	void methodDeleteShouldPass() throws Exception {
		int id = ONE;

		getRequestRedirection(mockMvc, TEACHER + DELETE + BY_TEACHER_ID + id);

		verify(service, times(ONE)).deleteById(id);
	}

	@Test
	void methodSearchByFirstNameShouldPass() throws Exception {

		String firstName = "Orel";

		String[] contents = { firstName, "Wollen", "teacherId=1" };

		List<Teacher> teacherInList = teachers.stream().filter(teacher -> teacher.getFirstName() == firstName)
				.collect(Collectors.toList());

		when(service.searchByFirstName(firstName)).thenReturn(teacherInList);

		getRequestContains(mockMvc, TEACHER + SEARCH_BY_FIRST_NAME + FIRST_NAME + firstName, contents);

		verify(service, times(ONE)).searchByFirstName(firstName);
	}

	@Test
	void methodSearchByLastNameShouldPass() throws Exception {

		String lastName = "Purvess";

		String[] contents = { lastName, "Valencia", "teacherId=2" };

		List<Teacher> teacherInList = teachers.stream().filter(teacher -> teacher.getLastName() == lastName)
				.collect(Collectors.toList());

		when(service.searchByLastName(lastName)).thenReturn(teacherInList);

		getRequestContains(mockMvc, TEACHER + SEARCH_BY_LAST_NAME + LAST_NAME + lastName, contents);

		verify(service, times(ONE)).searchByLastName(lastName);
	}
}
