package com.lachesis.mnis.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
/**
 * 
 * @author xin.chen
 *
 */
public interface WardPatrolService {
	/**
	 * 获取病房巡视计划即病房巡视清单
	 * @param workUnitCode 工作单元编号(deptCode 科室ID号)
	 * @param nurseCode 巡视护士Id
	 * @param strDate 查询日期
	 * @return 病房巡视清单结果
	 */
	List<WardPatrolInfo> getWardPatrolPlan(String deptCode,String nurseCode,String tendLevel, String strDate);
	/**
	 * 保存对某病人的病房巡视信息
	 * @param nurseCode 护士ID
	 * @param barcode 病人条码
	 * @return 病房巡视结构体
	 */
	WardPatrolInfo saveWardPatrolInfo(String nurseCode,String deptCode,String patientId);
	
	/**
	 * 根据患者信息保存期对应的巡视
	 * @param inpatientInfo
	 * @param nurseCode
	 * @param deptCode
	 * @return
	 */
	WardPatrolInfo saveWardPatrolInfoForPatient(Patient inpatientInfo, String nurseCode, String deptCode);
	
	/**
	 * 获取对某病人某天的病房巡视记录
	 * @param patientId
	 * @param day
	 * @return
	 */
	List<WardPatrolInfo> getWardPatrolLogByPatId(String patientId, String day);
	
	/**
	 * 通过病人列表查询某天的病房巡视记录
	 * @param patients
	 * @param day
	 * @return
	 */
	List<WardPatrolInfo> getWardPatrolLogByPatients(List<String> patients, String day);
	
	List<WardPatrolInfo> getPublishWardPatrols(String deptCode,Date startTime,Date endTime);

}
