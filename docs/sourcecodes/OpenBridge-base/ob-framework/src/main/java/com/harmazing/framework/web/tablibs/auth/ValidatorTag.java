package com.harmazing.framework.web.tablibs.auth;

import com.harmazing.framework.authorization.ValidatorUtil;
import com.harmazing.framework.authorization.exceptions.AuthorizationException;

@SuppressWarnings("serial")
public class ValidatorTag extends AbstractAuthTag {

	@Override
	protected boolean isValidation() {
		try {
			return ValidatorUtil.checkValidator(getRequest(), this.getValue());
		} catch (AuthorizationException e) {
			return false;
		}
	}
}
