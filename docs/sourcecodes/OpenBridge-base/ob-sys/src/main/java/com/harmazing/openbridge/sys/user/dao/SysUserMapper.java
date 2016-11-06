package com.harmazing.openbridge.sys.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.authorization.LoginLog;
import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.sys.user.model.SysUser;

public interface SysUserMapper extends IBaseMapper {
	@Select("select * from sys_user where user_id=#{userId}")
	SysUser getUserById(@Param("userId") String userId);

	@Select("select tenant_id as tenantId,tenant_name as tenantName from sys_tenant where tenant_id in ( "
			+ "	SELECT tenant_id FROM sys_tenant_relation where user_id = #{userId} "
			+ " ) limit 0,1 ")
	Map<String, Object> getTenantByUserId(@Param("userId") String userId);

	@Select("select * from sys_user where login_name=#{loginName}")
	SysUser getUserByLoginName(@Param("loginName") String loginName);

	List<Map<String, Object>> userPage(Map<String, Object> params);

	Integer userPageRecordCount(Map<String, Object> params);

	void create(SysUser user);

	@Insert("")
	void register(SysUser user);

	@Update("")
	void activate(boolean activate);

	@Update("update sys_user set user_name=#{userName},mobile=#{mobile}, login_name=#{loginName}, roles =#{roles},email=#{email},activate=#{activate} where user_id=#{userId}")
	void update(SysUser user);

	@Delete("delete from sys_user where user_id=#{id}")
	void deleteById(@Param("id") String id);

	@Update("update sys_user set activate=#{status} where user_id=#{userId}")
	void updateUserStatus(@Param("userId") String userId,
			@Param("status") Boolean status);

	@Update("update sys_user set login_password=#{loginPassword} where user_id=#{userId}")
	void updateUserPassword(@Param("userId") String userId,
			@Param("loginPassword") String loginPassword);

	@Select("select  u.user_id as userId,u.user_name as userName from sys_user u where sys_user=0")
	List<Map<String, Object>> findAllUser();

	@Select("select user_id,login_name from sys_user")
	List<Map<String, Object>> findAllUserLoginName();

	@Update("update sys_user set token=#{token} where user_id=#{userId}")
	void updateUserToken(@Param("userId") String userId,
			@Param("token") String token);

	@Select("select * from sys_user where token=#{token}")
	SysUser getUserByToken(@Param("token") String token);

	List<SysUser> getUsersByIds(List<String> ids);

	@Update("update sys_user set api_auth_key=#{secretKey} where user_id=#{userId}")
	void resetSecretKey(@Param("userId") String userId,
			@Param("secretKey") String secretKey);

	@Insert("insert into sys_login_logs(log_id,login_name,login_param,login_time,login_type,client_ip,user_agent,login_sys,user_id) "
			+ "values (#{logId},#{loginName},#{loginParam},#{loginTime},#{loginType},#{clientIp},#{userAgent},#{loginSys},#{userId})")
	void writeUserLoginLog(LoginLog log);
}
