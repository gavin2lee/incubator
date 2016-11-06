package com.harmazing.openbridge.alarm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.harmazing.framework.common.model.BaseModel;
import com.harmazing.openbridge.sys.user.model.SysUser;

/**
 * Created by dengteng on 2016/8/01.
 */
public class Team extends BaseModel {
	private int id;
	private String name;
	private int creator;
	private String resume;
	private String creatorUser;
	private String creatorUserName;
	private Date created;
	private List<SysUser> member = new ArrayList<SysUser>();
	private String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatorUser() {
		return creatorUser;
	}

	public void setCreatorUser(String creatorUser) {
		this.creatorUser = creatorUser;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public List<SysUser> getMember() {
		return member;
	}

	public void setMember(List<SysUser> member) {
		this.member = member;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
