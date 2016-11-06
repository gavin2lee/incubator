package com.harmazing.openbridge.sys.user.model;

import java.util.Date;
import java.util.List;

import com.harmazing.framework.common.model.IBaseModel;

@SuppressWarnings("serial")
public class SysDepart implements IBaseModel {
	private String deptId;
	private String deptName;
	private String parentId;
	private String hierarchyId;
	private Date createTime;
	private String createUser;
	private int dOrder;
	
	private int level;
	
	private List<SysDepart> subDepart;
	
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getHierarchyId() {
		return hierarchyId;
	}
	public void setHierarchyId(String hierarchyId) {
		this.hierarchyId = hierarchyId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public int getdOrder() {
		return dOrder;
	}
	public void setdOrder(int dOrder) {
		this.dOrder = dOrder;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<SysDepart> getSubDepart() {
		return subDepart;
	}
	public void setSubDepart(List<SysDepart> subDepart) {
		this.subDepart = subDepart;
	}
	
}
