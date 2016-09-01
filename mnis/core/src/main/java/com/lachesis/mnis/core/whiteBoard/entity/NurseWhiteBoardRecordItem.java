package com.lachesis.mnis.core.whiteBoard.entity;

import java.util.List;
/**
 * 小白板项和小白板子项记录集合
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardRecordItem {
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
	
	private List<NurseWhiteBoardRecordItemInfo> nurseWhiteBoardRecordItemInfos;

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

	public List<NurseWhiteBoardRecordItemInfo> getNurseWhiteBoardRecordItemInfos() {
		return nurseWhiteBoardRecordItemInfos;
	}

	public void setNurseWhiteBoardRecordItemInfos(
			List<NurseWhiteBoardRecordItemInfo> nurseWhiteBoardRecordItemInfos) {
		this.nurseWhiteBoardRecordItemInfos = nurseWhiteBoardRecordItemInfos;
	}

}
