package com.harmazing.openbridge.mod.operations.elasticsearch.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.openbridge.mod.operations.elasticsearch.dao.SynMateInfoMapper;
/**
 * 同步数据采用的service，nginx数据和dubbo数据
 * @author admin
 *
 */
@Service
@Transactional(readOnly = true)
public class SynMateInfoService implements ISynMateInfoService{
	
	@Autowired
	private SynMateInfoMapper serviceQueryMapper; 
	@Transactional(readOnly = true)
	
	public List<Map<String, Object>> getApiUrlMatchDict() {
		return serviceQueryMapper.getApiUrlMatchDict();
	}
	
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getAppUrlMatchDict() {
		return serviceQueryMapper.getAppUrlMatchDict();
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> getDubboUrlMatchDict() {
		return serviceQueryMapper.getDubboUrlMatchDict();
	}
	

}
