package com.harmazing.openbridge.paas.framework.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.TemplateUtil;
import com.harmazing.openbridge.paas.framework.dao.PaasFrameworkMapper;
import com.harmazing.openbridge.paas.framework.model.PaasFramework;
import com.harmazing.openbridge.paas.framework.service.IPaasFrameworkService;

@Service
public class PaasFrameworkServiceImpl implements IPaasFrameworkService {

	private static Log logger = LogFactory
			.getLog(PaasFrameworkServiceImpl.class);

	@Autowired
	private PaasFrameworkMapper paasFrameworkMapper;
	 
//	private static ThreadLocal<Map<String,Object>> config = new ThreadLocal<Map<String,Object>>();
	
	@Override
	@Transactional(readOnly = true)
	public List<PaasFramework> getPaasFrameworkList() {
		return paasFrameworkMapper.getPaasFrameworkList();
	}

	@Override
	@Transactional(readOnly = true)
	public PaasFramework getPaasFrameworkById(String id) {
		return paasFrameworkMapper.getPaasFrameworkById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PaasFramework> getPaasFrameworkByKey(String key) {
		return paasFrameworkMapper.getPaasFrameworkByKey(key);
	}

	@Override
	public String getDockerFile(PaasFramework framework,
			Map<String, Object> root, String userId) {
		// id用来缓存 freemarker模板
		if (StringUtils.isEmpty(framework.getDockerFile())) {
			return null;
		}
		if (root == null) {
			root = new HashMap<String, Object>();
		}
		root.put("imageId", framework.getImageId());
		// 去paasos中获取参数

		try {

			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "sys/config/find.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();

			String msg = PaasAPIUtil.post(userId, osPath, DataType.FORM, param);
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			JSONObject data = jo.getJSONObject("data");
			Map<String, String> config = JSONObject.parseObject(
					data.toJSONString(), HashMap.class);
			if (config != null) {
				for (String key : config.keySet()) {
					root.put(key.replaceAll("\\.", "_"), config.get(key));
				}
			}
			logger.debug(framework.getDockerFile());
			logger.debug(JSONObject.toJSONString(root));
			String docker = TemplateUtil.getOutputByTemplate(
					framework.getDockerFile(), root);
			logger.debug(docker);
			return docker;
		} catch (IOException e) {
			logger.error("解析模板出现问题", e);
		}
		return null;
	}

}
