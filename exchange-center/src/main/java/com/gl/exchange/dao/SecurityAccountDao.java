package com.gl.exchange.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.gl.exchange.model.SecurityAccount;

public interface SecurityAccountDao {
	@Insert("insert into SecurityAccount (holder, shares) values (#{holder}, #{shares})")
	void insertOne(SecurityAccount sa);
	
	@Select("select * from SecurityAccount where holder = #{holder}")
	SecurityAccount findByHolder(@Param("holder") String holder);
	
	@Update("update SecurityAccount set shares = #{shares} where id = #{id}")
	void updateOne(SecurityAccount sa);
}
