package com.anyi.report.entity;

/**
 * Copyright (c) 2016, Lachesis-mh.com
 * All rights reserved.
 * <p/>
 * Discription:用户打印信息记录
 * <p/>
 * Created by junming.ren
 * on 2016/7/13.
 */
public class PrintInfoRecord {
    private String ID;//记录ID
    private String patID;//患者住院号
    private String deptCode;//病区编号
    private String templateID;//模板ID
    private String printType;//打印类型，体温单或文书等
    private String printStart;//打印信息，起始页码（文书）/起始日期（体温单）
    private String printEnd;//打印信息，结束页码（文书）/结束日期（体温单）
    private String createTime;//创建时间（打印时间）
    private String createPerson;//创建人（打印者）

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getPrintStart() {
        return printStart;
    }

    public void setPrintStart(String printStart) {
        this.printStart = printStart;
    }

    public String getPrintEnd() {
        return printEnd;
    }

    public void setPrintEnd(String printEnd) {
        this.printEnd = printEnd;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }
}
