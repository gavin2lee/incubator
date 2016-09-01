package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComUserLearning;

public interface ComUserLearningMapper {
	/**
	 * 逻辑删除
	 * @param lea
	 * @return
	 */
    public int deleteByPrimaryKey(ComUserLearning lea);

    int insert(ComUserLearning record);

    int updateByPrimaryKey(ComUserLearning record);
}