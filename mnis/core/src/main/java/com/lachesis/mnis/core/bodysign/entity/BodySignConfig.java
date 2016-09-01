package com.lachesis.mnis.core.bodysign.entity;

public class BodySignConfig {
	private String cfgNo;//配置项目编号
	private String cfgType;//配置项目类型
	private String cfgName;//类型名称
	private String fatherCfgNo;//父节点编号
	private String value;//值
	private String reserved1;//缺省字段1
	private String reserved2;//缺省字段2
	private String reserved3;//缺省字段3
	private String reserved4;//缺省字段4
	private String reserved5;//缺省字段5
	private String reserved6;//缺省字段6
	private String status;//状态  0：无效记录，不使用；1：正确记录
	public String getCfgNo() {
		return cfgNo;
	}
	public void setCfgNo(String cfgNo) {
		this.cfgNo = cfgNo;
	}
	public String getCfgType() {
		return cfgType;
	}
	public void setCfgType(String cfgType) {
		this.cfgType = cfgType;
	}
	public String getCfgName() {
		return cfgName;
	}
	public void setCfgName(String cfgName) {
		this.cfgName = cfgName;
	}
	public String getFatherCfgNo() {
		return fatherCfgNo;
	}
	public void setFatherCfgNo(String fatherCfgNo) {
		this.fatherCfgNo = fatherCfgNo;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReserved1() {
		return reserved1;
	}
	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}
	public String getReserved2() {
		return reserved2;
	}
	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}
	public String getReserved3() {
		return reserved3;
	}
	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}
	public String getReserved4() {
		return reserved4;
	}
	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}
	public String getReserved5() {
		return reserved5;
	}
	public void setReserved5(String reserved5) {
		this.reserved5 = reserved5;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReserved6() {
		return reserved6;
	}
	public void setReserved6(String reserved6) {
		this.reserved6 = reserved6;
	}
}
