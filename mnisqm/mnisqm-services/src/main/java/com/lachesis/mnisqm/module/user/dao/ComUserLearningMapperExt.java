package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.user.domain.ComUserLearning;

import java.util.List;

public interface ComUserLearningMapperExt extends ISearchableDAO {
    /**
     * 根据员工编号获取教育信息
     * @param userCode
     * @return
     */
    public List<ComUserLearning> selectByUsercode(String userCode);
}