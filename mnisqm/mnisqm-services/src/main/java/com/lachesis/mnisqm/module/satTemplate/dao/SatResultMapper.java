package com.lachesis.mnisqm.module.satTemplate.dao;

import com.lachesis.mnisqm.module.satTemplate.domain.SatResult;

import java.util.List;
import java.util.Map;

public interface SatResultMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SatResult record);

    SatResult selectByPrimaryKey(Long seqId);

    List<SatResult> selectAll();

    int updateByPrimaryKey(SatResult record);
    
    int updateForDelete(SatResult record);
    
    /**
     * 根据时间、表单类型、患者姓名等条件查询患者调查结果
     * @param map
     * @return
     */
    List<SatResult> queryByDateOrFormTypeOrPatientOrDeptCode(Map<String,Object> map);
}