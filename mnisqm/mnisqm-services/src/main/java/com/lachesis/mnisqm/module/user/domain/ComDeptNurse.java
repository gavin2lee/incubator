package com.lachesis.mnisqm.module.user.domain;

import java.math.BigDecimal;


public class ComDeptNurse {
    private Long seqId;

    private String userCode;//员工编号
    
    private String hisCode;//HIS员工编号
    
    private String userName;//员工姓名

    private String status;//状态

    private String deptCode;//科室编号

    private String groupCode;//所属组组号
    
    private String groupName;//所属组名称
    
    private String isSchedule;//是否参与排班
    
    private String color;//颜色
    
    private String clinical;
    
    private BigDecimal leave;//积假
    
    private String beds;//所管床位,以英文;分割
    
    private String createPerson;
    
    private String updatePerson;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
/*
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }
*/
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getHisCode() {
		return hisCode;
	}

	public void setHisCode(String hisCode) {
		this.hisCode = hisCode;
	}

	public String getIsSchedule() {
		return isSchedule;
	}

	public void setIsSchedule(String isSchedule) {
		this.isSchedule = isSchedule;
	}

	/*public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getNightName() {
		return nightName;
	}

	public void setNightName(String nightName) {
		this.nightName = nightName;
	}*/

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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getClinical() {
		return clinical;
	}

	public void setClinical(String clinical) {
		this.clinical = clinical;
	}

	public BigDecimal getLeave() {
		return leave;
	}

	public void setLeave(BigDecimal leave) {
		this.leave = leave;
	}

	public String getBeds() {
		return beds;
	}

	public void setBeds(String beds) {
		this.beds = beds;
	}
}