package com.harmazing.openbridge.web.tablibs.ui;

import java.util.HashMap;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.web.tablibs.ComponentTag;

public class PaginationTag extends ComponentTag {
	private String href;

	public String getHref() {
		return StringUtil.isNull(href) ? "?" : href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getCss() {
		return StringUtil.isNull(css) ? "table_page" : css;
	}

	public String getTag() {
		return StringUtil.isNull(tag) ? "ul" : tag;
	}

	public String getStyle() {
		return StringUtil.isNull(style) ? "" : style;
	}

	private Object data;

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void release() {
		this.data = null;
		this.href = null;
		super.release();
	}

	private String getPageScript(int no, Page<?> p) {
		String temp = this.getHref();
		if (this.getHref().startsWith("javascript:")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pageNo", no);
			map.put("pageSize", p.getPageSize());
			map.put("recordCount", p.getRecordCount());
			return StringUtil.replaceArgs(temp, map);
		} else {
			int wen = this.getHref().indexOf("?");
			if (wen >= 0) {
				if (this.getHref().indexOf("=", wen) >= 0) {
					temp += "&pageNo=" + no + "&pageSize=" + p.getPageSize();
				} else {
					temp += "pageNo=" + no + "&pageSize=" + p.getPageSize();
				}
			} else {
				temp += "?pageNo=" + no + "&pageSize=" + p.getPageSize();
			}
			if (this.getHref().startsWith("/")) {
				return getContentPath() + temp;
			} else {
				return temp;
			}
		}
	}

	private void getPageItemHtml(int x, Page<?> p, StringBuilder sb) {
		String className = "";
		if (x == p.getPageNo()) {
			className = " class='active'";
		} else {
			className = "";
		}
		sb.append("<li" + className + "><a href='");
		if (x > 0) {
			sb.append(getPageScript(x, p));
		} else {
			sb.append("javascript:void(0)");
		}
		sb.append("'>");
		if (x > 0) {
			sb.append(x);
		} else {
			sb.append("...");
		}
		sb.append("</a></li>");
	}

	private String pagging() throws Exception {
		StringBuilder sb = new StringBuilder();
		Page<?> p = (Page<?>) this.data;
		boolean isFirst = (p.getPageNo() == 1);
		boolean isLast = (p.getPageNo() == p.getPageCount());
		boolean needShow = (p != null && p.getRecordCount() > 0);

		if (false) {
			sb.append("<li");
			if (isFirst) {
				sb.append(" class='disabled'");
			}
			sb.append("><a href='"
					+ getPageScript(1, p)
					+ "'><i class='ace-icon fa fa-angle-double-left'></i></a></li>");
		}
		if (needShow) {
			if (p.getPageCount() < 8) {
				for (int i = 0; i < p.getPageCount(); i++) {
					getPageItemHtml((i + 1), p, sb);
				}
			} else {
				// 总页数大于等于8
				if (p.getPageNo() <= 4) {
					for (int i = 0; i < 4; i++) {
						getPageItemHtml((i + 1), p, sb);
					}
					getPageItemHtml(5, p, sb);
					getPageItemHtml(-1, p, sb);
					getPageItemHtml(p.getPageCount(), p, sb);
				} else {
					getPageItemHtml(1, p, sb);
					getPageItemHtml(-1, p, sb);
					getPageItemHtml(p.getPageNo() - 1, p, sb);
					getPageItemHtml(p.getPageNo(), p, sb);
					if ((p.getPageCount() - p.getPageNo()) > 3) {
						getPageItemHtml(p.getPageNo() + 1, p, sb);
						getPageItemHtml(-1, p, sb);
						getPageItemHtml(p.getPageCount(), p, sb);
					} else {
						for (int i = p.getPageNo() + 1; i <= p.getPageCount(); i++) {
							getPageItemHtml(i, p, sb);
						}
					}
				}
			}
		}
		if (false) {
			sb.append("<li");
			if (isLast) {
				sb.append(" class='disabled'");
			}
			sb.append("><a href='" + getPageScript(p.getPageCount(), p)
					+ "'><i class='fa fa-angle-double-right'></i></a></li>");
		}
		StringBuilder html = new StringBuilder();
		html.append("<" + this.getTag() + ">");
		html.append(sb.toString());
		html.append("</" + this.getTag() + ">");
		return html.toString();
	}

	protected String acquireString(String body) throws Exception {
		StringBuffer sb = new StringBuffer();
		Page<?> p = (Page<?>) this.data;
		sb.append("<div " + this.buildAttribute() + ">");
		sb.append("<div class='pull-right pages_mun'>");
		sb.append(pagging());
		sb.append("</div>");
		sb.append("<div class='pull-left pages_info'>");
		sb.append("<span>"); 
		sb.append(String.format(" 页码 %s/%s 共 %s", p.getPageNo(),
				p.getPageCount(), p.getRecordCount()));
		sb.append("</span>");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString() + body;
	}
}
