package com.lachesis.mnis.web.task.notify.json;

import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.patient.entity.Patient;

public class InspectionJson extends BaseJson{

	private String subject;

	public InspectionJson(){}
	
	public InspectionJson(String bedCode, String patientName, String patientId, String subject) {
		super(bedCode, patientName, patientId);
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public static String createJson(InspectionRecord record, Patient patient) {
		return new InspectionJson(
				patient.getBedCode(),
				patient.getName(),
				patient.getPatId(), 
				record.getSubject()).toJson();
	}
}
