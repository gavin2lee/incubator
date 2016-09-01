package com.lachesis.mnis.core.identity.entity;

import java.util.Date;
/**
 * 登陆牌信息
 * @author ThinkPad
 *
 */
public class LoginCardInfo {
	private int id;
	/**
	 * 登陆唯一吗
	 */
	private String loginCardCode;
	/**
	 * 待生成码护士code
	 */
	private String nurseCode;
	private String nurseName;
	private String deptCode;
	private String deptName;
	/**
	 * 登陆牌状态:0:正常,1:作废,2:过期
	 */
	private int status;
	/**
	 * 操作护士code
	 */
	private String operaNurseCode;
	private String operaNurseName;
	private Date operaDate;
	/**
	 * 有效开始时间
	 */
	private Date startDate;
	/**
	 * 有效结束时间
	 */
	private Date endDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginCardCode() {
		return loginCardCode;
	}
	public void setLoginCardCode(String loginCardCode) {
		this.loginCardCode = loginCardCode;
	}
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOperaNurseCode() {
		return operaNurseCode;
	}
	public void setOperaNurseCode(String operaNurseCode) {
		this.operaNurseCode = operaNurseCode;
	}
	public String getOperaNurseName() {
		return operaNurseName;
	}
	public void setOperaNurseName(String operaNurseName) {
		this.operaNurseName = operaNurseName;
	}
	public Date getOperaDate() {
		return operaDate;
	}
	public void setOperaDate(Date operaDate) {
		this.operaDate = operaDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
