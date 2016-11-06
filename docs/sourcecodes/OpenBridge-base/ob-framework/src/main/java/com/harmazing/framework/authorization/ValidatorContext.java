package com.harmazing.framework.authorization;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.framework.util.WebUtil;

public class ValidatorContext {
	private HttpServletRequest request;
	private String validator;
	private Map<String, String> params;
	private String requestUrl;
	private IUser user;

	public ValidatorContext(String validator, IUser user) {
		this.validator = validator;
		this.user = user;
	}
	
	public ValidatorContext(String validator, HttpServletRequest request) {
		this.validator = validator;
		this.request = request;
		this.user = WebUtil.getUserByRequest(request);
	}

	public ValidatorContext(IUser user, String requestUrl) {
		this.requestUrl = requestUrl;
		this.user = user;
		this.validator = ValidatorUtil.findMatchValidatorByUrl(this.requestUrl);
	}

	public ValidatorContext(HttpServletRequest request) {
		this.request = request;
		this.requestUrl = ValidatorUtil.getRequestUrl(request, true);
		this.user = WebUtil.getUserByRequest(request);
		this.validator = ValidatorUtil.findMatchValidatorByUrl(this.requestUrl);
	}

	public IUser getUser() {
		return user;
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public String getRequestUrl() {
		return requestUrl;
	}
	public void setValidator(String validator) {
		this.validator = validator;
	}

	public String getValidator() {
		return validator;
	}

	public Map<String, String> getParams() {
		if (this.params == null) {
			this.params = new HashMap<String, String>();
		}
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void addParam(String key, String value) {
		if (this.params == null) {
			this.params = new HashMap<String, String>();
		}
		this.params.put(key, value);
	}
}
