package com.harmazing.openbridge.paasos.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.ValidatorContext;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;

@Component("ProjectEnvDeployValidator")
public class ProjectEnvDeployValidator extends ProjectEnvValidator {

	@Autowired
	private IPaasProjectEnvService paasProjectEnvService;

	@Autowired
	private IPaasProjectDeployService paasProjectDeployService;

	@Override
	public boolean validate(ValidatorContext validatorContext) throws Exception {
		HttpServletRequest request = validatorContext.getRequest();
		IUser user = WebUtil.getUserByRequest(request);
		String projectId = validatorContext.getParams().get("projectId");
		String businessId = request.getParameter("businessId");
		String businessType = request.getParameter("businessType");

		PaasProject project = loadPaasModel(projectId, businessId, businessType, request);
		PaasProjectDeploy deploy = null;
		String envId = request.getParameter("envId");
		if (StringUtil.isNull(envId)) {
			String deployId = request.getParameter("deployId");
			deploy = paasProjectDeployService.findById(deployId);
			envId = deploy.getEnvId();

			if (!deploy.getProjectId().equals(project.getProjectId())) {
				return false;
			}
		}
		if (StringUtil.isNull(envId)) {
			return false;
		}

		PaasEnv paasEnv = paasProjectEnvService.getEnvById(envId);
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
}
