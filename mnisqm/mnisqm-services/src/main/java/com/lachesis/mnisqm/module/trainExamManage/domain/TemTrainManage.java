package com.lachesis.mnisqm.module.trainExamManage.domain;

import java.util.Date;
import java.util.List;

public class TemTrainManage {
    private Long seqId;

    private String trainCode;

    private String courseName;

    private String lecturer;

    private Date beginTime;

    private Date endTime;

    private String place;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;
    
    private List<TemAttendanceManage> temAttendanceManageList;

    public List<TemAttendanceManage> getTemAttendanceManageList() {
		return temAttendanceManageList;
	}

	public void setTemAttendanceManageList(
			List<TemAttendanceManage> temAttendanceManageList) {
		this.temAttendanceManageList = temAttendanceManageList;
	}

	public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }
}