package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

/**
 * RangeFilter
 *
 */
public class RangeFilter extends Filter {

	private Range<?> range;

	public RangeFilter(String field, Range<?> range) {
		super(FilterType.range, field);
		this.setRange(range);
	}

	public Range<?> getRange() {
		return range;
	}

	public void setRange(Range<?> range) {
		this.range = range;
	}
}
