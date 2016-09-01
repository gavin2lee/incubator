package com.anyi.report.entity;

import com.lachesis.mnis.core.util.DateUtil;
import org.apache.axis.utils.StringUtils;

import java.util.Date;
import java.util.List;

public class DocReportPrintData {

	private String record_id;//记录id

	private String patID;//患者住院号

	private String templateID;//模板ID

	private String dateList;//日期

	private String timeList;//时间

	private int rowHight;//打印行高

	private int pageNo;//打印页码

	private List<DocReportPrintDataDetail> data_list;//记录明细

	/**
	 * 记录时期比较函数，晚于指定记录
	 * @param record
	 * @return
	 */
	public boolean after(DocReportPrintData record){
		Date thisDate = DateUtil.parse(this.dateList, DateUtil.DateFormat.YMD);
		Date recordDate = DateUtil.parse(record.getDateList(), DateUtil.DateFormat.YMD);
		if(null==thisDate || null==recordDate){
			return false;
		}else if(thisDate.after(recordDate)){
			return true;
		}else if(thisDate.before(recordDate)){
			return false;
		}else {
			Date thisTime = DateUtil.parse(this.timeList.substring(0, 5), DateUtil.DateFormat.HM);
			Date recordTime = DateUtil.parse(record.getTimeList().substring(0, 5), DateUtil.DateFormat.HM);
			if(null==thisTime || null==recordTime){
				return false;
			}else if(thisTime.after(recordTime)){
				return true;
			}else {
				return false;
			}
		}
	}

	/**
	 * 根据模板组件ID获取对应的打印信息
	 * @param itemID 组件ID
	 * @return 有则返回，无则返回空值
     */
    public DocReportPrintDataDetail getDetailDataByTemplateItemID(String itemID){
		if(StringUtils.isEmpty(itemID)){
			return null;
		}
		for (DocReportPrintDataDetail detail : this.data_list) {
			if(itemID.equals(detail.getTemplate_item_id())){
				return detail;
			}
		}
		return null;
	}

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	public String getPatID() {
		return patID;
	}

	public void setPatID(String patID) {
		this.patID = patID;
	}

	public String getTemplateID() {
		return templateID;
	}

	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}

	public List<DocReportPrintDataDetail> getData_list() {
		return data_list;
	}

	public void setData_list(List<DocReportPrintDataDetail> data_list) {
		this.data_list = data_list;
	}

	public int getRowHight() {
		return rowHight;
	}

	public void setRowHight(int rowHight) {
		this.rowHight = rowHight;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getDateList() {
		return dateList;
	}

	public void setDateList(String dateList) {
		this.dateList = dateList;
	}

	public String getTimeList() {
		return timeList;
	}

	public void setTimeList(String timeList) {
		this.timeList = timeList;
	}

}
