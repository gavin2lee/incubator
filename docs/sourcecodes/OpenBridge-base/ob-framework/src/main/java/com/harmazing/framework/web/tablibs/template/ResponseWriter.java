package com.harmazing.framework.web.tablibs.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

public class ResponseWriter extends Writer {
	private JspWriter jspWriter;
	private StringWriter strWriter;
	private Map<String, JspContent> contents;
	private String template;
	private ResponseWriter parent;

	public String getBlockFlag(String key) {
		return "======" + key + "======";
	}

	public ResponseWriter(ResponseWriter parent, JspWriter writer) {
		this.parent = parent;
		init(writer);
	}

	public ResponseWriter getParent() {
		return parent;
	}

	private void init(JspWriter writer) {
		this.jspWriter = writer;
		this.contents = new LinkedHashMap<String, JspContent>();
		this.strWriter = new StringWriter();
		this.template = "";
	}

	public ResponseWriter(JspWriter writer) {
		init(writer);
	}

	@Override
	public void close() throws IOException {
		jspWriter.close();
	}

	@Override
	public void flush() throws IOException {
		jspWriter.flush();
	}

	public void start() throws IOException {
		this.template = strWriter.toString().trim();
		strWriter = null;
	}

	public void end() throws IOException {
		Map<String, JspContent> jsps = contents;
		Iterator<String> xxx = jsps.keySet().iterator();
		while (xxx.hasNext()) {
			String yyy = xxx.next();
			JspContent content = jsps.get(yyy);
			if (content.getStatus().equals("replace")) {
				processReplace(content);
			} else if (this.parent == null
					&& content.getStatus().equals("block")) {
				processReplace(content);
			}
		}
		jspWriter.write(template);
	}

	private void processReplace(JspContent content) throws IOException {
		content.setStatus("delete");
		template = template.replace(getBlockFlag(content.getName()),
				content.getBody() != null ? content.getBody().trim() : "");
	}

	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		strWriter.write(arg0, arg1, arg2);
	}

	public void addBlockContent(String key, String content) {
		contents.put(key, new JspContent(key, content, "block"));
	}

	public void superTag(String key, BodyContent content) throws IOException {
		if (contents.containsKey(key)) {
			content.append(contents.get(key).getBody());
		}
	}

	public void replace(String key, BodyContent content) throws IOException {
		if (contents.containsKey(key)) {
			contents.get(key).setStatus("replace");
			contents.get(key).setBody(
					content == null ? "" : content.getString());
		}
	}

	public void release(final PageContext context) {
		final ResponseWriter ptc = this.parent;
		if (ptc != null) {
			TemplateUtil.safePut(context, ptc);
		} else {
			TemplateUtil.safePut(context, null);
		}
	}
}
