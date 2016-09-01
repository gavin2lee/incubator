package com.lachesis.mnisqm.module.schedule.dao;

import java.util.List;

import com.lachesis.mnisqm.module.schedule.domain.ScheduleRecord;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRecordDetail;

public interface ScheduleRecordDetailMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(ScheduleRecordDetail record);

    public ScheduleRecordDetail selectByPrimaryKey(Long seqId);

    /**
     * 获取科室排班主记录
     * @param recordCode
     * @return
     */
    public List<ScheduleRecordDetail> selectByRecordCode(String recordCode);

    public int updateByPrimaryKey(ScheduleRecordDetail record);
    
    /**
     * 根据周次的记录，更新明细
     * @param record
     * @return
     */
    public int updateByRecordCode(ScheduleRecord record);
}