package com.lachesis.mnis.core.mybatis.mapper;

import com.lachesis.mnis.core.bodysign.entity.*;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientEventDetail;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BodySignMapper {

	/***
	 * 查询bodysign item字典数据
	 */
	List<BodySignDict> getBodysignDicts();

	/**
	 * 获取某天的生命体征记录
	 * 
	 * @param date 指定日期必须是 yyyy-MM-dd格式
	 * @return
	 */
	List<BodySignRecord> getBodySignRecords(Date date, String[] patientIds);
	
	List<BodySignRecord> getBodySignRecordsByPatIdAndDate(Date date, String[] patientIds);

	BodySignRecord getLastTemperatureItem(String patId, String itemCode);
	
	List<PatientEvent> getEventInfosByPatIdAndEventCode(String patId, String eventCode,Date recordDate);
	
	String getEventDate(String patId,String eventCode);
	
	int getRyEventCount(HashMap<String, Object> params);

	/**
	 * 根据主键获取生命体征信息
	 * 
	 * @param masterId
	 * @return
	 */
	BodySignRecord getBodySignRecordById(int id);

	List<BodySignItem> getBodySignItemsByTimeAndPatient(Date date, String patientId);

	List<BodySignItemCodeCount> getBodySignItemInDayByItemCode(Date date, String patientId);
	
	void delete(int id);
	
	void deleteBodySignRecord(BodySignRecord record);

	void deleteBodySignItemBackup(BodySignRecord record);

	void saveBodySignItemBackup(List<BodySignItem> list);
	
	void save(BodySignRecord record);
	
	void saveItemList(List<BodySignItem> list);
	
	void saveBodySignMaster(BodySignRecord record);
	
	void updateSkinTestMasterRecordIdByOrderId(HashMap<String, Object> params);
	
	/**
	 * 查询患者第一次录入生命体征数据  key：value
	 * @param patientId
	 */
	public List<SpecialVo> getFirstBodySigns(String pat_id);
	
	/**
	 * 如果返回大于0，则该人在该时间点已经录入过体征项目，请选择该时间点进行修改
	 * @param record
	 * @return
	 */
	public int getPatBodySign(BodySignRecord record);
	
	/**
	 * 根据记录时间和体征项获取记录
	 * @param recordDate
	 * @param itemCode
	 * @param deptCode
	 * @return
	 */
	public List<BodySignItem> getBodySignItemByCode(HashMap<String, Object> params);
	
	 /**
	  * 根据记录时间获取体征记录
	  * @param recordDate
	  * @param deptCode
	  * @return
	  */
	public List<BodySignRecord> getBodySignRecordByRecordDate(String recordDate,String patId,
			String deptCode);

	/**
	 * 获取患者名下已有的体征记录个数
	 * @param patId 患者住院号
	 * @return 记录个数
	 */
    public Integer getBodySignRecordCountForPatient(String patId);

	/**
	 * 根据itemCode删除体征项
	 * @param itemCode
	 * @param deptCode
	 * @return
	 */
	public int deleteBodySignItemByCode(String itemCode, String patId);
	
	/**
	 * 查询生命体征
	 * @param parm
	 * @return
	 */
	public List<BodySignRecord> queryBodySignList(Map<String, Object> parm);
	
	/**
	 * 查询生命体征明细
	 * @param parm
	 * @return
	 */
	public List<BodySignRecord> queryBodySignItemList(Map<String, Object> parm);
	
	/**
	 * 体征明细删除，通过记录时间列表
	 * @param parm
	 * @return
	 */
	public int deleteBodysingItemByTime(Map<String,Object> parm);
	
	/**
	 * 获取体温单时间点数据
	 * @param params
	 * @return
	 */
	public List<BodySignRecord> getFixTimeBodySignRecords(Map<String, Object> params);
	
	/**
	 * 生命体征主表查询
	 * @param record
	 * @return
	 */
	public BodySignRecord queryBodySignRecord(BodySignRecord record);
	
	/**
	 * 
	 * 查询生命体征item信息
	 * @param item
	 * @return
	 */
	public List<BodySignItem> queryBodySignItem(BodySignItem item);
	
	/**
	 * 查询事件
	 * @param event
	 * @return
	 */
	public List<PatientEvent> queryEvent(PatientEvent event);
	
	/**
	 * 数据插入
	 * @param record
	 */
	public int insertBodySignRecord(BodySignRecord record);
	
	/**
	 * 数据更新-生命体征主表
	 * @param record
	 */
	public void updateBodySignMaster(BodySignRecord record);
	
	/**
	 * 数据录入-生命体征item
	 * @param item
	 */
	public void insertBodySignItems(Map<String,List<BodySignItem>> itemMap);
	
	/**
	 * 数据更新-生命体征item
	 */
	public void updateBodysignItem(BodySignItem item);
	
	/**
	 * 数据录入-事件
	 */
	public void insertPatEvent(PatientEvent event);
	
	/**
	 * 数据更新-事件
	 */
	public void updatePatEvent(PatientEvent event);
	
	/**
	 * 更新皮试信息
	 */
	public void updateSkinTest(PatientSkinTest test);
	
	/**
	 * 查询皮试信息
	 * @param test
	 * @return
	 */
	public List<PatientSkinTest> querySkinTest(PatientSkinTest test);
	
	/**
	 * 插入皮试信息
	 * @param test
	 */
	public void insertPatientSkinTest(PatientSkinTest test);
	/**
	 * 插入待删除体征信息
	 * @param record
	 */
	void insertBodySignDeleteRecord(BodySignDeleteRecord record);
	
	List<BodySignItemConfig> getBodySignConfigByDeptCode(String deptCode);
	
	/**
	 * 查询生命体征文书数据
	 * @param parm
	 * @return
	 */
	public List<BodySignItem> selectBodysignFromDoc(Map<String,String> parm);
	
	/**
	 * 查询指定时间内的出入院事件
	 * @param parm
	 * @return
	 */
	public List<PatientEventDetail> queryInOutEventByTime(Map<String,Object> conditionMap);

}
