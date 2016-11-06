package com.harmazing.openbridge.paasos.manager.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.manager.dao.PaaSNodeMapper;
import com.harmazing.openbridge.paasos.manager.model.PaaSNode;
import com.harmazing.openbridge.paasos.manager.service.IPaaSNodeService;
@Service
public class PaaSNodeService implements IPaaSNodeService{
	@Resource
	private PaaSNodeMapper paaSNodeMapper;
	@Override
	public List<PaaSNode> list() {
		return paaSNodeMapper.list();
	}

	@Override
	public Page<PaaSNode> getPage(int pageNo, int pageSize) {
		Page<PaaSNode> page = new Page<PaaSNode>(pageNo,pageSize);
		page.addAll(paaSNodeMapper.getPageData(page.getStart(),page.getPageSize()));
		page.setRecordCount(paaSNodeMapper.getPageCount());
		return page;
	}

	@Override
	public PaaSNode getById(String id) {
		return paaSNodeMapper.getById(id);
	}

	@Override
	@Transactional
	public void delete(String id) {
		paaSNodeMapper.delete(id);
	}

	@Override
	@Transactional
	public void saveOrUpdate(PaaSNode node) {
		String id = node.getId();
		if(StringUtil.isNull(id)){
			id = StringUtil.getUUID();
			node.setId(id);
			paaSNodeMapper.add(node);
		}else{
			paaSNodeMapper.update(node);
		}
		
	}

	@Override
	public PaaSNode getByName(String name) {
		return paaSNodeMapper.getByName(name);
	}

	@Override
	public List<PaaSNode> getTenantNodes(String tenantId) {
		return paaSNodeMapper.getTenantNodes(tenantId);
	}

}
