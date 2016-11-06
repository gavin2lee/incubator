/**
 * Project Name:ob-paasos-web
 * File Name:ConfigUtils.java
 * Package Name:com.harmazing.openbridge.paasos.utils
 * Date:2016年5月6日下午7:24:40
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.utils;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.service.impl.PaasEnvServiceImpl;
import com.harmazing.openbridge.paasos.project.service.impl.PaasProjectEnvServiceImpl;

/**
 * ClassName:ConfigUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月6日 下午7:24:40 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class ConfigUtils {
	
	//获取环境类别
	public static void setEnvType2Request(HttpServletRequest request){
		Map<String, String> envTypes = getEnvTypes();
		request.setAttribute("envTypes", envTypes);
		
		
	
	}

	public static Map<String, String> getEnvTypes(){
		return getEnvTypes("paasos.evntype");
	}
	public static Map<String, String> getEnvTypes(String key) {
		Map<String,String> envTypes = new HashMap<String,String>();
		String paasEnvType = ConfigUtil.getConfigString(key);
		if(StringUtil.isNotNull(paasEnvType)){
			String[] envTypeArray = StringUtil.split(paasEnvType);
			for(String envType: envTypeArray){
				String [] pairs = StringUtil.split(envType, "|");
				if(pairs!=null && pairs.length==2){
					envTypes.put(pairs[0], pairs[1]);
				}
			}
		}
		return envTypes;
	} 

}

