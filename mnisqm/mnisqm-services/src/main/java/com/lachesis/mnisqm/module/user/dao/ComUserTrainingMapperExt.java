package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.user.domain.ComUserTraining;

import java.util.List;

public interface ComUserTrainingMapperExt extends ISearchableDAO {
    /**
     * 根据员工编号获取继续教育信息
     * @param userCode
     * @return
     */
    public List<ComUserTraining> selectByUsercode(String userCode);
}