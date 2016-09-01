package com.lachesis.mnis.core.redismsg.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.RedisMsgMapper;
import com.lachesis.mnis.core.redismsg.entity.RedisMsgRecord;
import com.lachesis.mnis.core.redismsg.repository.RedisMsgRepository;

@Repository
public class RedisMsgRepositoryImpl implements RedisMsgRepository{

	@Autowired
	private RedisMsgMapper redisMsgMapper;
	@Override
	public int saveRedisMsgRecord(RedisMsgRecord redisMsgRecord) {
		return redisMsgMapper.saveRedisMsgRecord(redisMsgRecord);
	}
	@Override
	public Integer getSysMsgPublishByOperId(String operId, String operType) {
		return redisMsgMapper.getSysMsgPublishByOperId(operId, operType);
	}

}
