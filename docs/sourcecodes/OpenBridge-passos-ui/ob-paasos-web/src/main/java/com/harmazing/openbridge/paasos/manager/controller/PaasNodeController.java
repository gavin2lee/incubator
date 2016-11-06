package com.harmazing.openbridge.paasos.manager.controller;

import io.fabric8.kubernetes.api.model.ContainerState;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.kubectl.K8RestApiUtil;
import com.harmazing.openbridge.paasos.manager.model.PaaSNode;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenant;
import com.harmazing.openbridge.paasos.manager.model.PodInstances;
import com.harmazing.openbridge.paasos.manager.service.IPaaSNodeService;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;
import com.harmazing.openbridge.paasos.utils.ConfigUtils;
import com.harmazing.openbridge.paasos.utils.KubernetesUtil;

@Controller
@RequestMapping("/manager/node")
public class PaasNodeController extends AbstractController {
	public static Map<String, Map<String, Object>> node_types = new HashMap<String, Map<String, Object>>();
	static{
		Map<String, Object> map = new HashMap<String, Object>();
		map = new HashMap<String, Object>();
		map.put("name", "runtime");
		map.put("code", "runtime");
		node_types.put("runtime",map);
		
		map = new HashMap<String, Object>();
		map.put("name", "mysql");
		map.put("code", "mysql");
		node_types.put("mysql",map);
		
		map = new HashMap<String, Object>();
		map.put("name", "rabbitmq");
		map.put("code", "rabbitmq");
		node_types.put("rabbitmq",map);
		
		map = new HashMap<String, Object>();
		map.put("name", "redis");
		map.put("code", "redis");
		node_types.put("redis",map);
	}
	@Resource
	private IPaaSTenantService paaSTenantService;
	@Resource
	private IPaaSNodeService paaSNodeService;
	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
//		List<PaaSNode> nodes = paaSNodeService.list();
		List<PaaSNode> nodes = null;
		K8RestApiUtil query = new K8RestApiUtil(paaSTenantService);
		nodes = query.listNodes(nodes);
		/**
		 * update by weiyujia @2016/6/27 
		 * 统计节点信息
		 */
		request.setAttribute("countMaps", K8RestApiUtil.statisticData(nodes));
		request.setAttribute("nodes", nodes);
		request.setAttribute("tenants", paaSTenantService.list());
		request.setAttribute("envs", ConfigUtils.getEnvTypes());
		request.setAttribute("node_types", node_types);
		request.setAttribute("installScript", paaSTenantService.getInstallScript());
		return getUrlPrefix() + "/list";
	}
	
	@RequestMapping("/add_node")
	public String addNode(HttpServletRequest request, HttpServletResponse response,String id) {
		request.setAttribute("envs", ConfigUtils.getEnvTypes());
		request.setAttribute("tenants", paaSTenantService.list());
		PaaSNode node= null;
		if(StringUtil.isNotNull(id)){
			node = paaSNodeService.getById(id);
		}
		request.setAttribute("node", node);
		return getUrlPrefix() + "/add_node";
	}
	@RequestMapping("detail")
	public String detail(String name,String tab,HttpServletRequest request){
		if(StringUtil.isNotNull(name)){
			if(StringUtil.isNotNull(tab) && tab.equals("instance")){
				instanceList(name,request);
			}
			
			PaaSNode node= null;
			K8RestApiUtil query = new K8RestApiUtil(paaSTenantService);
			node = query.getNodeByName(name);
			String env = ConfigUtils.getEnvTypes().get(node.getEnvType());
			if(env!=null){
				node.setEnvType(env);
			}
			PaaSTenant tenant = paaSTenantService.get(node.getOrgId());
			request.setAttribute("tenant", tenant);
			request.setAttribute("node", node);
		}
		return getUrlPrefix() + "/nodeDetail";
	}
	
	protected void instanceList(String name,HttpServletRequest reqeust){
		K8RestApiUtil util = new K8RestApiUtil(paaSTenantService);
		List<Pod> pods = util.getNodePods(name);
		Iterator<Pod> it = pods.iterator();
		List<PodInstances> res = new ArrayList<PodInstances>();
		while (it.hasNext()) {
			Pod pod = (Pod) it.next();
			String podName = pod.getMetadata().getName();
			Iterator<ContainerStatus> it2 = pod.getStatus().getContainerStatuses().iterator();
			PodInstances instance = null;
			String hostIp  = pod.getStatus().getPodIP();
			String namespace = pod.getMetadata().getNamespace();
			String statusStr = pod.getStatus().getPhase()+"/"; 
			while (it2.hasNext()) {
				ContainerStatus containerStatus = (ContainerStatus) it2.next();
//				podName += "/" + containerStatus.getName();
				if(containerStatus!=null){
					String deployName = containerStatus.getImage();
					String startTime = null;
					try {
						startTime = containerStatus.getState().getRunning().getStartedAt();
						if(StringUtil.isNotNull(startTime)){
							startTime = startTime.replace("T", " ").replace("Z", "");
						}
					} catch (NullPointerException e) {
						
					}
					String deployVersion = "";
					ContainerState status =  containerStatus.getState();
					if(status.getRunning()!=null){
						statusStr += "Running";
					}else if(status.getTerminated()!=null){
						statusStr += "Terminated";
					}else if(status.getWaiting()!=null){
						statusStr += "Waiting";
					}
					instance = new PodInstances(podName, deployName, deployVersion, 
							startTime,hostIp,statusStr,namespace,containerStatus.getName());
				}
			}
			if(instance==null){
				instance = new PodInstances(podName, hostIp);
				instance.setName(namespace);
				instance.setStatus(statusStr);
			}
			res.add(instance);
			
		}
		reqeust.setAttribute("pods", res);
	}
	
	@RequestMapping("getInstLog")
	@ResponseBody
	public JsonResponse getInstLog(String name,String namespace){
		Map<String, Object> map  = new HashMap<String, Object>();
		String log = "";
		if(StringUtil.isNotNull(name)){
			name = name.split("/")[0];
			K8RestApiUtil util = new K8RestApiUtil(paaSTenantService);
			try {
				log = util.getClient().inNamespace(namespace).pods().withName(name).getLog();
			} catch (Exception e) {
				logger.debug(e);
			}
		}
		map.put("log", log);
		
		return JsonResponse.success(map);
	}
	@RequestMapping("getPodEvent")
	@ResponseBody
	public JsonResponse getPodEvent(String podName){
		try {
			return JsonResponse.success(KubernetesUtil.getPodEventLogs(podName));
		} catch (Exception e) {
			return JsonResponse.failure(500, "");
		}
	}
	
	@RequestMapping(value="saveOrUpdate",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse saveOrUpdate(PaaSNode node){
		JsonResponse jsonResponse = JsonResponse.success();
		try {
			if(node!=null){
//				paaSNodeService.saveOrUpdate(node);
				K8RestApiUtil util = new K8RestApiUtil(paaSTenantService);
				Map<String,String> labelMap = new HashMap<String,String>();
				labelMap.put(K8RestApiUtil.ENVTYPE, node.getEnvType());
				labelMap.put(K8RestApiUtil.TENANT_ID, node.getOrgId());
				labelMap.put(K8RestApiUtil.TYPE, node.getNodeType());
				util.setNodeLabels(node.getName(),labelMap);
			}
		} catch (Exception e) {
			logger.debug(e);
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
	
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse delete(String id){
		JsonResponse jsonResponse = JsonResponse.success();
		try {
			paaSNodeService.delete(id);
		} catch (Exception e) {
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
}
