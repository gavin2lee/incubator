package com.lachesis.mnis.core.mybatis.mapper;

import com.lachesis.mnis.core.scan.entity.NurseScanInfo;


public interface NurseScanMapper {
	/**
	 * 保存护士扫描信息
	 * 
	 */
	int insertNurseScan(NurseScanInfo nurseScanInfo);
}
