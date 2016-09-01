package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.EducationRecordInspection;


public interface EducationRecordInspectionMapper {

	/**
	 * 增加逻辑
	 * @param educationRecordInspection
	 * @return
	 */
	int insert(EducationRecordInspection educationRecordInspection);
	
	/**
	 * 删除逻辑
	 * @param educationRecordInspection
	 * @return
	 */
	int deleteByPrimaryKey(EducationRecordInspection educationRecordInspection);
	
	/**
	 * 更新逻辑
	 * @param educationRecordInspection
	 * @return
	 */
	int update(EducationRecordInspection educationRecordInspection);
	
	/**
	 * 查询逻辑
	 * @param educationRecordInspection
	 * @return
	 */
	List<EducationRecordInspection> select(EducationRecordInspection educationRecordInspection);
}
