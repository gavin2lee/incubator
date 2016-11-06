package com.harmazing.openbridge.paasos.resource.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.resource.model.PaasResource;

public interface PaasResourceMapper extends IBaseMapper {
	
	@Select("select * from os_resource where res_id=#{resId}")
	PaasResource getPaasResourceById(@Param("resId")String resouceId);

}
