package com.lachesis.mnisqm.module.schedule.dao;

import com.lachesis.mnisqm.module.schedule.domain.ScheduleChangeClass;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ScheduleChangeClassMapper {
    int insert(ScheduleChangeClass record);

    List<ScheduleChangeClass> selectAll(@Param(value="deptCode")String deptCode);
    
    /**
     * 逻辑删除
     * @param seqId
     */
    public void deleteByKey(Long seqId);
    
    /**
     * 逻辑修改
     * @param record
     * @return
     */
    int update(ScheduleChangeClass record);
    
}