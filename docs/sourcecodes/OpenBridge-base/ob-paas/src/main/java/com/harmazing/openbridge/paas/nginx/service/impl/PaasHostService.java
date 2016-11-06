package com.harmazing.openbridge.paas.nginx.service.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;
import com.harmazing.openbridge.paas.nginx.service.IPaasHostService;

@Service
public class PaasHostService implements IPaasHostService {
	
	private String getNginxRestUrl() {
		String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if (!osPath.endsWith("/")) {
			osPath = osPath + "/";
		}
		return osPath + "paas/nginx/rest/host";
	}

	private JSONObject getResponseJSON(String response) {
		if (StringUtil.isNull(response)) {
			throw new RuntimeException("无返回数据");
		}
		JSONObject json = JSONObject.parseObject(response);
		if (json == null || json.isEmpty()) {
			throw new RuntimeException("无返回数据");
		}
		if (json.containsKey("code") && json.getString("code").equals("0")) {
			return json;
		} else {
			throw new RuntimeException(json.getString("msg"));
		}
	}

	@Override
	public PaasHost getHostById(String hostId, String userId) {
		String url = getNginxRestUrl()+"/get.do?hostId="+hostId;
		String response = PaasAPIUtil.get(userId, url);
		JSONObject json = getResponseJSON(response);
		PaasHost host = JSONObject.parseObject(json.getString("data"), PaasHost.class);  
		return host;
	}

	@Override
	public Page<PaasHost> queryHostPage(Map<String, Object> params, String userId) {
		String url = getNginxRestUrl()+"/list.do?";
		if(params!=null && params.size()>0){
			Set<String> keys = params.keySet();
			for(String key: keys ){
				url = url + key +"="+params.get(key)+"&";
			}
			url = url.substring(0,url.length()-1);
		}
		String response = PaasAPIUtil.get(userId, url);
		JSONObject json = getResponseJSON(response);
		JSONObject data = json.getJSONObject("data");
		Page<PaasHost> pages = new Page<PaasHost>();
		int pageNo = (int)params.get("pageNo");
		int pageSize = (int)params.get("pageSize");
		int start = (pageNo - 1) * pageSize;
		pages.setPageNo(pageNo);
		pages.setPageSize(pageSize);
		pages.setStart(start);
		pages.setRecordCount(data.getIntValue("recordCount"));
		JSONArray array = data.getJSONArray("list");
		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				String temp = array.getString(i);
				PaasHost host = JSONObject.parseObject(temp,
						PaasHost.class);
				pages.add(host);
			}
		}
		return pages;
	}

	@Override
	public void addHost(PaasHost host, String userId) {
		String url = getNginxRestUrl()+"/add.do";
		JSONObject json = (JSONObject) JSONObject.toJSON(host);
		String response = PaasAPIUtil.post(userId, url, DataType.JSON, json.toJSONString());
		JSONObject jsonRes = getResponseJSON(response);
	}

	@Override
	public void updateHost(PaasHost host, String userId) {
		String url = getNginxRestUrl()+"/update.do";
		JSONObject json = (JSONObject) JSONObject.toJSON(host);
		String response = PaasAPIUtil.post(userId, url, DataType.JSON,json.toJSONString() );
		JSONObject jsonRes = getResponseJSON(response);
	}

}
