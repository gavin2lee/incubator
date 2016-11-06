package com.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harmazing.framework.util.JsonUtil;

public class CServiceShitTest {
Map<String, Object> obj = new HashMap<String, Object>();
	
	public static CServiceShitTest create(String key, Object value){
		return new CServiceShitTest().put(key, value);
	}
	
	public static CServiceShitTest create(String json){
		return new CServiceShitTest().parse(json);
	}
	
	public CServiceShitTest put(String key, Object value){
		obj.put(key, value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public CServiceShitTest parse(String json) {
		ObjectMapper mapper = new ObjectMapper();  
		Map<String, Object> tmp = null;
		try {
			tmp = mapper.readValue(json, HashMap.class);
			for (Object key: tmp.keySet()) {
				obj.put((String) key, tmp.get(key));
			}
		} catch (Exception e) {
		} 

		return this;
	}
	
	public String toJson(){
		String json = "";
		try {
			json = JsonUtil.toJsonString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public Map<String, Object> toMap(){
		return obj;
	}
	
	@Test
	public void test() throws Exception{
		Map<String, String> obj = new HashMap<>();
		obj.put("name", "test_"+System.currentTimeMillis());
		String json = JsonUtil.toJsonString(obj);
		System.out.println(json);
		
		String json2 = CServiceShitTest.create("haha", 123)
							.put("hheh", "789")
							.toJson();
		
		System.out.println(json2);	
		
		Map<String, Object> o2 = CServiceShitTest.create(json2).toMap();
		System.out.println(o2.get("haha"));
	}
}
