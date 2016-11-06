package com.harmazing.openbridge.alarm.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.harmazing.openbridge.alarm.model.Group;

public class GroupManageDTO extends ParentDTO {

	private int id;
	private String createUser;
	private String groupName;

	private List<Long> hostIds = new ArrayList<>();

	private List<Long> tplIds = new ArrayList<>();

	public Group getGroup(){
		return new Group(groupName,createUser,"1","0");
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Long> getHostIds() {
		return hostIds;
	}

	public void setHostIds(List<Long> hostIds) {
		this.hostIds = hostIds;
	}

	public List<Long> getTplIds() {
		return tplIds;
	}

	public void setTplIds(List<Long> tplIds) {
		this.tplIds = tplIds;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

}
