package com.lachesis.mnisqm.core;

/**
 * 
 * @author Paul Xu
 * @since 1.0.0
 * 
 */
public class InvalidPasswordException extends UserInvalidInputException {
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException(String message) {
		super(message);
	}

}
