package com.lachesis.mnis.core.bodysign.entity;

import java.util.List;

public class BodySignDailyRecord {
    private String recordDate; // required
    private String fullRecordDate;
    private String daysInHospital; // required
    private String daysAfterSurgery; // required
    private List<BodySignRecord> bodySignRecordList; // required
    
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getDaysInHospital() {
		return daysInHospital;
	}
	public void setDaysInHospital(String daysInHospital) {
		this.daysInHospital = daysInHospital;
	}
	public String getDaysAfterSurgery() {
		return daysAfterSurgery;
	}
	public void setDaysAfterSurgery(String daysAfterSurgery) {
		this.daysAfterSurgery = daysAfterSurgery;
	}
	public List<BodySignRecord> getBodySignRecordList() {
		return bodySignRecordList;
	}
	public void setBodySignRecordList(List<BodySignRecord> bodySignRecordList) {
		this.bodySignRecordList = bodySignRecordList;
	}
	public String getFullRecordDate() {
		return fullRecordDate;
	}
	public void setFullRecordDate(String fullRecordDate) {
		this.fullRecordDate = fullRecordDate;
	}
}
