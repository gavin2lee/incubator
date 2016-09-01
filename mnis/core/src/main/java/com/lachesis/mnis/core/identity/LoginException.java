package com.lachesis.mnis.core.identity;

public class LoginException extends Exception {

	private static final long serialVersionUID = -765957313185979169L;
	
	public static final int UNKNOWN_EXCEPTION_CODE = -99; 
	
	private int exceptionCode;

	public LoginException(int exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	public LoginException(int exceptionCode, String message, Throwable cause) {
		super(message, cause);
		this.exceptionCode = exceptionCode;
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
		this.exceptionCode = UNKNOWN_EXCEPTION_CODE;
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	
	
}