package com.harmazing.openbridge.sys.role.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.harmazing.framework.common.dao.IBaseMapper;

public interface SysFuncMapper extends IBaseMapper {

	@Select("SELECT distinct func_id FROM sys_role_func WHERE role_id = #{roleId}")
	List<String> findFuncByRoleId(String roleId);

	/**
	 * 根据用户id获取拥有功能
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select distinct rf.func_id from sys_role_func rf where rf.role_id in (select distinct role_id from sys_user_role where user_id=#userId)")
	List<String> findFuncByUserId(String userId);

}
