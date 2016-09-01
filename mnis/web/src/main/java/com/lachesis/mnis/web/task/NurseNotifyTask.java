package com.lachesis.mnis.web.task;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import redis.clients.jedis.Jedis;

import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisPublishUtil;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.web.task.notify.service.NotifyService;
import com.lachesis.mnis.web.task.notify.service.PlainMsgNotifyService;

public class NurseNotifyTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(NurseNotifyTask.class);
	
	@Autowired
	private RedisService redisService;

	@Autowired
	@Qualifier("bodysignNotifyService")
	private PlainMsgNotifyService plainMsgNotifyService;
	
	private List<NotifyService> notifyServices;
	
	@Autowired
	private RedisPublishUtil redisPublishUtil;
	
	public void setNotifyServices(List<NotifyService> notifyServices) {
		this.notifyServices = notifyServices;
	}

//	@Scheduled(fixedRate=60000)
	public void notifyNurses() {
		LOGGER.info("{}:Pushing Notification!", DateUtil.format());

		Set<String> userIds = redisPublishUtil.getUserIds(RedisConstants.PUBSUB_USERS_KEY);
		if(userIds == null) {
			LOGGER.info("No Online Users !!");
			return;
		}
		// Push notification for each user.
		for(String userId : userIds) {
			for(NotifyService notifier : notifyServices) {
				notifier.doNotify(userId);
			}
		}
	}

	//@Scheduled(cron="notify.general.msg.time")
	public void notifyBodySign() {
		plainMsgNotifyService.notifyMsg();
	}
}
