package com.harmazing.openbridge.project.vo;

import java.util.Date;

import com.harmazing.framework.common.model.BaseModel;

public class ProjectJiraStoryRelation extends BaseModel {
	//
	private static final long serialVersionUID = 1L;
	private String id;
	// story key
	private String storyKey;
	// jira项目与app、api关联记录
	private String jiraRelationId;
	/**
	 * 创建者
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date creationTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return storyKey;
	}

	public void setProjectId(String projectId) {
		this.storyKey = projectId;
	}

	public String getStoryKey() {
		return storyKey;
	}

	public void setStoryKey(String storyKey) {
		this.storyKey = storyKey;
	}

	public String getJiraRelationId() {
		return jiraRelationId;
	}

	public void setJiraRelationId(String jiraRelationId) {
		this.jiraRelationId = jiraRelationId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
}
