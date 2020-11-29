package com.gmail.tthiagoaze.route.exception;

import org.springframework.http.HttpStatus;

public class GenericErrorException extends RouteException {

	private static final long serialVersionUID = -19327799843604496L;

	public GenericErrorException(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
