package com.lachesis.mnis.core.identity;

import java.util.List;

import com.lachesis.mnis.core.identity.entity.RolePermission;
import com.lachesis.mnis.core.identity.entity.SysGroup;
import com.lachesis.mnis.core.identity.entity.SysModule;
import com.lachesis.mnis.core.identity.entity.SysOperate;
import com.lachesis.mnis.core.identity.entity.SysRole;

/***
 * 
 * 权限控制: 暂时未起到作用
 *
 * @author yuliang.xu
 * @date 2015年6月9日 上午10:33:51 
 *
 */
public interface RbacService {
	/**
	 * 为用户添加组
	 * @param userId 用户Id
	 * @param groupId 组Id数组
	 * @return
	 */
	int addGroupToUser(int userId,int[] groupId);

	/**
	 * 删除用户组
	 * @param userId 用户Id
	 * @param groupId 组Id数组
	 * @return
	 */
	int deleteUserGroup(int userId,int[] groupId);

	/**
	 * 获取用户所在的组
	 * @param userId 用户Id
	 * @return
	 */
	List<SysGroup> getUserGroups(int userId);

	/**
	 * 获取所有组
	 * @return
	 */
	List<SysGroup> getAllGroup();

	/**
	 * 为角色添加权限
	 * @param roleId
	 * @param moduleId
	 * @param operateCode
	 * @return
	 */
	int addPermissionToRole(int roleId,int moduleId,String[] operateCode,int validTime);

	/**
	 * 删除角色权限
	 * @param roleId
	 * @param moduleId
	 * @param operateCode
	 * @return
	 */
	int deleteRolePermission(int roleId,int[] moduleId,String[] operateCode);

	/**
	 * 获取角色所拥有的权限
	 * @param roleId 角色Id
	 * @return
	 */
	 RolePermission getRolePermissions(int roleId);

	/**
	 * 为用户添加角色
	 * @param userId 用户Id
	 * @param role 角色Id数组
	 * @return
	 */
	int addRoleToUser(int userId,int[] roleId);

	/**
	 * 删除用户角色
	 * @param userId 用户Id
	 * @param roleId 角色Id数组
	 * @return
	 */
	int deleteUserRole(int userId,int[] roleId);

	/**
	 * 获取用户所拥有的角色
	 * @param userId 用户Id
	 * @return
	 */
	List<SysRole> getUserRoles(int userId);

	/**
	 * 获取所有角色
	 * @return
	 */
	List<SysRole> getAllRole();

	/**
	 * 获取所有操作
	 * @return
	 */
	List<SysOperate> getAllOperate();

	/**
	 * 获取所有模块
	 * @return
	 */
	List<SysModule> getAllModule();
	/**
	 * 删除角色
	 * @param roleId
	 * @return
	 */
    int deleteRole(Integer roleId);

    /**
     * 添加角色
     * @param record
     * @return
     */
    int addRole(SysRole record);
    
    /**
     * 更新角色
     * @param record
     * @return
     */
    int updateRole(SysRole record);
    /**
     * 删除组
     * @param groupId
     * @return
     */
    int deleteGroup(Integer groupId);
    /**
     * 添加组
     * @param record
     * @return
     */
    int addGroup(SysGroup record);
    /**
     * 更新组
     * @param record
     * @return
     */
    int updateGroup(SysGroup record);
    /**
     * 赋予组角色
     * @param groupId
     * @param roleId
     * @return
     */
    int insertRoleGroup(int groupId,int[] roleId);
    /**
     * 删除组角色
     * @param groupId
     * @param roleId
     * @return
     */
    int deleteRoleGroup(int groupId,int[] roleId);
    /**
     * 获取组角色
     * @param groupId
     * @return
     */
    List<SysRole> getRoleGroups(int groupId);
}
