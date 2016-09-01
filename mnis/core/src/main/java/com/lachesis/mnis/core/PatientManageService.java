package com.lachesis.mnis.core;

import java.util.List;

import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientEventDetail;
import com.lachesis.mnis.core.patientManage.entity.PatCureLocalInfo;
import com.lachesis.mnis.core.patientManage.entity.PatLeaveGoout;
import com.lachesis.mnis.core.patientManage.entity.PatNdaManageInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationStatus;
import com.lachesis.mnis.core.patientManage.entity.PatOrderConfiguration;

public interface PatientManageService {

	/**
	 * 保存手术信息
	 * @param patOperationInfo
	 * @return
	 */
	public int savePatOperationInfo(PatOperationInfo patOperationInfo);
	
	/**
	 * 保存手术状态信息
	 * @param patOperationStatus
	 * @return
	 */
	public String savePatOperationStatus(PatOperationStatus patOperationStatus);
	
	/**
	 * 保存顺序配置信息
	 * @param patOrderConfiguration
	 * @return
	 */
	public int savePatOrderConfiguration(PatOrderConfiguration patOrderConfiguration);
	
	/**
	 * 保存本地患者信息
	 * @param patCureLocalInfo
	 * @return
	 */
	public int savePatCureLocalInfo(PatCureLocalInfo patCureLocalInfo); 
	
	/**
	 * 保存请假外出信息
	 * @param patLeaveGoout
	 * @return
	 */
	public int savePatLeaveGoout(PatLeaveGoout patLeaveGoout);
	
	/**
	 * 获取所有顺序配置信息
	 * @return
	 */
	public List<PatOrderConfiguration> queryAllPatOrderConfiguration();
	
	/**
	 * 根据患者id获取手术信息
	 * @param patId
	 * @return
	 */
	public PatOperationInfo getPatOperationInfoByPatId(String patId);
	
	/**
	 * 获取所有患者手术信息
	 * @return
	 */
	public List<PatOperationInfo> queryAllPatOperationInfo();
	
	/**
	 * 根据状态或时间查询手术患者信息
	 * @param status 状态 1 手术申请，2 手术通知，3 科室送出，4 手术室接收，5 麻醉，6 手术中，7 苏醒
	 * @param date 日期，如：2000-01-01
	 * @return
	 */
	public List<PatOperationInfo> queryPatOperationInfoByStatusOrDate(String status, String beginTime,String endTime);
	
	/**
	 * 获取所有外出患者信息
	 * @param dateTime 日期;格式: 2001-01-01
	 * @return
	 */
	public List<PatCureLocalInfo> queryAllOutPatientByDateAndStatus(String beginTime, String endTime, String status);
	
	/**
	 * 保存患者事件
	 * @param record
	 */
	public void savePatientEvent(PatientEvent record);
	
	/**
	 * 查询指定时间内的出入院事件
	 * @param eventCode 事件编号 ，null表示查询出院和住院
	 * @param beginTime 
	 * @param endTime 
	 * @return
	 */
	public List<PatientEventDetail>queryInOutEventByTime(String eventCode, String beginTime, String endTime);
	
	/**
	 * 获取各事件人数
	 * @param beginTime 时间范围起始 如 ：2001-01-01 01:01:59
	 * @param endTime 时间范围终止
	 * @return
	 */
	public PatNdaManageInfo queryNdaManageInfo(String beginTime, String endTime);
}
