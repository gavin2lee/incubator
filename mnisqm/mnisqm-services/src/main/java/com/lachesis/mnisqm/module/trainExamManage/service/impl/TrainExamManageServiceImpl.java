package com.lachesis.mnisqm.module.trainExamManage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.DateTimeUtils;
import com.lachesis.mnisqm.module.remote.training.dao.OutContinuingEducationMapper;
import com.lachesis.mnisqm.module.remote.training.dao.NurseLevelTrainingMapper;
import com.lachesis.mnisqm.module.remote.training.domain.ContinuingEducation;
import com.lachesis.mnisqm.module.trainExamManage.dao.TemAttendanceManageMapper;
import com.lachesis.mnisqm.module.trainExamManage.dao.TemExamManageMapper;
import com.lachesis.mnisqm.module.trainExamManage.dao.TemPerformanceManageMapper;
import com.lachesis.mnisqm.module.trainExamManage.dao.TemTrainManageMapper;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemAttendanceManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemExamManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemPerformanceManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemTrainManage;
import com.lachesis.mnisqm.module.trainExamManage.service.ITrainExamManageService;
import com.lachesis.mnisqm.module.training.domain.NurseLevelTraining;

@Service
public class TrainExamManageServiceImpl implements ITrainExamManageService {

	@Autowired
	TemTrainManageMapper temTrainManageMapper;
	@Autowired
	TemAttendanceManageMapper temAttendanceManageMapper;
	@Autowired
	TemExamManageMapper temExamManageMapper;
	@Autowired
	TemPerformanceManageMapper temPerformanceManageMapper;
	@Autowired
	NurseLevelTrainingMapper nurseLevelTrainingMapper;
	@Autowired
	OutContinuingEducationMapper outContinuingEducationMapper;
	
	@Override
	public int insertTemTrainManage(TemTrainManage temTrainManage) {
		temTrainManage.setTrainCode(CodeUtils.getSysInvokeId());
		return temTrainManageMapper.insert(temTrainManage);
	}

	@Override
	public int updateTemTrainManage(TemTrainManage temTrainManage) {
		return temTrainManageMapper.updateByPrimaryKey(temTrainManage);
	}

	@Override
	public void deleteTemTrainManage(Long seqId) {
		temTrainManageMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public int insertTemAttendanceManage(TemAttendanceManage temAttendanceManage) {
		return temAttendanceManageMapper.insert(temAttendanceManage);
	}

	@Override
	public int updateTemAttendanceManage(TemAttendanceManage temAttendanceManage) {
		return temAttendanceManageMapper.updateByPrimaryKey(temAttendanceManage);
	}

	@Override
	public void deleteTemAttendanceManage(Long seqId) {
		temAttendanceManageMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public int insertTemExamManage(TemExamManage temExamManage) {
		temExamManage.setExamCode(CodeUtils.getSysInvokeId());
		return temExamManageMapper.insert(temExamManage);
	}

	@Override
	public int updateTemExamManage(TemExamManage temExamManage) {
		return temExamManageMapper.updateByPrimaryKey(temExamManage);
	}

	@Override
	public void deleteTemExamManage(Long seqId) {
		temExamManageMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public int insertTemPerformanceManage(
			TemPerformanceManage temPerformanceManage) {
		return temPerformanceManageMapper.insert(temPerformanceManage);
	}

	@Override
	public int updateTemPerformanceManage(
			TemPerformanceManage temPerformanceManage) {
		return temPerformanceManageMapper.updateByPrimaryKey(temPerformanceManage);
	}

	@Override
	public void deleteTemPerformanceManage(Long seqId) {
		temPerformanceManageMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public List<TemTrainManage> queryTemTrainManageByTDC(String yearAndMonth, String deptCode, String courseName) {
		Map<String, Object> conditionMap = new HashMap<String, Object>(); 
		if(yearAndMonth.length()>7){ //处理输入的参数带天的，如 2016-06-01
			conditionMap.put("beginTime", yearAndMonth + " 00:00:00");
			conditionMap.put("endTime", yearAndMonth + " 23:59:59");
		}else{
			int lastDay = DateTimeUtils.getSumDatOfMonth(Integer.parseInt(yearAndMonth.split("-")[0]), Integer.parseInt(yearAndMonth.split("-")[1]));
			conditionMap.put("beginTime", yearAndMonth + "-01 00:00:00");
			conditionMap.put("endTime", yearAndMonth + "-" + lastDay + " 23:59:59");
		}
		conditionMap.put("deptCode", deptCode);
		conditionMap.put("courseName", courseName);
		return temTrainManageMapper.queryByTimeOrDeptCodeOrCourseName(conditionMap);
	}

	@Override
	public List<TemExamManage> queryTemExamManageByTOrDCOrENOrUCOrUN(
			String yearAndMonth, String deptCode, String examName, String userCode,
			String userName) {
		Map<String, Object> conditionMap = new HashMap<String, Object>(); 
		if(yearAndMonth.length()>7){ //处理输入的参数带天的，如 2016-06-01
			conditionMap.put("beginTime", yearAndMonth + " 00:00:00");
			conditionMap.put("endTime", yearAndMonth + " 23:59:59");
		}else{
			int lastDay = DateTimeUtils.getSumDatOfMonth(Integer.parseInt(yearAndMonth.split("-")[0]), Integer.parseInt(yearAndMonth.split("-")[1]));
			conditionMap.put("beginTime", yearAndMonth + "-01 00:00:00");
			conditionMap.put("endTime", yearAndMonth + "-" + lastDay + " 23:59:59");
		}
		conditionMap.put("deptCode", deptCode);
		conditionMap.put("examName", examName);
		conditionMap.put("userCode", userCode);
		conditionMap.put("userName", userName);
		return temExamManageMapper.queryByTOrDCOrENOrUCOrUN(conditionMap);
	}

	@Override
	public List<NurseLevelTraining> queryNurseLevelTraining() {
		return nurseLevelTrainingMapper.selectAll();
	}

	@Override
	public List<ContinuingEducation> queryContinuingEducation() {
		return outContinuingEducationMapper.selectAll();
	}

}
