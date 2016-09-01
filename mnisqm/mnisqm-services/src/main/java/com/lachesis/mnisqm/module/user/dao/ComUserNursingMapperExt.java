package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.user.domain.ComUserNursing;

import java.util.List;

public interface ComUserNursingMapperExt extends ISearchableDAO {
    /**
     * 根据用户编号获取护理信息
     * @param userCode
     * @return
     */
    public List<ComUserNursing> selectByUsercode(String userCode);
}