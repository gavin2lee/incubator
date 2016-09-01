package com.lachesis.mnis.core.nursing;

import java.util.List;

import com.lachesis.mnis.core.patient.entity.WorkUnitStat;

public interface NurseShiftService {
//	PatientEventInfo getPatientEventInfoByPatId(String patId, String day) throws ParseException;
	/**
	 * 根据部门code或护士id获取交班本列表信息
	 * @param deptCode
	 * @param nurseId
	 * @return
	 */
	List<NurseShiftEntity> getNurseShiftList(String deptCode, String nurseId,String patId,String rangeType);
	
	/**
	 * 根据输入的时间获取各患者病情变化及处理记录列表
	 * @param deptCode
	 * @param nurseCode
	 * @param startDate
	 * @param endDate
	 * @return 各患者病情变化及处理记录列表
	 */
	List<NurseShiftEntity> getNurseShiftListByTime(String deptCode, String nurseId,String patId,String startDateStr, String endDateStr,String rangeType);
	
	/**
	 * 根据病人id和时段获取交班本记录信息
	 * @param patId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<NurseShiftRecordEntity> getNurseShiftRecordEntitiesByPatId(String patId,String startDate,String endDate);
	
	/**
	 * 保存在护士交班中记录信息
	 * @param shiftBookRecordEntity
	 * @return
	 */
	int saveShiftRecord(NurseShiftRecordEntity nurseShiftRecordEntity);
	
	/**
	 * 根据交班记录id修改交班内容
	 * @param shiftRecordId
	 * @param recordData
	 * @return
	 */
	int updateShiftRecord(NurseShiftRecordEntity nurseShiftRecordEntity);
	
	/**
	 * 根据记录id删除该记录
	 * @param shiftRecordId
	 * @return
	 */
	int deleteShiftRecord(String shiftRecordId);	
	
	
	/**
	 * 保存护士交接班信息
	 * @param nurseShiftEntity
	 * @return
	 */
	int saveNurseShift(NurseShiftEntity nurseShiftEntity);
	
	/**
	 * 修改护士交接班信息
	 * @param nurseShiftEntity
	 * @return
	 */
	int updateNurseShift(NurseShiftEntity nurseShiftEntity,String shiftType);
	
	/**
	 * 删除护士交接班信息和该病人的记录信息
	 * @param id
	 * @return
	 */
	int deleteNurseShiftById(String nurseShiftId);	
	
	
	/**
	 * 从护理记录中拷贝事件记录到交班本
	 * @param nurseRecord
	 * @param deptCode
	 * @return
	 */
	int copyEventInfoFromNurseRecord(NurseRecord nurseRecord, String deptCode);
	
}
