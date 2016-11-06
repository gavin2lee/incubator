package com.harmazing.openbridge.paasos.store.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.manager.model.PaaSBaseBuild;


public class PaasStoreApp extends PaaSBaseBuild {

	
	//
	private static final long serialVersionUID = 1L;
	private String config;

	private Date updateTime;
	
	private String appName;//应用名称
	
	private String documentation;//详细说明
	
	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Map<String, String> getConfigFileByNO(String number){
		if(StringUtil.isNotNull(config)){
			JSONArray array = JSON.parseArray(config);
			if(array!=null){
				Iterator<Object> iterator = array.iterator();
				while (iterator.hasNext()) {
					JSONObject object = (JSONObject) iterator.next();
					JSONObject initAction = object.getJSONObject("initAction");
					String number1 = object.getString("number");
					if(initAction!=null){
						JSONObject file = initAction.getJSONObject("file");
						if(file!=null){
							if(number1!=null && number1.equals(number)){
								Map<String, String> map = new HashMap<String, String>();
								map.put("fileName", file.getString("fileName"));
								map.put("filePath", file.getString("filePath"));
								return map;
							}
						}
					}
				}
			}
			
		}
		return null;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	
}