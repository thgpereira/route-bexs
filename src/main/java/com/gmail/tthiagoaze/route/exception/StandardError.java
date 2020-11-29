package com.gmail.tthiagoaze.route.exception;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class StandardError implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long timesTamp;

	@Getter
	@Setter
	private Integer status;

	@Getter
	@Setter
	private List<String> errors;

	public StandardError(Long timesTamp, Integer status, List<String> errors) {
		super();
		this.timesTamp = timesTamp;
		this.status = status;
		this.errors = errors;
	}

	public StandardError(Long timesTamp, Integer status, String error) {
		this(timesTamp, status, Arrays.asList(error));
	}

}
