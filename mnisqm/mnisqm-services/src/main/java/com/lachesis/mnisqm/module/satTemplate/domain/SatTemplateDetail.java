package com.lachesis.mnisqm.module.satTemplate.domain;

import java.util.Date;
import java.util.List;

public class SatTemplateDetail {
    private Long seqId;

    private String detailCode;

    private String formCode;

    private String content;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;
    
    private List<SatOption> satOptionList;

    public List<SatOption> getSatOptionList() {
		return satOptionList;
	}

	public void setSatOptionList(List<SatOption> satOptionList) {
		this.satOptionList = satOptionList;
	}

	public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}