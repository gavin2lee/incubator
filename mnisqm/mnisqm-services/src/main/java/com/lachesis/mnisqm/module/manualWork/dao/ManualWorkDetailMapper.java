package com.lachesis.mnisqm.module.manualWork.dao;

import com.lachesis.mnisqm.module.manualWork.domain.ManualWorkDetail;
import java.util.List;

public interface ManualWorkDetailMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(ManualWorkDetail record);

    ManualWorkDetail selectByPrimaryKey(Long seqId);

    List<ManualWorkDetail> selectAll();

    int updateByPrimaryKey(ManualWorkDetail record);
    
    int updatemanualWorkDetailForDelete(ManualWorkDetail record);
}