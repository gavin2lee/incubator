package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.user.domain.ComUserPosition;

import java.util.List;

public interface ComUserPositionMapperExt extends ISearchableDAO {

    /**
     * 根据用户编号获职称位信息
     * @param userCode
     * @return
     */
    public List<ComUserPosition> selectByUsercode(String userCode);
}