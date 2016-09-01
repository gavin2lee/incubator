package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysRoleFun;
import java.util.List;

public interface SysRoleFunMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SysRoleFun record);

    SysRoleFun selectByPrimaryKey(Long seqId);

    List<SysRoleFun> selectAll();

    int updateByPrimaryKey(SysRoleFun record);
    
    public int updateSysRoleFun(SysRoleFun record); 
    
    int deleteByRoleFun(String roleCode);
}