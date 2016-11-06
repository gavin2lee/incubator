package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

/**
 * Range
 *
 */
public class Range<T> {

	private Class<T> cls;

	private T start;

	private T end;

	public Range(T start, T end) {
		super();
		this.start = start;
		this.end = end;
	}

	public Class<T> getCls() {
		return cls;
	}

	public void setCls(Class<T> cls) {
		this.cls = cls;
	}

	public T getStart() {
		return start;
	}

	public void setStart(T start) {
		this.start = start;
	}

	public T getEnd() {
		return end;
	}

	public void setEnd(T end) {
		this.end = end;
	}
}
