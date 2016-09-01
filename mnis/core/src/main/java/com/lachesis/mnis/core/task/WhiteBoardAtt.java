package com.lachesis.mnis.core.task;

import java.io.Serializable;

public class WhiteBoardAtt implements Serializable {

	private static final long serialVersionUID = 1L;
	private String attId;
	private String recordId;
	private String sendType;
	private String data;
	private byte[] byteData;

	public String getAttId() {
		return attId;
	}

	public void setAttId(String attId) {
		this.attId = attId;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

}
