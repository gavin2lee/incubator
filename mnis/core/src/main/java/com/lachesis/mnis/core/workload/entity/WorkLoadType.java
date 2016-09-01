package com.lachesis.mnis.core.workload.entity;

import java.util.ArrayList;
import java.util.List;
/**
 * 工作量统计类型
 * @author ThinkPad
 *
 */
public class WorkLoadType {
	private int id;
	private String parentType;
	private String type;
	private String name;
	private List<WorkLoadType> children = new ArrayList<WorkLoadType>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<WorkLoadType> getChildren() {
		return children;
	}
	public void setChildren(List<WorkLoadType> children) {
		this.children = children;
	}
}
