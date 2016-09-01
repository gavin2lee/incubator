package com.lachesis.mnisqm.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.module.system.domain.SysUser;
/**
 * 拦截器。拦截除登录外的所有接口
 * @author Administrator
 *
 */
public class CommonInterceptor extends HandlerInterceptorAdapter{
	Logger log = LoggerFactory.getLogger(CommonInterceptor.class);
	
	/**
	 * 接口执行前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		SysUser user = WebContextUtils.getSessionUserInfo();
		if(user == null){
			throw new CommRuntimeException(Constants.LoginError,"请重新登录!");
		}
		//记录接口开始时间
		Long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		return true;
	}
	
	/**
	 * 接口执行后
	 */
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		//获取接口开时间
		Long startTime = (Long) request.getAttribute("startTime");
		//获取接口结束时间
		Long endTime = System.currentTimeMillis();
		//接口执行时间
		Long exeTime = endTime - startTime;
		log.info("["+request.getRequestURI()+"]execTime:"+exeTime + "ms");
	}
}