package com.lachesis.mnisqm.module.user.domain;

public class SeniorSearch {
    private String id;

    private String nurseName;

    private String deptName;

    private Integer nurseNum;

    private Integer patNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getNurseNum() {
        return nurseNum;
    }

    public void setNurseNum(Integer nurseNum) {
        this.nurseNum = nurseNum;
    }

    public Integer getPatNum() {
        return patNum;
    }

    public void setPatNum(Integer patNum) {
        this.patNum = patNum;
    }
}