package com.shridhar.auth.exception;

public class UserRegistrationException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public UserRegistrationException() {
	}

	public UserRegistrationException(String message) {
		super(message);
	}

}
