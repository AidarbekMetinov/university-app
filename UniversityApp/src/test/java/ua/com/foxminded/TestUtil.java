package ua.com.foxminded;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ua.com.foxminded.entity.Audience;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.entity.Teacher;

public class TestUtil {

	protected static final String[] AUDIENCES = { "audienceId=1", "audienceId=2", "audienceId=3" };
	protected static final String[] COURSES = { "courseId=1", "courseId=2", "courseId=3" };
	protected static final String[] FACULTIES = { "facultyId=1", "facultyId=2", "facultyId=3" };
	protected static final String[] GROUPS = { "groupId=1", "groupId=2", "groupId=3" };
	protected static final String[] LECTURES = { "lectureId=1", "lectureId=2", "lectureId=3" };
	protected static final String[] STUDENTS = { "studentId=1", "studentId=2", "studentId=3" };
	protected static final String[] TEACHERS = { "teacherId=1", "teacherId=2", "teacherId=3" };

	protected static final String TEXT_HTML_UTF8 = "text/html;charset=UTF-8";

	protected static final String BY_NAME = "?name=";
	protected static final String SEARCH_BY_FIRST_NAME = "/searchByFirstName";
	protected static final String SEARCH_BY_LAST_NAME = "/searchByLastName";
	protected static final String FIRST_NAME = "?firstName=";
	protected static final String LAST_NAME = "?lastName=";

	protected static final String BY_AUDIENCE_ID = "?audienceId=";
	protected static final String BY_COURSE_ID = "?courseId=";
	protected static final String BY_FACULTY_ID = "?facultyId=";
	protected static final String BY_GROUP_ID = "?groupId=";
	protected static final String BY_LECTURE_ID = "?lectureId=";
	protected static final String BY_STUDENT_ID = "?studentId=";
	protected static final String BY_TEACHER_ID = "?teacherId=";

	protected static final int FIRST = 0;
	protected static final int SECOND = 1;
	protected static final int THIRD = 2;

	protected static final int ONE = 1;
	protected static final int TWO = 2;
	protected static final int THREE = 3;

	protected static List<Audience> audiences;
	protected static List<Course> courses;
	protected static List<Faculty> faculties;
	protected static List<Group> groups;
	protected static List<Lecture> lectures;
	protected static List<Student> students;
	protected static List<Teacher> teachers;

	protected static void createAudiences() {
		audiences = new ArrayList<>();
		audiences.add(new Audience(ONE, ONE, 30, true));
		audiences.add(new Audience(TWO, TWO, 30, true));
		audiences.add(new Audience(THREE, THREE, 30, true));
	}

	protected static void createCourses() {
		courses = new ArrayList<>();
		courses.add(new Course(ONE, "Mathematics",
				"Mathematics (knowledge, study, learning) includes the study of such topics as quantity (number theory), structure (algebra), space (geometry), and change (analysis)."));
		courses.add(new Course(TWO, "History",
				"History (inquiry, knowledge acquired by investigation) is the study of the past."));
		courses.add(new Course(THREE, "Geography",
				"Geography (\"earth description\") is a field of science devoted to the study of the lands, features, inhabitants, and phenomena of the Earth and planets."));
	}

	protected static void createFaculties() {
		faculties = new ArrayList<>();
		faculties.add(new Faculty(ONE, "Economic",
				"An economist is a specialist in the field of economics, economic expert. "
						+ "Economists are called scientists (that is, specialists in the field of economic science), "
						+ "and practitioners who work in the field of research, planning and management of the economic "
						+ "activities of the enterprise. By the way, in 2021, the ProfGid vocational guidance center "
						+ "developed an accurate vocational guidance test. He himself will tell you which professions "
						+ "suit you, will give an opinion about your personality type and intelligence.",
				null, null));
		faculties.add(new Faculty(TWO, "Juristical", "Juristical education is a body of knowledge about the state, "
				+ "management, law, the presence of which gives rise to the professional practice of legal activity. ",
				null, null));
		faculties.add(new Faculty(THREE, "Historical",
				"If you are interested in the past since childhood, you can easily "
						+ "remember dates and like to explore the causes of certain events, you should think about the profession "
						+ "of a historian. These are specialists who can restore the picture of the past of different countries "
						+ "and even of all mankind.",
				null, null));
	}

	protected static void createGroups() {
		groups = new ArrayList<>();
		groups.add(new Group(ONE, "Economic1", ONE, null));
		groups.add(new Group(TWO, "Juristical1", TWO, null));
		groups.add(new Group(THREE, "Historical1", THREE, null));
	}

	protected static void createLectures() {
		createCourses();
		createAudiences();
		createTeachers();
		createGroups();
		lectures = new ArrayList<>();
		lectures.add(new Lecture(ONE, courses.get(FIRST), audiences.get(FIRST), teachers.get(FIRST),
				LocalDateTime.now(), groups));
		lectures.add(new Lecture(TWO, courses.get(SECOND), audiences.get(SECOND), teachers.get(SECOND),
				LocalDateTime.now(), groups));
		lectures.add(new Lecture(THREE, courses.get(THIRD), audiences.get(THIRD), teachers.get(THIRD),
				LocalDateTime.now(), groups));
	}

	protected static void createStudents() {
		students = new ArrayList<>();
		students.add(new Student(ONE, "Adele", "Itzkovsky", "aitzkovsky0@biblegateway.com", ONE));
		students.add(new Student(TWO, "Robinett", "Meert", "rmeert1@joomla.org", TWO));
		students.add(new Student(THREE, "Casar", "Feld", "cfeld2@typepad.com", THREE));
	}

	protected static void createTeachers() {
		teachers = new ArrayList<>();
		teachers.add(new Teacher(ONE, "Orel", "Wollen", null));
		teachers.add(new Teacher(TWO, "Valencia", "Purvess", null));
		teachers.add(new Teacher(THREE, "Afton", "Shortland", null));
	}

	protected static void getRequestContains(MockMvc mockMvc, String webRequest, String[] expectedContents)
			throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(webRequest)//
				.accept(MediaType.parseMediaType(TEXT_HTML_UTF8)))//
				.andExpect(status().isOk())//
				.andExpect(content().contentType(TEXT_HTML_UTF8))//
				.andExpect(content().string(allOf(//
						containsString(expectedContents[FIRST]), //
						containsString(expectedContents[SECOND]), //
						containsString(expectedContents[THIRD]))));
	}

	protected static void getRequestRedirection(MockMvc mockMvc, String webRequest) throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(webRequest)//
				.accept(MediaType.parseMediaType(TEXT_HTML_UTF8)))//
				.andExpect(status().is3xxRedirection());
	}
}
