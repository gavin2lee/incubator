package com.lachesis.mnis.core.inoutmanager.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.inoutmanager.entity.InOutManager;
import com.lachesis.mnis.core.inoutmanager.entity.InOutManagerStatistic;
import com.lachesis.mnis.core.inoutmanager.repository.InOutManagerRepository;
import com.lachesis.mnis.core.mybatis.mapper.InOutManagerMapper;

@Repository("inOutManagerRepository")
public class InOutManagerRepositoryImpl implements InOutManagerRepository {

	@Autowired
	private InOutManagerMapper inOutManagerMapper;
	
	@Override
	public List<InOutManager> getInOutManagers(String patId, String deptCode,Date startDate,Date endDate) {
		return inOutManagerMapper.getInOutManagers(patId, deptCode, startDate, endDate);
	}

	@Override
	public InOutManager getInOutManagerById(int id) {
		return inOutManagerMapper.getInOutManagerById(id);
	}

	@Override
	public int insertInOutManager(InOutManager inOutManager) {
		return inOutManagerMapper.insertInOutManager(inOutManager);
	}

	@Override
	public int updateInOutManager(InOutManager inOutManager) {
		return inOutManagerMapper.updateInOutManager(inOutManager);
	}

	@Override
	public int deleteById(int id) {
		return inOutManagerMapper.deleteById(id);
	}

	@Override
	public InOutManagerStatistic getInOutManagerStatistic(String patId,
			String deptCode, Date startDate, Date endDate) {
		return inOutManagerMapper.getInOutManagerStatistic(patId, deptCode, startDate, endDate);
	}

	@Override
	public List<Map<String, String>> getOutDics(HashMap<String, Object> params) {
		return inOutManagerMapper.getOutDics(params);
	}

}
