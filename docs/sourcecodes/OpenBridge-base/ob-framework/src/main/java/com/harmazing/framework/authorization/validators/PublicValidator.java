package com.harmazing.framework.authorization.validators;

import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IValidator;
import com.harmazing.framework.authorization.ValidatorContext;

@Component("publicValidator")
public class PublicValidator implements IValidator {

	@Override
	public boolean validate(ValidatorContext validatorContext) throws Exception {
		return true;
	}

}
