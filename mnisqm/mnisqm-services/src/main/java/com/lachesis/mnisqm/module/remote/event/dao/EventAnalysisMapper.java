package com.lachesis.mnisqm.module.remote.event.dao;

import com.lachesis.mnisqm.module.event.domain.EventAnalysis;

import java.util.List;
import java.util.Map;

public interface EventAnalysisMapper {
    int insert(EventAnalysis record);

    List<EventAnalysis> selectAll();
    
    List<String> queryAllEventName();
    
    EventAnalysis getEventSumByNameAndDate(Map<String, Object> map);
}