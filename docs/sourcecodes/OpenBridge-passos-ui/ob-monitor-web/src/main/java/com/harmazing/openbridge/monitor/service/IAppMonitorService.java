package com.harmazing.openbridge.monitor.service;

import java.util.List;
import java.util.Map;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/15 12:17.
 */
public interface IAppMonitorService {
    String findProjectCodeByProjectId(String projectId);
    List<Map<String,Object>> findDeployByProjectId(int status, String projectId);
}
