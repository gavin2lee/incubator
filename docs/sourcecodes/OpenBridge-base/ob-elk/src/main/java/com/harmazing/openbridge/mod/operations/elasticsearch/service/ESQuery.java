package com.harmazing.openbridge.mod.operations.elasticsearch.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.pipeline.bucketscript.BucketScriptBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;

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
import com.harmazing.openbridge.mod.operations.elasticsearch.util.LogType;
import com.jcraft.jsch.Logger;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.TermAggVerb;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.TermFilter;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;

public class ESQuery {

	/*
	// Sample aggregation template for top 5 success ratio
	private final String aggrTemplate = "{"
			+ "\"query\" : {"
			+ " \"match_all\": {}"
			+ "},"
			+ "size:0,"
			+ "\"aggs\":{"
			+ " \"term_%aggrterm%\": {"
			+ "  \"terms\": {"
			+ "   \"field\": \"%aggrterm%\""
			+ "  },"
			+ "  \"aggs\": {"
			+ "   \"success\":{"
			+ "    \"filter\" : {"
			+ "     \"terms\" : {"
			+ "      \"status\": [\"200\"]"
			+ "     }"
			+ "    }"
			+ "   },"
			+ "   \"success_ratio\":{"
			+ "    \"bucket_script\": {"
			+ "     \"buckets_path\": {"
			+ "      \"p1\": \"success._count\","
			+ "      \"p2\": \"_count\""
			+ "     },"
			+ "     \"script\": \"p1 / p2\""
			+ "    }"
			+ "   }"
			+ "  }"
			+ " }"
			+ "}"
			+ "}";
	*/

	private static TransportClient client = null;

	private static Log log = LogFactory.getLog(ESQuery.class);
	public String aggregateOrderByRatio(List<Filter> filters, List<AggVerb> aggverbs) throws UnknownHostException {

		if (client == null) {
			connect();
		}
		if (aggverbs == null) {
			return null;
		}

		String queryTerm;
		List<QueryBuilder> qbs = new ArrayList<QueryBuilder>();
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qbs.add(qb);
		// Generate term queries
		if (filters != null) {
			for (Filter filter : filters) {
				switch (filter.getFilterType()) {
				case term:
					qb.must(QueryBuilders.termQuery(filter.getField(), ((TermFilter)filter).getValue()));
					break;
				case range:
					qb.must(QueryBuilders.rangeQuery(filter.getField())
							.from(((RangeFilter)filter).getRange().getStart())
							.to(((RangeFilter)filter).getRange().getEnd()));
					break;
				case bool:
					BoolQueryBuilder subqb = QueryBuilders.boolQuery();
					for (Filter subfilter : ((BoolFilter)filter).getFilters()) {
						subqb.should(QueryBuilders.termQuery(subfilter.getField(), ((TermFilter)subfilter).getValue()));
					}
					qb.must(subqb);
					break;
				default:
					break;
				}
			}
			queryTerm = qb.toString();
		} else {
			queryTerm = QueryBuilders.matchAllQuery().toString();
		}

		// Make aggregation builder
		TermsBuilder tb = AggregationBuilders.terms(
				AggVerb.TERM_ + aggverbs.get(0).getField()).field(aggverbs.get(0).getField());

		Map<String, String> bucketsPathsMap = new HashMap<String, String>();
		//bucketsPathsMap.put("p1", "success._count");
		//bucketsPathsMap.put("p2", "_count");
		BucketScriptBuilder bsb = new BucketScriptBuilder( aggverbs.get(1).getField()+"_ratio" );
		bsb.setBucketsPathsMap(bucketsPathsMap);
		//bsb.script(new Script("p1 / p2"));

		tb.subAggregation(AggregationBuilders.filter("success").filter(QueryBuilders.termQuery("status", "200")))
				.subAggregation(bsb);

		// Execute query
		AggregationBuilder lastAgg = AggregationBuilders
				.terms("success_ratio")
				.field("service_id.raw")
				.size(0)
				.subAggregation(
						AggregationBuilders.filter("ok").filter(QueryBuilders.termQuery("status", "200"))
				);
		List<AbstractAggregationBuilder> abs = new ArrayList<AbstractAggregationBuilder>();
		abs.add(lastAgg);
		//ESQuery es = new ESQuery();
		LogType logType=LogType.API_LOG;
		SearchResponse response = commonSearch(new String[]{"logstash-*"}, new String[]{logType.getType()}, qbs, abs);
		JSONObject Json =JSONObject.parseObject(response.toString()).getJSONObject("aggregations");
		/*Terms last = null;
		last=response.getAggregations().get("success_ratio");
		List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
		int i=0;
		if(last!=null && last.getBuckets()!=null && last.getBuckets().size()>0){
			for(org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket b : last.getBuckets()){
				Map<String,Object> m = new HashMap<String,Object>();
				String key = (String) b.getKeyAsString();
				m.put("key", key);
				long count = b.getDocCount();
				m.put("value", count);
					Aggregations as = b.getAggregations();
					Map<String, Aggregation> param = as.asMap();
					Filter s = (Filter) param.get("ok");
					//m.put("ok", s.value());		
				//r.add(m);
				//i++;
			}
		}*/
		return response.toString();
	//	return last.toString();
		/*synchronized (client) {
			SearchResponse sr = client.prepareSearch("logstash*")
					.setSearchType(SearchType.QUERY_THEN_FETCH)
					.setQuery(queryTerm)
					.setSize(0)
					.addAggregation(tb)
					.execute().actionGet();
			return sr.toString();
		}*/
	}
	public SearchResponse commonSearch(String[] index,String[] type,
			List<QueryBuilder> builders,List<AbstractAggregationBuilder> aggs) throws UnknownHostException{
		return commonSearch(index,type,builders,aggs,null,null,null);
	}
	public SearchResponse commonSearch(String[] index,String[] type,
			List<QueryBuilder> builders,List<AbstractAggregationBuilder> aggs,
			List<FieldSortBuilder> sorts,Integer from,Integer size) throws UnknownHostException{
		if (client == null) {
			connect();
		}
		SearchRequestBuilder main = client.prepareSearch(index)
						.setTypes(type)
						.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		if(builders!=null && builders.size()>0){
			for(QueryBuilder qb : builders){
				main.setQuery(qb);
			}
		}
		if(aggs !=null && aggs.size()>0){
			for(AbstractAggregationBuilder agg : aggs){
				main.addAggregation(agg);
			}
		}
		if(sorts!=null && sorts.size()>0){
			for(FieldSortBuilder sb : sorts){
				main.addSort(sb);
			}
		}
		if(from!=null){
			main.setFrom(from.intValue());
		}
		if(size!=null){
			main.setSize(size.intValue());
		}
		synchronized (client) {
			return main.execute().actionGet();
		}
	}
	
	public String aggregate(List<Filter> filters, List<AggVerb> aggverbs) throws UnknownHostException {

		if (client == null) {
			connect();
		}
		if (aggverbs == null || aggverbs.size() == 0) {
			return null;
		}

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// Generate term queries
		if (filters != null) {
			for (Filter filter : filters) {
				switch (filter.getFilterType()) {
				case term:
					qb.must(QueryBuilders.termQuery(filter.getField(), ((TermFilter)filter).getValue()));
					break;
				case range:
					qb.must(QueryBuilders.rangeQuery(filter.getField())
							.from(((RangeFilter)filter).getRange().getStart())
							.to(((RangeFilter)filter).getRange().getEnd()));
					break;
				case bool:
					BoolQueryBuilder subqb = QueryBuilders.boolQuery();
					for (Filter subfilter : ((BoolFilter)filter).getFilters()) {
						subqb.should(QueryBuilders.termQuery(subfilter.getField(), ((TermFilter)subfilter).getValue()));
					}
					qb.must(subqb);
					break;
				default:
					break;
				}
			}
		}
		// Generate aggregation terms
		AbstractAggregationBuilder[] aggTerms = new AbstractAggregationBuilder[aggverbs.size()];
		AggregationBuilder<?> tempParent = null;
		for (int i = 0; i < aggverbs.size(); i++) {
			if (aggverbs.get(i) == null) {
				continue;
			}
			if (aggverbs.get(i).getAggType() == AggType.stats) {
				AbstractAggregationBuilder[] statsAggTerms = getAggregationBuilders((StatsAggVerb)aggverbs.get(i));
				for (AbstractAggregationBuilder term : statsAggTerms) {
					if (tempParent != null) {
						tempParent.subAggregation(term);
					}
				}
				// Sort by avg, max, min
				if (i != 0 && tempParent != null && aggverbs.get(i - 1).getAggType() == AggType.term) {
					Sort sort = ((TermAggVerb)aggverbs.get(i - 1)).getSort();
					if (sort != null && sort.getSortMode() != StatsType.cnt) {
						((TermsBuilder)tempParent).order(Terms.Order.aggregation(
								StatsAggVerb.getStatsTypeString_(sort.getSortMode()) + sort.getField(), sort.isAsc()));
					}
				}
			} else {
				aggTerms[i] = getAggregationBuilder(aggverbs.get(i));
				if (tempParent != null) {
					tempParent.subAggregation(aggTerms[i]);
				}
				tempParent = (AggregationBuilder<?>) aggTerms[i];
			}
		}

		// Execute query
		synchronized (client) {
			SearchResponse sr = client.prepareSearch("logstash*")
					.setSearchType(SearchType.QUERY_THEN_FETCH)
					.setQuery(qb)
					.setSize(0)
					.addAggregation(aggTerms[0])
					.execute().actionGet();
			return sr.toString();
		}
	}

	private AbstractAggregationBuilder[] getAggregationBuilders(StatsAggVerb aggverb) {
		AbstractAggregationBuilder[] aggTerms = new AbstractAggregationBuilder[aggverb.getStatsTypes().size()];
		for (int i = 0; i < aggverb.getStatsTypes().size(); i++) {
			switch (aggverb.getStatsTypes().get(i)) {
			case avg:
				aggTerms[i] = AggregationBuilders.avg(StatsAggVerb.AVG_ + aggverb.getField()).field(aggverb.getField());
				break;
			case max:
				aggTerms[i] = AggregationBuilders.max(StatsAggVerb.MAX_ + aggverb.getField()).field(aggverb.getField());
				break;
			case min:
				aggTerms[i] = AggregationBuilders.min(StatsAggVerb.MIN_ + aggverb.getField()).field(aggverb.getField());
				break;
			default:
				continue;
			}
		}
		return aggTerms;
	}

	@SuppressWarnings("unchecked")
	private AbstractAggregationBuilder getAggregationBuilder(AggVerb aggverb) {
		switch (aggverb.getAggType()) {
		case term:
			TermsBuilder tb = AggregationBuilders.terms(AggVerb.TERM_ + aggverb.getField()).field(aggverb.getField());
			// Sort by count
			Sort sort = ((TermAggVerb)aggverb).getSort();
			if (sort != null && sort.getSortMode() == StatsType.cnt) {
				tb.order(Terms.Order.count(sort.isAsc()));
			}
			// Set size
			tb.size(((TermAggVerb)aggverb).getSize());
			return tb;
		case range:
			RangeBuilder rb =  AggregationBuilders.range(AggVerb.RANGE_ + aggverb.getField()).field(aggverb.getField());
			for (Map.Entry<String, Range<?>> range : ((RangeAggVerb)aggverb).getRanges().entrySet()) {
				rb.addRange(range.getKey(), ((Range<Double>)range.getValue()).getStart(), ((Range<Double>)range.getValue()).getEnd());
			}
			return rb;
		case datehistogram:
			return AggregationBuilders.dateHistogram(AggVerb.DATEHISTOGRAM_ + aggverb.getField()).field(aggverb.getField())
					.format(((DatehistogramAggVerb)aggverb).getFormat()).minDocCount(0)
					.order(Order.KEY_ASC)
					.interval(new DateHistogramInterval(((DatehistogramAggVerb)aggverb).getInterval()));
		default:
			return null;
		}
	}

	@SuppressWarnings("null")
	public static void connect() throws UnknownHostException {
		Settings settings = Settings.settingsBuilder().put("cluster.name", ConfigUtil.getConfigString("elasticsearch.cluster")).build();
		try {
			String esHostsStr = ConfigUtil.getConfigString("elasticsearch.url");
			String[] esHosts = esHostsStr.split(";");
			if (esHosts == null || esHosts.length == 0) {
				throw new RuntimeException("No elasticsearch.url in property file.");
			}
			client = TransportClient.builder().settings(settings).build();
			for (String esHost : esHosts) {
				String[] port=esHost.split(":");
				 String addr=port[0];
				 int por=Integer.parseInt(port[1]);
				client.addTransportAddress(
						new InetSocketTransportAddress(InetAddress.getByName(addr),  por ));
								//InetAddress.getByName(esHost),
						//ConfigUtil.getConfigInt("elasticsearch.port", 9300)));
			}
		} catch (Exception e) {
			log.error(e);
			client=null;
			throw e;
		}
	}

	public static void disconnect() {
		client.close();
		client = null;
	}
}
