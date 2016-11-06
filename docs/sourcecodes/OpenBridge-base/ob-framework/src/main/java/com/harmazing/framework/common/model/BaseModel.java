package com.harmazing.framework.common.model;

@SuppressWarnings("serial")
public class BaseModel implements IBaseModel {
	protected Attach attach;

	public Attach getAttach() {
		return attach;
	}

	public void setAttach(Attach attach) {
		this.attach = attach;
	}

	public void attObject(String key, Object obj) {
		if (this.attach == null)
			this.attach = new Attach();

		this.attach.put(key, obj);
	}
}
