package com.gl.avs.data.service;

import com.gl.avs.vo.UserVO;

public interface UserService {
	UserVO addUser(UserVO user);
	UserVO getUserById(Long id);
	UserVO getUserByUsername(String username);
}
