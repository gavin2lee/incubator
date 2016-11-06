package com.harmazing.framework.authorization;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.harmazing.framework.authorization.exceptions.AuthorizationException;
import com.harmazing.framework.authorization.filter.SecurityConfigVO;
import com.harmazing.framework.authorization.filter.SecurityConfigVO.Module;
import com.harmazing.framework.authorization.filter.SecurityConfigVO.Url;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.StringUtil;

public class ValidatorUtil {

	private static SecurityConfigVO configVO = null;
	private static PathMatcher matcher = new AntPathMatcher();
	private static String anonymousPaths[];

	public static String[] getAnonymousPaths() {
		return anonymousPaths;
	}

	public static void setAnonymousPaths(String anonymousPaths) {
		ValidatorUtil.anonymousPaths = anonymousPaths.trim().split("\\s*[,;]\\s*");
	}

	public static void setSecurityConfigVO(SecurityConfigVO vo) {
		configVO = vo;
	}

	public static SecurityConfigVO getSecurityConfigVO() {
		return configVO;
	}

	public static String findMatchValidatorByUrl(String requestURL) {
		Url url = findMatchUrl(requestURL);
		if (url != null) {
			return url.getValidator();
		} else {
			return null;
		}
	}

	private static Url findMatchUrl(String requestURL) {
		if (configVO != null && configVO.getModules() != null) {
			for (int i = 0; i < configVO.getModules().size(); i++) {
				Url url = findMatchUrlInModule(configVO.getModules().get(i), requestURL);
				if (url != null)
					return url;
			}
		}
		return null;
	}

	private static Url findMatchUrlInModule(Module module, String requestURL) {
		if (requestURL.startsWith(module.getPrefix())) {
			for (int i = 0; i < module.getModules().size(); i++) {
				Url url = findMatchUrlInModule(module.getModules().get(i), requestURL);
				if (url != null)
					return url;
			}
			for (int j = 0; j < module.getUrls().size(); j++) {
				Url url = module.getUrls().get(j);
				if (matcher.match(module.getPrefix() + url.getPattern(), requestURL)) {
					return url;
				}
			}
		}
		return null;
	}

	private static boolean isAnonymousPath(String requestURL) {
		if (requestURL.equals("/"))
			return true;
		if (anonymousPaths == null || anonymousPaths.length == 0) {
			return false;
		} else {
			for (int i = 0; i < anonymousPaths.length; i++) {
				if (matcher.match(anonymousPaths[i], requestURL)) {
					return true;
				}
			}
			return false;
		}
	}

	private static boolean isAdministrator(IUser user) {
		if (user != null)
			return user.isAdministrator();
		else
			return false;
	}

	public static boolean checkRequest(HttpServletRequest request) throws AuthorizationException {
		return execute(new ValidatorContext(request));
	}

	public static boolean checkValidator(IUser user, String validator) throws AuthorizationException {
		return execute(new ValidatorContext(validator, user));
	}

	public static boolean checkValidator(HttpServletRequest request, String validator) throws AuthorizationException {
		return execute(new ValidatorContext(validator, request));
	}

	public static boolean checkRequestURL(IUser user, String requestURL) throws AuthorizationException {
		return execute(new ValidatorContext(user, requestURL));
	}

	public static String getRequestUrl(HttpServletRequest request, boolean includeQueryString) {
		String pathInfo = request.getPathInfo();
		String queryString = request.getQueryString();
		String uri = request.getServletPath();
		if (uri == null) {
			uri = request.getRequestURI();
			uri = uri.substring(request.getContextPath().length());
		}
		String temp = uri + ((pathInfo == null) ? "" : pathInfo);
		if (includeQueryString) {
			temp = temp + ((queryString == null) ? "" : new StringBuffer().append("?").append(queryString).toString());
		}
		return temp;
	}

	public static boolean execute(ValidatorContext context) throws AuthorizationException {
		String requestURL = context.getRequestUrl();
		IUser user = context.getUser();
		if (StringUtil.isNotNull(requestURL)) {
			if (isAnonymousPath(requestURL)) {
				return true;
			}
		}
		if (user != null) {
			if (isAdministrator(user)) {
				return true;
			}
		}
		// 这个地方强制所有页面都必须登陆后才能访问。
		if (user.isAnonymous()) {
			return false;
		} else {
			// 表达式为空表示通过
			if (StringUtil.isNull(context.getValidator())) {
				return true;
			} else {
				List<?> validatorExpression = getValidatorExpressionList(context.getValidator());
				return runValidatorExpression(validatorExpression.get(0).toString(), context, validatorExpression);
			}
		}
	}

	/**
	 * 进行资源校验
	 * 
	 * @param validatorExpression
	 *            校验表达式
	 * @param validatorContext
	 *            上下文
	 * @param vList
	 *            表达式列表
	 * @return
	 * @throws Exception
	 */
	private static boolean runValidatorExpression(String validatorExpression, ValidatorContext validatorContext,
			List<?> vList) throws AuthorizationException {
		int i, j;
		String[] serviceOr, serviceAnd;
		boolean result;
		// 先处理&逻辑再处理|逻辑
		serviceOr = validatorExpression.trim().split("\\s*\\|\\s*");
		for (i = 0; i < serviceOr.length; i++) {
			serviceAnd = serviceOr[i].split("\\s*&\\s*");
			for (j = 0; j < serviceAnd.length; j++) {
				if (serviceAnd[j].equals("true"))
					result = true;
				else if (serviceAnd[j].equals("false"))
					result = false;
				else if (serviceAnd[j].startsWith("{")) {
					// {n}表示该项具体内容存放在vList.get(n)中
					int index = Integer.parseInt(serviceAnd[j].substring(1, serviceAnd[j].length() - 1));
					result = runValidatorExpression(vList.get(index).toString(), validatorContext, vList);
				} else {
					// 执行某一项表达式内容
					validatorContext.setValidator(serviceAnd[j]);
					result = executeContext(validatorContext);
					validatorContext.setParams(null);
				}
				if (!result)
					break;
			}
			if (j == serviceAnd.length)
				return true;
		}
		return false;
	}

	/**
	 * 解释校验表达式，支持用[]进行优先级处理<br>
	 * 样例：<br>
	 * 输入：[a(1)|b(2)]&c(3)<br>
	 * 输出：{1}&c(3), a(1)|b(2)<br>
	 * 其中{1}表示该项具体内容存放在list.get(1)中
	 * 
	 * @param validatorExpression
	 *            校验表达式
	 * @return 解释结果
	 */
	private static List<?> getValidatorExpressionList(String validatorExpression) {
		List rtnList = new ArrayList();
		List lvList = new ArrayList();
		int lv = 0;
		StringBuffer s = new StringBuffer();
		rtnList.add(s);
		lvList.add(s);
		for (int i = 0; i < validatorExpression.length(); i++) {
			char c = validatorExpression.charAt(i);
			switch (c) {
			case '[':
				lv++;
				if (lvList.size() > lv)
					lvList.set(lv, new StringBuffer());
				else
					lvList.add(new StringBuffer());
				break;
			case ']':
				rtnList.add(lvList.get(lv));
				lv--;
				((StringBuffer) lvList.get(lv)).append("{" + (rtnList.size() - 1) + "}");
				break;
			default:
				((StringBuffer) lvList.get(lv)).append(c);
			}
		}
		return rtnList;
	}

	private static boolean executeContext(ValidatorContext context) throws AuthorizationException {
		String validator = context.getValidator();
		if (StringUtil.isNotNull(validator)) {
			int pstart = validator.indexOf("(");
			int pend = validator.indexOf(")");
			String beanName = "";
			String params = "";
			if (pstart > 0) {
				beanName = validator.substring(0, pstart);
				params = validator.substring(pstart + 1, pend);
				String[] xx = StringUtil.split(params);
				for (int i = 0; i < xx.length; i++) {
					String p = xx[i];
					String[] pv = p.split("=");
					String val = null;
					if (pv.length > 1)
						val = expressionEvaluation(pv[1], context.getRequest());
					context.addParam(pv[0], val);
				}
			} else {
				beanName = validator;
			}
			IValidator alidator = (IValidator) SpringUtil.getBean(beanName);
			try {
				return alidator.validate(context);
			} catch (AuthorizationException e) {
				throw e;
			} catch (Exception e) {
				throw new AuthorizationException(e);
			}
		} else {
			throw new AuthorizationException("权限表达式为空");
		}
	}

	private static String expressionEvaluation(String el, HttpServletRequest reqeust) {
		if (el == null)
			return null;
		if (el.indexOf("${") < 0)
			return el;

		return replaceArgs(el, reqeust);
	}

	public static String replaceArgs(String template, HttpServletRequest reqeust) {
		StringBuffer sb = new StringBuffer();
		try {
			Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
			Matcher matcher = pattern.matcher(template);
			while (matcher.find()) {
				String name = matcher.group(1);
				String value = "";
				if (name.startsWith("request.param.")) {
					String pname = name.substring("request.param.".length()).trim();
					value = reqeust.getParameter(pname) == null ? "" : reqeust.getParameter(pname).toString();
				}
				if (value == null) {
					value = "";
				} else {
					value = value.replaceAll("\\$", "\\\\\\$");
				}
				matcher.appendReplacement(sb, value);
			}
			matcher.appendTail(sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
