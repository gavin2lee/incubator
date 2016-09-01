package com.lachesis.mnisqm.module.user.dao;

import java.util.List;

import com.lachesis.mnisqm.module.user.domain.ComDept;
import com.lachesis.mnisqm.module.user.domain.Statistics;

public interface ComDeptMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(ComDept record);

    ComDept selectByPrimaryKey(Long seqId);

    List<ComDept> selectAll();

    int updateByPrimaryKey(ComDept record);
    
    int updateByDeptCode(ComDept record);
    
    List<String> selectAllDeptCode();
    
    /**
     * 查询离职科室
     * @return
     */
    public ComDept selectLeaveDept(ComDept dept);
    
    /**
     * 查询职称统计
     * @param dept
     * @return
     */
    public List<Statistics> selectTechnicalPostCount(ComDept dept);
    
    /**
     * 查询职务统计
     * @param dept
     * @return
     */
    public List<Statistics> selectPositionCount(ComDept dept);
    
    /**
     * 查询临床层级统计
     * @param dept
     * @return
     */
    public List<Statistics> selectClinicalCount(ComDept dept);
    
    /**
     * 查询带教级别统计
     * @param dept
     * @return
     */
    public List<Statistics> selectTeachingCount(ComDept dept);
    
    /**
     * 查询学历统计
     * @param dept
     * @return
     */
    public List<Statistics> selectEducation(ComDept dept);
    
    /**
     * 查询性别统计
     * @param dept
     * @return
     */
    public List<Statistics> selectGender(ComDept dept);
    
    /**
     * 查询用户类型统计
     * @param dept
     * @return
     */
    public List<Statistics> selectUserType(ComDept dept);
    
    /**
     * 修改为科护长
     * @param dept
     */
    public void saveDeptNurseHeader(ComDept dept);
    
    
    ComDept getByDeptCode(String deptCode);
}