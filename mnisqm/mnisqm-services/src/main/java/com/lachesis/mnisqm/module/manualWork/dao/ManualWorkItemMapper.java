package com.lachesis.mnisqm.module.manualWork.dao;

import com.lachesis.mnisqm.module.manualWork.domain.ManualWorkItem;
import java.util.List;

public interface ManualWorkItemMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(ManualWorkItem record);

    ManualWorkItem selectByPrimaryKey(Long seqId);

    List<ManualWorkItem> selectAll();

    int updateByPrimaryKey(ManualWorkItem record);
    
    int updateManualWorkItemForDelete(ManualWorkItem record);
    
    List<ManualWorkItem> queryByDeptCode(String deptCode);
    
}