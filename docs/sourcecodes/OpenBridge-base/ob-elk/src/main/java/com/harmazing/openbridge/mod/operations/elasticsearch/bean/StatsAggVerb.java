package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregation Verb for statistics
 *
 */
public class StatsAggVerb extends AggVerb {

	public enum StatsType {avg, max, min, sum, cnt, ratio};

	final public static String CNT = "cnt";

	final public static String SUM = "sum";

	final public static String AVG = "avg";

	final public static String MAX = "max";

	final public static String MIN = "min";

	final public static String RATIO = "ratio";

	final public static String SUM_ = "sum_";

	final public static String AVG_ = "avg_";

	final public static String MAX_ = "max_";

	final public static String MIN_ = "min_";

	final public static String CNT_ = "cnt_";

	final public static String RATIO_ = "ratio_";

	final public static String _SUM = "_sum";

	final public static String _AVG = "_avg";

	final public static String _MAX = "_max";

	final public static String _MIN = "_min";

	final public static String _CNT = "_cnt";

	final public static String _RATIO = "_ratio";

	private List<StatsType> statsTypes;

	public StatsAggVerb(AggType aggType, String field) {
		super(aggType, field, true, false);
		this.statsTypes = new ArrayList<StatsType>();
	}

	public static StatsType getStatsType(String statsType) {
		if (AVG.equals(statsType)) {
			return StatsType.avg;
		} else if (MAX.equals(statsType)) {
			return StatsType.max;
		} else if (MIN.equals(statsType)) {
			return StatsType.min;
		} else if (CNT.equals(statsType)) {
			return StatsType.cnt;
		} else if (SUM.equals(statsType)) {
			return StatsType.sum;
		}
		return null;
	}

	public static String getStatsTypeString_(StatsType type) {
		switch (type) {
		case sum:
			return SUM_;
		case avg:
			return AVG_;
		case max:
			return MAX_;
		case min:
			return MIN_;
		case cnt:
			return CNT_;
		case ratio:
			return RATIO_;
		default:
			return null;
		}
	}

	public static String get_statsTypeString(StatsType type) {
		switch (type) {
		case sum:
			return _SUM;
		case avg:
			return _AVG;
		case max:
			return _MAX;
		case min:
			return _MIN;
		case cnt:
			return _CNT;
		case ratio:
			return _RATIO;
		default:
			return null;
		}
	}

	public List<StatsType> getStatsTypes() {
		return statsTypes;
	}

	public void setStatsTypes(List<StatsType> statsTypes) {
		this.statsTypes = statsTypes;
	}
}
