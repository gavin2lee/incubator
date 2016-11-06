package com.harmazing.openbridge.paasos.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.ValidatorContext;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;

@Component("ProjectEnvValidator")
public class ProjectEnvValidator extends ProjectValidator {

	@Autowired
	private IPaasProjectEnvService paasProjectEnvService;

	@Override
	public boolean validate(ValidatorContext validatorContext) throws Exception {
		HttpServletRequest request = validatorContext.getRequest();
		IUser user = WebUtil.getUserByRequest(request);
		String projectId = validatorContext.getParams().get("projectId");
		String businessId = request.getParameter("businessId");
		String businessType = request.getParameter("businessType");
		String envId = validatorContext.getParams().get("envId");
		PaasEnv paasEnv = paasProjectEnvService.getEnvById(envId);
		PaasProject project = loadPaasModel(projectId, businessId, businessType, request);
		// 用户对项目有权限
		if (project == null) {
			return false;
		}
		// 用户对项目有权限
		if (!project.getTenantId().equals(user.getTenantId())) {
			return false;
		}
		// 环境对应项目
		if (!paasEnv.getProjectId().equals(project.getProjectId())) {
			return false;
		}
		return funcValidator(user, paasEnv.getEnvType());
	}

	protected boolean funcValidator(IUser user, String envType) {
		// 是否有对应的环境权限
		if (envType.equals("test")) {
			if (!funcValidatorx(user, "paasos.project.envtest")) {
				return false;
			}
		}

		if (envType.equals("live")) {
			if (!funcValidatorx(user, "paasos.project.envlive")) {
				return false;
			}
		}
		return true;
	}

	private boolean funcValidatorx(IUser user, String functionId) {
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
