package com.lachesis.mnis.core.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ParamUtils {
	public static final String DEFAULT_SEPARATOR = ",";
	
	private ParamUtils() {}

	public static String getStringValue(Map<String, Object> paramMap, String key) {
		return getStringValue(paramMap, key, DEFAULT_SEPARATOR);
	}
	
	public static String getStringValue(Map<String, Object> paramMap, String key, String separator) {
		if (paramMap == null) {
			return "";
		}
		Object obj = paramMap.get(key);
		if (obj == null) {
			return "";
		}
//		if (obj.getClass().isArray()) {
//			obj = ((Object[]) obj)[0];
//		}
		if (obj instanceof String[]) {
			return joinIgnoreBlank((String[]) obj, separator);
		}
		return (String) obj;
	}
	
	public static String joinIgnoreBlank(String[] strs, String separator) {
		if (strs == null || strs.length == 0) {
			return "";
		} else if (strs.length == 1) {
			return strs[0];
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < strs.length; i++) {
				if (StringUtils.isNotBlank(strs[i])) {
					sb.append(strs[i]);
					if (i < strs.length - 1) {
						sb.append(separator);
					}
				}
			}
			return sb.toString();
		}
	}
}
