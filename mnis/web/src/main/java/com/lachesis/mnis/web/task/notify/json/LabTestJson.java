package com.lachesis.mnis.web.task.notify.json;

import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.patient.entity.Patient;

public class LabTestJson extends BaseJson{

	private String subject;

	public LabTestJson(){}
	
	public LabTestJson(String bedCode, String patientName, String patientId, String subject) {
		super(bedCode, patientName, patientId);
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public static String createJson(LabTestRecord record, Patient patient) {
		return new LabTestJson(
				patient.getBedCode(),
				patient.getName(),
				patient.getPatId(), 
				record.getSubject()).toJson();
	}
}
