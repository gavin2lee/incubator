package com.lachesis.mnis.core.bodysign.entity;

import java.util.Date;

public class BodySignSpeedy {

	private String patientId;
	private String patientName;
	private String bedNo;
	private String showBedNo;// 显示床号
	private String inHospitalNo;

	private int tendLevel;
	private int daysInHospital;
	private int daysAfterSurgery;

	private Date lastMsmentTime;
	private Date nextMsmentTime;

	private BodySignItem temperatureItem;

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getInHospitalNo() {
		return inHospitalNo;
	}

	public void setInHospitalNo(String inHospitalNo) {
		this.inHospitalNo = inHospitalNo;
	}

	public int getTendLevel() {
		return tendLevel;
	}

	public void setTendLevel(int tendLevel) {
		this.tendLevel = tendLevel;
	}

	public int getDaysInHospital() {
		return daysInHospital;
	}

	public void setDaysInHospital(int daysInHospital) {
		this.daysInHospital = daysInHospital;
	}

	public int getDaysAfterSurgery() {
		return daysAfterSurgery;
	}

	public void setDaysAfterSurgery(int daysAfterSurgery) {
		this.daysAfterSurgery = daysAfterSurgery;
	}

	public Date getLastMsmentTime() {
		return lastMsmentTime;
	}

	public void setLastMsmentTime(Date lastMsmentTime) {
		this.lastMsmentTime = lastMsmentTime;
	}

	public Date getNextMsmentTime() {
		return nextMsmentTime;
	}

	public void setNextMsmentTime(Date nextMsmentTime) {
		this.nextMsmentTime = nextMsmentTime;
	}

	public BodySignItem getTemperatureItem() {
		return temperatureItem;
	}

	public void setTemperatureItem(BodySignItem temperatureItem) {
		this.temperatureItem = temperatureItem;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getShowBedNo() {
		return showBedNo;
	}

	public void setShowBedNo(String showBedNo) {
		this.showBedNo = showBedNo;
	}
}
