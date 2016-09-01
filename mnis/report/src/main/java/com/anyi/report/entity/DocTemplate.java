package com.anyi.report.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.anyi.json.format.JsonDateFormYMDHMS;
import com.anyi.report.vo.SpecialVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DocTemplate {
	private String templateId;
	private String templateName;
	private String docType;
	private String deptCode;
	private String valid;
	private int showIndex;
	private String memo;
	private String templateShowName;
	
	private String orientation; // 打印方式 横向或纵向
	private int height;
	private int width;
	private String template_file_name;//保存的文件名字
	@JsonSerialize(using = JsonDateFormYMDHMS.class)
	private Date createTime;

	private List<DocBand> bands;

	private List<InpatientInfo> inpatient_info;//病人的基本信息
	
	private List<SpecialVo> ref_list;//需要反填的数据 如专科评估的得分，需要反填到首次护理记录单里面
	
	private String now_time;//当前时间，用于传递给前台用（前台时间不一定准确）
	
	private String report_type;//模板小分类，用于确定每个模板的类型，方便转抄时使用
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public int getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTemplateShowName() {
		return templateShowName;
	}

	public void setTemplateShowName(String templateShowName) {
		this.templateShowName = templateShowName;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public List<DocBand> getBands() {
		return bands;
	}

	public void setBands(List<DocBand> bands) {
		this.bands = bands;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplate_file_name() {
		return template_file_name;
	}

	public void setTemplate_file_name(String template_file_name) {
		this.template_file_name = template_file_name;
	}

	public List<InpatientInfo> getInpatient_info() {
		return inpatient_info;
	}

	public void setInpatient_info(List<InpatientInfo> inpatient_info) {
		this.inpatient_info = inpatient_info;
	}

	public String getNow_time() {
		return now_time;
	}

	public void setNow_time(String now_time) {
		this.now_time = now_time;
	}

	public List<SpecialVo> getRef_list() {
		return ref_list;
	}

	public void setRef_list(List<SpecialVo> ref_list) {
		this.ref_list = ref_list;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

}
