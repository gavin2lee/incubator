package com.lachesis.mnis.core.order.entity;

import java.util.List;
/**
 * 医嘱打印信息
 * @author ThinkPad
 *
 */
public class OrderPrintInfo {
	/**
	 * 患者id
	 */
	private String patId;
	/**
	 * 患者name
	 */
	private String patName;
	/**
	 * 床位信息
	 */
	private String bedCode;
	/**
	 * 科室编号
	 */
	private String deptCode;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 年龄
	 */
	private String age;
	/**
	 * 科室name
	 */
	private String deptName;
	/**
	 * 医嘱信息
	 */
	private List<OrderGroup> orderGroups;
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getBedCode() {
		return bedCode;
	}
	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public List<OrderGroup> getOrderGroups() {
		return orderGroups;
	}
	public void setOrderGroups(List<OrderGroup> orderGroups) {
		this.orderGroups = orderGroups;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
