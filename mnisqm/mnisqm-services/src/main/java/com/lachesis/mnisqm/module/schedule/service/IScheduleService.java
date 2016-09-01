package com.lachesis.mnisqm.module.schedule.service;

import java.util.List;

import com.lachesis.mnisqm.module.schedule.domain.Schedule;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleChangeClass;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleCount;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleDeptClass;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleLeave;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRule;
import com.lachesis.mnisqm.module.user.domain.ComBedGroup;
import com.lachesis.mnisqm.module.user.domain.ComDeptBed;
import com.lachesis.mnisqm.module.user.domain.ComDeptNurse;


public interface IScheduleService {
	
	/**
	 * 查询请假列表
	 * @return
	 */
	public List<ScheduleLeave> getLeaveList(String deptCode,String apprvUserCode,String userCode);
	
	/**
	 * 保存请假信息
	 * @param leave
	 */
	public void saveLeave(ScheduleLeave leave);
	
	/**
	 * 删除请假信息
	 * @param leave
	 */
	public void deleteLeave(ScheduleLeave leave);
	
	/**
	 * 获取床位分组管理
	 * @param deptCode
	 * @return
	 */
	public List<ComBedGroup> getGroupList(String deptCode);
	
	/**
	 * 保存床位分组
	 * @param group
	 */
	public void saveGroup(ComBedGroup group);
	
	
	/**
	 * 删除床位分组
	 * @param group
	 */
	public void deleteGroup(ComBedGroup group);
	
	/**
	 * 保存班次信息
	 */
	public List<ScheduleDeptClass> getClassList(String deptCode);
	
	/**
	 * 保存班次信息
	 * @param deptClass
	 */
	public void saveClass(ScheduleDeptClass deptClass);
	
	/**
	 * 删除班次信息
	 */
	public void deleteClass(ScheduleDeptClass deptClass);
	
	/**
	 * 保存床位
	 * @param group
	 */
	public void saveBed(ComDeptBed bed);
	
	/**
	 * 获取科室床位列表
	 * @param deptCode
	 * @return
	 */
	public List<ComDeptBed> getBedList(String deptCode);
	
	/**
	 * 查询排班规则列表
	 * @param deptCode
	 * @return
	 */
	public List<ScheduleRule> getRuleList(String deptCode);
	
	/**
	 * 保存排班规则
	 * @param deptCode
	 * @return
	 */
	public void saveRule(ScheduleRule rule);
	
	/**
	 * 删除科室排班规则
	 * @param rule
	 */
	public void deleteRule(ScheduleRule rule);
	
	/**
	 * 查询调班信息
	 * @param deptCode
	 * @return
	 */
	public List<ScheduleChangeClass> getChangeClass(String deptCode,String loginUser);
	
	/**
	 * 保存调班信息
	 * @param change
	 * @return
	 */
	public void saveChangeClass(ScheduleChangeClass change);
	
	/**
	 * 逻辑删除
	 * @param change
	 */
	public void deleteByKey(ScheduleChangeClass change);
	
	/**
	 * 新建排班
	 * @param deptCode
	 * @return
	 */
	public Schedule newSchedule(String deptCode,String week);
	
	/**
	 * 
	 * @param schedules
	 */
	public void saveSchedule(Schedule schedule);
	
	/**
	 * 查询科室排班信息
	 * @param deptCode
	 * @param weeks
	 * @param year
	 * @return
	 */
	public Schedule getSchedule(String deptCode,String week, String year);
	
	/**
	 * 周排班复制
	 * @param deptCode
	 * @param srcWeek
	 * @param outWeek
	 * @return
	 */
	public Schedule copySchedule(String deptCode,String srcWeek,String outWeek, String year);
	
	/**
	 * 获取病区护理人员信息
	 * @param deptCode
	 * @param userCode
	 * @return
	 */
	public List<ComDeptNurse> getUserNurse(String deptCode,String userCode);

	/**
	 * 数据保存
	 * @param nurses
	 */
	public void saveUserNurse(List<ComDeptNurse> nurses);
	
	/**
	 * 查询排班统计信息
	 * @param deptCode
	 * @param mounth
	 * @return
	 */
	public List<ScheduleCount> CountScheduleByLeave(String deptCode,String mounth);
	
	/**
	 * 日期维度统计
	 * @param deptCode
	 * @param mounth
	 * @return
	 */
	public List<ScheduleCount> CountScheduleByDays(String deptCode,String mounth);
	
	/**
	 * 
	 * @param deptCode
	 * @param mounth
	 * @return
	 */
	public List<ScheduleCount> CountScheduleByClass(String deptCode,String mounth);
	
	/**
	 * 改变请假审核状态
	 * @param scheduleLeave
	 */
	public void updateScheduleApproveStatus(ScheduleLeave leave);
}
