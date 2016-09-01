package com.anyi.report.service;

import com.anyi.report.entity.*;
import com.anyi.report.json.BaseListVo;
import com.anyi.report.json.BaseMapVo;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecordVo;
import com.lachesis.mnis.core.event.entity.DocReportEventEntity;
import com.lachesis.mnis.core.exception.MnisException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DocReportService {

	public void addTemplate(String fileName, MultipartFile temFile,String dept_code,String fName,String doc_type,
							String report_type,String templateId, int showOrder, boolean bDefaultTempl);
	public List<DocTemplate> getTemplateList(DocTemplate template,String inpatient_no,boolean isHistory);
	
	public HashMap<String, Object> saveData(DocRecord record,boolean isNurseRecord,String deptCode, boolean isCopy);
	public List<DocRecord> getTemplateData(DocRecord docRecord);
	
	public List<PrintModelMain> getDataForPrint(PrintModelMain pmm);
	
	
	/**
	 * 获取左边文书结构树
	 * @param queryType  查询类型  NDA、PC
	 * @param inpatient_no
	 * @return
	 */
	public BaseListVo getTrees(String queryType,String inpatient_no,String dept_code, int docQueryType);
	
	/**
	 * 加载模板数据
	 * @param docRecord 对象中：template_id：模板id inpatient_no:患者id  date_list: 日期  time_list:时间  isHistory:历史数据
	 * @return
	 */
	public BaseMapVo getTempDatas(DocRecord docRecord,String isprint,String startDate,String endDate,boolean isHistory,String deptCode);
	
	public String delete_template(DocTemplate template);
	public String update_template_valid(DocTemplate template);	
	
	public HashMap<String, Object> delte_data(DocRecord record,boolean isNurseRecord,String deptCode);
	
	public List<DocType> getDocType(DocType docType);	
	
	/**
	 * 动态获取数据源选项
	 * @param code
	 * @return
	 */
	public BaseListVo getDynaItemData(String checkbox_code,String type);
	
	/**
	 * 获取所有科室，用于上传模板
	 * @return
	 */
	public BaseListVo queryComWard();
	
	/**
	 * 审签
	 * @param data
	 * @return
	 */
	public BaseListVo approve(DocApprove data);
	
	/**
	 * 文书转抄接口
	 * @param docReportEventEntity
	 */
	public void saveMnisInfo(DocReportEventEntity docReportEventEntity);
	
	
	/**
	 * 高责医生，护士长查房记录
	 * @param type 类型 1 高责 2 护士长
	 * @param inpatient_no
	 * @param create_person
	 * @param date_list 日期
	 * @param time_list 时间
	 */
	public String nurseRounds(String type,String inpatient_no,String create_person,
							  String date_list,String time_list, String template_id);
	
	/**
	 * 获取护理文书配置信息
	 * @param doc_type
	 * @return
	 */
	public List<DocConfig> get_docConfig(Map<String,String> map);
	
	/**
	 * 更新防跌，防压疮标志 
	 * @param map
	 */
	public void updatePatInfoStat(Map<String,String> map);
	
	/**
	 * 数据备份
	 */
	public void reportDataBak();
	
	/**
	 * 获取出入量统计同步数据
	 * @param startDate
	 * @param endDate
	 * @param patId
	 * @return
	 */
	List<DocDataReportSync> getDocDataReportSync(String startDate,String endDate,String patId,List<String> outNames) throws Exception;
	
	void docDataReportSync(List<DocDataReportSync> docDataReportSyncs,String recordDay,Date fullDateTime) throws Exception;

	/**
	 * 设置患者默认护理记录单
	 * @param patID
	 * @param templateID
	 * @param deptCode
	 */
	public void setPatientDefaultTemplate(String patID, String templateID, String deptCode);

	/**
	 * 设置文书打印页码数
	 * @param patID
	 * @param templateID
	 * @param printNum
	 * @param createPerson
	 */
	public void setReportPrintNum(String patID, String templateID, String printNum, String createPerson);

	/**
	 * 获取文书打印页码数
	 * @param patID
	 * @param templateID
	 */
	public int getReportPrintNum(String patID, String templateID);
	/**
	 *  根据患者获取所有统计
	 * @param patId			:患者
	 * @param deptCode		:部门
	 * @param staticDate	:统计日期
	 * @param staticRegion	:统计区间0：上部分,1:下部分
	 * @return
	 */
	DocRecordDataStatistic  getDocReprotDataStatistic(String patId,String deptCode,String templateID
			,String staticDate,String staticTime);

	/**
	 *获取文书表头信息
	 * @param patID
	 * @param templateID
	 * @param isPrint
	 * @return
	 */
	public DocRecord getDocHeaderDate(String patID, String templateID, String isPrint) throws Exception;

	/**
	 * 清除模板及元数据缓存
	 * @throws Exception
	 */
	public void clearDocTemplateCache() throws Exception;

	/**
	 * 获取患者指定LIST类型文书的打印数据
	 * @param patID      患者住院号
	 * @param templateID 指定模板ID
	 * @param startPage  打印起始页码
	 * @param endPage    打印结束页码
	 * @param numType    打印页码类型，0 正常，1 奇数页打印，2 偶数页打印
	 * @param startDate  指定起始日期，打印该日期区间所在页的所有记录
	 * @param endDate    指定截止日期
	 * @param isHistory  历史文书记录（true）,当前文书记录（false）
	 * @return 患者文书记录的打印数据，包含出入量统计信息（如果必要的话）。
	 * @throws MnisException
	 */
	public List<String[]> loadPrintDataInListTemplateForPatient(final String patID, final String templateID, int startPage,
																int endPage, int numType, String startDate,
																String endDate, final boolean isHistory)throws MnisException;

	/**
	 * 记录患者相关的打印信息
	 * @param printInfo 打印信息
	 * @throws MnisException
     */
    public void recordPatPrintInfo(PrintInfoRecord printInfo) throws MnisException;

	/**
	 * 获取指定时间段内，具有体温记录最大值的文书记录数据（主要为生命体征数据）
	 * @param patID 患者住院号
	 * @param deptCode 病区编号
	 * @param date 指定日期
	 * @param time 指定时间
     * @return 文书记录数据，以生命体征数据结构的形式组织
     */
    public List<BodySignRecordVo> getDocRecordInfoWithMaxTemprature(String patID, String deptCode, String date, String time);

	/**
	 * 获取用户在指定患者文书模板下的权限
	 * @param userCode 用户编号
	 * @param deptCode 科室编号
	 * @param patID 患者住院号
	 * @param templateID 模板ID
     * @return 用户权限标识，无权限为0
     */
    public Integer getDocReportUserEditPermission(final String userCode, final String deptCode,
											  final String patID, final String templateID);

	/**
	 * 设置用户对文书操作的权限
	 * @param permission 权限信息
	 * @throws MnisException
     */
    public void saveDocReportUserPermission(DocUserPermission permission)throws MnisException;

}
