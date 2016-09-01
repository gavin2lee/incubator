package com.lachesis.mnisqm.module.event.dao;

import com.lachesis.mnisqm.module.event.domain.EventType;
import java.util.List;

public interface EventTypeMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(EventType record);

    EventType selectByPrimaryKey(Long seqId);

    List<EventType> selectAll();

    int updateByPrimaryKey(EventType record);
}