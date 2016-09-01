package com.lachesis.mnisqm.module.patientManage.dao;

import com.lachesis.mnisqm.module.patientManage.domain.BedInfo;
import java.util.List;

public interface BedInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BedInfo record);

    BedInfo selectByPrimaryKey(Long id);

    List<BedInfo> selectAll();

    int updateByPrimaryKey(BedInfo record);
    
    BedInfo getByCode(String code);
    
    int updateByCode(BedInfo record);
}