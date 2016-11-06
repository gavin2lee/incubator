package com.harmazing.openbridge.paasos.resource.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.openbridge.paasos.resource.model.PaasRabbitMQ;

public interface IPaasMQService {
	/**
	 * 查询Mysql列表信息
	 * @param params
	 * @return
	 */
	List<PaasRabbitMQ> queryPaasMQsByParams(Map<String,Object> params);
	
	/**
	 * 根据id查询mysql的基本信息
	 * @param mqId
	 * @return
	 */
	PaasRabbitMQ getPaasMQInfoById(String mqId);
	
	/**
	 * 新增mq实例
	 * @param mq
	 */
	void addPaasMQResource(PaasRabbitMQ mq);
	
	/**
	 * 删除mq实例
	 * @param mqId
	 * @param userId
	 * @param tenantId
	 */
	
	JSONObject deletePaasMQById(String mqId,String userId, String tenantId);
	
	/**
	 * 更新mq信息
	 * @param mq
	 */
	void updatePaasMQInfo(PaasRabbitMQ mq);
	
	/**
	 * 调用rest服务查询mq信息 返回 RabbitMQ 的JSON对象
	 * @param mqId
	 * @param userId
	 * @param tenantId
	 * @return RabbitMQ JSONObject
	 */
	JSONObject queryMQInfo(String mysqlId, String userId, String tenantId);
	
	/**
	 * 调用rest服务启动mq实例  
	 * @param mqId
	 * @param userId
	 * @param tenantId
	 */
	JSONObject startPaasMQ(String mqId, String userId, String tenantId);
	
	/**
	 * 调用reset服务停止mq实例， 
	 * @param mqId
	 * @param userId
	 * @param tenantId
	 */
	JSONObject stopPaasMQ(String mqId, String userId, String tenantId);
	
	/**
	 * 查询当前租户可以申请RabbitMQ的资源配额信息，返回RabbitMQOption的json对象
	 * @param userId
	 * @param tenantId
	 * @return RabbitMQOption的json对象
	 */
	JSONObject getPaasRabbitMQOptions(String userId, String tenantId);
	
	/**
	 * 通过id查询RabbitMQ的使用参数信息
	 * @param userId
	 * @param tenantId
	 * @param mqId
	 * @return
	 */
	JSONObject getPaasMQConfig(String mqId);
	
	/**
	 * 复杂条件查询RabbitMQ列表信息，用于资源共享
	 * @param params
	 * @return
	 */
	List<PaasRabbitMQ> queryPaasMQs(Map<String,Object> params);
	
	/**
	 * add on 20160528
	 * 查询RabbitMQ实例及实时状态信息
	 * @param params
	 * @param sb
	 * @return
	 */
	JSONObject queryPaasRabbitMQWithStatus(Map<String,Object> params,StringBuilder sb);
}
