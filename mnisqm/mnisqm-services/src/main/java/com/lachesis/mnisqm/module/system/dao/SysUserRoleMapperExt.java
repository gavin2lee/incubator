package com.lachesis.mnisqm.module.system.dao;

import java.util.Map;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.system.domain.SysUserRole;

public interface SysUserRoleMapperExt extends ISearchableDAO {
	/**
	 * 通过用户查询角色
	 * @param parm
	 * @return
	 */
	public SysUserRole selectUserRoleByUserCode(Map<String,Object> parm);
	
	/**
	 * 查询未使用的用户角色信息
	 * @param parm
	 * @return
	 */
	public SysUserRole selectNoUseUserRole(Map<String,Object> parm);
	
	/**
	 * 通过角色编号查询角色
	 * @param parm
	 * @return
	 */
	public SysUserRole selectUserRoleByRoleCode(Map<String,Object> parm);
	
	/**
	 * 更新用户角色的使用
	 * @param userRole
	 */
	public void updateSysUserRole(SysUserRole userRole);
}