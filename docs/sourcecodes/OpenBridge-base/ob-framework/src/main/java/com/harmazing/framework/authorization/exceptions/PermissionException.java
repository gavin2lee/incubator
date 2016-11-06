package com.harmazing.framework.authorization.exceptions;

@SuppressWarnings("serial")
public class PermissionException extends AuthorizationException {

	public PermissionException(Exception e) {
		super(e);
	}

	public PermissionException(String e) {
		super(e);
	}

}
