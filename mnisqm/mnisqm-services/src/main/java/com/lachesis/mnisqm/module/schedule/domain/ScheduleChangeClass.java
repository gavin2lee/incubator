package com.lachesis.mnisqm.module.schedule.domain;


public class ScheduleChangeClass {
    private Long seqId;

    private String applyUserCode;

    private String status;

    private String changeUserCode;

    private String applyClass;

    private String applyClassDate;

    private String changeClass;

    private String changeClassDate;

    private String changeCause;

    private String refuseCause;
    
    private String permission;//权限
    
    private String approveUserCode;//审核人

    private String createPerson;

    private String updatePerson;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getApplyUserCode() {
        return applyUserCode;
    }

    public void setApplyUserCode(String applyUserCode) {
        this.applyUserCode = applyUserCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChangeUserCode() {
        return changeUserCode;
    }

    public void setChangeUserCode(String changeUserCode) {
        this.changeUserCode = changeUserCode;
    }

    public String getApplyClass() {
        return applyClass;
    }

    public void setApplyClass(String applyClass) {
        this.applyClass = applyClass;
    }

    public String getApplyClassDate() {
        return applyClassDate;
    }

    public void setApplyClassDate(String applyClassDate) {
        this.applyClassDate = applyClassDate;
    }

    public String getChangeClass() {
        return changeClass;
    }

    public void setChangeClass(String changeClass) {
        this.changeClass = changeClass;
    }

    public String getChangeClassDate() {
        return changeClassDate;
    }

    public void setChangeClassDate(String changeClassDate) {
        this.changeClassDate = changeClassDate;
    }

    public String getChangeCause() {
        return changeCause;
    }

    public void setChangeCause(String changeCause) {
        this.changeCause = changeCause;
    }

    public String getRefuseCause() {
        return refuseCause;
    }

    public void setRefuseCause(String refuseCause) {
        this.refuseCause = refuseCause;
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

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getApproveUserCode() {
		return approveUserCode;
	}

	public void setApproveUserCode(String approveUserCode) {
		this.approveUserCode = approveUserCode;
	}
}