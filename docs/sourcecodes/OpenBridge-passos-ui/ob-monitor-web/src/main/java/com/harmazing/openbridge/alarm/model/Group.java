package com.harmazing.openbridge.alarm.model;

import com.harmazing.framework.common.model.BaseModel;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/7/28 16:31.
 */
public class Group extends BaseModel {
    private long id;
    private String grpName;
    private String createUser;
    private String createAt;
    /* 机器分组信息 come_from 0: 从机器管理同步过来的；1: 从页面创建的*/
    private String comeFrom;
    private String grpType;
    public Group(){};
    
    public Group(long id, String grpName, String createUser,String comeFrom,String grpType) {
    	this.id = id;
        this.grpName = grpName;
        this.createUser = createUser;
        this.comeFrom = comeFrom;
        this.grpType=grpType;
    }

    public Group(String grpName, String createUser,String comeFrom,String grpType) {
        this.grpName = grpName;
        this.createUser = createUser;
        this.comeFrom = comeFrom;
        this.grpType=grpType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }
    public String GetGrpType() {
        return grpType;
    }

    public void setGrpType(String grpType) {
        this.grpType = grpType;
    }
}
