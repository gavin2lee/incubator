package com.harmazing.openbridge.paas.plugin.bean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.plugin.xml.ResourceBridge;

@Service
public class Mysql51ResourceBridge implements ResourceBridge {

	

	/**
	 * 获取配置页面的配置参数，与mysql的config.jsp对应
	 */
	@Override
	public Map<String, String> getPageConfig(HttpServletRequest request) {
		Map<String, String> config = new HashMap<String, String>();
//		config.put("容量", request.getParameter("容量"));
		config.put("version", request.getParameter("version"));
		config.put("type", request.getParameter("type"));
		config.put("memory", request.getParameter("memory"));
		config.put("storage", request.getParameter("storage"));
		return config;
	}


	/**
	 * 暂时不存在修改
	 */
	@Override
	public Map<String, String> updateResource(String businessId,
			Map<String, String> pageConfig, Map<String, String> oldVal,
			String type) {
		return oldVal;
	}

	/**
	 * 获取mysql rest服务路径前缀
	 * @return
	 */
	private String getMysqlRestPrefix(){
		String restUrlPrefix = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if(StringUtil.isNull(restUrlPrefix)){
			throw new RuntimeException("无法操作mysql资源，缺少rest url配置");
		}
		String mysqlRestUrl = restUrlPrefix + (restUrlPrefix.endsWith("/")?"":"/")+"resource/mysql/";
		return mysqlRestUrl;
	}
	
	/**
	 * 申请mysql资源
	 * 调用paasOS的rest接口申请mysql
	 */
	@Override
	public Map<String,String> applyReource(String userId, Map<String, String> commonConfig,
			Map<String, String> pageConfig) {
		commonConfig.putAll(pageConfig);
		String mysqlSaveUrl = getMysqlRestPrefix() + "save.do";
		String restReponseStr = PaasAPIUtil.post(userId, mysqlSaveUrl, DataType.FORM, commonConfig);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					Map<String, String> data = new HashMap<String,String>();
					data.put("id", jsonResponse.getString("data"));
					return data;
				}
			}catch(Exception e){
				throw new RuntimeException("paasOS申请mysql资源出错"+e.getMessage());
			}
		}
		throw new RuntimeException("paasOS申请mysql资源出错");
	}
	
	/**
	 * 调用paasOS实现mysql资源的删除
	 */
	@Override
	public void removeResource(String userId, String paasOsResId) {
		String mysqlDeletegUrl = getMysqlRestPrefix() + "delete.do";
		Map<String,String> data = new HashMap<String,String>();
		data.put("resId", paasOsResId);
		String restReponseStr = PaasAPIUtil.post(userId, mysqlDeletegUrl, DataType.FORM, data);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					return;
				}
			}catch(Exception e){
				throw new RuntimeException("paasOS删除mysql出错"+e.getMessage());
			}
		}
		throw new RuntimeException("paasOS删除mysql出错");
	}

	/**
	 * 调用paasOS查询mysql的参数
	 */
	@Override
	public JSONObject getResourceParams(String userId,
			String paasOsResId) {
		String mysqlConfigUrl = getMysqlRestPrefix() + "getConfig.do?resId="+paasOsResId;
		String restReponseStr = PaasAPIUtil.get(userId, mysqlConfigUrl);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					return jsonResponse.getJSONObject("data");
				}
			}catch(Exception e){
				throw new RuntimeException("查询mysql资源参数出错"+e.getMessage());
			}
		}
		throw new RuntimeException("无法获取mysql资源参数");
	}


	@Override
	public JSONObject getResourceStatus(String userId, String paasOsResId) {
		String mysqlConfigUrl = getMysqlRestPrefix() + "queryMysqlStatusInfo.do?resId="+paasOsResId;
		String restReponseStr = PaasAPIUtil.get(userId, mysqlConfigUrl);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				return jsonResponse;
			}catch(Exception e){
				throw new RuntimeException("查询mysql状态参数出错"+e.getMessage());
			}
		}
		throw new RuntimeException("查询mysql状态参数出错");
	}
}
