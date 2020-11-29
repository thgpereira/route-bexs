package com.gmail.tthiagoaze.route.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends RouteException {

    private static final long serialVersionUID = -19327799843604496L;

    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
