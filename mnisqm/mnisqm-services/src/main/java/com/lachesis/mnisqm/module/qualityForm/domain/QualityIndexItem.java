package com.lachesis.mnisqm.module.qualityForm.domain;

public class QualityIndexItem {
	
	private Long seqId;
	
	private String indexItemName;//分子/分母指标名称
	
	private String indexItemType;//01：分子；02：分母
	
	private String status;
	
	private String createPerson;
	
	private String updatePerson;

	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getIndexItemName() {
		return indexItemName;
	}

	public void setIndexItemName(String indexItemName) {
		this.indexItemName = indexItemName;
	}

	public String getIndexItemType() {
		return indexItemType;
	}

	public void setIndexItemType(String indexItemType) {
		this.indexItemType = indexItemType;
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

}
