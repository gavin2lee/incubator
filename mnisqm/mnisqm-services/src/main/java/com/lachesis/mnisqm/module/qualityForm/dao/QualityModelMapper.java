package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityModel;

public interface QualityModelMapper {

	Long insert(QualityModel qualityModel);
	
	int update(QualityModel qualityModel);
	
	List<QualityModel> select(QualityModel qualityModel);
	
	void delete(Long seqId);
	
	/**
	 * 通过ID查询
	 * @param qualityModel
	 * @return
	 */
	public List<QualityModel> selectByCode(QualityModel qualityModel);
}
