package com.lachesis.mnis.core.bodysign.repository;

import com.lachesis.mnis.core.bodysign.entity.*;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientEventDetail;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

/***
 * 
 * 生命体征Repository接口
 *
 * @author yuliang.xu
 * @date 2015年6月4日 下午3:16:30 
 *
 */
public interface BodySignRepository {

	/***
	 * 
	 * 获取生命体征项的字典记录
	 *
	 * @return List<BodySignDict>
	 */
	List<BodySignDict> getBodySignDicts();
	
	/***
	 * 查询生命体征
	 *
	 * @param masterId
	 */
	BodySignRecord getById(int masterId);
	
	/***
	 * 查询病人某一天生命体征
	 *
	 * @param date
	 * @param patientIds
	 * @return List<BodySignRecord>    返回类型 
	 */
	List<BodySignRecord> getBodySignRecord(Date date, String[] patientIds);
	
	List<BodySignRecord> getBodySignRecordByPatIdAndDate(Date date, String[] patientIds);

	/***
	 * 获取病人某项体征值最新的记录
	 *
	 * @param patId
	 * @param itemCode
	 */
	BodySignRecord getLastTemperatureItem(String patId, String itemCode);
	
	/**
	 * 根据病人流水号以及事件编号来查询病人的事件信息
	 *
	 * @param patId
	 * @param eventCode
	 * @return List<PatientEvent> 返回类型 
	 */
	List<PatientEvent> getEventInfosByPatIdAndCode(String patId, String eventCode,Date recordDate);
	
	/***
	 * 
	 * 根据病人流水号以及日期来查询病人某天的体征信息
	 *
	 * @param date
	 * @param patientId
	 */
	List<BodySignItem> getBodySignItemsByTimeAndPatient(Date date, String patientId);
	
	/**
	 * 统计病人生命体征某项录入次数
	 *
	 * @param date
	 * @param patientId
	 */
	List<BodySignItemCodeCount> countBodySignItemInDayByItemCode(Date date, String patientId);
	
	/**
	 * 保存生命体征
	 *
	 * @param record
	 */
	void save(BodySignRecord record);
	
	/**
	 * 保存生命体征中的bodySignItemList
	 *
	 * @param bodySignRecord
	 */
	void saveItemList(List<BodySignItem> list);
	
	/**
	 * 删除生命体征
	 *
	 * @param masterRecordId
	 */
	void delete(int masterRecordId);
	
	void delete(BodySignRecord record);

	/**
	 * 删除从文书转抄过来的生命体征备份数据
	 * @param record
     */
    public void deleteBodySignItemBackup(BodySignRecord record);

	/**
	 * 保存从文书转抄过来的声明体征备份数据
	 * @param record
     */
    public void saveBodySignItemBackup(BodySignRecord record);

	/**
	 * 更新生命体征
	 *
	 * @param record
	 */
	void update(BodySignRecord record);
	
	/**
	 * 保存体征主信息
	 * @param record
	 */
	void saveBodySignMaster(BodySignRecord record);
	
	/**
	 * 更新皮试中的主记录id
	 * @param masterRecordId
	 * @param orderId
	 */
	void updateSkinTestMasterRecordIdByOrderId(int masterRecordId,String orderId);
	
	
	/**
	 * 获取第一次录入生命体征数据 
	 * @param pat_id
	 * @return
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
	public List<BodySignItem> getBodySignItemByCode(String recordDate,String itemCode,String deptCode,String patId,Date startDate,Date endDate);
	
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
	 * @param deptCode 科室编号
     * @return 记录个数
     */
	public Integer getBodySignRecordCountForPatient(String patId);

	public int deleteBodySignItemByCode(String itemCode,String patId);
	/**
	 * 指定时间区间的入院事件个数
	 * @param patId
	 * @param eventCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getRyEventCount(String patId,String eventCode,Date startDate,Date endDate);
	
	
	/**
	 * 查询生命体征记录
	 * @param parm
	 * @return
	 */
	public List<BodySignRecord> queryBodySignList(String deptCode,String patId,String recordDate,List<String> items);
	
	/**
	 * 查询指定时间段指定体征项目的体征明细信息
	 * @param deptCode
	 * @param patId
	 * @param strDate
	 * @param endDate
	 * @param items
	 * @return
	 */
	public List<BodySignRecord> queryBodySignItemList(
			String deptCode,String patId,List<String> recordDates,List<String> items);
	
	/**
	 * 删除体征明细数据,通过记录时间
	 * @param patId
	 * @param recordDates
	 * @return
	 */
	public int deleteBodysingItemByTime(String patId,List<String> recordDates,List<String> items);
	
	/**
	 * 获取体温单时间点数据
	 * @param params
	 * @return
	 */
	public List<BodySignRecord> getFixTimeBodySignRecords(String[] patIds,List<Date> recordDates,List<String> itemCodes);
	
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
	 * 获取事件对应的时间
	 * @param patId
	 * @return
	 */
	String getEventDate(String patId,String eventCode);
	
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
	public void insertBodySignItems(List<BodySignItem> item);
	
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
	/**
	 * 获取生命体征配置信息
	 * @param deptCode
	 * @return
	 */
	List<BodySignItemConfig> getBodySignConfigByDeptCode(String deptCode);
	
	/**
	 * 查询转抄到生命体征的文书信息
	 * @param parm
	 * @return
	 */
	public List<BodySignItem> selectBodysignFromDoc(Map<String,String> parm);
	
	/**
	 * 查询指定时间内的出入院事件
	 * @param eventCode 事件编号 ，null表示查询出院和住院
	 * @param beginTime 
	 * @param endTime 
	 * @return
	 */
	public List<PatientEventDetail>queryInOutEventByTime(String eventCode, String beginTime, String endTime);
}
