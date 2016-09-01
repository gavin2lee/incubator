package com.lachesis.mnis.core.bodysign.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 生命体征项目系统配置
 * @author ThinkPad
 *
 */
public class BodySignItemConfig {
	private int id;
	private String code;
	
	@SerializedName("fields")
	private BodySignItemConfigItem bodySignItemConfigItem;
	@SerializedName("opts")
	private List<BodySignItemConfigOpts> bodySignItemConfigOpts;
	
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


	public BodySignItemConfigItem getBodySignItemConfigItem() {
		return bodySignItemConfigItem;
	}

	public void setBodySignItemConfigItem(
			BodySignItemConfigItem bodySignItemConfigItem) {
		this.bodySignItemConfigItem = bodySignItemConfigItem;
	}

	public List<BodySignItemConfigOpts> getBodySignItemConfigOpts() {
		return bodySignItemConfigOpts;
	}

	public void setBodySignItemConfigOpts(
			List<BodySignItemConfigOpts> bodySignItemConfigOpts) {
		this.bodySignItemConfigOpts = bodySignItemConfigOpts;
	}


	public static class BodySignItemConfigItem{
		/**
		 * code
		 */
		private String code;
		/**
		 * name
		 */
		private String name;
		/**
		 * 单位
		 */
		private String unit;
		/**
		 * 一天次数
		 */
		private int index;
		/**
		 * 类型名称
		 */
		private String typeName;
		/**
		 * 校验类型
		 */
		private String validType;
		/**
		 * 校验函数
		 */
		private String validFunction;
		/**
		 * 错误消息提示
		 */
		private String invalidMsg;
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
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getValidType() {
			return validType;
		}
		public void setValidType(String validType) {
			this.validType = validType;
		}
		public String getTypeName() {
			return typeName;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getValidFunction() {
			return validFunction;
		}
		public void setValidFunction(String validFunction) {
			this.validFunction = validFunction;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public String getInvalidMsg() {
			return invalidMsg;
		}
		public void setInvalidMsg(String invalidMsg) {
			this.invalidMsg = invalidMsg;
		}
		
	}
	
	public static class BodySignItemConfigOpts{
		/**
		 * code
		 */
		private String code;
		private String parentCode;
		/**
		 * 设置选项text
		 */
		private String text;
		/**
		 * 是否可编辑
		 */
		private String editFlag;
		/**
		 * 是否默认选择
		 */
		private boolean isSelected;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getEditFlag() {
			return editFlag;
		}
		public void setEditFlag(String editFlag) {
			this.editFlag = editFlag;
		}
		public boolean isSelected() {
			return isSelected;
		}
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}
		public String getParentCode() {
			return parentCode;
		}
		public void setParentCode(String parentCode) {
			this.parentCode = parentCode;
		}
	}
}
