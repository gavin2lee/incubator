package com.lachesis.mnisqm.module.trainExamManage.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TemExamManage {
    private Long seqId;

    private String examCode;

    private String examName;

    private Date examTime;

    private String timeSpan;

    private String place;

    private BigDecimal grossScore;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;
    
    private List<TemPerformanceManage> temPerformanceManageList;

    public List<TemPerformanceManage> getTemPerformanceManageList() {
		return temPerformanceManageList;
	}

	public void setTemPerformanceManageList(
			List<TemPerformanceManage> temPerformanceManageList) {
		this.temPerformanceManageList = temPerformanceManageList;
	}

	public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }

    public String getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(String timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public BigDecimal getGrossScore() {
        return grossScore;
    }

    public void setGrossScore(BigDecimal grossScore) {
        this.grossScore = grossScore;
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