package com.lachesis.mnisqm.module.event.dao;

import java.util.List;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.event.domain.EventType;

public interface EventTypeMapperExt extends ISearchableDAO {
	
	/**
	 * 不良事件列表查询
	 * @return
	 */
	public List<EventType> selectEventTypeList();
}