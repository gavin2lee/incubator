package com.lachesis.mnis.core.nursing;

import java.util.List;
import java.util.Map;


public interface NurseShiftRepository {

	/**
	 * patId，deptCode,关注病人patients,startDate,endDate获取交班本列表信息
	 * 
	 * @param paramsMap
	 * @return
	 */
	List<NurseShiftEntity> getNurseShifts(Map<String, Object> paramsMap);

	/**
	 * 根据主键id获取交班本信息
	 * @param id
	 * @return
	 */
	NurseShiftEntity getNurseShiftEntityById(String nurseShiftId);

	/**
	 * 插入交班本信息
	 * 
	 * @param nurseShiftEntity
	 * @return
	 */
	int insertNurseShift(NurseShiftEntity nurseShiftEntity);

	/**
	 * 修改交班本信息
	 * 
	 * @param nurseShiftEntity
	 * @return
	 */
	int updateNurseShift(NurseShiftEntity nurseShiftEntity);

	/**
	 * 根据id和时段删除交班本信息
	 * @param id
	 * @return
	 */
	int deleteNurseShiftById(String nurseShiftId);

	/**
	 * 根据id获取交班本记录信息
	 * 
	 * @param shiftRecordId
	 * @return
	 */
	NurseShiftRecordEntity getShiftRecordById(String shiftRecordId);

	/**
	 * 根据病人Id获取交班本记录列表信息
	 * 
	 * @param patId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<NurseShiftRecordEntity> getShiftRecordsByNurseShiftId(String nurseShiftId);

	/**
	 * 插入交班本记录信息
	 * 
	 * @param nurseShiftRecordEntity
	 * @return
	 */
	int insertShiftRecord(NurseShiftRecordEntity nurseShiftRecordEntity);

	/**
	 * 修改交班本记录信息
	 * 
	 * @param nurseShiftRecordEntity
	 * @return
	 */
	int updateShiftRecord(NurseShiftRecordEntity nurseShiftRecordEntity);

	/**
	 * 删除交班本记录信息
	 * 
	 * @param shiftRecordId
	 * @return
	 */
	int deleteShiftRecordById(String shiftRecordId);

}
