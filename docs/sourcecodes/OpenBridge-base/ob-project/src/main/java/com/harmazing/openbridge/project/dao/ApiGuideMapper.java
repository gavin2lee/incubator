package com.harmazing.openbridge.project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.project.vo.ApiVersionProtocol;

public interface ApiGuideMapper extends IBaseMapper{

	public List<ApiVersionProtocol> getDepenciesApiInfoList(@Param("versionId") String versionId, @Param("type")String type);

}
