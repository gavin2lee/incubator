package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.PositionStatement;

public interface PositionStatementMapper {

	/**
	 * 增加逻辑
	 * @param positionStatement
	 * @return
	 */
	int insert(PositionStatement positionStatement);
	
	/**
	 * 删除逻辑
	 * @param positionStatement
	 * @return
	 */
	int deleteByPrimaryKey(PositionStatement positionStatement);
	
	/**
	 * 更新逻辑
	 * @param positionStatement
	 * @return
	 */
	int update(PositionStatement positionStatement);
	
	/**
	 * 查询逻辑
	 * @param positionStatement
	 * @return
	 */
	List<PositionStatement> select(PositionStatement positionStatement);
}
