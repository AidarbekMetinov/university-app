package ua.com.foxminded.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.Constants.AUDIENCE;
import static ua.com.foxminded.Constants.DELETE;
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

@WebMvcTest(AudienceController.class)
class AudienceControllerTest extends TestUtil {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AudienceService service;

	@BeforeAll
	static void init() {
		createAudiences();
	}

	@Test
	void methodFindAllShouldPass() throws Exception {

		when(service.findAll()).thenReturn(audiences);

		getRequestContains(mvc, AUDIENCE + LIST, AUDIENCES);

		verify(service, times(ONE)).findAll();
	}

	@Test
	void methodSaveFormShouldPass() throws Exception {
		String[] contents = { "id=\"id\" name=\"id\" value=\"\"",
				"id=\"audienceNumber\" name=\"audienceNumber\" value=\"", "id=\"seats\" name=\"seats\" value=\"\"" };

		getRequestContains(mvc, AUDIENCE + SAVE_FORM, contents);
	}

	@Test
	void methodUpdateFormShouldPass() throws Exception {
		int id = ONE;
		String[] contents = { "id=\"id\" name=\"id\" value=\"" + id + "\"",
				"id=\"audienceNumber\" name=\"audienceNumber\" value=\"" + id + "\"",
				"id=\"seats\" name=\"seats\" value=\"30\"" };

		when(service.findById(id)).thenReturn(audiences.get(FIRST));

		getRequestContains(mvc, AUDIENCE + UPDATE_FORM + BY_AUDIENCE_ID + id, contents);

		verify(service, times(ONE)).findById(id);
	}

	@Test
	void methodDeleteShouldPass() throws Exception {
		int id = ONE;

		getRequestRedirection(mvc, AUDIENCE + DELETE + BY_AUDIENCE_ID + id);

		verify(service, times(ONE)).deleteById(id);
	}
}
