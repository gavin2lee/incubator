package com.lachesis.mnis.core.exception;

public class MnisException extends RuntimeException {
	private static final long serialVersionUID = -6449453351284794982L;

	public MnisException(String message) {
		super(message);
	}

	public MnisException(String message, Throwable cause) {
		super(message, cause);
	}

}
