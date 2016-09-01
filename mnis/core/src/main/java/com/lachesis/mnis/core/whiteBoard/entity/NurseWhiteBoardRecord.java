package com.lachesis.mnis.core.whiteBoard.entity;

import java.util.List;
/**
 * 小白板记录信息(小白板项和该项信息集合)
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardRecord {
	/**
	 * 部门Code
	 */
	private String deptCode;
	/**
	 * 记录Code
	 */
	private String recordCode;
	/**
	 * 记录name
	 */
	private String recordName;
	/**
	 * 小白板记录信息集合
	 */
	/**
	 * 是否可以执行
	 */
	private boolean isExec;
	private List<NurseWhiteBoardRecordFreqInfo> nurseWhiteBoardRecordFreqInfos;

	public String getRecordCode() {
		return recordCode;
	}

	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	public boolean isExec() {
		return isExec;
	}

	public void setExec(boolean isExec) {
		this.isExec = isExec;
	}

	public List<NurseWhiteBoardRecordFreqInfo> getNurseWhiteBoardRecordFreqInfos() {
		return nurseWhiteBoardRecordFreqInfos;
	}

	public void setNurseWhiteBoardRecordFreqInfos(
			List<NurseWhiteBoardRecordFreqInfo> nurseWhiteBoardRecordFreqInfos) {
		this.nurseWhiteBoardRecordFreqInfos = nurseWhiteBoardRecordFreqInfos;
	}


}
