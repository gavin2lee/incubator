package com.lachesis.mnis.core.bloodPressMonitor.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.bloodPressMonitor.entity.PatBloodPressMonitor;
import com.lachesis.mnis.core.bloodPressMonitor.repository.BloodPressRepository;
import com.lachesis.mnis.core.mybatis.mapper.BloodPressMonitorMapper;

@Repository("BloodPressRepository")
public class BloodPressRepositoryImpl implements BloodPressRepository{
	
	@Autowired
	private BloodPressMonitorMapper bloodPressMonitorMapper;

	@Override
	public List<PatBloodPressMonitor> selectBloodPressList(
			PatBloodPressMonitor press) {
		return bloodPressMonitorMapper.selectBloodPressList(press);
	}
	
	@Override
	public PatBloodPressMonitor selectBloodPress(
			PatBloodPressMonitor press) {
		return bloodPressMonitorMapper.selectBloodPress(press);
	}

	@Override
	public void insertBloodPressMonitor(PatBloodPressMonitor press) {
		bloodPressMonitorMapper.insertBloodPressMonitor(press);
	}

	@Override
	public void updateBloodPressById(PatBloodPressMonitor press) {
		bloodPressMonitorMapper.updateBloodPressById(press);
	}
}
