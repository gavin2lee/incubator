package com.harmazing.openbridge.sys.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.harmazing.framework.util.LogUtil;
import com.harmazing.framework.util.ResourceUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.XmlUtil;
import com.harmazing.openbridge.sys.role.dao.SysFuncMapper;
import com.harmazing.openbridge.sys.role.dao.SysRoleMapper;
import com.harmazing.openbridge.sys.role.model.SysFunc;
import com.harmazing.openbridge.sys.role.service.ISysFuncService;

@Service
public class SysFuncServiceImpl implements ISysFuncService {
	@Autowired
	private SysRoleMapper roleMapper;
	@Autowired
	private SysFuncMapper sysFuncMapper;

	@Override
	public List<Map<String, Object>> funcPage(Map<String, Object> params) {
		return filter(params);
	}

	private List<Map<String, Object>> filter(Map<String, Object> params) {
		List<Map<String, Object>> funcs = new ArrayList<Map<String, Object>>();
		try {
			List<SysFunc> all = loadFunctions();
			for (int i = 0; i < all.size(); i++) {
				Map<String, Object> func = new HashMap<String, Object>();
				func.put("funcId", all.get(i).getFuncId());
				func.put("funcName", all.get(i).getFuncName());
				func.put("funcDesc", all.get(i).getFuncDesc());
				if (params.containsKey("keyword")
						&& StringUtil.isNotNull((String) params.get("keyword"))) {
					if (all.get(i).getFuncName()
							.indexOf(params.get("keyword").toString()) >= 0) {
						funcs.add(func);
					}
				} else {
					funcs.add(func);
				}
			}
			return funcs;
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}

	@Override
	public Integer funcPageRecordCount(Map<String, Object> params) {
		try {
			return filter(params).size();
		} catch (Exception e) {
			LogUtil.error(e);
			return 0;
		}
	}

	@Override
	public List<SysFunc> findAll() {
		try {
			return loadFunctions();
		} catch (Exception e) {
			LogUtil.error(e);
			return null;
		}
	}

	@Override
	public List<SysFunc> findFuncBySystemKey(String systemKey) {
		List<SysFunc> sysFunc = new ArrayList<SysFunc>();
		try {
			List<SysFunc> all = loadFunctions();
			for (int i = 0; i < all.size(); i++) {
				if (all.get(i).getFuncSystem().equalsIgnoreCase(systemKey)) {
					sysFunc.add(all.get(i));
				}
			}
			return sysFunc;
		} catch (Exception e) {
			LogUtil.error(e);
			return null;
		}
	}

	@Override
	public List<SysFunc> findFuncByUserId(String userId) {
		List<SysFunc> userFunc = new ArrayList<SysFunc>();
		try {
			List<SysFunc> all = loadFunctions();
			List<String> funcIds = sysFuncMapper.findFuncByUserId(userId);
			for (int i = 0; i < funcIds.size(); i++) {
				for (int j = 0; j < all.size(); j++) {
					if (all.get(j).getFuncId().equalsIgnoreCase(funcIds.get(i))) {
						userFunc.add(all.get(j));
					}
				}
			}
			return userFunc;
		} catch (Exception e) {
			LogUtil.error(e);
			return null;
		}
	}

	public List<String> findAllFuncByRoleId(String roleId) { 
		return sysFuncMapper.findFuncByRoleId(roleId);

	}

	@Override
	public List<Map<String, Object>> findFuncByRoleId(String roleId) {
		List<Map<String, Object>> roleFunc = new ArrayList<Map<String, Object>>();
		try {
			List<SysFunc> all = loadFunctions();
			List<String> funcIds = sysFuncMapper.findFuncByRoleId(roleId);
			for (int i = 0; i < funcIds.size(); i++) {
				for (int j = 0; j < all.size(); j++) {
					if (all.get(j).getFuncId().equalsIgnoreCase(funcIds.get(i))) {
						Map<String, Object> func = new HashMap<String, Object>();
						func.put("funcId", all.get(i).getFuncId());
						func.put("funcName", all.get(i).getFuncName());
						func.put("funcDesc", all.get(i).getFuncDesc());
						roleFunc.add(func);
					}
				}
			}
			return roleFunc;
		} catch (Exception e) {
			LogUtil.error(e);
			return null;
		}
	}

	private static List<SysFunc> funcs = null;

	private synchronized List<SysFunc> loadFunctions() throws Exception {
		if (funcs == null) {
			funcs = new ArrayList<SysFunc>();
			Resource[] r = ResourceUtil.getResources("classpath:config/functions.xml"); 
			Document doc = XmlUtil.buildDocument(r[0].getInputStream());
			List<Element> functions = XmlUtil.getChildElementsByTagName(
					doc.getDocumentElement(), "function");
			for (Element functionElement : functions) {
				String functionId = functionElement.getAttribute("id");
				String functionName = functionElement.getAttribute("name");
				String functionSystem = functionElement.getAttribute("system");
				String functionDesc = functionElement
						.getAttribute("description");

				String funcModule = functionElement.getAttribute("module");

				SysFunc func = new SysFunc();
				func.setFuncId(functionId);
				func.setFuncName(functionName);
				func.setFuncDesc(functionDesc);
				func.setFuncSystem(functionSystem);
				func.setFuncModule(funcModule);
				funcs.add(func);
			}
		}
		return funcs;
	}

}
