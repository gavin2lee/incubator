package com.harmazing.openbridge.alarm.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.SysUserDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateEditDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateIndexDTO;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/3 17:34.
 */
public interface ITemplateService {
    List<Template> findAll();
    Template findById(long id);
    /**
     * 由于tplname唯一，所以这个方法在新增策略时用
     * @param tplName
     * @return
     */
    Template findByTplName(String tplName);
    List<TemplateIndexDTO> findAllDTO();
    void deleteById(long id);
    Template insert(Template template);
    TemplateEditDTO findDtoById(long id);
    int updateTplNameById(String tplName,long id);
    int updateActionIdById(long actionId,long id);
    List<TemplateIndexDTO> findByGroupId(long id);
    Page<Map<String, Object>> Page(Map<String, Object> params);
    List<SysUserDTO>  findUserByTid(long id);//显示用户的名称
    List<TemplateEditDTO> findDtoByGroupId(long id);//通过groupId查找策略组
    Page<TemplateEditDTO> findDtoPageByGroupId(long id,int pageNo,int pageSize);
}
