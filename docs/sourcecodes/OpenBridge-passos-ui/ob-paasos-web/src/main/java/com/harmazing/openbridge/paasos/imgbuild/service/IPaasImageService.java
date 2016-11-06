/**
 * Project Name:ob-paasos-web
 * File Name:IPaasImageService.java
 * Package Name:com.harmazing.openbridge.paasos.imgbuild.service
 * Date:2016年5月6日下午4:03:53
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.imgbuild.service;

import java.util.*;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;

/**
 * ClassName:IPaasImageService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月6日 下午4:03:53 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public interface IPaasImageService {
	
	public int insert(PaasOsImage image);

	public int update(PaasOsImage image);

	public PaasOsImage findById(String param);

	public void deleteById(String imageId);

	public PaasOsImage findImageByName(String name,String tag);

}

