package com.lachesis.mnis.core.bodysign.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;

/***
 * 
 * 生命体征记录
 *
 * @author yuliang.xu
 * @date 2015年6月5日 上午10:41:44 
 */
public class BodySignRecord {

	private int masterRecordId; 
	private String patientId; 
	private String patientName;
	private String deptCode;
	private String bedCode;
	private Date fullDateTime;
	private String recordNurseCode; 
	private String recordNurseName; 
	private String remark; 
	
	private String recordDay; 
	private String recordTime; 
	
	private Date modifyTime;
	private String modifyNurseCode; 
	private String modifyNurseName; 
	
	private String copyFlag;
	private List<BodySignItem> bodySignItemList; 
	/**
	 * 记录体征时的事件：入院，出院，手术
	 */
	private PatientEvent event; 
	private PatientSkinTest skinTestInfo; 
	int cooled; 

	private String inHospNo;
	private Date firstDate;
	
	private String patStatus;
	
	public boolean hasData() {
		return (bodySignItemList != null && !bodySignItemList.isEmpty())
				|| (event != null && event.isValid()) || (skinTestInfo != null);
	}
	
	public boolean hasBodySignData() {
		return (bodySignItemList != null && !bodySignItemList.isEmpty())
				|| (event != null && event.isValid());
	}

	public boolean hasBodySignItems() {
		return bodySignItemList != null && bodySignItemList.size() > 0;
	}

	public void addToBodySignItems(BodySignItem item) {
		if (bodySignItemList == null) {
			bodySignItemList = new ArrayList<>();
		}
		bodySignItemList.add(item);
	}

	public void addToBodySignItems(Collection<BodySignItem> items) {
		if (bodySignItemList == null) {
			bodySignItemList = new ArrayList<>();
		}
		bodySignItemList.addAll(items);
	}
	
	public void removeToBodySignItems(Collection<BodySignItem> items) {
		if (bodySignItemList == null) {
			bodySignItemList = new ArrayList<>();
		}
		bodySignItemList.removeAll(items);
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getRecordDay() {
		return recordDay;
	}

	public void setRecordDay(String recordDay) {
		this.recordDay = recordDay;
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

	public List<BodySignItem> getBodySignItemList() {
		return bodySignItemList;
	}

	public void setBodySignItemList(List<BodySignItem> bodySignItemList) {
		this.bodySignItemList = bodySignItemList;
	}

	public int getMasterRecordId() {
		return masterRecordId;
	}

	public void setMasterRecordId(int masterRecordId) {
		this.masterRecordId = masterRecordId;
	}

	public PatientEvent getEvent() {
		return event;
	}

	public void setEvent(PatientEvent event) {
		this.event = event;
	}

	public PatientSkinTest getSkinTestInfo() {
		return skinTestInfo;
	}

	public void setSkinTestInfo(PatientSkinTest skinTestInfo) {
		this.skinTestInfo = skinTestInfo;
	}

	public int getCooled() {
		if(bodySignItemList == null){
			return 0;
		}
		for (Iterator<BodySignItem> iterator = bodySignItemList.iterator(); iterator.hasNext();) {
			BodySignItem bodySign = (BodySignItem) iterator.next();
			if("cooledTemperature".equals(bodySign.getItemCode())){
				return 1;
			}
		}
		return 0;
		//return cooled;
	}

	public void setCooled(int cooled) {
		this.cooled = cooled;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Date getFullDateTime() {
		return fullDateTime;
	}

	public void setFullDateTime(Date fullDateTime) {
		this.fullDateTime = fullDateTime;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyNurseCode() {
		return modifyNurseCode;
	}

	public void setModifyNurseCode(String modifyNurseCode) {
		this.modifyNurseCode = modifyNurseCode;
	}

	public String getModifyNurseName() {
		return modifyNurseName;
	}

	public void setModifyNurseName(String modifyNurseName) {
		this.modifyNurseName = modifyNurseName;
	}

	public String getCopyFlag() {
		return copyFlag;
	}

	public void setCopyFlag(String copyFlag) {
		this.copyFlag = copyFlag;
	}

	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}

	public String getPatStatus() {
		return patStatus;
	}

	public void setPatStatus(String patStatus) {
		this.patStatus = patStatus;
	}
}