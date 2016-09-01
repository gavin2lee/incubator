package com.anyi.report.db;

import com.anyi.report.entity.*;
import com.anyi.report.vo.LeftTreeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportMapper {

	int addTemplate(DocTemplate template);
	int addTemplateItem(DocTemplateItem items);
	List<DocTemplate> getTemplateList(DocTemplate template);
	void addTemplateBand(DocBand docBand);
	void saveRecord(DocRecord paramBean);
	void updateRecord(DocRecord docRecord);
	public int saveRecordItem(RecordDataItem dataitem);
	void updateRecordItem(RecordDataItem dataitem);
	void saveRecordList(RecordDataListForSave datalist);
	void updateRecordList(RecordDataListForSave datalist);
	List<DocRecord> getTemplateData(DocRecord docRecord);
	
	List<PrintModelMain> getDataForPrint(PrintModelMain pmm);
	
	public List<DocType> getDocType(DocType docType);
	public DocType getDocTypeByID(String docTypeID);
	public List<DocTemplate> getDocTemplate(DocTemplate template);
	public List<DocRecord> getDocData(DocRecord docRecord);
	public List<ModelForTreeDataList> getDataList(ModelForTreeDataList docRecord);
	
	//删除文书主表信息
	void delete_data(String recordId);
	//删除子表信息
	void delete_data_item(String recordId);
	
	void delete_template_item(DocTemplate template);
	void delete_template_band(DocTemplate template);
	void delete_template(DocTemplate template);
	void update_template_valid(DocTemplate template);
	
	/**
	 * 获取文书左边树结构
	 * @param  患者住院流水号
	 * @return
	 */
	public List<LeftTreeVo> getTrees(Map<String,String> map);
	
	/**
	 * 获取数据,组建左边树菜单结构(改写getTrees方法)
	 * @param map
	 * @return
	 */
	public List<DocTreeType> getDataForTree(Map<String,String> map);  
	
	public List<DocTreeType> getDataForHistoryTree(Map<String,String> map);

	/**
	 * 获取出院转科病人的文书信息
	 * @param map
	 * @return
	 */
	public List<DocTreeType> getDataOfTransferPatient(Map<String,String> map);
	
	/**
	 * 加载模板数据
	 * @param docRecord 对象中：template_id：模板id inpatient_no:患者id  date_list: 日期  time_list:时间 
	 * @return
	 */
	public DocRecord getTempDatas(DocRecord docRecord);
	
	/**
	 * 查找模板中是否包含特殊元素
	 * @param map   template_id  模板id  type:1、生命体征  2、专科评估
	 */
	public int getSpecials(Map<String,String> map);
	
	/**
	 * 动态列表的数据源
	 * @param dynaMetadata
	 * @return
	 */
	List<DynaMetadata> getDynaItemData(DynaMetadata dynaMetadata);
	
	/**
	 * 获取所有科室，用于上传模板
	 * @return
	 */
	public List <ComWard> queryComWard();
	
	/**
	 * 审签（更新主表审核人和审核时间，审核时间取当前时间）
	 * @param docApprove
	 */
	public void approve(DocApprove docApprove);
	
	/**
	 * 更新子表审核人
	 * @param docApprove
	 */
	public void approve_person(DocApprove docApprove);
	
	/**
	 * 更新字表审核时间（取客户自己填写的时间）
	 * @param docApprove
	 */
	public void approve_time(DocApprove docApprove);
	
	/**
	 * 根据条件获取所有数据，再在后台处理，避免SQL性能问题
	 * @param docRecord
	 * @return
	 */
	public List<DocDataRecord> loadAllData(DocDataRecord docRecord);
	/**
	 * 根据条件获取所有历史数据，再在后台处理，避免SQL性能问题
	 * @param docRecord
	 * @return
	 */
	public List<DocDataRecord> loadAllHistoryData(DocDataRecord docRecord);

	/**
	 * 获取打印数据展示方式
	 * @return
	 */
	public String getShowType();
	
	/**
	 * 根据键值获取数据源选项
	 * @param map
	 * @return
	 */
	public List<DynaMetadata> list_select(Map<String,String> map);
	

	/**
	 * 查询需要反填的数据（如，专科评估需要反填到首次护理记录单中）
	 * @param refMode
	 * @return
	 */
	public List<com.anyi.report.vo.SpecialVo> ref_list(RefModel refMode);
	
	/**
	 * 护理记录单，获取前一天的数据（统计入量和出量）
	 * @param map
	 * @return
	 */
	public List<RecordDataItem> get_before_data(Map<String,String> map);
	
	/**
	 * 护理记录单，获取前一天的数据（统计入量和出量）
	 * @param map
	 * @return
	 */
	public List<RecordDataItem> get_before_history_data(Map<String,String> map);
	
	/**
	 * 获取护理记得单的id
	 * @return
	 */
	public String getTemplateIdByDeptcode(String deptID);
	
	/**
	 * 获取护理文书的配置信息
	 * @param doc_type
	 * @return
	 */
	public  List<DocConfig> get_docConfig(Map<String,String> map);
	
	/**
	 * 更新防跌，防压疮标志 
	 * @param map
	 */
	public int updatePatInfoStat(Map<String,String> map);
	
	/**
	 * 插入防跌，防压疮标志 
	 * @param map
	 * @return
	 */
	public int insertPatInfoStat(Map<String,String> map);
	
	/**
	 * 查看是否有大于该时间段的记录
	 * @param map
	 * @return
	 */
	public int queryReportByTime(Map<String,String> map);
	
	/**
	 * 根据代码获取数据源名字
	 */
	public String getNameBycode(Map<String,String> map);
	
	/**
	 * 判断审核人和责任护士是不是一样的
	 * @param map
	 * @return
	 */
	public int isSamePerson(Map<String,String> map);
	
	/**
	 * 根据时间段统计出入量
	 * @param map
	 * @return
	 */
	public List<RecordDataItem> get_inout_data_bytime(Map<String,String> map);
	
	/**
	 * 根据模板的id查询护理记录单列表中所有的数据节点
	 * @param map
	 * @return
	 */
	public List<String> get_item_list(Map<String,String> map);
	
	/**
	 * 查询数据，用于打印。
	 * @param map
	 * @return
	 */
	public List<DocReportPrintData> queryDataForPrint(Map<String,String> map);
	
	public List<DocReportPrintData> queryHistoryDataForPrint(Map<String,String> map);
	
	/**
	 * 备份护理文书主表数据
	 */
	public void report_data_bak();
	
	/**
	 * 备份护理文书从表的数据
	 */
	public void report_data_item_bak();
	
	/**
	 * 删除护理文书记录（从表）
	 */
	public void delete_report_data_item();
	
	/**
	 * 删除护理文书记录（主表）
	 */	
	public void delete_report_data();
	
	/**
	 * 根据日期和时间，查询护理记录单
	 * @param map
	 */
	public List<String> getRecordIDsByTime(Map<String,Object> map);
	
	/**
	 * 根据记录的id,查询是否有相同的数据
	 * @param map
	 * @return
	 */
	public int getSameItemCountInRecord(Map<String,Object> map);
	
	/**
	 * 获取入量统计同步数据
	 * @param startDate
	 * @param endDate
	 * @param patId
	 * @return
	 */
	List<DocDataReportSync> getDocDataReportInSync(Map<String, Object> params);
	/**
	 * 获取出量统计同步数据
	 * @param startDate
	 * @param endDate
	 * @param patId
	 * @param outName
	 * @return
	 */
	 
	List<DocDataReportSync> getDocDataReportOutSync(Map<String, Object> params);

	/**
	 * 查询患者的默认护理记录单
	 * @param paramMap
	 * @return
	 */
	public List<PatientDefaultTemplate> getPatientDefaultTemplate(Map<String,String> paramMap);

	/**
	 * 更新患者的默认护理记录单
	 * @param paramMap
	 */
	public void updatePatientDefaultTemplate(Map<String,String> paramMap);

	/**
	 * 新增默认护理记录单
	 * @param paramMap
	 */
	public void insertPatientDefaultTemplate(Map<String,String> paramMap);

	/**
	 * 根据医嘱条码查询文书记录
	 * @param orderCode
	 * @return
	 */
	public Integer getRecordCountByOrdercode(String orderCode);

	/**
	 * 记录转抄失败的文书信息
	 * @param info
	 */
	public void saveFailRecordInfo(DocRecordFailInfo info);
	public String getRecordIDByPatId(String patId);

	/**
	 * 获取文书打印页码
	 * @param paramMap
	 * @return
	 */
	public Integer getReportPrintNum(Map<String,Object> paramMap);

	/**
	 *更新文书打印页码
	 * @param paramMap
	 * @return
	 */
	public void updateReportPrintNum(Map<String,Object> paramMap);

	/**
	 * 保存文书打印页码
	 * @param paramMap
	 */
	public void insertReportPrintNum(Map<String,Object> paramMap);
	/**
	 * 插入统计信息
	 * @param docRecordStatistic
	 * @return
	 */
	int insertDocReprotStatistic(DocRecordStatistic docRecordStatistic);
	/**
	 * 修改统计信息
	 * @param docRecordStatistic
	 * @return
	 */
	int updateDocReprotStatistic(DocRecordStatistic docRecordStatistic);
	/**
	 *  根据患者获取所有统计
	 * @param patId			:患者
	 * @param deptCode		:部门
	 * @param staticDate	:统计日期
	 * @param staticRegion	:统计区间0：上部分,1:下部分
	 * @return
	 */
	List<DocRecordStatistic> getDocReprotDataStatistic(String patId,String deptCode
			,String staticDate,String staticTime,String templateID);
	
	DocRecordStatistic getDocReprotStatistic(String patId,String deptCode
			,String staticDate,String staticTime,String type,String itemType, String templateID);
	/**
	 * 根据时间区间获取数据
	 * @param patId
	 * @param deptCode
	 * @param staticDate
	 * @param staticTime
	 * @return
	 */
	List<DocRecordDataStatistic> getDocReprotDataAllStatistic(String patId,String deptCode
			,String startDate,String endDate,String templateID);

	/**
	 * 获取指定打印页码上的统计记录
	 * @param patID
	 * @param templateID
	 * @param startPage
	 * @param endPage
	 * @return
	 */
	public List<DocRecordDataStatistic> getStatisticDataByPageNo(Map<String, Object> paramMap);

	/**
	 * 更新统计记录的打印信息
	 * @param record
	 */
	public void updatePrintInfo4StatisticRecord(DocReportPrintData record);

	/**
	 * 检查患者指定模板上含有无效页码的统计记录条目
	 * @param patID
	 * @param templateID
	 * @return
	 */
	public int getStatisticCountWithInvalidPageNo(@Param("patID")String patID, @Param("templateID")String templateID);
	
	List<DocDataRecordItem> getDocDataRecordItems(String recordId);
	/**
	 * 获取列表文书的表头信息
	 * @param paramMap
	 * @return
	 */
	public List<DocDataRecord> getDocHeaderRecord(Map<String,String> paramMap);

	/**
	 * 根据护理记录ID，获取对应的打印模板信息
	 * @param templateIDs
	 * @return
	 */
	public List<DocTemplate> getPrintTemplateByID(List<String> templateIDs);

	/**
	 * 根据时间排序，获取指定文书记录的前一条记录
	 * @param record
	 * @return
	 */
	public List<DocRecord> getOneRecordBeforTheRecord(DocRecord record);

	/**
	 * 获取指定打印页码及其以后的文书记录
	 * @param patID
	 * @param templateID
	 * @param pageNo
	 * @return
	 */
	public List<DocRecord> getRecordsOnAndAfterPageNo(@Param("patID")String patID, @Param("templateID")String templateID,
													  @Param("pageNo")int pageNo);

	/**
	 * 获取患者指定文书中所有的记录，以日期和时间排序
	 * @param patID      患者住院号
	 * @param templateID 文书模板ID
	 * @return
	 */
	public List<DocRecord> getAllRecordsForPatient(@Param("patID")String patID, @Param("templateID")String templateID);

	/**
	 * 更新指定文书记录的打印信息，包括行高、页码和分页信息
	 * @param recordID 指定文书录的ID
	 * @param rowHight 行高，大于0有效
	 * @param pageNo   页码，大于0有效
	 */
	public void updatePrintInfo4Record(@Param("recordID")String recordID, @Param("rowHight")int rowHight, @Param("pageNo")int pageNo);

	/**
	 * 获取指定文书记录的打印数据
	 * @param recordID 记录ID
	 * @param itemList 模板组件名称集合
	 * @return
	 */
	public DocReportPrintData getPrintDataForRecord(@Param("recordID")String recordID, @Param("itemList")List<String> itemList);

	/**
	 * 获取患者指定文书的所有记录打印数据
	 * @param patID      住院号
	 * @param templateID 模板ID
	 * @param itemList   组件名称集合
	 * @return 打印数据集合，以日期和时间排序
	 */
	public List<DocReportPrintData> getAllRecordsPrintDataForPatient(@Param("patID")String patID, @Param("templateID")String templateID,
																	 @Param("itemList")List<String> itemList);

	/**
	 * 获取患者文书中指定页码记录的打印数据
	 * @param patID      住院号
	 * @param templateID 模板ID
	 * @param startPage  起始页码
	 * @param endPage    结束页码
	 * @param numType    打印页码类型，0 正常，1 奇数页打印，2 偶数页打印
	 * @param itemList   组件名称集合
	 * @return 打印数据集合，以日期和时间排序
	 */
	public List<DocReportPrintData> getRecordsPrintDataForPatient(Map<String, Object> paramMap);

	/**
	 * 获取患者历史文书中指定页码记录的打印数据
	 * @param patID      住院号
	 * @param templateID 模板ID
	 * @param startPage  起始页码
	 * @param endPage    结束页码
	 * @param numType    打印页码类型，0 正常，1 奇数页打印，2 偶数页打印
	 * @param itemList   组件名称集合
	 * @return 打印数据集合，以日期和时间排序
	 */
	public List<DocReportPrintData> getRecordsPrintDataForPatientInHistory(Map<String, Object> paramMap);

	/**
	 * 获取患者文书中持有无效打印页码的记录数量
	 * @param patID      患者住院号
	 * @param templateID 文书模板ID
	 * @return 文书记录的数目
	 */
	public int getRecordCountWithInvalidPageNo(@Param("patID")String patID, @Param("templateID")String templateID);

	/**
	 * 获取患者历史文书中持有无效打印页码的记录数量
	 * @param patID      患者住院号
	 * @param templateID 文书模板ID
	 * @return 文书记录的数目
	 */
	public int getRecordCountWithInvalidPageNoInHistory(@Param("patID")String patID, @Param("templateID")String templateID);

	/**
	 * 获取患者指定模板文书记录中最大的打印页码
	 * @param patID
	 * @param templateID
     * @return
     */
    public int getMaxPageNoOfPatDocRecords(@Param("patID") String patID, @Param("templateID") String templateID);

	/**
	 * 获取指定日期文书记录所在的打印页码
	 * @param patID 患者住院号
	 * @param templateID 模板ID
	 * @param date 指定日期
     * @return 页码数组，按从小到大排序
     */
    public Integer[] getPrintPageNoForSpecifiedDate(@Param("patID")String patID, @Param("templateID")String templateID,
												@Param("startDate")String startDate, @Param("endDate")String endDate);

	/**
	 * 根据指定的ID获取文书记录信息，不包含具体记录数据
	 * @param recordID 记录ID
	 * @return
	 */
	public DocRecord getDocRecordByID(@Param("recordID")String recordID);

	/**
	 * 获取指定时间段内体温最大值对应的文书记录
	 * @param paramMap 参数集合，包括：patID 患者住院号，templateID 模板ID，startDate 起始日期，
	 *                    			 startTime 起始时间，endDate 截止日期，endTime 截止时间
     * @return 文书记录信息，不包含数据
     */
    public DocRecord getRecordWithMaxTempratureInTimeInterval(Map<String, String> paramMap);

	/**
	 * 保存用户打印信息
	 * @param printInfo
     */
    public void savePatPrintInfo(PrintInfoRecord printInfo);

	/**
	 * 设置文书的用户权限
	 * @param permission 用户权限信息
     */
    public void saveDocReportUserPermission(DocUserPermission permission);

	/**
	 * 获取用户权限信息
	 * @param permission 查询条件信息
	 * @return 用户权限信息集合，如果没有符合条件的记录，集合为空
     */
    public List<DocUserPermission> getDocReportUserPermission(DocUserPermission permission);

    /**
	 * 获取前一天的出入量累计
	 * @param parm
	 * @return
	 */
	public List<Map<String,Object>> getBackDayInOut7(Map<String,Object> parm);

	/**
	 * 获取指定日期内文书记录的大便次数
	 * @param date 日期
	 * @return
     */
    public Integer getRecordStoolCountForSpecifiedDate(@Param("patID")String patID, @Param("templateID")String templateID,
													   @Param("date")String date);

}
