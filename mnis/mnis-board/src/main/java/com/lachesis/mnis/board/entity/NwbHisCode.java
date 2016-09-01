package com.lachesis.mnis.board.entity;

import java.util.List;

/**
 * 小白板与His code映射
 * @author ThinkPad
 *
 */
public class NwbHisCode {
	private String deptCode;
	/**
	 * 模板id
	 */
	private String templateId;
	private List<NwbHisCodeItem> nwbHisCodeItems;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public List<NwbHisCodeItem> getNwbHisCodeItems() {
		return nwbHisCodeItems;
	}
	public void setNwbHisCodeItems(List<NwbHisCodeItem> nwbHisCodeItems) {
		this.nwbHisCodeItems = nwbHisCodeItems;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
