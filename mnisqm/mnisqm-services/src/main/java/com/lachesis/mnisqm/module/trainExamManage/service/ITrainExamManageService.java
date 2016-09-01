package com.lachesis.mnisqm.module.trainExamManage.service;

import java.util.List;

import com.lachesis.mnisqm.module.remote.training.domain.ContinuingEducation;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemAttendanceManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemExamManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemPerformanceManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemTrainManage;
import com.lachesis.mnisqm.module.training.domain.NurseLevelTraining;

public interface ITrainExamManageService {
	
	public int insertTemTrainManage(TemTrainManage temTrainManage);
	
	public int updateTemTrainManage(TemTrainManage temTrainManage);
	
	public void deleteTemTrainManage(Long seqId);
	
	public int insertTemAttendanceManage(TemAttendanceManage temAttendanceManage);
	
	public int updateTemAttendanceManage(TemAttendanceManage temAttendanceManage);
	
	public void deleteTemAttendanceManage(Long seqId);
	
	public int insertTemExamManage(TemExamManage temExamManage);
	
	public int updateTemExamManage(TemExamManage temExamManage);
	
	public void deleteTemExamManage(Long seqId);
	
	public int insertTemPerformanceManage(TemPerformanceManage temPerformanceManage);
	
	public int updateTemPerformanceManage(TemPerformanceManage temPerformanceManage);
	
	public void deleteTemPerformanceManage(Long seqId);
	
	/**
	 * 根据条件查询培训信息
	 * @param yearAndMonth 开始年月   如：2016-06
	 * @param deptCode 部门编号
	 * @param courseName 培训课程名称
	 * @return
	 */
	public List<TemTrainManage> queryTemTrainManageByTDC(String yearAndMonth, String deptCode, String courseName);
	
	/**
	 * 根据条件查询考试信息
	 * @param yearAndMonth 开始年月   如：2016-06
	 * @param deptCode 部门编号
	 * @param examName 考试名称
	 * @param userCode 参与考试人编号
	 * @param userName 参与考试人姓名
	 * @return
	 */
	public List<TemExamManage> queryTemExamManageByTOrDCOrENOrUCOrUN(String yearAndMonth, String deptCode, String examName, String userCode, String userName);
	
	
	/**
	 * 获取护士层级培训信息（北大）
	 * @return
	 */
	public List<NurseLevelTraining> queryNurseLevelTraining();
	
	/**
	 * 获取继续教育信息（北大）
	 * @return
	 */
	public List<ContinuingEducation> queryContinuingEducation();
}
