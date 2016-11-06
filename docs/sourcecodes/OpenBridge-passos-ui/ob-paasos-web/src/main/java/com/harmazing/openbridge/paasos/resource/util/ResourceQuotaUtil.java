package com.harmazing.openbridge.paasos.resource.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.NumberUtil;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.openbridge.paas.util.StorageUtil;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;
import com.harmazing.openbridge.paasos.project.model.vo.TwoTuple;

public class ResourceQuotaUtil {
	
	protected static final Log logger = LogFactory
			.getLog(ResourceQuotaUtil.class);
	
	public static TwoTuple<Boolean, String> isSatisfy(ResourceQuota apply,
			ResourceQuota used,ResourceQuota max,boolean defaultValue,String envType,String tenantId,String ...category){
		IPaaSTenantService iPaaSTenantService = SpringUtil.getBean(IPaaSTenantService.class);
		
		if(used.getCount()==null){
			used.setCount(0L);
		}
		if(used.getCpu()==null){
			used.setCpu(0.0);
		}
		if(used.getMemory()==null){
			used.setMemory(0.0);
		}
		if(used.getStorage()==null){
			used.setStorage(0.0);
		}
		if(max==null){
			String categoryType = category.length >= 1 ? category[0] : null;
			String subCategoryType=category.length >= 2 ? category[1] : null;
			String itemLookupType = category.length >= 3 ? category[2] : null;
			List<Map<String, String>> r = iPaaSTenantService.getTenantQuotaInfo(tenantId,envType,
					categoryType,subCategoryType,itemLookupType);
			max = new ResourceQuota();
			if(r!=null && r.size() !=0){
				for(Map<String, String> ref : r ){
					String lookupTypes = ref.get("itemLookupType");
					logger.debug("------------------环境");
					System.out.println("------------------环境");
					String sq = ref.get("quota")==null?"0":(ref.get("quota")+"");
					logger.debug(sq);
					System.out.println(sq);
					
					Double quota = Double.parseDouble(sq);
					if(lookupTypes.indexOf("_count")>-1){
						//默认是个数
						max.setCount(Long.parseLong(sq));
					}
					else if(lookupTypes.indexOf("_memory")>-1){
						//默认是内存
						max.setMemory(Double.parseDouble(NumberUtil.rounded(quota*1024, 4)));
					}
					else if(lookupTypes.indexOf("_volume")>-1){
						//默认是存储
						max.setStorage(Double.parseDouble(NumberUtil.rounded(quota*1024, 4)));
					}
					else if(lookupTypes.indexOf("_cpu")>-1){
						//默认是CPU
						max.setCpu(quota);
					}
				}
			}
			else{
				return new TwoTuple<Boolean, String>(defaultValue, "该类型没有设置配额["+itemLookupType+"]");
			}
		}
		JSONObject message = new JSONObject();
		boolean satisfy = true;
		if(max.getCount()!=null){
			message.put("实例个数上限", max.getCount());
			message.put("实例已申请", used.getCount());
			if(apply != null){
				message.put("实例本次申请", apply.getCount());
				if((used.getCount()+apply.getCount())>max.getCount()){
					satisfy = false;
				}
			}
			else{
				if(used.getCount() > max.getCount()){
					satisfy = false;
				}
			}
		}
		if(max.getStorage()!=null){
			message.put("容量上限", max.getStorage()+"M");
			message.put("容量已申请", used.getStorage()+"M");
			if(apply != null){
				message.put("容量本次申请", apply.getStorage()+"M");
				if((used.getStorage()+apply.getStorage())>max.getStorage()){
					satisfy = false;
				}
			}
			else{
				if(used.getStorage() > max.getStorage()){
					satisfy = false;
				}
			}
		}
		if(max.getMemory()!=null){
			message.put("内存上限", max.getMemory()+"M");
			message.put("内存已申请", used.getMemory()+"M");
			if(apply != null){
				message.put("内存本次申请", apply.getMemory()+"M");
				if((used.getMemory()+apply.getMemory())>max.getMemory()){
					satisfy = false;
				}
			}
			else{
				if(used.getMemory() > max.getMemory()){
					satisfy = false;
				}
			}
		}
		if(max.getCpu()!=null){
			message.put("CPU上限", max.getCpu());
			message.put("CPU已申请", used.getCpu());
			if(apply != null){
				message.put("CPU本次申请", apply.getCpu());
				if((used.getCpu()+apply.getCpu())>max.getCpu()){
					satisfy = false;
				}
			}
			else{
				if(used.getCpu() > max.getCpu()){
					satisfy = false;
				}
			}
		}
		String m = message.toJSONString();
		logger.debug(m);
		return new TwoTuple<Boolean, String>(satisfy, m);
	}

}
