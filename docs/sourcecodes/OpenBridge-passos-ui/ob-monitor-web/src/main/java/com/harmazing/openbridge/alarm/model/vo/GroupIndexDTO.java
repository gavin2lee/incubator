package com.harmazing.openbridge.alarm.model.vo;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/1 17:48.
 */
public class GroupIndexDTO extends ParentDTO {
    private long id;
    private String grpName;
    private String createUser;
    private String createAt;
    /* 机器分组信息 come_from 0: 从机器管理同步过来的；1: 从页面创建的*/
    private String comeFrom;
    private int countTemplates;//策略数
    private int countHosts;//节点数
    private int countUsers;//用户数
    private String grpType;

    public GroupIndexDTO() {
    }

    public GroupIndexDTO(long id, String grpName, String createUser, String createAt, String comeFrom, int countTemplates, int countHosts) {
        this.id = id;
        this.grpName = grpName;
        this.createUser = createUser;
        this.createAt = createAt;
        this.comeFrom = comeFrom;
        this.countTemplates = countTemplates;
        this.countHosts = countHosts;
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

    public int getCountTemplates() {
        return countTemplates;
    }

    public void setCountTemplates(int countTemplates) {
        this.countTemplates = countTemplates;
    }

    public int getCountHosts() {
        return countHosts;
    }

    public void setCountHosts(int countHosts) {
        this.countHosts = countHosts;
    }
    
    public int getCountUsers() {
        return countUsers;
    }

    public void setCountUsers(int countUsers) {
        this.countUsers = countUsers;
    }
    
    public String getGrpType() {
        return grpType;
    }

    public void setGrpType(String grpType) {
        this.grpType = grpType;
    }
}
