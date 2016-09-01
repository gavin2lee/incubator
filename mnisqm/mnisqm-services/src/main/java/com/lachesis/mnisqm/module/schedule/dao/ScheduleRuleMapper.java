package com.lachesis.mnisqm.module.schedule.dao;

import com.lachesis.mnisqm.module.schedule.domain.ScheduleRule;
import java.util.List;

public interface ScheduleRuleMapper {
	/**
	 * 数据插入
	 * @param rule
	 * @return
	 */
    public int insert(ScheduleRule rule);
    
    /**
     * 数据更新
     */
    public void updateRuleByKey(ScheduleRule rule);

    /**
     * 查询科室下所有的排班规则
     * @param rule
     * @return
     */
    public List<ScheduleRule> selectRuleByDept(ScheduleRule rule);
    
    /**
     * 删除数据
     * @param rule
     */
    public void deleteRuleByKey(ScheduleRule rule);
}