package ua.com.foxminded.controller;

import static ua.com.foxminded.Constants.AUDIENCE;
import static ua.com.foxminded.Constants.DELETE;
import static ua.com.foxminded.Constants.DELETE_ALL;
import static ua.com.foxminded.Constants.FORM;
import static ua.com.foxminded.Constants.GET_MAP;
import static ua.com.foxminded.Constants.INCOMMING_ERROR;
import static ua.com.foxminded.Constants.LIST;
import static ua.com.foxminded.Constants.POST_MAP;
import static ua.com.foxminded.Constants.REDIRECT;
import static ua.com.foxminded.Constants.SAVE_AUDIENCE;
import static ua.com.foxminded.Constants.SAVE_FORM;
import static ua.com.foxminded.Constants.UPDATE_FORM;
import static ua.com.foxminded.Constants._AUDIENCE;
import static ua.com.foxminded.Constants._AUDIENCES;
import static ua.com.foxminded.Constants._AUDIENCE_ID;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.entity.Audience;
import ua.com.foxminded.service.AudienceService;

@Slf4j
@Controller
@RequestMapping(AUDIENCE)
@RequiredArgsConstructor
public class AudienceController {

	private final AudienceService service;

	@GetMapping(LIST)
	public String findAll(Model model) {

		log.debug(GET_MAP + AUDIENCE + LIST);
		model.addAttribute(_AUDIENCES, service.findAll());

		return AUDIENCE + LIST;
	}

	@GetMapping(SAVE_FORM)
	public String saveForm(Model model) {

		log.debug(GET_MAP + AUDIENCE + SAVE_FORM);
		Audience audience = new Audience();
		audience.setId(null);

		model.addAttribute(_AUDIENCE, audience);

		return AUDIENCE + FORM;
	}

	@GetMapping(UPDATE_FORM)
	public String updateForm(@RequestParam(_AUDIENCE_ID) Integer id, Model model) {

		log.debug(GET_MAP + AUDIENCE + UPDATE_FORM);
		if (id != null) {
			model.addAttribute(_AUDIENCE, service.findById(id));
			return AUDIENCE + FORM;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}

	}

	@PostMapping(SAVE_AUDIENCE)
	public String saveAudience(@Valid @ModelAttribute(_AUDIENCE) Audience audience, BindingResult theBindingResult,
			Model model) {
		log.debug(POST_MAP + AUDIENCE + SAVE_AUDIENCE);

		if (theBindingResult.hasErrors()) {
			model.addAttribute(_AUDIENCE, audience);
			return AUDIENCE + FORM;
		}
		if (audience != null) {
			service.saveOrUpdate(audience);
			return REDIRECT + AUDIENCE + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}

	}

	@GetMapping(DELETE)
	public String deleteAudience(@RequestParam(_AUDIENCE_ID) Integer id) {

		log.debug(GET_MAP + AUDIENCE + DELETE);
		if (id != null) {
			service.deleteById(id);
			return REDIRECT + AUDIENCE + LIST;
		} else {
			throw new IllegalArgumentException(INCOMMING_ERROR);
		}

	}

	@GetMapping(DELETE_ALL)
	public String deleteAll() {

		log.debug(GET_MAP + AUDIENCE + DELETE_ALL);
		service.deleteAll();

		return REDIRECT + AUDIENCE + LIST;
	}
}