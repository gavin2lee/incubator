package com.harmazing.openbridge.paasos.resource.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;
import com.harmazing.openbridge.paasos.resource.model.PaasMysql;

public interface PaasMysqlMapper extends IBaseMapper {
	//查询记录总数
	int QueryPaasMysqlsByParamsCount(@Param("params")Map<String,Object> params);
	
	//查询分页的mysql信息
	List<PaasMysql> QueryPaasMysqlsByParams(@Param("params")Map<String,Object> params);
	
	
	//新增mysql信息
	void addPaasMysql(PaasMysql mysql);
	
	//根据id查询mysql信息
	PaasMysql getPaasMysqlInfoById(@Param("mysqlId")String mysqlId);
	
	//删除mysql信息
	void deletePaasMysqlById(@Param("mysqlId")String mysqlId);
	
	//更新mysql信息
	void updatePaasMysqlInfo(PaasMysql mysql);
	
	//更多的条件查询 add on 20160527,用于资源共享
	//查询记录总数
	int QueryPaasMysqlsCount(@Param("params")Map<String,Object> params);
	//查询分页的mysql信息
	List<PaasMysql> QueryPaasMysqls(@Param("params")Map<String,Object> params);
	
	ResourceQuota getAlreadyUsed(Map<String, Object> params);
}
