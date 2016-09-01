package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComUserNursing;

public interface ComUserNursingMapper {
	/**
	 * 逻辑删除
	 * @param nur
	 */
    public void deleteByPrimaryKey(ComUserNursing nur);

    int insert(ComUserNursing record);

    int updateByPrimaryKey(ComUserNursing record);
}