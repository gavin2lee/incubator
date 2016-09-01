package com.lachesis.mnisqm.module.event.dao;

import com.lachesis.mnisqm.module.event.domain.EventMeasures;
import java.util.List;

public interface EventMeasuresMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(EventMeasures record);

    EventMeasures selectByPrimaryKey(Long seqId);
	/**
	 * 查询措施列表
	 * @param measures
	 * @return
	 */
    public List<EventMeasures> selectEventMeasuresList(EventMeasures measures);

    int updateByPrimaryKey(EventMeasures record);
}