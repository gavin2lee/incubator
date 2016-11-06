package com.harmazing.openbridge.paasos.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.IValidator;
import com.harmazing.framework.authorization.ValidatorContext;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;

@Component("ProjectValidator")
public class ProjectValidator implements IValidator {

	@Autowired
	private IPaasProjectService iPaasProjectService;

	protected PaasProject loadPaasModel(String projectId, String businessId, String businessType,
			HttpServletRequest request) {
		PaasProject project = null;
		if (request.getAttribute("project") != null) {
			PaasProject project1 = (PaasProject) request.getAttribute("project");
			if (project1.getProjectId().equals(projectId)) {
				project = project1;
			} else if (project1.getBusinessId().equals(businessId) && project1.getProjectType().equals(businessType)) {
				project = project1;
			} else {
				throw new RuntimeException("参数错误，导致project在请求阶段缓存出错");
			}
		} else {
			if (StringUtil.isNotNull(projectId)) {
				project = iPaasProjectService.getPaasProjectInfoById(projectId);
				request.setAttribute("project", project);
			}
			if (StringUtil.isNotNull(businessId) && StringUtil.isNotNull(businessType)) {
				project = iPaasProjectService.getPaasProjectByBusinessIdAndType(businessId, businessType);
				request.setAttribute("project", project);
			}
		}
		return project;
	}

	@Override
	public boolean validate(ValidatorContext validatorContext) throws Exception {
		HttpServletRequest request = validatorContext.getRequest();
		IUser user = WebUtil.getUserByRequest(request);
		String projectId = validatorContext.getParams().get("projectId");
		String businessId = request.getParameter("businessId");
		String businessType = request.getParameter("businessType");
		if (StringUtil.isNull(projectId) && StringUtil.isNull(businessId) && StringUtil.isNull(businessType)) {
			return false;
		}
		PaasProject project = loadPaasModel(projectId, businessId, businessType, request);
		if (project == null) {
			return false;
		}
		if (project.getTenantId().equals(user.getTenantId())) {
			return true;
		}
		return false;
	}

}
