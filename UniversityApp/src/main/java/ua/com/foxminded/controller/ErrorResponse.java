package ua.com.foxminded.controller;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private Integer status;
	private String message;
	private LocalDateTime timeStamp;
}
