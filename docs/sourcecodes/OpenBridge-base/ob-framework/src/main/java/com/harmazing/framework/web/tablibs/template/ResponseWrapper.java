package com.harmazing.framework.web.tablibs.template;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper {
	private PrintWriter pwriter;

	public ResponseWrapper(HttpServletResponse response, Writer writer) {
		super(response);
		this.pwriter = new PrintWriter(writer);
	}

	public PrintWriter getWriter() throws IOException {
		return pwriter;
	}
}
