package com.harmazing.openbridge.paasos.nginx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;

public interface PaasNginxMapper extends IBaseMapper {

	public void createNginxConf(PaasNginxConf conf);

	@Update("update os_nginx_conf set conf_content=#{confContent},version_id=#{versionId},nginx_name=#{nginxName},skip_auth=#{skipAuth},is_support_ssl=#{isSupportSsl},ssl_crt_id=#{sslCrtId},ssl_key_id=#{sslKeyId} where conf_id=#{confId}")
	public void updateNginxConf(PaasNginxConf conf);

	@Delete("delete from os_nginx_conf where conf_id=#{confId}")
	public void deleteNginxConf(@Param("confId") String confId);

	public List<PaasNginxConf> findNginxConf(
			@Param("serviceId") String serviceId,
			@Param("envType") String envType,
			@Param("versionId") String versionId);

	@Select("select * from os_nginx_conf where env_id=#{envId}")
	public List<PaasNginxConf> getNginxListByEnvId(@Param("envId") String envId);

	@Select("select * from os_nginx_conf where conf_id=#{confId}")
	public PaasNginxConf findByConfId(@Param("confId") String confId);

	@Select("select host_id as hostId,ifnull(count(host_id),0) as count from os_nginx_conf where env_type=#{envType} group by host_id order by count")
	List<Map<String, Object>> getLightNginxHost(@Param("envType") String envType);

	@Select("select count(distinct conf_id) from os_nginx_conf  where host_id=#{hostId}")
	public int getExistConfNum(@Param("hostId") String hostId);

	@Select("select * from os_nginx_conf where service_id=#{serviceId} and nginx_name=#{nginxName} limit 0,1")
	public PaasNginxConf findNginxBySvrIdAndName(
			@Param("serviceId") String serviceId,
			@Param("nginxName") String nginxName);

	Integer queryNginxCount(Map<String, Object> params);

	List<PaasNginxConf> queryNginxPage(Map<String, Object> params);
	
	List<PaasNginxConf> findNginx(PaasNginxConf params);

	
	List<PaasNginxConf> getNginxListByVersionIds(@Param("collection") List<String> versionIds, @Param("envType") String envType,@Param("envMark") String envMark);
}
