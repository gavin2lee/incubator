package com.lachesis.mnis.core.bloodSugarMonitor.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.bloodSugarMonitor.entity.PatBloodSugarMonitor;
import com.lachesis.mnis.core.bloodSugarMonitor.repository.BloodSugarRepository;
import com.lachesis.mnis.core.mybatis.mapper.BloodSugarMonitorMapper;

@Repository("BloodSugarRepository")
public class BloodSugarRepositoryImpl implements BloodSugarRepository{
	
	@Autowired
	private BloodSugarMonitorMapper mapper;

	@Override
	public List<PatBloodSugarMonitor> selectBloodSugarMonitor(
			PatBloodSugarMonitor press) {
		return mapper.selectBloodSugarList(press);
	}

	@Override
	public void insertBloodSugarMonitor(PatBloodSugarMonitor press) {
		mapper.insertBloodSugarMonitor(press);
	}

	@Override
	public void updateBloodSugarById(PatBloodSugarMonitor press) {
		mapper.updateBloodSugarById(press);
	}
	
	@Override
	public PatBloodSugarMonitor selectBloodSugar(PatBloodSugarMonitor press) {
		return mapper.selectBloodSugar(press);
	}
}
