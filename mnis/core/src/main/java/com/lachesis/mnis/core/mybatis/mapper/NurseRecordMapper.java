package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.nursing.NurseRecordEntity;
import com.lachesis.mnis.core.nursing.NurseRecordSpecItem;

public interface NurseRecordMapper {
	/**
	 * 获取科室的护理记录特殊文本结果项
	 * @param deptCode
	 * @return
	 */
	List<NurseRecordSpecItem> selectNurseRecordSpecItems(String deptCode);
	
	/**
	 * 获取科室的护理记录树状项目（类似生命体征且带子项目的项,如入量，出量）
	 * @param deptCode
	 * @return
	 */
	List<NurseRecordSpecItem> selectNurseRecordTreeItems(String deptCode);
	
	/**
	 * 插入护理记录
	 * @param nurseRecord
	 * @return
	 */
	int insertNurseRecord(NurseRecordEntity nurseRecord);
	
	/**
	 * 删除护理记录
	 * 实现为：将对应纪录置为无效
	 * @param nurseRecord
	 * @return
	 */
	int deleteNurseRecord(String masterRecordId);
	
	/**
	 * 获取护理记录
	 * @param date
	 * @param patientId
	 * @return
	 */
	List<NurseRecordEntity> selectNurseRecord(String patientId, String startDate, String endDate);
	
	
	
	//////////////FROM XIANTAN VERSION ////////////////////	
//	/**
//	 * 插入护理记录
//	 * @param nurseRecord
//	 * @return
//	 */
//	void insertNurseRecord(TemplateBean bean);
//	
//	/**
//	 * 获取护理记录
//	 * @param date
//	 * @param patientId
//	 * @return
//	 */
//	List<NurseRecordEntity> selectNurseRecord(String patientId, String day);
//
//	List<TemplateItemBean> getRecordItemsById(Integer recordId);
//
//	TemplateBean getTemplate(String templateId);
//
//	List<TemplateBean> getTemplateList(String deptId);
//
//	Integer getTopRecord(Integer templateId, String patientId);
//
//	List<TemplateBean> getRecordListByPatientId(String patientId);
//
//	String getDocRecordById(Integer recordId);
//
//	TemplateBean getTableRecordById(Integer recordId);
//
//	void getNewRecordId(String tableName, Integer rowCount);
//
//	Integer getCurrentRecordId(String string);
//
//	void insertNurseDocRecordItem(Integer templateId,String value);
//
//	void updateNurseDocRecordItem(Integer recordId, String value);
//
//	void updateNurseRecord(TemplateBean requestBean);
//
//	void insertNurseTableItem(TemplateItemBean templateItemBean);
//
//	TemplateItemBean getTemplateItemsById(Integer templateId, String jsnPth);
//
//	TemplateItemBean getRecordItem(Integer recordId, String jsnPth);
//
//	void updateNurseRecordItem(TemplateItemBean bean);
//
//	void insertNurseTreeMap(Integer recordItemId, String patientId,
//			String docFormat, String docType, Integer templateId);
//
//	Integer getNurseTreeMap(String patientId, Integer templateId);
//
//	List<AssistantBean> getAssistantList();
//
//	String getAssistValue(Integer assId);
//
//	TemplateBean getChuangyeTemplateData(String recordId);
//
//	String getCommitNurseId(String nurseId);
//
//	void deleteRecord(Integer recordId);
//	
//	TemplateItemBean getOneDayBodySignDataFromNurseDoc(String hospitalNo, String day, String[] elements);
}
