package com.lachesis.mnis.core;

import java.util.List;

import com.lachesis.mnis.core.bloodPressMonitor.entity.PatBloodPressMonitor;

public interface BloodPressMonitorService {
	/**
	 * 
	 * @param deptCode : 科室编号
	 * @param patId :患者流水号
	 * @param recordDate:记录时间
	 * @param provisions:是否查询指定点的数据
	 */
	public List<PatBloodPressMonitor> queryBloodPressMonitor(String deptCode, String patId,
			String recordDate, boolean provisions);
	
	/**
	 * 血压监测数据保存
	 * @param datas
	 */
	public void saveBloodPressMonitor(List<PatBloodPressMonitor> datas);
}
