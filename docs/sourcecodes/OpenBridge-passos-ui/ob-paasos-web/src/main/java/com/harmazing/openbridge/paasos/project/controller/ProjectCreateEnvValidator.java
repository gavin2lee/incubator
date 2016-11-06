package com.harmazing.openbridge.paasos.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.ValidatorContext;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
 
@Component("ProjectCreateEnvValidator")
public class ProjectCreateEnvValidator extends ProjectEnvValidator {

	@Override
	public boolean validate(ValidatorContext validatorContext) throws Exception {
		HttpServletRequest request = validatorContext.getRequest();
		IUser user = WebUtil.getUserByRequest(request);
		String projectId = validatorContext.getParams().get("projectId");
		String businessId = request.getParameter("businessId");
		String businessType = request.getParameter("businessType");
		String envType = validatorContext.getParams().get("envType");
		PaasProject project = loadPaasModel(projectId, businessId, businessType, request);
		// 用户对项目有权限
		if (project == null) {
			return false;
		}
		// 用户对项目有权限
		if (!project.getTenantId().equals(user.getTenantId())) {
			return false;
		}

		return funcValidator(user, envType);
	}
}
