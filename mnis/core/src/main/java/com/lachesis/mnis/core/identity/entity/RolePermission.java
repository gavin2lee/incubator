package com.lachesis.mnis.core.identity.entity;

import java.util.List;

/**
 *@author xin.chen
 **/
public class RolePermission {
	private SysRole role;
	private List<Permission> listPermission;
	
	public List<Permission> getListPermission() {
		return listPermission;
	}
	public void setListPermission(List<Permission> listPermission) {
		this.listPermission = listPermission;
	}
	public SysRole getRole() {
		return role;
	}
	public void setRole(SysRole role) {
		this.role = role;
	}
}
