package com.lachesis.mnis.core.patientManage.entity;

import java.util.Date;

public class PatLeaveGoout {
    private Long seqId;

    private String patId;   //患者id

    private Integer status;   //患者状态 1 请假，2 外出

    private Date occurTime;   //状态发生时间

    private Date actuallyComebackTime;   //状态实际回来时间

    private Date beginTime;    //外出请假起始时间

    private Date predictEndTime;    //预计结束时间

    private Integer isAbnormal;    //是否异常(默认正常) 1 正常，0 异常

    private String remark;    //备注
    
    private String execNurseId;  ///执行护士
    
    private String execNursePhone;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;

	public String getExecNursePhone() {
		return execNursePhone;
	}

	public void setExecNursePhone(String execNursePhone) {
		this.execNursePhone = execNursePhone;
	}

	public String getExecNurseId() {
		return execNurseId;
	}

	public void setExecNurseId(String execNurseId) {
		this.execNurseId = execNurseId;
	}

	public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    public Date getActuallyComebackTime() {
        return actuallyComebackTime;
    }

    public void setActuallyComebackTime(Date actuallyComebackTime) {
        this.actuallyComebackTime = actuallyComebackTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getPredictEndTime() {
        return predictEndTime;
    }

    public void setPredictEndTime(Date predictEndTime) {
        this.predictEndTime = predictEndTime;
    }

    public Integer getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Integer isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }
}