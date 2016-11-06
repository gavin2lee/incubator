package com.harmazing.framework.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.springframework.web.servlet.DispatcherServlet;

import com.harmazing.framework.util.LogUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.framework.web.SystemEvent;

@SuppressWarnings("serial")
public class SafeDispatcherServlet extends HttpServlet {

	@Override
	public void destroy() {
		SystemEvent event = new SystemEvent(WebUtil.getApplicationContext(),
				SystemEvent.SHUTDOWN, WebUtil.getServletContext());
		WebUtil.getApplicationContext().publishEvent(event);
		super.destroy();
	}

	private static final DispatcherServlet delegate = new DispatcherServlet();

	public void init(final ServletConfig config) {
		try {
			delegate.init(config);
			SystemEvent event = new SystemEvent(
					WebUtil.getApplicationContext(), SystemEvent.STARTUP,
					config.getServletContext());
			WebUtil.getApplicationContext().publishEvent(event);
		} catch (Exception t) {
			t.printStackTrace();
			LogUtil.error("Srping Mvc 启动失败", t);
		}
	}

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		delegate.service(req, res);
	}

}
