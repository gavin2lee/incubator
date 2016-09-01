package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityIndex;

public interface QualityIndexMapper {
	
	int insert(QualityIndex qualityIndex);
	
	int update(QualityIndex qualityIndex);
	
	List<QualityIndex> select(QualityIndex qualityIndex);
	
	void delete(Long seqId);

}
