package com.lachesis.mnisqm.module.event.dao;

import com.lachesis.mnisqm.module.event.domain.EventRequirement;
import java.util.List;

public interface EventRequirementMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(EventRequirement record);

    EventRequirement selectByPrimaryKey(Long seqId);

    /**
     * 查询不良事件要求列表
     * @param req
     * @return
     */
    public List<EventRequirement> selectRequireList(EventRequirement req);

    int updateByPrimaryKey(EventRequirement record);
}