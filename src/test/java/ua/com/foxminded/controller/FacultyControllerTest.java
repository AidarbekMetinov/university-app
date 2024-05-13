package ua.com.foxminded.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.FACULTY;
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
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.TeacherService;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest extends TestUtil {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FacultyService service;
	@MockBean
	private GroupService groupService;
	@MockBean
	private TeacherService teacherService;

	@BeforeAll
	static void init() {
		createFaculties();
	}

	@Test
	void methodFindAllShouldPass() throws Exception {

		when(service.findAll()).thenReturn(faculties);

		getRequestContains(mockMvc, FACULTY + LIST, FACULTIES);

		verify(service, times(ONE)).findAll();
	}

	@Test
	void methodSaveFormShouldPass() throws Exception {
		String[] contents = { "Save Faculty", "id=\"id\" name=\"id\" value=\"\"",
				"placeholder=\"Name\" id=\"name\" name=\"name\" value=\"\"" };

		getRequestContains(mockMvc, FACULTY + SAVE_FORM, contents);
	}

	@Test
	void methodUpdateFormShouldPass() throws Exception {
		int id = ONE;
		String[] contents = { "Save Faculty", "id=\"id\" name=\"id\" value=\"" + id + "\"",
				"placeholder=\"Name\" id=\"name\" name=\"name\" value=\"Economic\"" };

		when(service.findById(id)).thenReturn(faculties.get(FIRST));

		getRequestContains(mockMvc, FACULTY + UPDATE_FORM + BY_FACULTY_ID + id, contents);

		verify(service, times(ONE)).findById(id);
	}

	@Test
	void methodDeleteShouldPass() throws Exception {
		int id = ONE;

		getRequestRedirection(mockMvc, FACULTY + DELETE + BY_FACULTY_ID + id);

		verify(service, times(ONE)).deleteById(id);
	}

	@Test
	void methodSearchShouldPass() throws Exception {

		String facultyName = "Economic";

		String[] contents = { facultyName, "<td>An economist is a specialist in the field of economics",
				"facultyId=1" };

		List<Faculty> mathInList = faculties.stream().filter(faculty -> faculty.getName() == facultyName)
				.collect(Collectors.toList());

		when(service.searchByName(facultyName)).thenReturn(mathInList);

		getRequestContains(mockMvc, FACULTY + SEARCH + BY_NAME + facultyName, contents);

		verify(service, times(ONE)).searchByName(facultyName);
	}
}
