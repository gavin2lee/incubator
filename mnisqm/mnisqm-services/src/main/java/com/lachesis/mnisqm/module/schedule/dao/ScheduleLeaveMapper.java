package com.lachesis.mnisqm.module.schedule.dao;

import java.util.List;

import com.lachesis.mnisqm.module.schedule.domain.ScheduleLeave;
import com.lachesis.mnisqm.module.user.domain.ComDept;

public interface ScheduleLeaveMapper {
	
	/**
	 * 数据插入
	 * @param record
	 * @return
	 */
    public int insert(ScheduleLeave record);

    /**
     * 更加科室编号查询本科室所有的请假列表
     * @return
     */
    public List<ScheduleLeave> selectLeave(ScheduleLeave leave);
    
    /**
     * 删除请假信息
     * @param seqId
     */
    public void deleteLeaveByKey(Long seqId);
    
    /**
     * 保存数据
     * @param leave
     */
    public void updateByKey(ScheduleLeave leave);
    
    /**
     * 修改审核状态
     * @param leave
     */
    public void updateApproveStatus(ScheduleLeave scheduleLeave);
    
    /**
     * 查询用户所在科室的护士长
     * @param userCode
     * @return
     */
    public ComDept selectDeptHeader(ScheduleLeave leave);
}