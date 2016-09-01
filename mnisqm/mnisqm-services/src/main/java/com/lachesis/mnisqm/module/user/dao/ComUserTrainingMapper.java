package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComUserTraining;

public interface ComUserTrainingMapper {
	/**
	 * 逻辑删除
	 * @param TRA
	 * @return
	 */
    public int deleteByPrimaryKey(ComUserTraining TRA);

    public int insert(ComUserTraining record);

    public int updateByPrimaryKey(ComUserTraining record);
}