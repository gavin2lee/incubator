package com.lachesis.mnis.core.patrol.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.WardPatrolMapper;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.patrol.repository.WardPatrolRepository;

@Repository("wardPatrolRepository")
public class WardPatrolRepositoryImpl implements WardPatrolRepository {

	@Autowired
	private WardPatrolMapper wardPatrolMapper;
	
	@Override
	public List<WardPatrolInfo> getWardPatrolPlan(Map<String, Object> params) {
		return wardPatrolMapper.getWardPatrolPlan(params);
	}

	@Override
	public List<WardPatrolInfo> selectWardPatrolByPatId(List<String> patients,
			Date startDate, Date endDate) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("patientIds", patients);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return wardPatrolMapper.selectWardPatrolByPatId(params);
	}

	@Override
	public int saveWardPatrolInfo(WardPatrolInfo wardPatrolInfo) {
		return wardPatrolMapper.saveWardPatrolInfo(wardPatrolInfo);
	}

	@Override
	public List<WardPatrolInfo> getPublishWardPatrols(Map<String, Object> params) {
		return wardPatrolMapper.getPublishWardPatrols(params);
	}

}
