package ua.com.foxminded.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.tool.UniversityManager;

@Controller
public class HomeController {

	UniversityManager universityManager;

	@Autowired
	public HomeController(UniversityManager universityManager) {
		this.universityManager = universityManager;
	}

	@RequestMapping("/")
	public String showDefaultPage() {
		return "index";
	}

	@GetMapping("/refresh")
	public String runSqlScripts() {

		universityManager.runSqlScripts();

		return "redirect:/";
	}
}