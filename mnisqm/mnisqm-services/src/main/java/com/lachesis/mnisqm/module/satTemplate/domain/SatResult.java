package com.lachesis.mnisqm.module.satTemplate.domain;

import java.util.Date;
import java.util.List;

public class SatResult {
    private Long seqId;

    private String deptCode;

    private String resultCode;
    
    private String resultName;

    private Integer userType;

    private String bedCode;

    private String formType;

    private String userCode;

    private String name;

    private String inHospNo;

    private String surverDate;

    private String advice;

    private String status;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;
    
    private List<SatResultDetail> satResultDetailList;

    public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public List<SatResultDetail> getSatResultDetailList() {
		return satResultDetailList;
	}

	public void setSatResultDetailList(List<SatResultDetail> satResultDetailList) {
		this.satResultDetailList = satResultDetailList;
	}

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

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getBedCode() {
        return bedCode;
    }

    public void setBedCode(String bedCode) {
        this.bedCode = bedCode;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInHospNo() {
        return inHospNo;
    }

    public void setInHospNo(String inHospNo) {
        this.inHospNo = inHospNo;
    }

    public String getSurverDate() {
        return surverDate;
    }

    public void setSurverDate(String surverDate) {
        this.surverDate = surverDate;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
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