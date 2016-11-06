package com.harmazing.openbridge.paasos.manager.model;

import com.harmazing.framework.common.model.BaseModel;

/**
* @ClassName: PaaSTenantQuota 
* @Description: 组织配额对象
* @author weiyujia
* @date 2016年7月7日 下午9:02:22 
*
 */
public class PaaSTenantQuota extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 租户ID
	 */
	private String tenantId;
	/**
	 * 主分类
	 */
	private String categoryType;
	/**
	 * 子分类
	 */
	private String subCategoryType;
	/**
	 * 项目
	 */
	private String itemLookupType;
	/**
	 * 配额
	 */
	private String quota;
	/**
	 * 单位
	 */
	private String units;
	
	/**
	 * 默认值
	 */
	private String defaultValue;
	
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getSubCategoryType() {
		return subCategoryType;
	}
	public void setSubCategoryType(String subCategoryType) {
		this.subCategoryType = subCategoryType;
	}
	public String getItemLookupType() {
		return itemLookupType;
	}
	public void setItemLookupType(String itemLookupType) {
		this.itemLookupType = itemLookupType;
	}
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
	
}
