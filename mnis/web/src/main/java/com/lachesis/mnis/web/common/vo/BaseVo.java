package com.lachesis.mnis.web.common.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.lachesis.mnis.web.common.MessageProperties;
import com.lachesis.mnis.web.common.ResultCst;

public class BaseVo implements Serializable{
	
	public final static String VO_KEY = "list";
			
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

	public String getRslt() {
		return rslt;
	}
	
	public void setRslt(String result) {
		this.rslt = result;
	}

	public String getMsg() {
		if(StringUtils.isEmpty(msg) && !ResultCst.SUCCESS.equals(rslt)){
			msg = MessageProperties.getInstance().getProperty(rslt);
		}
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
