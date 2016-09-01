package com.lachesis.mnis.core.bloodSugarMonitor.repository;

import java.util.List;

import com.lachesis.mnis.core.bloodSugarMonitor.entity.PatBloodSugarMonitor;


public interface BloodSugarRepository{
	/**
	 * 查询血糖监控
	 * @param press
	 * @return
	 */
	public List<PatBloodSugarMonitor> selectBloodSugarMonitor(
			PatBloodSugarMonitor press);

	/**
	 * 插入血糖监控信息
	 * @param press
	 */
	public void insertBloodSugarMonitor(PatBloodSugarMonitor press) ;
	
	/**
	 *更新血糖监控信息 
	 * @param press
	 */
	public void updateBloodSugarById(PatBloodSugarMonitor press) ;
	
	/**
	 * 查询血糖监控信息
	 * @param press
	 */
	public PatBloodSugarMonitor selectBloodSugar(PatBloodSugarMonitor press);
}