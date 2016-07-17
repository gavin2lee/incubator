package com.gl.avs.user.service;

import com.gl.avs.vo.UserVO;

public interface UserService {
	UserVO findUser(String username);
	String saveUser(UserVO user);
}
