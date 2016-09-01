package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.user.domain.ComDept;

import java.util.List;
import java.util.Map;

public interface ComDeptMapperExt extends ISearchableDAO {
    /**
     * 获取指定部门编号的所有直属部门
     * @param deptCode
     * @return
     */
    public List<ComDept> selectSubDeptsByCode(Map<String, Object> parm);
    
    /**
     * 通过部门编号获取部门信息
     * @param deptCode
     * @return
     */
    public ComDept selectDeptByCode(String deptCode);
}