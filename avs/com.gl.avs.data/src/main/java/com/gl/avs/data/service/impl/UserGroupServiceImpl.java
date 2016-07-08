package com.gl.avs.data.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gl.avs.data.repository.UserGroupRepository;
import com.gl.avs.data.service.UserGroupService;
import com.gl.avs.vo.UserGroupVO;

@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService {
	private static final Logger log = LoggerFactory.getLogger(UserGroupServiceImpl.class);
	@Autowired
	private UserGroupRepository userGroupRepository;

	@Transactional(readOnly=true)
	public UserGroupVO getUserGroupById(Long id) {
		if(log.isDebugEnabled()){
			log.debug("get user group by id:"+id);
		}
		return userGroupRepository.findById(id);
	}

	@Transactional(readOnly=true)
	public UserGroupVO getUserGroupByCode(Byte code) {
		if(log.isDebugEnabled()){
			log.debug("get user group by code:"+code);
		}
		return userGroupRepository.findByCode(code);
	}

	@Transactional(readOnly=true)
	public List<UserGroupVO> getAll() {
		if(log.isDebugEnabled()){
			log.debug("get user group all");
		}
		return userGroupRepository.findAll();
	}

}
