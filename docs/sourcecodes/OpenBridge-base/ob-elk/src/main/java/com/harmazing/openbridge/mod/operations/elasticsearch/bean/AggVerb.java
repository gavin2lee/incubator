package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

/**
 * Aggregation Verb
 *
 */
public abstract class AggVerb {

	public enum AggType {term, range, datehistogram, stats};

	final public static String TERM = "term";

	final public static String RANGE = "range";

	final public static String DATEHISTOGRAM = "datehistogram";

	final public static String TERM_ = "term_";

	final public static String RANGE_ = "range_";

	final public static String DATEHISTOGRAM_ = "datehistogram_";

	final public static String _TERM = "_term";

	final public static String _RANGE = "_range";

	final public static String _DATEHISTOGRAM = "_datehistogram";

	private AggType aggType;

	private String field;

	private boolean addCnt;

	private boolean isParent;

	public AggVerb(AggType aggType, String field) {
		super();
		this.aggType = aggType;
		this.field = field;
		this.addCnt = false;
		this.isParent = true;
	}

	public AggVerb(AggType aggType, String field, boolean addCnt, boolean isParent) {
		super();
		this.aggType = aggType;
		this.field = field;
		this.addCnt = addCnt;
		this.isParent = isParent;
	}

	public String getTypeString_() {
		switch (this.aggType) {
		case term:
			return TERM_;
		case range:
			return RANGE_;
		case datehistogram:
			return DATEHISTOGRAM_;
		default:
			return null;
		}
	}

	public String get_typeString() {
		switch (this.aggType) {
		case term:
			return _TERM;
		case range:
			return _RANGE;
		case datehistogram:
			return _DATEHISTOGRAM;
		default:
			return null;
		}
	}

	public static AggType getAggType(String aggType) {
		if (TERM.equals(aggType)) {
			return AggType.term;
		} else if (DATEHISTOGRAM.equals(aggType)) {
			return AggType.datehistogram;
		} else if (RANGE.equals(aggType)) {
			return AggType.range;
		}
		return null;
	}

	public AggType getAggType() {
		return aggType;
	}

	public void setAggType(AggType aggType) {
		this.aggType = aggType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public boolean isAddCnt() {
		return addCnt;
	}

	public void setAddCnt(boolean addCnt) {
		this.addCnt = addCnt;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
}
