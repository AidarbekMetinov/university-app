package ua.com.foxminded.controller;

import static ua.com.foxminded.Constants.AUDIENCE;
import static ua.com.foxminded.Constants.COURSE;
import static ua.com.foxminded.Constants.FACULTY;
import static ua.com.foxminded.Constants.GROUP;
import static ua.com.foxminded.Constants.LECTURE;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.STUDENT;
import static ua.com.foxminded.Constants.TEACHER;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.TestUtil;
import ua.com.foxminded.tool.UniversityManager;

@SpringBootTest
@TestPropertySource(locations = { "classpath:application-test.properties" })
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ControllerSystemTest extends TestUtil {

	@Autowired
	private MockMvc mockMvc;

	private UniversityManager universityManager;

	@Autowired
	private ControllerSystemTest(UniversityManager universityManager) {
		this.universityManager = universityManager;
		this.universityManager.runSqlScripts();
	}

	@Test
	void audiencesFromDatabaseAreAvailableOnWeb() throws Exception {
		dataFromTheDatabaseIsAvailableOnWeb(AUDIENCE + LIST, AUDIENCES);
	}

	@Test
	void coursesFromDatabaseAreAvailableOnWeb() throws Exception {
		dataFromTheDatabaseIsAvailableOnWeb(COURSE + LIST, COURSES);
	}

	@Test
	void facultiesFromDatabaseAreAvailableOnWeb() throws Exception {
		dataFromTheDatabaseIsAvailableOnWeb(FACULTY + LIST, FACULTIES);
	}

	@Test
	void groupsFromDatabaseAreAvailableOnWeb() throws Exception {
		dataFromTheDatabaseIsAvailableOnWeb(GROUP + LIST, GROUPS);
	}

	@Test
	void lecturesFromDatabaseAreAvailableOnWeb() throws Exception {
		dataFromTheDatabaseIsAvailableOnWeb(LECTURE + LIST, LECTURES);
	}

	@Test
	void studentsFromDatabaseAreAvailableOnWeb() throws Exception {
		dataFromTheDatabaseIsAvailableOnWeb(STUDENT + LIST, STUDENTS);
	}

	@Test
	void teachersFromDatabaseAreAvailableOnWeb() throws Exception {
		dataFromTheDatabaseIsAvailableOnWeb(TEACHER + LIST, TEACHERS);
	}

	private void dataFromTheDatabaseIsAvailableOnWeb(String webRequest, String[] expectedData) throws Exception {
		getRequestContains(mockMvc, webRequest, expectedData);
	}
}
