package com.anyi.report.json;

import java.io.Serializable;

public class BaseVo   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7247823509891712543L;
	private String rslt;
	private String msg;
	
	
	

	public BaseVo() {
		super();
	}

	public BaseVo(String ackResult, String msg) {
		super();
		this.rslt = ackResult;
		this.msg = msg;
	}

	
	/**
	 * @return Returns the ackResult.
	 */
	
	public String getRslt() {
		return rslt;
	}

	
	/**
	 * @param ackResult The ackResult to set.
	 */
	
	public void setRslt(String result) {
		this.rslt = result;
	}

	
	/**
	 * @return Returns the msg.
	 */
	
	public String getMsg() {
		/*if(StringUtils.isEmpty(msg) && !ResultCst.SUCCESS.equals(rslt)){
			msg = PropertiesUtils.readPropertiesValue(rslt);
		}*/
		return msg;
	}

	
	/**
	 * @param msg The msg to set.
	 */
	
	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
