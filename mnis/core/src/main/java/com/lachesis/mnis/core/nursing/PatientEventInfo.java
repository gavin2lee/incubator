package com.lachesis.mnis.core.nursing;

import java.util.List;

import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientEvent;

public class PatientEventInfo {
    private List<PatientEvent> eventInfoList; // required
    private Patient inpatientInfo; // required
    
	public List<PatientEvent> getEventInfoList() {
		return eventInfoList;
	}
	public void setEventInfoList(List<PatientEvent> eventInfoList) {
		this.eventInfoList = eventInfoList;
	}
	public Patient getInpatientInfo() {
		return inpatientInfo;
	}
	public void setInpatientInfo(Patient inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	public int getEventInfoListSize() {
		if(eventInfoList == null) {
			return 0;
		}
		return eventInfoList.size();
	}
}
