package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysConfig;
import java.util.List;

public interface SysConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysConfig record);

    SysConfig selectByPrimaryKey(Integer id);

    List<SysConfig> selectAll();

    int updateByPrimaryKey(SysConfig record);
    
    SysConfig getByConfigId(String configId);
}