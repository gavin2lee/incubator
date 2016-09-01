package com.lachesis.mnisqm.module.user.domain;

import java.util.Date;

public class ComDept {
    private Long seqId;

    private String deptCode;

    private String deptName;

    private String status;

    private String fatherDept;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;
    
    private String deptNurseHeader;//科户长

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFatherDept() {
        return fatherDept;
    }

    public void setFatherDept(String fatherDept) {
        this.fatherDept = fatherDept;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

	public String getDeptNurseHeader() {
		return deptNurseHeader;
	}

	public void setDeptNurseHeader(String deptNurseHeader) {
		this.deptNurseHeader = deptNurseHeader;
	}
    
    
}