package com.harmazing.openbridge.sys.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.sys.user.model.SysGroup;

public interface SysGroupMapper extends IBaseMapper {
	
	@Select("select * from sys_group")
	public List<SysGroup> getAll();
	
	List<Map<String, Object>> groupPage(Map<String, Object> params);

	Integer groupPageRecordCount(Map<String, Object> params);
	
	@Delete("delete from sys_group where group_id=#{groupId}")
	void deleteById(@Param("groupId")String groupId);
	
	@Select("select * from sys_group where group_id=#{groupId}")
	SysGroup getGroupById(@Param("groupId")String groupId);
	
	@Select("select * from sys_group where group_name=#{groupName} limit 0,1")
	SysGroup getGroupByName(@Param("groupName")String groupName);
	
	List<Map<String, Object>> getUsersByGroupId(@Param("groupId")String groupId);
	
	@Update("update sys_group set group_name =#{groupName} where group_id=#{groupId}")
	void updateGroupName(@Param("groupId")String groupId, @Param("groupName")String groupName);
	
	@Delete("delete from sys_user_group where user_id=#{userId} and group_id=#{groupId}")
	void deleteRelation(@Param("groupId")String groupId, @Param("userId")String userId);
	
	@Insert("insert into sys_user_group values(#{relationId},#{userId},#{groupId})")
	void addRelation(@Param("relationId")String relationId, @Param("userId")String userId, @Param("groupId")String groupId);
	
	void addGroup(SysGroup group);
	
	@Delete("delete from sys_user_group where group_id=#{groupId}")
	void deleteRelationByGroup(@Param("groupId")String groupId);
	
	@Delete("delete from sys_user_group where user_id=#{userId}")
	void deleteRelationByUser(@Param("userId")String userId);
}
