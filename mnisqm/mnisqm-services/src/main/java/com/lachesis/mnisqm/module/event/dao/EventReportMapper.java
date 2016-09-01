package com.lachesis.mnisqm.module.event.dao;

import java.util.List;

import com.lachesis.mnisqm.module.event.domain.EventReport;
import com.lachesis.mnisqm.module.user.domain.ComDept;

public interface EventReportMapper {
    int deleteByPrimaryKey(EventReport report);

    int insert(EventReport record);

    EventReport selectByPrimaryKey(Long seqId);

    /**
     * 获取不良事件列表
     * @return
     */
    public List<EventReport> selectEventList(EventReport report);

    int updateByPrimaryKey(EventReport record);
    
    /**
     * 查询不良事件对应科室的护士长
     * @param userCode
     * @return
     */
	ComDept selectDeptHeader(EventReport r);
}