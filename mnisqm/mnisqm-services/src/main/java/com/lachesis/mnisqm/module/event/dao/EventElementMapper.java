package com.lachesis.mnisqm.module.event.dao;

import com.lachesis.mnisqm.module.event.domain.EventElement;
import java.util.List;

public interface EventElementMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(EventElement record);

    EventElement selectByPrimaryKey(Long seqId);

    List<EventElement> selectAll();

    int updateByPrimaryKey(EventElement record);
    
    List<EventElement> selectByEventTypeCode(String eventTypeCode);
}