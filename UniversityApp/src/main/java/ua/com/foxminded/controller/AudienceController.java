package ua.com.foxminded.controller;

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
@RequestMapping("/audience")
@RequiredArgsConstructor
public class AudienceController {

	private final AudienceService audienceService;

	@GetMapping("/list")
	public String findAll(Model model) {

		log.debug("Get mapping:/audience/list");
		model.addAttribute("audiences", audienceService.findAll());

		return "audience/list-audiences";
	}

	@GetMapping("/formForSave")
	public String save(Model model) {

		log.debug("Get mapping:/audience/formForSave");
		Audience audience = new Audience();
		audience.setId(null);

		model.addAttribute("audience", audience);

		return "audience/audience-form";
	}

	@GetMapping("/formForUpdate")
	public String updateAudience(@RequestParam("audienceId") Integer id, Model model) {

		log.debug("Get mapping:/audience/formForUpdate");
		if (id != null) {
			model.addAttribute("audience", audienceService.findById(id));
			return "audience/audience-form";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}

	}

	@PostMapping("/saveAudience")
	public String saveAudience(@Valid @ModelAttribute("audience") Audience audience, BindingResult theBindingResult,
			Model model) {
		log.debug("Post mapping:/audience/saveAudience");

		if (theBindingResult.hasErrors()) {
			model.addAttribute("audience", audience);
			return "audience/audience-form";
		}
		if (audience != null) {
			audienceService.saveOrUpdate(audience);
			return "redirect:/audience/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}

	}

	@GetMapping("/delete")
	public String deleteAudience(@RequestParam("audienceId") Integer id) {

		log.debug("Get mapping:/audience/delete");
		if (id != null) {
			audienceService.deleteById(id);
			return "redirect:/audience/list";
		} else {
			throw new IllegalArgumentException("Incomming are incorrect");
		}

	}

	@GetMapping("/deleteAll")
	public String deleteAll() {

		log.debug("Get mapping:/audience/deleteAll");
		audienceService.deleteAll();

		return "redirect:/audience/list";
	}
}