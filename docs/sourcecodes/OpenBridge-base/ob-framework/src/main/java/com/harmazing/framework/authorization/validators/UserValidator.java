package com.harmazing.framework.authorization.validators;

import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.IValidator;
import com.harmazing.framework.authorization.ValidatorContext;

@Component("userValidator")
public class UserValidator implements IValidator {

	@Override
	public boolean validate(ValidatorContext validatorContext) throws Exception {
		IUser user = validatorContext.getUser();
		if (user.isAnonymous()) {
			return false;
		}
		return true;
	}
}