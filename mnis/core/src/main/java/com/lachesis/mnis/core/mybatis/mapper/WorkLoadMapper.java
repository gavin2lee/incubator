package com.lachesis.mnis.core.mybatis.mapper;

import java.util.HashMap;
import java.util.List;

import com.lachesis.mnis.core.workload.entity.WorkLoadInfo;
import com.lachesis.mnis.core.workload.entity.WorkLoadType;

/**
 * 工作量统计
 * 大类：医嘱执行，输液巡视，配液等
 * 小类:输液巡视：巡视，停止，暂停，完成，拔针
 * 		配液：备药，审核，配液
 * @author ThinkPad
 *
 */
public interface WorkLoadMapper {
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
