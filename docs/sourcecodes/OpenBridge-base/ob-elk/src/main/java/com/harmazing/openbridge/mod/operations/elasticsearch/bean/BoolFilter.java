package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

/**
 * BoolFilter
 *
 */
public class BoolFilter extends Filter {

	private Filter[] filters;

	public BoolFilter(Filter[] filters) {
		super(FilterType.bool);
		this.setFilters(filters);
	}

	public BoolFilter(String field, Filter[] filters) {
		super(FilterType.bool, field);
		this.setFilters(filters);
	}

	public Filter[] getFilters() {
		return filters;
	}

	public void setFilters(Filter[] filters) {
		this.filters = filters;
	}
}
