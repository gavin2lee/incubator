package com.harmazing.openbridge.paasos.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.manager.model.PaaSNode;

public interface PaaSNodeMapper extends IBaseMapper{
	
	@Select("select * from os_node")
	List<PaaSNode> list();
	
	@Select("select * from os_node where id=#{id}") 
	PaaSNode getById(String id);
	
	@Select("select * from os_node where name=#{name}") 
	PaaSNode getByName(String name);
	
	@Select("select * from os_node limit #{start},#{size}")
	List<PaaSNode> getPageData( @Param("start") int start, @Param("size") int size);
	
	@Select("select count(*) from os_node")
	int getPageCount();
	
	@Select("select * from os_node where org_id=#{tenantId}")
	List<PaaSNode> getTenantNodes(String tenantId);
	
	@Insert("insert into os_node(id,name,env_type,org_id) "
			+ "values(#{id},#{name},#{envType},#{orgId})")
	void add(PaaSNode node);
	
	@Update("update os_node set name=#{name},env_type=#{envType},org_id=#{orgId} where id=#{id}")
	void update(PaaSNode node);
	
	@Delete("delete from os_node where id=#{id}")
	void delete(String id);

	
	
}
