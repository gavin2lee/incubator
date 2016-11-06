/**
 * Project Name:ob-paasos-web
 * File Name:AbstractK8Command.java
 * Package Name:com.harmazing.openbridge.paasos.imgbuild
 * Date:2016年4月26日上午9:39:57
 * Copyright (c) 2016
 *
 */

package com.harmazing.openbridge.paasos.kubectl;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.EmptyDirVolumeSource;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarSource;
import io.fabric8.kubernetes.api.model.ExecAction;
import io.fabric8.kubernetes.api.model.HTTPGetAction;
import io.fabric8.kubernetes.api.model.Handler;
import io.fabric8.kubernetes.api.model.HostPathVolumeSource;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Lifecycle;
import io.fabric8.kubernetes.api.model.NFSVolumeSource;
import io.fabric8.kubernetes.api.model.ObjectFieldSelector;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerBuilder;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.TCPSocketAction;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.NumberUtil;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectProbes;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectProbesHandler;
import com.harmazing.openbridge.paas.util.StorageUtil;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.utils.KubernetesUtil;

/**
 * ClassName:AbstractK8Command <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年4月26日 上午9:39:57 <br/>
 * 
 * @author dengxq
 * @version
 * @since JDK 1.6
 * @see
 */
public abstract class AbstractK8Command {

	private Log logger = LogFactory.getLog(AbstractK8Command.class);

	public static  KubernetesClient getClient() {
		return KubernetesUtil.getKubernetesClient();
	}

	/**
	 * 
	 * createReplicationController:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author dengxq
	 * @param label
	 *            标签
	 * @param selector
	 *            pod选择器
	 * @param ports
	 *            PaasProjectDeployPort
	 * 
	 * @param envs
	 *            环境 json 数组 { key: 关键字， value: 值
	 * 
	 *            }
	 * 
	 * @param volumns
	 *            卷 json 数组 * { name: '名称', type : 类型, mount : 挂在点, capacity :
	 *            容量, outMount : 外部挂在点, server : 如果是nfs 这个有值 }
	 * @param name
	 * @since JDK 1.6
	 */
	public void createReplicationController(Map<String, String> rclabel,
			Map<String, String> podlabel, Map<String, String> selector,
			List<PaasProjectDeployPort> ports, List<PaasProjectDeployEnv> env, List<PaasProjectDeployVolumn> volumnList,
			String name, Integer replicas, String imageUrl, String nodeType,
			Map<String, String> nodeSelector, String tenant,String computerConfig,PaasOsImage poi,List<PaasProjectProbes> healths) {

		// 端口
		List<ContainerPort> extraPorts = new ArrayList<ContainerPort>();
		String networkType = null;
		if (ports != null && ports.size() > 0) {
			for (PaasProjectDeployPort p : ports) {
				if (networkType == null) {
					if (p.getNodePort() != null) {
						networkType = "NodePort";
					} else {
						networkType = "ClusterIP";
					}
				}
				ContainerPort cp = new ContainerPort();
				cp.setName(StringUtils.hasText(p.getPortKey())?p.getPortKey():p.getPortId());
				cp.setProtocol(p.getPortProtocol().toUpperCase().equals("HTTP")?"TCP":p.getPortProtocol().toUpperCase());
				cp.setContainerPort(p.getTargetPort());
			}
		}

		// 环境变量
		List<EnvVar> envs = new ArrayList<EnvVar>();
		String beishu = "0.8";
		if(env!=null && env.size()>0){
			for(PaasProjectDeployEnv e : env){
				EnvVar v = null;
				if(e.getKey().toUpperCase().equals("MEMORY_RATE")){
					beishu = e.getValue();
				}
				if(e.getKey().toUpperCase().equals("JAVA_ENV_OPTS")){
					String vs =e.getValue();
					vs = vs.replaceAll("-(?i)(xmx|xms)\\S+(?-i)", "");
					v = new EnvVar(e.getKey(), vs, null);
				}
				else{
					v = new EnvVar(e.getKey(), e.getValue(), null);
				}
				envs.add(v);
			}
		}
	
		EnvVar v = new EnvVar("CONTAINER_TYPE", nodeType, null);
		envs.add(v);

		EnvVarSource podIpVs = new EnvVarSource();
		ObjectFieldSelector podIpVsFs = new ObjectFieldSelector();
		podIpVsFs.setFieldPath("status.podIP");
		podIpVs.setFieldRef(podIpVsFs);
		EnvVar podIp = new EnvVar("PODIP", null, podIpVs);
		envs.add(podIp);
		
		EnvVarSource podNameVs = new EnvVarSource();
		ObjectFieldSelector podNameVsFs = new ObjectFieldSelector();
		podNameVsFs.setFieldPath("metadata.name");
		podNameVs.setFieldRef(podNameVsFs);
		EnvVar podName = new EnvVar("PODNAME", null, podNameVs);
		envs.add(podName);
		
		EnvVarSource nsVs = new EnvVarSource();
		ObjectFieldSelector nsVsFs = new ObjectFieldSelector();
		nsVsFs.setFieldPath("metadata.namespace");
		nsVs.setFieldRef(nsVsFs);
		EnvVar ns = new EnvVar("NAMESPACE", null, nsVs);
		envs.add(ns);
		
		// 卷
		List<VolumeMount> volumns = new ArrayList<VolumeMount>();
		List<Volume> vs = new ArrayList<Volume>();
		if (volumnList != null && volumnList.size()!=0) {
			for (PaasProjectDeployVolumn dv : volumnList) {
//				String vname = j.get("name") + "";
//				String type = j.get("type") + "";
//				String mount = j.get("mount") + "";
//				String capacity = j.get("capacity") + "";
//				String outMount = j.get("outMount") + "";
//				String server = j.get("server") + "";
				if(StringUtils.isEmpty(dv.getName())){
					dv.setName(dv.getId());
				}
				
				VolumeMount vm = new VolumeMount(dv.getMount(), dv.getName(), false);
				volumns.add(vm);

				Volume ve = new Volume();
				ve.setName(dv.getName());
				if ("emptydir".equals(dv.getType().toLowerCase())) {
					EmptyDirVolumeSource vd = new EmptyDirVolumeSource();
					ve.setEmptyDir(vd);
				} else if ("hostpath".equals(dv.getType().toLowerCase())) {
//					HostPathVolumeSource hv = new HostPathVolumeSource();
//					hv.setPath(outMount);
//					ve.setHostPath(hv);
				} else if ("nfs".equals(dv.getType().toLowerCase())) {
					NFSVolumeSource nfs = new NFSVolumeSource();
					String allocation = dv.getAllocateContent();
//					JSONObject obj = JSONObject.parseObject(allocation);
//					String[] path = obj.getString("export_location").split(":");
					String[] path = allocation.split(":");
					nfs.setPath(path[1]);
					nfs.setReadOnly(false);
					nfs.setServer(path[0]);
					ve.setNfs(nfs);
				} else if ("iscsi".equals(dv.getType())) {
					// TODO
					// ISCSIVolumeSource is = new ISCSIVolumeSource();
					// String fsType = j.get("fsType")+"";
					// String iqn = j.get("iqn")+"";
					// String lun = j.get("lun")+"";
					// String targetPortal = j.get("targetPortal")+"";
					//
					// ve.setIscsi(iscsi);
				}

				vs.add(ve);
			}
		}
		if (vs.size() == 0) {
			Volume ve = new Volume();
			ve.setName(name);
			EmptyDirVolumeSource vd = new EmptyDirVolumeSource();
			ve.setEmptyDir(vd);
			vs.add(ve);
		}
		Volume[] vss = new Volume[vs.size()];
		int i = 0;
		for (Volume v1 : vs) {
			vss[i] = v1;
			i++;
		}
		JSONObject cf = JSONObject.parseObject(computerConfig);
		String cpu = cf.getString("cpu");
		String memory = cf.getString("memory");
		memory = memory.toUpperCase()+"i";//G-->Gi M-->Mi
		ResourceRequirements r = new ResourceRequirements();
		Map<String, Quantity> r1 = new HashMap<String, Quantity>();
		r1.put("cpu", new Quantity(cpu));
		r1.put("memory", new Quantity(memory));
		r.setRequests(r1);
		
		Map<String, Quantity> r2 = new HashMap<String, Quantity>();
		Double maxCpu = new Double(cpu);
		maxCpu = maxCpu * 4;
		
		r2.put("cpu", new Quantity(NumberUtil.rounded(maxCpu, 2)));
		r2.put("memory", new Quantity(memory));
		r.setLimits(r2);
		
		String m = cf.getString("memory").toLowerCase();
		
		Double d = StorageUtil.getMemory(m);
		Double rate = new Double(beishu);
		d = d * rate;
		long m1 = Math.round(d);
		EnvVar v1 = new EnvVar("OS_MEMORY", m1+"m" , null);
		envs.add(v1);
		
		EnvVar v2 = new EnvVar("OS_CPU", cpu, null);
		envs.add(v2);
		
		//命令都是第一个
		List<String> cd = null;
		List<String> args = null;
		if(poi.getCommand()!=null && !poi.getCommand().trim().equals("")){
			String command = poi.getCommand().trim();
			String[] c = command.split("\\s+");
			cd=new ArrayList<String>();
			cd.add(c[0]);
			if(c.length>1){
				args = new ArrayList<String>();
				for(int n=1;n<c.length;n++){
					args.add(c[n]);
				}
			}
		}
		
//		
		Probe live = null;
		Probe read = null;
//		
		if(healths!=null && healths.size()>0){
			for(PaasProjectProbes p : healths){
				if(StringUtils.isEmpty(p.getType()) && (p.getHandlers()==null || p.getHandlers().size()==0)){
					//不可能
					continue ;
				}
				Probe tmp = null;
				if(p.getType().toLowerCase().equals("ReadinessProbe".toLowerCase())){
					read = new Probe();
					tmp = read;
				}
				else if(p.getType().toLowerCase().equals("LivenessProbe".toLowerCase())){
					live = new Probe();
					tmp = live;
				}
				if(tmp==null){
					continue ;
				}
				tmp.setTimeoutSeconds(Long.valueOf(p.getTimeoutSeconds()));
				tmp.setInitialDelaySeconds(Long.valueOf(p.getInitialDelaySeconds()));
				for(PaasProjectProbesHandler h : p.getHandlers()){
					if(StringUtils.isEmpty(h.getType())){
						//不可能
						continue ;
					}
					if(h.getType().toLowerCase().equals("httpGet".toLowerCase())){
						HTTPGetAction httpGet = new HTTPGetAction();
						httpGet.setPort(new IntOrString(Integer.valueOf(h.getPort())));
						httpGet.setPath(h.getPath());
						tmp.setHttpGet(httpGet);
					}
					else if(h.getType().toLowerCase().equals("tcpSocket".toLowerCase())){
						TCPSocketAction tcpSocket = new TCPSocketAction();
						tcpSocket.setPort(new IntOrString(Integer.valueOf(h.getPort())));
						tmp.setTcpSocket(tcpSocket);
					}
					else{
						//不管
					}
				}
				
			}
		}
		if(read!=null && logger.isDebugEnabled()){
			logger.debug(JSONObject.toJSONString(read));
		}
		if(live!=null && logger.isDebugEnabled()){
			logger.debug(JSONObject.toJSONString(live));
		}
//		
//		Lifecycle cycle = new Lifecycle();
//		Handler postStart = new Handler();
//		Handler preStop = new Handler();
////		HTTPGetAction getAction1 = new HTTPGetAction();
////		getAction1.setHost("192.168.31.22");
////		getAction1.setPath("/os/container/create.do");
////		getAction1.setPort(new IntOrString(8288));
//		
//		ExecAction exe = new ExecAction();
//		List<String> command = new ArrayList<String>();
//		command.add("curl");
//		command.add("http://192.168.31.22:8288/os/container/create.do?podName=$@");
//		exe.setCommand(command);
//		
////		getAction1.setScheme("HTTP");
//		postStart.setExec(exe);
//		
//		
////		HTTPGetAction getAction2 = new HTTPGetAction();
////		getAction2.setHost("192.168.31.22");
////		getAction2.setPath("/os/containers/delete.do");
////		getAction2.setPort(new IntOrString(8288));
//////		getAction2.setScheme("HTTP");
////		preStop.setHttpGet(getAction2);
//		
//		cycle.setPostStart(postStart);
////		cycle.setPreStop(preStop);
		ReplicationControllerBuilder builder = new ReplicationControllerBuilder()
				.withNewMetadata().withName(name).withNamespace(tenant)
				.addToLabels(rclabel).endMetadata().withNewSpec()
				.withReplicas(replicas).withSelector(selector)
				.withNewTemplate().withNewMetadata().addToLabels(podlabel)
				.endMetadata().withNewSpec().addNewContainer().withName(name)
				.withImage(imageUrl).withImagePullPolicy("Always")
				.withPorts(extraPorts).withEnv(envs).withVolumeMounts(volumns)
				.withWorkingDir(StringUtils.hasText(poi.getWorkdir())?poi.getWorkdir():null)
				.withReadinessProbe(read)
				.withLivenessProbe(live)
				.withCommand(cd)
				.withArgs(args)
				.withResources(r)
//				.withLifecycle(cycle)
				.endContainer().addToVolumes(vss).withRestartPolicy("Always")
				.withNodeSelector(nodeSelector).endSpec().endTemplate()
				.endSpec();

		ReplicationController rc = builder.build();
		logger.debug(rc.toString() + "  开始创建");
		getClient().replicationControllers().inNamespace(tenant).create(rc);

	}

	public void createService(String names, String tenant,
			Map<String, String> servicelabels, Map<String, String> podLabels,
			List<PaasProjectDeployPort> ports, String serviceIp) {
		
		String name = names;
		if(name.length()>24){
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
			name = "a"+sdf.format(new Date());
		}
		
		
		logger.debug(name+"  : "+name.length());
		// 端口
		List<ServicePort> extraPorts = new ArrayList<ServicePort>();
		String networkType = null;
		if (ports != null && ports.size() > 0) {
			for (PaasProjectDeployPort p : ports) {
				if (networkType == null) {
					if (p.getNodePort() != null) {
						networkType = "NodePort";
					} else {
						networkType = "ClusterIP";
					}
				}
				ServicePort cp = new ServicePort();
				cp.setName(StringUtils.hasText(p.getPortKey())?p.getPortKey():p.getPortId());
				cp.setProtocol(p.getPortProtocol().toUpperCase().equals("HTTP")?"TCP":p.getPortProtocol().toUpperCase());
				cp.setPort(p.getTargetPort());
				IntOrString ios = new IntOrString(p.getTargetPort());
				cp.setTargetPort(ios);
				if (p.getNodePort() != null) {
					cp.setNodePort(p.getNodePort());
				}
				extraPorts.add(cp);
			}
		}
		if (extraPorts.size() == 0) {
			//没有端口不需要创建service
			logger.debug("  没有端口不需要创建service");
			return ;
		}
		logger.debug("  开始创建service");
		getClient().services().inNamespace(tenant).createNew()
				.withNewMetadata().withName(name).withNamespace(tenant)
				.withLabels(servicelabels).endMetadata().withNewSpec()
				.withSelector(podLabels).withType(networkType)
				.withSessionAffinity("ClientIP")
				.withClusterIP(serviceIp).withPorts(extraPorts).endSpec()
				.done();
	}

	public void deleteReplicationController(String name, String tenant) {
		if (getClient().replicationControllers().inNamespace(tenant)
				.withName(name).get() != null) {
			logger.debug("-----" + name + " ----删除RC");
			getClient().replicationControllers().inNamespace(tenant)
					.withName(name).delete();
		}

	}

	public void deleteService(String name, String tenant) {
		
		
		Service service = getClient().services().inNamespace(tenant).withName(name).get();
		logger.debug(name+"(来自name)  "+(name==null?0:1));
		if (service!=null) {
			getClient().services().inNamespace(tenant).withName(name).delete();
		}
		ServiceList r = getClient().services().inNamespace(tenant).withLabel("deployCode", name).list();
		logger.debug(name+"(来自label)  "+ ((r==null || r.getItems()==null)? 0:r.getItems().size()));
		if(r!=null && r.getItems()!=null && r.getItems().size()>0){
			logger.debug("-----" + name + " ----删除SERVICE with label");
			getClient().services().inNamespace(tenant).withLabel("deployCode", name).delete();
		}
	}

	public void saveReplicas(String name, String tenant, int scale) {
		getClient().replicationControllers().inNamespace(tenant).withName(name)
				.scale(scale, true);
	}

	public void updateRC(Map<String, String> rclabel,
			Map<String, String> podlabel, Map<String, String> selector,
			List<PaasProjectDeployPort> ports, String envstr, List<PaasProjectDeployVolumn> volumnList,
			String name, Integer replicas, String imageUrl, String nodeType,
			Map<String, String> nodeSelector, String tenant){
		//现在只是修改卷
		ReplicationController rc = getClient().replicationControllers().inNamespace(tenant).withName(name).get();
		PodSpec podSpec = rc.getSpec().getTemplate().getSpec();
		// 卷
		List<VolumeMount> volumns = new ArrayList<VolumeMount>();
		List<Volume> vs = new ArrayList<Volume>();
		if (volumnList != null && volumnList.size()!=0) {
			for (PaasProjectDeployVolumn dv : volumnList) {
				if(StringUtils.isEmpty(dv.getName())){
					dv.setName(dv.getId());
				}
				VolumeMount vm = new VolumeMount(dv.getMount(), dv.getName(), false);
				volumns.add(vm);

				Volume ve = new Volume();
				ve.setName(dv.getName());
				if ("emptyDir".equals(dv.getType())) {
					EmptyDirVolumeSource vd = new EmptyDirVolumeSource();
					ve.setEmptyDir(vd);
				} else if ("hostPath".equals(dv.getType())) {
//					HostPathVolumeSource hv = new HostPathVolumeSource();
//					hv.setPath(outMount);
//					ve.setHostPath(hv);
				} else if ("nfs".equals(dv.getType())) {
					NFSVolumeSource nfs = new NFSVolumeSource();
					String allocation = dv.getAllocateContent();
					JSONObject obj = JSONObject.parseObject(allocation);
					String[] path = obj.getString("export_location").split(":");
					nfs.setPath(path[1]);
					nfs.setReadOnly(false);
					nfs.setServer(path[0]);
					ve.setNfs(nfs);
				} else if ("iscsi".equals(dv.getType())) {
					// TODO
					// ISCSIVolumeSource is = new ISCSIVolumeSource();
					// String fsType = j.get("fsType")+"";
					// String iqn = j.get("iqn")+"";
					// String lun = j.get("lun")+"";
					// String targetPortal = j.get("targetPortal")+"";
					//
					// ve.setIscsi(iscsi);
				}

				vs.add(ve);
			}
		}
		if (vs.size() == 0) {
			Volume ve = new Volume();
			ve.setName(name);
			EmptyDirVolumeSource vd = new EmptyDirVolumeSource();
			ve.setEmptyDir(vd);
			vs.add(ve);
		}
		Volume[] vss = new Volume[vs.size()];
		int i = 0;
		for (Volume v1 : vs) {
			vss[i] = v1;
			i++;
		}
		
		podSpec.setVolumes(vs);
		Container c =  podSpec.getContainers().get(0);
		c.setVolumeMounts(volumns);
		
		getClient().replicationControllers()
			.inNamespace(tenant).withName(name).cascading(false)
			.edit().editSpec().editTemplate().editSpec().withVolumes(vs).withContainers(c)
			.endSpec().endTemplate().endSpec().done();
	}
}
