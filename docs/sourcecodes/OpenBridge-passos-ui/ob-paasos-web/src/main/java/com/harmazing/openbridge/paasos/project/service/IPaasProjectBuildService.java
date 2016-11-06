package com.harmazing.openbridge.paasos.project.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.paasos.project.model.PaasProjectBuild;

public interface IPaasProjectBuildService {
	
	public static final String IMAGE_PREFIX = "project/";

	void save(IUser user, PaasProjectBuild build);

	Page<Map<String, Object>> page(Map<String, Object> params);

	PaasProjectBuild findById(String buildId);

	void beginBuild(String buildId);

	void endBuild(String buildId, int status, String buildLogs);

	void endBuild(String buildId, int status, String buildLogs, String imageId);

	void deleteById(String buildId);

	void beginBuildDelete(String buildId);

	void endBuildDelete(String buildId, int deleteStatus, String log);

	List<PaasProjectBuild> findAvailableBuild(String projectId);

	void deleteByProjectId(String projectId);

	List<Map<String,Object>> findByVersionId(String versionId);

	Map<String,List<Map<String,String>>> getBuildImageByBusinessId(String businessId);

	List<Map<String, Object>> data(Map<String, String> param);

	List<Map<String, Object>> findAlreadBuildVersionList(String projectId);
	
	void deleteBuildById(String buildId);

}
