package com.lachesis.mnis.web.task.notify.json;

import com.lachesis.mnis.core.util.GsonUtils;

public class BaseJson {
	
	private String bedCode;
	private String patientName;
	private String patientId;
	
	public BaseJson(){}

	public BaseJson(String bedCode, String patientName) {
		this.bedCode = bedCode;
		this.patientName = patientName;
	}
	
	public BaseJson(String bedCode, String patientName, String patientId) {
		this(bedCode, patientName);
		this.patientId = patientId;
	}


	public String toJson(){
		return GsonUtils.toJson(this);
	}
	
	public String getBedCode() {
		return bedCode;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getPatientId() {
		return patientId;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
}
