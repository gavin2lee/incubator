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
import com.lachesis.mnis.web.task.notify.service.NotifyService;

/**
 * NotifyTask for a user. Each user has a channel to publish. For each patient of 
 * this user, different configurations apply.
 * @author xin.chen
 **/
@Component("wardPatrolNotifyTask")
public class WardPatrolNotifyTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(WardPatrolNotifyTask.class);
	
	@Autowired
	@Qualifier("wardPatrolNotifyService")
	private NotifyService notifyService;
	
	@Autowired 
	private RedisService redisService;
	
	@Autowired
	private RedisPublishUtil redisPublishUtil;
	
//	@Scheduled(fixedRate=60000)
	public void notifyWardPatrol() {
		LOGGER.info("Pushing WardPatrol Notification!");
		// 病房巡视消息推送的频道和频道属性配置key
		Set<String> wardPatrolUsers = redisPublishUtil.getUserIds(RedisConstants.WARD_PATROL_USERS_KEY);
		if(wardPatrolUsers == null || wardPatrolUsers.isEmpty()) {
			return;
		}
		
		for(String userId : wardPatrolUsers) {
			// 判断对用户的推送是否启用
			Map<String, String> genericConfig = redisService.getPubSubUserConfigAll(userId, null, RedisConstants.CNL_WARD_PATROL);
			if(!"1".equals(genericConfig.get(RedisConstants.FIELD_ENABLE))) {
				// 未启用推送，则略过
				continue;
			}
			notifyService.doNotify(userId);
		}
	}


}
