package com.lachesis.mnisqm.module.qualityForm.domain;

import java.util.List;

public class QualityPlan {
	
	private Long seqId;
	
	private String teamName;
	
	private String startDate;
	
	private String endDate;
	
	private String modelName;
	
	private String taskNum;
	
	private String status;
	
	private String createPerson;
	
	private String updatePerson;
	
	private List<QualityTask> qualityTaskList;
	
	private Long modelId;//模板id
	
	private String teamCode;//质控小组编号

	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
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

	public List<QualityTask> getQualityTaskList() {
		return qualityTaskList;
	}

	public void setQualityTaskList(List<QualityTask> qualityTaskList) {
		this.qualityTaskList = qualityTaskList;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

}
