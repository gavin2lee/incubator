package com.harmazing.openbridge.paasos.imgbuild.log;

import java.util.Date;




import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;

public interface PaaSBuildLogMapper extends IBaseMapper{
	static String TABLE_NAME = " os_build_log ";
	static String PROPERTIES = "id,bus_id,build_logs,create_time";
	@Insert("insert into "+TABLE_NAME+"("+PROPERTIES+") values(#{id},#{busId},#{buildLog},#{createTime})")
	void add(@Param("id")String id,@Param("busId")String busId,
			@Param("buildLog")String buildLog,@Param("createTime")Date createTime);
	
	@Update("update "+TABLE_NAME+" set build_logs = #{buildLog} where bus_id=#{busId}")
	void updateLog(@Param("busId")String busId, @Param("buildLog")String buildLog);
	
	@Delete("delete from "+TABLE_NAME+" where bus_id=#{busId}")
	void delete(String busId);
	
	@Select("select build_logs from "+TABLE_NAME+" where bus_id=#{busId} order by create_time desc limit 0,1")
	String getLog(String busId);
	
	@Select("select count(*) from "+TABLE_NAME+" where bus_id=#{busId}")
	int count(String busId);
}
