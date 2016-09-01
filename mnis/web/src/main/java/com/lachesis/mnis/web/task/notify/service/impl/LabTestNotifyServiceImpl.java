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
import com.lachesis.mnis.core.LabTestService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.task.notify.json.BaseJson;
import com.lachesis.mnis.web.task.notify.json.LabTestJson;
import com.lachesis.mnis.web.task.notify.json.MsgJson;
import com.lachesis.mnis.web.task.notify.service.AbstarctNotifyService;

@Component("labTestNotifyService")
public class LabTestNotifyServiceImpl extends AbstarctNotifyService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LabTestNotifyServiceImpl.class);
	
	@Autowired private RedisService redisService;
	@Autowired private IdentityService identityService;
	@Autowired private LabTestService labTestService;

	private Map<String, Date> lastNotifyTimeMap = new HashMap<>();
	
	@Override
	public void doNotify(String userId) {
		if(userId == null) {
			return;
		}
		Date currentDate = new Date();
		Date lastNotifyTime = lastNotifyTimeMap.get(userId) == null ? currentDate : lastNotifyTimeMap.get(userId);
		lastNotifyTimeMap.put(userId, currentDate);
		
		Map<String, String> lisLabConfig = redisService.getAndSavePubSubConfig(
				RedisConstants.CNL_LISLAB, userId, null);
		if (!lisLabConfig.get(RedisConstants.FIELD_ENABLE).equals("1")) {
			return;
		}

		UserInformation userInformation = identityService
				.queryUserByCode(userId);
		if (null == userInformation) {
			return;
		}

		String deptCode = userInformation.getDeptCode();
//		lastNotifyTime = DateUtil.parse("2016-05-02 08:00:00", DateFormat.FULL);
		Date endTime = DateUtils.addHours(lastNotifyTime, 2);
		List<LabTestRecord> labTestRecords = labTestService
				.getPublishLabTests(deptCode, lastNotifyTime, endTime);

		if (labTestRecords.isEmpty()) {
			return;
		}

		String channel = RedisConstants.CNL_LISLAB + deptCode + ".*";
		//构造消息体
		MsgJson msgJson = new MsgJson();
		List<BaseJson> baseJsons = new ArrayList<BaseJson>();
		for (LabTestRecord labTestRecord : labTestRecords) {
			BaseJson baseJson = new BaseJson();
			baseJson.setBedCode(labTestRecord.getBedCode());
			baseJson.setPatientId(labTestRecord.getHospNo());
			baseJson.setPatientName(labTestRecord.getPatientName());
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
