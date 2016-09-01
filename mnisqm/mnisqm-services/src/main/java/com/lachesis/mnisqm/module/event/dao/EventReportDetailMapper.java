package com.lachesis.mnisqm.module.event.dao;

import com.lachesis.mnisqm.module.event.domain.EventReportDetail;
import java.util.List;

public interface EventReportDetailMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(EventReportDetail record);

    EventReportDetail selectByPrimaryKey(Long seqId);

    /**
     * 查询报告下所有的调查信息
     * @param detil
     * @return
     */
    public List<EventReportDetail> selectReportDetailByCode(EventReportDetail detil);

    int updateByPrimaryKey(EventReportDetail record);
}