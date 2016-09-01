package com.lachesis.mnis.core.identity.entity;


public class FPDataBean {
	  private String userCode;
	  private String deptCode;
	  private String fpFeature; // required
	  private String secretKey; // required
	  private int fpDataId; // required
	  private String modifyType; // required

	public FPDataBean(){
		
	}
	public FPDataBean(UserFinger fpEntity) {
		setFpDataId(fpEntity.getId());
		setUserCode(fpEntity.getUserCode());
		setDeptCode(fpEntity.getDeptCode());
		setSecretKey(fpEntity.getSecretKey());
		setFpFeature(fpEntity.getFeature());
		setModifyType(fpEntity.getModifyType());
	}

	public String getFpFeature() {
		return fpFeature;
	}
	public void setFpFeature(String fpFeature) {
		this.fpFeature = fpFeature;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public int getFpDataId() {
		return fpDataId;
	}
	public void setFpDataId(int fpDataId) {
		this.fpDataId = fpDataId;
	}
	public String getModifyType() {
		return modifyType;
	}
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
}
