package com.harmazing.openbridge.alarm.model;

import com.harmazing.framework.common.model.BaseModel;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/3 17:10.
 */
public class Template extends BaseModel {
	
    private long id;
    private String tplName;
    private long parentId;
    private long actionId;
    private String createUser;
    private String createAt;
    private String type;

	public Template() {

	}
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
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
	@Override
	public String toString() {
		return "Template [id=" + id + ", tplName=" + tplName + ", parentId="
				+ parentId + ", actionId=" + actionId + ", createUser="
				+ createUser + ", createAt=" + createAt + "]";
	}

	public Template(long id, String tplName, long parentId, long actionId,
			String createUser, String createAt) {
		super();
		this.id = id;
		this.tplName = tplName;
		this.parentId = parentId;
		this.actionId = actionId;
		this.createUser = createUser;
		this.createAt = createAt;
	}
    
    
}
