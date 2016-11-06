package com.harmazing.openbridge.mod.operations.elasticsearch.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.harmazing.framework.util.NumberUtil;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.AppMonitorForm;
import com.harmazing.openbridge.mod.operations.elasticsearch.service.IElasticSearchService;
import com.harmazing.openbridge.mod.operations.elasticsearch.service.IMonitorService;
import com.harmazing.openbridge.mod.operations.elasticsearch.service.LogSearchService;
import com.harmazing.openbridge.mod.operations.elasticsearch.util.LogType;

public class MonitorServiceImpl implements IMonitorService {
	
	private static Logger logger = Logger.getLogger(MonitorServiceImpl.class);
	
	@Autowired
	private IElasticSearchService iElasticSearchService;
	
	@Override
	public Map<String, Object>  cost(AppMonitorForm form, String type) {
		Map<String, Object> result =new HashMap<String, Object>();
		if(StringUtils.isEmpty(form.getBeginDate())){
			Calendar begin = Calendar.getInstance();
			begin.set(Calendar.HOUR_OF_DAY, 0);
			begin.set(Calendar.MINUTE, 0);
			begin.set(Calendar.SECOND, 0);
			begin.set(Calendar.MILLISECOND, 0);
			String beginTime = LogSearchService.sdf.format(begin.getTime());
			form.setBeginDate(beginTime);
		}
		if(StringUtils.isEmpty(form.getEndDate())){
			Calendar end = Calendar.getInstance();
			form.setEndDate(LogSearchService.sdf.format(end.getTime()));
		}
		logger.debug(form.getAppId()+"  "+form.getBeginDate()+"  "+ form.getEndDate());
		
		
		if(!StringUtils.hasText(form.getXtype())){
			//如果相差时间小于1天横坐标就按分计算
			//如果相差时间小于3天 横坐标就按小时算
			//否则时间大于3天 横坐标就按天计算
			try{
				Date begin = LogSearchService.sdf.parse(form.getBeginDate());
				Date end = LogSearchService.sdf.parse(form.getEndDate());
				long c = end.getTime()-begin.getTime();
				if(c < 2*60*60*1000){
					form.setXtype("m");
				}
				else if(c < 2*24*60*60*1000){
					form.setXtype("h");
				}
				else {
					form.setXtype("d");
				}
				logger.debug(c+"  "+ form.getXtype());
			}
			catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		result.put("beginDate", form.getBeginDate());
		result.put("endDate", form.getEndDate());
		result.put("xtype", form.getXtype());
		
		try{
			result.put("data",iElasticSearchService.cost(form, LogType.APP_LOG));
		}
		catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("还没创建运行环境，请先创建运用环境后再查看监控信息。");
		}
		//获取总坐标
		return result;
	}

	@Override
	public Map<String,Object> top(AppMonitorForm form,String type,LogType logType){
	
		Map<String, Object> result =new HashMap<String, Object>();
		if(StringUtils.isEmpty(form.getBeginDate())){
			Calendar begin = Calendar.getInstance();
			begin.set(Calendar.HOUR_OF_DAY, 0);
			begin.set(Calendar.MINUTE, 0);
			begin.set(Calendar.SECOND, 0);
			begin.set(Calendar.MILLISECOND, 0);
			String beginTime = LogSearchService.sdf.format(begin.getTime());
			form.setBeginDate(beginTime);
		}
		if(StringUtils.isEmpty(form.getEndDate())){
			Calendar end = Calendar.getInstance();
			form.setEndDate(LogSearchService.sdf.format(end.getTime()));
		}
		logger.debug(form.getAppId()+"  "+form.getBeginDate()+"  "+ form.getEndDate());
		
		if(form.getTop()==0){
			form.setTop(10);
		}
		
		result.put("beginDate", form.getBeginDate());
		result.put("endDate", form.getEndDate());
		
		try{
			List<Map<String,Object>> r= null;
			if(logType==null){
				r = iElasticSearchService.top(form, type,LogType.APP_LOG);
			}
			else{ 
				r = iElasticSearchService.top(form, type,logType);
			}
			if(type.equals("success_ratio") && form.getTop()!=-1){
				if(r!=null && r.size()!=0){
					Collections.sort(r, new RatioComparator() );
					r = r.subList(0, r.size() > form.getTop() ? form.getTop() : r.size());
				}
				if(r!=null && r.size()!=0){
					for(Map<String,Object> info : r){
						double o1Total = Double.parseDouble(info.get("value")+"");
						double o1Ok = Double.parseDouble(info.get("ok")+"");
						if(o1Total==0){
							continue ;
						}
						info.put("value", NumberUtil.rounded((o1Ok/o1Total)*100+"", 3));
					}
				}
			}
			//需要修改
			if(r!=null && r.size()>0 && form.getTop() !=-1){
				Map<String,String> ref = getKeyMap();
				for(Map<String,Object> r1 : r){
					String appName = ref.get(r1.get("key")+"");
					r1.put("keyName", StringUtils.hasText(appName)?appName : r1.get("key") );
				}
			}
			
			result.put("data",r);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("还没创建运行环境，请先创建运用环境后再查看监控信息。");
		}
		//获取总坐标
		return result;
	}
	@Override
	public Map<String, Object> top(AppMonitorForm form, String type) {
		return top( form,  type,null);
	}
	
	protected Map<String,String> getKeyMap(){
		return new HashMap<String,String>();
	}

	private static class RatioComparator implements Comparator<Map<String, Object>>{

		@Override
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			double o1Total = Double.parseDouble(o1.get("value")+"");
			double o1Ok = Double.parseDouble(o1.get("ok")+"");
			
			double o2Total = Double.parseDouble(o2.get("value")+"");
			double o2Ok = Double.parseDouble(o2.get("ok")+"");
			
			if(o1Total==0){
				return -1;
			}
			if(o2Total==0){
				return 1;
			}
			double m = o1Ok/o1Total - o2Ok/o2Total ;
			return m<0?-1 : 1;
		}
		
	}

	

}
