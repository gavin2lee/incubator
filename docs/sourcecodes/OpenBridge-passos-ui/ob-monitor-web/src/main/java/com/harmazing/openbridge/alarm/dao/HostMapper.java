package com.harmazing.openbridge.alarm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.Host;

/**
 * Created by liyang on 2016/7/28.
 * 主机
 */
public interface HostMapper extends IBaseMapper{
    @Select("SELECT id, hostname, ip, agent_version, plugin_version, maintain_begin, maintain_end, " +
            "update_at FROM host ORDER BY id desc ")
    List<Host> findAll();
    @Select("SELECT id, hostname, ip, agent_version, plugin_version, maintain_begin, maintain_end, " +
            "update_at FROM host WHERE id = #{id} ")
    Host findById(long id);
    @Select("SELECT h.id, h.hostname, h.ip, h.agent_version, h.plugin_version, h.maintain_begin, h.maintain_end, " +
            "h.update_at FROM host as h,grp_host WHERE h.id=grp_host.host_id and grp_host.grp_id = #{id} ")
    List<Host> findByGroupId(long id);
    @Select("SELECT h.id, h.hostname, h.ip, h.agent_version, h.plugin_version, h.maintain_begin, h.maintain_end, " +
            "h.update_at FROM host as h,grp_host WHERE h.id=grp_host.host_id and grp_host.grp_id = #{id} " +
            "limit #{pageNo},#{pageSize}")
    List<Host> pageFindByGroupId(Map<String, Object> params);
    @Insert("INSERT INTO host (hostname, ip, agent_version, plugin_version, maintain_begin, maintain_end) " +
            "VALUES (#{hostname},#{ip},#{agentVersion},#{pluginVersion},#{maintainBegin}," +
            "#{maintainEnd})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Host host);
    
    List<Map<String, Object>> Page(Map<String, Object> params);

	Integer PageRecordCount(Map<String, Object> params);
	
	@Select("SELECT id, hostname, ip, agent_version, plugin_version, maintain_begin, maintain_end, " +
            "update_at FROM host WHERE hostname = #{name} ")
    Host findByName(String name);
	
}
