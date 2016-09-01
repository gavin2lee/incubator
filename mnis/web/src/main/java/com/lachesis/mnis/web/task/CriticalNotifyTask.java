package com.lachesis.mnis.web.task;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisPublishUtil;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.web.task.notify.service.NotifyService;

@Component("criticalNotifyTask")
public class CriticalNotifyTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(CriticalNotifyTask.class);
	
	@Autowired
	@Qualifier("criticalValueNotifyService")
	private NotifyService notifyService;
	
	@Autowired
	private RedisPublishUtil redisPublishUtil;
	
	@Autowired
	private RedisService redisService;

//	@Scheduled(fixedRate=60000)
	public void notifyNurses() {
		LOGGER.info("{}:Pushing Notification!", DateUtil.format());
		Set<String> skintestUsers = redisPublishUtil.getUserIds(RedisConstants.PUBSUB_USERS_KEY);
		if(skintestUsers == null || skintestUsers.isEmpty()) {
			return;
		}
		
		for(String userId : skintestUsers) {
			// 判断对用户的推送是否启用
			Map<String, String> genericConfig = redisService.getPubSubUserConfigAll(userId, null, RedisConstants.CNL_CRITICAL_VALUE);
			if(!"1".equals(genericConfig.get(RedisConstants.FIELD_ENABLE))) {
				// 未启用推送，则略过
				continue;
			}
			notifyService.doNotify(userId);
		}
	}

}
