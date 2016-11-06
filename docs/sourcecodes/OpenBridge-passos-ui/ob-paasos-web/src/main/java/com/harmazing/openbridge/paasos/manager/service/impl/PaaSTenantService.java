package com.harmazing.openbridge.paasos.manager.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.kubectl.K8RestApiUtil;
import com.harmazing.openbridge.paasos.manager.dao.PaaSTenantMapper;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenant;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenantPreset;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenantQuota;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;
import com.harmazing.openbridge.sys.user.model.SysUser;
@Service
public class PaaSTenantService implements IPaaSTenantService {
	public static String SCRIPT_TEMPLATE = "sudo curl ${paasos.web.url}/install/install_node.sh && sudo sh install_node.sh --etcd_servers=${paasos.k8s.etcdserver}, --insecure_registry=${paasos.docker.registry} --api_server=${paasos.k8s.apiserver}";
	@Resource
	private PaaSTenantMapper paasTenantMapper;
	@Override
	@Transactional(readOnly=true)
	public List<PaaSTenant> list() {
		return paasTenantMapper.list();
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void saveOrUpdate(PaaSTenant tenant) {
		String tenantId = tenant.getTenantId();
		int sysTenant = paasTenantMapper.getTenSysTenantCountByName(tenant.getTenantName(),tenant.getTenantId());
		if(sysTenant > 0){
			throw new RuntimeException("已存在组织名称,请修改！");
		}
		/**
		 * update by weiyujia @2016/07/08 
		 * 添加组织配额功能
		 * 
		 * update by weiyujia @2016/07/08
		 * 添加组织分配预置应用功能
		 */
		if(StringUtil.isNotNull(tenantId)){
			paasTenantMapper.deleteMember(tenantId);
			paasTenantMapper.deleteTenantPreset(tenantId);
			paasTenantMapper.update(tenant);
			paasTenantMapper.deleteTenantQuota(tenantId);
		}else{
			tenantId = StringUtil.getUUID();
			tenant.setTenantId(tenantId);
			tenant.setCreateTime(new Date());
			paasTenantMapper.addTenant(tenant);
//			for (PaaSTenantQuota quota : tenant.getTenantQuotaList()) {
//				quota.setId(StringUtil.getUUID());
//			}
//			paasTenantMapper.batchAddTenantQuota(tenant);
		}
		for (PaaSTenantQuota quota : tenant.getTenantQuotaList()) {
			quota.setId(StringUtil.getUUID());
		}
		for (PaaSTenantPreset paaSTenantPreset : tenant.getPresetList()) {
			paaSTenantPreset.setId(StringUtil.getUUID());
			paaSTenantPreset.setTenantId(tenantId);
		}
		paasTenantMapper.batchAddTenantQuota(tenant);
		/**
		 * update by weiyujia @2016/07/08
		 * 添加组织分配预置应用功能
		 */
		paasTenantMapper.batchAddTenantPreset(tenant);
		for(SysUser user : tenant.getMember()){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", StringUtil.getUUID());
			params.put("tenantId", tenantId);
			params.put("userId", user.getUserId());
			paasTenantMapper.addMember(params);
		}
		K8RestApiUtil util = new K8RestApiUtil(this);
		util.saveNamespace(tenant.getTenantId(),tenant.getTenantName());
	}

	@Override
	@Transactional(readOnly=true)
	public List<PaaSTenant> getTenantByUserId(String userId) {
		return paasTenantMapper.getTenantByUserId(userId);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<PaaSTenant> getPage(int pageNo, int pageSize) {
		Page<PaaSTenant> page = new Page<PaaSTenant>(pageNo,pageSize);
		page.addAll(paasTenantMapper.getPageData(page.getStart(),page.getPageSize()));
		page.setRecordCount(paasTenantMapper.getPageCount());
		return page;
	}

	@Override
	@Transactional(readOnly=true)
	public PaaSTenant get(String id) {
		PaaSTenant paaSTenant = paasTenantMapper.getById(id);
		if(paaSTenant != null){
			paaSTenant.setTenantQuotaList(paasTenantMapper.getTenantQuotaListById(id));
			paaSTenant.setPresetList(paasTenantMapper.getTenantPresetListById(id));
		}
		return paaSTenant;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void delete(String id) {
		paasTenantMapper.deleteMember(id);
		paasTenantMapper.delete(id);
	}
	@Override
	@Transactional(readOnly=true)
	public List<PaaSTenant> listBrief() {
		return paasTenantMapper.listBrief();
	}
	@Override
	public String getInstallScript() {
		Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");
		Matcher matcher = pattern.matcher(SCRIPT_TEMPLATE);
		String script = SCRIPT_TEMPLATE;
		while(matcher.find()){
			String key = matcher.group(1);
			String configString = ConfigUtil.getConfigString(key);
			configString = configString.replace("http://", "");
			script = script.replace(matcher.group(0), configString);
		}
		return script;
	}
	@Override
	@Transactional(readOnly=true)
	public int getTenantImageCount(String tenantId) {
		PaaSTenant tenant = get(tenantId);
		if(tenant!=null){
			List<SysUser> users = tenant.getMember();
			return paasTenantMapper.getTenantImageCount(users);
		}
		return 0;
	}
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> findTenantItemList( List<Map<String, Object>> paramList) {
		return paasTenantMapper.findTenantItemList(paramList);
	}
	
	/**
	* @Title: getTenantQuotaInfo 
	* @author weiyujia
	* @Description: 获取租户资源配额信息
	* @param tenantId 租户ID
	* @param envType 环境类型
	* @param categoryType 一级分类，如部署，数据库，缓存，中间件
	* @param subCategoryType  二级分类，如docker  mysql  rabbitMQ
	* @param itemLookupType  三级分类，如内存，CPU，容量
	* @return List<Map<String,String>>    返回类型 
	* @throws 
	* @date 2016年7月12日 下午3:38:00
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, String>> getTenantQuotaInfo(String tenantId,String envType,String categoryType,String subCategoryType,String itemLookupType){
		Map<String, String> paramMap = new HashMap<String,String>();
		if(StringUtil.isEmpty(tenantId)){
			throw new RuntimeException("参数异常，请提供租户ID");
		}
		paramMap.put("tenantId", tenantId);
		paramMap.put("envType", envType);
		paramMap.put("categoryType", categoryType);
		paramMap.put("subCategoryType", subCategoryType);
		paramMap.put("itemLookupType", itemLookupType);
		return getTenantQuotaInfo(paramMap);
	}
	
	/**
	* @Title: getDeployResouceAllByEnvId 
	* @author weiyujia
	* @Description: 获取租户资源配额信息
	* paramMap 主要包含四参数
	* tenantId  租户ID
	* categoryType 一级分类，如部署，数据库，缓存，中间件
	* subCategoryType 二级分类，如docker  mysql  rabbitMQ
	* itemLookupType  三级分类，如内存，CPU，容量
	* @date 2016年7月12日 下午3:09:07
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, String>> getTenantQuotaInfo(Map<String, String> paramMap) {
		String tenantId = paramMap.get("tenantId");
		if(StringUtil.isEmpty(tenantId)){
			throw new RuntimeException("参数异常，请提供租户ID");
		}
		List<Map<String,String>> tenantQuotaList = paasTenantMapper.getTenantQuotaInfo(paramMap);
		List<Map<String,String>> defaultQuotaList = paasTenantMapper.getDefaultQuotaInfo(paramMap);
		for(Map<String,String> map : defaultQuotaList){
			String codeKey = map.get("itemLookupType");
			if(!checkCodeExistFlag(codeKey,tenantQuotaList)){
				map.put("tenantId", tenantId);
				map.put("id", "");
				tenantQuotaList.add(map);
			}
		}
		return tenantQuotaList;
	}
	
	/**
	* @Title: setDefaultValueToTenantQuota 
	* @author weiyujia
	* @Description: 将默认配额属性值赋值给组织配额
	* @param map 默认配额
	* @param tenantQuotaList  组织配额列表
	* @date 2016年7月14日 下午4:08:52
	 */
//	private void setDefaultValueToTenantQuota(Map<String, String> map, List<Map<String, String>> tenantQuotaList) {
//		
//	}
	
	/**
	* @Title: checkCodeExistFlag 
	* @author weiyujia
	* @Description: 判断默认配额属性是否存在于组织配额
	* @param codeKey 默认配额属性
	* @param tenantQuotaList  组织配额列表
	* @return boolean    返回类型 
	* @date 2016年7月14日 下午4:09:12
	 */
	private boolean checkCodeExistFlag(String codeKey, List<Map<String, String>> tenantQuotaList) {
		boolean flag = false;
		for(Map<String,String> map : tenantQuotaList){
			if(map.get("itemLookupType").equals(codeKey)){
				flag = true;
				break;
			}
		}
		return flag;
	}

}
