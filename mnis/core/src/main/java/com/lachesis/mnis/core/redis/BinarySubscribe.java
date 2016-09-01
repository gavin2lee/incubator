package com.lachesis.mnis.core.redis;

import java.io.Serializable;

import redis.clients.jedis.BinaryJedisPubSub;

/**
 *@author xin.chen
 *
 */
public class BinarySubscribe extends BinaryJedisPubSub implements Serializable{

	private static final long serialVersionUID = -5647315485014999380L;

	@Override
	public void onMessage(byte[] channel, byte[] message) {
	}

	@Override
	public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
		
	}

	@Override
	public void onPSubscribe(byte[] pattern, int subChannels) {
	}

	@Override
	public void onPUnsubscribe(byte[] pattern, int subChannels) {
		
	}

	@Override
	public void onSubscribe(byte[] channel, int subChannels) {
		
	}

	@Override
	public void onUnsubscribe(byte[] channel, int subChannels) {
		
	}


}
