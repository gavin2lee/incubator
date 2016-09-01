package com.lachesis.mnisqm.module.event.domain;

import java.util.Date;

public class EventMeasures {
    private Long seqId;

    private String reportCode;

    private String meaCode;

    private Date meaTime;

    private String userCode;//措施提出人员工编号
    
    private String userName;//措施提出人员工姓名

    private String status;

    private String meaTypeCode;

    private String meaTypeName;

    private String meaContent;

    private String headUserCode;

    private String resStatus;

    private Integer resPlan;

    private String resDescribe;

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

    public String getMeaCode() {
        return meaCode;
    }

    public void setMeaCode(String meaCode) {
        this.meaCode = meaCode;
    }

    public Date getMeaTime() {
        return meaTime;
    }

    public void setMeaTime(Date meaTime) {
        this.meaTime = meaTime;
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

    public String getMeaTypeCode() {
        return meaTypeCode;
    }

    public void setMeaTypeCode(String meaTypeCode) {
        this.meaTypeCode = meaTypeCode;
    }

    public String getMeaTypeName() {
        return meaTypeName;
    }

    public void setMeaTypeName(String meaTypeName) {
        this.meaTypeName = meaTypeName;
    }

    public String getMeaContent() {
        return meaContent;
    }

    public void setMeaContent(String meaContent) {
        this.meaContent = meaContent;
    }

    public String getHeadUserCode() {
        return headUserCode;
    }

    public void setHeadUserCode(String headUserCode) {
        this.headUserCode = headUserCode;
    }

    public String getResStatus() {
        return resStatus;
    }

    public void setResStatus(String resStatus) {
        this.resStatus = resStatus;
    }

    public Integer getResPlan() {
        return resPlan;
    }

    public void setResPlan(Integer resPlan) {
        this.resPlan = resPlan;
    }

    public String getResDescribe() {
        return resDescribe;
    }

    public void setResDescribe(String resDescribe) {
        this.resDescribe = resDescribe;
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