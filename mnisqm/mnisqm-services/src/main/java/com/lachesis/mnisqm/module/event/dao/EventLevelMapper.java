package com.lachesis.mnisqm.module.event.dao;

import com.lachesis.mnisqm.module.event.domain.EventLevel;
import java.util.List;

public interface EventLevelMapper {
	/**
	 * 逻辑删除
	 * @param record
	 * @return
	 */
	public int logicDelete(EventLevel record);

	/**
	 * 新增数据
	 * @param record
	 * @return
	 */
    public int insert(EventLevel record);

    public EventLevel selectByPrimaryKey(Long seqId);

    /**
     * 查询所有的不良事件分级
     * @return
     */
    public List<EventLevel> selectAll();
 
    public int updateByPrimaryKey(EventLevel record);
}