package com.lachesis.mnisqm.module.user.domain;

public class DeptSchedulInfo {
    private String deptName;

    private String headNurse;

    private Integer nurseSum;

    private Integer patientSum;

    private Integer criticalPatientSum;

    private Integer surgeryPatientSum;

    private String aShift;

    private String pShift;

    private String nShift;
    
    private int onLeaveNurseSum;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getHeadNurse() {
        return headNurse;
    }

    public void setHeadNurse(String headNurse) {
        this.headNurse = headNurse;
    }

    public int getOnLeaveNurseSum() {
		return onLeaveNurseSum;
	}

	public void setOnLeaveNurseSum(int onLeaveNurseSum) {
		this.onLeaveNurseSum = onLeaveNurseSum;
	}

	public Integer getNurseSum() {
        return nurseSum;
    }

    public void setNurseSum(Integer nurseSum) {
        this.nurseSum = nurseSum;
    }

    public Integer getPatientSum() {
        return patientSum;
    }

    public void setPatientSum(Integer patientSum) {
        this.patientSum = patientSum;
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

    public String getaShift() {
        return aShift;
    }

    public void setaShift(String aShift) {
        this.aShift = aShift;
    }

    public String getpShift() {
        return pShift;
    }

    public void setpShift(String pShift) {
        this.pShift = pShift;
    }

    public String getnShift() {
        return nShift;
    }

    public void setnShift(String nShift) {
        this.nShift = nShift;
    }
}