package com.gl.avs.data.service;

import java.util.List;

import com.gl.avs.vo.UserGroupVO;


public interface UserGroupService {
	UserGroupVO getUserGroupById(Long id);
	UserGroupVO getUserGroupByCode(Byte code);
	List<UserGroupVO> getAll();
}
