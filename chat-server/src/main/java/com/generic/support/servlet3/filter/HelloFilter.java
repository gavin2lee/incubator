package com.generic.support.servlet3.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName="helloFilter", value="/*")
public class HelloFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(HelloFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("doFilter");
		chain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
