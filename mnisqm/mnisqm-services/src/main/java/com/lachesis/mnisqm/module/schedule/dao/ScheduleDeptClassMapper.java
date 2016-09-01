package com.lachesis.mnisqm.module.schedule.dao;

import com.lachesis.mnisqm.module.schedule.domain.ScheduleDeptClass;
import java.util.List;

public interface ScheduleDeptClassMapper {
	/**
	 * 班次数据删除
	 * @param deptClass
	 * @return
	 */
    public int deleteByPrimaryKey(ScheduleDeptClass deptClass);

    public int insert(ScheduleDeptClass record);

    /**
     * 查询科室下面的所有班次
     * @param classes
     * @return
     */
    public List<ScheduleDeptClass> selectAllByCode(ScheduleDeptClass classes);

    int updateByPrimaryKey(ScheduleDeptClass record);
}