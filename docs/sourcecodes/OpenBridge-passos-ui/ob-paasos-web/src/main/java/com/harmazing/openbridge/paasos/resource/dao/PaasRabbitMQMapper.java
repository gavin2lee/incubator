package com.harmazing.openbridge.paasos.resource.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;
import com.harmazing.openbridge.paasos.resource.model.PaasRabbitMQ;

public interface PaasRabbitMQMapper extends IBaseMapper {
	//查询记录总数
	int QueryPaasRabbitMQsByParamsCount(@Param("params")Map<String,Object> params);
	
	//查询分页的消息队列信息
	List<PaasRabbitMQ> QueryPaasRabbitMQByParams(@Param("params")Map<String,Object> params);
	
	
	//新增消息队列信息
	void addPaasRabbitMq(PaasRabbitMQ paasMQ);
	
	//根据id查询消息队列信息
	PaasRabbitMQ getPaasRabbitMQInfoById(@Param("mqId")String mqId);
	
	//删除消息队列信息
	void deletePaasRabbitMQById(@Param("mqId")String mqId);
	
	//更新消息队列信息
	void updatePaasRabbitMQInfo(PaasRabbitMQ paasMQ);
	
	//更多的条件查询 add on 20160527,用于资源共享
	//查询记录总数
	int QueryPaasRabbitMQsCount(@Param("params")Map<String,Object> params);
	//查询分页的消息队列信息
	List<PaasRabbitMQ> QueryPaasRabbitMQs(@Param("params")Map<String,Object> params);
	
	ResourceQuota getAlreadyUsed(Map<String, Object> params);
}
