package com.lachesis.mnis.core.mybatis.mapper;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;

public interface InfusionMonitorMapper {

	/**
	 * 保存输液巡视信息, 成功返回插入记录的条数
	 * 
	 * @param infusionMonitorInfo
	 * @return 插入记录的条数
	 */
	int saveInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo);

	/**
	 * 获取指定病人指定时间段的输液巡视记录
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @param isFinishType:是否需要输液完状态null:表示全部，非null表示去除输液完数据
	 * @return
	 */
	List<InfusionMonitorInfo> selectInfusionMonitor(String patientId, Date startDate, Date endDate,String isFinishType,String orderTypeCode);
	
	/**
	 * 获取巡视记录信息
	 * @param patIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<InfusionMonitorRecord> selectInfusionMonitorRecordList(List<String> patientList,List<String> orderExecIds, Date startDate, Date endDate);

	/**
	 * 获取指定医嘱执行对象的输液巡视记录
	 * 
	 * @param orderExecId
	 * @return
	 */
	InfusionMonitorInfo selectInfusionMonitorForOrderExec(String orderExecId);

	/**
	 * 获取正在执行的输液记录
	 * 
	 * @param workUnitCode
	 *            工作单元编号，为null时只考虑本人所管患者
	 * @param nurseCode
	 *            护士工号，为null时则查询本科室患者
	 * @return
	 */
	List<InfusionMonitorInfo> selectInfusionMonitorList(List<String> patientList, Date startDate, Date endDate);

	/**
	 * 更新巡视记录状态
	 * @param record
	 * @return
	 */
	int updateInfusionMonitorItem(InfusionMonitorRecord record);

	/**
	 * 添加巡视记录
	 * @param currentRecord
	 */
	int addInfusionMonitorItem(InfusionMonitorRecord currentRecord);

	/**
	 * 修改巡视医嘱状态,  N为新的，I为巡视，S为中止状态，F为完成状态
	 * @param info
	 */
	int updateInfusionMonitor(InfusionMonitorInfo info);
	
	/**
	 * 根据infusionMonitorRecord id获取记录
	 * @param id
	 * @return
	 */
	InfusionMonitorRecord getInfusionMonitorRecordById(String id);
	
	/**
	 * 根据infusionMonitorRecord orderId获取记录
	 * @param id
	 * @return
	 */
	List<InfusionMonitorRecord> getInfusionMonitorRecordByOrderId(String orderId);
	
	/**
	 * 根据orderExecId获取巡视数量
	 * @param orderExecId
	 * @return
	 */
	int getMonitorCountByOrderExecId(String orderExecId,String isPause);

}
