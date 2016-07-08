package com.gl.avs.rest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gl.avs.data.service.UserGroupService;
import com.gl.avs.vo.UserGroupVO;

@RestController
@RequestMapping("usergroup")
public class UserGroupController {
	private static final Logger log = LoggerFactory.getLogger(UserGroupController.class);
	
	@Autowired
	private UserGroupService userGroupService;
	
	@RequestMapping(value="/usergroups",method=RequestMethod.GET)
	public List<UserGroupVO> fetchAll(){
		log.debug("fetch all");
		return userGroupService.getAll();
	}
}
