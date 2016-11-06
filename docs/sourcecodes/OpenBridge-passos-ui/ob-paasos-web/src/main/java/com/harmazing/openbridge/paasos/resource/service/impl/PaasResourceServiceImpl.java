package com.harmazing.openbridge.paasos.resource.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.openbridge.paasos.resource.dao.PaasResourceMapper;
import com.harmazing.openbridge.paasos.resource.model.PaasResource;
import com.harmazing.openbridge.paasos.resource.service.IPaasResourceService;

@Service
public class PaasResourceServiceImpl implements IPaasResourceService {

	@Autowired
	private PaasResourceMapper resourceMapper;
	
	@Override
	public PaasResource getPaasResourceById(String resId) {
		return resourceMapper.getPaasResourceById(resId);
	}

}
