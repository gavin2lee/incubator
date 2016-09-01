package com.lachesis.mnis.core.patrol.entity;

import java.util.Date;

import com.lachesis.mnis.core.patient.entity.Patient;

public class WardPatrolInfo {
	/**
	 * 巡视患者信息
	 */
	private Patient inpatientInfo; // required
	/**
	 * 巡视时间
	 */
	private Date patrolDate; // required
	/**
	 * 下一次巡视时间
	 */
	private Date nextPatrolDate; // required
	/**
	 * 患者id
	 */
	private String patientId; // required
	/**
	 * 患者姓名
	 */
	private String patientName;
	/**
	 * 护士护士name
	 */
	private String nurseName;
	/**
	 * 护士护士工号
	 */
	private String nurseEmplCode;
	/**
	 * 巡视标记
	 * -1：待巡视，1：已巡视。特级护理患者永远为-1
	 */
	private int flag; // required
	/**
	 * 巡视id(主键)
	 */
	private String patrolId;
	/**
	 * 患者所在科室
	 */
	private String deptId;
	/**
	 * 巡视床号
	 */
	private String bedCode;
	/**
	 * 患者护理等级
	 */
	private Integer tendLevel;

	public Patient getInpatientInfo() {
		return inpatientInfo;
	}

	public void setInpatientInfo(Patient inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}

	public Date getPatrolDate() {
		return patrolDate;
	}

	public void setPatrolDate(Date patrolDate) {
		this.patrolDate = patrolDate;
	}

	public Date getNextPatrolDate() {
		return nextPatrolDate;
	}

	public void setNextPatrolDate(Date nextPatrolDate) {
		this.nextPatrolDate = nextPatrolDate;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getPatrolId() {
		return patrolId;
	}

	public void setPatrolId(String patrolId) {
		this.patrolId = patrolId;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public String getNurseEmplCode() {
		return nurseEmplCode;
	}

	public void setNurseEmplCode(String nurseEmplCode) {
		this.nurseEmplCode = nurseEmplCode;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Integer getTendLevel() {
		return tendLevel;
	}

	public void setTendLevel(Integer tendLevel) {
		this.tendLevel = tendLevel;
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

}
