package com.harmazing.openbridge.monitor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.Host;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateEditDTO;
import com.harmazing.openbridge.alarm.service.IGroupService;
import com.harmazing.openbridge.alarm.service.IHostService;
import com.harmazing.openbridge.alarm.service.ITeamService;
import com.harmazing.openbridge.alarm.service.ITemplateService;
import com.harmazing.openbridge.monitor.model.vo.FalconVo;
import com.harmazing.openbridge.monitor.model.vo.MonitorForm;
import com.harmazing.openbridge.monitor.model.vo.PaaSNode;
import com.harmazing.openbridge.monitor.service.IMonitorResourcesService;
import com.harmazing.openbridge.util.FalconUtil;
import com.harmazing.openbridge.util.KubernetesUtil;

import io.fabric8.kubernetes.api.model.NodeStatus;
import io.fabric8.kubernetes.api.model.Quantity;

@Controller
@RequestMapping("/monitor/resources")
public class MonitorResourcesController extends AbstractController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private IMonitorResourcesService monitorResourcesService;
	@Autowired
	private IHostService hostService;
	   @Autowired
	    private IGroupService groupService;
	    @Autowired
	    private ITemplateService templateService;
	    @Autowired
	    private ITeamService teamService;
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			List<GroupIndexDTO> groups = new ArrayList<GroupIndexDTO>();
			
			monitorResourcesService.findNodeByType(user.getUserId(),"1","runtime","部署节点组","部署策略","部署告警组");
			monitorResourcesService.findNodeByType(user.getUserId(),"3","mysql","MySQL节点","MySQL策略","MySQL告警组");
			monitorResourcesService.findNodeByType(user.getUserId(),"4","rabbitmq","MQ节点","MQ策略","MQ告警组");
			monitorResourcesService.findNodeByType(user.getUserId(),"5","redis","缓存节点","缓存策略","缓存告警组");
			
			
			monitorResourcesService.findNginx(user.getUserId());
			monitorResourcesService.findPlatform(user.getUserId());
			// groups = groupService.findDTO(user.getUserId());
			groups = monitorResourcesService.findTypeDTO();
			request.setAttribute("groups", groups);
			return getUrlPrefix() + "/index";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/node/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		List<PaaSNode> nodes = new ArrayList<PaaSNode>();
		String id = request.getParameter("id");
		List<Host> host = new ArrayList<Host>();
		host = hostService.findByGroupId(Long.parseLong(id));
		if (host != null) {
			for (int i = 0; i < host.size(); i++) {
				io.fabric8.kubernetes.api.model.NodeList nl = KubernetesUtil.getNodes();
				if (nl == null) {
					return null;
				}
				for (io.fabric8.kubernetes.api.model.Node h : nl.getItems()) {
					if (h.getMetadata() == null) {
						continue;
					}
					if (h.getMetadata().getName().equals(host.get(i).getIp())) {
						PaaSNode n = new PaaSNode();
						NodeStatus status = h.getStatus();
						Map<String, Quantity> cap = status.getCapacity();
						Quantity cpu = cap.get("cpu");
						Quantity memory = cap.get("memory");
						String memoryAmount = memory.getAmount();
						memoryAmount = memoryAmount.substring(0, memoryAmount.length() - 2);
						n.setRam((Integer.valueOf(memoryAmount) / 1024) + "");
						n.setCpu(cpu.getAmount());
						n.setIp(h.getMetadata().getName());
						n.setName(h.getMetadata().getName());
						nodes.add(n);
					}
				}
			}
		}
		request.setAttribute("nodes", nodes);
		return getUrlPrefix() + "/node/list";
	}

	@RequestMapping("/last/data")
	@ResponseBody
	public JsonResponse lastData(MonitorForm form) {
		try {
			String data = FalconUtil.graphLastQuery(form.getEndpointCounters());
			logger.debug(data);
			List<FalconVo> rs = JSONArray.parseArray(data, FalconVo.class);
			return JsonResponse.success(rs);
		} catch (Exception e) {
			logger.error("获取数据失败", e);
			return JsonResponse.failure(500, "获取最新数据失败");
		}
	}
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String  id=request.getParameter("id");
			request.setAttribute("hosts", hostService.findAll());
			request.setAttribute("group", groupService.findById(Long.parseLong(id)));
			request.setAttribute("groupHosts", hostService.findByGroupId(Long.parseLong(id)));
			List<TemplateEditDTO> tem=templateService.findDtoByGroupId(Long.parseLong(id));
			request.setAttribute("templates", tem);
			Group group=groupService.findById(Long.parseLong(id));
			Team team=monitorResourcesService.findTeamByType(group.GetGrpType());
			team = teamService.get(Integer.toString(team.getId()));
			request.setAttribute("team", team);
			return getUrlPrefix() + "/edit";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	    @RequestMapping("/save")
		@ResponseBody
		public JsonResponse save(HttpServletRequest request,
				HttpServletResponse response) {
			try {
				IUser user = WebUtil.getUserByRequest(request);
				String data = request.getParameter("data");
				String users=request.getParameter("users");
				String groupName=request.getParameter("groupName");
				String  id=request.getParameter("id");
				Group group = new Group();
				group.setComeFrom("1");
				group.setGrpName(groupName);
				if(StringUtil.isNotNull(id)){
					group.setId(Long.parseLong(id));
				}
				monitorResourcesService.save(user.getUserId(),data,group,users);
				return JsonResponse.success();
			} catch (Exception e) {
				logger.error("新增节点组出错", e);
				return JsonResponse.failure(500, e.getMessage());
			}
		}
	
}
