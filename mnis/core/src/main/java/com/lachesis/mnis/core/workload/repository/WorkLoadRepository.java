package com.lachesis.mnis.core.workload.repository;

import java.util.HashMap;
import java.util.List;

import com.lachesis.mnis.core.workload.entity.WorkLoadInfo;
import com.lachesis.mnis.core.workload.entity.WorkLoadType;

public interface WorkLoadRepository {
	/**
	 * 获取--当天--大类统计
	 * @param params
	 * @return
	 */
	List<WorkLoadInfo> getTodayWorkLoadInfosByNurse(HashMap<String, Object> params);
	/**
	 * 获取--当天以前--大类统计
	 * @param params
	 * @return
	 */
	List<WorkLoadInfo> getPreTodayWorkLoadInfosByNurse(HashMap<String, Object> params);
	/**
	 * 获取--当天和当天一起--大类统计
	 * @param params
	 * @return
	 */
	List<WorkLoadInfo> getWorkLoadInfosByNurse(HashMap<String, Object> params);
	/**
	 * 获取--当天--小类统计
	 * @param params
	 * @return
	 */
	List<WorkLoadInfo> getTodayWorkLoadInfosByNurseType(HashMap<String, Object> params);
	/**
	 * 获取--当天以前--小类统计
	 * @param params
	 * @return
	 */
	List<WorkLoadInfo> getPreTodayWorkLoadInfosByNurseType(HashMap<String, Object> params);
	/**
	 * 获取--当天和当天以前--小类统计
	 * @param params
	 * @return
	 */
	List<WorkLoadInfo> getWorkLoadInfosByNurseType(HashMap<String, Object> params);
	
	/**
	 * 获取父级统计类型
	 * @return
	 */
	List<WorkLoadType> getTopWorkLoadTypes();
	
	/**
	 * 获取子级统计类型
	 * @param parentType
	 * @return
	 */
	List<WorkLoadType> getChildrenWorkLoadTypes(String parentType);
}
