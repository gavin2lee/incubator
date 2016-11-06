package com.harmazing.openbridge.alarm.model.vo;

import com.harmazing.openbridge.alarm.model.Action;
import com.harmazing.openbridge.alarm.model.Strategy;

import java.util.List;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/4 14:53.
 */
public class TemplateEditDTO {
    private long id;
    private String tplName;
    private long parentId;
    private long actionId;
    private Action action;
    private String createUser;
    private String createAt;
    private List<Strategy> strategies;
  
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
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

    public List<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
