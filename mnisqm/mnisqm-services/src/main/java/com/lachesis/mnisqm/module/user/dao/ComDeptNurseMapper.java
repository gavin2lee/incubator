package com.lachesis.mnisqm.module.user.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.user.domain.ComDeptNurse;

public interface ComDeptNurseMapper {
	/**
	 * 数据新增
	 * @param record
	 * @return
	 */
    public int insert(ComDeptNurse record);

    /**
     * 查询并且护理人员信息
     * @return
     */
    public List<ComDeptNurse> selectUserNurses(ComDeptNurse nurse);

    /**
     * 更新病区护理人员信息
     * @param record
     * @return
     */
    public int updateByPrimaryKey(ComDeptNurse record);
    
    /**
     * 查询病区护理人员
     * @param parm
     * @return
     */
    public List<ComDeptNurse> selectUserNurseByDepot(Map<String,String> parm);
    
    /**
     * 病区排班后新增人员排班
     * @param parm
     * @return
     */
    public List<ComDeptNurse> selectUserAddNurseByDepot(Map<String,Object> parm);
    
    /**
     * 保存积假
     * @param nurse
     */
    public void saveUserLeave(ComDeptNurse nurse);
    
}