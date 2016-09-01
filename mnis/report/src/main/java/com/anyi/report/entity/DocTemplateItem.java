package com.anyi.report.entity;

import java.util.List;

public class DocTemplateItem {
	private String parentBandId;
	private String itemId;
	private String item_name;
	private int posX;
	private int posY;
	private int width;
	private int height;
	private boolean required;
	private String type;
	private String inputType;
	private List<DocTemplateItemOption> options;
	private List<DocTemplateItemCheckBox> checkbox; 
	private int index;
	private String align;//左右居中
	private String margin;//上下居中
	private String isbold;//是否加粗
	private String size;//字体大小
	
	private String min_value;//最小值
	private String max_value;//最大值
	private String data_type;//数据类型
	private String readonly_flag;
	private String direction;//画table时分割线的方向
	private String if_show;//是否展示（多级联动时下一级不展示）
	private String event_type;//事件类型(CFS 父子联动 CFV 父设置子选内容)
	
	private String metadata_name;//全称
	private String metadata_simple_name;//简称
	private String precision;
	private String default_value;//默认值
	private String unit;//单位
	private String score;//分数
	private String image_url;//图片的路径
	private String if_rel;//字段是否需要反填（专科评估的数据需要反填到首次护理记录单）
	private String if_underline;//输入框是否需要划线
	private String target;//关联触犯的元素，如省份触发城市
	private String children;//字节点
	private boolean saveRequired;//是否必须要保存的字段

	public boolean isSaveRequired() {
		return saveRequired;
	}

	public void setSaveRequired(boolean saveRequired) {
		this.saveRequired = saveRequired;
	}

	public String getParentBandId() {
		return parentBandId;
	}

	public void setParentBandId(String parentBandId) {
		this.parentBandId = parentBandId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}



	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public List<DocTemplateItemOption> getOptions() {
		return options;
	}

	public void setOptions(List<DocTemplateItemOption> options) {
		this.options = options;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<DocTemplateItemCheckBox> getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(List<DocTemplateItemCheckBox> checkbox) {
		this.checkbox = checkbox;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getIsbold() {
		return isbold;
	}

	public void setIsbold(String isbold) {
		this.isbold = isbold;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMin_value() {
		return min_value;
	}

	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}

	public String getMax_value() {
		return max_value;
	}

	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getMetadata_name() {
		return metadata_name;
	}

	public void setMetadata_name(String metadata_name) {
		this.metadata_name = metadata_name;
	}

	public String getMetadata_simple_name() {
		return metadata_simple_name;
	}

	public void setMetadata_simple_name(String metadata_simple_name) {
		this.metadata_simple_name = metadata_simple_name;
	}

	public String getDefault_value() {
		return default_value;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getReadonly_flag() {
		return readonly_flag;
	}

	public void setReadonly_flag(String readonly_flag) {
		this.readonly_flag = readonly_flag;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getIf_rel() {
		return if_rel;
	}

	public void setIf_rel(String if_rel) {
		this.if_rel = if_rel;
	}

	public String getIf_underline() {
		return if_underline;
	}

	public void setIf_underline(String if_underline) {
		this.if_underline = if_underline;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getIf_show() {
		return if_show;
	}

	public void setIf_show(String if_show) {
		this.if_show = if_show;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

}
