package com.gl.avs.model;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6928120888327704487L;
	private Long oid;
	private Date createAt;
	private Date updateAt;
	
	
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	
	
}
