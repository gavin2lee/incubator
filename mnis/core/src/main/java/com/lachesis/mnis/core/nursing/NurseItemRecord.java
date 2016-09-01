package com.lachesis.mnis.core.nursing;

import java.io.Serializable;
import java.util.List;

public class NurseItemRecord implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8865459822695659748L;
	private List<NurseItem> nurseItemList; // required
    private String recordDate; // required
    private String recordNurseCode; // required
    private String recordNurseName; // required
    private String patientId; // required
	private int masterRecordId;
	
	public List<NurseItem> getNurseItemList() {
		return nurseItemList;
	}
	public void setNurseItemList(List<NurseItem> nurseItemList) {
		this.nurseItemList = nurseItemList;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getRecordNurseCode() {
		return recordNurseCode;
	}
	public void setRecordNurseCode(String recordNurseCode) {
		this.recordNurseCode = recordNurseCode;
	}
	public String getRecordNurseName() {
		return recordNurseName;
	}
	public void setRecordNurseName(String recordNurseName) {
		this.recordNurseName = recordNurseName;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public int getMasterRecordId() {
		return masterRecordId;
	}
	public void setMasterRecordId(int masterRecordId) {
		this.masterRecordId = masterRecordId;
	}
}
