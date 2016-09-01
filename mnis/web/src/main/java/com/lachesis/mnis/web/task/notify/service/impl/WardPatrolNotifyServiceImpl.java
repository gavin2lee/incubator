package com.lachesis.mnis.web.task.notify.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.patient.entity.PatientBaseInfo;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.task.notify.json.BaseJson;
import com.lachesis.mnis.web.task.notify.json.MsgJson;
import com.lachesis.mnis.web.task.notify.service.AbstarctNotifyService;

@Component("wardPatrolNotifyService")
public class WardPatrolNotifyServiceImpl extends AbstarctNotifyService {
	private Logger LOGGER = LoggerFactory
			.getLogger(WardPatrolNotifyServiceImpl.class);

	@Autowired
	private PatientService patientService;
	@Autowired
	private RedisService redisService;
	private Map<String, Long> lastNotifyTimeMap = new HashMap<String, Long>();

	@Override
	public void doNotify(String userId) {
		if (userId == null) {
			return;
		}
		LOGGER.info("Do WardPatrol Notify For User: " + userId);
		long current = new Date().getTime();
		long lastNotifyTime = lastNotifyTimeMap.get(userId) == null ? 0
				: lastNotifyTimeMap.get(userId);
		lastNotifyTimeMap.put(userId, current);

		// 获取推送频道的相关属性
		Map<String, String> genericConfig = redisService
				.getPubSubUserConfigAll(userId, null,
						RedisConstants.CNL_WARD_PATROL);
		String deptCode = genericConfig.get("deptCode");
		if(StringUtils.isBlank(deptCode)){
			return;
		}
		long beforeMillis = DateUtils.MILLIS_PER_MINUTE
				* Integer.parseInt(genericConfig
						.get(RedisConstants.FIELD_BEFORETIME_MINUTE));
		String channel = RedisConstants.CNL_WARD_PATROL + deptCode + ".*";
		
		// 对user和其每个病人，获取其wardPatrol推送配置，进行推送
		List<PatientBaseInfo> patientBaseInfos = patientService
				.getPatientBaseInfoByDeptCode(deptCode, null);
		if (patientBaseInfos == null || patientBaseInfos.size() == 0) {
			return;
		}
		MsgJson pushMsgJson = new MsgJson();
		List<BaseJson> baseJsons = new ArrayList<BaseJson>();
		for (PatientBaseInfo patient : patientBaseInfos) {
			Map<String, String> patientWiseConfig = redisService
					.getPubSubUserConfigAll(userId, patient.getPatId(),
							RedisConstants.CNL_WARD_PATROL);
			String msg = patientWiseConfig
					.get(RedisConstants.FIELD_MSG);
			if (patientWiseConfig.isEmpty()) {
				continue;
			}
			if (msg == null) {
				LOGGER.error("Message is null! There is a bug here!");
				continue;
			}
			MsgJson msgJson = GsonUtils.fromJson(msg, MsgJson.class);
			if(null == msgJson){
				LOGGER.error("Message is null! There is a bug here!");
				continue;
			}
			long planedPushTime = Long.parseLong(patientWiseConfig
					.get(RedisConstants.FIELD_PLANEDTIME_MILLIS));
			long refTime = planedPushTime - beforeMillis;
			LOGGER.info("current:refTime:last -- {}:{}:{}", current, refTime,
					lastNotifyTime);
			// 参考时间后且没有推送过（上次推送时间在参考时间前）
			if (current >= refTime && refTime > lastNotifyTime) {
				baseJsons.addAll(msgJson.getBaseJsons());
			}
		}
		
		if(baseJsons.isEmpty()){
			return;
		}
		
		pushMsgJson.setCount(baseJsons.size());
		pushMsgJson.setDeptCode(deptCode);
		pushMsgJson.setMsgDate(new Date());
		pushMsgJson.setBaseJsons(baseJsons);
		redisService.publish(channel, pushMsgJson.toJson());
		LOGGER.info(NOTIFY_CHANNEL_MSG, channel, pushMsgJson.toJson());
	}

}
