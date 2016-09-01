package com.lachesis.mnis.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.inspection.entity.InspectionRecord;


public interface InspectionService {
	/**
	 * 获取检查报告
	 * @param date          报告日期
	 * @param patientId     病人id
	 * @return
	 */
	List<InspectionRecord> getInspectionRecords(String date,String patientId);
	
	/**
	 * 根据条件，获取检查报告
	 * @param startDate     报告开始日期
	 * @param endDate     报告结束日期
	 * @param patientId     病人id
	 * @return
	 */
	List<InspectionRecord> getInspectionRecords(String startDate, String endDate, String patientId);
	
	/**
	 * 查询多个病人某天的检查报告
	 * @param patients
	 * @param execDate
	 * @return
	 */
	List<InspectionRecord> getInspectionRecordByPatients(List<String> patients,
			String execDate);

	/**
	 * 查询多个病人某段时间的某状态的检查报告
	 * @param patients 病人Id列表
	 * @param startDate 开始日期
	 * @param endDate 截止日期
	 * @param status 状态
	 * @return
	 */
	List<InspectionRecord> getInspectionRecordByPatients(String[] patients,
			String startDate,String endDate,String status);

	/**
	 * 变更检查报告优先级
	 * @param id 检查报告Id
	 * @param priFlag 优先标志。1表示优先，0表示不优先
	 * @return
	 */
	int updateInspectionPriFlag(String id, Integer priFlag);	
	
	/**
	 * 获取推送检查
	 * @param paramMap
	 * @return
	 */
	List<InspectionRecord> getPublishInspections(String deptCode,Date startTime,Date endTime);
}
