package com.harmazing.openbridge.sys.tag.model;

import java.util.Date;

import com.harmazing.framework.common.model.IBaseModel;

/**
 * 
 * <pre>
 * 标签实体类
 * </pre>
 * 
 * @author hehuajun,taoshuangxi
 * 
 */
public class SysTag implements IBaseModel {

	private static final long serialVersionUID = 961300839430343312L;
	private String tagId; // ID
	private String tagText; // 标签文字
	private Date createTime; // 创建时间
	private Boolean hot;//是否属于热门标签

	public SysTag(String tagId, String tagText, Date createTime) {
		super();
		this.tagId = tagId;
		this.tagText = tagText;
		this.createTime = createTime;
	}

	public SysTag() {
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getHot() {
		return hot;
	}

	public void setHot(Boolean hot) {
		this.hot = hot;
	}

}
