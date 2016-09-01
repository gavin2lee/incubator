package com.anyi.report.entity;

/**
 * Copyright (c) 2016, Lachesis-mh.com
 * All rights reserved.
 * <p/>
 * Discription:转抄失败的文书记录信息
 * <p/>
 * Created by junming.ren
 * on 2016/5/25.
 */
public class DocRecordFailInfo {
    private String createTime;
    private String patID;
    private String deptCode;
    private String createPerson;
    private String dateList;
    private String timeList;
    private String barcode;
    private String reason;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPatID() {
        return patID;
    }

    public void setPatID(String patID) {
        this.patID = patID;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getDateList() {
        return dateList;
    }

    public void setDateList(String dateList) {
        this.dateList = dateList;
    }

    public String getTimeList() {
        return timeList;
    }

    public void setTimeList(String timeList) {
        this.timeList = timeList;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
