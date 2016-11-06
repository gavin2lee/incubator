package com.harmazing.openbridge.paasos.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;

public abstract class ProjectBaseController extends AbstractController {
	@Autowired
	IPaasProjectService paasProjectService;

	protected PaasProject loadProjectModel(HttpServletRequest request) {
		String projectId = request.getParameter("projectId");
		String businessId = request.getParameter("businessId");
		String businessType = request.getParameter("businessType");
		if (request.getAttribute("project") != null
				&& (StringUtil.isNotNull(projectId) || (StringUtil
						.isNotNull(businessId) && StringUtil
						.isNotNull(businessType)))) {
			if (StringUtil.isNotNull(projectId)) {
				PaasProject project = (PaasProject) request
						.getAttribute("project");
				if (project.getProjectId().equals(projectId)) {
					return project;
				} else {
					throw new RuntimeException("参数错误，导致app在请求阶段缓存出错");
				}
			} else if (StringUtil.isNotNull(businessId)
					&& StringUtil.isNotNull(businessType)) {
				PaasProject project = (PaasProject) request
						.getAttribute("project");
				if (project.getBusinessId().equals(businessId)
						&& project.getProjectType().equals(businessType)) {
					return project;
				} else {
					throw new RuntimeException("参数错误，导致app在请求阶段缓存出错");
				}
			} else {
				throw new RuntimeException("参数错误，导致app在请求阶段缓存出错");
			}
		} else {
			if (StringUtil.isNotNull(projectId)) {
				PaasProject project = paasProjectService
						.getPaasProjectInfoById(projectId);
				request.setAttribute("project", project);
				return project;
			} else if (StringUtil.isNotNull(businessId)
					&& StringUtil.isNotNull(businessType)) {
				PaasProject project = paasProjectService
						.getPaasProjectByBusinessIdAndType(businessId,
								businessType);
				request.setAttribute("project", project);
				return project;
			}
			throw new RuntimeException("参数错误，导致app在请求阶段缓存出错");
		}
	}
}
