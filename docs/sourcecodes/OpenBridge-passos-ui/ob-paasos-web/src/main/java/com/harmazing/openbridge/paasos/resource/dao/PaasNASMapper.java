package com.harmazing.openbridge.paasos.resource.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;
import com.harmazing.openbridge.paasos.resource.model.PaasNAS;

public interface PaasNASMapper extends IBaseMapper {
	// 查询记录总数
	int QueryPaasNASByParamsCount(@Param("params") Map<String, Object> params);

	// 查询分页的Redis信息
	List<PaasNAS> QueryPaasNASByParams(
			@Param("params") Map<String, Object> params);

	// 新增NFS信息
	void addPaasNAS(PaasNAS nfs);

	// 根据id查询NFS信息
	PaasNAS getPaasNASInfoById(@Param("nasId") String nasId);

	// 删除NFS信息
	void deletePaasNASById(@Param("nasId") String nasId);

	// 更新NFS信息
	void updatePaasNASInfo(PaasNAS nfs);
	
	//更多的条件查询 add on 20160527,用于资源共享
	// 查询记录总数
	int QueryPaasNASsCount(@Param("params") Map<String, Object> params);
	// 查询分页的Redis信息
	List<PaasNAS> QueryPaasNASs(@Param("params") Map<String, Object> params);
	
	ResourceQuota getAlreadyUsed(Map<String, Object> params);
}
