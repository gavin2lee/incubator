package com.harmazing.openbridge.paasos.project.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.project.model.PaasProject;

public interface PaasProjectMapper extends IBaseMapper{

	public int pageCount(Map<String, Object> params);

	public Collection<? extends Map<String, Object>> page(Map<String, Object> params);

	@Update(" update os_project set project_type=#{projectType},business_id=#{businessId},project_name=#{projectName},project_code=#{projectCode},description=#{description},tenant_id=#{tenantId} where project_id=#{projectId}")
	public void update(PaasProject project);

	@Insert("insert into os_project (project_id,project_type,business_id,create_user,create_date,project_name,project_code,description,tenant_id) "
			+ "values (#{projectId},#{projectType},#{businessId},#{createUser},#{createDate},#{projectName},#{projectCode},#{description},#{tenantId})")
	public void create(PaasProject project);

	@Select(" SELECT `project_id` projectId,`project_type` projectType,`business_id` businessId,`create_user` createUser,u.user_name createUserName,p.`create_date` createDate,`project_name` projectName,p.`description` description,`project_code` projectCode,p.`tenant_id` tenantId,st.`tenant_name` tenantName  from os_project p , sys_user u , sys_tenant st "
			+"where p.`project_id` = #{param} and p.create_user=u.user_id and p.tenant_id=st.tenant_id")
	public PaasProject getPaasProjectInfoById(String projectId);
	
	@Select(" SELECT `project_id` projectId,`project_type` projectType,`business_id` businessId,`create_user` createUser,`create_date` createDate,`project_name` projectName,`description` description,`project_code` projectCode,`tenant_id` tenantId  "
			+" FROM `os_project` where business_id = #{businessId} and project_type=#{projectType} ")
	public PaasProject getPaasProjectByBusinessIdAndType(@Param("businessId") String businessId,@Param("projectType")  String projectType);

	public int getCountCheckBusinessId(Map<String,Object> param);

	
	@Select("select `project_id` projectId,`project_type` projectType,`business_id` businessId,p.`create_user` createUser,u.user_name createUserName,p.`create_date` createDate,`project_name` projectName,`description` description,`project_code` projectCode  from os_project p left join sys_user u on "
			+ "p.create_user=u.user_id where business_id=#{businessId}")
	public List<PaasProject> getProjectByBusId(String businessId);
	
 

	@Delete(" delete from os_project where project_id=#{param} ")
	public void delete(String param);

	public int getCountapi(Map<String, Object> params);
	public int getCountapp(Map<String, Object> params);
	public int getCountstore(Map<String, Object> params);
	public int getCount(Map<String, Object> params);
	
	/**
	* @Title: getPaasProjectList 
	* @author weiyujia
	* @Description: 查询已存在的项目列表
	* @return List<Map<String,Object>>    返回类型 
	* @throws 
	* @date 2016年5月30日 下午5:32:32
	 */
	public List<Map<String, Object>> getPaasProjectList();

	@Select(" SELECT `project_id` projectId,`project_type` projectType,`business_id` businessId,`create_user` createUser,`create_date` createDate,`project_name` projectName,`description` description,`project_code` projectCode  "
			+" FROM `os_project` where `project_code` = #{param} ")
	public PaasProject getPaasProjectInfoByProjectCode(String projectCode);

	
}
