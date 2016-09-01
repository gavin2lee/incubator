package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComBedGroup;
import java.util.List;

public interface ComBedGroupMapper {
	
	/**
	 * 逻辑删除
	 * @param seqId
	 * @return
	 */
    public void deleteByPrimaryKey(ComBedGroup group);

    int insert(ComBedGroup record);

    ComBedGroup selectByPrimaryKey(Long seqId);

    /**
     * 通过科室查询
     * @param group
     * @return
     */
    public List<ComBedGroup> selectByCode(ComBedGroup group);

    /**
     * 
     * @param record
     * @return
     */
    public int updateByPrimaryKey(ComBedGroup record);
}