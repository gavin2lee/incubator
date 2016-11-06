package com.harmazing.openbridge.paasos.sys;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IHttpUserManager;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.LoginLog;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.sys.user.controller.SysUserController;
import com.harmazing.openbridge.sys.user.service.ISysUserService;

@Controller
@RequestMapping("/sys/toolbox")
public class SysToolBoxController extends AbstractController {
	private static final Log logger = LogFactory
			.getLog(SysUserController.class);

	@Autowired
	private IHttpUserManager httpUserManager;

	@Autowired
	private ISysUserService sysUserService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return getUrlPrefix() + "/index";
	}

	@RequestMapping("/reload")
	@ResponseBody
	public JsonResponse reload(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			IUser user = WebUtil.getUserByRequest(request);
			String apiPath = ConfigUtil.getConfigString("paasos.api.url");
			if (!apiPath.endsWith("/")) {
				apiPath = apiPath + "/";
			}
			apiPath = apiPath + "sys/admin/refreshrolefunccache.do";
			String appPath = ConfigUtil.getConfigString("paasos.app.url");
			if (!appPath.endsWith("/")) {
				appPath = appPath + "/";
			}
			appPath = appPath + "sys/admin/refreshrolefunccache.do";

			String msg1 = PaasAPIUtil.post(user.getUserId(), apiPath,
					DataType.FORM, params);
			String msg2 = PaasAPIUtil.post(user.getUserId(), appPath,
					DataType.FORM, params);
			JSONObject json1 = JSONObject.parseObject(msg1);
			JSONObject json2 = JSONObject.parseObject(msg2);
			if ("0".equals(json1.getString("code"))
					&& "0".equals(json2.getString("code"))) {
				return JsonResponse.success();
			}
			/*
			 * if ("0".equals(json2.getString("code"))){ return
			 * JsonResponse.success(); }
			 */
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("刷新缓存出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, "刷新缓存失败");
		}
	}

	@RequestMapping("/changeuser")
	@ResponseBody
	public JsonResponse changeuser(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser original = WebUtil.getUserByRequest(request);
			String userId = request.getParameter("userId");
			IUser user = sysUserService.getUserById(userId);
			httpUserManager.userLogin(request, response, user, new LoginLog(
					request, user, "original:" + original.getUserId(), 2));
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("错误", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, "失败");
		}
	}
}
