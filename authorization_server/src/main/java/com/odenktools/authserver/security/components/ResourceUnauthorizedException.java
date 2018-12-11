package com.odenktools.authserver.security.components;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ResourceUnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 712340337635384332L;

	public ResourceUnauthorizedException() {

	}

	public ResourceUnauthorizedException(String message) {

		super(message);
	}
}
