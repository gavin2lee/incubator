package com.lachesis.mnisqm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lachesis.mnisqm.constants.Constants;

public class BaseVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty(Constants.ResultCode)
	private String code=Constants.Success;
	@JsonProperty(Constants.ResultMsg)
	private String msg=Constants.SuccessMsg;

	public BaseVo() {
		super();
	}

	public BaseVo(String ackResult, String msg) {
		super();
		this.code = ackResult;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String toJsonString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"code\":").append(code).append(",\"msg\":\"").append(msg).append("\"}");
		return buffer.toString();
	}
}
