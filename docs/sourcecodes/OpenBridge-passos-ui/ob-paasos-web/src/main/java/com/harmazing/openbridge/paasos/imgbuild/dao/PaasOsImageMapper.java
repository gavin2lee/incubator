/**
 * Project Name:ob-paasos-web
 * File Name:PaasOsImageMapper.java
 * Package Name:com.harmazing.openbridge.paasos.project.dao
 * Date:2016年4月26日上午11:21:24
 * Copyright (c) 2016
 *
 */

package com.harmazing.openbridge.paasos.imgbuild.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;

/**
 * ClassName:PaasOsImageMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年4月26日 上午11:21:24 <br/>
 * 
 * @author dengxq
 * @version
 * @since JDK 1.6
 * @see
 */
public interface PaasOsImageMapper extends IBaseMapper {

	public int insert(PaasOsImage image);

	public int update(PaasOsImage image);

	public PaasOsImage findById(String param);

	@Delete(" delete from os_image where image_id = #{param} ")
	public void deleteById(String imageId);

	@Select(" select * from os_image where image_name = #{name} and image_version=#{tag} ")
	PaasOsImage findImageByName(@Param("name") String name,
			@Param("tag") String tag);
}
