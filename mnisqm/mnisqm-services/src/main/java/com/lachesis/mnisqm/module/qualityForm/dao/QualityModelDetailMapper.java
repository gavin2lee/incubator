package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityForm;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityFormDetail;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityModelDetail;

public interface QualityModelDetailMapper {
	
	void insert(QualityModelDetail qualityModelDetail);
	
	List<QualityModelDetail> select(QualityModelDetail qualityModelDetail);

	void delete(QualityModelDetail qualityModelDetail);
	
	/**
	 * 通过模板查询项目
	 * @return
	 */
	public List<QualityFormDetail> selectModelById(QualityModelDetail detail);
	
	/**
	 * 通过编号查询类别
	 * @return
	 */
	public QualityForm selectFormByPCode(QualityForm form);
	
	/**
	 * 通过项目查询子类
	 * @return
	 */
	public QualityForm selectFormByCode(QualityFormDetail detail);
}
