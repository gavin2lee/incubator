/*
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.core.old.doc;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * The class for Base Entity.
 * 
 * @author: yanhui.wang
 * @since: 2014/03/15
 * @version: $Revision: 1.0 $ $Date: 2014/03/15 20:46 $ $LastChangedBy:yanhui.wang$
 * 
 */
public class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2999147843962544479L;

	private String refid;
	private String createUserRefid;
	private Date createDateTime;
	private String modifyUserRefid;
	private Date modifyDateTime;
	private Integer version;
	private Boolean active;

	private String head;
	private String foot;

	public BaseEntity() {
	}

	public BaseEntity(String refid) {
		this.refid = refid;
	}

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getCreateUserRefid() {
		return createUserRefid;
	}

	public void setCreateUserRefid(String createUserRefid) {
		this.createUserRefid = createUserRefid;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Date getModifyDateTime() {
		return modifyDateTime;
	}

	public void setModifyDateTime(Date modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}

	public String getModifyUserRefid() {
		return modifyUserRefid;
	}

	public void setModifyUserRefid(String modifyUserRefid) {
		this.modifyUserRefid = modifyUserRefid;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getFoot() {
		return foot;
	}

	public void setFoot(String foot) {
		this.foot = foot;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(Object object) {
		return EqualsBuilder.reflectionEquals(this, object, false);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, false);
	}

}
