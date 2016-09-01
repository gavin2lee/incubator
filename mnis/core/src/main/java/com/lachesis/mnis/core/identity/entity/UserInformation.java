package com.lachesis.mnis.core.identity.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Domain Object: representing a system user, his departments and roles
 *
 **/
public class UserInformation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
	private String userId;
	private SysUser user;
	private String deptCode;
	private String deptName;
	private List<SysDept> deptList;
	private List<SysRole> roleList;
	private Map<String, Map<String,String>> configs;
	private List<SysMenu> sysMenus;
	private String sysTime;
	
	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	/**
	 * Convenience method to get employee code 
	 * @return
	 */
	public String getCode() {
		if(user == null) {
			return null;
		}
		return user.getCode();
	}
	
	/**
	 * 获取默认科室号
	 * @return
	 */
	public String getDeptCode() {
		
		return deptCode;
	}
	
	public String getDeptName() {
		return deptName;
	}
	
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * Convenience method to get employee real name
	 * @return
	 */
	public String getName() {
		if(user == null) {
			return null;
		}
		return user.getName();
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}	

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<SysRole> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<SysRole> roleList) {
		this.roleList = roleList;
	}
	public List<SysDept> getDeptList() {
		return deptList;
	}
	public void setDeptList(List<SysDept> deptList) {
		this.deptList = deptList;
	}

	public Map<String, Map<String,String>> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, Map<String,String>> configs) {
		this.configs = configs;
	}

	public List<SysMenu> getSysMenus() {
		return sysMenus;
	}

	public void setSysMenus(List<SysMenu> sysMenus) {
		this.sysMenus = sysMenus;
	}
}
