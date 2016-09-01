package com.lachesis.mnis.core.bloodPressMonitor.repository;

import java.util.List;

import com.lachesis.mnis.core.bloodPressMonitor.entity.PatBloodPressMonitor;


public interface BloodPressRepository{
	/**
	 * 查询血压监测信息
	 * @param press
	 * @return
	 */
	public List<PatBloodPressMonitor> selectBloodPressList(PatBloodPressMonitor press);
	
	/**
	 * 保存血压监测信息
	 * @param press
	 * @return
	 */
	public void insertBloodPressMonitor(PatBloodPressMonitor press);
	
	/**
	 * 修改血压监测信息
	 * @param press
	 * @return
	 */
	public void updateBloodPressById(PatBloodPressMonitor press);
	
	/**
	 * 查询血压监测信息
	 * @param press
	 * @return
	 */
	public PatBloodPressMonitor selectBloodPress(PatBloodPressMonitor press);
}