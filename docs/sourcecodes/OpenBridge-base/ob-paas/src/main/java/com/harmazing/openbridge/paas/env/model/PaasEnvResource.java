package com.harmazing.openbridge.paas.env.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.model.BaseModel;
import com.harmazing.framework.util.StringUtil;

@SuppressWarnings("serial")
public class PaasEnvResource extends BaseModel {
	/*
	 * 申请资源的数据库组件
	 */
	private String recordId;

	private String envId;

	/**
	 * 资源类型的唯一ID
	 */
	private String resourceId;

	private String applyConfig;

	private String resultConfig;

	private String resAddition;

	private String paasosResId;

	/**
	 * 不要调用该方法，只是防止序列化时出错
	 * 
	 * @param aa
	 */
	public void setConfig(Map<String, Object> aa) {

	}

	/**
	 * 不要调用该方法，只是防止序列化时出错
	 * 
	 * @param aa
	 */
	public void setEnvironment(Map<String, Object> aa) {

	}

	public Map<String, Object> getConfig() {
		Map<String, Object> kv = new HashMap<String, Object>();
		if (StringUtil.isNotNull(applyConfig)) {
			String temp = StringUtil.trimWhitespace(applyConfig);
			if(temp.startsWith("[")){
				JSONArray array = JSONObject.parseArray(temp);
				for (int i = 0; i < array.size(); i++) {
					kv.put(array.getJSONObject(i).getString("key"), array
							.getJSONObject(i).getString("value"));
				}
			}else{
				JSONObject obj = JSONObject.parseObject(temp);
				Iterator<String> ite = obj.keySet().iterator();
				while(ite.hasNext()){
					String key = ite.next();
					kv.put(key, obj.getString(key));
				}
			}
		}
		return kv;
	}

	public Map<String, Object> getEnvironment() {
		Map<String, Object> kv = new HashMap<String, Object>();
		if (StringUtil.isNotNull(resultConfig)) {
			String temp = StringUtil.trimWhitespace(resultConfig);
			if(temp.startsWith("[")){
				JSONArray array = JSONObject.parseArray(temp);
				for (int i = 0; i < array.size(); i++) {
					kv.put(array.getJSONObject(i).getString("key"), array
							.getJSONObject(i).getString("value"));
				}
			}else{
				JSONObject obj = JSONObject.parseObject(temp);
				Iterator<String> ite = obj.keySet().iterator();
				while(ite.hasNext()){
					String key = ite.next();
					kv.put(key, obj.getString(key));
				}
			}
		}
		return kv;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getApplyConfig() {
		return applyConfig;
	}

	public void setApplyConfig(String applyConfig) {
		this.applyConfig = applyConfig;
	}

	public String getResultConfig() {
		return resultConfig;
	}

	public void setResultConfig(String resultConfig) {
		this.resultConfig = resultConfig;
	}

	public String getResAddition() {
		return resAddition;
	}

	public void setResAddition(String resAddition) {
		this.resAddition = resAddition;
	}

	public String getPaasosResId() {
		return paasosResId;
	}

	public void setPaasosResId(String paasosResId) {
		this.paasosResId = paasosResId;
	}
}
