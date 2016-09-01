package com.lachesis.mnis.core.nursing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.bodysign.entity.BodySignItem;

public class NurseRecord {
    private List<BodySignItem> bodySignItemList; // required
    private List<NurseRecordSpecItem> nurseRecordSpecItemList; // required
    private Date recordDate; // required
    private String recordNurseCode; // required
    private String recordNurseName; // required
    private String patientId; // required
    private String remark; // required
    private String recordTypeCode; // required
    private String masRecordId; // required
    private int cooled; // required
    
    public NurseRecord() {
    }
    
	public List<BodySignItem> getBodySignItemList() {
		return bodySignItemList;
	}
	public void setBodySignItemList(List<BodySignItem> bodySignItemList) {
		this.bodySignItemList = bodySignItemList;
	}
	public List<NurseRecordSpecItem> getNurseRecordSpecItemList() {
		return nurseRecordSpecItemList;
	}
	public void setNurseRecordSpecItemList(
			List<NurseRecordSpecItem> nurseRecordSpecItemList) {
		this.nurseRecordSpecItemList = nurseRecordSpecItemList;
	}
	
	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRecordTypeCode() {
		return recordTypeCode;
	}
	public void setRecordTypeCode(String recordTypeCode) {
		this.recordTypeCode = recordTypeCode;
	}
	public String getMasRecordId() {
		return masRecordId;
	}
	public void setMasRecordId(String masRecordId) {
		this.masRecordId = masRecordId;
	}
	public int getCooled() {
		return cooled;
	}
	public void setCooled(int cooled) {
		this.cooled = cooled;
	}

	public void addToNurseRecordSpecItemList(NurseRecordSpecItem fromBodySign) {
		if(nurseRecordSpecItemList == null) {
			nurseRecordSpecItemList = new ArrayList<>();
		}
		nurseRecordSpecItemList.add(fromBodySign);
		
	}

	public void addToBodySignItemList(BodySignItem bsItem) {
		if(bodySignItemList == null) {
			bodySignItemList = new ArrayList<>();
		}
		bodySignItemList.add(bsItem);
	}
}
