package com.harmazing.framework.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.util.UrlUtils;
import com.harmazing.framework.util.WebUtil;

public abstract class AbstractController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	protected String SUCCESS = "/common/jsp/success.jsp";
	protected String FAILURE = "/common/jsp/failure.jsp";
	protected String ERROR = "/common/jsp/error.jsp";

	protected String forward(String url) {
		return "forward:" + url;
	}

	protected String redirect(HttpServletRequest request, String url) {
		if (url.startsWith("/"))
			url = UrlUtils.getBasePath(request) + url;
		return "redirect:" + url;
	}

	protected String getUrlPrefix() {
		String temp = "";
		RequestMapping map = this.getClass()
				.getAnnotation(RequestMapping.class);
		if (map != null && map.value() != null && map.value().length > 0) {
			temp = map.value()[0];
		}
		return temp;
	}

	protected IUser getCurrentUser(HttpServletRequest request) {
		return WebUtil.getUserByRequest(request);
	}

}
