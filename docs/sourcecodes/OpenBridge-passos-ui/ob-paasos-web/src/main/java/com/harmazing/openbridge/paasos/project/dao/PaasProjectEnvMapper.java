package com.harmazing.openbridge.paasos.project.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;

public interface PaasProjectEnvMapper extends IBaseMapper {
	@Insert("insert into os_project_env(env_id,env_name,env_type,project_id,business_type,creator,creation_time,env_mark)"
			+ " values(#{envId},#{envName},#{envType},#{projectId},#{businessType},#{creator},#{creationTime},#{envMark})")
	void createEnv(PaasEnv environment);

	@Insert("insert into os_project_env_res(record_id,env_id,resource_id,apply_config,result_config,res_addition,paasos_res_id) values(#{recordId},#{envId},#{resourceId},#{applyConfig},#{resultConfig},#{resAddition},#{paasosResId})")
	void addEnvResource(PaasEnvResource resource);
	
	@Update("update os_project_env_res set apply_config=#{applyConfig},result_config=#{resultConfig},paasos_res_id=#{paasosResId} where record_id=#{recordId}")
	void updateEvnResource(PaasEnvResource resource);

	@Select("select * from os_project_env_res where record_id=#{recordId}")
	PaasEnvResource getEnvResourceById(@Param("recordId") String recordId);

	@Delete("delete from os_project_env_res where env_id=#{envId} and resource_id=#{resourceId}")
	void delEnvResource(@Param("envId") String evnId,
			@Param("resourceId") String resourceId);
	
	@Delete("delete from os_project_env_res where record_id=#{recordId}")
	void delEnvResourceById(@Param("recordId") String recordId);

	@Select("select a.*,b.business_id from os_project_env a,os_project b where a.env_id=#{envId} and a.project_id=b.project_id")
	PaasEnv getEnvById(@Param("envId") String envId);

	@Select("select * from os_project_env where project_id=#{projectId} and env_type = #{envType} and env_name=#{envName}")
	PaasEnv getEnvByName(@Param("projectId") String businessId,
			@Param("envType") String envType, @Param("envName") String envName);

	@Select("select * from os_project_env_res where env_id=#{envId}")
	List<PaasEnvResource> getEnvResources(@Param("envId") String envId);

	@Select("select a.*,b.business_id from os_project_env a,os_project b where a.project_id=#{projectId} and a.project_id=b.project_id")
	List<PaasEnv> getEnvListByBusinessId(@Param("projectId") String businessId);
	
	@Select("<script>select * from os_project_env where project_id in <foreach collection=\"list\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\"> #{item}</foreach></script>")
	List<PaasEnv> getEnvListByBusinessIds(@Param("list") List<String> list);

	@Update("update os_project_env set env_name=#{envName},env_mark=#{envMark} where env_id=#{envId}")
	void updateEnv(PaasEnv environment);

	@Delete("delete from os_project_env where env_id=#{envId}")
	void deleteEnv(@Param("envId") String envId);
	
	@Update("update os_project_env_res set res_addition=#{resAddition} where record_id=#{recordId}")
	void updateEnvResourceAddition(PaasEnvResource resource);
	
	@Select("select * from os_project_env_res where paasos_res_id=#{paasResId}")
	List<PaasEnvResource> getEnvResourcesByPaasOsResId(@Param("paasResId")String paasosResId);

	List<PaasEnv> getenvListByMark(Map<String, Object> param);

	List<Map<String, Object>> getPaasOsInfo(@Param("userId")String userId);

	List<Map<String, Object>> getPaasOsDeployInfo(@Param("userId")String userId);
	
}
