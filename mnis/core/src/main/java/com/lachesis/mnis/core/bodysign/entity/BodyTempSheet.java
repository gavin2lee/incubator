package com.lachesis.mnis.core.bodysign.entity;

import java.util.ArrayList;
import java.util.List;

import com.lachesis.mnis.core.patient.entity.Patient;

public class BodyTempSheet {
	private Patient patientInfo; 
	private List<BodySignDailyRecord> bodySignDailyRecordList; 
	private int nextFlag; 

	public Patient getPatientInfo() {
		return patientInfo;
	}

	public void setPatientInfo(Patient patientInfo) {
		this.patientInfo = patientInfo;
	}

	public List<BodySignDailyRecord> getBodySignDailyRecordList() {
		return bodySignDailyRecordList;
	}

	public void setBodySignDailyRecordList(
			List<BodySignDailyRecord> bodySignDailyRecordList) {
		this.bodySignDailyRecordList = bodySignDailyRecordList;
	}

	public void addToBodySignDailyRecordList(
			BodySignDailyRecord bodySignDailyRecord) {
		if (bodySignDailyRecordList == null) {
			bodySignDailyRecordList = new ArrayList<>();
		}
		bodySignDailyRecordList.add(bodySignDailyRecord);

	}

	public int getNextFlag() {
		return nextFlag;
	}

	public void setNextFlag(int nextFlag) {
		this.nextFlag = nextFlag;
	}

	public BodyTempSheet() {}
	
	public BodyTempSheet(Patient patientInfo, int nextFlag) {
		this();
		this.patientInfo = patientInfo;
		this.nextFlag = nextFlag;
	}
	
	public BodyTempSheet(Patient patientInfo,
			List<BodySignDailyRecord> bodySignDailyRecordList, int nextFlag) {
		this(patientInfo, nextFlag);
		this.bodySignDailyRecordList = bodySignDailyRecordList;
	}
}
