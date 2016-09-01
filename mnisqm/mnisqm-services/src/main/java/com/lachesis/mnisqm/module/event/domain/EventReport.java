package com.lachesis.mnisqm.module.event.domain;


public class EventReport {
    private Long seqId;

    private String reportCode;

    private String deptCode;

    private String deptName;

    private String damageLevel;
    
    private String damageLevelName;

    private String damageRemark;

    private String eventTime;

    private String eventTypeCode;

    private String eventTypeName;

    private String status;

    private String userCode;

    private String userName;

    private String userRole;

    private String patientBedNo;

    private String patientName;

    private String patientGender;
    
    private String patientGenderName;

    private String patientAge;

    private String patientHospNo;

    private String patientInDate;

    private String patientDiagnose;

    private String patientOther;

    private String eventDescribe;

    private String createPerson;

    private String updatePerson;
   
    private String taskCode;
    
    private String permission;//前端权限
    
    private String taskStatus;//处理状态

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
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

    public String getDamageLevel() {
        return damageLevel;
    }

    public void setDamageLevel(String damageLevel) {
        this.damageLevel = damageLevel;
    }

    public String getDamageRemark() {
        return damageRemark;
    }

    public void setDamageRemark(String damageRemark) {
        this.damageRemark = damageRemark;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getPatientBedNo() {
        return patientBedNo;
    }

    public void setPatientBedNo(String patientBedNo) {
        this.patientBedNo = patientBedNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientHospNo() {
        return patientHospNo;
    }

    public void setPatientHospNo(String patientHospNo) {
        this.patientHospNo = patientHospNo;
    }

    public String getPatientInDate() {
        return patientInDate;
    }

    public void setPatientInDate(String patientInDate) {
        this.patientInDate = patientInDate;
    }

    public String getPatientDiagnose() {
        return patientDiagnose;
    }

    public void setPatientDiagnose(String patientDiagnose) {
        this.patientDiagnose = patientDiagnose;
    }

    public String getPatientOther() {
        return patientOther;
    }

    public void setPatientOther(String patientOther) {
        this.patientOther = patientOther;
    }

    public String getEventDescribe() {
        return eventDescribe;
    }

    public void setEventDescribe(String eventDescribe) {
        this.eventDescribe = eventDescribe;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

	public String getPatientGenderName() {
		return patientGenderName;
	}

	public void setPatientGenderName(String patientGenderName) {
		this.patientGenderName = patientGenderName;
	}

	public String getDamageLevelName() {
		return damageLevelName;
	}

	public void setDamageLevelName(String damageLevelName) {
		this.damageLevelName = damageLevelName;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	
	
}