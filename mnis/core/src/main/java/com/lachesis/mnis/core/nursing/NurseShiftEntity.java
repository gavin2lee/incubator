package com.lachesis.mnis.core.nursing;

import java.util.Date;
import java.util.List;

public class NurseShiftEntity {
	private String nurseShiftId;// 护士交班本id
	private String patientId;
	private String patientName;
	private String deptCode;
	private String deptName;
	private String hospNo;
	private String bedNo;
	private String diagnose;// 诊断
	private int tend;// 护理等级
	private String criticalStatus;// 危机状态
	private String shiftNurseId;// 交班护士id
	private String shiftNurseName;// 交班护士name
	private Date shiftDate;// 交班时间
	private String successionNurseId;// 接班护士id
	private String successionNurseName;// 接班护士name
	private Date successionDate;// 接班时间
	private Date shiftStartDate;// 时段开始时间
	private Date shiftEndDate;// 时段结束时间
	private String shiftStatus;// 交接班状态(0,1,2;未交班，已交班，已接班)
	private boolean valid;

	private List<NurseShiftRecordEntity> nurseShiftRecordEntities;// 交接班记录

	public String getNurseShiftId() {
		return nurseShiftId;
	}

	public void setNurseShiftId(String nurseShiftId) {
		this.nurseShiftId = nurseShiftId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getHospNo() {
		return hospNo;
	}

	public void setHospNo(String hospNo) {
		this.hospNo = hospNo;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public int getTend() {
		return tend;
	}

	public void setTend(int tend) {
		this.tend = tend;
	}

	public String getCriticalStatus() {
		return criticalStatus;
	}

	public void setCriticalStatus(String criticalStatus) {
		this.criticalStatus = criticalStatus;
	}

	public String getShiftNurseId() {
		return shiftNurseId;
	}

	public void setShiftNurseId(String shiftNurseId) {
		this.shiftNurseId = shiftNurseId;
	}

	public String getShiftNurseName() {
		return shiftNurseName;
	}

	public void setShiftNurseName(String shiftNurseName) {
		this.shiftNurseName = shiftNurseName;
	}

	public String getSuccessionNurseId() {
		return successionNurseId;
	}

	public void setSuccessionNurseId(String successionNurseId) {
		this.successionNurseId = successionNurseId;
	}

	public String getSuccessionNurseName() {
		return successionNurseName;
	}

	public void setSuccessionNurseName(String successionNurseName) {
		this.successionNurseName = successionNurseName;
	}

	public String getShiftStatus() {
		return shiftStatus;
	}

	public void setShiftStatus(String shiftStatus) {
		this.shiftStatus = shiftStatus;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public List<NurseShiftRecordEntity> getNurseShiftRecordEntities() {
		return nurseShiftRecordEntities;
	}

	public void setNurseShiftRecordEntities(
			List<NurseShiftRecordEntity> nurseShiftRecordEntities) {
		this.nurseShiftRecordEntities = nurseShiftRecordEntities;
	}

	public Date getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate;
	}

	public Date getSuccessionDate() {
		return successionDate;
	}

	public void setSuccessionDate(Date successionDate) {
		this.successionDate = successionDate;
	}

	public Date getShiftStartDate() {
		return shiftStartDate;
	}

	public void setShiftStartDate(Date shiftStartDate) {
		this.shiftStartDate = shiftStartDate;
	}

	public Date getShiftEndDate() {
		return shiftEndDate;
	}

	public void setShiftEndDate(Date shiftEndDate) {
		this.shiftEndDate = shiftEndDate;
	}

}
