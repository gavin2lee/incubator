package com.lachesis.mnis.core.identity.entity;

import java.util.Date;
/**
 * 登陆牌管理信息
 * @author ThinkPad
 *
 */
public class LoginCardManager {
	private String nurseCode;
	private String nurseName;
	private String deptCode;
	private String deptName;
	//登陆牌唯一码
	private String loginCardCode;
	private Date operaDate;
	private boolean isPrint;
	public String getNurseCode() {
		return nurseCode;
	}
	public void setNurseCode(String nurseCode) {
		this.nurseCode = nurseCode;
	}
	public String getNurseName() {
		return nurseName;
	}
	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Date getOperaDate() {
		return operaDate;
	}
	public void setOperaDate(Date operaDate) {
		this.operaDate = operaDate;
	}
	public boolean isPrint() {
		return isPrint;
	}
	public void setPrint(boolean isPrint) {
		this.isPrint = isPrint;
	}
	public String getLoginCardCode() {
		return loginCardCode;
	}
	public void setLoginCardCode(String loginCardCode) {
		this.loginCardCode = loginCardCode;
	}
	
}
