package com.lachesis.mnisqm.module.research.domain;

import java.math.BigDecimal;

public class ResearchItem {
    private Long seqId;

    private String deptCode;

    private String subjectName;

    private String itemSource;

    private String startDate;

    private String endDate;

    private BigDecimal cost;

    private String status;

    private String subjectHead;

    private String subjectParticipant;

    private String createPerson;

    private String updatePerson;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getItemSource() {
        return itemSource;
    }

    public void setItemSource(String itemSource) {
        this.itemSource = itemSource;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubjectHead() {
        return subjectHead;
    }

    public void setSubjectHead(String subjectHead) {
        this.subjectHead = subjectHead;
    }

    public String getSubjectParticipant() {
        return subjectParticipant;
    }

    public void setSubjectParticipant(String subjectParticipant) {
        this.subjectParticipant = subjectParticipant;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }
}