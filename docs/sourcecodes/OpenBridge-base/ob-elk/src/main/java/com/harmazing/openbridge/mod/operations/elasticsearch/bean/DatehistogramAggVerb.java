package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

/**
 * Aggregation Verb for date histogram
 *
 */
public class DatehistogramAggVerb extends AggVerb {

	private String format;

	private String interval;

	public DatehistogramAggVerb(AggType aggType, String field, String format, String interval) {
		super(aggType, field);
		this.format = format;
		this.interval = interval;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}
}
