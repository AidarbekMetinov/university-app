package ua.com.foxminded.rest;

import static java.lang.String.format;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.Constants.API;
import static ua.com.foxminded.Constants.AUDIENCE;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ua.com.foxminded.TestUtil;
import ua.com.foxminded.entity.Audience;
import ua.com.foxminded.service.AudienceService;

@WebMvcTest(AudienceRestController.class)
class AudienceRestControllerTest extends TestUtil {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AudienceService service;

	@BeforeAll
	static void init() {
		createAudiences();
	}

	@Test
	void all() throws Exception {

		when(service.findAll()).thenReturn(audiences);
		Audience first = audiences.get(FIRST);

		mockMvc.perform(MockMvcRequestBuilders.get(API + AUDIENCE))//
				.andExpect(status().isOk())//
				.andExpect(jsonPath(format("$[%d].id", FIRST)).value(first.getId()))//
				.andExpect(jsonPath(format("$[%d].audienceNumber", FIRST)).value(first.getAudienceNumber()))//
				.andExpect(jsonPath(format("$[%d].seats", FIRST)).value(first.getSeats()))//
				.andExpect(jsonPath(format("$[%d].hasProjector", FIRST)).value(first.isHasProjector()));//

		verify(service, times(ONE)).findAll();
	}

	@Test
	void one() throws Exception {
		int id = ONE;
		Audience audience = audiences.get(FIRST);

		when(service.findById(id)).thenReturn(audience);

		mockMvc.perform(MockMvcRequestBuilders.get(API + AUDIENCE + "/" + id))//
				.andExpect(status().isOk())//
				.andExpect(jsonPath("$.id").value(audience.getId()))//
				.andExpect(jsonPath("$.audienceNumber").value(audience.getAudienceNumber()))//
				.andExpect(jsonPath("$.seats").value(audience.getSeats()))//
				.andExpect(jsonPath("$.hasProjector").value(audience.isHasProjector()));//

		verify(service, times(ONE)).findById(id);
	}

	@Test
	void deleteOne() throws Exception {
		int id = ONE;
		Audience audience = audiences.get(FIRST);

		when(service.findById(id)).thenReturn(audience);

		mockMvc.perform(MockMvcRequestBuilders.delete(API + AUDIENCE + "/" + id))//
				.andExpect(status().isOk());

		verify(service, times(ONE)).delete(audience);
	}

	@Test
	void deleteAll() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.delete(API + AUDIENCE))//
				.andExpect(status().isOk());
		verify(service, times(ONE)).deleteAll();
	}

	@Test
	void save() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post(API + AUDIENCE)//
				.contentType(MediaType.APPLICATION_JSON)//
				.accept(MediaType.APPLICATION_JSON)//
				.content(format("{\"audienceNumber\": %d, \"seats\": %d \"hasProjector\": %s}", 4, 40, false)))//
				.andExpect(status().isOk());
	}

	@Test
	void update() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.put(API + AUDIENCE)//
				.contentType(MediaType.APPLICATION_JSON)//
				.accept(MediaType.APPLICATION_JSON)//
				.content("{\"id\": 1, \"audienceNumber\": 10, \"seats\": 40 \"hasProjector\": false}"))//
				.andExpect(status().isOk());
	}
}
