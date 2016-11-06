package com.harmazing.openbridge.paasos.manager.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenant;

public interface IPaaSTenantService extends IBaseService{

	public static String DOCKER_MEMORY="docker_memory";
	
	public static String DOCKER_CPU="docker_cpu";
	
	public static String MYSQL_COUNT="mysql_count";
	
	public static String MYSQL_MEMORY="mysql_memory";
	
	public static String MYSQL_VOLUME="mysql_volume";
	
	public static String RABBITMQ_MEMORY="rabbitmq_memory";
	
	public static String RABBITMQ_COUNT="rabbitmq_count";
	
	/**
	 * @author chenjinfan
	 * @Description 查询所有
	 * @return
	 */
	List<PaaSTenant> list();
	
	/**
	 * @author chenjinfan
	 * @Description
	 * @return 只包含id name
	 */
	List<PaaSTenant> listBrief();
	
	/**
	 * @author chenjinfan
	 * @Description 分页查询所有
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<PaaSTenant> getPage(int pageNo, int pageSize);
	
	/**
	 * @author chenjinfan
	 * @Description 按ID查找
	 * @param id
	 * @return
	 */
	PaaSTenant get(String id);
	
	void delete(String id);
	
	/**
	 * @author chenjinfan
	 * @Description 根据用户Id查找此用户所属的组织（租户）
	 * @param userId
	 * @return
	 */
	List<PaaSTenant> getTenantByUserId(String userId);

	void saveOrUpdate(PaaSTenant tenant);
	
	String getInstallScript();
	
	int getTenantImageCount(String tenantId);

	List<Map<String, Object>> findTenantItemList(List<Map<String,Object>> paramList);

	List<Map<String, String>> getTenantQuotaInfo(Map<String, String> paramMap);

	List<Map<String, String>> getTenantQuotaInfo(String tenantId,String envType, String categoryType, String subCategoryType, String itemLookupType);
}
