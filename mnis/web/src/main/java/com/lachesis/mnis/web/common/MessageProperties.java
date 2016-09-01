package com.lachesis.mnis.web.common;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/***
 * 
 * 系统message读取
 *
 * @author yuliang.xu
 * @date 2015年5月14日 下午6:17:27 
 *
 */
public class MessageProperties {

	public static MessageProperties INSTANCE = new MessageProperties();

	public static MessageProperties getInstance() {
		return INSTANCE;
	}

	private Map<String, String> propMap = new HashMap<String, String>();

	private MessageProperties() {
		try {
			ResourceBundle m_resourceBundle = ResourceBundle.getBundle("message");

			for (String key : m_resourceBundle.keySet()) {
				String value = (String) m_resourceBundle.getObject(key);
				if (value != null)
					value = value.trim();
				propMap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setProperty(String key, String value) {
		propMap.put(key, value);
	}

	public String getProperty(String key) {
		String value = propMap.get(key);

		if (value == null) {
			System.out.println("AppProperties: property [" + key
					+ "] is missing ");
		}
		return value;
	}
}
