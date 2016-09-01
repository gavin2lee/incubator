package com.lachesis.mnisqm.module.schedule.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.schedule.domain.ScheduleCount;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleLeave;


public interface ScheduleMapper {
	/**
	 * 获取统计信息
	 * @param count
	 * @return
	 */
	public List<ScheduleLeave> getScheduleCount(ScheduleLeave leave);
	
	/**
	 * 日期维度，查询统计工作天数
	 * @return
	 */
	public List<ScheduleCount> getWorkDays(Map<String,Object> parm);
	
	/**
	 * 根据班次统计
	 * @param parm
	 * @return
	 */
	public List<ScheduleCount> getCountByClass(Map<String,Object> parm);
}