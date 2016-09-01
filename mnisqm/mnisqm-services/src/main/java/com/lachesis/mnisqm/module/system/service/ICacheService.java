package com.lachesis.mnisqm.module.system.service;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.system.domain.SysDic;

public interface ICacheService {
	/**
	 * 数据字典获取
	 * @param dicType
	 * @return
	 */
	public List<SysDic> getSysDics(String dicType);
	
	/**
	 * 获取数据字典，以map的形式输出
	 * @param dicType
	 * @return
	 */
	public Map<String,SysDic> getSysDicMap(String dicType);
	
	/**
	 * 获取指定的数据字典
	 * @param dicType
	 * @param dicCode
	 * @return
	 */
	public SysDic getSysDic(String dicType,String dicCode);
	
	/**
	 * 获取指定的数据字典
	 * @param dicType
	 * @param dicCode
	 * @return
	 */
	public String getSysDicValue(String dicType,String dicCode);
	
	/**
	 * 获取所有字典类型
	 * @return
	 */
	public List<String> queryDicTypes();
}
