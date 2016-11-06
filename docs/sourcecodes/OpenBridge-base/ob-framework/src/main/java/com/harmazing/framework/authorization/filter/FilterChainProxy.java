package com.harmazing.framework.authorization.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.harmazing.framework.util.UrlUtils;

public class FilterChainProxy implements Filter {

	private static final Log logger = LogFactory.getLog(FilterChainProxy.class);

	private List<Filter> filters;

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		for (int i = 0; i < filters.size(); i++)
			filters.get(i).init(filterConfig);
	}

	public void destroy() {
		for (int i = 0; i < filters.size(); i++)
			filters.get(i).destroy();
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		if (filters == null || filters.size() == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug(UrlUtils.buildRequestUrl(request)
						+ (filters == null ? " has no matching filters"
								: " has an empty filter list"));
			}
			chain.doFilter(request, response);
		} else {
			VirtualFilterChain vfc = new VirtualFilterChain(chain, filters);
			vfc.doFilter(request, response);
		}
	}

	private static class VirtualFilterChain implements FilterChain {
		private final FilterChain originalChain;
		private final List<Filter> additionalFilters;
		private final int size;
		private int currentPosition = 0;

		private VirtualFilterChain(FilterChain chain,
				List<Filter> additionalFilters) {
			this.originalChain = chain;
			this.additionalFilters = additionalFilters;
			this.size = additionalFilters.size();
		}

		public void doFilter(ServletRequest request, ServletResponse response)
				throws IOException, ServletException {
			if (currentPosition == size) {
				originalChain.doFilter(request, response);
			} else {
				currentPosition++;
				Filter nextFilter = additionalFilters.get(currentPosition - 1);
				nextFilter.doFilter(request, response, this);
			}
		}
	}
}
