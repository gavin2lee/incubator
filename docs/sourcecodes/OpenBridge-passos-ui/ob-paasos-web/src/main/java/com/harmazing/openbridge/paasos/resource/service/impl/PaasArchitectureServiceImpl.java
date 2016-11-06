package com.harmazing.openbridge.paasos.resource.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.paas.architecture.service.impl.ArchitectureServiceImpl;
import com.harmazing.openbridge.paas.architecture.vo.GraphVo;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;
import com.harmazing.openbridge.paas.nginx.service.IPaasNginxService;
import com.harmazing.openbridge.paasos.kubectl.K8RestApiUtil;
import com.harmazing.openbridge.paasos.manager.model.PaaSNode;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSHostService;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSNginxService;
import com.harmazing.openbridge.paasos.resource.service.IPaasArchitectureService;

@Service
public class PaasArchitectureServiceImpl extends ArchitectureServiceImpl implements IPaasArchitectureService{

	@Autowired
	private IPaasOSHostService iPaasOSHostService;
	
	@Autowired
	private IPaaSTenantService paaSTenantService;
	
	@Override
	public List<GraphVo> getPaasNodes(String userId) {
		List<PaaSNode> nodes = null;
		K8RestApiUtil query = new K8RestApiUtil(paaSTenantService);
		nodes = query.listNodes(nodes);
		
		if(nodes==null || nodes.size()==0){
			return null;
		}
		List<GraphVo> t = new ArrayList<GraphVo>();
		for(PaaSNode h : nodes){
			GraphVo v = new GraphVo();
			v.setName(h.getIp());
			v.getProperty().put("url", h.getIp()+":4194");
			v.getProperty().put("protocol", "http");
			t.add(v);
		}
		
		return t;
	}

	@Override
	public List<GraphVo> getPaasProxys(String userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pageNo", "1");
		params.put("pageSize", "99999");
		Page<PaasHost> page = iPaasOSHostService.queryHostPage(params);
		if(page==null || page.size()==0){
			return null;
		}
		List<GraphVo> t = new ArrayList<GraphVo>();
		for(PaasHost h : page){
			GraphVo v = new GraphVo();
			v.setName(h.getHostIp());
			v.getProperty().put("url", h.getHostIp()+":80");
			v.getProperty().put("protocol", "http");
			t.add(v);
		}
		return t;
	}

	@Override
	public List<GraphVo> getPaasResources(String userId) {
		List<GraphVo> t = new ArrayList<GraphVo>();
		GraphVo v = new GraphVo();
		v.setName("资源1");
		v.getProperty().put("protocol", "http");
		t.add(v);
		
		v = new GraphVo();
		v.setName("资源2");
		v.getProperty().put("protocol", "http");
		t.add(v);
		
		v = new GraphVo();
		v.setName("资源3");
		v.getProperty().put("protocol", "http");
		t.add(v);
		
		v = new GraphVo();
		v.setName("资源4");
		v.getProperty().put("protocol", "http");
		t.add(v);
		
		v = new GraphVo();
		v.setName("资源5");
		v.getProperty().put("protocol", "http");
		t.add(v);
		
		v = new GraphVo();
		v.setName("资源6");
		v.getProperty().put("protocol", "http");
		t.add(v);
		
		return t;
	}


	
	
}
