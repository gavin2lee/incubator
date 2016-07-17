package com.gl.avs.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.avs.vo.UserVO;

@RestController
@RequestMapping("users")
public class UserServiceController {
	
	//users/user?username=xxx
	@RequestMapping(value="/user",method=RequestMethod.GET)
	public UserVO findUser(@RequestParam("username") String username){
		return null;//TODO
	}
}
