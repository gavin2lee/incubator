package com.harmazing.openbridge.sys.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.sys.user.model.SysUser;
import com.harmazing.openbridge.sys.user.service.ISysUserService;

@Controller
@RequestMapping("/sys/portal")
public class SysPortalController extends AbstractController {
	private static final Log logger = LogFactory.getLog(SysPortalController.class);
	@Autowired
	private ISysUserService userService;

	@RequestMapping("/myportal")
	public String home(HttpServletRequest request, HttpServletResponse response) {
		return "/sys/portal/myportal";
	}

	@RequestMapping("/welcome")
	public String welcome(HttpServletRequest request,
			HttpServletResponse response) {
		return "/sys/portal/welcome";
	}

	@RequestMapping("/help")
	public String help(HttpServletRequest request, HttpServletResponse response) {
		IUser currentUser = WebUtil.getUserByRequest(request);
		SysUser user = userService.getUserById(currentUser.getUserId());
		request.setAttribute("currentUser", user);
		return "/sys/portal/help";
	}

	@RequestMapping("/myPassword")
	public String myPassword(HttpServletRequest request,
			HttpServletResponse response) {
		IUser currentUser = WebUtil.getUserByRequest(request);
		if (currentUser != null) {
			request.setAttribute("userId", currentUser.getUserId());
		}
		return "/sys/portal/myPassword";
	}

	@RequestMapping("/updateMyPassword")
	@ResponseBody
	public JsonResponse updateMyPassword(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = request.getParameter("userId");
			IUser currentUser = WebUtil.getUserByRequest(request);
			if (currentUser == null || !currentUser.getUserId().equals(userId)) {
				return JsonResponse.failure(500, "您只能修改个人密码");
			}
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			int ret = userService.updateUserPwd(userId, oldPassword,
					newPassword);
			if (ret == -1) {
				return JsonResponse.failure(500, "原始密码不正确");
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除群组出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}