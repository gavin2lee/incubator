package com.anyi.report.entity;

/**
 * Copyright (c) 2016, Lachesis-mh.com
 * All rights reserved.
 * <p/>
 * Discription:模块操作行为记录表
 * <p/>
 * Created by junming.ren
 * on 2016/6/23.
 */
public class ActionRecord {
    private String seqID;
    private String moduleName;
    private String interfaceName;
    private String paramStr;
    private String createTime;
    private String createPerson;

    public String getSeqID() {
        return seqID;
    }

    public void setSeqID(String seqID) {
        this.seqID = seqID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getParamStr() {
        return paramStr;
    }

    public void setParamStr(String paramStr) {
        this.paramStr = paramStr;
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
