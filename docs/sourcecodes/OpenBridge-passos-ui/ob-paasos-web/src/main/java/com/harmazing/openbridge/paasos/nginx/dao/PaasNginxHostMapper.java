package com.harmazing.openbridge.paasos.nginx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;

public interface PaasNginxHostMapper extends IBaseMapper {
	@Select("select * from os_nginx_host")
	List<PaasHost> getAllHost();

	Integer queryHostCount(Map<String, Object> params);
	
	int getcountHostByIp(PaasHost host);
	int getcountHostByName(PaasHost host);
	List<PaasHost> queryHostPage(Map<String, Object> params);

	@Insert("insert into os_nginx_host(host_id,host_ip,host_user,host_key_prv,host_key_pub,host_port,env_type,host_type,host_platform,backup_host,virtual_host,directory_name) "
			+ " values(#{hostId},#{hostIp},#{hostUser},#{hostKeyPrv},#{hostKeyPub},#{hostPort},#{envType},#{hostType},#{hostPlatform},#{backupHost},#{virtualHost},#{directoryName})")
	void insertHost(PaasHost host);

	@Update("update os_nginx_host set host_ip=#{hostIp}, "
			+ " host_user=#{hostUser}, " 
			+ " host_key_prv=#{hostKeyPrv}, "
			+ " host_key_pub=#{hostKeyPub}, " 
			+ " host_port=#{hostPort}, "
			+ " env_type=#{envType}, "
			+ " host_type=#{hostType}, host_platform=#{hostPlatform},backup_host=#{backupHost},virtual_host=#{virtualHost},directory_name=#{directoryName} where host_id=#{hostId} ")
	void updateHost(PaasHost host);
	
	@Delete("delete from os_nginx_host where host_id=#{hostId}")
	void deleteHostById(@Param("hostId")String hostId);
}