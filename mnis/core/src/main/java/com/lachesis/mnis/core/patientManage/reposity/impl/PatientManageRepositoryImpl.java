package com.lachesis.mnis.core.patientManage.reposity.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.PatCureLocalInfoMapper;
import com.lachesis.mnis.core.mybatis.mapper.PatLeaveGooutMapper;
import com.lachesis.mnis.core.mybatis.mapper.PatOperationInfoMapper;
import com.lachesis.mnis.core.mybatis.mapper.PatOperationStatusMapper;
import com.lachesis.mnis.core.mybatis.mapper.PatOrderConfigurationMapper;
import com.lachesis.mnis.core.patientManage.entity.PatCureLocalInfo;
import com.lachesis.mnis.core.patientManage.entity.PatLeaveGoout;
import com.lachesis.mnis.core.patientManage.entity.PatNdaManageInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationStatus;
import com.lachesis.mnis.core.patientManage.entity.PatOrderConfiguration;
import com.lachesis.mnis.core.patientManage.reposity.PatientManageRepository;

@Repository("patientManageRepository")
public class PatientManageRepositoryImpl implements PatientManageRepository {
	
	@Autowired
	private PatOperationInfoMapper patOperationInfoMapper;
	
	@Autowired
	private PatOperationStatusMapper patOperationStatusMapper;
	
	@Autowired
	private PatOrderConfigurationMapper patOrderConfigurationMapper;
	
	@Autowired
	private PatCureLocalInfoMapper patCureLocalInfoMapper;
	
	@Autowired
	private PatLeaveGooutMapper patLeaveGooutMapper;

	@Override
	public int savePatOperationInfo(PatOperationInfo patOperationInfo) {
		return patOperationInfoMapper.insert(patOperationInfo);
	}

	@Override
	public int savePatOperationStatus(PatOperationStatus patOperationStatus) {
		return patOperationStatusMapper.insert(patOperationStatus);
	}

	@Override
	public PatOperationInfo getPatOperationInfoByPatId(Map<String, Object> conditionMap) {
		List<PatOperationInfo> list = patOperationInfoMapper.getByPatId(conditionMap);
		return (null == list || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public List<PatOperationInfo> queryAllPatOperationInfo() {
		return patOperationInfoMapper.selectAll();
	}

	@Override
	public List<PatOperationInfo> queryByStatusOrDate(
			Map<String, Object> conditionMap) {
		return patOperationInfoMapper.queryByStatusOrDate(conditionMap);
	}

	@Override
	public int savePatOrderConfiguration(
			PatOrderConfiguration patOrderConfiguration) {
		return patOrderConfigurationMapper.insert(patOrderConfiguration);
	}

	@Override
	public List<PatOrderConfiguration> queryAllPatOrderConfiguration() {
		return patOrderConfigurationMapper.selectAll();
	}

	@Override
	public PatOperationStatus getRecentlyPatOperationByPatId(String patId) {
		return patOperationStatusMapper.getRecentlyRecordByPatId(patId);
	}

	@Override
	public int savePatCureLocalInfo(PatCureLocalInfo patCureLocalInfo) {
		return patCureLocalInfoMapper.insert(patCureLocalInfo);
	}

	@Override
	public int savePatLeaveGoout(PatLeaveGoout patLeaveGoout) {
		return patLeaveGooutMapper.insert(patLeaveGoout);
	}

	@Override
	public List<PatCureLocalInfo> queryAllOutPatientByDateAndStatus(Map<String, String> conditionMap) {
		return patCureLocalInfoMapper.queryAllOutPatientByDateAndStatus(conditionMap);
	}

	@Override
	public PatNdaManageInfo queryNdaManageInfo(Map<String, String> conditionMap) {
		return patCureLocalInfoMapper.queryNdaManageInfo(conditionMap);
	}

}
