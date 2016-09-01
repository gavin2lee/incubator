package com.lachesis.mnis.core.task;

import java.io.Serializable;

import com.lachesis.mnis.core.patient.entity.Patient;

public class TaskEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private String eventId;
	private Patient inpatientInfo;
	private String problem;
	private String interv;
	private String recNurseCode;
	private String eventTime;
	private String recNurseName;
	private String diagName;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Patient getInpatientInfo() {
		return inpatientInfo;
	}

	public void setInpatientInfo(Patient inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getInterv() {
		return interv;
	}

	public void setInterv(String interv) {
		this.interv = interv;
	}

	public String getRecNurseCode() {
		return recNurseCode;
	}

	public void setRecNurseCode(String recNurseCode) {
		this.recNurseCode = recNurseCode;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getRecNurseName() {
		return recNurseName;
	}

	public void setRecNurseName(String recNurseName) {
		this.recNurseName = recNurseName;
	}

	public String getDiagName() {
		return diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}

}
