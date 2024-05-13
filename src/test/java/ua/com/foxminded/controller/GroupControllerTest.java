package ua.com.foxminded.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.GROUP;
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
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.FacultyService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@WebMvcTest(GroupController.class)
class GroupControllerTest extends TestUtil {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GroupService service;
	@MockBean
	private FacultyService facultyService;
	@MockBean
	private StudentService studentService;

	@BeforeAll
	static void init() {
		createGroups();
	}

	@Test
	void methodFindAllShouldPass() throws Exception {

		when(service.findAll()).thenReturn(groups);

		getRequestContains(mockMvc, GROUP + LIST, GROUPS);

		verify(service, times(ONE)).findAll();
	}

	@Test
	void methodSaveFormShouldPass() throws Exception {
		String[] contents = { "Save Group", "id=\"id\" name=\"id\" value=\"\"",
				"placeholder=\"Name\" id=\"name\" name=\"name\" value=\"\"" };

		getRequestContains(mockMvc, GROUP + SAVE_FORM, contents);
	}

	@Test
	void methodUpdateFormShouldPass() throws Exception {
		int id = ONE;
		String[] contents = { "Save Group", "id=\"id\" name=\"id\" value=\"" + id + "\"",
				"placeholder=\"Name\" id=\"name\" name=\"name\" value=\"Economic1\"" };

		when(service.findById(id)).thenReturn(groups.get(FIRST));

		getRequestContains(mockMvc, GROUP + UPDATE_FORM + BY_GROUP_ID + id, contents);

		verify(service, times(ONE)).findById(id);
	}

	@Test
	void methodDeleteShouldPass() throws Exception {
		int id = ONE;

		getRequestRedirection(mockMvc, GROUP + DELETE + BY_GROUP_ID + id);

		verify(service, times(ONE)).deleteById(id);
	}

	@Test
	void methodSearchShouldPass() throws Exception {

		String groupName = "Economic1";

		String[] contents = { groupName, "groupId=1", "Group Directory" };

		List<Group> groupInList = groups.stream().filter(group -> group.getName() == groupName)
				.collect(Collectors.toList());

		when(service.searchByName(groupName)).thenReturn(groupInList);

		getRequestContains(mockMvc, GROUP + SEARCH + BY_NAME + groupName, contents);

		verify(service, times(ONE)).searchByName(groupName);
	}
}
