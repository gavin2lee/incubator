package com.harmazing.openbridge.mod.operations.elasticsearch.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.AggVerb;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.StatsAggVerb;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.StatsAggVerb.StatsType;

public class JsonUtil {

	public static JSON parse(String esResult, List<AggVerb> aggVerbs) {
		if (JSONObject.parseObject(esResult).containsKey("aggregations")) {
			JSONObject aggrJson = JSONObject.parseObject(esResult).getJSONObject("aggregations");
			if (aggrJson != null) {
				return extract(aggrJson, aggVerbs, 0);
			} else {
				return new JSONArray();
			}
		} else {
			return new JSONArray();
		}
	}

	private static JSON extract(JSONObject srcJson, List<AggVerb> aggVerbs, int currLvl) {
		JSONObject dstJson = new JSONObject();
		JSONArray subListJson = null;
		JSONArray dstSubListJson = new JSONArray();
		if (srcJson != null) {
			if (aggVerbs.get(currLvl).isAddCnt()) {
				Integer cnt = srcJson.getInteger("doc_count");
				dstJson.put("_cnt", cnt);
				if (aggVerbs.get(currLvl).isParent()) {
					dstSubListJson.add(dstJson);
				}
			}
			switch (aggVerbs.get(currLvl).getAggType()) {
			case stats:
				for (StatsType type : ((StatsAggVerb)(aggVerbs.get(currLvl))).getStatsTypes()) {
					Object value = srcJson.getJSONObject(StatsAggVerb.getStatsTypeString_(type) + aggVerbs.get(currLvl).getField()).get("value");
					dstJson.put(StatsAggVerb.get_statsTypeString(type), value);
				}
				break;
			case term:
				subListJson = srcJson.getJSONObject(
						aggVerbs.get(currLvl).getTypeString_() + aggVerbs.get(currLvl).getField()
				).getJSONArray("buckets");
				for (int i = 0; i < subListJson.size(); i++) {
					dstJson = new JSONObject();
					if (currLvl < aggVerbs.size() - 1) {
						dstJson.put("key", ((JSONObject)subListJson.get(i)).getString("key"));
						dstJson.put("value", extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
						//dstJson.put(((JSONObject)subListJson.get(i)).getString("key"),
						//		extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
					} else {
						dstJson.put("key", ((JSONObject)subListJson.get(i)).getString("key"));
						dstJson.put("value", new JSONObject());
						//dstJson.put(((JSONObject)subListJson.get(i)).getString("key"),
						//		new JSONObject());
					}
					if (aggVerbs.get(currLvl).isParent()) {
						dstSubListJson.add(dstJson);
					}
				}
				//dstJson.put("_key", ((JSONObject)subListJson.get(i)).getString("key"));
				//dstJson.put("_aggr", extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
				break;
			case range:
				subListJson = srcJson.getJSONObject(
						aggVerbs.get(currLvl).getTypeString_() + aggVerbs.get(currLvl).getField()
						).getJSONArray("buckets");
				for (int i = 0; i < subListJson.size(); i++) {
					dstJson = new JSONObject();
					if (currLvl < aggVerbs.size() - 1) {
						dstJson.put("key", ((JSONObject)subListJson.get(i)).getString("key"));
						dstJson.put("value", extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
						//dstJson.put(((JSONObject)subListJson.get(i)).getString("key"),
						//		extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
					} else {
						dstJson.put("key", ((JSONObject)subListJson.get(i)).getString("key"));
						dstJson.put("value", new JSONObject());
						//dstJson.put(((JSONObject)subListJson.get(i)).getString("key"),
						//		new JSONObject());
					}
					if (aggVerbs.get(currLvl).isParent()) {
						dstSubListJson.add(dstJson);
					}
					//dstJson.put("_key", ((JSONObject)subListJson.get(i)).getString("key"));
					//dstJson.put("_aggr", extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
				}
				break;
			case datehistogram:
				subListJson = srcJson.getJSONObject(
						aggVerbs.get(currLvl).getTypeString_() + aggVerbs.get(currLvl).getField()
				).getJSONArray("buckets");
				for (int i = 0; i < subListJson.size(); i++) {
					dstJson = new JSONObject();
					if (currLvl < aggVerbs.size() - 1) {
						dstJson.put("key", ((JSONObject)subListJson.get(i)).getString("key_as_string"));
						dstJson.put("value", extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
						//dstJson.put(((JSONObject)subListJson.get(i)).getString("key_as_string"),
						//		extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
					} else {
						dstJson.put("key", ((JSONObject)subListJson.get(i)).getString("key_as_string"));
						dstJson.put("value", new JSONObject());
						//dstJson.put(((JSONObject)subListJson.get(i)).getString("key_as_string"),
						//		new JSONObject());
					}
					if (aggVerbs.get(currLvl).isParent()) {
						dstSubListJson.add(dstJson);
					}
				}
				//dstJson.put("_key", ((JSONObject)subListJson.get(i)).getString("key_as_string"));
				//dstJson.put("_aggr", extract((JSONObject)subListJson.get(i), aggVerbs, currLvl + 1));
				break;
			default:
				break;
			}
		}

		if (aggVerbs.get(currLvl).isParent()) {
			return dstSubListJson;
		} else {
			return dstJson;
		}
	}
}
