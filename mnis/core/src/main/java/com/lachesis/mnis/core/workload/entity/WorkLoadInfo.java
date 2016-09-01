package com.lachesis.mnis.core.workload.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class WorkLoadInfo {
	@SerializedName("nurCod")
	private String nurseCode;
	@SerializedName("nurNam")
	private String nurseName;
	@SerializedName("deptCod")
	private String deptCode;
	@SerializedName("wlDetailInfs")
	List<WorkLoadDetailInfo> workLoadDetialInfos = new ArrayList<WorkLoadDetailInfo>();

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getNurseCode() {
		return nurseCode;
	}

	public void setNurseCode(String nurseCode) {
		this.nurseCode = nurseCode;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public List<WorkLoadDetailInfo> getWorkLoadDetialInfos() {
		return workLoadDetialInfos;
	}

	public void setWorkLoadDetialInfos(
			List<WorkLoadDetailInfo> workLoadDetialInfos) {
		this.workLoadDetialInfos = workLoadDetialInfos;
	}

}
