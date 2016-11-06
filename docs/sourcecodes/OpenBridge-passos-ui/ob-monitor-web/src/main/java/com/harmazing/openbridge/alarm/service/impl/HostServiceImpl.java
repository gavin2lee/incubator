package com.harmazing.openbridge.alarm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.alarm.dao.GroupHostMapper;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.dao.HostMapper;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.Host;
import com.harmazing.openbridge.alarm.model.vo.HostDTO;
import com.harmazing.openbridge.alarm.service.IHostService;

/**
 * Created by liyang on 2016/7/28.
 */
@Service
@Transactional
public class HostServiceImpl implements IHostService {
    @Autowired
    private HostMapper hostMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupHostMapper groupHostMapper;
    public Host findById(long id){
        return hostMapper.findById(id);
    }

    @Override
    public List<Host> findAll() {
        return hostMapper.findAll();
    }

    public List<Host> findByGroupId(long id){
        return hostMapper.findByGroupId(id);
    }
    public List<Host> pageFindByGroupId(Map<String, Object> params){
        return hostMapper.pageFindByGroupId(params);
    }
    public Host insert(HostDTO dto) throws Exception{
        Group group = groupMapper.findById(dto.getGroupId());
        if(group==null) throw new Exception("Group id ["+dto.getGroupId()+"] do not exist!");
        Host entity = new Host(dto.getHostname(),dto.getIp(),dto.getAgentVersion(),dto.getPluginVersion(),
                dto.getMaintainBegin(),dto.getMaintainEnd());
        hostMapper.insert(entity);
        groupHostMapper.insert(dto.getGroupId(),entity.getId());
        return entity;
    }
    
    @Override
	@Transactional(readOnly = true)
	public Page<Map<String, Object>> Page(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(hostMapper.PageRecordCount(params));
		xpage.addAll(hostMapper.Page(params));
		return xpage;
	}
}
