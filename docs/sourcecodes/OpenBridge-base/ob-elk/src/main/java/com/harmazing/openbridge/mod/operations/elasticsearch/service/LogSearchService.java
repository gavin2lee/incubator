package com.harmazing.openbridge.mod.operations.elasticsearch.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.AggVerb;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.AggVerb.AggType;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.BoolFilter;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.DatehistogramAggVerb;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.Filter;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.Range;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.RangeAggVerb;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.RangeFilter;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.Sort;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.StatsAggVerb;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.StatsAggVerb.StatsType;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.TermAggVerb;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.TermFilter;
import com.harmazing.openbridge.mod.operations.elasticsearch.util.JsonUtil;
import com.harmazing.openbridge.mod.operations.elasticsearch.util.LogType;
import com.harmazing.framework.util.LogUtil;
import com.harmazing.framework.web.SystemEvent;
import com.tagtraum.perf.gcviewer.imp.DataReader;
import com.tagtraum.perf.gcviewer.imp.DataReaderSun1_6_0;
import com.tagtraum.perf.gcviewer.imp.GcLogType;
import com.tagtraum.perf.gcviewer.model.GCEvent;
import com.tagtraum.perf.gcviewer.model.GCModel;

@Service
public class LogSearchService implements ApplicationListener<SystemEvent> {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//非线程安全
	public static String sdfformat = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String sdf1format = "yyyy-MM-dd HH:mm:ss";
	
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	public static String sdf2format = "yyyy-MM-dd";
	
	private static Logger logger = Logger.getLogger(LogSearchService.class);

	public JSON searchServiceLogs(LogType logType, String envType, String[] serviceIDs,
			String versionID, String interfaceID, String userID,
			String methodName, String startTime, String endTime,
			String[] termAggr, String statusAggr, String[] stats,
			String interval, String[] sort) throws Exception {

		// Make filters
		List<Filter> filters = makeFilters(logType, envType, serviceIDs,
				versionID, interfaceID, userID,
				methodName, startTime, endTime);

		// Make aggregation verbs
		List<AggVerb> aggverbs = makeAggVerbs(termAggr, statusAggr, stats, interval, sort);

		// Execute aggregation
		ESQuery q = new ESQuery();
		String esResult = q.aggregate(filters, aggverbs);

		return JsonUtil.parse(esResult, aggverbs);
	}

	public JSON getTop5RatioServices(LogType logType, String envType, String[] serviceIDs,
			String startTime, String endTime, String userId, int num) throws Exception {
		return getTopN(logType, envType, serviceIDs, null, null, userId,
				null, startTime, endTime, new String[] {"s"}, new String[] {"stats"},
				new String[] {"ratio", "desc", "" + num});
	}

	public JSON getTopN(LogType logType, String envType, String[] serviceIDs,
			String versionID, String interfaceID, String userID,
			String methodName, String startTime, String endTime,
			String[] termAggr, String[] stats, String[] sort) throws Exception {

		// Make filters
		List<Filter> filters = makeFilters(logType, envType, serviceIDs,
				versionID, interfaceID, userID,
				methodName, startTime, endTime);

		// Make aggregation verbs
		List<AggVerb> aggverbs = makeAggVerbs(termAggr, null, stats, null, sort);

		// Execute aggregation
		ESQuery q = new ESQuery();
		String esResult = q.aggregateOrderByRatio(filters, aggverbs);
		JSONArray logJson = (JSONArray)JsonUtil.parse(esResult, aggverbs);

		return getTop(logJson, sort);
	}

	private JSONArray getTop(JSONArray json, String[] sort) {

		if (sort == null || sort.length < 3) {
			return null;
		}
		boolean asc = "asc".equals(sort[1]);
		int num = Integer.parseInt(sort[2]);

		Map<String, Double> all = new HashMap<String, Double>();
		List<Entry<String, Double>> ratios = new ArrayList<Entry<String, Double>>();
		for (int i = 0; i < json.size(); i++) {
			double d = json.getJSONObject(i).getJSONObject("value").getDouble("_cnt");
			all.put(json.getJSONObject(i).getString("key"), d);
		}
		for (Entry<String, Double> entry : all.entrySet()) {
			ratios.add(entry);
		}
		Collections.sort(ratios, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				if (o1.getValue() == o2.getValue()) {
					return 0;
				} else if (o1.getValue() > o2.getValue()) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		// Get top N
		JSONArray result = new JSONArray();
		if (asc) {
			for (int i = 0; i < num && i < ratios.size(); i++) {
				JSONObject rec = new JSONObject();
				rec.put("key", ratios.get(i).getKey());
				JSONObject rvalue = new JSONObject();
				rvalue.put("_ratio", ratios.get(i).getValue());
				rec.put("value", rvalue);
				result.add(rec);
			}
		} else {
			for (int i = 0; i < num && i < ratios.size(); i++) {
				JSONObject rec = new JSONObject();
				rec.put("key", ratios.get(ratios.size() - i - 1).getKey());
				JSONObject rvalue = new JSONObject();
				rvalue.put("_ratio", ratios.get(ratios.size() - i - 1).getValue());
				rec.put("value", rvalue);
				result.add(rec);
			}
		}

		return result;
	}

	private List<Filter> makeFilters(LogType logType, String envType, String[] serviceIDs,
			String versionID, String interfaceID, String userID,
			String methodName, String startTime, String endTime) throws ParseException {

		// Make filters
		List<Filter> filters = new ArrayList<Filter>();

		// Term filters
		if (logType != null) {
			switch (logType) {
			case API_DUBBO_LOG:
				filters.add(new TermFilter("type.raw", logType.getType()));
				break;
			case API_LOG:
				filters.add(new TermFilter("type.raw", logType.getType()));
				break;
			case ALL:
			default:
				break;
			}
		}
		if (envType != null) {
			filters.add(new TermFilter("env_type.raw", envType));
		}
		if (serviceIDs != null && serviceIDs.length == 1) {
			filters.add(new TermFilter("service_id.raw", serviceIDs[0]));
		}
		if (versionID != null) {
			filters.add(new TermFilter("version_id.raw", versionID));
		}
		if (interfaceID != null) {
			filters.add(new TermFilter("interface_id.raw", interfaceID));
		}
		if (methodName != null) {
			filters.add(new TermFilter("method_name.raw", methodName));
		}
		if (userID != null) {
			filters.add(new TermFilter("user_id.raw", userID));
		}

		// Range filters
		SimpleDateFormat sdf = sdf1;
		if (startTime != null && endTime != null) {
			try {
				Range<Object> dateRange = new Range<Object>(
						sdf.parse(startTime), sdf.parse(endTime));
				filters.add(new RangeFilter("@timestamp", dateRange));
			} catch (ParseException e) {
				LogUtil.error("日期格式不正确", e);
				throw e;
			}
		}

		// Boolean filters
		if (serviceIDs != null && serviceIDs.length > 1) {
			Filter[] subfilters = new Filter[serviceIDs.length];
			for (int i = 0; i < serviceIDs.length; i++) {
				subfilters[i] = new TermFilter("service_id.raw", serviceIDs[i]);
			}
			filters.add(new BoolFilter(subfilters));
		}

		return filters;
	}

	private List<AggVerb> makeAggVerbs(String[] termAggr, String statusAggr, String[] stats,
			String interval, String[] sort) throws ParseException {

		// Make aggregation verbs
		List<AggVerb> aggverbs = new ArrayList<AggVerb>();

		// Term verbs
		AggVerb termverb = null;
		if (termAggr != null) {
			for (String aggr : termAggr) {
				if ("s".equals(aggr)) {
					termverb = new TermAggVerb(AggType.term, "service_id.raw");
				} else if ("v".equals(aggr)) {
					termverb = new TermAggVerb(AggType.term, "version_id.raw");
				} else if ("i".equals(aggr)) {
					termverb = new TermAggVerb(AggType.term, "interface_id.raw");
				} else if ("m".equals(aggr)) {
					termverb = new TermAggVerb(AggType.term, "method_name.raw");
				}
				// Add sort verb
				if (sort != null && sort.length >= 1) {
					Sort sortVerb = new Sort(StatsAggVerb.getStatsType(sort[0]));
					if (sort.length >= 2) {
						sortVerb.setAsc("asc".equals(sort[1]));
					}
					if (sort.length >= 3) {
						((TermAggVerb) termverb).setSize(Integer
								.parseInt(sort[2]));
					} else {
						((TermAggVerb) termverb).setSize(5);
					}
					((TermAggVerb) termverb).setSort(sortVerb);
				}
				aggverbs.add(termverb);
			}
		}

		// Range verbs
		AggVerb rangeverb = null;
		if (statusAggr != null) {
			Map<String, Range<?>> ranges = new HashMap<String, Range<?>>();
			if ("detail".equals(statusAggr)) {
				ranges.put("success", new Range<Double>((double) 200,
						(double) 300));
				ranges.put("failure", new Range<Double>((double) 500,
						(double) 600));
				ranges.put("400", new Range<Double>((double) 400, (double) 401));
				ranges.put("401", new Range<Double>((double) 401, (double) 402));
				ranges.put("402", new Range<Double>((double) 402, (double) 403));
				ranges.put("403", new Range<Double>((double) 403, (double) 404));
				ranges.put("404", new Range<Double>((double) 404, (double) 405));
				ranges.put("all",
						new Range<Double>((double) 100, (double) 1000));
			} else if ("binary".equals(statusAggr)) {
				ranges.put("success", new Range<Double>((double) 200,
						(double) 201));
				ranges.put("all",
						new Range<Double>((double) 100, (double) 1000));
			}
			rangeverb = new RangeAggVerb(AggType.range, "status", ranges, true);
			aggverbs.add(rangeverb);
		}

		// Date histogram verbs
		if (interval != null) {
			DatehistogramAggVerb dateHistogramVerb = new DatehistogramAggVerb(
					AggType.datehistogram, "@timestamp", "yyyy/MM/dd HH:mm", interval);
			aggverbs.add(dateHistogramVerb);
		}

		// Statistics verbs
		if (stats != null) {
			StatsAggVerb statsverb = new StatsAggVerb(AggType.stats, "request_time");
			for (String stat : stats) {
				if (StatsAggVerb.AVG.equals(stat)) {
					statsverb.getStatsTypes().add(StatsType.avg);
				} else if (StatsAggVerb.MAX.equals(stat)) {
					statsverb.getStatsTypes().add(StatsType.max);
				} else if (StatsAggVerb.MIN.equals(stat)) {
					statsverb.getStatsTypes().add(StatsType.min);
				} else if (StatsAggVerb.SUM.equals(stat)) {
					statsverb.getStatsTypes().add(StatsType.sum);
				} else if (StatsAggVerb.CNT.equals(stat)) {
					statsverb.getStatsTypes().add(StatsType.cnt);
				} else if (StatsAggVerb.RATIO.equals(stat)) {
					statsverb.getStatsTypes().add(StatsType.ratio);
					statsverb.setField("success");
				}
			}
			aggverbs.add(statsverb);
		}

		return aggverbs;
	}
	
	/**
	 * 获取gc日志信息
	 * @param beginTime
	 * @param endTime
	 * @param appId
	 * @param containerId
	 * @return
	 */
	public Map<String,Object> jvmGc(String beginTime,String endTime,String appId,String containerId){
		Map<String,Object> result = new HashMap<String, Object>();
		try{
			ESQuery query = new ESQuery();
			List<QueryBuilder> r = new ArrayList();
			QueryBuilder  r1 = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("containid", containerId)).filter(QueryBuilders.rangeQuery("@timestamp")
							.from(beginTime+" +0800")
							.to(endTime+" +0800")
							.includeLower(true)
							.includeUpper(false)
							.format("yyyy-MM-dd HH:mm:ss.SSS Z")
							.includeLower(false));
			r.add(r1);
//			r.add(QueryBuilders.termQuery("containid", containerId));
//			r.add(QueryBuilders.rangeQuery("@timestamp")
//							.from(beginTime+" +0800")
//							.to(endTime+" +0800")
//							.includeLower(true)
//							.includeUpper(false)
//							.format("yyyy-MM-dd HH:mm:ss.SSS Z")
//							.includeLower(false));
			List<FieldSortBuilder> fb = new ArrayList<FieldSortBuilder>();
			fb.add(SortBuilders.fieldSort("@timestamp").order(SortOrder.ASC));
			SearchResponse response = query.commonSearch(new String[]{"logstash-*"}, new String[]{LogType.APP_GC_LOG.getType()}, 
					r, null, fb, 0, 1000);
			
			result.put("code", response.status().getStatus());
			StringBuffer sb =new StringBuffer();
			if(response.getHits() !=null && response.getHits().getHits()!=null){
				for(SearchHit s : response.getHits().getHits()){
					sb.append(s.getSource().get("message")).append("\n");
				}
				ByteArrayInputStream ba = new ByteArrayInputStream(sb.toString().getBytes());
				DataReader reader = new DataReaderSun1_6_0(ba, GcLogType.SUN1_6);
				GCModel model = reader.read();
				List data = new ArrayList();
				
				Date begin = null;
				begin = sdf.parse(beginTime);
				Long latest = begin.getTime();
				
				if(model!=null && model.getEvents()!=null){
					Iterator<GCEvent> i = model.getGCEvents();
					while(i.hasNext()){
						GCEvent c = i.next();
						Long m = c.getDatestamp().getLong(ChronoField.INSTANT_SECONDS)*1000+c.getDatestamp().get(ChronoField.MICRO_OF_SECOND)/1000;
						latest = m;
						Map p = new HashMap();
						p.put("x", m);
						p.put("y", c.getPreUsed()/1024.00);
						data.add(p);
						Map p1 = new HashMap();
						p1.put("x", c.getPause()+m);
						p1.put("y", c.getPostUsed()/1024.00);
						data.add(p1);
					}
				}
				result.put("latestSencode", latest);
				result.put("latest", sdf.format(new Date(latest)));
				result.put("data", data);
						
			}
			return result;
		}
		catch(UnknownHostException e){
			throw new RuntimeException("无法识别logstash地址",e);
		}
		catch(UnsupportedEncodingException e1){
			throw new RuntimeException("编码格式不支持",e1);
		}
		catch(IOException e2){
			throw new RuntimeException("读取流报错",e2);
		}
		catch(ParseException e3){
			throw new RuntimeException("解析报错",e3);
		}
	}

	@Override
	public void onApplicationEvent(SystemEvent event) {
		if (event.getType().equals(SystemEvent.STARTUP)) {
			try {
				ESQuery.connect();
			} catch (Exception e) {
				LogUtil.error("ElasticSearch 链接错误", e);
			}
		}
		if (event.getType().equals(SystemEvent.SHUTDOWN)) {
			ESQuery.disconnect();
		}
	}
}
