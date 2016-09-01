package com.lachesis.mnisqm.module.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.user.domain.ComUser;

public interface ComUserMapperExt extends ISearchableDAO {
	/**
	 * 通过部门编号获取部门下的所有用户
	 * 
	 * @param param
	 * @return
	 */
	public List<ComUser> selectUserByDepot(Map<String, Object> param);

	/**
	 * 根据用户编号获取用户基本信息
	 * 
	 * @param userCode
	 * @return
	 */
	public ComUser selectUserByCode(String userCode);

	/**
	 * 根据用户HIS编号获取用户基本信息
	 * 
	 * @param hisCode
	 * @return
	 */
	public ComUser selectUserByHisCode(String hisCode);

	/**
	 * 根据编号，姓名，部门查找用户信息
	 * 
	 * @param userCode
	 * @param userName
	 * @param deptCode
	 * @param gender
	 * @param userType
	 * @return
	 */
	List<ComUser> selectUserByCodeNameDept(
			@Param(value = "userCode") String userCode,
			@Param(value = "userName") String userName,
			@Param(value = "deptCode") String deptCode,
			@Param(value = "gender")   String gender,
			@Param(value = "userType") String userType
			);

}