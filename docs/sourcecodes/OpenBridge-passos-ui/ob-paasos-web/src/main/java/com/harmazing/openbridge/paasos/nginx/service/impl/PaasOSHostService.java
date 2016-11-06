package com.harmazing.openbridge.paasos.nginx.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;
import com.harmazing.openbridge.paasos.nginx.dao.PaasNginxHostMapper;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSHostService;

@Service
public class PaasOSHostService implements IPaasOSHostService {
	@Autowired
	private PaasNginxHostMapper hostMapper;

	@Override
	public List<PaasHost> getHostByType(String type,String platform) {
		List<PaasHost> hosts = hostMapper.getAllHost();
		List<PaasHost> list = new ArrayList<PaasHost>();
		if(hosts!=null && hosts.size()>0){
			for (int i = 0; i < hosts.size(); i++) {
				PaasHost host = hosts.get(i);
				if (host.getHostType().equals(type) && host.getHostPlatform().equals(platform)) {
					list.add(hostKey(host));
				}
			}
		}
		return list;
	}

	private PaasHost hostKey(PaasHost host) {
		if (StringUtil.isNull(host.getHostKeyPrv())) {
			host.setHostKeyPrv(ConfigUtil.getConfigString("ssh.prv"));
			host.setHostKeyPub(ConfigUtil.getConfigString("ssh.pub"));
		}
		return host;
	}

	@Override
	public PaasHost getHostById(String hostId) {
		List<PaasHost> hosts = hostMapper.getAllHost();
		if(hosts!=null && hosts.size()>0){
			for (int i = 0; i < hosts.size(); i++) {
				if (hosts.get(i).getHostId().equals(hostId)) {
					return hostKey(hosts.get(i));
				}
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PaasHost> queryHostPage(Map<String, Object> params) {
		Page<PaasHost> xpage = Page.create(params);
		xpage.setRecordCount(hostMapper.queryHostCount(params));
		xpage.addAll(hostMapper.queryHostPage(params));
		return xpage;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addHost(PaasHost host) {
		hostMapper.insertHost(host);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateHost(PaasHost host) {
		hostMapper.updateHost(host);
	}

	@Override
	public int getcountHostByIp(PaasHost host) {
		return hostMapper.getcountHostByIp(host);
	}

	@Override
	public int getcountHostByName(PaasHost host) {
		return hostMapper.getcountHostByName(host);
	}

	@Override
	public void deleteHostById(String hostId) {
		hostMapper.deleteHostById(hostId);
	}
}
