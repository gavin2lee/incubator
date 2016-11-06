/**
 * Project Name:ob-paasos-web
 * File Name:PaasProjectDeployServiceImpl.java
 * Package Name:com.harmazing.openbridge.paasos.project.service.impl
 * Date:2016年4月22日下午9:43:19
 * Copyright (c) 2016
 *
 */

package com.harmazing.openbridge.paasos.project.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.NumberUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paasos.kubectl.K8RestApiUtil;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectDeployEnvMapper;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectDeployMapper;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectDeployPortMapper;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectDeployVolumnMapper;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.model.PaasProjectBuild;
import com.harmazing.openbridge.paasos.project.model.vo.TwoTuple;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectBuildService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;
import com.harmazing.openbridge.paasos.resource.util.ResourceQuotaUtil;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;
import com.harmazing.openbridge.paasos.store.service.IPaasStoreAppService;
import com.harmazing.openbridge.paasos.utils.KubernetesUtil;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;

/**
 * ClassName:PaasProjectDeployServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年4月22日 下午9:43:19 <br/>
 * 
 * @author dengxq
 * @version
 * @since JDK 1.6
 * @see
 */
@Service
public class PaasProjectDeployServiceImpl implements IPaasProjectDeployService {

	private static Log logger = LogFactory
			.getLog(PaasProjectDeployServiceImpl.class);

	@Autowired
	private PaasProjectDeployMapper paasProjectDeployMapper;

	@Autowired
	private IPaasProjectBuildService iPaasProjectBuildService;

	@Autowired
	private PaasProjectDeployPortMapper paasProjectDeployPortMapper;
	
	@Autowired
	private PaasProjectDeployVolumnMapper paasProjectDeployVolumnMapper;
	
	@Autowired
	private PaasProjectDeployEnvMapper paasProjectDeployEnvMapper;

	@Autowired
	private IPaasProjectService iPaasProjectService;
	
	@Resource
	private IPaasStoreAppService paasStoreAppService;
	
	@Autowired
	private IPaasProjectEnvService iPaasProjectEnvService;
	
	@Autowired
	private IPaaSTenantService iPaaSTenantService;
	
	private final static Object PORT_LOCK = new Object();
	
	private Map<String,Object> lock = Collections.synchronizedMap(new HashMap<String,Object>());

	@Transactional
	@Override
	public String save(IUser user, PaasProjectDeploy deploy) {
		// 估计没有update方法了
		boolean isUpdate = true;
		
		PaasProject project = null;
		if(StringUtils.hasText(deploy.getBusinessId())){
			//api 和app id 从api和app过来的请求  没有projectId
			project = iPaasProjectService.getPaasProjectByBusinessIdAndType(deploy.getBusinessId(), deploy.getProjectType());
			if(project==null){
				throw new RuntimeException("该业务没有在paasos中生成项目");
			}
			deploy.setProjectId(project.getProjectId());
		}
		else{
			project = iPaasProjectService.getPaasProjectInfoById(deploy.getProjectId());
		}
		
		if (StringUtils.isEmpty(deploy.getDeployId())) {
			deploy.setDeployId(StringUtil.getUUID());
			deploy.setStatus(1);
			isUpdate = false;
			if(!StringUtils.hasText(deploy.getExtraKey())){
				deploy.setExtraKey("application");
			}
			if(StringUtil.isEmpty(deploy.getDeployCode())){
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
				String t = sdf.format(new Date());
				t = Long.toString(Long.parseLong(t),32);
				
				deploy.setDeployCode(project.getProjectCode() + t);
			}
			deploy.setTenantId(project.getTenantId());
			
			if(deploy.getEnvVariable()==null){
				deploy.setEnvVariable(new ArrayList<PaasProjectDeployEnv>());
			}
			PaasProjectDeployEnv v1 = new PaasProjectDeployEnv();
			v1.setKey("PROJECT_CODE");
			v1.setValue(project.getProjectCode());
			
			PaasProjectDeployEnv v2 = new PaasProjectDeployEnv();
			v2.setKey("DEPLOY_CODE");
			v2.setValue(deploy.getDeployCode());
			
			deploy.getEnvVariable().add(v1);
			deploy.getEnvVariable().add(v2);
			
			if (!StringUtils.hasText(deploy.getDeployName())) {
				throw new RuntimeException("部署名称不能为空");
			}
			
			if(deploy.getDeployCode().length()>64){
				throw new RuntimeException(deploy.getDeployCode()+"已经超过64个字符,请检查项目编码");
			}
			if("store".equals(project.getProjectType())){
				PaasStoreApp storeApp = paasStoreAppService.getById(project.getBusinessId());
				if(storeApp!=null){
					if(StringUtil.isNull(storeApp.getImageId())){
						throw new RuntimeException("预置应用没有构建镜像！");
					}
					deploy.setImageId(storeApp.getImageId());
					String portsJson = storeApp.getPorts();
					if(StringUtil.isNotNull(portsJson)){
						JSONArray array = JSON.parseArray(portsJson);
						Iterator<Object> ite = array.iterator();
						List<PaasProjectDeployPort> ports = new ArrayList<PaasProjectDeployPort>();
						while (ite.hasNext()) {
							JSONObject portObj = (JSONObject) ite.next();
							int number = portObj.getInteger("portNo");
							String type = portObj.getString("portType");
							String desc = portObj.getString("portDesc");
							PaasProjectDeployPort deployPort = new PaasProjectDeployPort();
							deployPort.setPortProtocol(type);
							deployPort.setPortRemark(desc);
							deployPort.setTargetPort(number);
							deployPort.setPortKey(deploy.getDeployCode()+"-"+number);
							ports.add(deployPort);
						}
						deploy.setPorts(ports );
					}
				}
			}
		}
		if(StringUtils.hasText(deploy.getBuildId())){
			PaasProjectBuild b = iPaasProjectBuildService.findById(deploy.getBuildId());
			deploy.setImageId(b.getImageId());
		}
		if (StringUtils.isEmpty(deploy.getCreateUser())) {
			deploy.setCreateUser(user.getUserId());
		}
		if (deploy.getCreateTime() == null) {
			deploy.setCreateTime(new Date());
		}

		JSONObject cf = JSONObject.parseObject(deploy.getComputeConfig());
		String replicas = cf.getString("replicas");
		deploy.setReplicas(Integer.valueOf(StringUtils.hasText(replicas) ? replicas: "0"));
		
		String cpu = cf.getString("cpu");
		deploy.setCpu(Double.parseDouble(cpu));
		
		String memory = cf.getString("memory");
		memory = memory.toUpperCase();//G-->Gi M-->Mi
		Double rm = Double.parseDouble(memory.substring(0, memory.length()-1));
		if(memory.endsWith("G")){
			deploy.setMemory(Double.parseDouble(NumberUtil.rounded(rm*1024,2)));
		}
		else if(memory.endsWith("M")){
			deploy.setMemory(rm);
		}
		else{
			throw new RuntimeException("内存格式只支持M和G");
		}
		
		ResourceQuota apply = new ResourceQuota();
		apply.setCpu(deploy.getCpu()*deploy.getReplicas());
		apply.setMemory(deploy.getMemory() * deploy.getReplicas());
		apply.setStorage(0.0);
		apply.setCount(Long.valueOf(deploy.getReplicas()));
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tenantId", project.getTenantId());
		//不算上本次的
		param.put("deployId", deploy.getDeployId());
		ResourceQuota used = paasProjectDeployMapper.getAlreadyUsed(param);
		TwoTuple<Boolean, String> result = ResourceQuotaUtil.isSatisfy(apply, used, null, false, 
				deploy.getEnvType(), project.getTenantId(), "deploy","docker");
		if(!result.a){
			throw new RuntimeException("租户配额不足:"+result.b);
		}
		
		
		
		
		
		if (isUpdate) {
			PaasProjectDeploy old = paasProjectDeployMapper.findById(deploy.getDeployId());
			if (old.getStatus() != null && old.getStatus().intValue() != 1
					&& old.getStatus().intValue() != 0) {
				throw new RuntimeException("构建不处于未部署、部署失败状态,不能修改");
			}
			if (StringUtils.isEmpty(deploy.getModifyUser())) {
				deploy.setModifyUser(user.getUserId());
			}
			if (deploy.getModifyTime() == null) {
				deploy.setModifyTime(new Date());
			}	
			deploy.setTenantId(user.getTenantId());
			paasProjectDeployMapper.update(deploy);
		} else {
			deploy.setServiceIp(getServiceIp());
			paasProjectDeployMapper.create(deploy);
		}

//		设置
//		hasMoreResource(deploy.getEnvType(), project.getTenantId(), true);
		
		paasProjectDeployPortMapper.deleteByDeployId(deploy.getDeployId());
		
		synchronized(PORT_LOCK){
			if (deploy.getPorts() != null && deploy.getPorts().size() > 0) {
				Integer m = getUnUsedPort();
				for (PaasProjectDeployPort p : deploy.getPorts()) {
					p.setDeployId(deploy.getDeployId());
					p.setPortId(StringUtil.getUUID());
					
					if(StringUtils.isEmpty(p.getNodePort())){
						p.setNodePort(++m);
					}
				}
				paasProjectDeployPortMapper.batchSave(deploy.getPorts());
			}
		}
		
		
		
		paasProjectDeployVolumnMapper.deleteByDeployId(deploy.getDeployId());
		if(deploy.getVolumn()!=null && deploy.getVolumn().size()>0){
			for(PaasProjectDeployVolumn v : deploy.getVolumn()){
				v.setId(StringUtil.getUUID());
				v.setDeployId(deploy.getDeployId());
			}
			paasProjectDeployVolumnMapper.batchSave(deploy.getVolumn());
		}
		
		paasProjectDeployEnvMapper.deleteByDeployId(deploy.getDeployId());
		if(deploy.getEnvVariable()!=null && deploy.getEnvVariable().size()>0){
			for(PaasProjectDeployEnv e : deploy.getEnvVariable()){
				e.setId(StringUtil.getUUID());
				e.setDeployId(deploy.getDeployId());
			}
			paasProjectDeployEnvMapper.batchSave(deploy.getEnvVariable());
		}
		return deploy.getDeployId();
	}

	@Override
	public Page<Map<String, Object>> page(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(paasProjectDeployMapper.pageCount(params));
		xpage.addAll(paasProjectDeployMapper.page(params));
		return xpage;
	}

	private String getPublicIp() {
		return null;
		// TODO 可替换的 后面
//		String publicIp = ConfigUtil.getConfigString("paasos.publicips");
//		String[] pips = publicIp.split("\\s*[,;]\\s*");
//		String ip = null;
//		for (String pip : pips) {
//			if (StringUtil.isNull(pip)) {
//				continue;
//			}
//			String ipPrefix = pip.substring(0, pip.lastIndexOf("."));
//			String maxIp = paasProjectDeployMapper.getMaxPublicIp(ipPrefix
//					+ "%");
//			if (maxIp.endsWith("254")) {
//				continue;
//			}
//			if (maxIp == null || "0".equals(maxIp)) {
//				maxIp = pip;
//			}
//			long l = ip2int(maxIp);
//			String m = int2ip(++l);
//			logger.debug(m);
//			ip = m;
//		}
//		if (ip == null) {
//			throw new RuntimeException("没有可用的public ip地址段");
//		}
//		return ip;
	}

	private String getServiceIp() {
		String serviceIp = ConfigUtil.getConfigString("paasos.serviceips");
		String maxServiceIp = paasProjectDeployMapper.getMaxServiceIp();
		if (maxServiceIp == null || "0".equals(maxServiceIp)) {
			maxServiceIp = serviceIp;
		}
		long l = ip2int(maxServiceIp);
		String m = int2ip(++l);
		logger.debug(m);
		return m;
	}

	private Integer getUnUsedPort() {
		int m = paasProjectDeployPortMapper.getMaxPort();
		int min = ConfigUtil.getConfigInt("paasos.publicPort", 20001);
		if (m < min) {
			m = min;
		}

		return m;
	}

	private long ip2int(String ip) {
		String[] items = ip.split("\\.");
		return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16
				| Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
	}

	private String int2ip(long ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append((ipInt >> 24) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append(ipInt & 0xFF);
		return sb.toString();
	}

	@Override
	public PaasProjectDeploy findById(String deployId) {
		return paasProjectDeployMapper.findById(deployId);
	}

	@Transactional
	@Override
	public void beginDeploy(String deployId,String deployTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("deployId", deployId);
		param.put("status", 5);
		param.put("deployTime", deployTime);
		paasProjectDeployMapper.updateStatus(param);
	}

	@Transactional
	@Override
	public void endDeploy(String deployId, Integer status) {
		endDeploy(deployId,status,"");
	}
	
	@Transactional
	@Override
	public void endDeploy(String deployId, Integer status,String result) {
		endDeploy(deployId,status,null,result);
	}
	
	
	@Transactional
	@Override
	public void endDeploy(String deployId, Integer status,List<PaasProjectDeployVolumn> volumn) {
		endDeploy(deployId,status,volumn,null);
	}
	
	@Transactional
	@Override
	public void endDeploy(String deployId, Integer status,List<PaasProjectDeployVolumn> volumn,String result) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("deployId", deployId);
		param.put("status", status);
		if(StringUtils.hasText(result)){
			param.put("result", result);
		}
		paasProjectDeployMapper.updateStatus(param);
		if(volumn!=null){
			paasProjectDeployVolumnMapper.deleteByDeployId(deployId);
			if(volumn.size()>0){
				paasProjectDeployVolumnMapper.batchSave(volumn);
			}
		}
	}

	@Transactional
	@Override
	public void deleteDeploy(String deployId) {
		paasProjectDeployVolumnMapper.deleteByDeployId(deployId);
		paasProjectDeployPortMapper.deleteByDeployId(deployId);
		paasProjectDeployMapper.delete(deployId);
	}  

	public List<PaasProjectDeployPort> findDeployPortByDeployId(String deployId) {
		return paasProjectDeployPortMapper.findByDeployId(deployId);
	}

	@Transactional
	@Override
	public void updateReplicas(PaasProjectDeploy pd) {
		paasProjectDeployMapper.updateReplicas(pd);
	}

	@Override
	public List<PaasProjectDeployVolumn> findDeployVolumnByDeployId(
			String deployId) {
		return paasProjectDeployVolumnMapper.findByDeployId(deployId);
	}

	@Override
	public List<PaasProjectDeploy> findDeployByEntity(PaasProjectDeploy param) {
		return paasProjectDeployMapper.findDeployByEntity(param);
	}

	@Override
	public List<PaasProjectDeployEnv> findDeployEnvByDeployId(
			String deployId) {
		return paasProjectDeployEnvMapper.findByDeployId(deployId);
	}

	@Override
	public boolean hasDifference(String deployId,IUser user) {
		PaasProjectDeploy deploy = paasProjectDeployMapper.findById(deployId);
		PaasProject pp = iPaasProjectService.getPaasProjectInfoById(deploy.getProjectId());
		DeployResource dr = iPaasProjectEnvService.getDeployResouceAllByEnvId(deploy.getEnvId(), user.getUserId(),deployId);
		List<PaasProjectDeployEnv> newEnv = new ArrayList<PaasProjectDeployEnv>();
		if(!CollectionUtils.isEmpty(dr.getEnvs())){
			newEnv.addAll(dr.getEnvs());
		}
		
		if("api".equals(pp.getProjectType()) || "app".equals(pp.getProjectType())){
			PaasProjectBuild pb = iPaasProjectBuildService.findById(deploy.getBuildId());
			DeployResource dr1 = iPaasProjectEnvService.getDeployResouceAllByVersionId(deploy.getProjectId(), pb.getVersionId(), user.getUserId(),deploy);
			if(!CollectionUtils.isEmpty(dr1.getEnvs())){
				newEnv.addAll(dr1.getEnvs());
			}
		}

		List<PaasProjectDeployEnv> oldEnv = paasProjectDeployEnvMapper.findByDeployId(deployId);
		
		if(CollectionUtils.isEmpty(newEnv) && CollectionUtils.isEmpty(oldEnv)){
			logger.debug("都空  环境变量不需要修改");
			return false;
		}
		if(CollectionUtils.isEmpty(newEnv) && !CollectionUtils.isEmpty(oldEnv)){
			logger.debug("一个空一个有 需要修改");
			return true;
		}
		if(CollectionUtils.isEmpty(oldEnv) && !CollectionUtils.isEmpty(newEnv)){
			logger.debug("一个空一个有 需要修改");
			return true;
		}
		
		if(oldEnv.size() != newEnv.size()){
			logger.debug("长度不一样 需要修改");
			return true;
		}
		Map<String,String> ref = new HashMap<String,String>();
		for(PaasProjectDeployEnv o : oldEnv){
			ref.put(o.getKey(), o.getValue());
		}
		for(PaasProjectDeployEnv o : newEnv){
			String ov = ref.get(o.getKey());
			String nv = o.getValue();
			if(ov==null && nv==null){
				continue ;
			}
			if(ov==null && nv!=null){
				logger.debug(o.getKey()+"一个空一个不空");
				return true;
			}
			if(ov!=null && nv==null){
				logger.debug(o.getKey()+"一个空一个不空");
				return true;
			}
			if(!ov.equals(nv)){
				logger.debug(o.getKey()+"值不同");
				return true;
			}
		}
		return false;
	}
	
	@Transactional
	@Override
	public void changeLatestEnvVariables(String deployId, IUser user){
		PaasProjectDeploy deploy = paasProjectDeployMapper.findById(deployId);
		PaasProject pp = iPaasProjectService.getPaasProjectInfoById(deploy.getProjectId());
		String versionId = null;
		
		//从api或app中获取所有的固定环境变量 和非固定环境变量
		DeployResource dr = iPaasProjectEnvService.getDeployResouceAllByEnvId(deploy.getEnvId(), user.getUserId(),deployId);
		
		if("api".equals(pp.getProjectType()) || "app".equals(pp.getProjectType())){
			PaasProjectBuild pb = iPaasProjectBuildService.findById(deploy.getBuildId());
			versionId = pb.getVersionId();
		}
		
		DeployResource dr1 = iPaasProjectEnvService.getDeployResouceAllByVersionId(deploy.getProjectId(), versionId, user.getUserId(),deploy);
		paasProjectDeployEnvMapper.deleteByDeployId(deployId);
		if(!CollectionUtils.isEmpty(dr.getEnvs())){
			for(PaasProjectDeployEnv env : dr.getEnvs()){
				env.setDeployId(deployId);
				env.setId(StringUtil.getUUID());
			}
			paasProjectDeployEnvMapper.batchSave(dr.getEnvs());
		}
		if(dr1!=null && !CollectionUtils.isEmpty(dr1.getEnvs())){
			for(PaasProjectDeployEnv env : dr1.getEnvs()){
				env.setDeployId(deployId);
				env.setId(StringUtil.getUUID());
			}
			paasProjectDeployEnvMapper.batchSave(dr1.getEnvs());
		}
		
		
	}
	
	@Override
	public Map<String, Object> getTopInstance(Map<String, Object> params) {
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("data", paasProjectDeployMapper.getTopInstance(params));
		return r;
	}
	
	/**
	 * 保存的时候 deployId为空 ,后面有值
	 * 当部署的时候 deployId有只 其他没值
	 * @param deployId
	 * @param replicas
	 * @param cpu
	 * @param memory
	 * @param tentId
	 * @return
	 */
	@Override
	public boolean hasMoreResource(String envType,String tenantId,boolean isError,String deployId){
		
		if(!lock.containsKey(tenantId)){
			lock.put(tenantId, new Object());
		}
		synchronized(lock.get(tenantId)){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("tenantId", tenantId);
			ResourceQuota used = paasProjectDeployMapper.getAlreadyUsed(param);
			TwoTuple<Boolean, String> result = ResourceQuotaUtil.isSatisfy(null, used, null, false, 
					envType, tenantId, "deploy","docker");
			if(!result.a){
				throw new RuntimeException("租户配额不足:"+result.b);
			}
			return true;
		}
	}
	
	@Override
	public ResourceQuota getAlreadyUsed(Map<String, Object> params){
		return paasProjectDeployMapper.getAlreadyUsed(params);
	}
	
	@Override
	public Page<Map<String, Object>> getAllDeploy(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(paasProjectDeployMapper.getAllDeployCount(params));
		xpage.addAll(paasProjectDeployMapper.getAllDeploy(params));
		return xpage;
	}
	
	
	/**
	 * 这个逻辑是获取所有pod的ip 和节点ip
	 * 
	 * 大致逻辑 
	 * 先找出所有节点 取前面3个可用节点（怕太多节点）
	 * 获取部署pod锁在host  针对 pod所在nodeip:port 放在前面 
	 * 针对节点                                 nodeip:port 放在后面
	 * 
	 * pod ip 放前面
	 * @param envId
	 * @return
	 */
	@Override
	public List<PaasProjectDeployPort> findDeployPortByEnvIdForNginx(String envId){
		//获取所有节点IP地址
		
		PaasEnv env = iPaasProjectEnvService.getEnvById(envId);
		Map<String,String> labels = new HashMap<String, String>();
		labels.put("envtype", env.getEnvType());
		labels.put("type", "runtime");
		List<String> ips = K8RestApiUtil.getNodesIp(labels);
		
		
		List<String> preIps = ips.subList(0, ips.size()>3 ? 3:ips.size());
		
		//获取部署列表 所属IP 放第一位
		Set<String> result = new LinkedHashSet<String>();
		Set<String> br = new LinkedHashSet<String>();
		PaasProjectDeploy param = new PaasProjectDeploy();
		param.setEnvId(envId);
//		param.setStatus(10);
		List<PaasProjectDeploy> deploys = findDeployByEntity(param);
		if(deploys==null || deploys.size()==0){
			return null;
		}
		String[] deployIds = new String[deploys.size()];
		Map<String,Integer> ref = new HashMap<String,Integer>();
		int n = 0;
		for(PaasProjectDeploy deploy : deploys){
			List<PaasProjectDeployPort> ports = paasProjectDeployPortMapper.findByDeployId(deploy.getDeployId());
			if(ports==null || ports.size()==0){
				continue ;
			}
			Integer port = null;
			if(ports.size()==1){
				port = ports.get(0).getNodePort();
			}
			else{
				Map<String,Integer> r = new HashMap<String,Integer>();
				for(int i=0;i<ports.size();i++){
					r.put(ports.get(i).getPortProtocol().toLowerCase(),ports.get(i).getNodePort());
				}
				if(r.containsKey("http")){
					port = r.get("http");
				}
				//tcp ? //默认取第一个 不在取判断tcp协议了
				if(port==null){
					port = ports.get(0).getNodePort();
				}
			}
			ref.put(deploy.getDeployId(), port);
			for(String ip : preIps){
				br.add(ip+":"+port);
			}
			deployIds[n++]=deploy.getDeployId();
		}
		PodList pl = KubernetesUtil.getPods(null, "serviceId" , deployIds);
		if(pl==null || pl.getItems()==null || pl.getItems().size()==0){
			result.addAll(br);
		}
		else{
			for(Pod pod : pl.getItems()){
				String deployId = pod.getMetadata().getLabels().get("serviceId");
				Integer port = ref.get(deployId);
				if(pod.getStatus()==null || StringUtils.isEmpty(pod.getStatus().getHostIP())){
					continue ;
				}
				result.add(pod.getStatus().getHostIP()+":"+port);
			}
			result.addAll(br);
		}
		List<PaasProjectDeployPort> dps = new ArrayList<PaasProjectDeployPort>();
		for(String s : result){
			PaasProjectDeployPort p = new PaasProjectDeployPort();
			String[] ss = s.split(":");
			p.setNodePort(Integer.parseInt(ss[1]));
			p.setHostIp(ss[0]);
			dps.add(p);
		}
		return dps;
	}
}
