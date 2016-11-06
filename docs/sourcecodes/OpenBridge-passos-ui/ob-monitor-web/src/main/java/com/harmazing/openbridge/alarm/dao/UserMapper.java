package com.harmazing.openbridge.alarm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.User;

    /** created By钟梦元**/
public interface UserMapper extends IBaseMapper{

	@Select("select DISTINCT sys_user.user_name  as name,sys_user.email as email,sys_user.mobile as phone,sys_user.login_name as loginName   from grp_tpl,tpl,team,action,rel_team_user ,sys_user"+ 
     " where  tpl.id=grp_tpl.tpl_id  AND tpl.action_id = action.id and  team.`name`=action.uic and team.id=rel_team_user.tid  and rel_team_user.user_id=sys_user.user_id and  grp_tpl.grp_id=#{id}")
	 List<User> findUsersByGroupId(long id);
	
	@Select("select DISTINCT sys_user.user_name  as name,sys_user.email as email,sys_user.mobile as phone,sys_user.login_name as loginName   from grp_tpl,tpl,team,action,rel_team_user ,sys_user"+ 
		     " where  tpl.id=grp_tpl.tpl_id  AND tpl.action_id = action.id and  team.`name`=action.uic and team.id=rel_team_user.tid  and rel_team_user.user_id=sys_user.user_id and  grp_tpl.grp_id=#{id}  limit #{start},#{size}")
	 List<User> findUserPageByGroupId(@Param("id")long id,@Param("start") int start,@Param("size") int size);
	 
	 int findUserCount(long id);
	 
}
