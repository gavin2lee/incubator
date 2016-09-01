package com.lachesis.mnisqm.module.schedule.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.schedule.domain.ScheduleRecord;
import com.lachesis.mnisqm.module.user.domain.ComDeptNurse;

public interface ScheduleRecordMapper {
	/**
	 * 逻辑删除
	 * @param record
	 * @return
	 */
    public int deleteByPrimaryKey(ScheduleRecord record);

    /**
     * 录入排班主表信息
     * @param record
     * @return
     */
    public int insert(ScheduleRecord record);

    /**
     * 数据查询，通过科室编号和周数查询主记录
     * @param record
     * @return
     */
    public ScheduleRecord selectByWeeks(ScheduleRecord record);

    public List<ScheduleRecord> selectAll();

    public int updateByPrimaryKey(ScheduleRecord record);
    
    /**
     * 查询积假
     * @param parm
     * @return
     */
    public List<ComDeptNurse> selectUserLeave(Map<String,Object> parm);
}