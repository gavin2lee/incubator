package com.lachesis.mnis.core.workload.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.WorkLoadMapper;
import com.lachesis.mnis.core.workload.entity.WorkLoadInfo;
import com.lachesis.mnis.core.workload.entity.WorkLoadType;
import com.lachesis.mnis.core.workload.repository.WorkLoadRepository;

@Repository
public class WorkLoadRepositoryImpl implements WorkLoadRepository {

	@Autowired
	private WorkLoadMapper workLoadMapper;

	@Override
	public List<WorkLoadInfo> getWorkLoadInfosByNurse(
			HashMap<String, Object> params) {
		return workLoadMapper.getWorkLoadInfosByNurse(params);
	}

	@Override
	public List<WorkLoadInfo> getWorkLoadInfosByNurseType(
			HashMap<String, Object> params) {
		return workLoadMapper.getWorkLoadInfosByNurseType(params);
	}

	@Override
	public List<WorkLoadType> getTopWorkLoadTypes() {
		return workLoadMapper.getTopWorkLoadTypes();
	}

	@Override
	public List<WorkLoadType> getChildrenWorkLoadTypes(String parentType) {
		return workLoadMapper.getChildrenWorkLoadTypes(parentType);
	}

	@Override
	public List<WorkLoadInfo> getTodayWorkLoadInfosByNurse(
			HashMap<String, Object> params) {
		return workLoadMapper.getTodayWorkLoadInfosByNurse(params);
	}

	@Override
	public List<WorkLoadInfo> getPreTodayWorkLoadInfosByNurse(
			HashMap<String, Object> params) {
		return workLoadMapper.getPreTodayWorkLoadInfosByNurse(params);
	}

	@Override
	public List<WorkLoadInfo> getTodayWorkLoadInfosByNurseType(
			HashMap<String, Object> params) {
		return workLoadMapper.getTodayWorkLoadInfosByNurseType(params);
	}

	@Override
	public List<WorkLoadInfo> getPreTodayWorkLoadInfosByNurseType(
			HashMap<String, Object> params) {
		return workLoadMapper.getPreTodayWorkLoadInfosByNurseType(params);
	}


}
