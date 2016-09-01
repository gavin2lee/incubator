package com.anyi.report.entity;

public class DocConfig {

	private String doc_type;//文书类型
	private int tri_grade_min;//触发的分数
	private int tri_grade_max;//触发的分数
	private int frequency;//提醒时间（xx小时后）
	private String config_type;//类型 0 展示，1 频次提醒
	private String ref_column;//相关联的字段
	private String tri_content;//提示内容
	public String getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String getConfig_type() {
		return config_type;
	}
	public void setConfig_type(String config_type) {
		this.config_type = config_type;
	}
	public String getRef_column() {
		return ref_column;
	}
	public void setRef_column(String ref_column) {
		this.ref_column = ref_column;
	}
	public int getTri_grade_min() {
		return tri_grade_min;
	}
	public void setTri_grade_min(int tri_grade_min) {
		this.tri_grade_min = tri_grade_min;
	}
	public int getTri_grade_max() {
		return tri_grade_max;
	}
	public void setTri_grade_max(int tri_grade_max) {
		this.tri_grade_max = tri_grade_max;
	}
	public String getTri_content() {
		return tri_content;
	}
	public void setTri_content(String tri_content) {
		this.tri_content = tri_content;
	}
	
}
