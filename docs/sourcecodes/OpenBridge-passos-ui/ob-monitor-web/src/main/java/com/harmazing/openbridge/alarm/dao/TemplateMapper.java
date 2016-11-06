package com.harmazing.openbridge.alarm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.SysUserDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateEditDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateIndexDTO;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/3 17:06.
 */
public interface TemplateMapper extends IBaseMapper {
    @Select("SELECT id, tpl_name, parent_id, action_id, create_user, create_at FROM tpl ORDER BY id desc ")
    List<Template> findAll();
    @Select("SELECT id, tpl_name, parent_id, action_id, create_user, create_at FROM tpl WHERE id = #{id} ")
    Template findById(long id);
    @Select("SELECT id, tpl_name, parent_id, action_id, create_user, create_at FROM tpl WHERE tpl_name = #{tplName} ")
    Template findByTplName(String tplName);
    
    @Select("SELECT id, tpl_name, parent_id, action_id, create_user, create_at FROM tpl WHERE id = #{id} ")
    @Results({@Result(id=true,column = "id",property = "id"),
            @Result(property = "action", column = "action_id",
                    one = @One(select = "com.harmazing.openbridge.alarm.dao.ActionMapper.findById")),
            @Result(property = "strategies", column = "id",
                    many = @Many(select = "com.harmazing.openbridge.alarm.dao.StrategyMapper.findByTplId"))})
    TemplateEditDTO findDtoById(long id);
    @Select("SELECT p.id, p.tpl_name, p.parent_id, p.action_id, p.create_user, p.create_at," +
            "if(a.uic is null,'',a.uic) as team_name FROM tpl as p left join action as a on p.action_id=a.id " +
            "where type=0 ORDER BY p.id DESC ")
    List<TemplateIndexDTO> findAllDTO();
    
    @Select("SELECT id, tpl_name, parent_id, action_id, create_user, create_at FROM tpl,grp_tpl WHERE grp_tpl.tpl_id =tpl.id and  grp_tpl.grp_id=#{id}")
    @Results({@Result(id=true,column = "id",property = "id"),
            @Result(property = "action", column = "action_id",
                    one = @One(select = "com.harmazing.openbridge.alarm.dao.ActionMapper.findById")),
            @Result(property = "strategies", column = "id",
                    many = @Many(select = "com.harmazing.openbridge.alarm.dao.StrategyMapper.findByTplId"))})
    List<TemplateEditDTO> findDtoByGroupId(long id);
    
    @Select("SELECT id, tpl_name, parent_id, action_id, create_user, create_at FROM tpl,grp_tpl WHERE grp_tpl.tpl_id =tpl.id and  grp_tpl.grp_id=#{id} limit #{start},#{size}")
    @Results({@Result(id=true,column = "id",property = "id"),
            @Result(property = "action", column = "action_id",
                    one = @One(select = "com.harmazing.openbridge.alarm.dao.ActionMapper.findById")),
            @Result(property = "strategies", column = "id",
                    many = @Many(select = "com.harmazing.openbridge.alarm.dao.StrategyMapper.findByTplId"))})
    List<TemplateEditDTO> findDtoPageByGroupId(@Param("id") long id,@Param("start")int start,@Param("size")int size);
    
    int getDToPageCountByGroupId(long id);
    @Update("UPDATE tpl SET tpl_name = #{0} WHERE id = #{1}")
    int updateTplNameById(String tplName,long id);
    @Update("UPDATE tpl SET action_id = #{0} WHERE id = #{1}")
    int updateActionIdById(long actionId,long id);
    @Insert("INSERT INTO tpl (tpl_name, create_user,action_id) VALUES (#{tplName},#{createUser},#{actionId}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Template template);
    @Delete("DELETE FROM tpl WHERE id = #{id} ")
    int deleteById(long id);
    @Select("SELECT t.id, t.tpl_name, t.parent_id, t.action_id, t.create_user, t.create_at," +
            "if(a.uic is null,'',a.uic) as team_name FROM tpl as t LEFT JOIN action AS a ON t.action_id = a.id,grp_tpl WHERE t.id=grp_tpl.tpl_id  and grp_tpl.grp_id = #{id} ")
    List<TemplateIndexDTO> findByGroupId(long id);
    
    List<Map<String, Object>> Page(Map<String, Object> params);

  	Integer PageRecordCount(Map<String, Object> params);
  	
  	int getTplCountByName(@Param("name")String name, @Param("id")long id);
  	
  	@Select("SELECT  t.tpl_name,rtu.user_id,su.user_name from tpl as t,action as a,team as te,rel_team_user as rtu,sys_user su  where t.action_id=a.id "+
  			"and a.uic=te.name and te.id=rtu.tid and rtu.user_id=su.user_id and t.id=#{id}")
  	List<SysUserDTO>  findUserByTid(long id);
}
