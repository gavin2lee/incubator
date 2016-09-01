package com.lachesis.mnisqm.module.schedule.domain;

import java.util.ArrayList;
import java.util.List;

public class ScheduleCount {
	private String userCode;//员工编号
	private String hisCode;//HIS员工编号
	private String userName;//员工名称
	private String deptCode;//部门编号
	private String deptName;//部门名称
	private String shj;//事假
	private String bj;//病假
	private String nj;//年假
	private String txj;//调休假
	private String cj;//产假
	private String sj;//丧假
	private String hj;//婚假
	private String month;//月份
	private String yb;//夜班
	private String jjr;//节假日
	private String classCode;
	private List<ScheduleDeptClass> classList = new ArrayList<ScheduleDeptClass>();
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getHisCode() {
		return hisCode;
	}
	public void setHisCode(String hisCode) {
		this.hisCode = hisCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	
	public String getBj() {
		return bj;
	}
	public void setBj(String bj) {
		this.bj = bj;
	}
	
	public String getTxj() {
		return txj;
	}
	public void setTxj(String txj) {
		this.txj = txj;
	}
	public String getCj() {
		return cj;
	}
	public void setCj(String cj) {
		this.cj = cj;
	}
	public String getYb() {
		return yb;
	}
	public void setYb(String yb) {
		this.yb = yb;
	}
	public String getJjr() {
		return jjr;
	}
	public void setJjr(String jjr) {
		this.jjr = jjr;
	}
	public String getShj() {
		return shj;
	}
	public void setShj(String shj) {
		this.shj = shj;
	}
	public String getNj() {
		return nj;
	}
	public void setNj(String nj) {
		this.nj = nj;
	}
	public String getSj() {
		return sj;
	}
	public void setSj(String sj) {
		this.sj = sj;
	}
	
	public List<ScheduleDeptClass> getClassList() {
		return classList;
	}
	public void setClassList(List<ScheduleDeptClass> classList) {
		this.classList = classList;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getHj() {
		return hj;
	}
	public void setHj(String hj) {
		this.hj = hj;
	}
}