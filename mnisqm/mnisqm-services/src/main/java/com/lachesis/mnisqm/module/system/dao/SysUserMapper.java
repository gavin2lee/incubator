package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysUser;
import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SysUser record);

    int updateByPrimaryKey(SysUser record);
    
    /**
     * 查询系统用户
     * @param queryKey
     * @param flag
     * @return
     */
    public List<SysUser> getSysUsers(String queryKey,String deptCode);
    
    /**
     * 
     * 删除用户角色
     * @param user
     */
    public void updateSysUserForDelete(SysUser user);
    
    /**
     * 通过员工编号查询用户
     * @param user
     * @return
     */
    public SysUser selectSysUserByCode(SysUser user);
}