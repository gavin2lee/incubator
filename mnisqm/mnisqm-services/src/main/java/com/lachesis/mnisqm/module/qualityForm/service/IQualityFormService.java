package com.lachesis.mnisqm.module.qualityForm.service;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityForm;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityFormDetail;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIndex;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIndexItem;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityInfo;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIssueManage;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityModel;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityModelTrans;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityPlan;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityResult;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityResultDetail;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityTask;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityTeam;



public interface IQualityFormService{

	public int insertQualityForm(QualityForm qualityForm);
	
	public int updateQualityForm(QualityForm qualityForm);
	
	public void deleteQualityForm(QualityForm qualityForm);
	
	public List<QualityForm> queryAllQualityForm();
	
	public void insertQualityFormDetail(QualityFormDetail qualityFormDetail);
	
	public int updateQualityFormDetail(QualityFormDetail qualityFormDetail);
	
	public void deleteQualityFormDetail(QualityFormDetail qualityFormDetail);
	
	public int insertQualityTask(QualityTask qualityTask);
	
	public int updateQualityTask(QualityTask qualityTask);
	
	public void deleteQualityTask(QualityTask qualityTask);
	
	public int insertQualityResult(QualityResult qualityResult);
	
	public int updateQualityResult(QualityResult qualityResult);
	
	public void deleteQualityResult(QualityResult qualityResult);
	
	public List<QualityResult> queryAllQualityResult();
	
	public int insertQualityResultDetail(QualityResultDetail qualityResultDetail);
	
	public int updateQualityResultDetail(QualityResultDetail qualityResultDetail);
	
	public void deleteQualityResultDetail(QualityResultDetail qualityResultDetail);
	
	public int insertQualityIssueManage(QualityIssueManage qualityIssueManage);
	
	public int updateQualityIssueManage(QualityIssueManage qualityIssueManage);
	
	public void deleteQualityIssueManage(String seqId);
	
	/**
	 * 根据部门号和表单类型查找质量检查表
	 * @param deptCode 部门编号
	 * @param type 表单类型
	 * @return
	 */
	public List<QualityForm> queryQualityFormByDeptCodeAndType(String type);
	
	/**
	 * 根据条件查询质量检查任务
	 * @param date 任务日期
	 * @param deptCode 部门编号
	 * @param type 表单类型
	 * @return
	 */
	public List<QualityTask> queryQTRByTimeAndTypeAndDeptCode(String date, String deptCode, String type);
	
	/**
	 * 根据检查结果表编号查找质检问题处理记录
	 * @param resultCode
	 * @return
	 */
	public QualityIssueManage getQualityIssueManageByResultCode(String resultCode);
	
	/**
	 * 根据条件查询质量检查问题
	 * @param date 日期
	 * @param deptCode 部门编号
	 * @param type 表单类型
	 * @return
	 */
	public List<QualityIssueManage> queryQIMByTimeAndTypeAndDeptCode(String date, String deptCode, String type);
	
	/**
	 * 根据条件查询质量检查结果
	 * @param date 日期
	 * @param deptCode 部门编号
	 * @param formName 表单名称
	 * @return
	 */
	public List<QualityResult> queryQRByTimeAndFormNameAndDeptCode(String date, String deptCode);
	
	/**
	 * 获取质量管理主界面信息
	 * @return
	 */
	public List<QualityInfo> selectAllQualityInfo();
	
	/**
	 * 保存质控小组信息
	 * @param qualityTeam
	 */
	public void saveQualityTeam(QualityTeam qualityTeam);
	
	/**
	 * 根据组名获取质控小组
	 * @param teamName
	 * @return
	 */
	List<QualityTeam> selectQualityTeamByTeamName(String teamName);
	
	/**
	 * 删除质控小组
	 * 
	 */
	public void deletQualityTeamByPrimaryKey(QualityTeam qualityTeam);
	
	/**
	 * 保存指标分子分母项
	 * @param qualityIndexItem
	 */
	public void saveQualityIndexItem(QualityIndexItem qualityIndexItem);
	
	/**
	 * 获得指标分子分母项
	 * @param map
	 * @return
	 */
	List<QualityIndexItem> selectQualityIndexItem(Map<String, Object> map);
	
	/**
	 * 删除指标分子分母项
	 * @param seqId
	 */
	public void deleteQualityIndexItem(Long seqId);
	
	/**
	 * 保存调查模板
	 * @param qualityModel
	 */
	public Long saveQualityModel(QualityModel qualityModel);
	
	/**
	 * 查询调查模板
	 * @param qualityModel
	 * @return
	 */
	public List<QualityModel> selectQualityModel(QualityModel qualityModel);
	
	/**
	 * 根据id删除模板
	 * @param seqID
	 */
	public void deleteQualityModel(Long seqId);
	
	/**
	 * 查询调查计划
	 * @param qualityPlan
	 */
	public List<QualityPlan> selectQualityPlan(QualityPlan qualityPlan);
	
	/**保存调查计划
	 * @param qualityPlan
	 * @return
	 */
	public Long saveQualityPlan(QualityPlan qualityPlan);
	
	/**
	 * 根据id删除调查计划
	 * @param seqId
	 */
	public void deleteQualityPlan(Long seqId);
	
	/**
	 * 查询质量指标
	 * @param qualityIndex
	 * @return
	 */
	public List<QualityIndex> selectQualityIndex(QualityIndex qualityIndex);
	
	/**
	 * 保存质量指标
	 * @param qualityIndex
	 */
	public void saveQualityIndex(QualityIndex qualityIndex);
	
	/**
	 * 删除质量指标
	 * @param seqId
	 */
	public void deleteQualityIndex(Long seqId);
	
	/**
	 * 编辑质量项目
	 * @param qualityModelTrans
	 */
	public void savetModelDetail(QualityModelTrans qualityModelTrans);
	
	/**
	 * 复制模板
	 * @param seqId
	 */
	public QualityModel copyQualityModel(Long seqId, String userCode);
}
