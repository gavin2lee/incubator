package com.lachesis.mnisqm;

import com.lachesis.mnisqm.constants.Constants;

public class BaseDataVo extends BaseVo {
	private static final long serialVersionUID = -8448782546547543833L;
	
	

	public BaseDataVo() {
		super();
		setCode(Constants.Success);
	}

	public BaseDataVo(String ackResult, String msg) {
		super(ackResult, msg);
	}

	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
