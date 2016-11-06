package com.harmazing.framework.common;

import java.util.ArrayList;
import java.util.Map;

public class Page<T> extends ArrayList<T> {

	private static final long serialVersionUID = 1L;
	private int start;
	private int pageSize;
	private int recordCount;
	private int pageNo;

	public Page() {

	}

	public static <T> Page<T> create(Map<String, Object> params) {
		Page<T> xpage = new Page<T>(Integer.valueOf(params.get("pageNo")
				.toString()),
				Integer.valueOf(params.get("pageSize").toString()));

		params.put("start", xpage.getStart());
		params.put("size", xpage.getPageSize());

		return xpage;
	}

	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.start = (pageNo - 1) * pageSize;
	}

	public int getPageCount() {
		int x = recordCount / pageSize;
		return recordCount % pageSize == 0 ? x : x + 1;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "{Page recordCount:" + recordCount + ", list:"
				+ super.toString() + "}";
	}
}
