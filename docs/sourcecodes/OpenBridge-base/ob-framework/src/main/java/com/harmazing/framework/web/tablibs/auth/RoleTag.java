package com.harmazing.framework.web.tablibs.auth;

import com.harmazing.framework.authorization.ValidatorUtil;
import com.harmazing.framework.authorization.exceptions.AuthorizationException;
import com.harmazing.framework.util.WebUtil;

@SuppressWarnings("serial")
public class RoleTag extends AbstractAuthTag {
	private Boolean has;

	public Boolean getHas() {
		return has;
	}

	public void setHas(Boolean has) {
		this.has = has;
	}

	protected String getExpression() {
		return "roleValidator(role=" + this.getValue() + ")";
	}

	@Override
	protected boolean isValidation() {
		try {
			boolean validate = ValidatorUtil.checkValidator(WebUtil.getUserByRequest(getRequest()), getExpression());
			if(has!=null){
				if(validate && has)
					return true;
				if(!validate && !has)
					return true;
				return false;
			}
			return validate;
		} catch (AuthorizationException e) {
			return false;
		}
	}

}
