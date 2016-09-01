package com.lachesis.mnis.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.lachesis.mnis.core.workload.entity.WorkLoadInfo;
import com.lachesis.mnis.core.workload.entity.WorkLoadType;

/**
 * 获取工作量统计
 * 
 * @author ThinkPad
 *
 */
public interface WorkLoadService {

	/**
	 * 获取各个护士每一天，大类统计工作量
	 * 
	 * @param deptCode
	 * @param nurseCode
	 * @param types
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<WorkLoadInfo> getWorkLoadInfosByNurse(String deptCode,
			String nurseCode, List<String> types, String startDate, String endDate) throws Exception;

	/**
	 * 获取各个护士,小类统计工作量
	 * 
	 * @param deptCode
	 * @param nurseCode
	 * @param types
	 * @param childrenTypes
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<WorkLoadInfo> getWorkLoadInfosByNurseType(String deptCode,
			String nurseCode, List<String> types, List<String> childrenTypes,
			String startDate, String endDate) throws Exception;

	/**
	 * 获取级统计类型
	 * 
	 * @return
	 */
	List<WorkLoadType> getWorkLoadTypes();
	HashMap<String, String> getWorkLoadTypesMap();
}
