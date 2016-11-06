/**
 * Project Name:ob-paasos-web
 * File Name:PaasImageServiceImpl.java
 * Package Name:com.harmazing.openbridge.paasos.imgbuild.service.impl
 * Date:2016年5月6日下午4:04:17
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.imgbuild.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.openbridge.paasos.imgbuild.dao.PaasOsImageMapper;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;
import com.harmazing.openbridge.paasos.imgbuild.service.IPaasImageService;

/**
 * ClassName:PaasImageServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月6日 下午4:04:17 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
@Service
public class PaasImageServiceImpl implements IPaasImageService{
	
	@Autowired
	private PaasOsImageMapper paasOsImageMapper;

	@Transactional
	@Override
	public int insert(PaasOsImage image) {
		return paasOsImageMapper.insert(image);
	}

	@Transactional
	@Override
	public int update(PaasOsImage image) {
		return paasOsImageMapper.update(image);
	}

	@Override
	public PaasOsImage findById(String param) {
		return paasOsImageMapper.findById(param);
	}

	@Transactional
	@Override
	public void deleteById(String imageId) {
		paasOsImageMapper.deleteById(imageId);
	}

	@Override
	public PaasOsImage findImageByName(String name, String tag) {
		return paasOsImageMapper.findImageByName(name,tag);
	}

}

