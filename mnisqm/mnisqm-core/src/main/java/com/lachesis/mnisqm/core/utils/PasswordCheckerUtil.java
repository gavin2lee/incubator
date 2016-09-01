package com.lachesis.mnisqm.core.utils;

import com.lachesis.mnisqm.core.InvalidPasswordException;

/**
 * 
 * @author Paul Xu.
 * @since 1.0
 * 
 */
public final class PasswordCheckerUtil {

	private static final String[] partialRegexChecks = { ".*[a-z]+.*", // lower
			".*[A-Z]+.*", // upper
			".*[\\d]+.*", // digits
			".*[@#$%]+.*" // symbols
	};

	public static void checkValidPassword(String password) {
		if (password.length() < 6) {
			throw new InvalidPasswordException(
					"Your password is too short - must be at least 6 characters");
		} else {
			checkPasswordStrength(password);
		}
	}

	private static void checkPasswordStrength(String password) {
		int strengthPercentage = 0;

		if (password.matches(partialRegexChecks[0])) {
			strengthPercentage += 25;
		}
		if (password.matches(partialRegexChecks[1])) {
			strengthPercentage += 25;
		}
		if (password.matches(partialRegexChecks[2])) {
			strengthPercentage += 25;
		}
		if (password.matches(partialRegexChecks[3])) {
			strengthPercentage += 25;
		}

		if (strengthPercentage < 50)
			throw new InvalidPasswordException(
					"Password must contain at least one digit letter, one character and one symbol");
	}
}