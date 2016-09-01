package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysTaskLog;
import java.util.List;

public interface SysTaskLogMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SysTaskLog record);

    SysTaskLog selectByPrimaryKey(Long seqId);

    List<SysTaskLog> selectAll();

    int updateByPrimaryKey(SysTaskLog record);
}