package com.gl.avs.data.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.gl.avs.model.UserGroup;
import com.gl.avs.vo.UserGroupVO;

public interface UserGroupRepository {
	@Select("insert into user_group " + "(" + "create_at,update_at,code,name" + ")" + "values" + "("
			+ "#{createAt},#{updateAt},#{code},#{name}" + ")")
	void insertOne(UserGroup userGroup);

	@Select("select " + "oid," + "code," + "create_at as createAt," + "update_at as updateAt," + "name "
			+ " from user_group " + " where oid=#{id}")
	UserGroupVO findById(@Param("id") Long id);

	@Select("select oid,code,name,create_at as createAt,update_at as updateAt "
			+" from user_group "
			+" where code = #{code}")
	UserGroupVO findByCode(@Param("code") Byte code);
	
	@Select("select oid,code,name,create_at as createAt,update_at as updateAt "
			+" from user_group")
	List<UserGroupVO> findAll();
}
