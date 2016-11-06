package com.harmazing.openbridge.paas.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paas.framework.model.PaasFramework;

public interface PaasFrameworkMapper extends IBaseMapper {
	@Select("select * from paas_framework")
	List<PaasFramework> getPaasFrameworkList();

	@Select("select * from paas_framework where fw_id = #{fwId}")
	PaasFramework getPaasFrameworkById(@Param("fwId") String fwId);

	@Select("select * from paas_framework where fw_key = #{fwKey}")
	List<PaasFramework> getPaasFrameworkByKey(@Param("fwKey") String fwKey);
}
