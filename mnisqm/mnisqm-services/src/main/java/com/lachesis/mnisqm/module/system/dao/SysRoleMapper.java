package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysRole;
import java.util.List;

public interface SysRoleMapper {

    int insert(SysRole record);

    int updateByPrimaryKey(SysRole record);
    
    /**
     * 获取用户下的角色信息
     * @param userCode
     * @return
     */
    public List<SysRole> getUserSysRoles(String userCode);
    
    /**
     * 获取所有有效的角色
     * @param userCode
     * @return
     */
    public List<SysRole> getAllRoles();
    
    /**
     * 通过角色编号获取角色信息
     * @return
     */
    public SysRole selectRoleByCode(SysRole role);
    
    /**
     * 删除角色
     */
    public void updateForDelete(SysRole role);
}