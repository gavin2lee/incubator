package com.harmazing.openbridge.paas.env.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.extension.Plugin;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paas.nginx.service.IPaasNginxService;
import com.harmazing.openbridge.paas.plugin.xml.ResourceNode;

public class EnvController extends AbstractEnvController {
	@Autowired
	private IPaasNginxService paasNginxService;

	public String index(HttpServletRequest request, String businessId,
			String businessType) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envType = request.getParameter("envType");
			String envId = request.getParameter("envId");
			
			List<PaasEnv> envs = paasEnvService.getEnvListByBusinessId(
					user.getUserId(), businessId, businessType);
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
			PaasEnv env = paasEnvService.getEnvById(user.getUserId(), envId);
			request.setAttribute("env", env);
			setEnvTypes2Request(request);

			if (StringUtil.isNull(envType)) {
				envType = getInitEnvTypes();
			}
			request.setAttribute("envType", envType);
			return getUrlPrefix() + "/index";
		} catch (Exception e) {
			logger.error("环境页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	public String info(HttpServletRequest request) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			if (StringUtil.isNull(envId)) {
				throw new Exception("envType is null");
			}
			request.setAttribute("envId", envId);
			PaasEnv env = getEnvById(request);
			request.setAttribute("env", env);
			request.setAttribute("loadbalanceList",
					paasNginxService.getNginxListByEnvId(env.getEnvId(),user.getUserId()));
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
			logger.error("环境列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	public PaasEnv getEnvFromRequest(HttpServletRequest request) {
		IUser user = WebUtil.getUserByRequest(request);
		String envId = request.getParameter("envId");
		return paasEnvService.getEnvById(user.getUserId(), envId);
	}

	public void createEnv(String userId, PaasEnv env) {
		paasEnvService.createEnv(userId, env);
	}

	public void renameEnv(HttpServletRequest request) {
//		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			String envName = request.getParameter("envName");
			String envMark = request.getParameter("envMark");
			paasEnvService.updateEnv(user.getUserId(), envId, envName,envMark);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}

	public void delete(String userId, String envId) {
		paasEnvService.deleteEnv(envId, userId);
	}

	public PaasEnvResource getEnvResourceById(String userId, String recordId) {
		return paasEnvService.getEnvResourceById(userId, recordId);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getEvnEnvironment(String userId, String envId) {
		return paasEnvService.getEvnEnvironment(userId, envId);
	}

	public void deleteEnvResource(String userId, String recordId) {
		paasEnvService.deleteEnvResource(userId, recordId);
	}

	/**
	 * 获取资源配额参数
	 * 
	 * @param request
	 * @param envType
	 */
	public void getResourceOptions(HttpServletRequest request, String envType) {
		IUser user = WebUtil.getUserByRequest(request);
		String resId = request.getParameter("resId");
		String paasOsRestUrlprefix = ConfigUtil
				.getConfigString("paasOsRestUrlprefix");
		if (StringUtil.isNotNull(paasOsRestUrlprefix)) {
			paasOsRestUrlprefix = paasOsRestUrlprefix
					+ (paasOsRestUrlprefix.endsWith("/") ? "" : "/");
			String restReponse = null;
			if ("mysql51".equals(resId)) {
				paasOsRestUrlprefix = paasOsRestUrlprefix
						+ "resource/mysql/options.do";
				restReponse = PaasAPIUtil.get(user.getUserId(),
						paasOsRestUrlprefix);
			} else if ("redis".equals(resId)) {
				paasOsRestUrlprefix = paasOsRestUrlprefix
						+ "resource/redis/options.do";
				restReponse = PaasAPIUtil.get(user.getUserId(),
						paasOsRestUrlprefix);
			} else if ("nas".equals(resId)) {
				paasOsRestUrlprefix = paasOsRestUrlprefix
						+ "resource/nas/nfs/options.do";
				restReponse = PaasAPIUtil.get(user.getUserId(),
						paasOsRestUrlprefix);
			} else if ("rocket".equals(resId)) {
				paasOsRestUrlprefix = paasOsRestUrlprefix
						+ "resource/messagequeue/options.do";
				restReponse = PaasAPIUtil.get(user.getUserId(),
						paasOsRestUrlprefix);
			}
			if (StringUtil.isNotNull(restReponse)) {
				try {
					JSONObject jsonObj = JSONObject.parseObject(restReponse);
					if (jsonObj.containsKey("code")
							&& jsonObj.getString("code").equals("0")) {
						JSONObject options = jsonObj.getJSONObject("data");
						if ("nas".equals(resId)) {
							JSONArray limits = options.getJSONArray("storage");
							for (int index = 0; index < limits.size(); index++) {
								JSONObject envLimit = limits
										.getJSONObject(index);
								if (envLimit.getString("envType").equals(
										envType)) {
									options.remove("storage");
									options.put("storage",
											envLimit.getJSONArray("free"));
								}
							}
						}
						if (options.size() > 0) {
							request.setAttribute("options", options);
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 获取但个资源的环境变量参数
	 * 
	 * @param request
	 * @return
	 */
	public JSONObject queryEnvResourceInfo(HttpServletRequest request) {
		IUser user = WebUtil.getUserByRequest(request);
		String recordId = request.getParameter("recordId");
		JSONObject returnJson = new JSONObject();
		PaasEnvResource paasEnvResource = paasEnvService
				.getEnvResourceInfoById(user.getUserId(), recordId);
		String resourceId = paasEnvResource.getResourceId();
		if ("nas".equals(resourceId)) {
			returnJson.put("PAASOS_NFS_PATH", paasEnvResource.getResAddition());
		}
		String resultConfig = paasEnvResource.getResultConfig();
		JSONObject jsonObj = JSONObject.parseObject(resultConfig);

		if ("mysql51".equals(resourceId)) {
			returnJson.put("PAASOS_MYSQL_USERNAME",
					jsonObj.getString("username"));
			returnJson.put("PAASOS_MYSQL_PASSWORD",
					jsonObj.getString("password"));
			returnJson.put("PAASOS_MYSQL_DATABASE",
					paasEnvResource.getResAddition());
			returnJson.put("PAASOS_MYSQL_HOST_IP",
					jsonObj.getString("clusterIP"));
			returnJson.put("PAASOS_MYSQL_HOST_PORT",
					jsonObj.getString("clusterPort"));
		} else if ("redis".equals(resourceId)) {
			returnJson.put("PAASOS_REDIS_USERNAME",
					jsonObj.getString("username"));
			returnJson.put("PAASOS_REDIS_PASSWORD",
					jsonObj.getString("password"));
			returnJson.put("PAASOS_REDIS_HOST_IP",
					jsonObj.getString("clusterIP"));
			returnJson.put("PAASOS_REDIS_HOST_PORT",
					jsonObj.getString("clusterPort"));
		} else if ("rocket".equals(resourceId)) {
			returnJson.put("PAASOS_RABBIT_HOST_IP",
					jsonObj.getString("clusterIP"));
			returnJson.put("PAASOS_RABBIT_HOST_PORT",
					jsonObj.getString("clusterPort"));
		}
		return returnJson;
	}

	/**
	 * 获取单个资源帮助参数
	 * 
	 * @param request
	 * @return
	 */
	public String getResourceHelpInfo(HttpServletRequest request) {
		IUser user = WebUtil.getUserByRequest(request);
		String recordId = request.getParameter("recordId");
		PaasEnvResource paasEnvResource = paasEnvService
				.getEnvResourceInfoById(user.getUserId(), recordId);
		String resourceId = paasEnvResource.getResourceId();
		ResourceNode resourceNode = (ResourceNode) Plugin.getExtensionPoint(
				"com.harmazing.paas.resource").getConfigNode("resource",
				resourceId);
		String helpPage = resourceNode.getHelp();
		if ("nas".equals(resourceId)) {
			request.setAttribute("PAASOS_NFS_PATH",
					paasEnvResource.getResAddition());
		}
		String resultConfig = paasEnvResource.getResultConfig();
		JSONObject jsonObj = JSONObject.parseObject(resultConfig);

		if ("mysql51".equals(resourceId)) {
			request.setAttribute("PAASOS_MYSQL_USERNAME",
					jsonObj.getString("username"));
			request.setAttribute("PAASOS_MYSQL_PASSWORD",
					jsonObj.getString("password"));
			request.setAttribute("PAASOS_MYSQL_DATABASE",
					paasEnvResource.getResAddition());
			request.setAttribute("PAASOS_MYSQL_HOST_IP",
					jsonObj.getString("clusterIP"));
			request.setAttribute("PAASOS_MYSQL_HOST_PORT",
					jsonObj.getString("clusterPort"));
			request.setAttribute("externalIP", jsonObj.getString("externalIP"));
			request.setAttribute("externalPort",
					jsonObj.getString("externalPort"));
		} else if ("redis".equals(resourceId)) {
			request.setAttribute("PAASOS_REDIS_USERNAME",
					jsonObj.getString("username"));
			request.setAttribute("PAASOS_REDIS_PASSWORD",
					jsonObj.getString("password"));
			request.setAttribute("PAASOS_REDIS_HOST_IP",
					jsonObj.getString("clusterIP"));
			request.setAttribute("PAASOS_REDIS_HOST_PORT",
					jsonObj.getString("clusterPort"));
			request.setAttribute("externalIP", jsonObj.getString("externalIP"));
			request.setAttribute("externalPort",
					jsonObj.getString("externalPort"));
			request.setAttribute("manageIP",
					jsonObj.getString("manage.externalIP"));
			request.setAttribute("managePort",
					jsonObj.getString("manage.externalPort"));
		} else if ("rocket".equals(resourceId)) {
			request.setAttribute("PAASOS_RABBIT_HOST_IP",
					jsonObj.getString("clusterIP"));
			request.setAttribute("PAASOS_RABBIT_HOST_PORT",
					jsonObj.getString("clusterPort"));

			request.setAttribute("externalIP", jsonObj.getString("externalIP"));
			request.setAttribute("externalPort",
					jsonObj.getString("externalPort"));

			request.setAttribute("manageIP",
					jsonObj.getString("manage.externalIP"));
			request.setAttribute("managePort",
					jsonObj.getString("manage.externalPort"));
		}
		return helpPage;
	}

	/**
	 * 获取环境的参数，分为资源参数和自定义参数
	 * 
	 * @param request
	 */
	public void setEnvironemt2Request(HttpServletRequest request) {
		IUser user = WebUtil.getUserByRequest(request);
		String envId = request.getParameter("envId");

		Map<String, String> envVariables = paasEnvService.getEvnEnvironment(
				user.getUserId(), envId);
		Map<String, String> sysEnv = new HashMap<String, String>();
		Map<String, String> defineEnv = new HashMap<String, String>();
		String[] sysEnvArray = new String[] { "PAASOS_NFS_PATH",
				"PAASOS_MYSQL_USERNAME", "PAASOS_MYSQL_PASSWORD",
				"PAASOS_MYSQL_DATABASE", "PAASOS_MYSQL_HOST_IP",
				"PAASOS_MYSQL_HOST_PORT", "PAASOS_REDIS_HOST_IP",
				"PAASOS_REDIS_HOST_PORT", "PAASOS_REDIS_USERNAME",
				"PAASOS_REDIS_PASSWORD", "PAASOS_RABBIT_HOST_IP",
				"PAASOS_RABBIT_HOST_PORT" };
		Iterator<String> keyIterator = envVariables.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			int j = 0;
			for (; j < sysEnvArray.length; j++) {
				if (key.equals(sysEnvArray[j])) {
					sysEnv.put(key, envVariables.get(key));
					break;
				}
			}
			if (j == sysEnvArray.length) {
				defineEnv.put(key, envVariables.get(key));
			}
		}
		request.setAttribute("environment", sysEnv);
		request.setAttribute("defineEnv", defineEnv);
		request.setAttribute("envId", envId);
	}

	public JSONObject getOldResources(HttpServletRequest request,
			String businessType) {
		IUser user = WebUtil.getUserByRequest(request);
		String resourceType = request.getParameter("resId");
		String envType = request.getParameter("envType");
		int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
		int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resouceType", resourceType);
		params.put("envType", envType);
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);
		params.put("source", businessType);// app or api

		String userId = user.getUserId();
		return paasEnvService.getOldResource(userId, params);
	}
	
	public DeployResource getDeployResource(HttpServletRequest request){
		IUser user = WebUtil.getUserByRequest(request);
		String envId = request.getParameter("envId");
		return paasEnvService.getDeployResouceByEnvId(user.getUserId(), envId);
	}
}
