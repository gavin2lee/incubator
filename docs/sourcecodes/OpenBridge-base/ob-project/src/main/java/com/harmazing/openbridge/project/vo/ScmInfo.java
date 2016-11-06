package com.harmazing.openbridge.project.vo;

import java.io.Serializable;

public abstract class ScmInfo implements Serializable {
	protected String scmType;

	public String getScmType() {
		return scmType;
	}

	public ScmInfo(String scmType) {
		this.scmType = scmType;
	}

}
