package com.lachesis.mnisqm.module.event.domain;

import java.util.Date;

public class EventRequirement {
    private Long seqId;

    private String reportCode;

    private String reqCode;

    private Date reqTime;

    private String userCode;
    
    private String userName;

    private String status;

    private String reqTypeCode;

    private String reqTypeName;

    private String reqContent;

    private String remark;

    private String createPerson;

    private String updatePerson;

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

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReqTypeCode() {
        return reqTypeCode;
    }

    public void setReqTypeCode(String reqTypeCode) {
        this.reqTypeCode = reqTypeCode;
    }

    public String getReqTypeName() {
        return reqTypeName;
    }

    public void setReqTypeName(String reqTypeName) {
        this.reqTypeName = reqTypeName;
    }

    public String getReqContent() {
        return reqContent;
    }

    public void setReqContent(String reqContent) {
        this.reqContent = reqContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
}