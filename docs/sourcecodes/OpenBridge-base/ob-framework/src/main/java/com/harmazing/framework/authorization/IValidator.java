package com.harmazing.framework.authorization;

public interface IValidator {
	public abstract boolean validate(ValidatorContext validatorContext)
			throws Exception;
}
