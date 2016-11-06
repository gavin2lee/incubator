package com.harmazing.openbridge.alarm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.model.User;
import com.harmazing.openbridge.sys.user.model.SysUser;

public interface TeamMapper extends IBaseMapper {
	@ResultMap("team")
	@Select("select t.*,su.user_name as creatorUserName from team t left join sys_user su on t.creator_user = su.user_id where type=0 limit #{start},#{size}")
	List<Team> getPageData(@Param("start") int start, @Param("size") int size);

	@Select("SELECT name FROM team ORDER BY name")
	List<String> getTeamName();
	
	@Select("SELECT * FROM team WHERE name = #{name} ORDER BY name")
	List<String> getTeamByName(String name);


	int getPageCount();
	
    List<Map<String, Object>> Page(Map<String, Object> params);

  	Integer PageRecordCount(Map<String, Object> params);

	@ResultMap("team")
	@Select("select * from team where id=#{id}")
	Team getById(String id);

	@Delete("delete from Team where id=#{id}")
	void delete(String id);
	
	@Delete("delete from rel_team_user where tid=#{id}")
	void deleteMember(String id);
	
	
	@Insert("insert into team(name,resume,creator,created,creator_user,type)"
			+ " values(#{name},#{resume},#{creator},#{created},#{creatorUser},#{type})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void addTeam(Team team);
	
	@Update("update team set name=#{name},resume=#{resume} where id=#{id}")
	void update(Team team);
	@Insert("insert into rel_team_user(tid,uid,user_id) values(#{tId},#{tId},#{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void addMember(Map<String, Object> member);
	
	int getTeamCountByName(@Param("name")String name, @Param("id")int id);
	
	@Select("select * from sys_user where user_id=#{id}")
	SysUser getUserById(String id);

	@Select("select u.user_id as id,u.login_name as name,u.user_name as cnname,u.email,u.mobile as phone " +
			"from team as t,rel_team_user as rtu, sys_user as u " +
			"where t.id=rtu.tid and rtu.user_id=u.user_id and t.name = #{teamName}")
	List<User> getAllUserByTeamName(String teamName);
}