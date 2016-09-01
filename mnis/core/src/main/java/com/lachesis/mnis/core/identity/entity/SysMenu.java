package com.lachesis.mnis.core.identity.entity;

import java.util.List;
/**
 * PC端系统菜单设置
 * @author ThinkPad
 *
 */
public class SysMenu {
	/**
	 * 主键
	 */
	private int id;
	/**
	 * 菜单code
	 */
	private String code;
	/**
	 * 菜单name
	 */
	private String name;
	/**
	 * 父菜单code
	 */
	private String parentCode;
	/**
	 * url
	 */
	private String url;
	/**
	 * 排序
	 */
	private int orderNo;
	
	/**
	 * 资源文件路径
	 */
	private String resourceUrl;
	/**
	 * url类型：1：无需参数,0:需要参数
	 */
	private String urlType;
	/**
	 * 子菜单项
	 */
	private List<SysMenu> children;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public List<SysMenu> getChildren() {
		return children;
	}
	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getUrlType() {
		return urlType;
	}
	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	
}
