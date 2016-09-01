package com.lachesis.mnisqm.module.remote.user.dao;

import java.util.List;

import com.lachesis.mnisqm.module.user.domain.JuniorAskForLeave;

public interface JuniorAskForLeaveMapper {

	List<JuniorAskForLeave> queryAllJuniorLeaveInfo();
	
	List<JuniorAskForLeave> selectAll();
	
}
