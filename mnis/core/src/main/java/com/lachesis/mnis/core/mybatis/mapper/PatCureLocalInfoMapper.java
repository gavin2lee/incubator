package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.patientManage.entity.PatCureLocalInfo;
import com.lachesis.mnis.core.patientManage.entity.PatNdaManageInfo;

public interface PatCureLocalInfoMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(PatCureLocalInfo record);

    PatCureLocalInfo selectByPrimaryKey(Long seqId);

    List<PatCureLocalInfo> selectAll();

    int updateByPrimaryKey(PatCureLocalInfo record);
    
    List<PatCureLocalInfo> queryAllOutPatientByDateAndStatus(Map<String, String> conditionMap);
    
    PatNdaManageInfo queryNdaManageInfo(Map<String, String> conditionMap);
}
