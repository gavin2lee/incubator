package com.lachesis.mnisqm.module.system.service;

import java.util.List;

import com.lachesis.mnisqm.module.system.domain.ClassFieldMap;
import com.lachesis.mnisqm.module.system.domain.SysConfig;
import com.lachesis.mnisqm.module.system.domain.SysDate;
import com.lachesis.mnisqm.module.system.domain.SysRole;
import com.lachesis.mnisqm.module.system.domain.SysRoleFun;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.system.domain.SysUserRole;


public interface ISysService {
	
	/**
	 * 登录验证
	 * @param loginName
	 * @param password
	 */
	public SysUser checkLogin(String loginName,String password);

	/**
	 * 通过角色获取功能
	 * @param roleCode
	 * @return
	 */
	public List<Object> getFunctionByRole(String roleCode);
	
	/**
	 * 角色未有菜单权限
	 * @param roleCode
	 * @return
	 */
	public List<Object> getNotHaveRole(String roleCode);
	
	/**
	 * 保存用户角色信息
	 * @param userRole
	 */
	public void saveSysUserRole(SysUserRole userRole);
	
	/**
	 * 通过用户编号查询用户使用的角色
	 * @param userCode
	 * @return
	 */
	public SysUserRole getUserRoleByUserCode(Long userId);
	
	/**
	 * 通过角色编号查询角色信息
	 * @param userCode
	 * @return
	 */
	public SysUserRole getUserRoleByRoleCode(String roleCode);
	
	 /**
     * 获取用户下的角色信息
     * @param userCode  用户id
     * @return
     */
	public List<SysRole> getUserSysRoles(String userCode);
	
	/**
	 * 增加角色
	 * @param record
	 * @return
	 */
	public int insertSysRole(SysRole record);
	
	/**
	 * 修改角色信息
	 * @param record
	 * @return
	 */
	public int updateSysRole(SysRole record);
	
	
	/**
	 * 新增用户角色信息
	 * @param sysUserRole
	 * @return
	 */
	public void insertSysUserRole(List<SysUserRole> sysUserRoles);
	
	/**
	 * 修改用户角色信息  包括修改作废状态
	 * @param sysUserRole
	 * @return
	 */
	public int updateSysUserRole(SysUserRole sysUserRole);
	
	/**
	 * 角色新增菜单、修改角色数据
	 * @param record
	 * @return
	 */
	public void saveSysRoleFun(List<SysRoleFun> sysRoleFuns,SysRole sysRole);
	
	/**
	 * 新增系统用户
	 * @param sysUser
	 * @return
	 */
	public int saveSysUser(SysUser sysUser);
	
	/**
	 * 获取用户列表数据
	 * @param deptCode
	 * @param queryKey
	 * @return
	 */
	public List<SysUser> getSysUsers(String deptCode,String queryKey);
	
	/**
	 * 删除系统用户
	 * @param sysUser
	 */
	public void deleteSysUser(SysUser sysUser);
	
	/**
	 * 新增角色
	 * @param role
	 */
	public void saveSysRole(SysRole role);
	
	/**
	 * 删除角色信息
	 * @param role
	 */
	public void deleteSysRole(SysRole role);
	
	/**
	 * 删除系统用户角色
	 * @param sysUser
	 */
	public void deleteUserRole(SysUserRole userRole);
	
	/**
	 * 生成规定年份的系统日期表
	 * @param year 年份
	 */
	public void creatSysDate(String year);
	
	/**
	 * 修改系统日期记录
	 * @param sysDate
	 * @return
	 */
	public int updateSysDate(SysDate sysDate);
	
	/**
	 * 获取规定年份的系统日期表
	 * @param year 年份
	 * @param month 月份  如：01
	 */
	public List<SysDate> querySysDate(String year, String month);
	
	/**
	 * 根据类名查找 该类字段对应的映射
	 * @param className
	 * @return
	 */
	public List<ClassFieldMap> queryClassFieldMapByClass(String className);
	
	/**
	 * 通过configid获取配置信息
	 * @param configId
	 * @return
	 */
	SysConfig getSysConfigByConfigId(String configId);
	
	/**
	 * 更新系统配置
	 * @param record
	 * @return
	 */
	int updateSysConfig(SysConfig record);
}
