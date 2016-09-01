package com.lachesis.mnis.core.nursing;

import java.util.Date;

/**
 * 护士交接班护理记录信息
 * 
 * @author liangming.deng
 *
 */
public class NurseShiftRecordEntity {
	private String shiftRecordId;// 护理记录id
	private String nurseShiftId;// 交班本id
	private String shiftRecordNurseId;// 护理记录护士id
	private String shiftRecordNurseName;// 护理记录姓名
	private Date shiftRecordTime;// 护理记录时间
	private Date shiftRecordStartDate;//交班区间记录开始时间
	private Date shiftRecordEndDate;//交班区间记录结束时间
	private String shiftRecordData;// 护理记录数据
	private boolean isValid;//是否有效
	private String patientId;
	private String deptCode;
	
	public String getShiftRecordId() {
		return shiftRecordId;
	}
	public void setShiftRecordId(String shiftRecordId) {
		this.shiftRecordId = shiftRecordId;
	}
	public String getNurseShiftId() {
		return nurseShiftId;
	}
	public void setNurseShiftId(String nurseShiftId) {
		this.nurseShiftId = nurseShiftId;
	}
	public String getShiftRecordNurseId() {
		return shiftRecordNurseId;
	}
	public void setShiftRecordNurseId(String shiftRecordNurseId) {
		this.shiftRecordNurseId = shiftRecordNurseId;
	}
	public String getShiftRecordNurseName() {
		return shiftRecordNurseName;
	}
	public void setShiftRecordNurseName(String shiftRecordNurseName) {
		this.shiftRecordNurseName = shiftRecordNurseName;
	}
	
	public Date getShiftRecordTime() {
		return shiftRecordTime;
	}
	public void setShiftRecordTime(Date shiftRecordTime) {
		this.shiftRecordTime = shiftRecordTime;
	}
	public Date getShiftRecordStartDate() {
		return shiftRecordStartDate;
	}
	public void setShiftRecordStartDate(Date shiftRecordStartDate) {
		this.shiftRecordStartDate = shiftRecordStartDate;
	}
	public Date getShiftRecordEndDate() {
		return shiftRecordEndDate;
	}
	public void setShiftRecordEndDate(Date shiftRecordEndDate) {
		this.shiftRecordEndDate = shiftRecordEndDate;
	}
	public String getShiftRecordData() {
		return shiftRecordData;
	}
	public void setShiftRecordData(String shiftRecordData) {
		this.shiftRecordData = shiftRecordData;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
}
