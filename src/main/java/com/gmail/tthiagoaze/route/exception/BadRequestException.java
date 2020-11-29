package com.gmail.tthiagoaze.route.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RouteException {

	private static final long serialVersionUID = -19327799843604496L;

	public BadRequestException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}

}
