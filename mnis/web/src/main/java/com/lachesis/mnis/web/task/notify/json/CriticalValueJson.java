package com.lachesis.mnis.web.task.notify.json;

import com.lachesis.mnis.core.critical.entity.CriticalValue;

public class CriticalValueJson extends BaseJson{

	private String sendTime;
	private String nurseId;
	private String nurseName;
	private String status;
	
	private String sendPerson;
	private String inHospNo;
	private String criticalValue;
	private String pushId;
	
	public CriticalValueJson(){}

	public CriticalValueJson(String bedCode, String  patientName, String sendTime,
			String nurseId, String nurseName, String status, String sendPerson,
			String inHospNo, String criticalValue, String pushId) {
		super(bedCode, patientName);
		this.sendTime = sendTime;
		this.nurseId = nurseId;
		this.nurseName = nurseName;
		this.status = status;
		this.sendPerson = sendPerson;
		this.inHospNo = inHospNo;
		this.criticalValue = criticalValue;
		this.pushId = pushId;
	}

	public static String createJson(CriticalValue criticalValue) {
		return new CriticalValueJson(
				criticalValue.getBedCode(),
				criticalValue.getPatientName(),
				criticalValue.getSendTime(), 
				criticalValue.getNurseId(), 
				criticalValue.getNurseName(), 
				criticalValue.getStatus()==null ? "0" : criticalValue.getStatus(), 
				criticalValue.getSendPeople(), 
				criticalValue.getInHospNo(), 
				criticalValue.getCriticalValue(), 
				criticalValue.getCriticalValueId()).toJson();
	}

	public String getSendTime() {
		return sendTime;
	}

	public String getNurseId() {
		return nurseId;
	}

	public String getNurseName() {
		return nurseName;
	}

	public String getStatus() {
		return status;
	}

	public String getSendPerson() {
		return sendPerson;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public String getCriticalValue() {
		return criticalValue;
	}

	public String getPushId() {
		return pushId;
	}
}
