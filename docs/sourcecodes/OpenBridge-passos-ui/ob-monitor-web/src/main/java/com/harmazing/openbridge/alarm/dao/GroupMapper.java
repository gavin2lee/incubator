package com.harmazing.openbridge.alarm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/7/29 10:07.
 */
public interface GroupMapper extends IBaseMapper {
    @Select("SELECT id, grp_name, create_user, create_at, come_from,grp_type FROM grp WHERE id = #{id} ")
    Group findById(long id);
    @Select("SELECT id, grp_name, create_user, create_at, come_from FROM grp ORDER BY id desc ")
    List<Group> findAll();
      
    @Select("SELECT g.id, g.grp_name AS grpName, g.create_user AS createUser, g.create_at AS createAt, g.come_from AS comeFrom, " +
            "(select count(grp_tpl.tpl_id) from grp, grp_tpl  where grp.id = grp_tpl.grp_id and grp.id=g.id) AS countTemplates, " +
            "(select count(grp_host.host_id) from grp , grp_host  where  grp.id = grp_host.grp_id and grp.id=g.id) AS countHosts, " +
            "(select count( DISTINCT rel_team_user.user_id)   from grp_tpl,tpl,team,action,rel_team_user  "+
           " where  tpl.id=grp_tpl.tpl_id  and tpl.action_id=action.id and  team.`name`=action.uic and team.id=rel_team_user.tid and  grp_tpl.grp_id=g.id )  as countUsers "+
            "FROM grp AS g where g.grp_type='0' GROUP BY g.id ORDER BY g.id DESC ")
    List<GroupIndexDTO> findAllDTO();
    
    @Select("SELECT g.id, g.grp_name AS grpName, g.create_user AS createUser, g.create_at AS createAt, g.come_from AS comeFrom, " +
            "(select count(grp_tpl.tpl_id) from grp, grp_tpl  where grp.id = grp_tpl.grp_id and grp.id=g.id) AS countTemplates, " +
            "(select count(grp_host.host_id) from grp , grp_host  where  grp.id = grp_host.grp_id and grp.id=g.id) AS countHosts, " +
            "(select count( DISTINCT rel_team_user.user_id)   from grp_tpl,tpl,team,action,rel_team_user  "+
           " where  tpl.id=grp_tpl.tpl_id  and tpl.action_id=action.id and team.`name`=action.uic and team.id=rel_team_user.tid and  grp_tpl.grp_id=g.id )  as countUsers "+
            "FROM grp AS g where g.grp_type='0' GROUP BY g.id ORDER BY g.id DESC limit #{start},#{size}  ")  
    List<GroupIndexDTO> findPageAllDTO(@Param("start") int start,@Param("size") int size);
    
    @Insert("INSERT INTO grp (grp_name, create_user, create_at, come_from,grp_type) " +
            "VALUES (#{grpName},#{createUser},#{createAt},#{comeFrom},#{grpType}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Group group);
    
    int getCount();
    
    @Update("UPDATE grp SET grp_name = #{grpName}, create_user = #{createUser},come_from = #{comeFrom} " +
            "WHERE id = #{id} ")
    int update(Group group);
    @Delete("DELETE FROM grp WHERE id = #{id} ")
    int deleteById(long id);
    
    int getCountByName(@Param("name")String name, @Param("id")long id);
}
