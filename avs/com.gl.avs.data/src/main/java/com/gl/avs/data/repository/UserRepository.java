package com.gl.avs.data.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;

import com.gl.avs.model.User;
import com.gl.avs.vo.UserVO;

public interface UserRepository {
	@Insert("insert into user (create_at,first_name,last_name,gender,birth,username,passwd,user_group_id)"
			+"values("
			+"#{createAt},#{firstName},#{lastName},#{gender},#{birth},#{username},#{passwd},#{userGroup.oid}"
			+")")
	int insertUser(User user);
	UserVO findById(Long id);
	UserVO findByUsername(String username);
	
	List<UserVO> findByTemplate(User user);
	
	int updateUser(User user);
	int deleteUser(User user);
}
