package com.lachesis.mnis.core.patrol.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;


public interface WardPatrolRepository {

	List<WardPatrolInfo> getWardPatrolPlan(Map<String, Object> params);

	List<WardPatrolInfo> selectWardPatrolByPatId(List<String> patients,
			Date startDate, Date endDate);

	int saveWardPatrolInfo(WardPatrolInfo wardPatrolInfo);
	
	List<WardPatrolInfo> getPublishWardPatrols(Map<String, Object> params);

}
