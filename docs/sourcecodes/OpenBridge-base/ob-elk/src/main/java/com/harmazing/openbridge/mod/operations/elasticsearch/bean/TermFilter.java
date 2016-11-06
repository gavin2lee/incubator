package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

/**
 * TermFilter
 *
 */
public class TermFilter extends Filter {

	private String value;

	public TermFilter(String field, String value) {
		super(FilterType.term, field);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
