package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityIndexItem;

public interface QualityIndexItemMapper {

	/**
	 * 插入分子分母指标
	 * @param qualityIndexItem
	 * @return
	 */
	int insert(QualityIndexItem qualityIndexItem);
	
	/**
	 * 修改
	 * @param qualityIndexItem
	 * @return
	 */
	int update(QualityIndexItem qualityIndexItem);
	
	/**
	 * 根据名词和类型查询
	 * @param indexItemName
	 * @param indexItemType
	 */
	List<QualityIndexItem> select(Map<String, Object> map);
	
	/**
	 * 删除
	 * @param seqId
	 */
	void deleteByUpdate(Long seqId);
}
