package com.harmazing.openbridge.sys.user.service;

import java.util.List;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.sys.user.model.SysDepart;

public interface ISysDepartService extends IBaseService{
	/**
	 * 获取组织结构树形结构的字符串
	 * @return
	 */
	String getLevelStrutString();
	
	/**
	 * 获取组织结构下拉列表
	 * @return
	 */
	String getDepartOptionString(SysDepart depart);
	
	/**
	 * 获取单个组织结构的详细信息
	 * @param deptId
	 * @return
	 */	
	SysDepart getDepartById(String deptId);
	
	/**
	 *获取当前部门的子部门 
	 */
	List<SysDepart> getSubDepartById(String departId);
	
	/**
	 * 删除组织结构
	 * @param deptId
	 */
	int deleteDepartById(String deptId);
	
	/**
	 * 新增或修改组织结构
	 * @param depart
	 * @return
	 */
	int saveOrUpdateDepart(SysDepart depart);
	
	/**
	 * 用户所属部门下拉框
	 * @param id
	 * @return
	 */
	String getUserDepartOptionString(String id);
}
