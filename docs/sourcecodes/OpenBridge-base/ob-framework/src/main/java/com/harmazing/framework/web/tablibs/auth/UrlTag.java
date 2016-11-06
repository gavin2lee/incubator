package com.harmazing.framework.web.tablibs.auth;
import com.harmazing.framework.authorization.ValidatorUtil;
import com.harmazing.framework.authorization.exceptions.AuthorizationException;
import com.harmazing.framework.util.WebUtil;

@SuppressWarnings("serial")
public class UrlTag extends AbstractAuthTag {

	@Override
	protected boolean isValidation() {
		try {
			return ValidatorUtil.checkRequestURL(
					WebUtil.getUserByRequest(getRequest()), this.getValue());
		} catch (AuthorizationException e) {
			return false;
		}
	}
}
