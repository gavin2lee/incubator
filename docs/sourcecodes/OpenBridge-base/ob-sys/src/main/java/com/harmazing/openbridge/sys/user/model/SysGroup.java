package com.harmazing.openbridge.sys.user.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.model.IBaseModel;

@SuppressWarnings("serial")
public class SysGroup implements IBaseModel {
	private String groupId;
	private String groupName;
	private Date createTime;
	private String createrId;
	private String createrName;
	private List<Map<String, Object>> users;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getGroupId() {
		return this.groupId;
	}
	


	public void setGroupId(String groupId) {
		this.groupId=groupId;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName=groupName;
	}
	public List<Map<String, Object>> getUsers() {
		return users;
	}

	public void setUsers(List<Map<String, Object>> users) {
		this.users = users;
	}
}
