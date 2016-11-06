package com.harmazing.openbridge.paasos.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.manager.model.PaaSBaseBuild;

public interface PaaSBaseBuildMapper extends IBaseMapper{
	static String properties = "id,name,file_type,file_path,file_data,icon_path,version,command,work_dir,ports,tenant_ids,description,create_time,status,dockerfile";
	static String tableName = "os_base_build";
	@Select("select "+properties+" from "+tableName+" limit #{start},#{size}")
	List<PaaSBaseBuild> getPageData( @Param("start") int start, @Param("size") int size);
	
	@Select("select count(*) from "+tableName)
	int getPageCount();
	
	@Select("select "+properties+" from "+tableName+" where id=#{id}")
	PaaSBaseBuild getById(String id);
	
	@Insert("insert into "+tableName+"("+properties+") "
			+ "values(#{id},#{name},#{fileType},#{filePath},#{fileData},#{iconPath},#{version},#{command},#{workDir},#{ports},#{tenantIds},#{description},#{createTime},#{status},#{dockerfile})")
	void add(PaaSBaseBuild build);
	
	@Update("update "+tableName+" set name=#{name},file_type=#{fileType},file_path=#{filePath},file_data=#{fileData},icon_path=#{iconPath},"
			+ "version=#{version},command=#{command},work_dir=#{workDir},ports=#{ports},tenant_ids=#{tenantIds},"
			+ "description=#{description},status=#{status},build_logs=#{buildLogs},image_id=#{imageId},dockerfile=#{dockerfile} where id=#{id}")
	void update(PaaSBaseBuild build);
	
	@Update("update "+tableName+" set icon_path=#{iconPath},description=#{description},tenant_ids=#{tenantIds},dockerfile=#{dockerfile} where id=#{id}")
	void updateLogoAndDesc(@Param("iconPath")String iconPath, @Param("description")String description,@Param("tenantIds")String tenantIds,@Param("dockerfile")String dockerfile,@Param("id")String id);
	
	@Delete("delete from "+tableName+" where id=#{id}")
	void delete(String id);
	
	@Select("select "+properties+" from "+tableName+" where tenant_ids='toAll' or tenant_ids like concat('%',#{tenantId},'%')")
	List<PaaSBaseBuild> getTenantBuild(String tenantId);
	
	@Select("select count(*) from "+tableName+" where tenant_ids='toAll'")
	int getPublicImageCount();
	
	@Select("select build_logs from "+tableName+" where id=#{id}")
	public String getBuildLog(String id);
	
	@Update("update "+tableName+" set status=#{status} where id=#{id}")
	public void updateStatus(@Param("id") String id,@Param("status") Integer status);
	
	@Select("select id from "+tableName+" where name=#{name} and version=#{version}")
	List<String> getByNameAndVersion(@Param("name")String name, @Param("version")String version );
	
	@Select("select * from  "+tableName+" where name=#{name} and image_id is not null order by create_time desc")
	public List<PaaSBaseBuild> getByName(String name);
}
