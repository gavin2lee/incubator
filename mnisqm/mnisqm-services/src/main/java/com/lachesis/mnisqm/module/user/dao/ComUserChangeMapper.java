package com.lachesis.mnisqm.module.user.dao;

import java.util.List;

import com.lachesis.mnisqm.module.user.domain.ComUserChange;


public interface ComUserChangeMapper {
	
	int deleteByPrimaryKey(Long seqId);

	int insert(ComUserChange comUserChange);
	
	public List<ComUserChange> selectAll(ComUserChange comUserChange);
	
	int update(ComUserChange comUserChange);
}
