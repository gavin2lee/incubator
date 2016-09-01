package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.user.domain.ComUserFamily;

import java.util.List;

public interface ComUserFamilyMapperExt extends ISearchableDAO {
    /**
     * 根据员工编号获取家庭信息
     * @param userCode
     * @return
     */
    public List<ComUserFamily> selectByUsercode(String userCode);
}