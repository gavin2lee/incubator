package com.lachesis.mnisqm.module.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnisqm.core.utils.GsonUtils;
import com.lachesis.mnisqm.module.system.dao.SysDicMapperExt;
import com.lachesis.mnisqm.module.system.domain.SysDic;
import com.lachesis.mnisqm.module.system.service.ICacheService;

@Service
public class CacheServiceImpl implements ICacheService{
	
	private Logger log = LoggerFactory.getLogger(CacheServiceImpl.class);
	
	public static Map<String,List<SysDic>> dicCache =  new HashMap<String, List<SysDic>>();
	
	@Autowired
	private SysDicMapperExt dicMapper;
	
	/**
	 * 数据初始化
	 */
	@PostConstruct
	public void init(){
		//获取数据字典的缓存
		List<SysDic> dics =  dicMapper.selectAllDics();
		log.debug(GsonUtils.toJson(dics));
		if(null != dics){
			for(SysDic dic : dics){
				String dicType = dic.getDicType();
				List<SysDic> dicList = dicCache.get(dicType);
				if(null == dicList){
					dicList = new ArrayList<SysDic>();
				}
				dicList.add(dic);
				//缓存信息存放
				dicCache.put(dicType, dicList);
			}
			//是否缓存
			dics = null;
		}
	}
	
	/**
	 * 获取缓存中的数据字典
	 * @param dicType
	 * @return
	 */
	public List<SysDic> getSysDics(String dicType){
		List<SysDic> dics = dicCache.get(dicType);
		return dics;
	}
	
	/**
	 * 获取缓存中的数据字典
	 * 以MAP的形式输出
	 * @param dicType
	 * @return
	 */
	public Map<String,SysDic> getSysDicMap(String dicType){
		Map<String,SysDic> dicMap = new HashMap<String,SysDic>();
		List<SysDic> dics = dicCache.get(dicType);
		if(null != dics){
			for(SysDic dic : dics){
				dicMap.put(dic.getDicCode(), dic);
			}
		}
		dics = null;
		return dicMap;
	}
	
	/**
	 * 获取指定的数据字典
	 * @param dicType
	 * @param dicCode
	 * @return
	 */
	public SysDic getSysDic(String dicType,String dicCode){
		SysDic sysDic = null;
		List<SysDic> dics = dicCache.get(dicType);
		if(null != dics){
			for(SysDic dic : dics){
				if(dic.getDicCode().equals(dicCode)){
					sysDic = dic;
				}
			}
		}
		dics = null;
		return sysDic;
	}
	
	/**
	 * 获取指定的数据字典
	 * @param dicType
	 * @param dicCode
	 * @return
	 */
	public String getSysDicValue(String dicType,String dicCode){
		String dicName = null;
		List<SysDic> dics = dicCache.get(dicType);
		if(null != dics){
			for(SysDic dic : dics){
				if(dic.getDicCode().equals(dicCode)){
					dicName = dic.getDicName();
				}
			}
		}
		dics = null;
		return dicName;
	}

	@Override
	public List<String> queryDicTypes() {
		return dicMapper.queryDicTypes();
	}
}