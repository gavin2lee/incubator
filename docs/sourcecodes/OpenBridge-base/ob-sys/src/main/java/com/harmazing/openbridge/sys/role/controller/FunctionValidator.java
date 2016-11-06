package com.harmazing.openbridge.sys.role.controller;

import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.IValidator;
import com.harmazing.framework.authorization.ValidatorContext;

/**
 * 根据当前用户的角色查看用户拥有的功能权限，检查对当前功能是否拥有操作权限
 * 
 * @author taoshuangxi
 *
 */
@Component("functionValidator")
public class FunctionValidator implements IValidator {

	@Override
	public boolean validate(ValidatorContext validatorContext) throws Exception {
		IUser user = validatorContext.getUser();
		String functionId = validatorContext.getParams().get("functionId");

		// 从缓存中获取用户角色具备的功能，检查用户是否拥有该功能的权限
		String[] userFunctionIds = user.getUserFunc();
		if (userFunctionIds != null && userFunctionIds.length > 0) {
			for (String function : userFunctionIds) {
				if (function.equals(functionId)) {
					return true;
				}
			}
		}
		return false;
	}

}
