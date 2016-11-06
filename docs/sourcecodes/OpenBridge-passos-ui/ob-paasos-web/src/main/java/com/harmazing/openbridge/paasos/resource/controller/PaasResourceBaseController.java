package com.harmazing.openbridge.paasos.resource.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paasos.utils.ConfigUtils;

public abstract class PaasResourceBaseController extends AbstractController {

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return getUrlPrefix() + "/index";
	}

	@RequestMapping("/add")
	// 添加资源的输入页面
	public String addMysql(HttpServletRequest request,
			HttpServletResponse response) {
		setUserInfo2Request(request);
		ConfigUtils.setEnvType2Request(request);
		setResourceLimit2Request(request);
		return getUrlPrefix() + "/add";
	}

	// 获取当前用户所属的租户
	protected void setUserInfo2Request(HttpServletRequest request) {
		IUser user = WebUtil.getUserByRequest(request);
		request.setAttribute("userId", user.getUserId());
		request.setAttribute("userName", user.getUserName());
		request.setAttribute("tenantId", user.getTenantId());
		request.setAttribute("tenantName", user.getTenantName());
	}

	// the child must override this method，获取资源配额
	protected abstract void setResourceLimit2Request(HttpServletRequest request);
}
