package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

import java.util.Map;

/**
 * Aggregation Verb for range
 *
 */
public class RangeAggVerb extends AggVerb {

	private Map<String, Range<?>> ranges;

	private boolean hasOther;

	public RangeAggVerb(AggType aggType, String field, Map<String, Range<?>> ranges) {
		super(aggType, field);
		this.ranges = ranges;
		this.setHasOther(false);
	}

	public RangeAggVerb(AggType aggType, String field, Map<String, Range<?>> ranges, boolean hasOther) {
		super(aggType, field);
		this.ranges = ranges;
		this.setHasOther(hasOther);
	}

	public RangeAggVerb(AggType aggType, String field, boolean addCnt, boolean isParent, Map<String, Range<?>> ranges, boolean hasOther) {
		super(aggType, field, addCnt, isParent);
		this.ranges = ranges;
		this.setHasOther(hasOther);
	}

	public Map<String, Range<?>> getRanges() {
		return ranges;
	}

	public void setRanges(Map<String, Range<?>> ranges) {
		this.ranges = ranges;
	}

	public boolean isHasOther() {
		return hasOther;
	}

	public void setHasOther(boolean hasOther) {
		this.hasOther = hasOther;
	}
}
