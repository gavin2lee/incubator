package com.lachesis.mnisqm.module.user.domain;

public class BusyDeptInfo {
	private String deptName;

    private String bedRate;

    private Integer bedSum;

    private Integer patientSum;

    private Integer onJobNurseSum;

    private String bedNurseRate;

    private Integer criticalPatientSum;
    
    private Integer surgeryPatientSum;
    
    private Integer highRiskPatientSum;

    private Integer onLeaveNurseSum;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getBedRate() {
		return bedRate;
	}

	public void setBedRate(String bedRate) {
		this.bedRate = bedRate;
	}

	public Integer getBedSum() {
		return bedSum;
	}

	public void setBedSum(Integer bedSum) {
		this.bedSum = bedSum;
	}

	public Integer getPatientSum() {
		return patientSum;
	}

	public void setPatientSum(Integer patientSum) {
		this.patientSum = patientSum;
	}

	public Integer getOnJobNurseSum() {
		return onJobNurseSum;
	}

	public void setOnJobNurseSum(Integer onJobNurseSum) {
		this.onJobNurseSum = onJobNurseSum;
	}

	public String getBedNurseRate() {
		return bedNurseRate;
	}

	public void setBedNurseRate(String bedNurseRate) {
		this.bedNurseRate = bedNurseRate;
	}

	public Integer getCriticalPatientSum() {
		return criticalPatientSum;
	}

	public void setCriticalPatientSum(Integer criticalPatientSum) {
		this.criticalPatientSum = criticalPatientSum;
	}

	public Integer getSurgeryPatientSum() {
		return surgeryPatientSum;
	}

	public void setSurgeryPatientSum(Integer surgeryPatientSum) {
		this.surgeryPatientSum = surgeryPatientSum;
	}

	public Integer getHighRiskPatientSum() {
		return highRiskPatientSum;
	}

	public void setHighRiskPatientSum(Integer highRiskPatientSum) {
		this.highRiskPatientSum = highRiskPatientSum;
	}

	public Integer getOnLeaveNurseSum() {
		return onLeaveNurseSum;
	}

	public void setOnLeaveNurseSum(Integer onLeaveNurseSum) {
		this.onLeaveNurseSum = onLeaveNurseSum;
	}
}