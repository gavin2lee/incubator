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

import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.order.entity.HisOrderGroup;
import com.lachesis.mnis.core.order.entity.OrderExecLog;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.web.task.notify.json.BaseJson;
import com.lachesis.mnis.web.task.notify.json.OrderJson;
import com.lachesis.mnis.web.task.notify.service.AbstarctNotifyService;
/**
 * 对医嘱计划执行的预警和HIS执行（医嘱“审核”）的通知
 * @author wenhuan.cui
 *
 */
@Component("orderExecNotifyService")
public class OrderExecNotifyServiceImpl extends AbstarctNotifyService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderExecNotifyServiceImpl.class);
	
	@Autowired private RedisService redisService;
	@Autowired private PatientService patientService;
	@Autowired private OrderService orderService;
	
	private Map<String, Long> lastNotifyTimeMap = new HashMap<String, Long>();

	@Override
	public void doNotify(String userId) {
		if(userId == null) {
			return;
		}
		long lastNotifyTime = lastNotifyTimeMap.get(userId) == null ? 0 : lastNotifyTimeMap.get(userId);
		long currentTime = new Date().getTime();
		lastNotifyTimeMap.put(userId, currentTime);
		
		Map<String, String> docOrderExecConfig = redisService.getAndSavePubSubConfig(RedisConstants.CNL_DOCTORADVICE_EXEC, userId, null);
		if (docOrderExecConfig.get(RedisConstants.FIELD_ENABLE).equals("1")) {
			docOrderExecConfig = redisService.getPubSubUserConfigAll(userId, null, RedisConstants.CNL_DOCTORADVICE_EXEC);
			// 提前多少分钟提醒
			long beforeMillis = DateUtils.MILLIS_PER_MINUTE * Integer.parseInt(docOrderExecConfig.get(RedisConstants.FIELD_BEFORETIME_MINUTE));
			List<Patient> list = patientService.getWardPatientList(0, userId);
			if(list == null || list.size() == 0){
				return;
			}
			
			String orderExecChannel = RedisConstants.CNL_DOCTORADVICE_EXEC + userId + ".*";
			String orderChangeChannel = RedisConstants.CNL_DOCTORADVICE_CHANGE + userId + ".*";
			for (Patient patient : list) {
				BaseJson baseJsonParams = super.getBaseJsonParamsFromBedBase(patient);
				
//				List<HisOrderGroup> hisOrderGroups = orderService.getPendingHisOrderGroups(patientId, null);
				List<HisOrderGroup> hisOrderGroups = new ArrayList<>();
				for (HisOrderGroup order : hisOrderGroups) {
					for (OrderExecLog orderExec : order.getOrderExecList()) {
						// 计划执行时间
						long planTime = 0;
						try {
							planTime = orderExec.getPlanDate().getTime();
						} catch (RuntimeException e) {
							LOGGER.error("Error to parse planTime !!");
							continue;
						}
						// 以计划时间提前的时间作为参考时间
						long refTime = planTime - beforeMillis;
						// 参考时间后且没有推送过（上次推送时间在参考时间前）
						if (currentTime >= refTime && refTime > lastNotifyTime) {
							String json = baseJsonParams.toJson();
							redisService.publish(orderExecChannel, json);
							LOGGER.info(NOTIFY_CHANNEL_MSG, orderExecChannel, json);
						}
						
						// 新"审核"医嘱（即HIS刚执行）需要提醒
						long confirmTime = 0;
						if(orderExec.getExecDate() != null){
							confirmTime = orderExec.getExecDate().getTime();
						}

						if(confirmTime > lastNotifyTime) {
							order.getOrderGroup().setOrderStatusCode(MnisConstants.ORDER_STATUS_CONFIRMED);
							String json =OrderJson.createJson(order.getOrderGroup(), patient);
							redisService.publish(orderChangeChannel, json);
							LOGGER.info(NOTIFY_CHANNEL_MSG, orderChangeChannel, json);
						}
					}
				}
			}
		}
	}
	
}
