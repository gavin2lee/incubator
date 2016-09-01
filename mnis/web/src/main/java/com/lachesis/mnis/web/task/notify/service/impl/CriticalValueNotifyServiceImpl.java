package com.lachesis.mnis.web.task.notify.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.CriticalValueService;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.web.task.notify.json.BaseJson;
import com.lachesis.mnis.web.task.notify.json.MsgJson;
import com.lachesis.mnis.web.task.notify.service.NotifyService;

@Component("criticalValueNotifyService")
public class CriticalValueNotifyServiceImpl implements NotifyService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CriticalValueNotifyServiceImpl.class);

	@Autowired
	private RedisService redisService;
	@Autowired
	private CriticalValueService criticalValueService;

	@Override
	public void doNotify(String userId) {
		try {

			Map<String, String> criticalValueConfig = redisService
					.getAndSavePubSubConfig(RedisConstants.CNL_CRITICAL_VALUE,
							userId, null);
			if (criticalValueConfig.get(RedisConstants.FIELD_ENABLE)
					.equals("1")) {
				List<CriticalValueRecord> criticalValueRecords = criticalValueService
						.getCriticalValueRecord(null, null, null, null, false);
				if (criticalValueRecords.size() == 0) {
					return;
				}
				
				//定义消息体
				List<MsgJson> msgJsons = new ArrayList<MsgJson>();
				List<String> deptCodes = new ArrayList<String>();
				for (CriticalValueRecord criticalValueRecord : criticalValueRecords) {
					if(deptCodes.contains(criticalValueRecord.getDeptNo())){
						continue;
					}
					deptCodes.add(criticalValueRecord.getDeptNo());
				}
				//构造消息体
				Date msgDate = new Date();
				for (String deptCode : deptCodes) {
					MsgJson msgJson = new MsgJson();
					msgJson.setDeptCode(deptCode);
					msgJson.setMsgDate(msgDate);
					List<BaseJson> baseJsons = new ArrayList<BaseJson>();
					for (CriticalValueRecord criticalValueRecord : criticalValueRecords) {
						if(deptCode.equals(criticalValueRecord.getDeptNo())){
							BaseJson baseJson = new BaseJson();
							baseJson.setBedCode(criticalValueRecord.getBedCode());
							baseJson.setPatientName(criticalValueRecord.getPatientName());
							baseJsons.add(baseJson);
						}
					}
					msgJson.setBaseJsons(baseJsons);
					msgJson.setCount(baseJsons.size());
					msgJsons.add(msgJson);
				}
				
				if(msgJsons.isEmpty()){
					return;
				}
				
				for (MsgJson msgJson : msgJsons) {
					//频道
					String channel = RedisConstants.CNL_CRITICAL_VALUE
							+ msgJson.getDeptCode() + ".*";

					redisService.publish(channel, msgJson.toJson());
					LOGGER.info(NOTIFY_CHANNEL_MSG, channel, msgJson.toJson());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
