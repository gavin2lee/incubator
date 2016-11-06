package com.harmazing.openbridge.paas.env.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.service.IPaasEnvService;

public abstract class AbstractEnvController extends AbstractController {

	@Autowired
	protected IPaasEnvService paasEnvService;

	protected void setEnvTypes2Request(HttpServletRequest request)
			throws Exception {
		Map<String, String> envTypes = new HashMap<String, String>();
		String paasEnvType = ConfigUtil.getConfigString("paasos.evntype");
		if (StringUtil.isNotNull(paasEnvType)) {
			String[] envTypeArray = StringUtil.split(paasEnvType);
			for (String envType : envTypeArray) {
				String[] pairs = StringUtil.split(envType, "|");
				if (pairs != null && pairs.length == 2) {
					envTypes.put(pairs[0], pairs[1]);
				}
			}
		}
		request.setAttribute("envTypes", envTypes);
	}

	protected String getInitEnvTypes() {
		String paasEnvType = ConfigUtil.getConfigString("paasos.evntype");
		if (StringUtil.isNotNull(paasEnvType)) {
			String[] envTypeArray = StringUtil.split(paasEnvType);
			for (String envType : envTypeArray) {
				String[] pairs = StringUtil.split(envType, "|");
				if (pairs != null && pairs.length == 2) {
					return pairs[0];
				}
			}
		}
		return null;
	}

	protected PaasEnv getEnvById(HttpServletRequest request) {
		IUser user = WebUtil.getUserByRequest(request); 
		String envId = request.getParameter("envId");
		if (StringUtil.isNull(envId)) {
			return null;
		}
		if (request.getAttribute("env") != null) {
			return (PaasEnv) request.getAttribute("env");
		}
		PaasEnv env = paasEnvService.getEnvById(user.getUserId(), envId);
		request.setAttribute("env", env);
		return env;
	}
	
}
