package com.harmazing.openbridge.alarm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.dao.GroupTemplateMapper;
import com.harmazing.openbridge.alarm.dao.TemplateMapper;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.SysUserDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateEditDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateIndexDTO;
import com.harmazing.openbridge.alarm.service.ITemplateService;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/3 17:35.
 */
@Service
@Transactional
public class TemplateServiceImpl implements ITemplateService {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private GroupTemplateMapper groupTemplateMapper;
    @Override
    public List<Template> findAll() {
        return templateMapper.findAll();
    }

    @Override
    public List<TemplateIndexDTO> findAllDTO() {
        return templateMapper.findAllDTO();
    }

    @Override
    public Template findById(long id) {
        return templateMapper.findById(id);
    }

    @Override
    public void deleteById(long id){
        templateMapper.deleteById(id);
        groupTemplateMapper.deleteByTplId(id);
    }

    @Override
    public Template insert(Template template){
    	if(StringUtil.isNull(template.getTplName())){
			throw new RuntimeException("策略名不能为空！");
		}
    	int sysTeam = templateMapper.getTplCountByName(template.getTplName(),template.getId());
		if(sysTeam > 0){
			throw new RuntimeException("已存在策略名,请修改！");
		}
        templateMapper.insert(template);
        return template;
    }

    public int updateTplNameById(String tplName,long id){
    	if(StringUtil.isNull(tplName)){
			throw new RuntimeException("策略名不能为空！");
		}
    	int sysTeam = templateMapper.getTplCountByName(tplName,id);
		if(sysTeam > 0){
			throw new RuntimeException("已存在策略名,请修改！");
		}
        return templateMapper.updateTplNameById(tplName,id);
    }

    public int updateActionIdById(long actionId,long id){
        return templateMapper.updateActionIdById(actionId,id);
    }

    @Override
    public TemplateEditDTO findDtoById(long id){
        return templateMapper.findDtoById(id);
    }
    public List<TemplateIndexDTO> findByGroupId(long id){
        return templateMapper.findByGroupId(id);
    }
    
    @Override
   	@Transactional(readOnly = true)
   	public Page<Map<String, Object>> Page(Map<String, Object> params) {
   		Page<Map<String, Object>> xpage = Page.create(params);
   		xpage.setRecordCount(templateMapper.PageRecordCount(params));
   		xpage.addAll(templateMapper.Page(params));
   		return xpage;
   	}
    
    @Override
	public List<SysUserDTO> findUserByTid(long id) {	
		return templateMapper.findUserByTid(id);
	}

	@Override
	public List<TemplateEditDTO> findDtoByGroupId(long id) {
		return templateMapper.findDtoByGroupId(id);
	}

	@Override
	public Page<TemplateEditDTO> findDtoPageByGroupId(long id, int pageNo, int pageSize) {		
		   Page<TemplateEditDTO>  page=new Page<TemplateEditDTO>(pageNo, pageSize);
		   page.addAll(templateMapper.findDtoPageByGroupId(id, page.getStart(), page.getPageSize()));
	       page.setRecordCount(templateMapper.getDToPageCountByGroupId(id));
		   return page;
	}
	public Template findByTplName(String tplName) {
		return templateMapper.findByTplName(tplName);
	}
}
