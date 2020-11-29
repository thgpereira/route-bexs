package com.gmail.tthiagoaze.route.exception;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());
		StandardError error = new StandardError(System.currentTimeMillis(), status.value(), errors);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(RouteException.class)
	public ResponseEntity<StandardError> generalError(RouteException ex, HttpServletRequest request) {
		StandardError error = new StandardError(System.currentTimeMillis(), ex.getHttpStatus().value(),
				ex.getMessage());
		return ResponseEntity.status(ex.getHttpStatus()).body(error);
	}

}
