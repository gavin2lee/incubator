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
public class NasResourceBridge implements ResourceBridge {

	@Override
	public Map<String, String> getPageConfig(HttpServletRequest request) {
		Map<String, String> config = new HashMap<String, String>();
//		config.put("容量", request.getParameter("容量"));
		config.put("storage", request.getParameter("storage"));
		return config;
	}

	@Override
	public Map<String, String> updateResource(String businessId,
			Map<String, String> pageConfig, Map<String, String> oldVal,
			String type) {
		return oldVal;
	}
	
	/**
	 * 获取网络存储rest服务路径前缀
	 * @return
	 */
	private String getNasRestPrefix(){
		String restUrlPrefix = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if(StringUtil.isNull(restUrlPrefix)){
			throw new RuntimeException("无法操作网络存储资源，缺少rest url配置");
		}
		String nasRestUrl = restUrlPrefix + (restUrlPrefix.endsWith("/")?"":"/")+"resource/nas/nfs/";
		return nasRestUrl;
	}

	/**
	 *申请网络存储资源
	 *调用paasOS的接口申请网络存储 
	 */
	@Override
	public Map<String,String> applyReource(String userId, Map<String, String> commonConfig,
			Map<String, String> pageConfig) {
		commonConfig.putAll(pageConfig);
		commonConfig.put("nasType", "NFS");
		commonConfig.put("source", "paasOS-UI");
		String nasSaveUrl = getNasRestPrefix() + "save.do";
		String restReponseStr = PaasAPIUtil.post(userId, nasSaveUrl, DataType.FORM, commonConfig);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					Map<String, String> data = new HashMap<String,String>();
					data.put("id", jsonResponse.getString("data"));
					return data;
				}
			}catch(Exception e){
				throw new RuntimeException("paasOS申请网络存储资源出错"+e.getMessage());
			}
		}
		throw new RuntimeException("paasOS申请网络存储资源出错");
	}
	
	/**
	 * 调用paasOS的接口删除网络存储资源
	 */
	@Override
	public void removeResource(String userId, String paasOsResId) {
		String nasDeletegUrl = getNasRestPrefix() + "delete.do";
		Map<String,String> data = new HashMap<String,String>();
		data.put("resId", paasOsResId);
		String restReponseStr = PaasAPIUtil.post(userId, nasDeletegUrl, DataType.FORM, data);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					return;
				}
			}catch(Exception e){
				throw new RuntimeException("paasOS删除网络存储出错"+e.getMessage());
			}
		}
		throw new RuntimeException("paasOS删除网络存储出错");
	}

	/**
	 * 调用paasOS的接口获取网络存储的参数
	 */
	@Override
	public JSONObject getResourceParams(String userId,
			String paasOsResId) {
		String nasConfigUrl = getNasRestPrefix() + "getConfig.do?resId="+paasOsResId;
		String restReponseStr = PaasAPIUtil.get(userId, nasConfigUrl);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				if(jsonResponse.containsKey("code")&&jsonResponse.getString("code").equals("0")){
					return jsonResponse.getJSONObject("data");
				}
			}catch(Exception e){
				throw new RuntimeException("查询网络存储资源参数出错"+e.getMessage());
			}
		}
		throw new RuntimeException("无法获取网络存储资源参数");
	}

	@Override
	public JSONObject getResourceStatus(String userId, String paasOsResId) {
		String nasConfigUrl = getNasRestPrefix() + "queryNasStatusInfo.do?resId="+paasOsResId;
		String restReponseStr = PaasAPIUtil.get(userId, nasConfigUrl);
		if(StringUtil.isNotNull(restReponseStr)){
			try{
				JSONObject jsonResponse = JSONObject.parseObject(restReponseStr);
				return jsonResponse;
			}catch(Exception e){
				throw new RuntimeException("查询网络存储资源参数出错"+e.getMessage());
			}
		}
		throw new RuntimeException("无法获取网络存储资源参数");
	}
}
