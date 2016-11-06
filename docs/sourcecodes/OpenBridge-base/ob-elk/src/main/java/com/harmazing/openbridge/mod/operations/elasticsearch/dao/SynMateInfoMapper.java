package com.harmazing.openbridge.mod.operations.elasticsearch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;


public interface SynMateInfoMapper extends IBaseMapper {
	
	List<Map<String, Object>> getApiUrlMatchDict();
	
	List<Map<String, Object>> getAppUrlMatchDict();
	
	/**
	 * 获取内部dubbo日志字典
	 * @return
	 */
	List<Map<String, Object>> getDubboUrlMatchDict();
	
}
