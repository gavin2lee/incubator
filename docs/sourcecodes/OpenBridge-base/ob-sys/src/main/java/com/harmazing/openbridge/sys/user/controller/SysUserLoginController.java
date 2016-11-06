package com.harmazing.openbridge.sys.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.authorization.IHttpUserManager;
import com.harmazing.framework.authorization.LoginLog;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.sys.user.model.SysUser;
import com.harmazing.openbridge.sys.user.service.ISysUserService;

@Controller
public class SysUserLoginController extends AbstractController {
	private static final Log logger = LogFactory
			.getLog(SysUserController.class);

	@Autowired
	private IHttpUserManager httpUserManager;
	@Autowired
	private ISysUserService userService;

	@RequestMapping("/mlogin")
	@ResponseBody
	public JsonResponse mLogin(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String loginName = request.getParameter("j_username");
			String password = request.getParameter("j_password");
			request.setAttribute("j_username", loginName);
			try {
				SysUser user = userService.mLogin(loginName, password);
				return JsonResponse.success(user.getToken());
			} catch (Exception e) {
				logger.error("用户[" + loginName + "]执行认证失败," + e.getMessage());
				return JsonResponse.failure(10, "登陆认证失败");
			}
		} catch (Exception e) {
			logger.error("用户列表页面出错", e);
			return JsonResponse.failure(11, "登陆认证失败");
		}
	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		try {
			String loginName = request.getParameter("j_username");
			String password = request.getParameter("j_password");

			request.setAttribute("j_username", loginName);
			SysUser user = null;
			try {
				user = userService.login(loginName, password);
			} catch (Exception e) {
				logger.error("用户[" + loginName + "]执行认证失败," + e.getMessage());
				request.setAttribute("loginError", e.getMessage());
				return loginResult(request, false);
			}
			if (user != null && user.getLoginName().equals(loginName)) {
				httpUserManager.userLogin(request, response, user,
						new LoginLog(request, user, "pwd:" + password, 0));
				return loginResult(request, true);
			} else {
				return loginResult(request, false);
			}
		} catch (Exception e) {
			logger.error("用户列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	private String loginResult(HttpServletRequest request, boolean success) {
		String targetUrl = request.getParameter("targetUrl");
		if (success) {
			if (StringUtil.isNotNull(targetUrl))
				return redirect(
						request,
						"/index.jsp?targetUrl="
								+ StringUtil.urlEncode(targetUrl));
			else
				return redirect(request, "/index.jsp");
		} else {
			if (StringUtil.isNotNull(targetUrl))
				return forward("/login.jsp?targetUrl="
						+ StringUtil.urlEncode(targetUrl));
			else
				return forward("/login.jsp");
		}
	}
}
