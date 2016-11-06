package com.harmazing.openbridge.monitor.service;

import java.util.List;

import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;

public interface IMonitorResourcesService {

	List<GroupIndexDTO> findTypeDTO();

	Team findTeamByType(String type);
	
	void findNodeByType(String userId, String type, String typeName, String grpName,String tplName,String teamName);

	void findNginx(String userId);

	void findPlatform(String userId);
	
	void save(String userId, String tplData, Group group,String users);
}
