package com.harmazing.openbridge.sys.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.sys.user.model.SysUserDepartment;

public interface SysUserDepartmentMapper extends IBaseMapper {
	@Select("select * from sys_user_dept where user_id=#{userId}")
	SysUserDepartment getByUserId(@Param("userId")String userId);
	
	@Select("select * from sys_user_dept where dep_id=#{departId}")
	List<SysUserDepartment> getByDepartmentId(@Param("departId")String departId);
	
	@Delete("delete from sys_user_dept where user_id=#{userId}")
	void delUserDepartment(@Param("userId")String userId);
	
	@Delete("delete from sys_user_dept where dep_id=#{deptId}")
	void delUserDepartByDeptId(@Param("deptId")String deptId);
	
	@Insert("insert into sys_user_dept values(#{relationId},#{userId},#{depId})")
	void addUserDepartment(SysUserDepartment userDepartment);
	
	@Update("update sys_user_dept set dep_id=#{depId} where user_id=#{userId}")
	void updateUserDepartment(@Param("depId")String depId, @Param("userId")String userId);
}
