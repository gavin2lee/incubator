package com.harmazing.openbridge.sys.role.service;

import java.util.List;
import java.util.Map;

import com.harmazing.openbridge.sys.role.model.SysFunc;

public interface ISysFuncService {
	List<Map<String, Object>> funcPage(Map<String, Object> params);

	Integer funcPageRecordCount(Map<String, Object> params);

	List<String> findAllFuncByRoleId(String roleId);

	List<Map<String, Object>> findFuncByRoleId(String roleId);

	List<SysFunc> findAll();

	List<SysFunc> findFuncBySystemKey(String systemKey);

	List<SysFunc> findFuncByUserId(String userId);
}
