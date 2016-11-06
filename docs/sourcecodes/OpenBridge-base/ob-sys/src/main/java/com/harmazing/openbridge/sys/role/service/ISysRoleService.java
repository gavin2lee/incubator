package com.harmazing.openbridge.sys.role.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.sys.role.model.SysRole;

public interface ISysRoleService extends IBaseService {

	public List<SysRole> findRoleBySystemKey(String systemKey);

	/**
	 * 根据用户id删除用户角色关联信息
	 * 
	 * @param userId
	 */
	public void deleteUserRelationByUserId(String userId);

	/**
	 * 功能分页查询
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> funcDialog(Map<String, Object> params);

	/**
	 * 保存或更新
	 * 
	 * @param sysRole
	 */
	public void save(SysRole sysRole);

	public void saveRoleFunc(String roleId, String[] funcId);

	/**
	 * 保存或更新角色和角色包含的人员信息
	 * 
	 * @param sysRole
	 */
	public void saveRoleUser(SysRole sysRole, String[] userId);

	public void addRoleUser(SysRole sysRole, String[] userId);

	/**
	 * 删除角色
	 * 
	 * @param roleId
	 */
	public void delete(String roleId);

	/**
	 * 获取角色信息
	 * 
	 * @param roleId
	 * @return
	 */
	public SysRole findById(String roleId);

	/**
	 * 根据角色名查询角色
	 * 
	 * @param roleName
	 * @return
	 */
	public SysRole findByName(String roleName);

	public List<SysRole> findByEntity(SysRole role);

	/**
	 * 根据用户去查找 角色 ，权限 key : role 角色 key : func 功能
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, List> findRoleInfo(String userId);

	/**
	 * 保存用户角色关系
	 * 
	 * @param reuslt
	 */
	public void saveUserRelation(List<Map<String, Object>> reuslt);

}
