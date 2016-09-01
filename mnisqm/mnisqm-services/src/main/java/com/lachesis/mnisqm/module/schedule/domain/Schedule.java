package com.lachesis.mnisqm.module.schedule.domain;

import java.util.List;

public class Schedule {
	private Long seqId;
	private String recordCode;
	private String startDate;
    private String endDate;
	private String week1;
	private String week2;
	private String week3;
	private String week4;
	private String week5;
	private String week6;
	private String week7;
	private String userCode;
	private String deptCode;
	private String deptName;
	private Integer weeks;
	private List<ScheduleDetail> dtls;
	public String getRecordCode() {
		return recordCode;
	}
	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getWeek1() {
		return week1;
	}
	public void setWeek1(String week1) {
		this.week1 = week1;
	}
	public String getWeek2() {
		return week2;
	}
	public void setWeek2(String week2) {
		this.week2 = week2;
	}
	public String getWeek3() {
		return week3;
	}
	public void setWeek3(String week3) {
		this.week3 = week3;
	}
	public String getWeek4() {
		return week4;
	}
	public void setWeek4(String week4) {
		this.week4 = week4;
	}
	public String getWeek5() {
		return week5;
	}
	public void setWeek5(String week5) {
		this.week5 = week5;
	}
	public String getWeek6() {
		return week6;
	}
	public void setWeek6(String week6) {
		this.week6 = week6;
	}
	public String getWeek7() {
		return week7;
	}
	public void setWeek7(String week7) {
		this.week7 = week7;
	}
	public List<ScheduleDetail> getDtls() {
		return dtls;
	}
	public void setDtls(List<ScheduleDetail> dtls) {
		this.dtls = dtls;
	}
	public Integer getWeeks() {
		return weeks;
	}
	public void setWeeks(Integer weeks) {
		this.weeks = weeks;
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
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Long getSeqId() {
		return seqId;
	}
	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}
}
