package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.PreJobTraining;

public interface PreJobTrainingMapper {

	/**
	 * 增加逻辑
	 * @param preJobTraining
	 * @return
	 */
	int insert(PreJobTraining preJobTraining);
	
	/**
	 * 删除逻辑
	 * @param preJobTraining
	 * @return
	 */
	int deleteByPrimaryKey(PreJobTraining preJobTraining);
	
	/**
	 * 更新逻辑
	 * @param preJobTraining
	 * @return
	 */
	int update(PreJobTraining preJobTraining);
	
	/**
	 * 查询逻辑
	 * @param preJobTraining
	 * @return
	 */
	List<PreJobTraining> select(PreJobTraining preJobTraining);
}
