package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysDateMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SysDate record);

    SysDate selectByPrimaryKey(Long seqId);

    List<SysDate> selectAll();

    int updateByPrimaryKey(SysDate record);
    
    List<SysDate> getByDateUseLike(@Param(value="date") String date);
}