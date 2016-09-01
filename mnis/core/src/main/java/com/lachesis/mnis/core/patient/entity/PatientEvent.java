package com.lachesis.mnis.core.patient.entity;

import java.util.Date;

import org.apache.commons.lang.StringUtils;


/***
 * 
 * 病人事件
 *
 * @author yuliang.xu
 * @date 2015年6月16日 下午2:18:17 
 *
 */
public class PatientEvent {
	
	/**事件自增长号*/
	private int id;
	/**住院流水号*/
	private String patientId;
	/**患者姓名*/
	private String patientName;
	/**体征记录主ID*/
	private String masterRecordId;
	/**事件时间*/
	private String recordDate;
	/**事件中文时间，防止查询是对eventDate进行转换*/
	private String chineseEventDate;
	/**记录人工号*/
	private String recorderId;
	/**记录人姓名*/
	private String recorderName;
	/**等同体征中measureNoteCode*/
	private String eventCode;
	/**事件名称*/
	private String eventName;
	
	private Date record_date;   //体征记录时间点
	private int index;
	
	private String status;//事件状态   01:新建  09:已删除 

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 事件是否有效
	 * 
	 * @return
	 */
	public boolean isValid() {
		return StringUtils.isNotBlank(eventCode) || StringUtils.isNotBlank(eventName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getMasterRecordId() {
		return masterRecordId;
	}

	public void setMasterRecordId(String masterRecordId) {
		this.masterRecordId = masterRecordId;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public String getChineseEventDate() {
		return chineseEventDate;
	}

	public void setChineseEventDate(String chineseEventDate) {
		this.chineseEventDate = chineseEventDate;
	}

	public String getRecorderId() {
		return recorderId;
	}

	public void setRecorderId(String recorderId) {
		this.recorderId = recorderId;
	}

	public String getRecorderName() {
		return recorderName;
	}

	public void setRecorderName(String recorderName) {
		this.recorderName = recorderName;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Date getRecord_date() {
		return record_date;
	}

	public void setRecord_date(Date record_date) {
		this.record_date = record_date;
	}
	
	
}
