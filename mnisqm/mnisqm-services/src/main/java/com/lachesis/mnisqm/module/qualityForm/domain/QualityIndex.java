package com.lachesis.mnisqm.module.qualityForm.domain;

public class QualityIndex {
	
	private Long seqId;
	
	private String deptCode;//科室
	
	private String numeratorId;//分子id
	
	private String denominatorId;//分母id
	
	private Integer numeratorVal;//分子值
	
	private Integer denominatorVal;//分母值
	
	private Float indexVal;//指标值
	
	private String bigType;//大类名
	
	private String target;//改善目标
	
	private String limitVal;//超限值
	
	private String isUse;//是否使用
	
	private Integer orderVal;//排序值
	
	private String status;
	
	private String createPerson;
	
	private String updatePerson;
	
	private String indexName;//指标名称

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

	public String getNumeratorId() {
		return numeratorId;
	}

	public void setNumeratorId(String numeratorId) {
		this.numeratorId = numeratorId;
	}

	public String getDenominatorId() {
		return denominatorId;
	}

	public void setDenominatorId(String denominatorId) {
		this.denominatorId = denominatorId;
	}

	public Integer getNumeratorVal() {
		return numeratorVal;
	}

	public void setNumeratorVal(Integer numeratorVal) {
		this.numeratorVal = numeratorVal;
	}

	public Integer getDenominatorVal() {
		return denominatorVal;
	}

	public void setDenominatorVal(Integer denominatorVal) {
		this.denominatorVal = denominatorVal;
	}

	public Float getIndexVal() {
		return indexVal;
	}

	public void setIndexVal(Float indexVal) {
		this.indexVal = indexVal;
	}

	public String getBigType() {
		return bigType;
	}

	public void setBigType(String bigType) {
		this.bigType = bigType;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getLimitVal() {
		return limitVal;
	}

	public void setLimitVal(String limitVal) {
		this.limitVal = limitVal;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public Integer getOrderVal() {
		return orderVal;
	}

	public void setOrderVal(Integer orderVal) {
		this.orderVal = orderVal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	
}
