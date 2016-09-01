package com.lachesis.mnis.core;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.sysDic.entity.SysDic;

public interface SysDicService {

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
	
	/**
	 * 获取指定模块的顺序配置
	 * @param modelName 模块名
	 * @return
	 */
	public Map<String, String> getOrderCache(String modelName);
	
	/**
	 * 获取指定模块名以及当前状态的后置状态
	 * @param modelName 模块名
	 * @param status 当前状态
	 * @return
	 * 注意：结果为以下格式   后置状态&当前状态描述  如： 2&手术申请
	 */
	public String getBackStatus(String modelName, String status);
}
