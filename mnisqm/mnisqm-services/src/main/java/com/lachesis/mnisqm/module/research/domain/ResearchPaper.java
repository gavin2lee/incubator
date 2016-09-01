package com.lachesis.mnisqm.module.research.domain;


public class ResearchPaper {
    private Long seqId;

    private String deptCode;

    private String paperName;

    private String author;

    private String publictionName;

    private String publictionNo;

    private Integer wordsNumber;

    private String corePubliction;

    private String publishDate;

    private String status;

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

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublictionName() {
        return publictionName;
    }

    public void setPublictionName(String publictionName) {
        this.publictionName = publictionName;
    }

    public String getPublictionNo() {
        return publictionNo;
    }

    public void setPublictionNo(String publictionNo) {
        this.publictionNo = publictionNo;
    }

    public Integer getWordsNumber() {
        return wordsNumber;
    }

    public void setWordsNumber(Integer wordsNumber) {
        this.wordsNumber = wordsNumber;
    }

    public String getCorePubliction() {
        return corePubliction;
    }

    public void setCorePubliction(String corePubliction) {
        this.corePubliction = corePubliction;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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