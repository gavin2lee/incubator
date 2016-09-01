package com.lachesis.mnis.web.intercept;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.web.common.vo.BaseDataVo;

/**
 * 用户登录成功后，保存订阅配置信息
 * @author xin.chen
 *
 */
@Aspect
@Component
public class LoginSubInterceptor {
	private BaseDataVo result;
	private UserInformation account;
	private String userId;
	private static Logger LOGGER = LoggerFactory.getLogger(LoginSubInterceptor.class);
	@Autowired
	private RedisService redisService;

	@Around("execution(* com.lachesis.mnis.web.IndexAction.doNdaLogin*(..))")
	public Object publish(final ProceedingJoinPoint joinPoint) throws Throwable {
		result = (BaseDataVo) joinPoint.proceed();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 登录成功后处理
				if (result.getRslt().equals(MnisConstants.ACKRESULT_SUCCESS)) {
					account = (UserInformation)result.getData();
					userId = String.valueOf(account.getUserId());
					// Store user in pubsub set
					try {
						Jedis jedis = redisService.getRedis();
						//用户订阅
						jedis.sadd(RedisConstants.PUBSUB_USERS_KEY, userId);
						redisService.returnRedis(jedis);

						for (String channel : RedisConstants.LOGINSUB_CHANNELS) {
							redisService.getAndSavePubSubConfig(channel, userId,
									null);
						}
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.error("ProceedingJoinPoint error:" + e.getMessage());
					}
				}
			}
		}).start();
		return result;
	}
}
