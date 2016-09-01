package com.lachesis.mnisqm.module.patientManage.domain;

import java.math.BigDecimal;
import java.util.Date;

public class BedInfo {
    private Long id;

    private String code;

    private String wardCode;  //病区代码
    
    private String deptCode;   //科室代码

    private String bedType;

    private String bedTypeName;

    private BigDecimal bedPrice;

    private String tags;

    private String flag;

    private Date syncCreate;

    private Date syncUpdate;

    private String hisCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getBedTypeName() {
        return bedTypeName;
    }

    public void setBedTypeName(String bedTypeName) {
        this.bedTypeName = bedTypeName;
    }

    public BigDecimal getBedPrice() {
        return bedPrice;
    }

    public void setBedPrice(BigDecimal bedPrice) {
        this.bedPrice = bedPrice;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Date getSyncCreate() {
        return syncCreate;
    }

    public void setSyncCreate(Date syncCreate) {
        this.syncCreate = syncCreate;
    }

    public Date getSyncUpdate() {
        return syncUpdate;
    }

    public void setSyncUpdate(Date syncUpdate) {
        this.syncUpdate = syncUpdate;
    }

    public String getHisCode() {
        return hisCode;
    }

    public void setHisCode(String hisCode) {
        this.hisCode = hisCode;
    }
}