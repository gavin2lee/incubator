package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysTask;

import java.util.List;
import java.util.Map;

public interface SysTaskMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SysTask record);

    SysTask selectByPrimaryKey(Long seqId);

    List<SysTask> selectAll();

    int updateByPrimaryKey(SysTask record);
    
    /**
     * 根据taskCode找对应的任务
     * @param param
     * @return
     */
    SysTask selectByTaskCode(Map<String, Object> param);
    
    List<SysTask> selectByTaskStatus(Map<String, Object> param);
}