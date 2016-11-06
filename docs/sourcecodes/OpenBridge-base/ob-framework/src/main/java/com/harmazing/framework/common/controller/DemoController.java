package com.harmazing.framework.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController extends AbstractController {
	@RequestMapping("/upload")
	public String upload(HttpServletRequest request,
			HttpServletResponse response) {
		return getUrlPrefix() + "/upload";
	}
}
