package com.harmazing.openbridge.project.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.project.vo.ScmInfo;

public interface IBuildService extends IBaseService {

	List<String> getProjectVersionFile(String projectId, String versionId);

	String getAssemblyFolder(String projectId, String versionId);
 
	String buildServiceSource(String projectId, String versionId,
			String scmUrl, ScmInfo scmInfo, StringBuilder result,String output);
	public String buildServiceSource(String projectId, String versionId,
			String scmUrl, ScmInfo scmInfo, StringBuilder result,
			String outputFile,Map<String, String> param);
}
