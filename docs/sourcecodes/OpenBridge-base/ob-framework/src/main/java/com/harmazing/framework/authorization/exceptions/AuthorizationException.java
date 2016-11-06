package com.harmazing.framework.authorization.exceptions;

@SuppressWarnings("serial")
public class AuthorizationException extends Exception {
	public AuthorizationException(Exception e) {
		super(e);
	}

	public AuthorizationException(String e) {
		super(e);
	}
}
