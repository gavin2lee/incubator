package com.lachesis.mnis.core.nursing;

import java.text.ParseException;
import java.util.List;


public interface NurseRecordService {
	List<NurseRecord> getNurseRecord(String date, String patientId) throws ParseException;
	
	NurseRecordConfig getNurseRecordConfig(String deptCode);
	
	int saveNurseRecord(NurseRecord nurseRecord, boolean modifying);
	
	/*******************************/
	/***** from xiangtan version****/
	/*******************************/
	TemplateBean getTemplate(String templateId);

	List<TemplateBean> getTemplateList(String deptId);

	List<TemplateItemBean> getRecordItemsById(String templateId,
			String patientId);

	String insertNurseRecord(TemplateBean requestBean);

	void updateNurseRecord(TemplateBean requestBean);

	List<TemplateBean> getRecordListByPatientId(String patientId);

	List<TemplateItemBean> getRecordItemsById(String recordId);

	String getDocRecordById(String recordId);

	TemplateBean getTableRecordById(Integer templateId, String patientId);

	TemplateBean getTableRecordById(String recordId);

	List<AssistantBean> getAssistantList();

	String getAssisValue(Integer parseInt);

	TemplateBean getChuangyeTemplateData(String recordId);

	void deleteRecord(Integer parseInt);
	
	TemplateItemBean getOneDayBodySignDataFromNurseDoc(String hospitalNo, String day, String[] elements);
	
}
