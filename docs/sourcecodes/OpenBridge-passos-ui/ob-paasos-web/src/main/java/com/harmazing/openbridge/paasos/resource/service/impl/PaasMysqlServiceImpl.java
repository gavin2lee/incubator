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
import org.springframework.util.StringUtils;

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
import com.harmazing.openbridge.paasos.resource.dao.PaasMysqlMapper;
import com.harmazing.openbridge.paasos.resource.model.PaasMysql;
import com.harmazing.openbridge.paasos.resource.service.IPaasMysqlService;
import com.harmazing.openbridge.paasos.resource.util.ResourceQuotaUtil;

@Service
public class PaasMysqlServiceImpl implements IPaasMysqlService {
	@Autowired
	private PaasMysqlMapper mysqlMapper;
	
	@Autowired
	private IPaaSTenantService iPaaSTenantService;
	
	private Map<String,Object> lock = Collections.synchronizedMap(new HashMap<String,Object>());
	
	private String getPassMysqlRestUrlPrefix(){
		String paasOsRestfulUrl = ConfigUtil.getConfigString("paasos.resRestfulUrl");
		StringBuilder paasRestfulFinalUrl = new StringBuilder(paasOsRestfulUrl);
		if(!StringUtil.endsWithIgnoreCase(paasOsRestfulUrl, "/")){
			paasRestfulFinalUrl.append("/");
		}
		paasRestfulFinalUrl.append("rdb/mysql/instances");
		return paasRestfulFinalUrl.toString();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PaasMysql> queryPaasMysqlsByParams(Map<String, Object> params) {
		Page<PaasMysql> mysqls = Page.create(params);
		mysqls.setRecordCount(mysqlMapper.QueryPaasMysqlsByParamsCount(params));
		mysqls.addAll(mysqlMapper.QueryPaasMysqlsByParams(params));
		return mysqls;
	}

	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
	public void addPaasMysqlResource(PaasMysql mysql) {
		
		if(mysql.getMemory()==null || mysql.getStorage()==null){
			JSONObject mysqlConfig = JSONObject.parseObject(mysql.getApplyContent());
			String memory = mysqlConfig.getString("memory");
			String storage = mysqlConfig.getString("storage");
			mysql.setMemory(StorageUtil.getMemory(memory));
			mysql.setStorage(StorageUtil.getMemory(storage));
		}
		ResourceQuota apply = new ResourceQuota();
		apply.setCpu(mysql.getCpu());
		apply.setMemory(mysql.getMemory());
		apply.setStorage(mysql.getStorage());
		apply.setCount(1L);
		
		if(!lock.containsKey(mysql.getTenantId())){
			lock.put(mysql.getTenantId(), new Object());
		}
		synchronized(lock.get(mysql.getTenantId())){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tenantId", mysql.getTenantId());
			params.put("mysqlType", "mysql");//mysql 集群还没实现 算配额先不算里面
			ResourceQuota used = mysqlMapper.getAlreadyUsed(params);
			TwoTuple<Boolean, String> result = ResourceQuotaUtil.isSatisfy(apply, used, null, false, 
					mysql.getEnvType(), mysql.getTenantId(), "database","mysql");
			if(!result.a){
				throw new RuntimeException("租户配额不足:"+result.b);
			}
			mysqlMapper.addPaasMysql(mysql);
			addPaasMysqlValRest(mysql);
		}
	}
	
	/**
	 * 新开事务发起rest请求创建数据库
	 * @param mysql
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void addPaasMysqlValRest(PaasMysql mysql){
		String restResponse = "";
		try {
			//调用paasOS接口申请数据库
			String paasOsRestfulUrl = getPassMysqlRestUrlPrefix();
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", mysql.getCreater());
			headers.put("paasos-tenant-id", mysql.getTenantId());
			restResponse = PaasAPIUtil.post(mysql.getCreater(), paasOsRestfulUrl, headers, DataType.JSON, mysql.getApplyContent());
		} catch (Exception e) {
			mysqlMapper.deletePaasMysqlById(mysql.getMysqlId());
			throw new RuntimeException("添加实例失败");
		}
		JSONObject JSONMysql = JSONObject.parseObject(restResponse);
		JSONObject allocateContent = new JSONObject();
		allocateContent.put("id", JSONMysql.getString("id"));
		allocateContent.put("attributes", JSONMysql.getString("attributes"));
		allocateContent.put("version", JSONMysql.getString("version"));
		allocateContent.put("memory", JSONMysql.getString("memory"));
		
		Double applyMemory = null;
		Double applyCpu = null;
		Double applyStorage = null;
		
		String memory = JSONMysql.getString("memory");
		if(StringUtils.hasText(memory)){
			applyMemory = StorageUtil.getMemory(memory);
		}
		
		allocateContent.put("storage", JSONMysql.getString("storage"));
		
		String storage = JSONMysql.getString("storage");
		if(StringUtils.hasText(storage)){
			applyStorage = StorageUtil.getMemory(storage);
		}
		
		
		allocateContent.put("cpu", JSONMysql.getString("cpu"));
		String cpu = JSONMysql.getString("cpu");
		if(StringUtils.hasText(cpu)){
			applyCpu = StorageUtil.getCpu(cpu);
		}
		
		mysql.setCpu(applyCpu);
		mysql.setMemory(applyMemory);
		mysql.setStorage(applyStorage);
		
		allocateContent.put("connection", JSONMysql.getJSONObject("connection"));
		if(JSONMysql.containsKey("backupPolicy")){
			allocateContent.put("backupPolicy", JSONMysql.getJSONObject("backupPolicy"));
		}
		mysql.setUpdateDate(new Date());
		mysql.setAllocateContent(allocateContent.toJSONString());
		mysql.setOnReady(true);
		mysqlMapper.updatePaasMysqlInfo(mysql);
	}

	@Override
	public PaasMysql getPaasMysqlInfoById(String mysqlId) {
		return mysqlMapper.getPaasMysqlInfoById(mysqlId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject deletePaasMysqlById(String mysqlId,String userId, String tenantId) {
		try{
			//调用paasOS接口删除数据库
			PaasMysql mysql = mysqlMapper.getPaasMysqlInfoById(mysqlId);
			mysqlMapper.deletePaasMysqlById(mysqlId);
			String paasOsRestfulUrl = getPassMysqlRestUrlPrefix()+"/"+mysql.getResId()+"/delete";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String deleteResponse = PaasAPIUtil.post(userId,paasOsRestfulUrl,headers,null,null);
			return JSONObject.parseObject(deleteResponse);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePaasMysqlInfo(PaasMysql mysql) {
		//调用paasOS接口更新数据库
		PaasMysql oldMysql = mysqlMapper.getPaasMysqlInfoById(mysql.getMysqlId());
		String paasOsRestfulUrl = getPassMysqlRestUrlPrefix()+"/"+oldMysql.getResId();
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("paasos-user-id", mysql.getCreater());
		headers.put("paasos-tenant-id", mysql.getTenantId());
		String restResponse = PaasAPIUtil.put(oldMysql.getCreater(), paasOsRestfulUrl, headers, DataType.JSON, mysql.getApplyContent());
		JSONObject JSONMysql = JSONObject.parseObject(restResponse);
		JSONObject allocateContent = new JSONObject();
		allocateContent.put("id", JSONMysql.getString("id"));
		allocateContent.put("attributes", JSONMysql.getString("attributes"));
		allocateContent.put("version", JSONMysql.getString("version"));
		allocateContent.put("memory", JSONMysql.getString("memory"));
		allocateContent.put("storage", JSONMysql.getString("storage"));
		allocateContent.put("cpu", JSONMysql.getString("cpu"));
		allocateContent.put("connection", JSONMysql.getString("connection"));
		allocateContent.put("backupPolicy", JSONMysql.getString("backupPolicy"));
		mysql.setUpdateDate(new Date());
		mysql.setAllocateContent(allocateContent.toJSONString());
		mysqlMapper.updatePaasMysqlInfo(mysql);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject queryMysqlInfo(String mysqlId, String userId,
			String tenantId) {
		try{
			PaasMysql mysql = mysqlMapper.getPaasMysqlInfoById(mysqlId);
			String paasOsRestfulUrl = getPassMysqlRestUrlPrefix()+"/"+mysql.getResId();
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
	public JSONObject startPaasMysql(String mysqlId, String userId,
			String tenantId) {
		try{
			PaasMysql mysql = mysqlMapper.getPaasMysqlInfoById(mysqlId);
			String paasOsRestfulUrl = getPassMysqlRestUrlPrefix()+"/"+mysql.getResId()+"/start";
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
	public JSONObject stopPaasMysql(String mysqlId, String userId,
			String tenantId) {
		try{
			PaasMysql mysql = mysqlMapper.getPaasMysqlInfoById(mysqlId);
			String paasOsRestfulUrl = getPassMysqlRestUrlPrefix()+"/"+mysql.getResId()+"/stop";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String stopResponse= PaasAPIUtil.post(userId, paasOsRestfulUrl, headers, null, null);
			return JSONObject.parseObject(stopResponse);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly=true)
	public JSONObject getPaasMysqlOptions(String userId, String tenantId) {
		try{
			String paasOsRestfulUrl = ConfigUtil.getConfigString("paasos.resRestfulUrl");
			if(!StringUtil.endsWithIgnoreCase(paasOsRestfulUrl, "/")){
				paasOsRestfulUrl = paasOsRestfulUrl+"/";
			}
			paasOsRestfulUrl= paasOsRestfulUrl+"rdb/mysql/options";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String MysqlOptionStr = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
			return JSONObject.parseObject(MysqlOptionStr);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public JSONObject getPaasMysqlConfig(String mysqlId) {
		PaasMysql mysql = mysqlMapper.getPaasMysqlInfoById(mysqlId);
		JSONObject obj = new JSONObject();
		JSONObject allocateJson = JSONObject.parseObject(mysql.getAllocateContent());
		JSONObject connection = allocateJson.getJSONObject("connection");
		obj.put("username", connection.getString("account"));
		obj.put("password", connection.getString("password"));
		obj.put("externalIP", connection.getString("externalIP"));
		obj.put("externalPort", connection.getString("externalPort"));
		obj.put("clusterIP", connection.getString("clusterIP"));
		obj.put("clusterPort", connection.getString("port"));
		if(connection.containsKey("slave")){
			obj.put("slave", "true");
			JSONObject slaveJson= connection.getJSONObject("slave");
			obj.put("slave.externalIP", slaveJson.getString("externalIP"));
			obj.put("slave.externalPort", slaveJson.getString("externalPort"));
			obj.put("slave.clusterIP", slaveJson.getString("clusterIP"));
			obj.put("slave.clusterPort", slaveJson.getString("port"));
		}
		return obj;
	}

	//资源共享时动态参数查询
	@Override
	public List<PaasMysql> queryPaasMysqls(Map<String, Object> params) {
		Page<PaasMysql> mysqls = Page.create(params);
		mysqls.setRecordCount(mysqlMapper.QueryPaasMysqlsCount(params));
		mysqls.addAll(mysqlMapper.QueryPaasMysqls(params));
		return mysqls;
	}

	@Override
	public JSONObject queryPaasMysqlWithStatus(Map<String, Object> params,StringBuilder sb) {
		List<PaasMysql> mysqls = mysqlMapper.QueryPaasMysqlsByParams(params);
		String userId = (String) params.get("userId");
		String tenantId = (String) params.get("tenantId");
		JSONObject mysqlJSON = new JSONObject();
		int running=0, others=0;
		JSONArray runData = new JSONArray();
		JSONArray otherData = new JSONArray();
		if(mysqls!=null && mysqls.size()>0){
			for(PaasMysql mysql: mysqls){
				try{
					String paasOsRestfulUrl = getPassMysqlRestUrlPrefix()+"/"+mysql.getResId();
					Map<String,String> headers = new HashMap<String,String>();
					headers.put("paasos-user-id", userId);
					headers.put("paasos-tenant-id", tenantId);
					String restResponse = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
					JSONObject restObj = JSONObject.parseObject(restResponse);
					String status = restObj.getString("status");
					if("running".equals(status)){
						running++;
						JSONObject temp = new JSONObject();
						temp.put("id", mysql.getMysqlId());
						runData.add(temp);
					}else{
						others++;
						JSONObject temp = new JSONObject();
						temp.put("id", mysql.getMysqlId());
						otherData.add(temp);
					}
				}catch(Exception e){
					sb.append("\t查询状态出错:"+mysql.getMysqlId());
					others++;
					JSONObject temp = new JSONObject();
					temp.put("id", mysql.getMysqlId());
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
