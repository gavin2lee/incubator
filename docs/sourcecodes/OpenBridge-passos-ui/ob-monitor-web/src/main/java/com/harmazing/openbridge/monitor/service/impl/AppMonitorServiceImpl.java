package com.harmazing.openbridge.monitor.service.impl;

import com.harmazing.openbridge.monitor.dao.AppMonitorMapper;
import com.harmazing.openbridge.monitor.service.IAppMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/12 17:38.
 */
@Service
@Transactional
public class AppMonitorServiceImpl implements IAppMonitorService {
    @Autowired
    private AppMonitorMapper appMonitorMapper;
    @Transactional(readOnly=true)
    @Override
    public String findProjectCodeByProjectId(String projectId) {
        return appMonitorMapper.findProjectCodeByProjectId(projectId);
    }
    @Transactional(readOnly=true)
    @Override
    public List<Map<String, Object>> findDeployByProjectId(int status, String projectId) {
        return appMonitorMapper.findDeployByProjectId(status,projectId);
    }
}
