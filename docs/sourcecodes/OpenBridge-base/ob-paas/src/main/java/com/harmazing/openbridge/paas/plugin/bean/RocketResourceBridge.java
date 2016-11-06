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
public class RocketResourceBridge implements ResourceBridge {

	@Override
	public Map<String, String> getPageConfig(HttpServletRequest request) {
		Map<String, String> config = new HashMap<String, String>();
		config.put("version", request.getParameter("version"));
		config.put("memory", request.getParameter("memory"));
		return config;
	}

	@Override
	public Map<String, String> updateResource(String businessId,
			Map<String, String> pageConfig, Map<String, String> oldVal,String type) {

		return oldVal;
	} 
	
	/**
	 * 获取消息队列rest服务路径前缀
	 * @return
	 */
	private String getRocketRestPrefix(){
		String restUrlPrefix = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if(StringUtil.isNull(restUrlPrefix)){
			throw new RuntimeException("无法操作消息队列资源，缺少rest url配置");
		}
		String mqUrl = restUrlPrefix + (restUrlPrefix.endsWith("/")?"":"/")+"resource/messagequeue/";
		return mqUrl;
	}

	/**
	 * 调用paasOS接口申请RabbitMQ资源
	 */
	@Override
	public Map<String,String> applyReource(String userId, Map<String, String> commonConfig,
			Map<String, String> pageConfig) {
		commonConfig.putAll(pageConfig);
		String mqSaveUrl = getRocketRestPrefix()+"save.do";
		String restReponseStr = PaasAPIUtil.post(userId, mqSaveUrl, DataType.FORM, commonConfig);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					Map<String, String> data = new HashMap<String,String>();
					data.put("id", jsonResponse.getString("data"));
					return data;
				}
			}catch(Exception e){
				throw new RuntimeException("申请消息队列资源出错"+e.getMessage());
			}
		}
		throw new RuntimeException("申请消息队列资源出错");
	}
	
	/**
	 * 调用paasOS接口删除RabbitMQ资源
	 */
	@Override
	public void removeResource(String userId, String paasOsResId) {
		String mqDeletegUrl = getRocketRestPrefix() + "delete.do";
		Map<String,String> data = new HashMap<String,String>();
		data.put("resId", paasOsResId);
		String restReponseStr = PaasAPIUtil.post(userId, mqDeletegUrl, DataType.FORM, data);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					return;
				}
			}catch(Exception e){
				throw new RuntimeException("paasOS删除消息队列出错"+e.getMessage());
			}
		}
		throw new RuntimeException("paasOS删除消息队列出错");
	}

	/**
	 * 调用paasOS接口获取RabbitMQ资源参数
	 */
	@Override
	public JSONObject getResourceParams(String userId, String paasOsResId) {
		String mqConfigUrl = getRocketRestPrefix()+"getConfig.do?resId="+paasOsResId;
		String restReponseStr = PaasAPIUtil.get(userId, mqConfigUrl);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					return jsonResponse.getJSONObject("data");
				}
			}catch(Exception e){
				throw new RuntimeException("查询消息队列资源参数出错"+e.getMessage());
			}
		}
		throw new RuntimeException("无法获取消息队列资源参数");
	}

	@Override
	public JSONObject getResourceStatus(String userId, String paasOsResId) {
		String mqConfigUrl = getRocketRestPrefix() + "queryMQStatusInfo.do?resId="+paasOsResId;
		String restReponseStr = PaasAPIUtil.get(userId, mqConfigUrl);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				return jsonResponse;
			}catch(Exception e){
				throw new RuntimeException("查询消息队列状态参数出错"+e.getMessage());
			}
		}
		throw new RuntimeException("查询消息队列状态参数出错");
	}
}
