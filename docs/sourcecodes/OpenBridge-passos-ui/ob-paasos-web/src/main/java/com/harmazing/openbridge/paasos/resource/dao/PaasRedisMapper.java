package com.harmazing.openbridge.paasos.resource.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;
import com.harmazing.openbridge.paasos.resource.model.PaasRedis;

public interface PaasRedisMapper extends IBaseMapper {
	//查询记录总数
	int QueryPaasRedissByParamsCount(@Param("params")Map<String,Object> params);
	
	//查询分页的Redis信息
	List<PaasRedis> QueryPaasRedissByParams(@Param("params")Map<String,Object> params);
	
	
	//新增Redis信息
	void addPaasRedis(PaasRedis redis);
	
	//根据id查询Redis信息
	PaasRedis getPaasRedisInfoById(@Param("redisId")String redisId);
	
	//删除Redis信息
	void deletePaasRedisById(@Param("redisId")String redisId);
	
	//更新Redis信息
	void updatePaasRedisInfo(PaasRedis redis);
	
	//更多的条件查询 add on 20160527,用于资源共享
	//查询记录总数
	int QueryPaasRedissCount(@Param("params")Map<String,Object> params);
	//查询分页的Redis信息
	List<PaasRedis> QueryPaasRediss(@Param("params")Map<String,Object> params);
	
	ResourceQuota getAlreadyUsed(Map<String, Object> params);
}
