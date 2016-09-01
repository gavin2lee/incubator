package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.user.domain.ComUserEducation;

import java.util.List;

public interface ComUserEducationMapperExt extends ISearchableDAO {
    /**
     * 根据用户编号获取用户学历信息
     * @param userCode
     * @return
     */
    public List<ComUserEducation> selectByUsercode(String userCode);
}