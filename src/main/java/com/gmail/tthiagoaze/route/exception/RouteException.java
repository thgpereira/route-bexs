package com.gmail.tthiagoaze.route.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

public class RouteException extends RuntimeException {

    private static final long serialVersionUID = 2845564415644250873L;

    @Getter
    @Setter
    private final HttpStatus httpStatus;

    public RouteException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
