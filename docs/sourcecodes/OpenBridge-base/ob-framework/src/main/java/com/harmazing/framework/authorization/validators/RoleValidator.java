package com.harmazing.framework.authorization.validators;

import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.IValidator;
import com.harmazing.framework.authorization.ValidatorContext;

@Component("roleValidator")
public class RoleValidator implements IValidator {
	@Override
	public boolean validate(ValidatorContext context) throws Exception {
		IUser user = context.getUser();

		String[] roles = user.getUserRole();

		if (roles != null) {
			String roleStr = context.getParams().get("role");
			String[] rs = roleStr.split("\\|");
			for (int j = 0; j < rs.length; j++) {
				for (int i = 0; i < roles.length; i++) {
					if (roles[i].equals(rs[j])) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
