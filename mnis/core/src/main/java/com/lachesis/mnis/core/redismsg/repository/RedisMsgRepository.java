package com.lachesis.mnis.core.redismsg.repository;

import com.lachesis.mnis.core.redismsg.entity.RedisMsgRecord;

public interface RedisMsgRepository {
	int saveRedisMsgRecord(RedisMsgRecord redisMsgRecord);
	
	Integer getSysMsgPublishByOperId(String operId,String operType);
}
