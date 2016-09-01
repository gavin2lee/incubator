package com.lachesis.mnisqm.module.event.domain;

import java.util.Date;

public class EventAssessment {
    private Long seqId;

    private String assessCode;

    private String meaCode;

    private Date assessTime;

    private String userCode;

    private String status;

    private String assessTypeCode;

    private String assessTypeName;

    private String assessContent;

    private String assessScore;

    private String remark;

    private String createPerson;

    private String updatePerson;

    private Date createTime;

    private Date updateTime;
    
    private String meaContent;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getAssessCode() {
        return assessCode;
    }

    public void setAssessCode(String assessCode) {
        this.assessCode = assessCode;
    }

    public String getMeaCode() {
        return meaCode;
    }

    public void setMeaCode(String meaCode) {
        this.meaCode = meaCode;
    }

    public Date getAssessTime() {
        return assessTime;
    }

    public void setAssessTime(Date assessTime) {
        this.assessTime = assessTime;
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

    public String getAssessTypeCode() {
        return assessTypeCode;
    }

    public void setAssessTypeCode(String assessTypeCode) {
        this.assessTypeCode = assessTypeCode;
    }

    public String getAssessTypeName() {
        return assessTypeName;
    }

    public void setAssessTypeName(String assessTypeName) {
        this.assessTypeName = assessTypeName;
    }

    public String getAssessContent() {
        return assessContent;
    }

    public void setAssessContent(String assessContent) {
        this.assessContent = assessContent;
    }

    public String getAssessScore() {
        return assessScore;
    }

    public void setAssessScore(String assessScore) {
        this.assessScore = assessScore;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getMeaContent() {
		return meaContent;
	}

	public void setMeaContent(String meaContent) {
		this.meaContent = meaContent;
	}
    
    
}