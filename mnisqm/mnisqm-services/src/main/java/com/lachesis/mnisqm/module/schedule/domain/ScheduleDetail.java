package com.lachesis.mnisqm.module.schedule.domain;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDetail {
	private String userCode;
	private String hisCode;
	private String userName;
	private String color;
	private String groupName;
	private String clinical;
	private String leave;
	private String beds;
	private String count3;
	private String remark;
	private List<ScheduleRecordDetail> week1 = new ArrayList<ScheduleRecordDetail>();
	private List<ScheduleRecordDetail> week2 = new ArrayList<ScheduleRecordDetail>();
	private List<ScheduleRecordDetail> week3 = new ArrayList<ScheduleRecordDetail>();
	private List<ScheduleRecordDetail> week4 = new ArrayList<ScheduleRecordDetail>();
	private List<ScheduleRecordDetail> week5 = new ArrayList<ScheduleRecordDetail>();
	private List<ScheduleRecordDetail> week6 = new ArrayList<ScheduleRecordDetail>();
	private List<ScheduleRecordDetail> week7 = new ArrayList<ScheduleRecordDetail>();
	
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
	public List<ScheduleRecordDetail> getWeek1() {
		return week1;
	}
	public void setWeek1(List<ScheduleRecordDetail> week1) {
		this.week1 = week1;
	}
	public List<ScheduleRecordDetail> getWeek2() {
		return week2;
	}
	public void setWeek2(List<ScheduleRecordDetail> week2) {
		this.week2 = week2;
	}
	public List<ScheduleRecordDetail> getWeek3() {
		return week3;
	}
	public void setWeek3(List<ScheduleRecordDetail> week3) {
		this.week3 = week3;
	}
	public List<ScheduleRecordDetail> getWeek4() {
		return week4;
	}
	public void setWeek4(List<ScheduleRecordDetail> week4) {
		this.week4 = week4;
	}
	public List<ScheduleRecordDetail> getWeek5() {
		return week5;
	}
	public void setWeek5(List<ScheduleRecordDetail> week5) {
		this.week5 = week5;
	}
	public List<ScheduleRecordDetail> getWeek6() {
		return week6;
	}
	public void setWeek6(List<ScheduleRecordDetail> week6) {
		this.week6 = week6;
	}
	public List<ScheduleRecordDetail> getWeek7() {
		return week7;
	}
	public void setWeek7(List<ScheduleRecordDetail> week7) {
		this.week7 = week7;
	}
	public String getBeds() {
		return beds;
	}
	public void setBeds(String beds) {
		this.beds = beds;
	}
	public String getCount3() {
		return count3;
	}
	public void setCount3(String count3) {
		this.count3 = count3;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getClinical() {
		return clinical;
	}
	public void setClinical(String clinical) {
		this.clinical = clinical;
	}
	public String getLeave() {
		return leave;
	}
	public void setLeave(String leave) {
		this.leave = leave;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
