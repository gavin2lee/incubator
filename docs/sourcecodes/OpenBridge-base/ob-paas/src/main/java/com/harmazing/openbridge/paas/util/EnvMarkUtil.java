package com.harmazing.openbridge.paas.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;

public class EnvMarkUtil {
	
	private static Map<String,String> ENV_MARK = null;
	
	private final static Object lock = new Object();
	
	private final static EnvMarkUtil MARKUTIL = new EnvMarkUtil();
	
	public static String get(String key){
		if(ENV_MARK==null){
//			String envType = request.getParameter("envType");
			MARKUTIL.init();
		}
		return ENV_MARK.get(key);
	}
	
	private void init(){
		synchronized(lock){
			ENV_MARK = new HashMap<String, String>();
			String remark = ConfigUtil.getConfigString("paasos.evnmark");
			if(StringUtils.hasText(remark)){
				JSONObject jo = JSONObject.parseObject(remark);
				for(String k : jo.keySet()){
					JSONArray data = jo.getJSONArray(k);
					List<Map> envmark = JSONObject.parseArray(data.toJSONString(), Map.class);
					for(Map m : envmark){
						String code = m.get("code")+"";
						String name = m.get("name")+"";
						ENV_MARK.put(code, name);
					}
				}
			}
		}
	}
	
	public static Map<String,String> getEnvMark(){
		if(ENV_MARK==null){
//			String envType = request.getParameter("envType");
			MARKUTIL.init();
		}
		return ENV_MARK;
	}

}
