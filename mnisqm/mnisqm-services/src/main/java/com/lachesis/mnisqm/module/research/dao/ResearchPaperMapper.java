package com.lachesis.mnisqm.module.research.dao;

import com.lachesis.mnisqm.module.research.domain.ResearchPaper;
import java.util.List;

public interface ResearchPaperMapper {
	//逻辑删除
    public int deleteByPrimaryKey(ResearchPaper paper);

    int insert(ResearchPaper record);

    ResearchPaper selectByPrimaryKey(Long seqId);

    /**
     * 数据查询
     * @return
     */
    public List<ResearchPaper> selectPaperByDept(ResearchPaper paper);

    int updateByPrimaryKey(ResearchPaper record);
}