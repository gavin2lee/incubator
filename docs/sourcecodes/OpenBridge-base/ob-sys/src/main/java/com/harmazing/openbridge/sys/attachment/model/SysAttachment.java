package com.harmazing.openbridge.sys.attachment.model;

import java.util.Date;

import com.harmazing.framework.common.model.IBaseModel;

@SuppressWarnings("serial")
public class SysAttachment implements IBaseModel, IAttachment {
	private String attId;
	private String attName;
	private String filePath;
	private int attSize;
	private Date createTime;
	private String businessId;
	private String businessKey;
	private String businessType;

	public String getAttId() {
		return attId;
	}

	public void setAttId(String attId) {
		this.attId = attId;
	}

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public int getAttSize() {
		return attSize;
	}

	public void setAttSize(int attSize) {
		this.attSize = attSize;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
