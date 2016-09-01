package com.anyi.report.entity;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.bodysign.entity.SpecialVo;


public class DocRecord {
	private String recordId;
	private Date createTime;
	private String create_person;//创建人
	private String approve_status;//审核状态
	private Date modify_time;
	private String template_id;
	private String inpatient_no;
	private String dept_code;
	private String order_code;//如果是转抄过来的医嘱执行记录，需要保存医嘱条码
	private List<RecordDataList> data_list;
	private List<RecordDataItem> data_item;//仅提供给NDA，本来可以与data_item_list合并的，但是NDA不愿改其结构，只好做两个了
	private List<RecordDataItemList> data_item_list;//针对前端打印时使用，需要显示所有的数据
	private List<SpecialVo> specList;  //体征、专科评估数据   业务层调用接口
	private List<RecordDataListDetail> dyna_list;     //动态列数据
	private String date_list;//日期
	private String time_list ;//时间 
	private String now_time;//系统时间
	private String count_time;//统计时间，护理记录单里面入量出量要按照每天来统计
	private String count_time_afternoon;//下午的统计时间
	private DocReportCountInOut count_list;//统计数据 护理记录单里面入量出量要按照每天来统计
	private String isHeader;//是否表头信息
	private int rowHight;//记录的行高，针对list类型文书
	private int pageNo;  //记录所在的页码，针对list类型文书

	private List<String[]> data_list_print;//护理记录单打印时需要用到
	
	//打印时表头如何显示
	//NEW显示最新记录OLD显示最早的记录OLDTONEWE显示显示最早和最新的CHANGE显示该页变化的记录
	private String print_show;
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public List<RecordDataList> getData_list() {
		return data_list;
	}

	public void setData_list(List<RecordDataList> data_list) {
		this.data_list = data_list;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getInpatient_no() {
		return inpatient_no;
	}

	public void setInpatient_no(String inpatient_no) {
		this.inpatient_no = inpatient_no;
	}

	public List<RecordDataItem> getData_item() {
		return data_item;
	}

	public void setData_item(List<RecordDataItem> data_item) {
		this.data_item = data_item;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public String getDate_list() {
		return date_list;
	}

	public void setDate_list(String date_list) {
		this.date_list = date_list;
	}

	public String getTime_list() {
		return time_list;
	}

	public void setTime_list(String time_list) {
		this.time_list = time_list;
	}

	public List<SpecialVo> getSpecList() {
		return specList;
	}

	public void setSpecList(List<SpecialVo> specList) {
		this.specList = specList;
	}

	public List<RecordDataListDetail> getDyna_list() {
		return dyna_list;
	}

	public void setDyna_list(List<RecordDataListDetail> dyna_list) {
		this.dyna_list = dyna_list;
	}

	public List<RecordDataItemList> getData_item_list() {
		return data_item_list;
	}

	public void setData_item_list(List<RecordDataItemList> data_item_list) {
		this.data_item_list = data_item_list;
	}

	public String getPrint_show() {
		return print_show;
	}

	public void setPrint_show(String print_show) {
		this.print_show = print_show;
	}

	public String getNow_time() {
		return now_time;
	}

	public void setNow_time(String now_time) {
		this.now_time = now_time;
	}

	public String getCreate_person() {
		return create_person;
	}

	public void setCreate_person(String create_person) {
		this.create_person = create_person;
	}

	public String getApprove_status() {
		return approve_status;
	}

	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}

	public String getCount_time() {
		return count_time;
	}

	public void setCount_time(String count_time) {
		this.count_time = count_time;
	}


	public DocReportCountInOut getCount_list() {
		return count_list;
	}

	public void setCount_list(DocReportCountInOut count_list) {
		this.count_list = count_list;
	}

	public String getCount_time_afternoon() {
		return count_time_afternoon;
	}

	public void setCount_time_afternoon(String count_time_afternoon) {
		this.count_time_afternoon = count_time_afternoon;
	}

	public List<String[]> getData_list_print() {
		return data_list_print;
	}

	public void setData_list_print(List<String[]> data_list_print) {
		this.data_list_print = data_list_print;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getIsHeader() {
		return isHeader;
	}

	public void setIsHeader(String isHeader) {
		this.isHeader = isHeader;
	}

	public String getDept_code() {
		return dept_code;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
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

}
