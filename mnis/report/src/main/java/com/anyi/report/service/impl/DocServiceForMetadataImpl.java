package com.anyi.report.service.impl;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.anyi.report.db.MetadataMapper;
import com.anyi.report.entity.Metadata;
import com.anyi.report.entity.MetadataOption;
import com.anyi.report.service.DocServiceForMetadata;

@Service("docServiceForMetadata")
public class DocServiceForMetadataImpl implements DocServiceForMetadata{

	@Autowired
	private MetadataMapper metadataMapper;
	@Autowired
	private ApplicationContext applicationContext;
	
	
	public String addMetadada(Metadata metadata) {
		try{
			metadataMapper.addMetadada(metadata);
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
		return "0";
	}

	public String deletaMetadata(Metadata metadata) {
		try {
			metadataMapper.deletaMetadata(metadata);
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
		return "0";
	}

	public String updateMetadata(Metadata metadata) {
		try{
			metadataMapper.updateMetadata(metadata);
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
		return "0";		
	}

	public List<Metadata> queryMetadata(Metadata metadata) {
		return metadataMapper.queryMetadata(metadata);
	}

	public String addMetadataOption(MetadataOption option) {
		try{
			metadataMapper.addMetadataOption(option);
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
		return "0";		
	}

	public String deleteMetadataOption(MetadataOption option) {
		try{
			metadataMapper.deleteMetadataOption(option);
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
		return "0";	
	}

	public String updateMetadataOption(MetadataOption option) {
		try{
			metadataMapper.updateMetadataOption(option);
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
		return "0";		
	}

	public List<MetadataOption> queryMetadataOption(MetadataOption option) {
		return metadataMapper.queryMetadataOption(option);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 判断是否存在该代码
	 */
	public String isExitSameCode(String metadata_code) {
		return metadataMapper.isExitSameCode(metadata_code);
	}

}
