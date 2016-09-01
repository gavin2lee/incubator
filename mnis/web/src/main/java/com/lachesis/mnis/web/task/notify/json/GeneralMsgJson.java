package com.lachesis.mnis.web.task.notify.json;

import com.lachesis.mnis.core.util.GsonUtils;

public class GeneralMsgJson {
	public GeneralMsgJson(String type, String msg) {
		this.type = type;
		this.msg = msg;
	}
	public String getType() {
		return type;
	}
	public String getMsg() {
		return msg;
	}
	private String type;
	private String msg;
	
	public String toJson(){
		return GsonUtils.toJson(this);
	}
}
