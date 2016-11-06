package com.harmazing.openbridge.paasos.project.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.extension.Plugin;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paas.plugin.xml.ResourceNode;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSNginxService;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;

@Controller
@RequestMapping("/project/env")
public class PaasProjectEnvController extends ProjectBaseController {
	protected static final Log logger = LogFactory
			.getLog(PaasProjectEnvController.class);

	@Autowired
	private IPaasProjectEnvService envService;
	@Autowired
	private IPaasOSNginxService paasNginxService;

	private void setEnvTypes2Request(HttpServletRequest request)
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

	private String getInitEnvTypes() {
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

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String envType = request.getParameter("envType");
			String envId = request.getParameter("envId");

			List<PaasEnv> envs = envService.getEnvListByBusinessId(project
					.getProjectId());
			List<PaasEnv> list = new ArrayList<PaasEnv>();
			if (StringUtil.isNull(envId) && StringUtil.isNull(envType)) {
				if (envs.size() > 0) {
					envId = envs.get(0).getEnvId();
					envType = envs.get(0).getEnvType();
					for (int i = 0; i < envs.size(); i++) {
						if (envs.get(i).getEnvType().equals(envType)) {
							list.add(envs.get(i));
						}
					}
				}
			} else if (StringUtil.isNull(envType)) {
				if (envs.size() > 0) {
					for (int i = 0; i < envs.size(); i++) {
						if (envId.equals(envs.get(i).getEnvId())) {
							envType = envs.get(i).getEnvType();
							break;
						}
					}
					for (int i = 0; i < envs.size(); i++) {
						if (envs.get(i).getEnvType().equals(envType)) {
							list.add(envs.get(i));
						}
					}
				}
			} else if (StringUtil.isNull(envId)) {
				if (envs.size() > 0) {
					for (int i = 0; i < envs.size(); i++) {
						if (envs.get(i).getEnvType().equals(envType)) {
							list.add(envs.get(i));
						}
					}
					if (list.size() > 0) {
						envId = list.get(0).getEnvId();
					}
				}
			}

			request.setAttribute("envList", list);
			request.setAttribute("envId", envId);

			setEnvTypes2Request(request);

			if (StringUtil.isNull(envType)) {
				envType = getInitEnvTypes();
			}
			request.setAttribute("envType", envType);
			return getUrlPrefix() + "/index";
		} catch (Exception e) {
			logger.error("环境列表失败" + e.getMessage());
			return forward(ERROR);
		}
	}

	@RequestMapping("/info")
	public String info(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			if (StringUtil.isNull(envId)) {
				throw new Exception("envType is null");
			}
			request.setAttribute("envId", envId);
			PaasEnv env = envService.getEnvById(envId);
			request.setAttribute("env", env);
			request.setAttribute("loadbalanceList",
					paasNginxService.getNginxListByEnvId(env.getEnvId()));
			List<PaasEnvResource> resourceList = env.getResources();
			if (env.getResources() != null) {
				for(int i=resourceList.size()-1;i>-1;i--){
					PaasEnvResource resource = env.getResources().get(i);
					if(resource.getResourceId().equals("define")){
						resourceList.remove(i);
					}
				}
				for (int i = 0; i < resourceList.size(); i++) {
					PaasEnvResource resource = env.getResources().get(i);
					resource.attObject(
							"node",
							(ResourceNode) Plugin.getExtensionPoint(
									ResourceNode.pointId).getConfigNode(
									ResourceNode.nodeName,
									resource.getResourceId()));
				}
			}
			request.setAttribute("resourceList", resourceList);
			return getUrlPrefix() + "/info";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PaasProject project = loadProjectModel(request);
			
			//dengxq
			String envType = request.getParameter("envType");
			String remark = ConfigUtil.getConfigString("paasos.evnmark");
			if(StringUtils.hasText(remark)){
				JSONObject jo = JSONObject.parseObject(remark);
				if(jo.containsKey(envType)){
					JSONArray data = jo.getJSONArray(envType);
					List<Map> evnmark = JSONObject.parseArray(data.toJSONString(), Map.class);
					request.setAttribute("evnmark", evnmark);
				}
				else{
					request.setAttribute("defaultEnvmark", "");
				}
			}
			else{
				request.setAttribute("defaultEnvmark", "");
			}
			
			return getUrlPrefix() + "/create";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) {
		try {
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			PaasEnv env = envService.getEnvById(envId);
			
			String remark = ConfigUtil.getConfigString("paasos.evnmark");
			if(StringUtils.hasText(remark)){
				String envType = env.getEnvType();
				JSONObject jo = JSONObject.parseObject(remark);
				if(jo.containsKey(envType)){
					JSONArray data = jo.getJSONArray(envType);
					List<Map> evnmark = JSONObject.parseArray(data.toJSONString(), Map.class);
					request.setAttribute("evnmark", evnmark);
				}
				else{
//					request.setAttribute("defaultEnvmark", envType);
					request.setAttribute("defaultEnvmark", "");
				}
			}
			else{
				request.setAttribute("defaultEnvmark", "");
			}
			
			request.setAttribute("env", env);
			return getUrlPrefix() + "/edit";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse save(HttpServletRequest request,
			HttpServletResponse response, PaasEnv env) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			env.setCreationTime(new Date());
			env.setCreator(user.getUserId());
			env.setEnvId(StringUtil.getUUID());
			env.setProjectId(project.getProjectId());
			env.setBusinessType("paasos");
			envService.createEnv(env);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("新建环境页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/rename")
	@ResponseBody
	public JsonResponse rename(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			String envName = request.getParameter("envName");
			String envMark = request.getParameter("envMark");
			PaasEnv env = envService.getEnvById(envId);
			env.setEnvName(envName);
			env.setEnvMark(StringUtils.isEmpty(envMark)?null:envMark);
			envService.updateEnv(env);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("环境更新出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/del")
	@ResponseBody
	public JsonResponse del(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			if (StringUtil.isNull(envId)) {
				return JsonResponse.failure(1, "参数错误");
			}
			envService.deleteEnv(envId, user.getUserId());
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除环境页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	// ////////

}
