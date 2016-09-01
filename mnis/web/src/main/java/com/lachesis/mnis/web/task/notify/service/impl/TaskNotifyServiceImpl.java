package com.lachesis.mnis.web.task.notify.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.web.task.notify.service.AbstarctNotifyService;

@Component("taskNotifyService")
public class TaskNotifyServiceImpl extends AbstarctNotifyService {
	static final Logger LOGGER = LoggerFactory.getLogger(TaskNotifyServiceImpl.class);
	
	@Autowired private RedisService redisService;
	@Autowired private TaskService taskService;

	@Override
	public void doNotify(String userId) {
		if (userId == null) {
			return;
		}

		int count =  10;//taskService.getTaskCount(userId);
		if (count > 0) {
			String channel = RedisConstants.CNL_NURSE_TASK + userId + ".*";
			redisService.publish(channel, String.valueOf(count));
			LOGGER.info(NOTIFY_CHANNEL_MSG, channel, count);
		}
	}

}
