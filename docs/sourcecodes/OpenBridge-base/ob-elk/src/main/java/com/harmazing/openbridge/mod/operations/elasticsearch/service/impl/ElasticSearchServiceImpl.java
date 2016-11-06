package com.harmazing.openbridge.mod.operations.elasticsearch.service.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.AndQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filters.InternalFilters;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.AppMonitorForm;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.vo.FilterInfoVo;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.vo.FilterVo;
import com.harmazing.openbridge.mod.operations.elasticsearch.service.ESQuery;
import com.harmazing.openbridge.mod.operations.elasticsearch.service.IElasticSearchService;
import com.harmazing.openbridge.mod.operations.elasticsearch.service.LogSearchService;
import com.harmazing.openbridge.mod.operations.elasticsearch.util.LogType;

/**
 * elasticsearch 模块
 * @author admin
 *
 */
@Service
public class ElasticSearchServiceImpl implements IElasticSearchService{
	
	
	private static String[] pics = new String[]{"gif","png","css","js","ico","html"};
//	curl -XPOST '101.200.82.115:9200/logstash-*/app_log/_search?pretty' -d '{
//	"query":{
//	"bool":{
//	"filter":[
//	{
//	"range":{
//	"@timestamp":{
//	"gte":"2016-01-2710:26:40.111 +0800",
//	"lte":"2016-01-2810:26:42.222 +0800",
//	"format":"yyyy-MM-ddHH:mm:ss.SSS Z"
//	}
//	}
//	},
//	{
//	"term":{
//	"env_id":"12313"
//	}
//	}
//	]
//	}
//	},
//	"aggs":{
//	"result_agg":{
//	"date_histogram":{
//	"field":"@timestamp",
//	"interval":"hour",
//	"format":"yyyy-MM-ddHH:mm:ss.SSS Z",
//	"min_doc_count":0,
//	"extended_bounds":{
//	"min":"2016-01-2710:26:40.111 +0800",
//	"max":"2016-01-2810:26:42.222 +0800"
//	}
//	},
//	"aggs":{
//	"total_sum":{
//	"sum":{"field":"request_time"}
//	},
//	"success_sum":{
//	"filter":{
//	"term":{"status":"200"}
//	}
//	},
//	 "total_ip" : {
//    	"cardinality" : {
//        	"field" : "remote_addr.raw"
//        }
//    }
//	}
//	}
//	}
//	}
//	'

	@Override
	public List<Map<String,Object>> cost(AppMonitorForm form, LogType logType) throws UnknownHostException {
		ESQuery query = new ESQuery();
		AggregationBuilder sub1 = AggregationBuilders
				.filter("success_count")
			    .filter(QueryBuilders.termQuery("status", "200"));
		SumBuilder sub2 = AggregationBuilders
				.sum("total_sum")
				.field("request_time");
		CardinalityBuilder sub3 = AggregationBuilders
				.cardinality("total_ip")
				.field("remote_host.raw");
		AggregationBuilder aggregation = AggregationBuilders
                .dateHistogram("result_agg")
                .field("@timestamp")
                .interval(form.getXtype().equals("d")?DateHistogramInterval.DAY : 
                		(form.getXtype().equals("h")?DateHistogramInterval.HOUR:DateHistogramInterval.MINUTE))
                .format(LogSearchService.sdf.toPattern()+" Z")
                .minDocCount(0)
                .extendedBounds(form.getBeginDate()+" +0800", form.getEndDate()+" +0800")
                .subAggregation(sub1).subAggregation(sub2).subAggregation(sub3);
		List<AbstractAggregationBuilder> abs = new ArrayList<AbstractAggregationBuilder>();
		abs.add(aggregation);
		
		List<QueryBuilder> qbs = new ArrayList<QueryBuilder>();
		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		qbs.add(bqb);
		QueryBuilder timeQuery = QueryBuilders.rangeQuery("@timestamp")
				.from(form.getBeginDate()+" +0800")
				.to(form.getEndDate()+" +0800")
				.includeLower(true)
				.includeUpper(false)
				.format(LogSearchService.sdf.toPattern()+" Z");
		bqb.filter(timeQuery);
//		qbs.add(timeQuery);
		if(StringUtils.hasText(form.getAppId())){
			QueryBuilder appId = QueryBuilders.termQuery("service_id", form.getAppId());
			bqb.filter(appId);
		}
		if(StringUtils.hasText(form.getEnvId())){
			QueryBuilder containId = QueryBuilders.termQuery("env_id", form.getEnvId());
			bqb.filter(containId);
		}
		
		if(StringUtils.hasText(form.getShowStaticSource()) && "1".equals(form.getShowStaticSource())){
			AndQueryBuilder qad = QueryBuilders.andQuery();
			for(String pic : pics){
				QueryBuilder aqn1 = QueryBuilders.notQuery(QueryBuilders.termQuery("request_type", pic));
				qad.add(aqn1);
			}
			bqb.filter(qad);
		}
		
		if(StringUtils.hasText(form.getStrfilter()) && !"".equals(form.getStrfilter().trim())){
//			QueryBuilder strFilter = QueryBuilders.notQuery(QueryBuilders.matchQuery("uri", form.getStrfilter()));
//			bqb.filter(strFilter);
			FilterVo filter = JSONObject.parseObject(form.getStrfilter(), FilterVo.class);
			
			if(filter.getFilterc() !=null && filter.getFilterc().size() >0){
				StringBuffer  msg = new StringBuffer();
				for(int i =0 ;i<filter.getFilterc().size();i++){
					FilterInfoVo info = filter.getFilterc().get(i);
					if(i!=0){
						msg.append(" ").append(info.getOperator()).append(" ").append(info.getKeyword());
					}
					else {
						msg.append(info.getKeyword());
					}
				}
				QueryStringQueryBuilder sb = QueryBuilders.queryStringQuery(msg.toString());
				sb.defaultField("uri");
				bqb.filter(sb);
			}
			
			if(filter.getFilternc() !=null && filter.getFilternc().size() >0){
				StringBuffer  msg = new StringBuffer();
				for(int i =0 ;i<filter.getFilternc().size();i++){
					FilterInfoVo info = filter.getFilternc().get(i);
					if(i!=0){
						msg.append(" ").append(info.getOperator()).append(" ").append(info.getKeyword());
					}
					else {
						msg.append(info.getKeyword());
					}
				}
				QueryStringQueryBuilder sb = QueryBuilders.queryStringQuery(msg.toString());
				sb.defaultField("uri");
				bqb.filter(QueryBuilders.notQuery(sb));
			}
		}
		ESQuery es = new ESQuery();
		SearchResponse response1 = es.commonSearch(new String[]{"logstash-*"}, new String[]{logType.getType()}, qbs, abs);
		List<Map<String,Object>> rs = new ArrayList<Map<String,Object>>();
		if(response1.getAggregations()!=null){
			InternalHistogram<Bucket> a = (InternalHistogram)response1.getAggregations().get("result_agg");
			for(Bucket b : a.getBuckets()){
				Map<String,Object> r = new HashMap<String,Object>();
				Aggregations as = b.getAggregations();
				Map<String, Aggregation> param = as.asMap();
				Filter  p1 = (Filter) param.get("success_count");
				Sum p2 = (Sum)param.get("total_sum");
				Cardinality p3 = (Cardinality)param.get("total_ip");
				if(b.getKey() instanceof org.joda.time.DateTime){
					org.joda.time.DateTime dt = (org.joda.time.DateTime)b.getKey();
					r.put("timestamp", dt.getMillis());
				}
				else{
					r.put("timestamp", b.getKey());
				}
				r.put("count", b.getDocCount());
				r.put("success_count", p1.getDocCount());
				r.put("total_sum", p2.getValue());
				r.put("total_ip", p3.getValue());
				rs.add(r);
			}
		}
		return rs;
		
	}

	@Override
	public List<Map<String, Object>> top(AppMonitorForm form, String type,LogType logType)
			throws UnknownHostException {
		ESQuery query = new ESQuery();
		AggregationBuilder lastAgg = null;
		//不能用size方法 排行不准确，具体原因看官网，这边就全部统计出来 然后取最前面的TOP的数据即可
		if(type.equals("frequency")){
			//按次数排行 根据url 用于监控页面
			lastAgg = AggregationBuilders
	                .terms("methodCount")
	                .field("uri.raw")
	                .size(0);
		}
		else if(type.equals("visit_time")){
			//访问次数 根据 service_id 用于看板页面
			lastAgg = AggregationBuilders
	                .terms("visit_time")
	                .field("service_id.raw")
	                .size(0);
		}
		else if(type.equals("elapsed")){
			//访问耗时  根据service_id 用于看板页面
			lastAgg = AggregationBuilders
					.terms("elapsed")
					.field("service_id.raw")
					.size(0)
					.order(Terms.Order.aggregation("sum_request_time", false))
					.subAggregation(
							AggregationBuilders.sum("sum_request_time").field("request_time")
					);
					
		}
		else if(type.equals("total_ip")){
			//访问ip数  根据service_id 用于看板页面
			lastAgg = AggregationBuilders
					.terms("total_ip")
					.field("service_id.raw")
					.size(0)
					.order(Terms.Order.aggregation("total_ip_count", false))
					.subAggregation(
							AggregationBuilders.cardinality("total_ip_count").field("remote_host.raw")
					);
					
		}
		else if(type.equals("success_ratio")){
			//访问成功率  根据service_id 用于看板页面
			lastAgg = AggregationBuilders
					.terms("success_ratio")
					.field("service_id.raw")
					.size(0)
					.subAggregation(
							AggregationBuilders.filter("ok").filter(QueryBuilders.termQuery("status", "200"))
					);
		}
		else if(type.equals("ip")){
			//按IP数排行 remote_addr
			lastAgg = AggregationBuilders
	                .terms("ipCount")
	                .field("remote_host.raw")
	                .size(0);
		}
		else{
			//按耗时排行  
			lastAgg = AggregationBuilders
					.terms("methodTime")
					.field("uri.raw")
					.size(0)
					.order(Terms.Order.aggregation("sum_request_time", false))
					.subAggregation(
				        AggregationBuilders.sum("sum_request_time").field("request_time")
				    );
		}
		List<AbstractAggregationBuilder> abs = new ArrayList<AbstractAggregationBuilder>();
		abs.add(lastAgg);
		
		List<QueryBuilder> qbs = new ArrayList<QueryBuilder>();
		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		qbs.add(bqb);
		QueryBuilder timeQuery = QueryBuilders.rangeQuery("@timestamp")
				.from(form.getBeginDate()+" +0800")
				.to(form.getEndDate()+" +0800")
				.includeLower(true)
				.includeUpper(false)
				.format(LogSearchService.sdf.toPattern()+" Z");
		bqb.filter(timeQuery);
		if(StringUtils.hasText(form.getAppId())){
			QueryBuilder appId = QueryBuilders.termQuery("service_id", form.getAppId());
			bqb.filter(appId);
		}
		if(StringUtils.hasText(form.getEnvId())){
			QueryBuilder containId = QueryBuilders.termQuery("env_id", form.getEnvId());
			bqb.filter(containId);
		}
		if(form.getFilter()!=null){
			for(Map.Entry<String, Object> o : form.getFilter().entrySet()){
				String k = o.getKey();
				Object v = o.getValue();
				QueryBuilder containId = QueryBuilders.termQuery(k, v);
				bqb.filter(containId);
			}
		}
		if(StringUtils.hasText(form.getShowStaticSource()) && "1".equals(form.getShowStaticSource())){
			AndQueryBuilder qad = QueryBuilders.andQuery();
			for(String pic : pics){
				QueryBuilder aqn1 = QueryBuilders.notQuery(QueryBuilders.termQuery("request_type", pic));
				qad.add(aqn1);
			}
			bqb.filter(qad);
		}
		if(StringUtils.hasText(form.getStrfilter()) && !"".equals(form.getStrfilter().trim())){
//			QueryBuilder strFilter = QueryBuilders.notQuery(QueryBuilders.matchQuery("uri", form.getStrfilter()));
//			bqb.filter(strFilter);
			FilterVo filter = JSONObject.parseObject(form.getStrfilter(), FilterVo.class);
//			if(StringUtils.hasText(filter.getFilterpic()) && filter.getFilterpic().equals("n")){
//				AndQueryBuilder qad = QueryBuilders.andQuery();
//				for(String pic : pics){
//					QueryBuilder aqn1 = QueryBuilders.notQuery(QueryBuilders.termQuery("request_type", pic));
//					qad.add(aqn1);
//				}
//				bqb.filter(qad);
//			}
			if(filter.getFilterc() !=null && filter.getFilterc().size() >0){
				StringBuffer  msg = new StringBuffer();
				for(int i =0 ;i<filter.getFilterc().size();i++){
					FilterInfoVo info = filter.getFilterc().get(i);
					if(i!=0){
						msg.append(" ").append(info.getOperator()).append(" ").append(info.getKeyword());
					}
					else {
						msg.append(info.getKeyword());
					}
				}
				QueryStringQueryBuilder sb = QueryBuilders.queryStringQuery(msg.toString());
				sb.defaultField("uri");
				bqb.filter(sb);
			}
			
			if(filter.getFilternc() !=null && filter.getFilternc().size() >0){
				StringBuffer  msg = new StringBuffer();
				for(int i =0 ;i<filter.getFilternc().size();i++){
					FilterInfoVo info = filter.getFilternc().get(i);
					if(i!=0){
						msg.append(" ").append(info.getOperator()).append(" ").append(info.getKeyword());
					}
					else {
						msg.append(info.getKeyword());
					}
				}
				QueryStringQueryBuilder sb = QueryBuilders.queryStringQuery(msg.toString());
				sb.defaultField("uri");
				bqb.filter(QueryBuilders.notQuery(sb));
			}
		}
		ESQuery es = new ESQuery();
		SearchResponse response1 = es.commonSearch(new String[]{"logstash-*"}, new String[]{logType.getType()}, qbs, abs);
		Terms last = null;
		if(response1.getAggregations()!=null){
			if(type.equals("frequency")){
				last = response1.getAggregations().get("methodCount");
			}
			else if(type.equals("ip")){
				//按IP数排行 remote_addr
				last = response1.getAggregations().get("ipCount");
			}
			else if(type.equals("visit_time")){
				last = response1.getAggregations().get("visit_time");
			}
			else if(type.equals("elapsed")){
				last = response1.getAggregations().get("elapsed");
			}
			else if(type.equals("total_ip")){
				last = response1.getAggregations().get("total_ip");
			}
			else if(type.equals("success_ratio")){
				last = response1.getAggregations().get("success_ratio");
			}
			else{
				last = response1.getAggregations().get("methodTime");
			}
		}
		List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
		int i=0;
		if(last!=null && last.getBuckets()!=null && last.getBuckets().size()>0){
			for(org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket b : last.getBuckets()){
				if(form.getTop()!=-1 && i>=form.getTop() && !type.equals("success_ratio")){
					//成功率 全部放进去 然后手动排序
					break ;
				}
				Map<String,Object> m = new HashMap<String,Object>();
				String key = (String) b.getKeyAsString();
				m.put("key", key);
				long count = b.getDocCount();
				m.put("value", count);
				if(type.equals("time")){
					Aggregations as = b.getAggregations();
					Map<String, Aggregation> param = as.asMap();
					Sum s = (Sum) param.get("sum_request_time");
					m.put("value", s.value());
				}
				else if(type.equals("elapsed")){
					Aggregations as = b.getAggregations();
					Map<String, Aggregation> param = as.asMap();
					Sum s = (Sum) param.get("sum_request_time");
					m.put("value", s.value());
				}
				else if(type.equals("total_ip")){
					Aggregations as = b.getAggregations();
					Map<String, Aggregation> param = as.asMap();
					Cardinality s = (Cardinality) param.get("total_ip_count");
					m.put("value", s.value());
				}
//				else if(type.equals("total_ip")){
//					Aggregations as = b.getAggregations();
//					Map<String, Aggregation> param = as.asMap();
//					Cardinality s = (Cardinality) param.get("total_ip_count");
//					m.put("value", s.value());
//				}
				else if(type.equals("success_ratio")){
					Aggregations as = b.getAggregations();
					Map<String, Aggregation> param = as.asMap();
					Filter s = (Filter) param.get("ok");
					m.put("ok", s.getDocCount());
				}
				r.add(m);
				i++;
			}
		}
		return r;
	}

//	{
//		  "aggs" : {
//		    "messages" : {
//		      "filters" : {
//		        "filters" : {
//		          "6pajfn55kl1d7ux5rjvzp8dwsavqiyz" :   { "term" : { "service_id.raw" : "6pajfn55kl1d7ux5rjvzp8dwsavqiyz"   }},
//		          "6p7fg2nomeqglxzg6jx9gquftcdhhyj1" : { "term" : { "service_id.raw" : "6p7fg2nomeqglxzg6jx9gquftcdhhyj1" }}
//		        }
//		      },
//		      "aggs" : {
//		      	 "total_ip" : {
//		         	"cardinality" : {
//		            	"field" : "remote_addr.raw"
//		            } 
//		         }
//		      }
//		    }
//		  }
//		}
	public List<Map<String,Object>> appState(List<String> appIds,LogType logType) throws UnknownHostException{
		List<AbstractAggregationBuilder> abs = new ArrayList<AbstractAggregationBuilder>();
		FiltersAggregationBuilder fb = AggregationBuilders.filters("messages");
		for(String appId : appIds){
			fb.filter(appId, QueryBuilders.termQuery("service_id", appId));
		}
		fb.subAggregation(AggregationBuilders.cardinality("total_ip").field("remote_host.raw"));
		abs.add(fb);
		
		ESQuery es = new ESQuery();
		SearchResponse response1 = es.commonSearch(new String[]{"logstash-*"}, new String[]{logType.getType()}, null, abs);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(response1.getAggregations()!=null){
			InternalFilters last = response1.getAggregations().get("messages");
			if(last!=null && last.getBuckets()!=null){
				for( org.elasticsearch.search.aggregations.bucket.filters.InternalFilters.Bucket  b : last.getBuckets()){
					String key = b.getKey();
					long docCount = b.getDocCount();
					InternalCardinality totalIp = b.getAggregations().get("total_ip");
					long ipCount = totalIp.getValue();
					Map<String,Object> m = new HashMap<String, Object>();
					m.put("key", key);
					m.put("ipPd", ipCount);
					m.put("timePd", docCount);
					result.add(m);
				}
			}
		}
		return result;
	}
	
}
