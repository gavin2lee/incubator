package com.harmazing.openbridge.paas.env.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paas.env.service.IPaasEnvService;

@Service
public class PaasEnvServiceImpl implements IPaasEnvService {
	private static Log logger = LogFactory
			.getLog(PaasEnvServiceImpl.class);
	
	private String getEnvRestUrl() {
		String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if (!osPath.endsWith("/")) {
			osPath = osPath + "/";
		}
		return osPath + "project/env/rest";
	}

	private JSONObject getResponseJSON(String response) {
		if (StringUtil.isNull(response)) {
			throw new RuntimeException("无返回数据");
		}
		JSONObject json = JSONObject.parseObject(response);
		if (json == null || json.isEmpty()) {
			throw new RuntimeException("无返回数据");
		}
		if (json.containsKey("code") && json.getString("code").equals("0")) {
			return json;
		} else {
			throw new RuntimeException(json.getString("msg"));
		}
	}

	@Override
	public void createEnv(String userId, PaasEnv env) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("envName", env.getEnvName());
		params.put("envMark", env.getEnvMark());
		params.put("envType", env.getEnvType());
		params.put("businessId", env.getProjectId());
		params.put("businessType", env.getBusinessType());
		String response = PaasAPIUtil.post(userId, getEnvRestUrl()
				+ "/addEnv.do", DataType.FORM, params);
		JSONObject json = getResponseJSON(response);
	}

	@Override
	public PaasEnv getEnvById(String userId, String envId) {
		try {
			String response = PaasAPIUtil.get(userId, getEnvRestUrl()
					+ "/getEnvInfo.do?envId=" + envId);
			JSONObject json = getResponseJSON(response);
			if ("0".equals(json.getString("code"))) { 
				PaasEnv env = JSONObject.parseObject(json.getString("data"), PaasEnv.class);  
				return env;
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException("获取环境信息" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getEvnEnvironment(String userId, String evnId) {
		String response = PaasAPIUtil.get(userId, getEnvRestUrl()
				+ "/getEnvironmentByEnv.do?envId=" + evnId);
		JSONObject json = getResponseJSON(response);
		if ("0".equals(json.getString("code"))) {
			return (Map<String, String>) JSONObject.parseObject(json.getString("data"), Map.class);
		}
		return null;
	}

	@Override
	public PaasEnv getEnvByName(String businessId, String envType,
			String envName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PaasEnv> getEnvListByBusinessId(String userId,
			String businessId, String businessType) {
		try {
			List<PaasEnv> envs = new ArrayList<PaasEnv>();
			String response = PaasAPIUtil.get(userId, getEnvRestUrl()
					+ "/getEnvList.do?businessId=" + businessId
					+ "&businessType=" + businessType);
			JSONObject json = getResponseJSON(response);
			if ("0".equals(json.getString("code"))) {
				JSONArray array = json.getJSONObject("data").getJSONArray(
						"list");
				if (array != null && array.size() > 0) {
					for (int i = 0; i < array.size(); i++) {
						JSONObject temp = array.getJSONObject(i);
						PaasEnv env = JSONObject.toJavaObject(temp,
								PaasEnv.class);
						envs.add(env);
					}
				}
			}
			return envs;
		} catch (Exception e) {
			throw new RuntimeException("获取环境列表出错" + e.getMessage());
		}
	}

	@Override
	public void updateEnv(String userId, String envId, String envName,String envMark) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("envId", envId);
		params.put("envName", envName);
		params.put("envMark", envMark);
		String response = PaasAPIUtil.post(userId, getEnvRestUrl()
				+ "/updateEnv.do", DataType.FORM, params);
		JSONObject updateJson = getResponseJSON(response);
	}
	
	@Override
	public void updateEnv(String userId, String envId, String envName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("envId", envId);
		params.put("envName", envName);
		String response = PaasAPIUtil.post(userId, getEnvRestUrl()
				+ "/updateEnv.do", DataType.FORM, params);
		JSONObject updateJson = getResponseJSON(response);
	}

	@Override
	public void deleteEnv(String envId, String userId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("envId", envId);
		String response = PaasAPIUtil.post(userId, getEnvRestUrl()
				+ "/deleteEnv.do", DataType.FORM, params);
		JSONObject updateJson = getResponseJSON(response);
	}

	@Override
	public PaasEnvResource getEnvResourceById(String userId, String recordId) {
		String response = PaasAPIUtil.get(userId, getEnvRestUrl()
				+ "/getEnvResource.do?recordId=" + recordId);
		JSONObject json = getResponseJSON(response);
		if ("0".equals(json.getString("code"))) {
			return JSONObject.parseObject(json.getString("data"), PaasEnvResource.class);
		}
		throw new RuntimeException("无法获取资源信息");
	}

	@Override
	public void envApplyResource(String userId, Map<String, String> params) {
		logger.debug(getEnvRestUrl()+ "/addEnvResource.do");
		String response = PaasAPIUtil.post(userId, getEnvRestUrl()
				+ "/addEnvResource.do", DataType.FORM, params);
		logger.debug(response);
		JSONObject json = getResponseJSON(response);
		/*if (!"0".equals(json.getString("code"))) {
			throw new RuntimeException("添加资源失败");
		}*/
	}

	@Override
	public void envUpdateResource(PaasEnv env, String resId,
			Map<String, String> pageConfig) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, List<PaasEnv>> getEnvListByBusinessId(String userId,
			List<String> businessIds, boolean hasNginxConf) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaasEnvResource getEnvResourceInfoById(String userId, String recordId) {
		PaasEnvResource resource = getEnvResourceById(userId, recordId);
		return resource;
	}

	@Override
	public void updateEnvResourceAddition(PaasEnvResource resource) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject queryEnvResourceStatusById(String userId, String recordId) {
		String response = PaasAPIUtil.get(userId, getEnvRestUrl()
				+ "/getEnvResourceStatus.do?recordId=" + recordId);
		JSONObject json = getResponseJSON(response);
		return json.getJSONObject("data");
	}

	@Override
	public void saveOrUpdateEnvResourceDefine(String userId, String envId, String resourceData) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("envId", envId);
		params.put("data", resourceData);
		String response = PaasAPIUtil.post(userId, getEnvRestUrl()
				+ "/saveOrUpdateDefines.do", DataType.FORM,params);
		JSONObject json = getResponseJSON(response);
		if(!json.containsKey("code") || !"0".equals(json.getString("code"))){
			throw new RuntimeException("自定义资源保存失败");
		}
	}

	@Override
	public DeployResource getDeployResouceByEnvId(String userId, String envId) {
		String response = PaasAPIUtil.get(userId, getEnvRestUrl()
				+ "/getEnvDeployInfo.do?envId=" + envId);
		JSONObject json = getResponseJSON(response);
		return JSONObject.parseObject(json.getString("data"), DeployResource.class);
	}

	@Override
	public JSONObject getOldResource(String userId, Map<String, Object> params) {
		String restUrlPrefix = ConfigUtil
				.getConfigString("paasOsRestUrlprefix");
		if (StringUtil.isNull(restUrlPrefix)) {
			throw new RuntimeException("无法操作资源，缺少rest url配置");
		}
		String restUrl = restUrlPrefix
				+ (restUrlPrefix.endsWith("/") ? "" : "/")
				+ "project/env/rest/page.do";
		try {
			String strResponse = PaasAPIUtil.post(userId, restUrl,
					DataType.FORM, params);
			return JSONObject.parseObject(strResponse);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void deleteEnvResource(String userId, String recordId) {
		String response = PaasAPIUtil.get(userId, getEnvRestUrl()
				+ "/deleteEnvResource.do?recordId=" + recordId);
		JSONObject json = getResponseJSON(response);
	}

	@Override
	public JSONObject getResourcePodEvent(String userId, String podName,
			String namespace) {
		String url = getEnvRestUrl() + "/getPodEvent.do";
		Map<String, String> params = new HashMap<String,String>();
		params.put("podName", podName);
		params.put("namespace", namespace);
		String response = PaasAPIUtil.post(userId, url, DataType.FORM, params);
		JSONObject json = getResponseJSON(response);
		return json;
	}

}
