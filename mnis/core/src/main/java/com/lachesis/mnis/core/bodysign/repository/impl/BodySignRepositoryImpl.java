package com.lachesis.mnis.core.bodysign.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.bodysign.entity.BodySignDeleteRecord;
import com.lachesis.mnis.core.bodysign.entity.BodySignDict;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignItemCodeCount;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.bodysign.entity.BodySignItemConfig;
import com.lachesis.mnis.core.bodysign.entity.SpecialVo;
import com.lachesis.mnis.core.bodysign.repository.BodySignRepository;
import com.lachesis.mnis.core.mybatis.mapper.BodySignMapper;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientEventDetail;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;

@Repository("bodySignRepository")
public class BodySignRepositoryImpl implements BodySignRepository {

	@Autowired
	BodySignMapper bodySignMapper;
	
	@Override
	public List<BodySignDict> getBodySignDicts() {
		return bodySignMapper.getBodysignDicts();
	}
	
	@Override
	public List<BodySignRecord> getBodySignRecord(Date date, String[] patientIds) {
		return bodySignMapper.getBodySignRecords(date, patientIds);
	}
	
	@Override
	public List<PatientEvent> getEventInfosByPatIdAndCode(String patId, String eventCode,Date recordDate) {
		return bodySignMapper.getEventInfosByPatIdAndEventCode(patId, eventCode,recordDate);
	}

	@Override
	public String getEventDate(String patId,String eventCode){
		return bodySignMapper.getEventDate(patId, eventCode);
	}
	
	@Override
	public BodySignRecord getLastTemperatureItem(String patId, String itemCode) {
		return bodySignMapper.getLastTemperatureItem(patId, itemCode);
	}
	
	@Override
	public List<BodySignItem> getBodySignItemsByTimeAndPatient(
			Date date, String patientId) {
		return bodySignMapper.getBodySignItemsByTimeAndPatient(date, patientId);
	}
	

	@Override
	public List<BodySignItemCodeCount> countBodySignItemInDayByItemCode(
			Date date, String patientId) {
		return bodySignMapper.getBodySignItemInDayByItemCode(date, patientId);
	}
	
	@Override
	public void delete(int id) {
		bodySignMapper.delete(id);
	}

	@Override
	public BodySignRecord getById(int masterId) {
		return bodySignMapper.getBodySignRecordById(masterId);
	}

	@Override
	public void save(BodySignRecord record) {
		bodySignMapper.save(record);
	}

	@Override
	public void saveItemList(List<BodySignItem> list) {
		bodySignMapper.saveItemList(list);
	}

	@Override
	public void update(BodySignRecord record) {
		bodySignMapper.delete(record.getMasterRecordId());
		bodySignMapper.save(record);
	}

	@Override
	public void saveBodySignMaster(BodySignRecord record) {
		bodySignMapper.saveBodySignMaster(record);
	}

	@Override
	public void updateSkinTestMasterRecordIdByOrderId(int masterRecordId,
			String orderId) {
		HashMap<String, Object> params =new HashMap<String, Object>();
		params.put("masterRecordId", masterRecordId);
		params.put("orderId", orderId);
		bodySignMapper.updateSkinTestMasterRecordIdByOrderId(params);
	}

	@Override
	public List<SpecialVo> getFirstBodySigns(String pat_id) {
		return bodySignMapper.getFirstBodySigns(pat_id);
	}

	@Override
	public void delete(BodySignRecord record) {
		bodySignMapper.deleteBodySignRecord(record);
	}

	@Override
	public void deleteBodySignItemBackup(BodySignRecord record) {
		bodySignMapper.deleteBodySignItemBackup(record);
	}

	@Override
	public void saveBodySignItemBackup(BodySignRecord record) {
		bodySignMapper.saveBodySignItemBackup(record.getBodySignItemList());
	}

	@Override
	public int getPatBodySign(BodySignRecord record) {
		return bodySignMapper.getPatBodySign(record);
	}

	@Override
	public List<BodySignItem> getBodySignItemByCode(String recordDate, String itemCode,String deptCode,String patId,Date startDate,Date endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("recordDate", recordDate);
		params.put("itemCode", itemCode);
		params.put("deptCode", deptCode);
		params.put("recordDate", recordDate);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("patId", patId);
		return bodySignMapper.getBodySignItemByCode(params);
	}

	@Override
	public List<BodySignRecord> getBodySignRecordByRecordDate(
			String recordDate, String patId, String deptCode) {
		return bodySignMapper.getBodySignRecordByRecordDate(recordDate,patId, deptCode);
	}

	@Override
	public Integer getBodySignRecordCountForPatient(String patId) {
		return bodySignMapper.getBodySignRecordCountForPatient(patId);
	}

	@Override
	public int deleteBodySignItemByCode(String itemCode, String patId) {
		return bodySignMapper.deleteBodySignItemByCode(itemCode, patId);
	}

	@Override
	public int getRyEventCount(String patId, String eventCode, Date startDate,
			Date endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("patId", patId);
		params.put("eventCode", eventCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return bodySignMapper.getRyEventCount(params);
	}

	@Override
	public List<BodySignRecord> queryBodySignList(String deptCode,String patId,String recordDate,List<String> items) {
		//数据查询
		Map<String,Object> parm = new HashMap<String,Object>();
		parm.put("patId", patId);//患者ID
		parm.put("deptCode", deptCode);//科室编号
		parm.put("recordDate", recordDate);//记录日期时间
		parm.put("items",items);
		return bodySignMapper.queryBodySignList(parm);
	}

	@Override
	public List<BodySignRecord> getBodySignRecordByPatIdAndDate(Date date,
			String[] patientIds) {
		return bodySignMapper.getBodySignRecordsByPatIdAndDate(date, patientIds);
	}
	
	@Override
	public List<BodySignRecord> queryBodySignItemList(
			String deptCode,String patId,List<String> recordDates,List<String> items){
		//数据查询
		Map<String,Object> parm = new HashMap<String,Object>();
		parm.put("patId", patId);//患者ID
		parm.put("deptCode", deptCode);//科室编号
		parm.put("recordDates", recordDates);//记录日期时间
		parm.put("items",items);
		//数据处理
		List<BodySignRecord> rs = bodySignMapper.queryBodySignItemList(parm);
		return rs;
	}
	
	@Override
	public int deleteBodysingItemByTime(String patId,List<String> recordDates,List<String> items){
		//数据查询
		Map<String,Object> parm = new HashMap<String,Object>();
		parm.put("patId", patId);//患者ID
		parm.put("recordDates",recordDates);//记录时间列表
		parm.put("items",items);//项目编号
		return bodySignMapper.deleteBodysingItemByTime(parm);
	}

	@Override
	public List<BodySignRecord> getFixTimeBodySignRecords(
			String[] patIds,List<Date> recordDates,List<String> itemCodes) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("patIds", patIds);
		params.put("recordDates", recordDates);
		params.put("itemCodes", itemCodes);
		return bodySignMapper.getFixTimeBodySignRecords(params);
	}
	
	/**
	 * 生命体征主表查询
	 * @param record
	 * @return
	 */
	public BodySignRecord queryBodySignRecord(BodySignRecord record){
		return bodySignMapper.queryBodySignRecord(record);
	}
	
	/**
	 * 
	 * 查询生命体征item信息
	 * @param item
	 * @return
	 */
	public List<BodySignItem> queryBodySignItem(BodySignItem item){
		return bodySignMapper.queryBodySignItem(item);
	}
	
	/**
	 * 查询事件
	 * @param event
	 * @return
	 */
	public List<PatientEvent> queryEvent(PatientEvent event){
		return bodySignMapper.queryEvent(event);
	}

	@Override
	public int insertBodySignRecord(BodySignRecord record) {
		 bodySignMapper.insertBodySignRecord(record);
		 return record.getMasterRecordId();
	}

	@Override
	public void updateBodySignMaster(BodySignRecord record) {
		bodySignMapper.updateBodySignMaster(record);
	}

	@Override
	public void insertBodySignItems(List<BodySignItem> item) {
		Map<String,List<BodySignItem>> itemMap = new HashMap<String,List<BodySignItem>>();
		itemMap.put("itemList", item);
		bodySignMapper.insertBodySignItems(itemMap);
	}

	@Override
	public void updateBodysignItem(BodySignItem item) {
		bodySignMapper.updateBodysignItem(item);
	}

	@Override
	public void insertPatEvent(PatientEvent event) {
		bodySignMapper.insertPatEvent(event);
	}

	@Override
	public void updatePatEvent(PatientEvent event) {
		bodySignMapper.updatePatEvent(event);
	}

	@Override
	public void updateSkinTest(PatientSkinTest test) {
		bodySignMapper.updateSkinTest(test);
	}

	@Override
	public List<PatientSkinTest> querySkinTest(PatientSkinTest test) {
		return bodySignMapper.querySkinTest(test);
	}

	@Override
	public void insertPatientSkinTest(PatientSkinTest test) {
		bodySignMapper.insertPatientSkinTest(test);
	}

	@Override
	public void insertBodySignDeleteRecord(BodySignDeleteRecord record) {
		bodySignMapper.insertBodySignDeleteRecord(record);
	}

	@Override
	public List<BodySignItemConfig> getBodySignConfigByDeptCode(String deptCode) {
		return bodySignMapper.getBodySignConfigByDeptCode(deptCode);
	}
	
	@Override
	public List<BodySignItem> selectBodysignFromDoc(Map<String,String> parm){
		return bodySignMapper.selectBodysignFromDoc(parm);
	}

	@Override
	public List<PatientEventDetail> queryInOutEventByTime(String eventCode,
			String beginTime, String endTime) {
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		conditionMap.put("beginTime", beginTime);
		conditionMap.put("endTime", endTime);
		conditionMap.put("eventCode", eventCode);
		return bodySignMapper.queryInOutEventByTime(conditionMap);
	}


}
