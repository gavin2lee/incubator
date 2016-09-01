package com.lachesis.mnisqm.module.qualityForm.domain;

import java.util.Date;
import java.util.List;

public class QualityModel {

	private Long seqId;
	
	private String modelName;//模板名称
	
	private String isUsed;//是否启用
	
	private Integer totalScore;//项目累加总分
	
	private String status;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String createPerson;
	
	private String updatePerson;
	
	private List<QualityForm> qualityFormList;//类型
	
	private List<QualityFormDetail> qualityFormDetailList;//项目

	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
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

	public List<QualityForm> getQualityFormList() {
		return qualityFormList;
	}

	public void setQualityFormList(List<QualityForm> qualityFormList) {
		this.qualityFormList = qualityFormList;
	}

	public List<QualityFormDetail> getQualityFormDetailList() {
		return qualityFormDetailList;
	}

	public void setQualityFormDetailList(
			List<QualityFormDetail> qualityFormDetailList) {
		this.qualityFormDetailList = qualityFormDetailList;
	}
	
	
	
}
