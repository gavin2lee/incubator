package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

/**
 * Filter
 *
 */
public abstract class Filter {

	public enum FilterType {term, range, bool};

	final public static String TERM = "term";

	final public static String RANGE = "range";

	final public static String BOOL = "bool";

	private FilterType filterType;

	private String field;

	public Filter(FilterType filterType) {
		super();
		this.filterType = filterType;
	}

	public Filter(FilterType filterType, String field) {
		super();
		this.filterType = filterType;
		this.field = field;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
