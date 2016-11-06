package com.harmazing.openbridge.paas.plugin.xml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public interface ResourceBridge {
	Map<String, String> getPageConfig(HttpServletRequest request);

	//申请资源
	Map<String,String> applyReource(String userId, Map<String,String> commonConfig,
			Map<String, String> resConfig);

	Map<String, String> updateResource(String businessId,
			Map<String, String> pageConfig, Map<String, String> oldVal,
			String type);

	//删除资源
	void removeResource(String userId, String paasosResId);
	
	//查看资源参数
	JSONObject getResourceParams(String userId, String paasosResId);
	
	JSONObject getResourceStatus(String userId, String paasosResId);
}
