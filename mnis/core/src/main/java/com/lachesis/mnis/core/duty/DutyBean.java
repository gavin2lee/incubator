package com.lachesis.mnis.core.duty;

import java.io.Serializable;

public class DutyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String deptId;
	private String userId;
	private String userName;
	private String tel;

	/**
	 * h_nd:护理部电话 h_op:手术室电话 h_ad:行政值班 h_pt:保卫科电话 d_dc:值班医生 d_nr:值班护士 d_nL:值班护士长
	 */
	private String type;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
