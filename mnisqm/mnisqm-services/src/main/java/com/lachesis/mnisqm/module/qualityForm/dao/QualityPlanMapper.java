package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityPlan;

public interface QualityPlanMapper {

	int insert(QualityPlan qualityPlan);
	
	int update(QualityPlan qualityPlan);
	
	List<QualityPlan> select(QualityPlan qualityPlan);
	
	int delete(Long seqId);
}
