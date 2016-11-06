package com.harmazing.openbridge.paas.plugin.bean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.plugin.xml.ResourceBridge;

@Service
public class RedisResourceBridge implements ResourceBridge {

	@Override
	public Map<String, String> getPageConfig(HttpServletRequest request) {
		Map<String, String> config = new HashMap<String, String>();
//		config.put("容量", request.getParameter("容量"));
//		config.put("类型", request.getParameter("类型"));
		config.put("version", request.getParameter("version"));
		config.put("memory", request.getParameter("memory"));
		return config;
	}

	@Override
	public Map<String, String> updateResource(String businessId,
			Map<String, String> pageConfig, Map<String, String> oldVal,
			String type) {
		return oldVal;
	}
	
	/**
	 * 获取高速缓存 rest服务路径前缀
	 * @return
	 */
	private String getRedisRestPrefix(){
		String restUrlPrefix = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if(StringUtil.isNull(restUrlPrefix)){
			throw new RuntimeException("无法操作Redis资源，缺少rest url配置");
		}
		String redisRestUrl = restUrlPrefix + (restUrlPrefix.endsWith("/")?"":"/")+"resource/redis/";
		return redisRestUrl;
	}

	/**
	 * 调用paasOS接口申请redis资源
	 */
	@Override
	public Map<String,String> applyReource(String userId, Map<String, String> commonConfig,
			Map<String, String> pageConfig) {
		commonConfig.putAll(pageConfig);
		String redisSaveUrl = getRedisRestPrefix() + "save.do";
		String restReponseStr = PaasAPIUtil.post(userId, redisSaveUrl, DataType.FORM, commonConfig);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					Map<String, String> data = new HashMap<String,String>();
					data.put("id", jsonResponse.getString("data"));
					return data;
				}
			}catch(Exception e){
				throw new RuntimeException("申请Redis资源出错"+e.getMessage());
			}
		}
		throw new RuntimeException("申请Redis资源出错");
	}
	
	/**
	 *  调用paasOS接口删除redis资源
	 */
	@Override
	public void removeResource(String userId, String paasOsResId) {
		String redisDeletegUrl = getRedisRestPrefix() + "delete.do";
		Map<String,String> data = new HashMap<String,String>();
		data.put("resId", paasOsResId);
		String restReponseStr = PaasAPIUtil.post(userId, redisDeletegUrl, DataType.FORM, data);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					return;
				}
			}catch(Exception e){
				throw new RuntimeException("paasOS删除redis出错"+e.getMessage());
			}
		}
		throw new RuntimeException("paasOS删除redis出错");
	}

	/**
	 *  调用paasOS接口获取redis资源参数
	 */
	@Override
	public JSONObject getResourceParams(String userId, String paasOsResId) {
		String redisConfigUrl = getRedisRestPrefix() + "getConfig.do?resId="+paasOsResId;
		String restReponseStr = PaasAPIUtil.get(userId, redisConfigUrl);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					return jsonResponse.getJSONObject("data");
				}
			}catch(Exception e){
				throw new RuntimeException("查询Redis资源参数出错"+e.getMessage());
			}
		}
		throw new RuntimeException("无法获取Redis资源参数");
	}

	@Override
	public JSONObject getResourceStatus(String userId, String paasOsResId) {
		String redisConfigUrl = getRedisRestPrefix() + "queryRedisStatusInfo.do?resId="+paasOsResId;
		String restReponseStr = PaasAPIUtil.get(userId, redisConfigUrl);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				return jsonResponse;
			}catch(Exception e){
				throw new RuntimeException("查询Redis状态参数出错"+e.getMessage());
			}
		}
		throw new RuntimeException("查询Redis状态参数出错");
	}
}
