package com.lachesis.mnisqm.module.qualityForm.domain;

import java.util.Date;
import java.util.List;

public class QualityForm {
    private Long seqId;

    private String formCode;

    private String formName;

    private String formType;

    private String status;

    private Date createTime;

    private Date updateTime;

    private String createPerson;

    private String updatePerson;
    
    private List<QualityFormDetail> qualityFormDetailList;
    
    private String parentType;//父类

    public List<QualityFormDetail> getQualityFormDetailList() {
		return qualityFormDetailList;
	}

	public void setQualityFormDetailList(
			List<QualityFormDetail> qualityFormDetailList) {
		this.qualityFormDetailList = qualityFormDetailList;
	}

	public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
    
}