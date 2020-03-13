package com.shridhar.auth.exception;

public class LoginFailedException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public LoginFailedException() {
	}

	public LoginFailedException(String message) {
		super(message);
	}

}
