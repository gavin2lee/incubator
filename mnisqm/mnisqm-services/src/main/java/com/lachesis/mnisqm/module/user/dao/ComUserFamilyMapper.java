package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComUserFamily;

public interface ComUserFamilyMapper {
	/**
	 * 逻辑删除
	 * @param family
	 * @return
	 */
    public int deleteByPrimaryKey(ComUserFamily family);

    int insert(ComUserFamily record);

    int updateByPrimaryKey(ComUserFamily record);
}