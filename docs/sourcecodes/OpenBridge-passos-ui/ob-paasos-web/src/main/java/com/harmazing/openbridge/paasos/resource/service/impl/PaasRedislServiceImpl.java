package com.harmazing.openbridge.paasos.resource.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.harmazing.openbridge.paasos.project.model.vo.TwoTuple;
import com.harmazing.openbridge.paasos.resource.dao.PaasRedisMapper;
import com.harmazing.openbridge.paasos.resource.model.PaasRedis;
import com.harmazing.openbridge.paasos.resource.service.IPaasRedisService;
import com.harmazing.openbridge.paasos.resource.util.ResourceQuotaUtil;

@Service
public class PaasRedislServiceImpl implements IPaasRedisService {
	@Autowired
	private PaasRedisMapper redisMapper;
	
	private Map<String,Object> lock = Collections.synchronizedMap(new HashMap<String,Object>());
	
	private String getPassRedisRestUrlPrefix(){
		String paasOsRestfulUrl = ConfigUtil.getConfigString("paasos.resRestfulUrl");
		StringBuilder paasRestfulFinalUrl = new StringBuilder(paasOsRestfulUrl);
		if(!StringUtil.endsWithIgnoreCase(paasOsRestfulUrl, "/")){
			paasRestfulFinalUrl.append("/");
		}
		paasRestfulFinalUrl.append("db/redis/instances");
		return paasRestfulFinalUrl.toString();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PaasRedis> queryPaasRedissByParams(Map<String, Object> params) {
		Page<PaasRedis> PaasRedis = Page.create(params);
		PaasRedis.setRecordCount(redisMapper.QueryPaasRedissByParamsCount(params));
		PaasRedis.addAll(redisMapper.QueryPaasRedissByParams(params));
		return PaasRedis;
	}

	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
	public void addPaasRedisResource(PaasRedis redis) {
		
		if(redis.getMemory()==null){
			JSONObject mysqlConfig = JSONObject.parseObject(redis.getApplyContent());
			String memory = mysqlConfig.getString("memory");
			redis.setMemory(StorageUtil.getMemory(memory));
		}
		
		ResourceQuota apply = new ResourceQuota();
		apply.setCpu(redis.getCpu());
		apply.setMemory(redis.getMemory());
		apply.setStorage(redis.getStorage());
		apply.setCount(1L);
		
		if(!lock.containsKey(redis.getTenantId())){
			lock.put(redis.getTenantId(), new Object());
		}
		
		synchronized(lock.get(redis.getTenantId())){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tenantId", redis.getTenantId());
			ResourceQuota used = redisMapper.getAlreadyUsed(params);
			TwoTuple<Boolean, String> result = ResourceQuotaUtil.isSatisfy(apply, used, null, false, 
					redis.getEnvType(), redis.getTenantId(), "cache","redis");
			if(!result.a){
				throw new RuntimeException("租户配额不足:"+result.b);
			}
			
			redisMapper.addPaasRedis(redis);
			addPaasRedisValRest(redis);
		}
		
	}
	
	/**
	 * 新开事务发起rest请求创建Redis
	 * @param redis
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void addPaasRedisValRest(PaasRedis redis){
		String restResponse = "";
		try {
			String paasOsRestfulUrl = getPassRedisRestUrlPrefix();
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", redis.getCreater());
			headers.put("paasos-tenant-id", redis.getTenantId());
			restResponse = PaasAPIUtil.post(redis.getCreater(), paasOsRestfulUrl, headers, DataType.JSON, redis.getApplyContent());
		} catch (Exception e) {
			redisMapper.deletePaasRedisById(redis.getRedisId());
			throw new RuntimeException("添加实例失败");
		}
		JSONObject JSONRedis = JSONObject.parseObject(restResponse);
		redis.setUpdateDate(new Date());
		redis.setAllocateContent(JSONRedis.toJSONString());
		redis.setOnReady(true);
		redisMapper.updatePaasRedisInfo(redis);
	}

	@Override
	public PaasRedis getPaasRedisInfoById(String redisId) {
		return redisMapper.getPaasRedisInfoById(redisId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject deletePaasRedisById(String redisId,String userId, String tenantId) {
		try{
			PaasRedis redis = redisMapper.getPaasRedisInfoById(redisId);
			redisMapper.deletePaasRedisById(redisId);
			//调用paasOS接口删除数据库
			String paasOsRestfulUrl = getPassRedisRestUrlPrefix()+"/"+redis.getResId()+"/delete";
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
	public void updatePaasRedisInfo(PaasRedis redis) {
		//调用paasOS接口更新数据库
		redisMapper.updatePaasRedisInfo(redis);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject queryRedisInfo(String redisId, String userId,
			String tenantId) {
		try{
			PaasRedis redis = redisMapper.getPaasRedisInfoById(redisId);
			String paasOsRestfulUrl = getPassRedisRestUrlPrefix()+"/"+redis.getResId();
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String restResponse =PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
			JSONObject MySQLObj = JSONObject.parseObject(restResponse);
			return MySQLObj;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject startPaasRedis(String redisId, String userId,
			String tenantId) {
		try{
			PaasRedis redis = redisMapper.getPaasRedisInfoById(redisId);
			String paasOsRestfulUrl = getPassRedisRestUrlPrefix()+"/"+redis.getResId()+"/start";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String startRes = PaasAPIUtil.post(userId, paasOsRestfulUrl, headers, null, null);
			return JSONObject.parseObject(startRes);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject stopPaasRedis(String redisId, String userId,
			String tenantId) {
		try{
			PaasRedis redis = redisMapper.getPaasRedisInfoById(redisId);
			String paasOsRestfulUrl = getPassRedisRestUrlPrefix()+"/"+redis.getResId()+"/stop";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String stopRes = PaasAPIUtil.post(userId, paasOsRestfulUrl, headers, null, null);
			return JSONObject.parseObject(stopRes);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public JSONObject getPaasRedisOptions(String userId, String tenantId) {
		try{
			String paasOsRestfulUrl = ConfigUtil.getConfigString("paasos.resRestfulUrl");
			if(!StringUtil.endsWithIgnoreCase(paasOsRestfulUrl, "/")){
				paasOsRestfulUrl = paasOsRestfulUrl+"/";
			}
			paasOsRestfulUrl= paasOsRestfulUrl+"db/redis/options";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String RedisOptionStr = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
			return JSONObject.parseObject(RedisOptionStr);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public JSONObject getPaasRedisConfig(String redisId) {
		PaasRedis redis = redisMapper.getPaasRedisInfoById(redisId);
		JSONObject obj = new JSONObject();
		JSONObject allocateJson = JSONObject.parseObject(redis.getAllocateContent());
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
	public List<PaasRedis> queryPaasRediss(Map<String, Object> params) {
		Page<PaasRedis> PaasRedis = Page.create(params);
		PaasRedis.setRecordCount(redisMapper.QueryPaasRedissCount(params));
		PaasRedis.addAll(redisMapper.QueryPaasRediss(params));
		return PaasRedis;
	}

	@Override
	public JSONObject queryPaasRedisWithStatus(Map<String, Object> params,
			StringBuilder sb) {
		List<PaasRedis> rediss = redisMapper.QueryPaasRediss(params);
		String userId = (String) params.get("userId");
		String tenantId = (String) params.get("tenantId");
		JSONObject mysqlJSON = new JSONObject();
		int running=0, others=0;
		JSONArray runData = new JSONArray();
		JSONArray otherData = new JSONArray();
		if(rediss!=null && rediss.size()>0){
			for(PaasRedis redis: rediss){
				try{
					String paasOsRestfulUrl = getPassRedisRestUrlPrefix()+"/"+redis.getResId();
					Map<String,String> headers = new HashMap<String,String>();
					headers.put("paasos-user-id", userId);
					headers.put("paasos-tenant-id", tenantId);
					String restResponse = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
					JSONObject restObj = JSONObject.parseObject(restResponse);
					String status = restObj.getString("status");
					if("running".equals(status)){
						running++;
						JSONObject temp = new JSONObject();
						temp.put("id", redis.getRedisId());
						runData.add(temp);
					}else{
						others++;
						JSONObject temp = new JSONObject();
						temp.put("id", redis.getRedisId());
						otherData.add(temp);
					}
				}catch(Exception e){
					sb.append("\t查询状态出错:"+redis.getRedisId());
					others++;
					JSONObject temp = new JSONObject();
					temp.put("id", redis.getRedisId());
					otherData.add(temp);
				}
			}
			mysqlJSON.put("runningCount", running);
			mysqlJSON.put("runningData", runData);
			mysqlJSON.put("stoppedCount", others);
			mysqlJSON.put("stoppedData", otherData);
		}
		return mysqlJSON;
	}
}
