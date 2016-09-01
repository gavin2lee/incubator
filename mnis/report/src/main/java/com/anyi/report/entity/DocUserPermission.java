package com.anyi.report.entity;

/**
 * Copyright (c) 2016, Lachesis-mh.com
 * All rights reserved.
 * <p/>
 * Discription:文书的用户权限控制信息实体
 * <p/>
 * Created by junming.ren
 * on 2016/7/21.
 */
public class DocUserPermission {
    private String seqID;
    private String userCode; //用户编号
    private String deptCode; //科室编号
    private String patID;    //患者住院号
    private String templateID; //模板ID
    private String startTime; //权限起始时间
    private String endTime; //权限截止时间
    private String valid; //是否有效
    private String createPerson; //授权人编号
    private String createTime; //授权时间

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getPatID() {
        return patID;
    }

    public void setPatID(String patID) {
        this.patID = patID;
    }

    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
