package com.harmazing.openbridge.paas.plugin.bean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.plugin.xml.ResourceBridge;

@Service
public class UserDefinedResourceBridge implements ResourceBridge {



	/**
	 * 获取配置页面的配置参数，与自定义的config.jsp对应a
	 */
	@Override
	public Map<String, String> getPageConfig(HttpServletRequest request) {
		String[] resourceKeys = request.getParameterValues("resourceKey");
		String[] resourceValues = request.getParameterValues("resourceValue");
		Map<String, String> config = new HashMap<String, String>();
		for (int i = 0; i < resourceKeys.length; i++) {
			if (StringUtil.isNotNull(resourceKeys[i])
					&& StringUtil.isNotNull(resourceValues[i])) {
				config.put(resourceKeys[i], resourceValues[i]);
			}
		}
		return config;
	}

	/**
	 * 暂时不存在修改
	 */
	@Override
	public Map<String, String> updateResource(String businessId,
			Map<String, String> pageConfig, Map<String, String> oldVal,
			String type) {
		return pageConfig;
	}

	/**
	 * 申请自定义资源
	 */
	@Override
	public Map<String,String> applyReource(String userId, Map<String, String> commonConfig,
			Map<String, String> pageConfig) {
		return pageConfig;
	}
	
	/**
	 * 删除自定义资源
	 */
	@Override
	public void removeResource(String userId, String paasOsResId) {

	}

	/**
	 * 获取自定资源参数
	 */
	@Override
	public JSONObject getResourceParams(String userId, String paasOsResId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getResourceStatus(String userId, String paasOsResId) {
		// TODO Auto-generated method stub
		return null;
	}
}
