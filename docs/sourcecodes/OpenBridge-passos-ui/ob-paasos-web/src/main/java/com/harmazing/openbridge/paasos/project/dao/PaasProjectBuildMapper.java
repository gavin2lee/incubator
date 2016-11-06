package com.harmazing.openbridge.paasos.project.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.project.model.PaasProjectBuild;

public interface PaasProjectBuildMapper extends IBaseMapper{

	@Update("UPDATE  `os_project_build`  SET  `version_id` = #{versionId},`version_code` = #{versionCode},`file_path` = #{filePath},`file_name` = #{fileName},`build_name` = #{buildName},`build_tag` = #{buildTag},`port` = #{port},`build_no`=#{buildNo}  WHERE  `build_id` = #{buildId} ")
	public void update(PaasProjectBuild build);

	@Insert("INSERT INTO  `os_project_build`  (`build_id`,`image_id`,`build_logs`,`build_time`,`build_success`,`version_id`,`version_code`,`file_path`,`file_name`,`build_name`,`build_tag`,`port`,`create_user`,`create_date`,`status`,`project_id`,`image_name`,`build_no`) "+
					"VALUES ( #{buildId},#{imageId},#{buildLogs},#{buildTime},#{buildSuccess},#{versionId},#{versionCode},#{filePath},#{fileName},#{buildName},#{buildTag},#{port},#{createUser},#{createDate},#{status},#{projectId},#{imageName},#{buildNo} ) ")
	public void create(PaasProjectBuild build);

	public int pageCount(Map<String, Object> params);

	public Collection<? extends Map<String, Object>> page(Map<String, Object> params);

	@Select(" SELECT `build_id` buildId ,"+
			"     `image_id` imageId,"+
			"     `build_logs` buildLogs,"+
			"     `build_time` buildTime,"+
			"     `build_success` buildSuccess,"+
			"     `version_id` versionId,"+
			"     `version_code` versionCode,"+
			"     `file_path` filePath,"+
			"     `file_name` fileName,"+
			"     `build_name` buildName,"+
			"     `build_tag` buildTag,"+
			"     `port` port,"+
			"     `create_user` createUser,"+
			"     `create_date` createDate,"+
			"     `project_id` projectId,"+
			"     `delete_status` deleteStatus,"+
			"     `status` status ,"+
			"     `image_name` imageName " +
			" FROM `os_project_build`"+
			" where build_id = #{param} ")
	public PaasProjectBuild findById(String buildId);

	public void updateStatus(Map<String,Object> param);

	@Delete(" delete from os_project_build where  build_id = #{param} ")
	public void deleteById(String buildId);

	@Select(" SELECT `build_id` buildId ,"+
			"     `image_id` imageId,"+
			"     `build_logs` buildLogs,"+
			"     `build_time` buildTime,"+
			"     `build_success` buildSuccess,"+
			"     `version_id` versionId,"+
			"     `version_code` versionCode,"+
			"     `file_path` filePath,"+
			"     `file_name` fileName,"+
			"     `build_name` buildName,"+
			"     `build_tag` buildTag,"+
			"     `port` port,"+
			"     `create_user` createUser,"+
			"     `create_date` createDate,"+
			"     `project_id` projectId,"+
			"     `delete_status` deleteStatus,"+
			"     `status` status"+
			" FROM `os_project_build`"+
			" where project_id = #{projectId} and status=10 and delete_status is null order by build_time desc ")
	public List<PaasProjectBuild> findAvailableBuild(String projectId);

	public Integer findAlreadyCount(Map<String, Object> param);

	@Delete(" delete from os_project_build where  project_id = #{param}  ")
	public void deleteByProjectId(String param);

	@Select(" SELECT `build_id` buildId ,"+
			"     `image_id` imageId,"+
			"     `build_name` buildName" +
			" FROM `os_project_build`"+
			" where version_id = #{param} and status=10 and delete_status is null order by build_time desc ")
	public List<Map<String,Object>> findByVersionId(String param);

	public List<Map<String, String>> getBuildImageByBusinessId(String businessId);

	public List<Map<String, Object>> data(Map<String, String> param);

	public List<Map<String, Object>> findAlreadBuildVersionList(Map<String, Object> paraMap);

}
