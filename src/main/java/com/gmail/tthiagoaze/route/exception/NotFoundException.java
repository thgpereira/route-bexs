package com.gmail.tthiagoaze.route.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RouteException {

	private static final long serialVersionUID = -19327799843604496L;

	public NotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}

}
