package com.lachesis.mnis.core.infusionmonitor.repository;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;


public interface InfusionMonitorRepository {
	/**
	 * 获取指定病人指定时间段的输液巡视记录
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @param isFinishType:是否需要输液完状态null:表示全部，非null表示去除输液完数据
	 * @return
	 */
	List<InfusionMonitorInfo> selectInfusionMonitor(
			String patientId, Date startDate, Date endDate,String isFinishType,String orderTypeCode);

	int saveInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo);
	
	int updateInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo);

	InfusionMonitorInfo selectInfusionMonitorForOrderExec(String orderExecId);

	List<InfusionMonitorInfo> selectInfusionMonitorList(List<String> patientList,Date startDate, Date endDate) ;
	
	/**
	 * 获取巡视记录信息
	 * @param patIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<InfusionMonitorRecord> selectInfusionMonitorRecordList(List<String> patientList,List<String> orderExecIds, Date startDate, Date endDate);
	
	int updateInfusionMonitorItem(InfusionMonitorRecord record);

	int addInfusionMonitorItem(InfusionMonitorRecord infusion);
	
	InfusionMonitorRecord getInfusionMonitorRecordById(String id);
	/**
	 * 根据orderExecId获取暂停巡视数量
	 * @param orderExecId
	 * @return
	 */
	int getMonitorCountByOrderExecId(String orderExecId,String isPause);

	
}
