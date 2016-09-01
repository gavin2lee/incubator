package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysUserRole;

public interface SysUserRoleMapper {

    int insert(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);
    
    /**
     * 逻辑删除用户角色信息
     * @param userRole
     */
    public void updateForDelete(SysUserRole userRole);
}