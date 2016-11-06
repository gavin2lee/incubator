package com.harmazing.openbridge.sys.role.model;

import com.harmazing.framework.common.model.IBaseModel;

@SuppressWarnings("serial")
public class SysFunc implements IBaseModel {
	private String funcId;
	private String funcName;
	private String funcDesc;
	private String funcSystem;
	private String funcModule;

	public String getFuncSystem() {
		return funcSystem;
	}

	public void setFuncSystem(String funcSystem) {
		this.funcSystem = funcSystem;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getFuncDesc() {
		return funcDesc;
	}

	public void setFuncDesc(String funcDesc) {
		this.funcDesc = funcDesc;
	}

	public String getFuncModule() {
		return funcModule;
	}

	public void setFuncModule(String funcModule) {
		this.funcModule = funcModule;
	}

}
