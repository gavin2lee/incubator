package com.harmazing.openbridge.project.vo;

import java.util.Date;

import com.harmazing.framework.common.model.BaseModel;

public class ProjectJiraRelation extends BaseModel{
	//
	private static final long serialVersionUID = 1L;
	//app 关联
	public static int SOURCE_API_MANAGER = 1;
	//api 关联
	public static int SOURCE_APP_FACTORY = 2;
	private String id;
	//项目ID
	private String projectId;
	//jira项目key
	private String jiraProjectKey;
	
	//关联来自. 1：APPFactory    2：APIManager
	private int source;
	
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
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getJiraProjectKey() {
		return jiraProjectKey;
	}

	public void setJiraProjectKey(String jiraProjectKey) {
		this.jiraProjectKey = jiraProjectKey;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
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

	@Override
	public String toString() {
		return "ProjectJiraRelation [id=" + id + ", projectId=" + projectId
				+ ", jiraProjectKey=" + jiraProjectKey + ", source=" + source
				+ ", creator=" + creator + ", creationTime=" + creationTime
				+ "]";
	}
	
}
