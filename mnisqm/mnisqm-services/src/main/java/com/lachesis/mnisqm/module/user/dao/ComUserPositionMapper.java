package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComUserPosition;

public interface ComUserPositionMapper {
	/**
	 * 逻辑删除
	 * @param po
	 * @return
	 */
    public int deleteByPrimaryKey(ComUserPosition po);

    int insert(ComUserPosition record);

    int updateByPrimaryKey(ComUserPosition record);
}