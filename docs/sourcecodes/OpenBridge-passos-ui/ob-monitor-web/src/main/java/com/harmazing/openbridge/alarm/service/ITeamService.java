package com.harmazing.openbridge.alarm.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.model.User;
import com.harmazing.openbridge.sys.user.model.SysUser;

public interface ITeamService extends IBaseService{


	
	/**
	 * @author dt
	 * @Description 分页查询所有
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<Team> getPage(int pageNo, int pageSize);
	
	/**
	 * @author dt
	 * @Description 按ID查找
	 * @param id
	 * @return
	 */
	Team get(String id);
	SysUser getUserById(String id);
	
	void delete(String id);
	
	void saveOrUpdate(Team team,String userId);

	List<String> getTeamName();
	 List<String> getTeamByName(String name);
	List<User> getAllUserByTeamName(String teamName);
	/**
	 * @author luoan
	 * 用于策略编辑界面，分页展示策略组。
	 * @param params
	 * @return
	 */
	Page<Map<String, Object>> Page(Map<String, Object> params);
}
