package com.lachesis.mnis.web.task.notify.service.impl;

import java.util.Calendar;
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

import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.task.notify.json.BaseJson;
import com.lachesis.mnis.web.task.notify.json.GeneralMsgJson;
import com.lachesis.mnis.web.task.notify.service.AbstarctNotifyService;
import com.lachesis.mnis.web.task.notify.service.PlainMsgNotifyService;

/**
 * 生命体征测量提醒规则：入院后三天（可配置）以内，手术后三天（可配置）以内等；每天固定时间点需测量；提前多少时间提醒由客户端配置；
 * @author wenhuan.cui
 *
 */
@Component("bodysignNotifyService")
public class BodySignNotifyServiceImpl extends AbstarctNotifyService implements PlainMsgNotifyService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BodySignNotifyServiceImpl.class);
	
	@Autowired private RedisService redisService;
	@Autowired private BodySignService bodySignService;
	@Autowired private PatientService patientService;
	private Map<String, Long> lastNotifyTimeMap = new HashMap<String, Long>();

	@Override
	public void doNotify(String userId) {
		if(userId == null) {
			return;
		}
		Map<String, String> bodySignMsmentConfig = redisService.getAndSavePubSubConfig(RedisConstants.CNL_BODYSIGN_MEASUREMENT, userId, null);
		if (!"1".equals(bodySignMsmentConfig.get(RedisConstants.FIELD_ENABLE))) {
			return;
		}
		if (StringUtils.isEmpty(bodySignMsmentConfig.get(RedisConstants.FIELD_BEFORETIME_MINUTE))) {
			return;
		}
		long lastNotifyTime = lastNotifyTimeMap.get(userId) == null ? 0 : lastNotifyTimeMap.get(userId);
		long currentTime = new Date().getTime();
		lastNotifyTimeMap.put(userId, currentTime);
		
		String channel = RedisConstants.CNL_BODYSIGN_MEASUREMENT + userId + ".*";
/*		BodySignMsmentRule newInHosResult = bodySignService.getBodySignMsmentRule(BodySignConstants.NEWINHOS);
		BodySignMsmentRule afterSurgResult = bodySignService.getBodySignMsmentRule(BodySignConstants.AFTERSURG);
		BodySignMsmentRule highTempResult = bodySignService.getBodySignMsmentRule(BodySignConstants.HIGHTEMP);
*/		List<Patient> list = patientService.getWardPatientList(0, userId);
		if(list == null || list.size() == 0){
			return;
		}
		
		// 提前提醒的时间
		long beforeMillis = DateUtils.MILLIS_PER_MINUTE * Integer.parseInt(bodySignMsmentConfig.get(RedisConstants.FIELD_BEFORETIME_MINUTE));
		for (Patient patient : list) {
			if(patient.getInDate() == null) {
				LOGGER.error("Patient's data is invalid !!");
				continue;
			}
			BaseJson baseJsonParams = super.getBaseJsonParamsFromBedBase(patient);
/*			if(highTempResult != null) {
				double ruleValue = Double.parseDouble(highTempResult.getRuleValue());
				double bodyTemp = bodySignService.getLatestBodyTemperature(inpatientInfo.getPatientBaseInfo().getPatientId());
				/// 体温超标要提醒
				// 如果最近一次体征测量得到的体温值超标，则进行测量提醒
				if (bodyTemp >= ruleValue) {
					if(notifyByRule(highTempResult, beforeMillis, lastNotifyTime, currentTime, channel, baseJsonParams)) {
						// 避免重复提醒
						continue;
					}
				}
			}*/
			
			/// 手术后要提醒
			long surgeryTime = 0;
			Date surgeryDate = DateUtil.parse(patient.getSurgeryDate(), DateFormat.FULL);
			if(surgeryDate != null){
				surgeryTime = surgeryDate.getTime();
			}

/*			if(afterSurgResult != null && surgeryTime > 0) {
				if(!hasRoutineExpired(Integer.parseInt(afterSurgResult.getRuleValue()), 
						surgeryTime, currentTime)) {
					// 在有效期内，需要进行提醒
					if(notifyByRule(afterSurgResult, beforeMillis, lastNotifyTime, currentTime, channel, baseJsonParams)) {
						// 避免重复提醒
						continue;
					}
				}				
			}*/

			/// 新入院三天内要提醒
			long admitDate = patient.getInDate().getTime();

/*			if(newInHosResult != null) {
				if(!hasRoutineExpired(Integer.parseInt(newInHosResult.getRuleValue())
						, admitDate, currentTime)) {
					// 在有效期内，需要进行提醒
					notifyByRule(newInHosResult, beforeMillis, lastNotifyTime, currentTime, channel, baseJsonParams);
				}
			}*/
		}
	}
	
	/**
	 * 按照规则进行通知：上次提醒时间  < 当前适用的规则时间点-提前时间 < 当前时间
	 * @param rule			   体征测量规则
	 * @param beforeMillis	   提前提醒的时间
	 * @param lastNotifyTime 上次提醒时间
	 * @param currentTime	   当前时间
	 * @param channel		 通知的频道
	 * @param msg			通知的信息
	 * @return
	 */
	private boolean notifyByRule(String ruleTime, long beforeMillis, 
			long lastNotifyTime, long currentTime, String channel, BaseJson msg) {
		long refTime = calcRefTime(ruleTime, beforeMillis);
		// 没有提醒过且当前时间过了提前提醒时间
		if (refTime > lastNotifyTime && refTime <= currentTime) {
			String json = msg.toJson();
			redisService.publish(channel, json);
			LOGGER.info(NOTIFY_CHANNEL_MSG, channel, json);
			return true;
		}
		return false;
	}

	/**
	 * 事件后例行测量提醒：入院后三天内和手术后三天内
	 */
	private boolean hasRoutineExpired(int rutineDays, long eventTime, long checkTime) {
		// 在患者入院或手术后若干天(ruleValue)内才进行是否提醒的判断
		if ((checkTime - eventTime) <= rutineDays * DateUtils.MILLIS_PER_DAY) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取目标提醒时间：如果当前为15点多，则下一个参考时间为16点减去提前时间
	 * @param ruleTime 3,6,9格式的时间
	 * @param beforeMillis
	 * @return
	 */
	private long calcRefTime(String ruleTime, long beforeMillis) {
		Calendar calendar = Calendar.getInstance();
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		int planHour = 0;
		for (String time : ruleTime.split(",")) {
			int setHour = Integer.parseInt(time);
			if (setHour > currentHour) {
				planHour = setHour;
				break;
			}
		}
		calendar.set(Calendar.HOUR_OF_DAY, planHour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis() - beforeMillis;
	}
	

	@Override
	public void notifyMsg() {
		String msg = new GeneralMsgJson(
				RedisConstants.CNL_BODYSIGN_MEASUREMENT, "请及时测量生命体征").toJson();
		
		redisService.publish(RedisConstants.CNL_GENERAL_MESSAGE, msg);
		LOGGER.info(NOTIFY_CHANNEL_MSG, RedisConstants.CNL_GENERAL_MESSAGE, msg);
	}
}
