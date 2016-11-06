package com.harmazing.framework.web.tablibs.template;

import javax.servlet.jsp.PageContext;

public class TemplateUtil {
	private static final String BLOCK_REPLACE_VARIBLE_KEY = "tag_template_block_replace_varible";

	public static ResponseWriter safeGet(PageContext context) {
		return (ResponseWriter) context.getAttribute(BLOCK_REPLACE_VARIBLE_KEY,
				PageContext.REQUEST_SCOPE);
	}

	public static void safePut(PageContext context, ResponseWriter writer) {
		context.setAttribute(BLOCK_REPLACE_VARIBLE_KEY, writer,
				PageContext.REQUEST_SCOPE);
	}

	public static boolean hasContext(final PageContext context) {
		return (safeGet(context) != null);
	}

	public static ResponseWriter get(final PageContext context) {
		final ResponseWriter tc = safeGet(context);
		if (tc == null) {
			throw new RuntimeException("ResponseWriter is not exist !");
		}
		return tc;
	}

	public static ResponseWriter getTop(final PageContext context) {
		final ResponseWriter tc = safeGet(context);
		if (tc == null) {
			throw new RuntimeException("ResponseWriter is not exist !");
		}
		if (tc.getParent() == null) {
			return tc;
		} else {
			ResponseWriter xobj = tc.getParent();
			while (xobj.getParent() != null) {
				xobj = xobj.getParent();
			}
			return xobj;
		}
	}
}
