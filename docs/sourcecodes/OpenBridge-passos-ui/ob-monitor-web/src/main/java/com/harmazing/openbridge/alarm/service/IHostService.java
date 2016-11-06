package com.harmazing.openbridge.alarm.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.alarm.model.Host;
import com.harmazing.openbridge.alarm.model.vo.HostDTO;

/**
 * Created by liyang on 2016/7/28.
 */
public interface IHostService {
    Host findById(long id);
    List<Host> findAll();
    List<Host> findByGroupId(long id);
    List<Host> pageFindByGroupId(Map<String, Object> params);
    Host insert(HostDTO dto) throws Exception;
    Page<Map<String, Object>> Page(Map<String, Object> params);
}
