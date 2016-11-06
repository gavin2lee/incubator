package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

/**
 * Aggregation Verb for term
 *
 */
public class TermAggVerb extends AggVerb {

	private int size = 0;

	private Sort sort = null;

	public TermAggVerb(AggType aggType, String field) {
		super(aggType, field);
	}

	public TermAggVerb(AggType aggType, String field, boolean addCnt, boolean isParent) {
		super(aggType, field, addCnt, isParent);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
}
