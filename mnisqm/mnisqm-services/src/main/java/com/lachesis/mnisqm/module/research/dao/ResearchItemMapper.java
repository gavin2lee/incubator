package com.lachesis.mnisqm.module.research.dao;

import com.lachesis.mnisqm.module.research.domain.ResearchItem;
import java.util.List;

public interface ResearchItemMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(ResearchItem record);

    ResearchItem selectByPrimaryKey(Long seqId);

    /**
     * 数据查询
     * @return
     */
    public List<ResearchItem> selectItemsByDept(ResearchItem item);

    int updateByPrimaryKey(ResearchItem record);
}