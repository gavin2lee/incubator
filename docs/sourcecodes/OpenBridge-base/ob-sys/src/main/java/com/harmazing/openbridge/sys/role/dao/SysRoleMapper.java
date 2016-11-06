package com.harmazing.openbridge.sys.role.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.sys.role.model.SysRole;

public interface SysRoleMapper extends IBaseMapper {

	@Select("select * from sys_role where role_system = #{systemKey}")
	List<SysRole> findRoleBySystemKey(@Param("systemKey") String systemKey);

	int save(SysRole sr);

	int update(SysRole sr);

	int delete(String roleId);

	int deleteFuncRelation(String roleId);

	int addFuncRelation(@Param("id") String id, @Param("roleId") String roleId,
			@Param("funcId") String funcId);

	int saveFuncRelations(List<Map<String, Object>> parameter);

	int deleteUserRelation(String roleId);

	int saveUserRelation(List<Map<String, Object>> parameter);

	SysRole findById(String roleId);

	List<Map<String, Object>> findUserByRoleId(String roleId);

	/**
	 * 根据角色去查找
	 * 
	 * @param role
	 * @return
	 */
	List<SysRole> findByEntity(SysRole role);

	/**
	 * 根据用户id获取拥有角色
	 * 
	 * @param userId
	 * @return
	 */
	List<SysRole> findRoleByUserId(String userId);

	/**
	 * 根据用户id删除用户角色关联信息
	 * 
	 * @param userId
	 */
	void deleteUserRelationByUserId(String userId);

	/**
	 * 查询系统所有角色id以及角色关联的功能id
	 * 
	 * @return
	 */
	List<Map<String, Object>> getAllRoleFunctions();

}
