package com.harmazing.openbridge.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.harmazing.framework.common.controller.AbstractController;

@Controller()
@RequestMapping("/portal")
public class PortalController extends AbstractController {

	@RequestMapping("/*")
	public String welcome(HttpServletRequest request,
			HttpServletResponse response) {
		String path = request.getServletPath();
		String prefix = getUrlPrefix();
		if (path.startsWith(prefix)) {
			path = path.substring(prefix.length());
		}
		return prefix + path;
	}
}
