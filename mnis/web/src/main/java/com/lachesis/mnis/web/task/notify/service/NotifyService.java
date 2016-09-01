package com.lachesis.mnis.web.task.notify.service;

public interface NotifyService {
	
	String NOTIFY_CHANNEL_MSG = "channel: {} , msg: {}";
	
	/**
	 * Push notification to nurse with userId
	 * @param userId
	 */
	void doNotify(String userId);
}
