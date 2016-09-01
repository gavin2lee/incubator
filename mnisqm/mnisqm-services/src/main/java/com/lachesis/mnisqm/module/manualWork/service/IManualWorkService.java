package com.lachesis.mnisqm.module.manualWork.service;

import java.util.List;

import com.lachesis.mnisqm.module.manualWork.domain.ManualWork;
import com.lachesis.mnisqm.module.manualWork.domain.ManualWorkDetail;
import com.lachesis.mnisqm.module.manualWork.domain.ManualWorkItem;

public interface IManualWorkService {
	
	/**
	 * 新增工作项目
	 * @param manualWorkItem 
	 * @return
	 */
	public int insertManualWorkItem(ManualWorkItem manualWorkItem);
	
	/**
	 * 修改工作项目
	 * @param manualWorkItem
	 * @return
	 */
	public int updateManualWorkItem(ManualWorkItem manualWorkItem);
	
	/**
	 * 删除工作项目
	 * @param manualWorkItem
	 */
	public void deleteManualWorkItem(ManualWorkItem manualWorkItem);
	
	/**
	 * 新增工作安排记录
	 * @param manualWork
	 * @return
	 */
	public int insertManualWork(ManualWork manualWork);
	
	/**
	 * 修改工作安排记录
	 * @param manualWork
	 * @return
	 */
	public int updateManualWork(ManualWork manualWork);
	
	/**
	 * 删除工作安排记录
	 * @param manualWork
	 * @return
	 */
	public void deleteManualWork(ManualWork manualWork);
	
	/**
	 * 新增工作明细
	 * @param manualWorkDetail
	 * @return
	 */
	public int insertManualWorkDetail(ManualWorkDetail manualWorkDetail);
	
	/**
	 * 修改工作明细
	 * @param manualWorkDetail
	 * @return
	 */
	public int updateManualWorkDetail(ManualWorkDetail manualWorkDetail);
	
	/**
	 * 删除工作明细
	 * @param manualWorkDetail
	 */
	public void deleteManualWorkDetail(ManualWorkDetail manualWorkDetail);
	
	/**
	 * 根据科室名称获取工作项目
	 * @param deptCode 科室编号
	 * @return
	 */
	public List<ManualWorkItem> queryManualWorkItemByDeptCode(String deptCode);
	
	/**
	 * 根据科室编号和日期查询工作安排记录
	 * @param deptCode
	 * @param date
	 * @return
	 */
	public List<ManualWork> queryManualWorkByDeptCodeAndDate(String deptCode,String beginTime,String endTime);
	
}
