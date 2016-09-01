package com.lachesis.mnis.web.task.notify.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.InspectionService;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.task.notify.json.BaseJson;
import com.lachesis.mnis.web.task.notify.json.MsgJson;
import com.lachesis.mnis.web.task.notify.service.AbstarctNotifyService;

@Component("inspectionNotifyService")
public class InspectionNotifyServiceImpl extends AbstarctNotifyService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InspectionNotifyServiceImpl.class);

	@Autowired
	private RedisService redisService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private InspectionService inspectionSypacsService;

	private Map<String, Date> lastNotifyTimeMap = new HashMap<>();

	@Override
	public void doNotify(String userId) {
		if (userId == null) {
			return;
		}
		Date currentDate = new Date();
		Date lastNotifyTime = lastNotifyTimeMap.get(userId) == null ? currentDate
				: lastNotifyTimeMap.get(userId);
		lastNotifyTimeMap.put(userId, currentDate);
		Map<String, String> lisLabConfig = redisService.getAndSavePubSubConfig(
				RedisConstants.CNL_INSPECTION, userId, null);

		if (!lisLabConfig.get(RedisConstants.FIELD_ENABLE).equals("1")) {
			return;
		}

		UserInformation userInformation = identityService
				.queryUserByCode(userId);
		if (null == userInformation) {
			return;
		}

		String deptCode = userInformation.getDeptCode();
		Date endTime = DateUtils.addHours(lastNotifyTime, 2);
//		lastNotifyTime = DateUtil.parse("2016-04-26 08:00:00", DateFormat.FULL);
		
		List<InspectionRecord> inspectionRecords = inspectionSypacsService
				.getPublishInspections(deptCode, lastNotifyTime, endTime);

		if (inspectionRecords.isEmpty()) {
			return;
		}

		String channel = RedisConstants.CNL_INSPECTION + deptCode + ".*";
		//构造消息体
		MsgJson msgJson = new MsgJson();
		List<BaseJson> baseJsons = new ArrayList<BaseJson>();
		for (InspectionRecord inspectionRecord : inspectionRecords) {
			BaseJson baseJson = new BaseJson();
			baseJson.setBedCode(inspectionRecord.getBedNo());
			baseJson.setPatientId(inspectionRecord.getPatId());
			baseJson.setPatientName(inspectionRecord.getPatName());
			baseJsons.add(baseJson);
		}

		msgJson.setMsgDate(new Date());
		msgJson.setDeptCode(deptCode);
		msgJson.setCount(baseJsons.size());
		msgJson.setBaseJsons(baseJsons);
		//发布消息
		redisService.publish(channel, msgJson.toJson());
		LOGGER.info(NOTIFY_CHANNEL_MSG, channel, msgJson.toJson());
	}

}
