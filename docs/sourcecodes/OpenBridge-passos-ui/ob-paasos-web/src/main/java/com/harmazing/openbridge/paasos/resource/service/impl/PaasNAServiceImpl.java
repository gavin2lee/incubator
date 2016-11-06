package com.harmazing.openbridge.paasos.resource.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import com.harmazing.openbridge.paasos.resource.dao.PaasNASMapper;
import com.harmazing.openbridge.paasos.resource.model.PaasNAS;
import com.harmazing.openbridge.paasos.resource.service.IPaasNASService;
import com.harmazing.openbridge.paasos.resource.util.ResourceQuotaUtil;

@Service
public class PaasNAServiceImpl implements IPaasNASService {

	@Autowired
	private PaasNASMapper nasMapper; 
	protected final Logger logger = Logger.getLogger(this.getClass());
	private Map<String,Object> lock = Collections.synchronizedMap(new HashMap<String,Object>());

	private String getPassNASRestUrlPrefix(String type){
		String paasOsRestfulUrl = ConfigUtil.getConfigString("paasos.resRestfulUrl");
		StringBuilder paasRestfulFinalUrl = new StringBuilder(paasOsRestfulUrl);
		if(!StringUtil.endsWithIgnoreCase(paasOsRestfulUrl, "/")){
			paasRestfulFinalUrl.append("/");
		}
		paasRestfulFinalUrl.append("storage/");
		if("NFS".equals(type)){
			paasRestfulFinalUrl.append("nfs/instances");
		}else{
			//ISCSI
			paasRestfulFinalUrl.append("iscsi/instances");
		}
		return paasRestfulFinalUrl.toString();
	}
	
	// 映射网络存储的姿态
	private String mappingStatus(String osStatus, String type) {
		switch (osStatus) {
		case "creating":
			return "创建中";
		case "deleting":
			return "删除中";
		case "available":
			if (type.equals("NFS")) {
				return "可用";
			} else {
				return "等待挂载";
			}
		case "attaching":
			return "挂载中";
		case "in-use":
			return "可用";
		default:
			return "其他状态";
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<PaasNAS> queryPaasNASByParams(Map<String, Object> params) {
		Page<PaasNAS> PaasNAS = Page.create(params);
		PaasNAS.setRecordCount(nasMapper.QueryPaasNASByParamsCount(params));
		PaasNAS.addAll(nasMapper.QueryPaasNASByParams(params));
		return PaasNAS;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addPaasNASResource(PaasNAS nfs) {
		
		if(nfs.getStorage()==null){
			JSONObject mysqlConfig = JSONObject.parseObject(nfs.getApplyContent());
			String storage = mysqlConfig.getString("size");
			nfs.setStorage(StorageUtil.getMemory(storage));
		}
		
		ResourceQuota apply = new ResourceQuota();
		apply.setCpu(nfs.getCpu());
		apply.setMemory(nfs.getMemory());
		apply.setStorage(nfs.getStorage());
		apply.setCount(1L);
		
		if(!lock.containsKey(nfs.getTenantId())){
			lock.put(nfs.getTenantId(), new Object());
		}
		
		synchronized(lock.get(nfs.getTenantId())){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tenantId", nfs.getTenantId());
			ResourceQuota used = nasMapper.getAlreadyUsed(params);
			TwoTuple<Boolean, String> result = ResourceQuotaUtil.isSatisfy(apply, used, null, false, 
					nfs.getEnvType(), nfs.getTenantId(), "storage","nas");
			if(!result.a){
				throw new RuntimeException("租户配额不足:"+result.b);
			}
			
			nasMapper.addPaasNAS(nfs);
			addPaasNASValRest(nfs);
		}
		
	}

	/**
	 * 新开事务调用openstack接口创建NFS/Volume
	 * 
	 * @param nfs
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void addPaasNASValRest(PaasNAS nfs) {
		String restResponse = "";
		String type = nfs.getNasType();
		try {
			//调用paasOS接口申请NAs
			String paasOsRestfulUrl = getPassNASRestUrlPrefix(type);
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", nfs.getCreater());
			headers.put("paasos-tenant-id", nfs.getTenantId());
//			JSONObject apply = JSONObject.parseObject(nfs.getApplyContent());
//			String size = apply.getString("size");
//			String gbSize = size.substring(0, size.length()-1);
//			int mbSize = Integer.parseInt(gbSize)*1024;
//			apply.replace("size", mbSize);
			restResponse = PaasAPIUtil.post(nfs.getCreater(), paasOsRestfulUrl, headers, DataType.JSON, nfs.getApplyContent());
		} catch (Exception e) {
			nasMapper.deletePaasNASById(nfs.getNasId());
			throw new RuntimeException("添加实例失败");
		}
		JSONObject JSONNAS = JSONObject.parseObject(restResponse);
		JSONObject allocateContent = new JSONObject();
		allocateContent.put("id", JSONNAS.getString("id"));
		allocateContent.put("name", JSONNAS.getString("name"));
		allocateContent.put("protocol", JSONNAS.getString("type"));
		JSONObject allocateInfo = JSONNAS.getJSONObject("info");
		int mbSize = allocateInfo.getIntValue("size");
		allocateContent.put("size", mbSize/1024+"G");
		if("NFS".equals(type)){
			allocateContent.put("nfsServer", allocateInfo.getString("server"));
			allocateContent.put("nfsPath", allocateInfo.getString("path"));
		}else{
			allocateContent.put("iscsiPortal", allocateInfo.getString("targetPortal"));
			allocateContent.put("iscsiIQN", allocateInfo.getString("iqn"));
			allocateContent.put("iscsiLUN", allocateInfo.getString("lun"));
		}
		nfs.setUpdateDate(new Date());
		nfs.setAllocateContent(allocateContent.toJSONString());
		nfs.setOnReady(true);
		nasMapper.updatePaasNASInfo(nfs);
	}

	@Override
	public PaasNAS getPaasNASInfoById(String nfsId) {
		return nasMapper.getPaasNASInfoById(nfsId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject deletePaasNASById(String nfsId, String userId,
			String tenantId) {
		try {
			PaasNAS nfs = nasMapper.getPaasNASInfoById(nfsId);
			nasMapper.deletePaasNASById(nfsId);
			String type = nfs.getNasType();
			String resId = nfs.getResId();
			String paasOsRestfulUrl = getPassNASRestUrlPrefix(type)+"/"+resId+"/delete";
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String deleteResponse = PaasAPIUtil.post(userId,paasOsRestfulUrl,headers,null,null);
			return JSONObject.parseObject(deleteResponse);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePaasNASInfo(PaasNAS nfs) {
		// 调用Openstack接口更新
		nasMapper.updatePaasNASInfo(nfs);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONObject queryNASInfo(String nfsId, String userId, String tenantId) {
		try {
			PaasNAS nas = nasMapper.getPaasNASInfoById(nfsId);
			String resId = nas.getResId();
			String type = nas.getNasType();
			String paasOsRestfulUrl = getPassNASRestUrlPrefix(type)+"/"+resId;
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String restResponse = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
			JSONObject NASObj = JSONObject.parseObject(restResponse);
			logger.debug(paasOsRestfulUrl);
			return NASObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public JSONObject getPaasNASOptions(String userId, String tenantId,
			String nasType) {
		try {
			String paasOsRestfulUrl = ConfigUtil.getConfigString("paasos.resRestfulUrl");
			paasOsRestfulUrl = paasOsRestfulUrl + (paasOsRestfulUrl.endsWith("/")?"":"/")+"storage/";
			// 调用Openstack接口查询NFS的limit
			JSONObject json = new JSONObject();
			int leftStorageGB = 10000;// 剩余可用存储的空间
			int leftStorageNum = 100000;// 剩余可创建存储个数
			if (nasType.equals("NFS")) {
				paasOsRestfulUrl = paasOsRestfulUrl +"nfs/options";
			} else {
				paasOsRestfulUrl = paasOsRestfulUrl +"iscsi/options";
			}
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("paasos-user-id", userId);
			headers.put("paasos-tenant-id", tenantId);
			String restResponse = PaasAPIUtil.get(userId, paasOsRestfulUrl, headers);
			JSONArray NASObj = JSONArray.parseArray(restResponse);
			if(NASObj!=null && NASObj.size()>0){
				for(int i=0;i<NASObj.size();i++){
					JSONObject ob = NASObj.getJSONObject(i);
					int size = ob.getIntValue("free")/1024;
					if(size>50){
						ob.replace("free", new String[]{"1G","2G","5G","10G","50G"});
					}else if(size>10){
						ob.replace("free", new String[]{"1G","2G","5G","10G"});
					}else if(size>5){
						ob.replace("free", new String[]{"1G","2G","5G"});
					}else if(size>2){
						ob.replace("free", new String[]{"1G","2G"});
					}else if(size>1){
						ob.replace("free", new String[]{"1G"});
					}else{
						ob.replace("free", new String[]{});
					}
				}
				json.put("storage", NASObj);
			}else {
				json.put("storage", new String[] {});
			}

			json.put("version", new String[] { "V2" });
			return json;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void grantPaasNASAccess(String userId, String tenantId, PaasNAS nfs) {

	}

	// 对卷存储执行attach操作
	@Override
	public void attachForVolume(String userId, String tenantId, PaasNAS nfs) {
		// Cinder cinderClient= getCinderClient();
	}

	@Override
	public JSONArray queryNASAccessIps(PaasNAS NASInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> queryAttachServerList(PaasNAS NAS) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getPaasNASConfig(String nasId) {
		PaasNAS nas = nasMapper.getPaasNASInfoById(nasId);
		JSONObject allocateJson = JSONObject.parseObject(nas.getAllocateContent());
		return allocateJson;
	}

	@Override
	public List<PaasNAS> queryPaasNASs(Map<String, Object> params) {
		Page<PaasNAS> PaasNAS = Page.create(params);
		PaasNAS.setRecordCount(nasMapper.QueryPaasNASsCount(params));
		PaasNAS.addAll(nasMapper.QueryPaasNASs(params));
		return PaasNAS;
	}

}
