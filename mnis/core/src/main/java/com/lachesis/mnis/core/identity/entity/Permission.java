package com.lachesis.mnis.core.identity.entity;




/**
 *@author xin.chen
 **/
public class Permission{
	private SysModule module;
	private SysOperate operate;
	private int validTime;
	
	public int getValidTime() {
		return validTime;
	}
	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}
	public SysModule getModule() {
		return module;
	}
	public void setModule(SysModule module) {
		this.module = module;
	}
	public SysOperate getOperate() {
		return operate;
	}
	public void setOperate(SysOperate operate) {
		this.operate = operate;
	}
}
