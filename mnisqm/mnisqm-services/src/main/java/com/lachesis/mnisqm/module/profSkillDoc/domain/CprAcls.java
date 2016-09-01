package com.lachesis.mnisqm.module.profSkillDoc.domain;

import java.util.Date;

public class CprAcls {

	private Long seqId;

	private String userCode;

	private String userName;

	private String trainTime;

	private String trainName;

	private String assessmentContent;

	private String certificateQualification;

	private String status;
	
	private String auditor;

	private String remark;

	private Date createTime;

	private String createPerson;

	private Date updateTime;

	private String updatePerson;

	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
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

	public String getTrainTime() {
		return trainTime;
	}

	public void setTrainTime(String trainTime) {
		this.trainTime = trainTime;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getAssessmentContent() {
		return assessmentContent;
	}

	public void setAssessmentContent(String assessmentContent) {
		this.assessmentContent = assessmentContent;
	}

	public String getCertificateQualification() {
		return certificateQualification;
	}

	public void setCertificateQualification(String certificateQualification) {
		this.certificateQualification = certificateQualification;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
