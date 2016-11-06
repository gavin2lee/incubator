package com.harmazing.openbridge.paasos.resource.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.openbridge.paasos.resource.model.PaasNAS;

public interface IPaasNASService {
	/**
	 * 查询NAS列表信息
	 * 
	 * @param params
	 * @return
	 */
	List<PaasNAS> queryPaasNASByParams(Map<String, Object> params);

	/**
	 * 根据id查询NFS的基本信息
	 * 
	 * @param nfsId
	 * @return
	 */
	PaasNAS getPaasNASInfoById(String nasId);

	/**
	 * 新增NAS实例
	 * 
	 * @param nas
	 */
	void addPaasNASResource(PaasNAS nas);

	/**
	 * 删除PaasNAS实例
	 * 
	 * @param NASId
	 * @param userId
	 * @param tenantId
	 */

	JSONObject deletePaasNASById(String NASId, String userId, String tenantId);

	/**
	 * 更新PaasNAS信息
	 * 
	 * @param redis
	 */
	void updatePaasNASInfo(PaasNAS NAS);

	/**
	 * 调用rest服务查询NAS信息 返回 NAS 的JSON对象
	 * 
	 * @param NASId
	 * @param userId
	 * @param tenantId
	 * @return PaasNAS JSONObject
	 */
	JSONObject queryNASInfo(String NASId, String userId, String tenantId);

	/**
	 * 查询当前租户可以申请NAS的资源配额信息，返回json对象
	 * 
	 * @param userId
	 * @param tenantId
	 * @return json对象
	 */
	JSONObject getPaasNASOptions(String userId, String tenantId, String nasType);

	/**
	 * 对共享存储授权
	 * 
	 * @param userId
	 * @param tenantId
	 * @param NAS
	 */
	void grantPaasNASAccess(String userId, String tenantId, PaasNAS NAS);

	/**
	 * 查询共享存储授权的IP网段信息
	 * 
	 * @param NASInfo
	 * @return
	 */
	JSONArray queryNASAccessIps(PaasNAS NASInfo);

	/**
	 * 对卷存储attach server
	 * 
	 * @param userId
	 * @param tenantId
	 * @param NAS
	 */
	void attachForVolume(String userId, String tenantId, PaasNAS NAS);

	/**
	 * 查询可挂载的服务器
	 * 
	 * @param NAS
	 * @return
	 */
	Map<String, String> queryAttachServerList(PaasNAS NAS);
	
	/**
	 * 通过id查询NAS的使用参数信息
	 * @param userId
	 * @param tenantId
	 * @param nasId
	 * @return
	 */
	JSONObject getPaasNASConfig(String nasId);
	
	/**
	 * 复杂条件查询NAS列表信息
	 * 
	 * @param params
	 * @return
	 */
	List<PaasNAS> queryPaasNASs(Map<String, Object> params);
}
