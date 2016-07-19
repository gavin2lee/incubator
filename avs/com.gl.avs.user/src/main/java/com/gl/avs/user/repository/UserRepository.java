package com.gl.avs.user.repository;

import com.gl.avs.vo.UserVO;

public interface UserRepository{
	UserVO findOne(String username);
	String save(UserVO user);
}
