package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComUserEducation;

public interface ComUserEducationMapper {
	/**
	 * 逻辑删除
	 * @param edu
	 * @return
	 */
    public int deleteByPrimaryKey(ComUserEducation edu);

    int insert(ComUserEducation record);

    int updateByPrimaryKey(ComUserEducation record);
}