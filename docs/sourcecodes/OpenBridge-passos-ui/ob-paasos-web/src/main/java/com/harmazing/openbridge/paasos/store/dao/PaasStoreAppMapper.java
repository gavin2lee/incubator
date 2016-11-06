package com.harmazing.openbridge.paasos.store.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenantPreset;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;

public interface PaasStoreAppMapper extends IBaseMapper {
	static String properties = "id,name,file_type,file_path,file_data,icon_path,version,command,work_dir,ports,config,description,create_time,update_time,status,image_id,app_name,documentation,dockerfile";
	static String tableName = "os_preset_app";
	
	List<PaasStoreApp> getPageData( @Param("start") int start, @Param("size") int size, @Param("keyword") String keyword, @Param("user")IUser iUser);

	int getPageCount(@Param("keyword") String keyword,@Param("iUser")IUser iUser);
	
	/**
	 * 
	 * getStoreCount:(项目管理中用到的分页查询). 和getStoreList方法一起<br/>
	 *
	 * @author dengxq
	 * @param params
	 * @return
	 * @since JDK 1.6
	 */
	public int getStoreCount(Map<String, Object> params);

	public Collection<? extends Map<String, Object>> getStoreList(Map<String, Object> params);
	
	@Select("select "+properties+" from "+tableName+" where id=#{id}")
	PaasStoreApp getById(String id);
	
	@Insert("insert into "+tableName+"("+properties+") "
			+ "values(#{id},#{name},#{fileType},#{filePath},#{fileData},#{iconPath},#{version},#{command},#{workDir},#{ports},#{config},#{description},#{createTime},#{createTime},#{status},#{imageId},#{appName},#{documentation},#{dockerfile})")
	void add(PaasStoreApp build);
	
	@Update("update "+tableName+" set name=#{name},file_type=#{fileType},file_path=#{filePath},file_data=#{fileData},icon_path=#{iconPath},"
			+ "version=#{version},command=#{command},work_dir=#{workDir},ports=#{ports},config=#{config},"
			+ "description=#{description},status=#{status},build_logs=#{buildLogs},image_id=#{imageId},update_time=#{updateTime},"
			+ "app_name=#{appName},documentation=#{documentation},dockerfile=#{dockerfile} where id=#{id}")
	void update(PaasStoreApp build);
	
//	@Update("update "+tableName+" set icon_path=#{iconPath},description=#{description},app_name=#{appName},documentation=#{documentation},dockerfile=#{dockerfile} where id=#{id}")
//	void updateLogoAndDesc(@Param("iconPath")String iconPath, @Param("description")String description,@Param("id")String id,
//			@Param("appName")String appName,@Param("documentation")String documentation,@Param("dockerfile")String dockerfile);
	
	void updateLogoAndDesc(@Param("build")PaasStoreApp build);
	
	@Delete("delete from "+tableName+" where id=#{id}")
	void delete(String id);
	
	@Select("select build_logs from "+tableName+" where id=#{id}")
	public String getBuildLog(String id);
	
	@Update("update "+tableName+" set status=#{status} where id=#{id}")
	public void updateStatus(@Param("id") String id,@Param("status") Integer status);
	
	@Select("select id from "+tableName+" where name=#{name} and version=#{version}")
	List<String> getByNameAndVersion(@Param("name")String name, @Param("version")String version );

	@Insert("insert into sys_tenant_preset(id,tenant_id,preset_id)values(#{id},#{tenantId},#{presetId})")
	void addSysTenantPreset(PaaSTenantPreset paaSTenantPreset);
}
