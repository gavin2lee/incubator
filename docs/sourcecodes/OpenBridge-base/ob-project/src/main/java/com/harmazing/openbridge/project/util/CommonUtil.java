package com.harmazing.openbridge.project.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;


public abstract class CommonUtil {
	
	/**
	 * @author weiyujia
	 * @Description: 将JSON串转换为Map
	 * @param @param resultJson
	 * @param @return
	 * @date 2016年3月14日 下午8:34:01
	 * @return
	 * @throws
	 */
	public static Map<String, Object> fromJsonToObject(String resultJson) {
		JSONObject jsonObject = JSONObject.parseObject(resultJson);
		Map<String, Object> resultList = parseJSON2List(jsonObject);
		return resultList;
	}

	/**
	 * @author weiyujia
	 * @Description: 将JSONObject转换为List
	 * @param @param jsonObject
	 * @param @return
	 * @date 2016年3月14日 上午11:55:53
	 * @return
	 * @throws
	 */
	private static Map<String, Object> parseJSON2List(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Set<?> keys = jsonObject.keySet();
		for (Object key : keys) {
			Object o = jsonObject.get(key);
			if (o instanceof JSONObject)
				map.put((String) key, parseJSON2List((JSONObject) o));
			else
				map.put((String) key, o);
		}
		return map;
	}

	
	
	/**
	 * @author weiyujia
	 * @Title:
	 * @Description: JSON HTML标签的转义，防止传到前端JS异常
	 * @param @param source
	 * @param @return
	 * @date 2016年3月17日 下午3:23:58
	 * @return
	 * @throws
	 */
	public static String htmlEncode(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\'':
				sb.append("\\'");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
