package com.harmazing.openbridge.monitor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.alarm.dao.ActionMapper;
import com.harmazing.openbridge.alarm.dao.GroupHostMapper;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.dao.GroupTemplateMapper;
import com.harmazing.openbridge.alarm.dao.StrategyMapper;
import com.harmazing.openbridge.alarm.dao.TeamMapper;
import com.harmazing.openbridge.alarm.dao.TemplateMapper;
import com.harmazing.openbridge.alarm.model.Action;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.Host;
import com.harmazing.openbridge.alarm.model.Strategy;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateEditDTO;
import com.harmazing.openbridge.monitor.dao.ResourcesMapper;
import com.harmazing.openbridge.monitor.model.PaasHost;
import com.harmazing.openbridge.monitor.service.IMonitorResourcesService;
import com.harmazing.openbridge.util.KubernetesUtil;

@Service
@Transactional
public class MonitorResourcesServiceImpl implements IMonitorResourcesService {
	@Autowired
	private ResourcesMapper resourcesMapper;
	@Autowired
	private GroupHostMapper groupHostMapper;
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private StrategyMapper strategyMapper;
	@Autowired
	private TemplateMapper templateMapper;
	@Autowired
	private GroupTemplateMapper groupTemplateMapper;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private ActionMapper actionMapper;

	public List<GroupIndexDTO> findTypeDTO() {
		return resourcesMapper.findTypeDTO();
	}

	public Team findTeamByType(String type) {
		return resourcesMapper.findTeamByType(type);
	}

	public void findNodeByType(String userId, String type, String typeName, String grpName, String tplName,
			String teamName) {
		int sysType = resourcesMapper.getCountByType(type);
		Group group = new Group();
		if (sysType == 0) {
			group.setGrpName(grpName);
			group.setCreateUser(userId);
			group.setComeFrom("1");
			group.setGrpType(type);
			groupMapper.insert(group);
			io.fabric8.kubernetes.api.model.NodeList nl = KubernetesUtil.getNodes();
			if (nl != null) {
				for (io.fabric8.kubernetes.api.model.Node h : nl.getItems()) {
					if (h.getMetadata() == null) {
						continue;
					}
					if (h.getMetadata().getLabels().get("type") != null) {
						if (h.getMetadata().getLabels().get("type").equals(typeName)) {
							Host host = resourcesMapper.findByName(h.getMetadata().getName());
							if (host != null) {
								groupHostMapper.insert(group.getId(), host.getId());
							}
						}
					}
				}
			}
		}
		Template template = new Template();
		int tplType = resourcesMapper.getTplCountByType(type);
		if (tplType == 0) {
			template.setTplName(tplName);
			template.setType(type);
			template.setCreateUser(userId);
			resourcesMapper.insert(template);
			groupTemplateMapper.insert(group.getId(), template.getId(), userId);
			String tpldata = "[{'nm':'cpu','note':'CPU','op':'>=','rightValue':'80%','maxStep':'3','alarmLive':'3'},{'nm':'memory','note':'内存','op':'>=','rightValue':'80%','maxStep':'3','alarmLive':'3'}]";
			List<Map> tpl = JSONArray.parseArray(tpldata, Map.class);
			for (Map m : tpl) {
				Strategy strategy = new Strategy();
				strategy.setMetric(m.get("nm").toString());
				strategy.setFunc("");
				String op=m.get("op").toString();
				strategy.setOp(op.replace("&gt;",">"));
				strategy.setRightValue(m.get("rightValue").toString());
				strategy.setMaxStep(Long.parseLong(m.get("maxStep").toString()));
				strategy.setPriority(Integer.parseInt(m.get("alarmLive").toString()));
				strategy.setTags("");
				strategy.setNote(m.get("note").toString());
				strategy.setRunBegin("");
				strategy.setRunEnd("");
				strategy.setTplId(template.getId());
				strategyMapper.insert(strategy);
			}
		}
		int teamType = resourcesMapper.getTeamCountByType(type);
		if (teamType == 0) {
			Team team = new Team();
			team.setName(teamName);
			team.setResume("");
			team.setType(type);
			team.setCreated(new Date());
			team.setCreatorUser(userId);
			teamMapper.addTeam(team);
			Action action = new Action();
			action.setUic(teamName);
			action.setUrl("");
			actionMapper.insert(action);
			templateMapper.updateActionIdById(action.getId(), template.getId());
		}
	}

	public void findNginx(String userId) {
		int sysType = resourcesMapper.getCountByType("2");
		Group group = new Group();
		if (sysType == 0) {
			group.setGrpName("访问代理");
			group.setCreateUser(userId);
			group.setComeFrom("1");
			group.setGrpType("2");
			groupMapper.insert(group);
			List<PaasHost> phost = new ArrayList<PaasHost>();
			phost = resourcesMapper.findAllPaasHost();
			if (phost != null) {
				for (int i = 0; i < phost.size(); i++) {
					Host host = resourcesMapper.findByName(phost.get(i).getHostIp());
					if (host != null) {
						groupHostMapper.insert(group.getId(), host.getId());
					}
				}
			}
		}
		Template template = new Template();
		int tplType = resourcesMapper.getTplCountByType("2");
		if (tplType == 0) {
			template.setTplName("访问策略");
			template.setType("2");
			template.setCreateUser(userId);
			resourcesMapper.insert(template);
			groupTemplateMapper.insert(group.getId(), template.getId(), userId);
			String tpldata = "[{'nm':'cpu','note':'CPU','op':'>=','rightValue':'80%','maxStep':'3','alarmLive':'3'},{'nm':'memory','note':'内存','op':'>=','rightValue':'80%','maxStep':'3','alarmLive':'3'}]";
			List<Map> tpl = JSONArray.parseArray(tpldata, Map.class);
			for (Map m : tpl) {
				Strategy strategy = new Strategy();
				strategy.setMetric(m.get("nm").toString());
				strategy.setFunc("");
				String op=m.get("op").toString();
				strategy.setOp(op.replace("&gt;",">"));
				strategy.setRightValue(m.get("rightValue").toString());
				strategy.setMaxStep(Long.parseLong(m.get("maxStep").toString()));
				strategy.setPriority(Integer.parseInt(m.get("alarmLive").toString()));
				strategy.setTags("");
				strategy.setNote(m.get("note").toString());
				strategy.setRunBegin("");
				strategy.setRunEnd("");
				strategy.setTplId(template.getId());
				strategyMapper.insert(strategy);
			}
		}
		int teamType = resourcesMapper.getTeamCountByType("2");
		if (teamType == 0) {
			Team team = new Team();
			team.setName("访问代理告警组");
			team.setResume("");
			team.setType("2");
			team.setCreated(new Date());
			team.setCreatorUser(userId);
			teamMapper.addTeam(team);
			Action action = new Action();
			action.setUic("访问代理告警组");
			action.setUrl("");
			actionMapper.insert(action);
			templateMapper.updateActionIdById(action.getId(), template.getId());
		}
	}

	public void findPlatform(String userId) {
		int sysType6 = resourcesMapper.getCountByType("6");
		Group group = new Group();
		if (sysType6 == 0) {
			group.setGrpName("平台节点");
			group.setCreateUser(userId);
			group.setComeFrom("1");
			group.setGrpType("6");
			groupMapper.insert(group);
		}
		int tplType = resourcesMapper.getTplCountByType("6");
		Template template = new Template();
		if (tplType == 0) {
			template.setTplName("平台节点策略");
			template.setType("6");
			template.setCreateUser(userId);
			resourcesMapper.insert(template);
			groupTemplateMapper.insert(group.getId(), template.getId(), userId);
			String tpldata = "[{'nm':'cpu','note':'CPU','op':'>=','rightValue':'80%','maxStep':'3','alarmLive':'3'},{'nm':'memory','note':'内存','op':'>=','rightValue':'80%','maxStep':'3','alarmLive':'3'}]";
			List<Map> tpl = JSONArray.parseArray(tpldata, Map.class);
			for (Map m : tpl) {
				Strategy strategy = new Strategy();
				strategy.setMetric(m.get("nm").toString());
				strategy.setFunc("");
				String op=m.get("op").toString();
				strategy.setOp(op.replace("&gt;",">"));
				strategy.setRightValue(m.get("rightValue").toString());
				strategy.setMaxStep(Long.parseLong(m.get("maxStep").toString()));
				strategy.setPriority(Integer.parseInt(m.get("alarmLive").toString()));
				strategy.setTags("");
				strategy.setNote(m.get("note").toString());
				strategy.setRunBegin("");
				strategy.setRunEnd("");
				strategy.setTplId(template.getId());
				strategyMapper.insert(strategy);
			}
		}
		int teamType = resourcesMapper.getTeamCountByType("6");
		if (teamType == 0) {
			Team team = new Team();
			team.setName("平台告警组");
			team.setResume("");
			team.setType("6");
			team.setCreated(new Date());
			team.setCreatorUser(userId);
			teamMapper.addTeam(team);
			Action action = new Action();
			action.setUic("平台告警组");
			action.setUrl("");
			actionMapper.insert(action);
			templateMapper.updateActionIdById(action.getId(), template.getId());
		}
	}

	public void save(String userId, String tplData, Group group, String users) {
		Long id = group.getId();
		group.setCreateUser(userId);
		groupMapper.update(group);
		List<TemplateEditDTO> tpl = templateMapper.findDtoByGroupId(id);
		Group group1 = groupMapper.findById(id);
		Team team = resourcesMapper.findTeamByType(group1.GetGrpType());
		resourcesMapper.deleteById(tpl.get(0).getId());
		teamMapper.deleteMember(Integer.toString(team.getId()));
		if (StringUtil.isNotNull(tplData) && JSONArray.parseArray(tplData).size() > 0) {
			List<Map> tpldata = JSONArray.parseArray(tplData, Map.class);
			for (Map m : tpldata) {
				Strategy strategy = new Strategy();
				strategy.setMetric(m.get("nm").toString());
				strategy.setFunc("");
				String op=m.get("op").toString();
				strategy.setOp(op.replace("&gt;",">"));
				strategy.setRightValue(m.get("rightValue").toString());
				strategy.setMaxStep(Long.parseLong(m.get("maxStep").toString()));
				strategy.setPriority(Integer.parseInt(m.get("alarmLive").toString()));
				strategy.setTags("");
				strategy.setNote("");
				strategy.setRunBegin("");
				strategy.setRunEnd("");
				strategy.setTplId(tpl.get(0).getId());
				strategyMapper.insert(strategy);
			}
		}
		if (StringUtil.isNotNull(users) && JSONArray.parseArray(users).size() > 0) {
			List<Map> u = JSONArray.parseArray(users, Map.class);
			for (Map m : u) {
				if (StringUtil.isNotNull(m.get("userId").toString())) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("tId", team.getId());
					params.put("userId", m.get("userId").toString());
					teamMapper.addMember(params);
				}
			}
		}
	}
}
