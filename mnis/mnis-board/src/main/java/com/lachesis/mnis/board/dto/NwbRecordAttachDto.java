package com.lachesis.mnis.board.dto;

import java.util.Date;

/**
 * 记录附件信息:记录保存时传输
 * @author ThinkPad
 *
 */
public class NwbRecordAttachDto {
	private String nurseCode;
	private String nurseName;
	private Date createTime;
	private String templateId;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
