package ua.com.foxminded.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ua.com.foxminded.exception.NotFoundException;

@ControllerAdvice
public class RequestExceptionHandler {

	@ExceptionHandler
	public String handleException(NotFoundException exc, Model model) {

		ErrorResponse error = new ErrorResponse();

		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(LocalDateTime.now());

		model.addAttribute("errorResponce", error);

		return "error-page";
	}

	@ExceptionHandler
	public String handleException(Exception exc, Model model) {

		ErrorResponse error = new ErrorResponse();

		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(LocalDateTime.now());

		model.addAttribute("errorResponce", error);

		return "error-page";
	}
}
