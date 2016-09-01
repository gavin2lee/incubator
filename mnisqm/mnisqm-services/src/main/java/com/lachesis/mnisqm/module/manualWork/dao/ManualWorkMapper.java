package com.lachesis.mnisqm.module.manualWork.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.manualWork.domain.ManualWork;

public interface ManualWorkMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(ManualWork record);

    ManualWork selectByPrimaryKey(Long seqId);

    List<ManualWork> selectAll();

    int updateByPrimaryKey(ManualWork record);
    
    int updateManualWorkForDelete(ManualWork record);
    
    public List<ManualWork> queryByDeptCodeAndDate(Map<String,Object> map);
}