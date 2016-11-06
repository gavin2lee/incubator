package com.harmazing.openbridge.paasos.resource.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.util.StorageUtil;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;
import com.harmazing.openbridge.paasos.project.model.vo.TwoTuple;
import com.harmazing.openbridge.paasos.resource.dao.PaasRabbitMQMapper;
import com.harmazing.openbridge.paasos.resource.model.PaasRabbitMQ;
import com.harmazing.openbridge.paasos.resource.service.IPaasMQService;
import com.harmazing.openbridge.paasos.resource.util.ResourceQuotaUtil;

@Service
public class PaasMQServiceImpl implements IPaasMQService {
	
	protected static final Log logger = LogFactory.getLog(PaasMQServiceImpl.class);
	
	@Autowired
	private PaasRabbitMQMapper mqMapper;
	
	@Autowired
	private IPaaSTenantService iPaaSTenantService;
	
	private Map<String,Object> lock = Collections.synchronizedMap(new HashMap<String,Object>());
	
	private String getPassMQRestUrlPrefix(){
		String paasOsRestfulUrl = ConfigUtil.getConfigString("paasos.resRestfulUrl");
		StringBuilder paasRestfulFinalUrl = new StringBuilder(paasOsRestfulUrl);
		if(!StringUtil.endsWithIgnoreCase(paasOsRestfulUrl, "/")){
			paasRestfulFinalUrl.append("/");
		}
		paasRestfulFinalUrl.append("mq/rabbitmq/instances");
		return paasRestfulFinalUrl.toString();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PaasRabbitMQ> queryPaasMQsByParams(Map<String, Object> params) {
		Page<PaasRabbitMQ> mqs = Page.create(params);
		mqs.setRecordCount(mqMapper.QueryPaasRabbitMQsByParamsCount(params));
		mqs.addAll(mqMapper.QueryPaasRabbitMQByParams(params));
		return mqs;
	}

	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
	public void addPaasMQResource(PaasRabbitMQ mq) {
		
		if(mq.getMemory()==null){
			JSONObject mysqlConfig = JSONObject.parseObject(mq.getApplyContent());
			String memory = mysqlConfig.getString("memory");
			mq.setMemory(StorageUtil.getMemory(memory));
		}
		
		ResourceQuota apply = new ResourceQuota();
		apply.setCpu(mq.getCpu());
		apply.setMemory(mq.getMemory());
		apply.setStorage(mq.getStorage());
		apply.setCount(1L);
		
		if(!lock.containsKey(mq.getTenantId())){
			lock.put(mq.getTenantId(), new Object());
		}
		synchronized(lock.get(mq.getTenantId())){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tenantId", mq.getTenantId());
			ResourceQuota used = mqMapper.getAlreadyUsed(params);
			TwoTuple<Boolean, String> result = ResourceQuotaUtil.isSatisfy(apply, used, null, false, 
					mq.getEnvType(), mq.getTenantId(), "mq","rabbitmq");
			if(!result.a){
				throw new RuntimeException("租户配额不足:"+result.b);
			}
			mqMapper.addPaasRabbitMq(mq);
			addPaasMQValRest(mq);
		}
	}
	
	/**
	 * 新开事务发起rest请求创建RabbitMQ
	 * @param mq
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void addPaasMQValRest(PaasRabbitMQ mq){
		String restResponse="";
		try {
			//调用paasOS接口申请RabbitMQ
			String paasOsRestfulUrl = getPassMQRestUrlPrefix();
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", mq.getCreater());
			headers.put("paasos-tenant-id", mq.getTenantId());
			logger.debug(paasOsRestfulUrl);
			restResponse = PaasAPIUtil.post(mq.getCreater(), paasOsRestfulUrl, headers, DataType.JSON, mq.getApplyContent());
			logger.debug(restResponse);
		} catch (Exception e) {
			mqMapper.deletePaasRabbitMQById(mq.getMqId());
			throw new RuntimeException("添加实例失败"+e.getMessage());
		}
		JSONObject JSONMysql = JSONObject.parseObject(restResponse);
		mq.setUpdateDate(new Date());
		mq.setAllocateContent(JSONMysql.toJSONString());
		mq.setOnReady(true);
		mqMapper.updatePaasRabbitMQInfo(mq);
	}

	@Override
	public PaasRabbitMQ getPaasMQInfoById(String mqId) {
		return mqMapper.getPaasRabbitMQInfoById(mqId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject deletePaasMQById(String mqId,String userId, String tenantId) {
		try{
			PaasRabbitMQ mq = mqMapper.getPaasRabbitMQInfoById(mqId);
			mqMapper.deletePaasRabbitMQById(mqId);
			//璋冪敤paasOS鎺ュ彛鍒犻櫎娑堟伅闃熷垪
			String paasOsRestfulUrl = getPassMQRestUrlPrefix()+"/"+mq.getResId()+"/delete";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String deleteRes = PaasAPIUtil.post(userId, paasOsRestfulUrl, headers, null, null);
			return JSONObject.parseObject(deleteRes);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePaasMQInfo(PaasRabbitMQ mq) {
		//璋冪敤paasOS鎺ュ彛鏇存柊鏁版嵁搴�
		mqMapper.updatePaasRabbitMQInfo(mq);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject queryMQInfo(String mqId, String userId,
			String tenantId) {
		try{
			PaasRabbitMQ mq = mqMapper.getPaasRabbitMQInfoById(mqId);
			String paasOsRestfulUrl = getPassMQRestUrlPrefix()+"/"+mq.getResId();
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String restResponse = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
			JSONObject MySQLObj = JSONObject.parseObject(restResponse);
			return MySQLObj;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject startPaasMQ(String mqId, String userId,
			String tenantId) {
		try{
			PaasRabbitMQ mq = mqMapper.getPaasRabbitMQInfoById(mqId);
			String paasOsRestfulUrl = getPassMQRestUrlPrefix()+"/"+mq.getResId()+"/start";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String startResponse = PaasAPIUtil.post(userId, paasOsRestfulUrl, headers, null, null);
			return JSONObject.parseObject(startResponse);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject stopPaasMQ(String mqId, String userId,
			String tenantId) {
		try{
			PaasRabbitMQ mq = mqMapper.getPaasRabbitMQInfoById(mqId);
			String paasOsRestfulUrl = getPassMQRestUrlPrefix()+"/"+mq.getResId()+"/stop";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String stopResponse = PaasAPIUtil.post(userId, paasOsRestfulUrl, headers, null, null);
			return JSONObject.parseObject(stopResponse);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public JSONObject getPaasRabbitMQOptions(String userId, String tenantId) {
		try{
			String paasOsRestfulUrl = ConfigUtil.getConfigString("paasos.resRestfulUrl");
			if(!StringUtil.endsWithIgnoreCase(paasOsRestfulUrl, "/")){
				paasOsRestfulUrl = paasOsRestfulUrl+"/";
			}
			paasOsRestfulUrl= paasOsRestfulUrl+"mq/rabbitmq/options";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String RabbitMQOptionStr = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
			return JSONObject.parseObject(RabbitMQOptionStr);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public JSONObject getPaasMQConfig(String mqId) {
		PaasRabbitMQ mq = mqMapper.getPaasRabbitMQInfoById(mqId);
		JSONObject obj = new JSONObject();
		JSONObject allocateJson = JSONObject.parseObject(mq.getAllocateContent());
		JSONObject connection = allocateJson.getJSONObject("connection");
		obj.put("username", connection.getString("account"));
		obj.put("password", connection.getString("password"));
		JSONObject serverJson = connection.getJSONObject("server");
		obj.put("externalIP", serverJson.getString("externalIP"));
		obj.put("externalPort", serverJson.getString("externalPort"));
		obj.put("clusterIP", serverJson.getString("clusterIP"));
		obj.put("clusterPort", serverJson.getString("port"));
		JSONObject manageJson = connection.getJSONObject("management");
		obj.put("manage.externalIP", manageJson.getString("externalIP"));
		obj.put("manage.externalPort", manageJson.getString("externalPort"));
		obj.put("manage.clusterIP", manageJson.getString("clusterIP"));
		obj.put("manage.clusterPort", manageJson.getString("port"));
		return obj;
	}

	@Override
	public List<PaasRabbitMQ> queryPaasMQs(Map<String, Object> params) {
		Page<PaasRabbitMQ> mqs = Page.create(params);
		mqs.setRecordCount(mqMapper.QueryPaasRabbitMQsCount(params));
		mqs.addAll(mqMapper.QueryPaasRabbitMQs(params));
		return mqs;
	}

	@Override
	public JSONObject queryPaasRabbitMQWithStatus(Map<String, Object> params,
			StringBuilder sb) {
		List<PaasRabbitMQ> mqs = mqMapper.QueryPaasRabbitMQs(params);
		String userId = (String) params.get("userId");
		String tenantId = (String) params.get("tenantId");
		JSONObject mqJSON = new JSONObject();
		int running=0, others=0;
		JSONArray runData = new JSONArray();
		JSONArray otherData = new JSONArray();
		if(mqs!=null && mqs.size()>0){
			for(PaasRabbitMQ mq: mqs){
				try{
					String paasOsRestfulUrl = getPassMQRestUrlPrefix()+"/"+mq.getResId();
					Map<String,String> headers = new HashMap<String,String>();
					headers.put("paasos-user-id", userId);
					headers.put("paasos-tenant-id", tenantId);
					String restResponse = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
					JSONObject restObj = JSONObject.parseObject(restResponse);
					String status = restObj.getString("status");
					if("running".equals(status)){
						running++;
						JSONObject temp = new JSONObject();
						temp.put("id", mq.getMqId());
						runData.add(temp);
					}else{
						others++;
						JSONObject temp = new JSONObject();
						temp.put("id", mq.getMqId());
						otherData.add(temp);
					}
				}catch(Exception e){
					sb.append("\t查询状态出错:"+mq.getMqId());
					others++;
					JSONObject temp = new JSONObject();
					temp.put("id", mq.getMqId());
					otherData.add(temp);
				}
			}
			mqJSON.put("runningCount", running);
			mqJSON.put("runningData", runData);
			mqJSON.put("stoppedCount", others);
			mqJSON.put("stoppedData", otherData);
		}
		return mqJSON;
	}

}
