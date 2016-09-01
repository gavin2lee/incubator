package com.lachesis.mnis.core.order.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class HttpResponeMsg implements Serializable{
	private static final long serialVersionUID = -8448782546547543833L;

	@SerializedName("data")
	private Object data;
	
	private String rslt;
	private String msg;
	private String msgType;
	
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRslt() {
		return rslt;
	}

	public void setRslt(String rslt) {
		this.rslt = rslt;
	}

	public String getMsg() {
		return msg;
	}
}
