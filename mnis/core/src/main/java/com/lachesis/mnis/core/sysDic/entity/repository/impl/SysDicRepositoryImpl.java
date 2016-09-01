package com.lachesis.mnis.core.sysDic.entity.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.SysDicMapper;
import com.lachesis.mnis.core.sysDic.entity.SysDic;
import com.lachesis.mnis.core.sysDic.entity.repository.SysDicRepository;

@Repository("sysDicRepository")
public class SysDicRepositoryImpl implements SysDicRepository {

	@Autowired
	SysDicMapper sysDicMapper;
	
	@Override
	public int save(SysDic record) {
		return sysDicMapper.insert(record);
	}

	@Override
	public List<SysDic> queryAll() {
		return sysDicMapper.selectAll();
	}

	@Override
	public List<String> queryDicTypes() {
		return sysDicMapper.queryDicTypes();
	}

}
