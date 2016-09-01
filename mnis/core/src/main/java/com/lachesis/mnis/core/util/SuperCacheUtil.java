package com.lachesis.mnis.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.bodysign.entity.BodySignConfig;
import com.lachesis.mnis.core.identity.entity.SysMenu;

/**
 * 数据缓存类
 * @author qingzhi.liu 2015.12.14
 *
 */
public class SuperCacheUtil {
	/**
	 * 系统缓存表数据
	 */
	public static Map<String, String> SYSTEM_CONFIGS = new HashMap<String,String>();
	
	public static Map<String,List<SysMenu>> MENU_CONFIG = new HashMap<String, List<SysMenu>>();
	
	/**
	 * 缓存BODY_SIGN_CONFIGS中的用户配置信息 map 第一个String 存放的为体征时间点配置信息, 
	 * 第二个List存放该类型的所有生命体征配置项信息
	 **/
	public static Map<String, BodySignConfig> BODY_SIGN_CONFIGS = new HashMap<String,BodySignConfig>();
	
	public static Map<String, Object> CACHE_DATA = new HashMap<String, Object>();

	public static Map<String, String> getSYSTEM_CONFIGS() {
		return SYSTEM_CONFIGS;
	}
	

	public static void setSYSTEM_CONFIGS(Map<String, String> sYSTEM_CONFIGS) {
		SYSTEM_CONFIGS = sYSTEM_CONFIGS;
	}


	public static Map<String, List<SysMenu>> getMENU_CONFIG() {
		return MENU_CONFIG;
	}


	public static void setMENU_CONFIG(Map<String, List<SysMenu>> mENU_CONFIG) {
		MENU_CONFIG = mENU_CONFIG;
	}


	public static Map<String, BodySignConfig> getBODY_SIGN_CONFIGS() {
		return BODY_SIGN_CONFIGS;
	}


	public static void setBODY_SIGN_CONFIGS(
			Map<String, BodySignConfig> bODY_SIGN_CONFIGS) {
		BODY_SIGN_CONFIGS = bODY_SIGN_CONFIGS;
	}
	
	/**
	 * 获取系统配置项目
	 * @param configId
	 * @return
	 */
	public static String getSystemConfig(String configId){
		return SYSTEM_CONFIGS.get(configId);
	}
}
