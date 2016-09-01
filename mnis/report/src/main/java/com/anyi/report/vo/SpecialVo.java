package com.anyi.report.vo;

/**
 * 体征：
 * 体温：TP		脉搏：PLS	呼吸频率：DE04_10_081_00	
 * 血压：BLOOD_PRESSURE		体重：DE04_10_188_00
 * 
 * 专科评估：ADL评分：LX_SZSANYUAN_059		跌倒评分：LX_SZSANYUAN_054		
 * 压疮评分：LX_SZSANYUAN_055				疼痛评估：LX_ZJYONGKANG_043		
 * 其他：LX_SZSANYUAN_058		
 *  第一次生成模板，加载生命体征、专科评估
 * @author qingzhi.liu
 *
 */
public class SpecialVo {
	private String record_key;   //数据源
	private String record_value;   //数据值
	
	public SpecialVo() {
		super();
	}
	
	
	public SpecialVo(String record_key, String record_value) {
		super();
		this.record_key = record_key;
		this.record_value = record_value;
	}

	public String getRecord_key() {
		return record_key;
	}
	public void setRecord_key(String record_key) {
		this.record_key = record_key;
	}
	public String getRecord_value() {
		return record_value;
	}
	public void setRecord_value(String record_value) {
		this.record_value = record_value;
	}
	
	
}
