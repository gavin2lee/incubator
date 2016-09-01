package com.lachesis.mnisqm.module.event.dao;

import com.lachesis.mnisqm.module.event.domain.EventAssessment;
import java.util.List;

public interface EventAssessmentMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(EventAssessment record);

    EventAssessment selectByPrimaryKey(Long seqId);
    
    /**
     * 根据不良事件措施查询评估
     * @param record
     * @return
     */
    List<EventAssessment> selectEventAssessmentList(EventAssessment record);

    int updateByPrimaryKey(EventAssessment record);
}