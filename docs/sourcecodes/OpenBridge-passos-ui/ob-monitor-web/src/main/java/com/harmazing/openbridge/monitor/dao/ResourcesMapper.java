package com.harmazing.openbridge.monitor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.Host;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import com.harmazing.openbridge.monitor.model.PaasHost;

public interface ResourcesMapper extends IBaseMapper {

	@Select("select count(*) from grp where grp_type=#{type}")
	int getCountByType(String type);
	
	@Select("select count(*) from tpl where type=#{type}")
	int getTplCountByType(String type);
	
	@Select("select count(*) from team where type=#{type}")
	int getTeamCountByType(String type);
	
	@Select("SELECT id, hostname, ip, agent_version, plugin_version, maintain_begin, maintain_end, "
			+ "update_at FROM host WHERE hostname = #{name} ")
	Host findByName(String name);

	@Select("SELECT * FROM os_nginx_host  ")
	List<PaasHost> findAllPaasHost();

	@Select("SELECT g.id, g.grp_name AS grpName, g.create_user AS createUser, g.create_at AS createAt, g.come_from AS comeFrom, "
			+ "(select count(grp_tpl.tpl_id) from grp, grp_tpl  where grp.id = grp_tpl.grp_id and grp.id=g.id) AS countTemplates, "
			+ "(select count(grp_host.host_id) from grp , grp_host  where  grp.id = grp_host.grp_id and grp.id=g.id) AS countHosts, "
			+ "(select count(rtu.user_id) from grp ,grp_tpl gt,tpl t,action a,team te,rel_team_user rtu "
			+ "where grp.id = gt.grp_id and gt.tpl_id = t.id and  t.action_id = a.id and a.uic = te.`name` and te.id = rtu.tid and grp.id = g.id) as countUsers  "
			+ "FROM grp AS g where g.grp_type BETWEEN 1 and 6 GROUP BY g.id ORDER BY g.grp_type  ")
	List<GroupIndexDTO> findTypeDTO();
	
	 @Select("SELECT id, grp_name, create_user, create_at, come_from FROM grp WHERE grp_type = #{type} ")
	    Group findByType(String type);
	 @Delete("DELETE FROM strategy WHERE tpl_id = #{id} ")
	    int deleteById(long id);
	 
	 @Insert("INSERT INTO tpl (tpl_name, create_user,type) VALUES (#{tplName},#{createUser},#{type}) ")
	    @Options(useGeneratedKeys = true, keyProperty = "id")
	    int insert(Template template);
	 
	 @Select("SELECT  t.id, t.name, t.resume, t.creator, t.created, t.creator_user FROM team t WHERE type = #{type} ")
	    Team findTeamByType(String type);
}
