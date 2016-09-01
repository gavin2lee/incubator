package com.lachesis.mnisqm.core;

/**
 * 操作不被允许时抛出此错误
 * 
 * @author Paul Xu.
 * @since 1.0
 */
public class UserInvalidInputException extends CommRuntimeException {
	private static final long serialVersionUID = 1L;

	public UserInvalidInputException(final String message) {
		super(message);
	}

	public UserInvalidInputException(final Throwable e) {
		super(e);
	}

	public UserInvalidInputException(String message, Throwable e) {
		super(message, e);
	}
}
