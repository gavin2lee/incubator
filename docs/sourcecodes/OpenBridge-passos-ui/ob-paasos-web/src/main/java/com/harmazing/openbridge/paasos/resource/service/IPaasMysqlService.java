package com.harmazing.openbridge.paasos.resource.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.openbridge.paasos.resource.model.PaasMysql;

public interface IPaasMysqlService {
	/**
	 * 查询Mysql列表信息
	 * @param params
	 * @return
	 */
	List<PaasMysql> queryPaasMysqlsByParams(Map<String,Object> params);
	
	/**
	 * 根据id查询mysql的基本信息
	 * @param mysqlId
	 * @return
	 */
	PaasMysql getPaasMysqlInfoById(String mysqlId);
	
	/**
	 * 新增mysql实例
	 * @param mysql
	 */
	void addPaasMysqlResource(PaasMysql mysql);
	
	/**
	 * 删除mysql实例
	 * @param mysqlId
	 */
	
	JSONObject deletePaasMysqlById(String mysqlId,String userId, String tenantId);
	
	/**
	 * 更新mysql信息
	 * @param mysql
	 */
	void updatePaasMysqlInfo(PaasMysql mysql);
	
	/**
	 * 查询mysql信息 返回 MySQL 的JSON对象
	 * @param mysqlId
	 * @param userId
	 * @param tenantId
	 * @return MySQL JSONObject
	 */
	JSONObject queryMysqlInfo(String mysqlId, String userId, String tenantId);
	
	/**
	 * 启动mysql实例  
	 * @param mysqlId
	 * @param userId
	 * @param tenantId
	 */
	JSONObject startPaasMysql(String mysqlId, String userId, String tenantId);
	
	/**
	 * 停止mysql实例， 
	 * @param mysqlId
	 * @param userId
	 * @param tenantId
	 */
	JSONObject stopPaasMysql(String mysqlId, String userId, String tenantId);
	
	/**
	 * 查询当前租户可以申请mysql的资源配额信息，返回MysqlOption的json对象
	 * @param userId
	 * @param tenantId
	 * @return MysqlOption的json对象
	 */
	JSONObject getPaasMysqlOptions(String userId, String tenantId);
	
	/**
	 * 通过id查询mysql的使用参数信息
	 * @param userId
	 * @param tenantId
	 * @param mysqlId
	 * @return
	 */
	JSONObject getPaasMysqlConfig(String mysqlId);
	
	/**
	 * 复杂条件查询Mysql列表信息
	 * @param params
	 * @return
	 */
	List<PaasMysql> queryPaasMysqls(Map<String,Object> params);
	
	/**
	 * add on 20160528
	 * 查询mysql实例及实时状态信息
	 * @param params
	 * @param sb
	 * @return
	 */
	JSONObject queryPaasMysqlWithStatus(Map<String,Object> params,StringBuilder sb);
}
