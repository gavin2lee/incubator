package com.harmazing.openbridge.alarm.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.alarm.dao.TeamMapper;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.model.User;
import com.harmazing.openbridge.alarm.service.ITeamService;
import com.harmazing.openbridge.sys.user.model.SysUser;
@Service
@Transactional
public class TeamServiceImpl implements ITeamService {
	@Resource
	private TeamMapper teamMapper;

	
	
	@Override
	@Transactional(readOnly=true)
	public Page<Team> getPage(int pageNo, int pageSize) {
		Page<Team> page = new Page<Team>(pageNo,pageSize);
		page.addAll(teamMapper.getPageData(page.getStart(),page.getPageSize()));
		page.setRecordCount(teamMapper.getPageCount());
		return page;
	}
	
	@Override
	@Transactional(readOnly=true)
	public Team get(String id) {
		Team team = teamMapper.getById(id);
		
		return team;
	}
	
	@Override
	@Transactional(readOnly=true)
	public SysUser getUserById(String id) {
		SysUser user = teamMapper.getUserById(id);
		
		return user;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void delete(String id) {
		teamMapper.deleteMember(id);
		teamMapper.delete(id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void saveOrUpdate(Team team,String userId ) {
		String id = Integer.toString(team.getId());
		int sysTeam = teamMapper.getTeamCountByName(team.getName(),team.getId());
		if(sysTeam > 0){
			throw new RuntimeException("已存在用户组名,请修改！");
		}
	
		if(!id.equals("0")){
			teamMapper.deleteMember(id);	
			teamMapper.update(team);
		}else{
			team.setType("0");
			team.setCreated(new Date());
			team.setCreatorUser(userId);
			teamMapper.addTeam(team);
		}

		for(SysUser user : team.getMember()){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tId", team.getId());
			params.put("userId", user.getUserId());
			teamMapper.addMember(params);
		}
	}

	@Override
	public List<String> getTeamName() {
		return teamMapper.getTeamName();
	}
	
	@Override
	public List<String> getTeamByName(String name){
		return teamMapper.getTeamByName(name);
	}

	public List<User> getAllUserByTeamName(String teamName){
		return teamMapper.getAllUserByTeamName(teamName);
	}

	@Override
	public Page<Map<String, Object>> Page(
			Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
   		xpage.setRecordCount(teamMapper.PageRecordCount(params));
   		xpage.addAll(teamMapper.Page(params));
   		return xpage;
	}


}
