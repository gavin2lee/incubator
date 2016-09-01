package com.lachesis.mnis.core.scan.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.NurseScanMapper;
import com.lachesis.mnis.core.scan.entity.NurseScanInfo;
import com.lachesis.mnis.core.scan.repository.NurseScanRepository;

@Repository
public class NurseScanRepositoryImpl implements NurseScanRepository {

	@Autowired
	private NurseScanMapper nurseScanMapper;
	@Override
	public int insertNurseScan(NurseScanInfo nurseScanInfo) {
		return nurseScanMapper.insertNurseScan(nurseScanInfo);
	}

}
