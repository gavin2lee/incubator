package com.harmazing.openbridge.paasos.resource.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.openbridge.paasos.resource.model.PaasRedis;

public interface IPaasRedisService {
	/**
	 * 查询Redis列表信息
	 * @param params
	 * @return
	 */
	List<PaasRedis> queryPaasRedissByParams(Map<String,Object> params);
	
	/**
	 * 根据id查询Redis的基本信息
	 * @param redisId
	 * @return
	 */
	PaasRedis getPaasRedisInfoById(String redisId);
	
	/**
	 * 新增Redis实例
	 * @param redis
	 */
	void addPaasRedisResource(PaasRedis redis);
	
	/**
	 * 删除Redis实例
	 * @param redisId
	 * @param userId
	 * @param tenantId
	 */
	
	JSONObject deletePaasRedisById(String redisId,String userId, String tenantId);
	
	/**
	 * 更新Redis信息
	 * @param redis
	 */
	void updatePaasRedisInfo(PaasRedis redis);
	
	/**
	 * 调用rest服务查询redis信息 返回 Redis 的JSON对象
	 * @param redisId
	 * @param userId
	 * @param tenantId
	 * @return Redis JSONObject
	 */
	JSONObject queryRedisInfo(String redislId, String userId, String tenantId);
	
	/**
	 * 调用rest服务启动Redis实例  
	 * @param redisId
	 * @param userId
	 * @param tenantId
	 */
	JSONObject startPaasRedis(String redisId, String userId, String tenantId);
	
	/**
	 * 调用reset服务停止Redis实例， 
	 * @param redisId
	 * @param userId
	 * @param tenantId
	 */
	JSONObject stopPaasRedis(String redisId, String userId, String tenantId);
	
	/**
	 * 查询当前租户可以申请Redis的资源配额信息，返回RedisOption的json对象
	 * @param userId
	 * @param tenantId
	 * @return RedisOption的json对象
	 */
	JSONObject getPaasRedisOptions(String userId, String tenantId);
	
	/**
	 * 通过id查询redis的使用参数信息
	 * @param userId
	 * @param tenantId
	 * @param redisId
	 * @return
	 */
	JSONObject getPaasRedisConfig(String redisId);
	
	/**
	 * 复杂条件查询redis列表信息
	 * @param params
	 * @return
	 */
	List<PaasRedis> queryPaasRediss(Map<String,Object> params);
	
	/**
	 * add on 20160528
	 * 查询redis实例及实时状态信息
	 * @param params
	 * @param sb
	 * @return
	 */
	JSONObject queryPaasRedisWithStatus(Map<String,Object> params, StringBuilder sb);
}
