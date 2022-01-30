package ua.com.foxminded.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.STUDENT;
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
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest extends TestUtil {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StudentService service;
	@MockBean
	private GroupService groupService;

	@BeforeAll
	static void init() {
		createStudents();
	}

	@Test
	void methodFindAllShouldPass() throws Exception {

		when(service.findAll()).thenReturn(students);

		getRequestContains(mockMvc, STUDENT + LIST, STUDENTS);

		verify(service, times(ONE)).findAll();
	}

	@Test
	void methodSaveFormShouldPass() throws Exception {
		String[] contents = { "<h3>Save Student</h3>", "id=\"id\" name=\"id\" value=\"\"",
				"placeholder=\"First name\" id=\"firstName\" name=\"firstName\" value=\"\"" };

		getRequestContains(mockMvc, STUDENT + SAVE_FORM, contents);
	}

	@Test
	void methodUpdateFormShouldPass() throws Exception {
		int id = ONE;
		String[] contents = { "<h3>Save Student</h3>", "id=\"id\" name=\"id\" value=\"" + id + "\"",
				"placeholder=\"First name\" id=\"firstName\" name=\"firstName\" value=\"Adele\"" };

		when(service.findById(id)).thenReturn(students.get(FIRST));

		getRequestContains(mockMvc, STUDENT + UPDATE_FORM + BY_STUDENT_ID + id, contents);

		verify(service, times(ONE)).findById(id);
	}

	@Test
	void methodDeleteShouldPass() throws Exception {
		int id = ONE;

		getRequestRedirection(mockMvc, STUDENT + DELETE + BY_STUDENT_ID + id);

		verify(service, times(ONE)).deleteById(id);
	}

	@Test
	void methodSearchByFirstNameShouldPass() throws Exception {

		String firstName = "Casar";

		String[] contents = { firstName, "Feld", "cfeld2@typepad.com" };

		List<Student> studentInList = students.stream().filter(student -> student.getFirstName() == firstName)
				.collect(Collectors.toList());

		when(service.searchByFirstName(firstName)).thenReturn(studentInList);

		getRequestContains(mockMvc, STUDENT + SEARCH_BY_FIRST_NAME + FIRST_NAME + firstName, contents);

		verify(service, times(ONE)).searchByFirstName(firstName);
	}

	@Test
	void methodSearchByLastNameShouldPass() throws Exception {

		String lastName = "Meert";

		String[] contents = { lastName, "Robinett", "rmeert1@joomla.org" };

		List<Student> studentInList = students.stream().filter(student -> student.getLastName() == lastName)
				.collect(Collectors.toList());

		when(service.searchByLastName(lastName)).thenReturn(studentInList);

		getRequestContains(mockMvc, STUDENT + SEARCH_BY_LAST_NAME + LAST_NAME + lastName, contents);

		verify(service, times(ONE)).searchByLastName(lastName);
	}
}
