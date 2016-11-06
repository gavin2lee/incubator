package com.harmazing.openbridge.paasos.kubectl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import io.fabric8.kubernetes.api.model.DoneableNode;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.NodeCondition;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.NodeStatus;
import io.fabric8.kubernetes.api.model.NodeSystemInfo;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ClientMixedOperation;
import io.fabric8.kubernetes.client.dsl.ClientNonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.ClientResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.manager.model.PaaSBaseBuild;
import com.harmazing.openbridge.paasos.manager.model.PaaSNode;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenant;
import com.harmazing.openbridge.paasos.manager.service.IPaaSBaseBuildService;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;

public class K8RestApiUtil extends AbstractK8Command {

	
	public static final String TYPE = "type";
	public static final String ENVTYPE = "envtype";
	public static final String TENANT_ID = "tenantid";
	
//	public static final Map<String,String> nodeType = new HashMap<String,String>();
	
	static{
		//不用 放配置文件里面了
//		nodeType.put("runtime", "项目部署");
//		nodeType.put("mysql", "数据库");
//		nodeType.put("redis", "高速缓存");
//		nodeType.put("rabbitmq", "消息中间件");
	}
	
	public K8RestApiUtil(IPaaSTenantService paaSTenantService){
		this.paaSTenantService = paaSTenantService;
	}
	
	private IPaaSTenantService paaSTenantService;
	
	public IPaaSTenantService getPaaSTenantService() {
		return paaSTenantService;
	}

	private List<PaaSTenant> tenants;
	
	public PaaSTenant getTenants(String tenantId) {
		if(tenants==null) 	this.tenants = paaSTenantService.listBrief();
		Iterator<PaaSTenant> it = tenants.iterator();
		while (it.hasNext()) {
			PaaSTenant paaSTenant = (PaaSTenant) it.next();
			if(paaSTenant.getTenantId().equals(tenantId)){
				return paaSTenant;
			}
		}
		return null;
	}

	/**
	 * @author chenjinfan
	 * @Description 生成节点列表。
	 * @param nodes 数据库中nodes信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<PaaSNode> listNodes(List<PaaSNode> nodes ){
		if(nodes==null){
			nodes = new ArrayList<PaaSNode>();
		}
		KubernetesClient client = getClient();
		ClientNonNamespaceOperation opt = client.nodes();
		NodeList list =  (NodeList)opt.list();
		Iterator iterator = list.getItems().iterator();
//		Map<String, Object> podsMap = getAllNodePods();
		while (iterator.hasNext()) {
			Node object = (Node) iterator.next();
			ObjectMeta meta = object.getMetadata();
			String name = meta.getName();
			PaaSNode paaSNode = findListNodeByName(nodes, name);
			if(paaSNode==null){
				paaSNode = new PaaSNode();
				nodes.add(paaSNode);
			}
			paaSNode = getNodeInfo(object, paaSNode);
		}
		return nodes;
	}

//	protected void getNodeInfo(Map<String, Object> podsMap, Node object,
//			ObjectMeta meta, String name, PaaSNode paaSNode) {
	protected PaaSNode getNodeInfo(Node object,  PaaSNode paaSNode) {
		if(paaSNode==null)	paaSNode = new PaaSNode();
		ObjectMeta meta = object.getMetadata();
		String name = meta.getName();
		paaSNode.setName(name);
		Map<String, String> labelsMap = meta.getLabels();
		String envType = labelsMap.get(ENVTYPE);
		if(StringUtil.isNotNull(envType)){
			paaSNode.setNodeEvnType(envType);
			paaSNode.setEnvType(envType);
		}
		String nodeType = labelsMap.get(TYPE);
		if(StringUtil.isNotNull(nodeType)){
			paaSNode.setNodeType(nodeType);
		}
		String tenantId = labelsMap.get(TENANT_ID);
		if(StringUtil.isNotNull(tenantId)){
			PaaSTenant tenant = getTenants(tenantId);
			paaSNode.setOrgId(tenantId);
			if(tenant!=null){
				paaSNode.setTenant(tenant);
			}
		}
		NodeStatus status = object.getStatus();
		Map<String, Quantity> cap = status.getCapacity();
		Quantity cpu = cap.get("cpu");
		Quantity memory = cap.get("memory");
		paaSNode.setCpu(cpu.getAmount());
		String memoryAmount = memory.getAmount();
		memoryAmount = memoryAmount.substring(0, memoryAmount.length()-2);
		paaSNode.setRam((Integer.valueOf(memoryAmount)/1024)+"");
		NodeSystemInfo nodeSystemInfo = status.getNodeInfo();
		paaSNode.setOperateSystem(nodeSystemInfo.getOsImage());
		Iterator<NodeAddress> i2 = status.getAddresses().iterator();
		while (i2.hasNext()) {
			NodeAddress adr = (NodeAddress) i2.next();
			if(adr.getType().equals("InternalIP")){
				paaSNode.setIp(adr.getAddress());
				break;
			}
		}
		Iterator<NodeCondition> i3 = status.getConditions().iterator();
		while (i3.hasNext()) {
			NodeCondition nodeCondition = (NodeCondition) i3.next();
			if(nodeCondition.getType().equals("Ready")){
				if("True".equals(nodeCondition.getStatus())){
					paaSNode.setStatus("在线");
				}else if("false".equalsIgnoreCase(nodeCondition.getStatus())){
					paaSNode.setStatus("离线");
				}else if("Unknown".equals(nodeCondition.getStatus())){
					paaSNode.setStatus("未知");
				}
			}
		}
		List<Pod> podList = getNodePods(name);
//		List podList = (List) podsMap.get(name);
		if(podList!=null){
			paaSNode.setInstanceCount(podList.size());
		}
		return paaSNode;
	}

	/**
	 * @author chenjinfan
	 * @Description 查找集合中 指定 name 的 节点
	 * @param nodes
	 * @param name
	 * @return
	 */
	protected PaaSNode findListNodeByName(List<PaaSNode> nodes, String name) {
		Iterator<PaaSNode> ni =  nodes.iterator();
		PaaSNode paaSNode = null;
		while (ni.hasNext()) {
			PaaSNode paaSNode1 = (PaaSNode) ni.next();
			if(paaSNode1.getName().equals(name)){
				paaSNode = paaSNode1;
				break;
			}
		}
		return paaSNode;
	}
	
	public Map<String, Object> getAllNodePods(){
		return getAllNodePods(null);
	}
	/**
	 * @author chenjinfan
	 * @Description 查找pods
	 * @param name 为空返回所有，不为空则返回相应节点的pods
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getAllNodePods(String name){
		Map<String, Object> map = new HashMap<String, Object>();
		ClientMixedOperation opt = getClient().pods();
		PodList pods = (PodList)opt.list();
		Iterator<Pod> iterator = pods.getItems().iterator();
		while (iterator.hasNext()) {
			Pod pod = (Pod) iterator.next();
			PodSpec spec = pod.getSpec();
			String nodeName = spec.getNodeName();
			List list;
			if(StringUtil.isNotNull(name) && !name.equals(nodeName)){
				continue;
			}
			if(map.get(nodeName)==null){
				list = new ArrayList<>();
				map.put(nodeName, list);
			}else{
				list = (List) map.get(nodeName);
			}
			list.add(pod);
//			spec.getContainers()
		}
		return map;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 获取节点pod
	 * @param nodeName
	 * @return
	 */
	public List<Pod> getNodePods(String nodeName){
		return getClient().pods().withField("spec.nodeName", nodeName).list().getItems();
	}
	
	/**
	 * @author chenjinfan
	 * @Description 设置节点标签
	 * @param nodeName
	 * @param labels
	 */
	public void setNodeLabels(String nodeName,Map<String, String> labels){
		checkArgument(!isNullOrEmpty(nodeName),"NodeName should not be null.");
		checkArgument(labels.size()>0,"labels should have items.");
		
		KubernetesClient client = getClient();
		/*client.nodes()
				.withName(nodeName)
				.edit()
				.editMetadata()
				.addToLabels(labels)
				.endMetadata();*/
		ClientResource<Node, DoneableNode> cr = client.nodes().withName(nodeName);
		Node node = cr.get();
		ObjectMeta meta = node.getMetadata();
		Map<String, String> labelsOrigin = meta .getLabels();
		labelsOrigin.putAll(labels);
		node.setMetadata(meta);
		cr.update(node);
//		opt.addToLabels(labels);
		
		/*NodeList list =  (NodeList)opt.list();
		Iterator iterator = list.getItems().iterator();
		Map<String, Object> podsMap = getAllNodePods();
		while (iterator.hasNext()) {
			Node object = (Node) iterator.next();
			ObjectMeta meta = object.getMetadata();
			String name = meta.getName();
			if(nodeName.equals(name)){
				Map<String, String> labelsOrigin = meta.getLabels();
				labelsOrigin.putAll(labels);
				object.setMetadata(meta);
				break;
			}
		}*/
	}

	/**
	 * @author chenjinfan
	 * @Description
	 * @param nodeName
	 * @return 
	 */
	public PaaSNode getNodeByName(String nodeName) {
		KubernetesClient client = getClient();
		Node node = client.nodes().withName(nodeName).get();
		return getNodeInfo(node, null);
	}
	
	/**
	 * @author chenjinfan
	 * @Description 查找某个组织（租户）的节点
	 * @param tenantId
	 * @return
	 */
	public List<PaaSNode> getTenantNodes(String tenantId){
		List<PaaSNode> list = new ArrayList<PaaSNode>();
		Iterator<Node> it =  getClient().nodes().withLabel(TENANT_ID, tenantId).list().getItems().iterator();
		while (it.hasNext()) {
			Node node = (Node) it.next();
			list.add(getNodeInfo(node, null));
		}
		return list;
	}
	
	
	public void saveNamespace(String namespace,String tenantName){
		Namespace namespaceObj = getClient().namespaces().withName(namespace).get();
		if(namespaceObj==null){
			namespaceObj = new Namespace();
			ObjectMeta objectMeta = new ObjectMeta();
			objectMeta.setName(namespace);
			Map<String, String> ann = new HashMap<String, String>();
			ann.put("tenantname", tenantName);
			objectMeta.setAnnotations(ann);
			namespaceObj.setMetadata(objectMeta);
			namespaceObj = getClient().namespaces().create(namespaceObj);
		}else{
			namespaceObj.getMetadata().setName(namespace);
			Map<String, String> ann = namespaceObj.getMetadata().getAnnotations();
			if(ann == null){
				ann = new HashMap<String, String>();
			}
			ann.put("tenantname", tenantName);
			namespaceObj.getMetadata().setAnnotations(ann);
			getClient().namespaces().withName(namespace).update(namespaceObj);
		}
	}
	
	
	/**
	 * @author chenjinfan
	 * @Description  获取某个组织的节点统计信息
	 * @param tenantId
	 * @return
	 */
	public static Map<String, Object> getTenantNodeStatistics(String tenantId){
		checkArgument(StringUtil.isNotNull(tenantId), "tenantId is not specified.");
		Map<String, Object> map = new HashMap<String, Object>();
		KubernetesClient client = getClient();
		NodeList list = client.nodes().list();
		Iterator<Node> iterator = list.getItems().iterator();
		int total = 0;
		int online = 0;
		int offline = 0;
		int unknown = 0;
		while (iterator.hasNext()) {
			Node object = (Node) iterator.next();
			ObjectMeta meta = object.getMetadata();
			Map<String, String> labelsMap = meta.getLabels();
			String tenantLabel = labelsMap.get(TENANT_ID);
			if(StringUtil.isNotNull(tenantLabel) && tenantId.equals(tenantLabel)){
				total ++;
				NodeStatus status = object.getStatus();
				Iterator<NodeCondition> i3 = status.getConditions().iterator();
				while (i3.hasNext()) {
					NodeCondition nodeCondition = (NodeCondition) i3.next();
					if(nodeCondition.getType().equals("Ready")){
						if("True".equals(nodeCondition.getStatus())){
							online++;
						}else if("false".equalsIgnoreCase(nodeCondition.getStatus())){
							offline++;
						}else if("Unknown".equals(nodeCondition.getStatus())){
							unknown++;
						}
					}
				}
			}
		}
		map.put("total", total);
		map.put("online", online);
		map.put("offline", offline);
		map.put("unknown", unknown);
		return map;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 获取某个组织的镜像统计信息
	 * @param tenantId
	 * @return
	 */
	public static Map<String, Object> getTenantImageStatistics(String tenantId){
		Map<String, Object> map = new HashMap<String, Object>();
		IPaaSBaseBuildService paasBaseBuildService = SpringUtil.getBean(IPaaSBaseBuildService.class);
		IPaaSTenantService paasTenantService = SpringUtil.getBean(IPaaSTenantService.class);
		List<PaaSBaseBuild> baseBuilds = paasBaseBuildService.getTenantBuild(tenantId);
//		int pub = paasBaseBuildService.getPublicImageCount();
		int available = baseBuilds.size();
		int userProjectImage = paasTenantService.getTenantImageCount(tenantId);
		map.put("total", available+available);
		map.put("base", available);
		map.put("preset", userProjectImage);
		return map;
	}

	/**
	* @Title: statisticData 
	* @author weiyujia
	* @Description: 统计节点相关信息
	* @param nodes 从Kubernetes 服务器收集的节点信息
	* @return Object    返回类型 
	* @date 2016年6月27日 下午4:27:23
	 */
	public static Map<String,Object> statisticData(List<PaaSNode> nodes) {
		if(nodes == null || nodes.size() == 0){
			return new HashMap<String,Object>();
		}
		Integer cpuCounts = 0;
		Integer menmoryCounts = 0;
		Integer instanceCount = 0;
		Map<String,Object> resultMap = new HashMap<String,Object>();
		for(PaaSNode paaSNode : nodes){
			cpuCounts += Integer.parseInt(paaSNode.getCpu());
			menmoryCounts += Integer.parseInt(paaSNode.getRam());
			instanceCount += paaSNode.getInstanceCount();
		}
		resultMap.put("cpuCounts", cpuCounts);
		resultMap.put("menmoryCounts", menmoryCounts);
		resultMap.put("instanceCount", instanceCount);
		return resultMap;
	}
	
	public static List<String> getNodesIp(){
		return getNodesIp(null);
	}
	public static List<String> getNodesIp(Map<String,String> labels){
		NodeList nl = null ;
		if(labels!=null){
			 nl = getClient().nodes().withLabels(labels).list();
		}
		else{
			nl = getClient().nodes().list();
		}
		if(nl==null || nl.getItems()==null || nl.getItems().size()==0){
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (io.fabric8.kubernetes.api.model.Node h : nl.getItems()) {
			if (h.getMetadata() == null) {
				continue;
			}
			if (h.getStatus() == null || h.getStatus() == null
					|| h.getStatus().getAddresses() == null
					|| h.getStatus().getAddresses().size() == 0) {
				continue;
			}
			List<NodeAddress> addresses = h.getStatus().getAddresses();
			String ip = null;
			for (NodeAddress a : addresses) {
				if ("InternalIP".equals(a.getType())) {
					ip = a.getAddress();
					result.add(ip);
					break;
				}
			}
		}
		return result;
	}
}
