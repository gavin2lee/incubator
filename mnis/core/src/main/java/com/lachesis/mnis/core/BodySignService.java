package com.lachesis.mnis.core;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.bodysign.entity.BodySignDict;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecordVo;
import com.lachesis.mnis.core.bodysign.entity.BodySignSpeedy;
import com.lachesis.mnis.core.bodysign.entity.BodySignItemConfig;
import com.lachesis.mnis.core.bodysign.entity.BodyTempSheet;
import com.lachesis.mnis.core.bodysign.entity.SpecialVo;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;

/***
 * 
 * 生命体征Facade接口
 *
 * @author yuliang.xu
 * @date 2015年6月4日 下午3:16:30 
 *
 */
public interface BodySignService {
	
	/**
	 * 缓存系统生命体征选项key - value
	 */
	public static Map<String, BodySignDict> BODY_SIGN_DICT_MAP = new HashMap<String, BodySignDict>();

//	//时间点的选择
//	BodySignTimePointStrategy POINT_STRATEGY = BodySignTimePointStrategy.THREE; 
//	//索引计算规则
//	BodySignIndexStrategy INDEX_STRATEGY = BodySignIndexStrategy.BEFORE;
//	//数据取值方式
//	BodySignDataChoiceStrategy DATA_STRATEGY = BodySignDataChoiceStrategy.SIX_POINT;
//	
	/**
	 * 获取所有体征项目
	 */
	List<BodySignDict> getBodySignDicts();
	
	/***
	 * 
	 * 根据体征项目代码获取体征项目名称
	 *
	 * @param itemCode
	 * @return  项目名称
	 */
	String getBodySignItemNameByCode(String itemCode);
	
	/**
	 * 获得指定日期的体温/生命体征记录
	 * @param date 指定日期 yyyy-MM-dd
	 * @param patientId 病人id
	 * @return
	 */
	List<BodySignRecord> getBodySignRecord(Date date, String patientId,boolean isSpecialDay);
	
	/**
	 * 获得指定日期的体温/生命体征记录
	 * @param date 指定日期 yyyy-MM-dd
	 * @param patientIds 病人ids
	 * @param isSpecialDay 是否是指定时间还是整天
	 * @return
	 */
	List<BodySignRecord> getBodySignRecord(Date date, String[] patientIds,boolean isSpecialDay);

	/**
	 * 保存一次测量的体温/生命体征记录
	 * @param record 病人体温/生命体征记录
	 * @return
	 */
	void saveBodySignRecord(BodySignRecord record,boolean isCopy);
	
	/**
	 *删除测量的体温/生命体征记录 
	 * @param bodySignRecord病人体温/生命体征记录
	 * @return
	 */
	void deleteBodySignRecord(int id);
	
	/**
	 * 删除体征记录
	 * @param bodySignRecord
	 */
	void delete(BodySignRecord bodySignRecord);
	
	/**
	 * 修改一次测量的体温/生命体征记录
	 * @param record 病人体温/生命体征记录
	 * @return
	 */
	void updateBodySignRecord(BodySignRecord record,boolean isCopy);
	
	/**
	 * 保存皮试结果到体温单
	 * @param test
	 * @return
	 */
	void saveSkinTestInfoToBodySign(PatientSkinTest test);
	
	/**
	 * 批量保存生命体征
	 *
	 * @param bodySignRecords
	 * @param isSave
	 * @param userInformation
	 */
	void batchBodySignRecord(List<BodySignRecord> bodySignRecords, boolean isSave, UserInformation userInformation,boolean isCopy);

	/**
	 * 获取指定日期(天)前或后一周的体温单。如果该指定日在入院前或出院后，则自动重置为入院、出院进行计算
	 * @param day 			指定日期 yyyy-MM-dd
	 * @param patient 	病人信息
	 * @param weekOffset 	-1:上周 0:本周 1:下周
	 * @return
	 * @throws ParseException 
	 */
	BodyTempSheet getBodyTempSheet(Date day, Patient patient, int weekOffset);
	
	void updateBodySignRecordUser(BodySignRecord record, UserInformation user) ;
	
	/**
	 * 生命体征快捷查询
	 * @param deptCode 科室ID(为空则查询全院)
	 * @param nurseCode 责任护士ID
	 * @return
	 */
	List<BodySignSpeedy> getBodySignSpeedy(String deptCode,String nurseCode);
	
	
	/**
	 * 获取第一次录入生命体征数据 
	 * @param pat_id
	 * @return
	 */
	public List<SpecialVo> getFirstBodySigns(String pat_id);
	
	/**
	 * 根据记录时间和体征项获取记录
	 * @param recordDate
	 * @param itemCode
	 * @param deptCode
	 * @return
	 */	public List<BodySignItem> getBodySignItemByCode(String recordDate,String itemCode,String deptCode,String patId,Date startDate,Date endDate);

	 /**
	  * 根据记录时间获取体征记录
	  * @param recordDate
	  * @param deptCode
	  * @return
	  */
	List<BodySignRecord> getBodySignRecordByRecordDate(String recordDate,String patId,
			String deptCode);
	
	/**
	 * 查询科室患者的所有生命体征信息
	 * @param deptCode:科室编号
	 * @param patId：患者ID
	 * @param recordDate：记录日期
	 * @param recordTime：记录时间
	 * @return
	 */
	public List<BodySignRecord> queryBodySignList(String deptCode,String patId,String recordDate);
	
	/**
	 * PC端批量新增生命体征
	 * @param bodySignRecords
	 */
	public void batchSaveFixTimeBodySignRecords(List<BodySignRecord> bodySignRecords,UserInformation userInformation);
	
	/**
	 * 获取pc端体温单的生命体征数据
	 * @param patId
	 * @param date
	 * @return
	 */
	public List<BodySignRecordVo> getBodySignRecordVosToSheet(String patId,String date);
	/**
	 * 保存固定生命体征记录
	 * @param bodySignRecord
	 */
	public void saveFixTimeBodySignRecord(BodySignRecord bodySignRecord,UserInformation userInformation);
	/**
	 * 获取单个时间点,多个患者的体征记录
	 * @param patIds
	 * @param date
	 * @param isSpecialDay
	 * @return
	 */
	public List<BodySignRecord> getSingleFixTimeBodySignRecords(String[] patIds, Date date,boolean isSpecialDay);
	
	/**
	 * 获取多个患者,体温单所有时间点记录
	 * @param patIds
	 * @param date
	 * @param isNda
	 * @param isAllItem
	 * @return
	 */
	public List<BodySignRecord> getFixTimeBodySignRecords(String[] patIds, String date,boolean isNda,boolean isAllItems);
	
	/**
	 * 针对生命体征批量录入功能,需要删除部分体征信息
	 * @param patId
	 * @param recordDate
	 */
	public void deletePartByTime(String patId,String recordDate);
	
	/**
	 * 保存生命体征信息
	 * @param records
	 */
	public void saveBodySignRecords(List<BodySignRecord> records);
	
	/**
	 * 查询体温单信息
	 * @param deptCode:科室编号
	 * @param patId：患者ID
	 * @param recordDate：记录日期
	 * @param recordTime：记录时间
	 * @return
	 */
	public List<BodySignRecord> queryBodyTemperature(String deptCode,String patId,String recordDate);
	
	/**
	 * 获取生命体征配置信息
	 * @param deptCode
	 * @return
	 */
	List<BodySignItemConfig> getBodySignConfigByDeptCode(String deptCode);
	
	/**
	 * 从文书中读取生命体征数据
	 */
	public void copyBodySignFromDoc();

	/**
	 * 更新（文书记录中的）生命体征信息到数据库体征表
	 * @param record 传入的体征信息
     */
    public void updateBodySignRecordValue(BodySignRecord record);

	/**
	 * 获取指定时间内进行了降温处理的生命体征信息
	 * @param patID 患者住院号
	 * @param deptCode 病区编号
	 * @param date 指定日期
	 * @param time 指定时间
     * @return 生命体征信息记录，不包含具体数据
     */
    public List<BodySignRecord> getBodySignRecordsWithCoolingProcess(String patID, String deptCode,
																	 String date, String time);

	/**
	 * 把生命体征记录数据组织成VO形式，方便输出到体温单
	 * @param records 生命体征记录
	 * @return 生命体征VO形式数据集合，含有元素个数为体温录入时间点的个数
	 */
	public List<BodySignRecordVo> getBodySignRecordVosFromBodySignRecords(List<BodySignRecord> records);

	/**
	 * 转抄指定的生命体征数据到护理文书
	 * @param patIDs 指定患者的住院号集合
	 * @param deptCode 科室编号
	 * @param date 指定日期
	 * @param time 指定体征录入时间
	 * @throws Exception
	 */
	public void copyBodySignRecordsToDocRecords(List<String> patIDs, String deptCode, String date, String time)throws Exception;
}
