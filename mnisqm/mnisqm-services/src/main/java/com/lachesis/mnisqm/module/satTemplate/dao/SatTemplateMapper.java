package com.lachesis.mnisqm.module.satTemplate.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.satTemplate.domain.SatTemplate;

public interface SatTemplateMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SatTemplate record);

    SatTemplate selectByPrimaryKey(Long seqId);
    
    SatTemplate getByTemplateName(String templateName);

    List<SatTemplate> selectAll();

    int updateByPrimaryKey(SatTemplate record);
    
    int updateForDelete(SatTemplate record);
    
    /**
     * 根据表单类型和使用类型查找满意度模板
     * @param 
     * @return
     */
    List<SatTemplate> queryByFormTypeAndUserType(Map<String,Object> map);
    
    /**
     * 根据表单编号查找满意度模板
     * @param formCode 满意度表单编号
     * @return
     */
    SatTemplate getByFormCode(String formCode);
    
    /**
     * 获取所有表单编号和表单名
     * @return
     */
    List<SatTemplate> queryAllCodeAndFormName();
}