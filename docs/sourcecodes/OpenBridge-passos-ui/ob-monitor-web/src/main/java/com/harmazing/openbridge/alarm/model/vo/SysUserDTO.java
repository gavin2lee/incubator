package com.harmazing.openbridge.alarm.model.vo;


/**
 * Created by 钟梦元 [zhongmengyuan@yihecloud.com] on 2016/8/9 16:44
 */
public class SysUserDTO extends ParentDTO {
	
private long id;
private String  userName;
private String  loginName;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getLoginName() {
	return loginName;
}
public void setLoginName(String loginName) {
	this.loginName = loginName;
}


	
}
