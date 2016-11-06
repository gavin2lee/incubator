package com.harmazing.framework.util;

public abstract class SpringUtil {
	public static Object getBean(String name) {
		return WebUtil.getApplicationContext().getBean(name);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return WebUtil.getApplicationContext().getBean(clazz);
	}

	public static boolean containsBean(String name) {
		return WebUtil.getApplicationContext().containsBean(name);
	}
}
