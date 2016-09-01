package com.lachesis.mnis.core.whiteBoard.entity;
/**
 * 小白板编辑属性
 * @author ThinkPad
 *
 */

public class NurseWhiteBoardEditType {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 样式类型code
	 */
	private String code;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 是否多行
	 */
	private boolean isMulti;
	/**
	 * 是否有子项
	 */
	private boolean isInner;
	/**
	 * 关联元数据code
	 */
	private String metadataCode;
	/**
	 * 是否只显示床号
	 */
	private boolean isBedCode;
	
	/**
	 * 对应的模板id
	 */
	private String templateId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isMulti() {
		return isMulti;
	}
	public void setMulti(boolean isMulti) {
		this.isMulti = isMulti;
	}
	public boolean isInner() {
		return isInner;
	}
	public void setInner(boolean isInner) {
		this.isInner = isInner;
	}
	public String getMetadataCode() {
		return metadataCode;
	}
	public void setMetadataCode(String metadataCode) {
		this.metadataCode = metadataCode;
	}
	public boolean isBedCode() {
		return isBedCode;
	}
	public void setBedCode(boolean isBedCode) {
		this.isBedCode = isBedCode;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
