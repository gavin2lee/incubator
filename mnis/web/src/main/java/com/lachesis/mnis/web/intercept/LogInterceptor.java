package com.lachesis.mnis.web.intercept;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 系统日志拦截器
 * 
 * @author yuliang.xu
 * 
 */
@Component
@Aspect
public class LogInterceptor {

	static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
		
	@Around("execution(public * com.lachesis.mnis.web.*.*(..))")
	public Object invoke(ProceedingJoinPoint  joinPoint) throws Throwable {
		Long startTime = System.currentTimeMillis();
		logger.info("ACTION START: THREAD-"+startTime+"  " + joinPoint.getTarget().getClass()
				+ "." + joinPoint.getSignature().getName() + "("+ Arrays.toString(joinPoint.getArgs())+")  ");
		Object o = joinPoint.proceed();
		Long costTime = System.currentTimeMillis() - startTime;
		logger.info("ACTION   END : THREAD-"+startTime+", CONSUME TIME: " + costTime +"ms");
		return o;
	}
}
