package com.lachesis.mnis.web.task.notify.json;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.util.GsonUtils;

public class MsgJson {
	//消息个数
	private int count;
	private String deptCode;
	//消息时间
	private Date msgDate;
	//消息患者体
	private List<BaseJson> baseJsons;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public List<BaseJson> getBaseJsons() {
		return baseJsons;
	}
	public void setBaseJsons(List<BaseJson> baseJsons) {
		this.baseJsons = baseJsons;
	}
	public Date getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(Date msgDate) {
		this.msgDate = msgDate;
	}
	
	public String toJson(){
		return GsonUtils.toJson(this);
	}
}
