package com.lachesis.mnisqm.module.user.domain;

public class TotalInfo {
    private Integer nurseSum;

    private Integer postPearsonSum;

    private Integer patientSum;

    private Integer criticalPatientSum;

    private Integer surgeryPatientSum;
    
    private Integer leaveNurseSum;
    
    private Integer allocateApplySum;

    public Integer getNurseSum() {
        return nurseSum;
    }

    public void setNurseSum(Integer nurseSum) {
        this.nurseSum = nurseSum;
    }

    public Integer getPostPearsonSum() {
        return postPearsonSum;
    }

    public void setPostPearsonSum(Integer postPearsonSum) {
        this.postPearsonSum = postPearsonSum;
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

	public Integer getLeaveNurseSum() {
		return leaveNurseSum;
	}

	public void setLeaveNurseSum(Integer leaveNurseSum) {
		this.leaveNurseSum = leaveNurseSum;
	}

	public Integer getAllocateApplySum() {
		return allocateApplySum;
	}

	public void setAllocateApplySum(Integer allocateApplySum) {
		this.allocateApplySum = allocateApplySum;
	}
}