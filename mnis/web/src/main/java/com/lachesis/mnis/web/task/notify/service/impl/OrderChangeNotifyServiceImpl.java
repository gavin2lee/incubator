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
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.order.entity.HisOrderGroup;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.order.entity.OrderGroup;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.task.notify.json.BaseJson;
import com.lachesis.mnis.web.task.notify.json.MsgJson;
import com.lachesis.mnis.web.task.notify.json.OrderJson;
import com.lachesis.mnis.web.task.notify.service.AbstarctNotifyService;

/**
 * 护士应收到新开立医嘱和新停止医嘱的提醒
 * 
 * @author wenhuan.cui
 *
 */
@Component("orderChangeNotifyService")
public class OrderChangeNotifyServiceImpl extends AbstarctNotifyService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderChangeNotifyServiceImpl.class);

	@Autowired
	private RedisService redisService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private OrderService orderService;
	private Map<String, String> lastNotifyTimeMap = new HashMap<String, String>();

	@Override
	public void doNotify(String userId) {
		if (userId == null) {
			return;
		}

		String current = DateUtil.format();
		// 只处理启动之后改变状态的提醒
		Date lastNotifyTime = DateUtil.parse(
				lastNotifyTimeMap.get(userId) == null ? current
						: lastNotifyTimeMap.get(userId), DateFormat.FULL);
		lastNotifyTimeMap.put(userId, current);

		Map<String, String> docAdviceChangeConfig = redisService
				.getAndSavePubSubConfig(RedisConstants.CNL_DOCTORADVICE_CHANGE,
						userId, null);

		if (!docAdviceChangeConfig.get(RedisConstants.FIELD_ENABLE).equals("1")) {
			LOGGER.info(RedisConstants.CNL_DOCTORADVICE_CHANGE + " not start!");
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
		List<HisOrderGroup> orderGroups = orderService
				.getPublishOriginalOrders(deptCode, lastNotifyTime, endTime);

		if (orderGroups == null || orderGroups.isEmpty()) {
			return;
		}

		String channel = RedisConstants.CNL_DOCTORADVICE_CHANGE + deptCode + ".*";
		//构造消息体
		MsgJson msgJson = new MsgJson();
		List<BaseJson> baseJsons = new ArrayList<BaseJson>();
		for (HisOrderGroup hisOrderGroup : orderGroups) {
			if(null == hisOrderGroup.getOrderGroup()){
				continue;
			}
			BaseJson baseJson = new BaseJson();
			baseJson.setBedCode(hisOrderGroup.getOrderGroup().getPatientBedCode());
			baseJson.setPatientId(hisOrderGroup.getOrderGroup().getPatientId());
			baseJson.setPatientName(hisOrderGroup.getOrderGroup().getPatientName());
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
