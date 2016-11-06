package com.harmazing.openbridge.paasos.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenant;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenantPreset;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenantQuota;
import com.harmazing.openbridge.sys.user.model.SysUser;

public interface PaaSTenantMapper extends IBaseMapper{
	PaaSTenant getById(String id);
	
	List<PaaSTenant> list();
	
	@Select("select tenant_id,tenant_name from sys_tenant")
	List<PaaSTenant> listBrief();
	
	List<PaaSTenant> getPageData( @Param("start") int start, @Param("size") int size);
	
	int getPageCount();
	
	@Delete("delete from sys_tenant where tenant_id=#{id}")
	void delete(String id);
	
	@Insert("insert into sys_tenant(tenant_id,tenant_name,create_time,admin_id,description)"
			+ " values(#{tenantId},#{tenantName},#{createTime},#{admin.userId},#{description})")
	void addTenant(PaaSTenant tenant);
	
	void batchAddTenantQuota(@Param("tenant")PaaSTenant tenant);
	
	@Update("update sys_tenant set tenant_name=#{tenantName},description=#{description},admin_id=#{admin.userId} where tenant_id=#{tenantId}")
	void update(PaaSTenant tenant);
	@Insert("insert into sys_tenant_relation(rel_id,tenant_id,user_id) values(#{id},#{tenantId},#{userId})")
	void addMember(Map<String, Object> member);
	
	@Delete("delete from sys_tenant_relation where tenant_id=#{id}")
	void deleteMember(String id);
	
	List<PaaSTenant> getTenantByUserId(String userId);

	int getTenSysTenantCountByName(@Param("tenantName")String tenantName, @Param("tenantId")String tenantId);
	
	
	int getTenantImageCount(@Param("users")List<SysUser> users);

	List<Map<String, Object>> findTenantItemList(List<Map<String,Object>> paramList);

	List<PaaSTenantQuota> getTenantQuotaListById(@Param("id")String id);

	void deleteTenantQuota(@Param("id")String tenantId);

	List<Map<String, String>> getTenantQuotaInfo(@Param("paramMap")Map<String, String> paramMap);

	void deleteTenantPreset(@Param("id")String tenantId);

	void batchAddTenantPreset(@Param("tenant")PaaSTenant tenant);

	List<PaaSTenantPreset> getTenantPresetListById(String id);

	List<Map<String, String>> getDefaultQuotaInfo(@Param("paramMap")Map<String, String> paramMap);
}
