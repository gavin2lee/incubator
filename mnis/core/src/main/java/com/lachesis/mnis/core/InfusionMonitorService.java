package com.lachesis.mnis.core;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;


public interface InfusionMonitorService {

	/**
	 * 保存输液巡视记录，返回插入记录的条数
	 * @param infusionMonitorInfo
	 * @return 插入记录的条数
	 */
	int saveInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo);
	/**
	 * 更新输液迅速记录
	 * @param infusionMonitorInfo
	 * @return
	 */
	int updateInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo,String existStatus,String deptCode,String patId,String bedCode);
	
	/**
	 * 获取指定病人指定时间段的输液巡视记录
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @param isFinishType:是否需要输液完状态null:表示全部，非null表示去除输液完数据
	 * @return
	 */
	List<InfusionMonitorInfo> getInfusionMonitor(String patientId,Date startDate, Date endDate,String isFinishType,String orderTypeCode);
	
	/**
	 * 获取对应某个医嘱执行的巡视历史
	 * @param orderExecId
	 * @return
	 */
	InfusionMonitorInfo getMonitorLogForOrderExec(String orderExecId);

	/**
	 * 获取科室或关注的输液巡视记录
	 * 如果科室ID不为空，则查询科室的巡视记录，否则查询护士的关注病人记录
	 * @param workUnitCode 科室ID
	 * @param nurseCode 护士ID 
	 * @return
	 */
	List<InfusionMonitorInfo> selectInfusionMonitorList(String workUnitCode,
			String nurseCode, String date);

	/**
	 * 获取该患者所有巡视记录
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<InfusionMonitorRecord> getInfusionMonitorRecordList(String patientId,List<String> orderExecIds,Date startDate,Date endDate);
	/**
	 * 更新巡视记录
	 * @param infusion
	 * @return
	 */
	InfusionMonitorRecord updateInfusionMonitorItem(InfusionMonitorRecord infusion);
	/**
	 * 新增巡视记录
	 * @param infusion
	 * @return
	 */
	InfusionMonitorRecord addInfusionMonitorItem(InfusionMonitorRecord infusion);
	
	/**
	 * 根据orderExecId获取暂停巡视数量
	 * @param orderExecId
	 * @return
	 */
	boolean isPauseMonitorByOrderExecId(String orderExecId);
	/**
	 * 结束指定的医嘱
	 * @param endOrderExecId
	 * @param userName
	 * @param userCode
	 * @param recordDate
	 */
	void endInfusionMonitorByOrderExecId(String endOrderExecId,String userName,String userCode,Date recordDate);
}
