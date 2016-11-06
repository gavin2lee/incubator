package com.harmazing.openbridge.paas.plugin.bean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.openbridge.paas.plugin.xml.ResourceBridge;

@Service
public class Python2ResourceBridge implements ResourceBridge {
	@Override
	public Map<String, String> getPageConfig(HttpServletRequest request) {
		Map<String, String> config = new HashMap<String, String>();

		return config;
	}


	@Override
	public Map<String, String> updateResource(String businessId,
			Map<String, String> pageConfig, Map<String, String> oldVal,
			String type) {

		return oldVal;
	}

	@Override
	public Map<String,String> applyReource(String userId, Map<String, String> commonConfig,
			Map<String, String> pageConfig) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void removeResource(String userId, String paasOsResId) {

	}


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
