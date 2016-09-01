package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.bloodSugarMonitor.entity.PatBloodSugarMonitor;

public interface BloodSugarMonitorMapper {
	/**
	 * 查询血糖监测信息
	 * @param press
	 * @return
	 */
	public List<PatBloodSugarMonitor> selectBloodSugarList(PatBloodSugarMonitor press);
	
	/**
	 * 查询血糖监测信息
	 * @param press
	 * @return
	 */
	public PatBloodSugarMonitor selectBloodSugar(PatBloodSugarMonitor press);
	
	/**
	 * 保存血糖监测信息
	 * @param press
	 * @return
	 */
	public void insertBloodSugarMonitor(PatBloodSugarMonitor press);
	
	/**
	 * 修改血糖监测信息
	 * @param press
	 * @return
	 */
	public void updateBloodSugarById(PatBloodSugarMonitor press);
}
