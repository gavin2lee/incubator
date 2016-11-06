package com.harmazing.openbridge.monitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.framework.quartz.IJob;
import com.harmazing.framework.quartz.IJobContext;
import com.harmazing.openbridge.alarm.dao.GroupHostMapper;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.Host;
import com.harmazing.openbridge.monitor.dao.ResourcesMapper;
import com.harmazing.openbridge.monitor.model.PaasHost;
import com.harmazing.openbridge.util.KubernetesUtil;

@Service("jobResourcesService")
public class JobResourcesServiceImpl implements IJob {
	@Autowired
	private ResourcesMapper resourcesMapper;
	@Autowired
	private GroupHostMapper groupHostMapper;
	@Autowired
	private GroupMapper groupMapper;

	@Override
	public void runJob(IJobContext context) throws Exception {

		findNodeByType("", "1", "runtime", "部署节点组");
		findNodeByType("", "3", "mysql", "MySQL节点");
		findNodeByType("", "4", "rabbitmq", "MQ节点");
		findNodeByType("", "5", "redis", "缓存节点");
		findNginx("");
		findPlatform("");
	}

	public void findNodeByType(String userId, String type, String typeName, String grpName) {
		int sysType = resourcesMapper.getCountByType(type);
		if (sysType == 0) {
			Group group = new Group();
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
					if (h.getMetadata().getLabels().get("type")!= null) {
						if (h.getMetadata().getLabels().get("type").equals(typeName)) {
							Host host = resourcesMapper.findByName(h.getMetadata().getName());
							if (host != null) {
								groupHostMapper.insert(group.getId(), host.getId());
							}
						}
					}
				}
			}
		} else {
			Group group = resourcesMapper.findByType(type);
			groupHostMapper.deleteById(group.getId());
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
	}
	public void findNginx(String userId) {
		int sysType = resourcesMapper.getCountByType("2");
		if (sysType == 0) {
			Group group = new Group();
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
		}else{
			Group group = resourcesMapper.findByType("2");
			groupHostMapper.deleteById(group.getId());
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
	}
	
	public void findPlatform(String userId) {
		int sysType6 = resourcesMapper.getCountByType("6");
		if (sysType6 == 0) {
			Group group = new Group();
			group.setGrpName("平台节点");
			group.setCreateUser(userId);
			group.setComeFrom("1");
			group.setGrpType("6");
			groupMapper.insert(group);
		}
	}
}
