package com.lachesis.mnis.core.redis;

import java.io.Serializable;

import redis.clients.jedis.JedisPubSub;

/**
 *@author xin.chen
 *
 */
public class Subscribe extends JedisPubSub implements Serializable{


	private static final long serialVersionUID = -8889531169528281854L;

	@Override
	public void onMessage(String channel, String message) {
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
	}

	@Override
	public void onPSubscribe(String pattern, int subChannels) {
	}

	@Override
	public void onPUnsubscribe(String pattern, int subChannels) {
	}

	@Override
	public void onSubscribe(String channel, int subChannels) {
	}

	@Override
	public void onUnsubscribe(String channel, int subChannels) {
	}

}
