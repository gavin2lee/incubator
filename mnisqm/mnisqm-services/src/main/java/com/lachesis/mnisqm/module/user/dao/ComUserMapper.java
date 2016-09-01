package com.lachesis.mnisqm.module.user.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lachesis.mnisqm.module.user.domain.ComUser;

public interface ComUserMapper {
	/**
	 * 逻辑删除
	 * @param code
	 * @return
	 */
    public int deleteByPrimaryKey(ComUser user);

    /**
     * 数据新增
     * @param record
     * @return
     */
    public int insert(ComUser record);

    /**
     *数据修改
     * @param record
     * @return
     */
    public int updateByPrimaryKey(ComUser record);
    
    /**
     * 修改用户类型
     * @param userType
     * @param userCode
     * @return
     */
    public int updateUserType(@Param(value="userType")String userType, @Param(value="userCode")String userCode);
    
    /**
     *数据修改:修改职称
     * @param record
     * @return
     */
    public int updateTechnicalPost(ComUser user);
    
    /**
     *数据修改:修改职务
     * @param record
     * @return
     */
    public int updateDuty(ComUser user);
    
    /**
     *数据修改:修改带教级别
     * @param record
     * @return
     */
    public int updateTeachLevel(ComUser user);
    
    /**
     * 查询登录人是否是上级部门的人
     * @param parm
     * @return
     */
    public int selectDeptByUser(Map<String,Object> parm);
}