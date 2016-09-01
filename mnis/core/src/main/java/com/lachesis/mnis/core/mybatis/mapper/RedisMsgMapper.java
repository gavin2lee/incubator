package com.lachesis.mnis.core.mybatis.mapper;

import com.lachesis.mnis.core.redismsg.entity.RedisMsgRecord;

public interface RedisMsgMapper {
	int saveRedisMsgRecord(RedisMsgRecord redisMsgRecord);
	
	Integer getSysMsgPublishByOperId(String operId,String operType);
}
