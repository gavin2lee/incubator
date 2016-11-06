package com.harmazing.openbridge.paas.nginx.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paas.nginx.service.IPaasNginxService;
import com.harmazing.openbridge.paas.nginx.vo.NginxConfVo;

@Service
public class PaasNginxService implements IPaasNginxService {
	
	private String getNginxRestUrl() {
		String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if (!osPath.endsWith("/")) {
			osPath = osPath + "/";
		}
		return osPath + "paas/nginx/rest";
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
	public void createNginxConf(PaasNginxConf conf, IUser user,
			String platform, StringBuilder sb) throws Exception {
		String url = getNginxRestUrl()+"/add.do?platform="+platform;
		JSONObject entity = (JSONObject) JSONObject.toJSON(conf);
		String response = PaasAPIUtil.post(user.getUserId(), url, DataType.JSON, entity.toJSONString());
		JSONObject jsonRes = getResponseJSON(response);
	}

	@Override
	public void deleteNginxConf(String confId, IUser user) throws Exception {
		String url = getNginxRestUrl()+"/delete.do";
		Map<String,String> data = new HashMap<String,String>();
		data.put("confId", confId);
		String response = PaasAPIUtil.post(user.getUserId(), url, DataType.FORM, data);
		JSONObject jsonRes = getResponseJSON(response);
	}

	@Override
	public void updateNginxConf(PaasNginxConf conf, IUser user, StringBuilder sb)
			throws Exception {
		String url = getNginxRestUrl()+"/update.do";
		JSONObject entity = (JSONObject) JSONObject.toJSON(conf);
		String response = PaasAPIUtil.post(user.getUserId(), url, DataType.JSON, entity.toJSONString());
		JSONObject jsonRes = getResponseJSON(response);
	}

	@Override
	public PaasNginxConf findConfByConfId(String confId, String userId) {
		String url = getNginxRestUrl()+"/get.do?confId="+confId;
		String response = PaasAPIUtil.get(userId, url);
		JSONObject json = getResponseJSON(response);
		PaasNginxConf data = JSONObject.parseObject(json.getString("data"), PaasNginxConf.class);  
		return data;
	}

	@Override
	public NginxConfVo findConfVoByConfId(String confId, String userId) throws Exception {
		String url = getNginxRestUrl()+"/getvo.do?confId="+confId;
		String response = PaasAPIUtil.get(userId, url);
		JSONObject json = getResponseJSON(response);
		NginxConfVo data = JSONObject.parseObject(json.getString("data"), NginxConfVo.class);  
		return data;
	}

	@Override
	public List<NginxConfVo> findNginxConf(String serviceId, String envType, String userId)
			throws Exception {
		String url = getNginxRestUrl()+"/vo/list.do?serviceId="+serviceId+"&envType="+envType;
		String response = PaasAPIUtil.get(userId, url);
		JSONObject json = getResponseJSON(response);
		JSONObject data = json.getJSONObject("data");
		JSONArray array = data.getJSONArray("list");
		List<NginxConfVo> nginxs = new ArrayList<NginxConfVo>();
		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				String temp = array.getString(i);
				NginxConfVo nginxVo = JSONObject.parseObject(temp,
						NginxConfVo.class);
				nginxs.add(nginxVo);
			}
		}
		return nginxs;
	}

	@Override
	public List<NginxConfVo> getNginxListByEnvId(String envId, String userId) throws Exception {
		String url = getNginxRestUrl()+"/vo/list.do?envId="+envId;
		String response = PaasAPIUtil.get(userId, url);
		JSONObject json = getResponseJSON(response);
		JSONObject data = json.getJSONObject("data");
		JSONArray array = data.getJSONArray("list");
		List<NginxConfVo> nginxs = new ArrayList<NginxConfVo>();
		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				String temp = array.getString(i);
				NginxConfVo nginxVo = JSONObject.parseObject(temp,
						NginxConfVo.class);
				nginxs.add(nginxVo);
			}
		}
		return nginxs;
	}

	@Override
	public Page<PaasNginxConf> queryNginxPage(Map<String, Object> params, String userId) {
		String url = getNginxRestUrl()+"/instances.do?";
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
		Page<PaasNginxConf> pages = new Page<PaasNginxConf>();
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
				PaasNginxConf nginx = JSONObject.parseObject(temp,
						PaasNginxConf.class);
				pages.add(nginx);
			}
		}
		return pages;
	}
	
}
