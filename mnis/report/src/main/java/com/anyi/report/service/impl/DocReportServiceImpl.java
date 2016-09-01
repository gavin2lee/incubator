package com.anyi.report.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseBreak;
import net.sf.jasperreports.engine.base.JRBaseComponentElement;
import net.sf.jasperreports.engine.base.JRBaseImage;
import net.sf.jasperreports.engine.base.JRBaseLine;
import net.sf.jasperreports.engine.base.JRBaseStaticText;
import net.sf.jasperreports.engine.base.JRBaseTextField;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.anyi.report.DocReportConstants;
import com.anyi.report.constants.DocPrintConstants;
import com.anyi.report.constants.ReportConstants;
import com.anyi.report.db.ActionMapper;
import com.anyi.report.db.ReportMapper;
import com.anyi.report.entity.ActionRecord;
import com.anyi.report.entity.DcoTreeTemplate;
import com.anyi.report.entity.DocApprove;
import com.anyi.report.entity.DocBand;
import com.anyi.report.entity.DocConfig;
import com.anyi.report.entity.DocDataRecord;
import com.anyi.report.entity.DocDataRecordItem;
import com.anyi.report.entity.DocDataReportSync;
import com.anyi.report.entity.DocPrintPaper;
import com.anyi.report.entity.DocRecord;
import com.anyi.report.entity.DocRecordDataStatistic;
import com.anyi.report.entity.DocRecordFailInfo;
import com.anyi.report.entity.DocRecordStatistic;
import com.anyi.report.entity.DocReportCountInOut;
import com.anyi.report.entity.DocReportPrintData;
import com.anyi.report.entity.DocReportPrintDataDetail;
import com.anyi.report.entity.DocTemplate;
import com.anyi.report.entity.DocTemplateItem;
import com.anyi.report.entity.DocTreeRecord;
import com.anyi.report.entity.DocTreeType;
import com.anyi.report.entity.DocType;
import com.anyi.report.entity.DocUserPermission;
import com.anyi.report.entity.DynaMetadata;
import com.anyi.report.entity.DynaMetadata2;
import com.anyi.report.entity.InpatientInfo;
import com.anyi.report.entity.PatientDefaultTemplate;
import com.anyi.report.entity.PrintInfoRecord;
import com.anyi.report.entity.PrintModelCheckBox;
import com.anyi.report.entity.PrintModelCheckBoxSon;
import com.anyi.report.entity.PrintModelCheckBoxText;
import com.anyi.report.entity.PrintModelItem;
import com.anyi.report.entity.PrintModelMain;
import com.anyi.report.entity.RecordDataItem;
import com.anyi.report.entity.RecordDataItemList;
import com.anyi.report.entity.RecordDataList;
import com.anyi.report.entity.RecordDataListDetail;
import com.anyi.report.entity.RefModel;
import com.anyi.report.entity.TemplateCache;
import com.anyi.report.json.BaseListVo;
import com.anyi.report.json.BaseMapVo;
import com.anyi.report.json.ResultCst;
import com.anyi.report.service.DocReportService;
import com.anyi.report.util.DocPrintUtil;
import com.anyi.report.util.PropertiesUtils;
import com.anyi.report.util.ReportUtil;
import com.anyi.report.vo.LeftTreeVo;
import com.anyi.report.vo.TemplateVo;
import com.anyi.report.vo.TimeTreeVo;
import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.bodysign.BodySignConstants;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecordVo;
import com.lachesis.mnis.core.bodysign.entity.SpecialVo;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.event.MnisThreadPoolTaskExecutor;
import com.lachesis.mnis.core.event.entity.DocReportEventEntity;
import com.lachesis.mnis.core.event.entity.DocReportEventItemEntity;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.util.CodeUtils;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.core.util.SuperCacheUtil;

@Service("docReportService")
public class DocReportServiceImpl implements DocReportService{
	//日志输出
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DocReportServiceImpl.class);
	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private ActionMapper actionMapper;

	@Autowired
	MnisThreadPoolTaskExecutor mnisThreadPoolTaskExecutor;

	@Autowired
	private PatientService patientService;
	
	@Autowired
	private BodySignService bodySignService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private IdentityService identityService;

	private static String ISO88591_ENCODE = "ISO8859_1";
	private static String UTF8_ENCODE = "UTF-8";
	private static String GBK_ENCODE = "GBK";
	
	private String oFileName="";//模板ID(定义为全局变量方便处理模板中图片的名字，两者一致才好管理)

	@Transactional(value = "transactionManager")
	@Override
	public void addTemplate(String fileName, MultipartFile temFile,String dept_code,String fName,String doc_type,
							String report_type,String templateId, int showOrder, boolean bDefaultTempl) {
		try {
			InputStream input = temFile.getInputStream();
			JasperReport report = (JasperReport) JRLoader.loadObject(input);

			oFileName = new Date().getTime()+"";
			DocTemplate template = parseReport(report);
			input.close();
			template.setDeptCode(dept_code);
			template.setTemplateName("201503050213");
			template.setDocType(doc_type);
			template.setReport_type(report_type);
			template.setMemo("test");
			template.setShowIndex(0);
			template.setValid("0");
			template.setShowIndex(showOrder);
			boolean bReplace = false;
			//如果有模板id则表示更新模板。先删除原有的，再插入新的
			if(templateId!=null&&templateId.trim().length()>0){
				oFileName=templateId;
				template.setTemplateId(templateId);
				reportMapper.delete_template_item(template);
				reportMapper.delete_template_band(template);
				reportMapper.delete_template(template);

				bReplace = true;
			}
			System.out.println("oFileName:"+oFileName);
			template.setTemplateId(oFileName);
			template.setTemplateShowName(fName);
			template.setCreateTime(new Date());

			//复制文件
			Resource resource = applicationContext.getResource("/WEB-INF/jasper/");
			String filePath = resource.getFile().getPath()+"/"+ oFileName+".jasper";
			template.setTemplate_file_name(filePath);
			temFile.transferTo(new File(filePath));


			/*Resource resource = context.getResource("/WEB-INF/jasper/spring_report.jasper");
			FileOutputStream fout = new FileOutputStream(resource.getFile());
			fout.write(out.toByteArray());
			fout.close();

			out.close();
			input2.close();*/

			reportMapper.addTemplate(template);
			//对于护理记录单类型文书，需要同时插入一个对应的打印模板
			if(!bReplace && !StringUtils.isEmpty(template.getTemplateId())
					&& "1".equals(template.getReport_type())) {
				createPrintTemplate(template);
			}
			//设置默认护理记录单
			if(bDefaultTempl && !StringUtils.isEmpty(template.getTemplateId())
					&& !StringUtils.isEmpty(template.getDeptCode())) {
				setPatientDefaultTemplate(template.getDeptCode(), template.getTemplateId(), template.getDeptCode());
			}

			List<DocBand> docBands = template.getBands();
			int index = 0;
			for (Iterator iterator = docBands.iterator(); iterator.hasNext();) {
				DocBand docBand = (DocBand) iterator.next();
				docBand.setParentTemplateId(oFileName);
				docBand.setBandId(oFileName + docBand.getBandName());
				reportMapper.addTemplateBand(docBand);
				List<DocTemplateItem> items = docBand.getItems();
				for (Iterator iterator2 = items.iterator(); iterator2.hasNext();) {
					DocTemplateItem docTemplateItem = (DocTemplateItem) iterator2
							.next();
//					if ("Static".equals(docTemplateItem.getType())) {
//						docTemplateItem.setItemId(docBand.getBandId() + index);
//					} else {
//						docTemplateItem.setItemId(docBand.getBandId()
//								+ docTemplateItem.getItem_name());
//					}
					docTemplateItem.setItemId(new Date().getTime()+""+index);
					docTemplateItem.setParentBandId(docBand.getBandId());
					docTemplateItem.setIndex(index);
					reportMapper.addTemplateItem(docTemplateItem);
					index++;
				}
			}

			//如果是替换已经存在的模板，则将该模板清除出缓存
			if(bReplace) {
				TemplateCache.clearTemplateInMap(templateId);
				TemplateCache.clearMetadataInMap(templateId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("添加模版失败");
		}
	}

	/**
	 * 创建护理记录单对应的打印模板
	 * @param template
	 */
	private void createPrintTemplate(DocTemplate template){
		DocTemplate printTempl = new DocTemplate();
		printTempl.setTemplateId("print_"+template.getTemplateId());
		printTempl.setTemplateName(printTempl.getTemplateId());
		printTempl.setDeptCode(template.getDeptCode());
		printTempl.setTemplateShowName(template.getTemplateShowName()+"打印");
		printTempl.setShowIndex(template.getShowIndex()+1);
		printTempl.setDocType(template.getDocType());
		printTempl.setOrientation(template.getOrientation());
		printTempl.setValid("0");
		printTempl.setCreateTime(new Date());
		printTempl.setTemplate_file_name(template.getTemplate_file_name());
		reportMapper.addTemplate(printTempl);
	}

	private String getTemplateIDByPrintID(String printTemplateID){
		if(StringUtils.isEmpty(printTemplateID) || !printTemplateID.contains("print")){
			return null;
		}
		//打印护理记录单的id格式为: print_xxxx(对应模板ID)
		String[] strTempID = printTemplateID.split("_");
		if(2>strTempID.length){
			//打印模板ID的格式不对
			return null;
		}else{
			return strTempID[1];
		}
	}

	/**
	 * 获取模板节点信息和病人基本信息
	 */
	public List<DocTemplate> getTemplateList(DocTemplate template,String inpatient_no,boolean isHistory) {
		List<DocTemplate> list=null;
		//护理文书的打印要单独做
		if(template.getTemplateId().contains("print")) {
			//打印护理记录单的id格式为: print_xxxx(对应模板ID)
			String[] strTempID = template.getTemplateId().split("_");
			if(2>strTempID.length){
				//打印模板ID的格式不对
				return list;
			}else{
				String template_id = strTempID[1];
				template.setTemplateId(template_id);
			}
		}
		DocTemplate docTemplate = getTemplateByID(template.getTemplateId());
		if(null != docTemplate) {
			list = new ArrayList<DocTemplate>();
			list.add(docTemplate);
		}
		if(inpatient_no==null||inpatient_no.trim().length()==0){//如果为空，则表示后上传界面查看已上传的模板
			return list;
		}
		
		List <DocTemplate>  list_return=new ArrayList<DocTemplate>();
		//以下是为了将病人基本信息放到模板的节点里面
		if(list!=null&&list.size()>0){
			DocTemplate t=list.get(0);

			//获取病人的基本信息
			t.setInpatient_info(getBaseInfo(inpatient_no));
			//前端时间不准确，从后台传递过去
			t.setNow_time(DateUtil.format(new Date(), com.lachesis.mnis.core.util.DateUtil.DateFormat.FULL));
			
			//处理需要反填的信息
			t.setRef_list(dealRef(list, inpatient_no));
			list_return.add(t);
		}
		return list_return;
	}

	/**
	 * 获取病人的基本信息
	 * 这个地方，既然信息是以 key-value 形式保存的，为啥不做成map呢？这样后续取值也会方便很多啊？
	 * @param inpatient_no
	 * @return
	 */
	public List<InpatientInfo> getBaseInfo(String inpatient_no){
		List<InpatientInfo> info=new ArrayList<InpatientInfo>();
		InpatientInfo info1=new InpatientInfo();
		Patient p = patientService.getPatientByPatId(inpatient_no);

		info1.setKey(DocReportConstants.PAT_NAME);//姓名
		info1.setValue(p.getName());
		info.add(info1);

		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.DEPT_CODE);//科室编号
		info1.setValue(p.getDeptCode());
		info.add(info1);

		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.DEPT_NAME);//科室
		info1.setValue(p.getDeptName());
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.BED_NO);//床号
		info1.setValue(p.getBedCode());
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.INPAT_ID);//住院号
		info1.setValue(p.getInHospNo());
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.PAT_GENDER);//性别
		info1.setValue(p.getGender().getDisplayName());
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.MARITAL_STATUS);//婚姻状况
		info1.setValue("");
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.PAT_PROFESSION);//职业
		info1.setValue("");
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.EDUCATION_LEVEL);//文化程度
		info1.setValue("");
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.PAT_BIRTHPLACE);//籍贯
		info1.setValue("");
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.PAT_AGE);//年龄
		info1.setValue(p.getAge());
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.PAT_RELIGION);//宗教
		info1.setValue("");
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.PAYMENT_TYPE);//付费方式
		info1.setValue(p.getChargeTypeName());
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.DATA_SOURCE);//资料来源
		info1.setValue("");
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.CARE_GIVER);//日常照顾者
		info1.setValue("");
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.ADMISSION_TIME);//入院时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		info1.setValue(sdf.format(p.getInDate()).substring(0, 16));//精确到分
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.NURSING_GRADE);//护理等级
		if(4==p.getTendLevel()){//特级护理
			info1.setValue(DocReportConstants.NURSING_GRADE_SPECIAL);//特级护理
		}else if(1==p.getTendLevel()){
			info1.setValue(DocReportConstants.NURSING_GRADE_1);//一级护理
		}else if(2==p.getTendLevel()){
			info1.setValue(DocReportConstants.NURSING_GRADE_2);//二级护理
		}else if(3==p.getTendLevel()){
			info1.setValue(DocReportConstants.NURSING_GRADE_3);//三级护理
		}
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.ADMISSION_FORM);//入院方式
		info1.setValue("");
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.ALLERGY_HISTORY);//过敏史
		info1.setValue(p.getAllergen());
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.PAT_DIAGNOSE);//入院诊断
		//三院要求：诊断信息如果有多条，只显示前三条；
		info1.setValue(getPartOfDiagnoseInfo(p.getInDiag(), DocReportConstants.DIAGNOSE_NUM));
		info.add(info1);
		
		info1=new InpatientInfo();
		info1.setKey(DocReportConstants.PAT_COMPLAINT);//主诉
		info1.setValue(p.getAppeal());
		info.add(info1);
		
		return info;
	}
	
	/**
	 * 处理需要反填的数据
	 * @param list
	 * @param inpatient_no
	 * @return
	 */
	public List<com.anyi.report.vo.SpecialVo> dealRef(List<DocTemplate> list,String inpatient_no){
		List<com.anyi.report.vo.SpecialVo> list_return=null;
		RefModel refMode=null;//需要反填的数据
		boolean flag=false;//生命体征标识
		if(list!=null&&list.size()>0){
			DocTemplate t=list.get(0);
			for(int i=0;i<t.getBands().size();i++)
			{
				DocBand bands=t.getBands().get(i);
				if(bands!=null)
				{
					List<DocTemplateItem> items=bands.getItems();
					if(items!=null&&items.size()>0){
						List list_temp=new ArrayList();
						for(int m=0,n=items.size();m<n;m++)
						{
							DocTemplateItem item=items.get(m);
							//数据需要反填,在数据源中配置if_rel为Y
							if("Y".equals(item.getIf_rel())){
								if(refMode==null){
									refMode=new RefModel();
								}
								refMode.setInpatient_no(inpatient_no);
								list_temp.add(item.getItem_name());
							}
							//生命体征，下面为生命体征字段（dic_body_sign表中内容）
							if(BodySignConstants.ABDOMINAL_CIR.equals(item.getItem_name())//胸围
									||BodySignConstants.BLOOD_GLU.equals(item.getItem_name())//指测血糖
									||BodySignConstants.BLOODPRESS.equals(item.getItem_name())//血压
									||BodySignConstants.BREATH.equals(item.getItem_name())//呼吸
									||BodySignConstants.HEARTRATE.equals(item.getItem_name())//心率
									||BodySignConstants.HEIGHT.equals(item.getItem_name())//身高
									||BodySignConstants.INTAKE.equals(item.getItem_name())//入量
									||BodySignConstants.PULSE.equals(item.getItem_name())//脉搏
									||BodySignConstants.TEMPERATURE.equals(item.getItem_name())//体温
									||BodySignConstants.URINE.equals(item.getItem_name())//尿量
									||BodySignConstants.WEIGHT.equals(item.getItem_name())//体重
							)
							{
								flag=true;//生命体征标识
							}
							
						}
						if(refMode!=null){
							refMode.setCode_list(list_temp);
						}
					}
				}
				
			}
		}
		//有需要反填的数据
		if(refMode!=null){
			list_return=reportMapper.ref_list(refMode);
		}
		//生命体征
		if(flag){
			List<SpecialVo> specList =bodySignService.getFirstBodySigns(inpatient_no);
			if(specList!=null&&specList.size()>0){
				if(list_return==null) list_return=new ArrayList();
				for(int i=0;i<specList.size();i++)
				{
					SpecialVo s=specList.get(i);
					com.anyi.report.vo.SpecialVo v=new com.anyi.report.vo.SpecialVo();
					v.setRecord_key(s.getRecord_key());
					v.setRecord_value(s.getRecord_value());
					list_return.add(v);
				}
			}
		}
		return list_return;
	}
	
	
	/**
	 * 保存数据
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public HashMap<String, Object> saveData(DocRecord record,boolean isNurseRecord,String deptCode, boolean isCopy) {
		HashMap<String, Object> maps = new HashMap<String, Object>();
		// 数据校验
		if (record == null) {
			throw new MnisException("记录信息为空！");
		}else if(record.getTemplate_id().contains("print")){
			//有时候居然会把打印模板传过来？
			throw new MnisException("模板信息错误。");
		}
		String date_list = record.getDate_list();// 日期
		String time_list = record.getTime_list();// 时间
		String create_person = "";// 创建人，保存到主表中（审核时审签人跟创建人不能一样，在主表方便前台比较）

		// 审核人，有时前端会把审核人也传过来，导致责任护士和审核人为同一个人，特别加上判断
		String approve_person = "";
		String in_date = StringUtils.EMPTY;

		boolean haveInOut = false;//是否存在出入量
		// 以下代码主要是保存护理记录单中的日期和时间，存在主表中，方便菜单展示和排序。也是为了防止性能出问题
		List<RecordDataList> dataLists = record.getData_list();
		if (dataLists != null && dataLists.size() > 0
				&& dataLists.get(0).getList_detail().size() > 2) {
			RecordDataList dl = dataLists.get(0);
			List<RecordDataListDetail> detail = dl.getList_detail();
			if (detail != null && detail.size() > 0) {
				for (int i = 0; i < detail.size(); i++) {
					if ("DATE_LIST".equals(detail.get(i).getData_key())) {
						date_list = detail.get(i).getData_value();
					}
					if ("TIME_LIST".equals(detail.get(i).getData_key())) {
						time_list = detail.get(i).getData_value();
					}
					if ("CREATE_PERSON".equals(detail.get(i).getData_key())) {
						create_person = detail.get(i).getData_value();
					}
					if ("APPROVE_PERSON".equals(detail.get(i).getData_key())) {
						approve_person = detail.get(i).getData_value();
					}
					
					if ("BE_HOSPITALIZED_TIME".equals(detail.get(i).getData_key())) {
						in_date = detail.get(i).getData_value();
					}
					//判断是否需要统计出入量
					if(!StringUtils.isEmpty(detail.get(i).getData_value())
						&& (ReportConstants.IN_TAKE.equals(detail.get(i).getData_key())
							|| ReportConstants.OUT_TAKE.equals(detail.get(i).getData_key()))){
						haveInOut = true;
					}
				}
			}
		} else if (record.getData_item() != null
				&& record.getData_item().size() > 0) {
			List<RecordDataItem> data_item = record.getData_item();
			if (data_item != null && data_item.size() > 0) {
				for (int i = 0, l = data_item.size(); i < l; i++) {
					RecordDataItem temp = data_item.get(i);
					if ("CREATE_PERSON".equals(temp.getTemplate_item_id())) {
						create_person = temp.getRecord_value();
					}
					if ("APPROVE_PERSON".equals(temp.getTemplate_item_id())) {
						approve_person = temp.getRecord_value();
					}
					//判断是否需要统计出入量
					if(!StringUtils.isEmpty(temp.getRecord_value())
						&& (ReportConstants.IN_TAKE.equals(temp.getTemplate_item_id())
							|| ReportConstants.OUT_TAKE.equals(temp.getTemplate_item_id()))){
						haveInOut = true;
					}
				}
			}
		}
		if(!StringUtils.isEmpty(create_person)){
			//责任护士和审核护士校验
			String[] str = create_person.split("-");
			if (str == null || str.length != 2) {
				throw new MnisException("责任护士格式不对");
			}
			// 责任护士的格式为 000238-段钢。不知道为什么系统中经常出现000238-000238这样的数据，直接拒绝进入系统
			if (!ReportUtil.isChineseChar(str[1])) {
				throw new MnisException("责任护士格式不对");
			}
			if (create_person.equals(approve_person)) {
				throw new MnisException("责任护士和审核人，不能为同一个人");
			}
		}else {
			throw new MnisException("责任护士为空。");
		}

		record.setCreate_person(create_person);//创建人
		record.setDate_list(date_list);//日期
		record.setTime_list(time_list);//时间

		//先判断是否有操作权限，再进行后续处理
		if("1".equals(SuperCacheUtil.getSystemConfig("IS_CHECK"))){
			checkPermissionBeforRecordOperation(record);
		}

		//对于表头数据，需要进行数据过滤，以免掺杂记录数据
		if(MnisConstants.IS_HEADER.equals(record.getIsHeader())){
			preprocessHeaderRecord(record);
		}

		//1.判断是否为护理记录单,且判断是否为24h医嘱,历史出入量保存
		DocRecord oldRecordInfo = null;
		List<DocDataRecordItem> docDataRecordItems = new ArrayList<DocDataRecordItem>();
		if(hasInOutOrderForPatient(record.getInpatient_no())
				&& !MnisConstants.IS_HEADER.equals(record.getIsHeader())){
			docDataRecordItems = reportMapper.getDocDataRecordItems(record.getRecordId());
			//NOTE:对于编辑文书记录的情况，如果编辑前存在出入量信息，编辑后去除了出入量信息
			//这种情况也应该认为改变了出入量，需要更新对应的出入量统计信息
			if(!haveInOut){
				for (DocDataRecordItem item : docDataRecordItems) {
					if(!StringUtils.isEmpty(item.getRecord_value())
							&&(DocReportConstants.IN_TAKE.equals(item.getTemplate_item_id())
								|| DocReportConstants.OUT_TAKE.equals(item.getTemplate_item_id()))){
						haveInOut = true;
						break;
					}
				}
			}
			//获取变更前的记录信息，以确认是否改变了日期和时间信息
			oldRecordInfo = reportMapper.getDocRecordByID(record.getRecordId());
		}
		
		// 如果record不为空则更新。为空新增
		if (StringUtils.isNotBlank(record.getRecordId())) {// 修改
			record.setModify_time(new Date());
			reportMapper.updateRecord(record);// 更新更改时间
		} else {// 新增数据
			record.setCreateTime(new Date());
			record.setRecordId(CodeUtils.getSysInvokeId());
			reportMapper.saveRecord(record);
		}
		reportMapper.delete_data_item(record.getRecordId());

		/*
		 * 对于文书中有些信息，例如病人信息，付费方式、入院诊断、主诉等，无论其值是否为空，文书记录中都要保存这些字段
		 * 因此，需要先把模板中的这些必要保存字段获取出来，合并到对应的文书记录当中一起保存。 by renjunming, 2016-04-16
		 */
		preprocessRecordWithSaverequiredItem(record);
		
		// 有动态列表的数据使用单独的格式进行传递,单独保存
		List<RecordDataListDetail> dyna_list = record.getDyna_list();
		if (dyna_list != null) {
			for (RecordDataListDetail model : dyna_list) {
				RecordDataItem bean = new RecordDataItem();
				bean.setRecord_id(record.getRecordId());//记录住ID
				bean.setTemplate_item_id(model.getData_key());//key
				bean.setRecord_value(model.getData_value());//值
				bean.setParent_id("0");
				saveRecordItem(bean);
			}
		}
		//PC端数据
		if (record.getData_item() != null) {
			for (RecordDataItem bean : record.getData_item()) {
				// 添加行
				bean.setRecord_id(record.getRecordId());
				saveRecordItem(bean);

				if (bean.getList_item() != null) {// 对有复选款多个输入值，每个都需保存
					String parent_l = bean.getRecord_item_id();
					for (RecordDataItem rl : bean.getList_item()) {
						RecordDataItem item = new RecordDataItem();
						
						item.setTemplate_item_id(rl.getTemplate_item_id());// 项目KEY
						item.setRecord_value(rl.getRecord_value());// 项目值
						item.setParent_id(parent_l);
						// 添加行
						item.setRecord_id(record.getRecordId());
						saveRecordItem(item);

						// 对于有子复选框或多个输入框的情况，要每条都录入。
						if (rl.getList_item() != null) {
							// 保存复选框的值并返回该记录的id，用作子节点的父ID
							String parent_id = item.getRecord_item_id();// 记录ID
							for (RecordDataItem sonItem : rl.getList_item()) {
								RecordDataItem item_son = new RecordDataItem();
								item_son.setTemplate_item_id(sonItem.getTemplate_item_id());// 记录的KEY
								item_son.setRecord_value(sonItem.getRecord_value());// 记录的值
								item_son.setParent_id(parent_id);// 父记录的id
								// 添加行
								item_son.setRecord_id(record.getRecordId());
								saveRecordItem(item_son);
							}
						}
					}
				}
			}
		}

		// 以下代码是设计时错误，pc ,nda不愿再修改代码，只好在这里强行做转换
		if (null != record.getData_list()) {
			for (RecordDataList bean : record.getData_list()) {
				List<RecordDataListDetail> list_detail = bean.getList_detail();
				if (list_detail != null) {
					for (RecordDataListDetail detail : list_detail) {
						RecordDataItem item = new RecordDataItem();
						item.setRecord_id(record.getRecordId());
						// 循环列表中的日期和时间，必须填写，否则树形列表会出错
						if ("DATE_LIST".equals(detail.getData_key())
								|| "TIME_LIST".equals(detail.getData_key())) {
							if (StringUtils.isEmpty(detail.getData_value())) {
								throw new MnisException("日期或时间不能为空!");
							}
						}
						item.setTemplate_item_id(detail.getData_key());
						item.setRecord_value(detail.getData_value());
						item.setParent_id("0");
						// 添加行
						item.setRecord_id(record.getRecordId());
						saveRecordItem(item);
					}
				}
			}
		}
		
		// 数据返回
		maps.put(ReportConstants.RECORD_ID, record.getRecordId());
		
		if(haveInOut && hasInOutOrderForPatient(record.getInpatient_no())
				&& !MnisConstants.IS_HEADER.equals(record.getIsHeader())){
			maps.put(ReportConstants.IS_STATISTIC, true);
			
			//2.处理统计量信息
			processDocReportStatistic(record,deptCode,record.getTemplate_id(),date_list,time_list,docDataRecordItems);
			
			if(StringUtils.isBlank(in_date)){
				//出院日期
				Patient patient = patientService.getPatientByPatId(record.getInpatient_no());
				in_date = DateUtil.format(patient.getInDate(), com.lachesis.mnis.core.util.DateUtil.DateFormat.YMDHM);
			}
			
			String[] times = getStatisticTiems(time_list);
			maps.put(ReportConstants.TIME_LIST, times[0]);
			maps.put("docStatistic", showDocRecordDataStatistic(
					getDocReprotDataStatistic(record.getInpatient_no(), 
					deptCode, record.getTemplate_id(),date_list, times[0]),times[1],times[2],in_date));
		}

		//如果不是表头数据，进行后续处理
		if(!MnisConstants.IS_HEADER.equals(record.getIsHeader())){
			//1、异步处理打印信息：计算行高和页码
			try {
				dealPrintInfoForRecordAndStatictics(record, (DocRecordDataStatistic)maps.get("docStatistic"));
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e.toString(),"");
			}
			//2、更新文书数据到生命体征
			if(!isCopy && isReportTypeHLJL(record.getTemplate_id())){
				//转抄的文书记录不用更新, 非护记不用更新
				try {
					//体征数据
					updatedBodySignToTempratureSheet(record);
					if(null!=oldRecordInfo && (!record.getDate_list().equals(oldRecordInfo.getDate_list())
						|| !record.getTime_list().equals(oldRecordInfo.getTime_list()))){
						updatedBodySignToTempratureSheet(oldRecordInfo);
					}
					//出入量数据
					if(haveInOut){
						updatedStatisticToTempratureSheet(record, oldRecordInfo,docDataRecordItems);
					}
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error(e.getMessage(),"");
				}
			}
		}

		//记录操作行为
		if(DocPrintConstants.bRecordAction){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("DocRecord", record);
			paramMap.put("boolean", isNurseRecord);
			paramMap.put("String", deptCode);
			String paramStr = GsonUtils.toJson(paramMap);
			recordAction(paramStr, record.getCreate_person());
		}

		return maps;
	}

	/**
	 * 对文书数据的表头数据进行预处理，过滤掉不相干的数据
	 * @param record 表头记录
     */
    private void preprocessHeaderRecord(final DocRecord record){
		if (record == null) {
			return;
		}
		//获取模板中的表头组件信息
		Map<String, DocTemplateItem> headerItems = getItemsInBandOfListTemplate(record.getTemplate_id(),
														DocReportConstants.TEMPLATE_BAND_NAME_DETAIL);
		//数据记录中过滤掉不相干数据
		//由于表头数据除了page-hader部分，还可能包含page-foot部分，因此主要过滤detail中的数据
		List<RecordDataList> dataList = record.getData_list();
		if (dataList != null) {
			for (int i = 0; i < dataList.size(); i++) {
				if (dataList.get(i) == null) {
					continue;
				}
				String itemId = dataList.get(i).getRecord_item_id();
				if(null==itemId || (headerItems.containsKey(itemId)
						&& !DocReportConstants.DATE_LIST.equals(itemId)
						&& !DocReportConstants.TIME_LIST.equals(itemId)
						&& !DocReportConstants.CREATE_PERSON.equals(itemId))){
					dataList.remove(i--);
				}
			}
		}
		List<RecordDataItem> itemList = record.getData_item();
		if (itemList != null) {
			for (int j = 0; j < itemList.size(); j++) {
				if (itemList.get(j) == null) {
					continue;
				}
				String itemId = itemList.get(j).getTemplate_item_id();
				if(null==itemId || (headerItems.containsKey(itemId)
						&& !DocReportConstants.DATE_LIST.equals(itemId)
						&& !DocReportConstants.TIME_LIST.equals(itemId)
						&& !DocReportConstants.CREATE_PERSON.equals(itemId))){
					itemList.remove(j--);
				}
			}
		}

		record.setDyna_list(null);
		record.setSpecList(null);
		record.setData_item_list(null);
	}

	/**
	 * 计算数据的行高和页码
	 * @param docRecord
	 * @param statisticRecord
	 */
	private void dealPrintInfoForRecordAndStatictics(DocRecord docRecord, DocRecordDataStatistic statisticRecord) throws Exception{
		if(null == docRecord){
			return;
		}
		boolean hasInout = true;
		int pageNo = 0;
		if (null == statisticRecord || null == statisticRecord.getDocRecordStatistics()
				|| null == statisticRecord.getDocRecordStatistics().get(0)) {
			hasInout = false;
		} else {
			pageNo = statisticRecord.getDocRecordStatistics().get(0).getPageNo();
		}
		final boolean hasStatistic = hasInout;//是否存在出入量统计数据
		final int statisticPageNo = 0 < pageNo ? pageNo : 0;
		final DocRecord record = docRecord;
		mnisThreadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				//对于出入量数据，如果原来有页码，就不用重算，如果原来没页码，则需要重算页码，以免影响到后续文书记录的页码更新
				if (hasStatistic && 0 >= statisticPageNo) {
					DocRecord statisticRecord = new DocRecord();
					statisticRecord.setRecordId("InOutStatistics");
					statisticRecord.setInpatient_no(record.getInpatient_no());
					statisticRecord.setTemplate_id(record.getTemplate_id());
					statisticRecord.setDate_list(record.getDate_list());
					statisticRecord.setTime_list(record.getTime_list());
					statisticRecord.setRowHight(DocPrintUtil.getOneRowHightForDocPrint());
					statisticRecord.setPageNo(0);
					updatePageNoForDocRecord(statisticRecord);
				}

				int rowHight = record.getRowHight();
				int newHight = countRowHightForRecord(record);
				if (hasStatistic || rowHight != newHight) {
					//如果记录中包含有出入量信息，考虑到累计值得变化，需要重新计算相关记录的页码
					updatePageNoForDocRecord(record);
				}
			}
		});
	}
	
	
	/**
	 * 处理统计
	 * 
	 * @param patId
	 * @param deptCode
	 * @param dateList
	 * @param timeList
	 * @param detiationValues
	 */
	private void processDocReportStatistic(DocRecord record, String deptCode,String templateID,
			String dateList, String timeList,
			List<DocDataRecordItem> docDataRecordItems) {
		// 2.获取最新的出入量
		List<DocDataRecordItem> newDocDataRecordItems = reportMapper
				.getDocDataRecordItems(record.getRecordId());
		
		//1.历史数据删除,2.新数据加入
		deleteDocReportStatistic(docDataRecordItems, record.getInpatient_no(),deptCode,templateID);
		
		
		//新数据加入
		String[] newValues = getStaticValues(newDocDataRecordItems);
		// 4.根据当天时间判断区间
		String[] props = getStatisticProp();
		int region = ReportUtil.caclRegion(timeList, props[0], props[1]);
		// 保存日期
		String saveDateList = getStaticDate(region, dateList);
		// 保存上午信息
		saveDocReportStatic(record.getInpatient_no(), deptCode, templateID, saveDateList,
				props[0], newValues);
		if (1 == region) {
			saveDocReportStatic(record.getInpatient_no(), deptCode, templateID, dateList,
					props[1], newValues);
		}

	}
	
	/**
	 * 保存统计信息
	 * @param patId
	 * @param deptCode
	 * @param dateList
	 * @param timeList
	 * @param detiationValues
	 */
	private void saveDocReportStatic(String patId,String deptCode,String templateID, String dateList,String timeList
			,String[] detiationValues){
		// 4.根据相关区间和患者信息获取统计信息
		//入量
		DocRecordStatistic inDocRecordStatistic = reportMapper
				.getDocReprotStatistic(patId, deptCode, dateList, timeList, ReportConstants.IN_TAKE, null, templateID);
		
		//出量
		DocRecordStatistic outDocRecordStatistic = reportMapper
				.getDocReprotStatistic(patId, deptCode, dateList, timeList, ReportConstants.OUT_TAKE, detiationValues[2],templateID);
		
		
		if( null == inDocRecordStatistic){
			addDocReportStatic(patId, deptCode, dateList, timeList, ReportConstants.IN_TAKE, detiationValues[0], true,templateID);
		}else{
			inDocRecordStatistic.setStaticValue(caluStaticValue(detiationValues[0],inDocRecordStatistic.getStaticValue(),true));
			if(inDocRecordStatistic.getCreateDate() == null){
				inDocRecordStatistic.setCreateDate(new Date());
			}
			reportMapper.updateDocReprotStatistic(inDocRecordStatistic);
			
		}
		
		if(null == outDocRecordStatistic){
			//新增
			addDocReportStatic(patId, deptCode, dateList, timeList, detiationValues[2], detiationValues[1], false,templateID);
		}else{
			//修改
			if(outDocRecordStatistic.getCreateDate() == null){
				outDocRecordStatistic.setCreateDate(new Date());
			}
			outDocRecordStatistic.setStaticValue(caluStaticValue(detiationValues[1],outDocRecordStatistic.getStaticValue(),true));
			reportMapper.updateDocReprotStatistic(outDocRecordStatistic);
		}
	}
	
	

	/**
	 * 数据库不存在,直接新增统计
	 * 
	 * @param record
	 * @param deptCode
	 * @param dateList
	 * @param time
	 * @param statisticType
	 */
	private void addDocReportStatic(String patId, String deptCode,
			String dateList, String time, String takeType, String takeValue,
			boolean isInTake,String templateID) {
		if(StringUtils.isBlank(takeValue) || ReportConstants.ZERO.equals(takeValue)){
			return;
		}
		// 包装新增
		DocRecordStatistic docRecordStatistic = new DocRecordStatistic();
		docRecordStatistic.setCreateDate(new Date());
		docRecordStatistic.setDeptCode(deptCode);
		docRecordStatistic.setPatId(patId);
		docRecordStatistic.setStaticTime(time);
		docRecordStatistic.setStaticDate(dateList);
		docRecordStatistic.setTemplateID(templateID);
		if (StringUtils.isBlank(takeValue)) {
			takeValue = ReportConstants.ZERO;
		}
		docRecordStatistic.setStaticValue(takeValue);
		docRecordStatistic.setStaticType(isInTake ? ReportConstants.IN_TAKE : ReportConstants.OUT_TAKE);
		docRecordStatistic.setStaticItemType(takeType);
		reportMapper.insertDocReprotStatistic(docRecordStatistic);
	}

	/**
	 * 处理统计删除业务
	 * @param docRecord
	 * @param deptCode
	 * @return
	 */
	private DocRecordDataStatistic processDeleteDocReportStatistic(DocRecord docRecord,String deptCode){
		if(null == docRecord){
			return null;
		}
		//处理已删除的信息
		List<DocDataRecordItem> docDataRecordItems= reportMapper.getDocDataRecordItems(docRecord.getRecordId());
		
		return deleteDocReportStatistic(docDataRecordItems,docRecord.getInpatient_no(),deptCode, docRecord.getTemplate_id());
	}
	
	
	/**
	 * 处理删除时文书统计
	 * @param docRecord
	 * @param deptCode
	 */
	private DocRecordDataStatistic deleteDocReportStatistic(List<DocDataRecordItem> docDataRecordItems,String patId,
															String deptCode, String templateID){
		
		String[] oldValues = getStaticValues(docDataRecordItems);
		if(StringUtils.isBlank(oldValues[0]) && StringUtils.isBlank(oldValues[1])){
			return null;
		}
	
		String[] props = getStatisticProp();
		//判断时间区间
		int region = ReportUtil.caclRegion(oldValues[4], props[0], props[1]);
		
		//保存日期
		String saveDateList = getStaticDate(region, oldValues[3]);
		
		deleteStatic(patId, deptCode, templateID, saveDateList, props[0], oldValues[0],
				oldValues[1],oldValues[2]);
		
		String staticTimeList = props[0];
		
		//7-15区间保存
		if( 1== region){
			staticTimeList = props[1];
			deleteStatic(patId, deptCode, templateID,oldValues[3], props[1], oldValues[0],
					oldValues[1],oldValues[2]);
		}
		
		DocRecordDataStatistic docRecordDataStatisticNew = showDocRecordDataStatistic(
				getDocReprotDataStatistic(patId,
						deptCode, templateID,oldValues[3], staticTimeList), props[0], props[1], oldValues[5]);
		return docRecordDataStatisticNew;
	}
	
	/**
	 * 修改统计信息
	 * @param patId
	 * @param deptCode
	 * @param saveDateList
	 * @param timeList
	 * @param inValue
	 * @param outValue
	 * @param outType
	 */
	private void deleteStatic(String patId,String deptCode,String templateID,String saveDateList,
			String timeList,String inValue,String outValue,String outType){
		DocRecordStatistic inDocRecordStatistic = reportMapper
				.getDocReprotStatistic(patId, deptCode, saveDateList, timeList, ReportConstants.IN_TAKE, null,templateID);
		
		DocRecordStatistic outDcRecordStatistic = reportMapper
				.getDocReprotStatistic(patId, deptCode, saveDateList, timeList, ReportConstants.OUT_TAKE, outType, templateID);
		//入量
		if(inDocRecordStatistic != null){
			inDocRecordStatistic.setStaticValue(
					caluStaticValue(inValue, inDocRecordStatistic.getStaticValue(), false));
			reportMapper.updateDocReprotStatistic(inDocRecordStatistic);
		}
		//出量
		if(outDcRecordStatistic != null){
			outDcRecordStatistic.setStaticValue(
					caluStaticValue(outValue, outDcRecordStatistic.getStaticValue(), false));
			reportMapper.updateDocReprotStatistic(outDcRecordStatistic);
		}
	}
	


		// 返回编码格式
		private String getEncode(String str) {
			String encode = null;
			if(str!=null
					&&!"".equals(str)){
				if (verifyEncode(str, GBK_ENCODE)) {
					encode = GBK_ENCODE;
				} else if (verifyEncode(str, ISO88591_ENCODE)) {
					encode = ISO88591_ENCODE;
				} else if (verifyEncode(str, UTF8_ENCODE)) {
					encode = UTF8_ENCODE;
				}

			}
			
			return encode;
		}

		// 判断编码格式是否相符
		private boolean verifyEncode(String str, String encode) {
			try {
				if (str.equals(new String(str.getBytes(encode), encode))) {
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				
			}
			return false;
		}
	
	/**
	 * 解析模板
	 * @param report
	 * @return
	 * @throws JRException
	 */
	private DocTemplate parseReport(JasperReport report) throws JRException {
		/*ByteArrayInputStream input = new ByteArrayInputStream(data);
		JasperReport report = (JasperReport) JRLoader.loadObject(input);*/

		DocTemplate tempalte = new DocTemplate();
		tempalte.setOrientation(report.getOrientationValue().getName());
		tempalte.setWidth(report.getPageWidth());
		tempalte.setHeight(report.getPageHeight());

		List<DocBand> bands = new ArrayList<DocBand>();
		tempalte.setBands(bands);
		int postion = 0;

		JRBand jrTitleBand = report.getTitle();
		DocBand titleBand = parseBand(jrTitleBand);
		if (titleBand != null) {
			titleBand.setBandName("title");
			bands.add(titleBand);
			titleBand.setPosY(postion);
			postion += titleBand.getHeight();
		}

		JRBand jrPageHeadBand = report.getPageHeader();
		DocBand pageHeadBand = parseBand(jrPageHeadBand);
		if (pageHeadBand != null) {
			pageHeadBand.setBandName("page-header");
			bands.add(pageHeadBand);
			pageHeadBand.setPosY(postion);
			postion += pageHeadBand.getHeight();
		}

		JRBand jrColumnHeadBand = report.getColumnHeader();
		DocBand cloumnHeadBand = parseBand(jrColumnHeadBand);
		if (cloumnHeadBand != null) {
			cloumnHeadBand.setBandName("cloumn-header");
			bands.add(cloumnHeadBand);
			cloumnHeadBand.setPosY(postion);
			postion += cloumnHeadBand.getHeight();
		}

		JRBand[] details = report.getDetailSection().getBands();
		for (int i = 0; i < details.length; i++) {
			JRBand jrDetailBand = details[i];
			DocBand detailBand = parseBand(jrDetailBand);
			if (detailBand != null) {
				detailBand.setBandName("detail");
				bands.add(detailBand);
				detailBand.setPosY(postion);
				postion += detailBand.getHeight();
			}
		}

		JRBand jrColumnFootBand = report.getColumnFooter();
		DocBand columnFootBand = parseBand(jrColumnFootBand);
		if (columnFootBand != null) {
			columnFootBand.setBandName("column-footer");
			bands.add(columnFootBand);
			columnFootBand.setPosY(postion);
			postion += columnFootBand.getHeight();
		}

		JRBand jrPageFootBand = report.getPageFooter();
		DocBand pageFootBand = parseBand(jrPageFootBand);
		if (pageFootBand != null) {
			pageFootBand.setBandName("page-footer");
			bands.add(pageFootBand);
			pageFootBand.setPosY(postion);
			postion += pageFootBand.getHeight();
		}

		JRBand jrLastPageFootBand = report.getLastPageFooter();
		DocBand lastPageFootBand = parseBand(jrLastPageFootBand);
		if (lastPageFootBand != null) {
			lastPageFootBand.setBandName("last-page-footer");
			bands.add(lastPageFootBand);
			lastPageFootBand.setPosY(postion);
			postion += lastPageFootBand.getHeight();
		}

		JRBand jrSummaryBand = report.getSummary();
		DocBand summaryBand = parseBand(jrSummaryBand);
		if (summaryBand != null) {
			summaryBand.setBandName("summary");
			bands.add(summaryBand);
			summaryBand.setPosY(postion);
			postion += summaryBand.getHeight();
		}

		return tempalte;
	}

	/**
	 * 解析模板
	 * @param jrBand
	 * @param oFileName
	 * @return
	 */
	private DocBand parseBand(JRBand jrBand) {

		if (jrBand == null) {
			return null;
		}

		JRElement[] child = jrBand.getElements();
		DocBand band = null;
		if (jrBand.getHeight() > 0) {
			band = new DocBand();
		} else {
			return null;
		}
		List<DocTemplateItem> docItems = new ArrayList<DocTemplateItem>();
		band.setItems(docItems);
		band.setHeight(jrBand.getHeight());

		// band.setBandName(jrBand.);

		for (int j = 0; j < child.length; j++) {
			DocTemplateItem item = new DocTemplateItem();
			JRElement e = child[j];
			if (e instanceof JRBaseTextField) {
				String fName = ((JRBaseTextField) e).getExpression().getText();
				String type = fName.substring(1, 2);
				String value = fName.substring(fName.indexOf("{") + 1,
						fName.indexOf("}"));
				item.setItemId("itemId" + j);
				item.setItem_name(value);
				
				item.setAlign(((JRBaseTextField) e).getHorizontalAlignmentValue().toString());
				item.setMargin(((JRBaseTextField) e).getVerticalAlignmentValue().toString());
				item.setSize(String.valueOf(((JRBaseTextField) e).getFontSize()));
				item.setIsbold(((JRBaseTextField) e).isBold()==true?"bold":"");
				if ("F".equals(type)) {
					item.setType("Field");
				} else if ("P".equals(type)) {
					boolean if_underline=((JRBaseTextField) e).isUnderline();
					if(if_underline){//在ireport画图时默认为false，而实际上默认需要下划线，特此转换
						item.setIf_underline("N");
					}
					item.setType("Param");
				}
			} else if (e instanceof JRBaseStaticText) {
				item.setItem_name(((JRBaseStaticText) e).getText());
				item.setItemId("itemId" + j);
				item.setType("Static");
	
				item.setAlign(((JRBaseStaticText) e).getHorizontalAlignmentValue().toString());
				item.setMargin(((JRBaseStaticText) e).getVerticalAlignmentValue().toString());
				item.setSize(String.valueOf(((JRBaseStaticText) e).getFontSize()));
				item.setIsbold(((JRBaseStaticText) e).isBold()==true?"bold":"");				
				
			}else if(e instanceof JRBaseComponentElement){
				if(((JRBaseComponentElement) e).getStyle().toString().startsWith("table")){//如果是一个table
					//((JRBaseComponentElement) e).g
				}
			}else if (e instanceof JRBaseLine) {
				item.setItemId("itemId" + j);
				item.setItem_name("line");
				item.setType("line");	
				item.setDirection(((JRBaseLine) e).getDirectionValue().toString());
				
			}else if (e instanceof JRBaseBreak) {
				item.setItemId("break" + j);
				item.setItem_name("break");
				item.setType("break");
			}else if(e instanceof JRBaseImage){
				item.setItemId("image"+j);
				item.setItem_name("image"+"_"+j);
				item.setType("image");
				String image_url=((JRBaseImage) e).getExpression().getText().replaceAll("\"", "");//图片的路径,ireport解析出来的图片路径带有双引号，需要去掉
				item.setImage_url(image_url);
				//image_url=image_url.replaceAll("\\\\", "/");
				
				//image_url=image_url.replaceAll("//", "/");
				
				try{
//					Resource resource = applicationContext.getResource("/resources/img/report/");
//					String filePath = resource.getFile().getPath()+"/"+oFileName+(image_url.substring(image_url.lastIndexOf("."), image_url.length())); 
//					item.setImage_url(filePath);
//					copyFile(image_url,filePath);//拷贝图片至web目录下
					
				}catch(Exception ee){
					ee.printStackTrace();
				}
			} else {
				continue;
			}
			item.setHeight(e.getHeight());
			item.setPosX(e.getX());
			item.setPosY(e.getY());
			item.setWidth(e.getWidth());
			docItems.add(item);
		}
		return band;
	}


	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.applicationContext = context;
	}

	/**
	 * 获取数据（用于编辑）
	 */
	public List<DocRecord> getTemplateData(DocRecord docRecord){
		List <DocRecord> list= reportMapper.getTemplateData(docRecord);
		
		/*List <DocRecord> list= new ArrayList<DocRecord>(); 
		docRecord.setInpatient_no("1");
		DocRecord doc1 =reportMapper.getTempDatas(docRecord);
		list.add(doc1);*/
		return list;
	}
	
	/**
	 * 后台打印处理数据
	 */
	public List<PrintModelMain> getDataForPrint(PrintModelMain pmm) {
		if(pmm==null||pmm.getInpatient_no()==null|pmm.getInpatient_no().trim().length()==0
				||pmm.getTemplate_id()==null||pmm.getTemplate_id().trim().length()==0
				){
			return null;
		}
		List <PrintModelMain> list=reportMapper.getDataForPrint(pmm);
		List <PrintModelMain> list_return=new ArrayList();
		if(list!=null&&list.size()>0){
			PrintModelMain pm=list.get(0);

			List <PrintModelItem> list_item=pm.getData_item();
			if(list_item!=null&&list_item.size()>0)
			{
				for(int i=0;i<list_item.size();i++)
				{
					PrintModelItem pmi=list_item.get(i);
					if(pmi==null) continue;
					String value=pmi.getValue();
					if(value==null)
					{
						value="";
					}
					//DYNA_TITLE为动态列表数据源的类型
					if(DocReportConstants.DATA_TYPE_OPT.equals(pmi.getData_type())
							||DocReportConstants.DATA_TYPE_DYNA_TITLE.equals(pmi.getData_type()))
					{//如果是下拉菜单，将显示下拉菜单的名字
						value=pmi.getOption_name();
					}
					//复选框，包括单选和多选
					if(DocReportConstants.DATA_TYPE_MSEL.equals(pmi.getData_type())
							||DocReportConstants.DATA_TYPE_SEL.equals(pmi.getData_type()))
					{
						 List<PrintModelCheckBox> pmcb_list=pmi.getCheckbox_item();
						 if(pmcb_list!=null&&pmcb_list.size()>0)
						 {//组装有复选框的数据
							 StringBuffer str=new StringBuffer();
							 for(int j=0;j<pmcb_list.size();j++)
							 {
								 
								 PrintModelCheckBox pmcb=pmcb_list.get(j);
								 if(pmcb==null) continue;
								 if(pmcb.getCheckbox_value()!=null&&pmcb.getCheckbox_value().trim().length()>0)
								 {
									str.append("[√]").append(pmcb.getCheckbox_name());
										
								 }else{//没有选择任何值的情况
									 str.append("□").append(pmcb.getCheckbox_name());
								 }
								 
								 List<PrintModelCheckBoxSon> pmcb_son_list=pmcb.getCheckbox_son();
								 //复选框有子复选框的情况
								 if(pmcb_son_list!=null&&pmcb_son_list.size()>0)
								 {
									 str.append("(");
									 for(int m=0;m<pmcb_son_list.size();m++)
									 {
										 PrintModelCheckBoxSon pmcb_son=pmcb_son_list.get(m);
										 if(pmcb_son==null) continue;
										 
											 if(pmcb_son.getSon_value()!=null&&pmcb_son.getSon_value().trim().length()>0)
											 //选中
											 {
												str.append("[√]").append(pmcb_son.getCheckbox_name());												 
											 }
											 else
											 {
												 str.append("□").append(pmcb_son.getCheckbox_name());
											 }
									 }
									 
									 str.append(")");
								 }
								 
								 List<PrintModelCheckBoxText> pmcb_text_list=pmcb.getCheckbox_text();
								 /*
								 //复选框后面有输入框的情况
								 if(pmcb_text_list!=null&&pmcb_text_list.size()>0)
								 {
									 str.append("  ");
									 for(int n=0;n<pmcb_text_list.size();n++)
									 {
										 PrintModelCheckBoxText pmcb_text=pmcb_text_list.get(n);
										 if(pmcb_text==null) continue;
										 String text_name=pmcb_text.getText_name();
										 if(text_name==null) text_name="";
										 String text_unit=pmcb_text.getText_unit();
										 if(text_unit==null) text_unit="";
										 str.append(text_name);
										 //有多个输入框
										 for(int l=0;l<checkbox.length;l++)
										 {
											 String check_box=checkbox[l];//获取填写的每项数据
											 if(check_box==null)check_box="";
											 
											 String [] tem=checkbox[l].split("#");
											 if(tem==null||tem.length<2){
												 continue;
											 }
											 if(pmcb.getCheckbox_code().equals(tem[0]))
											 {
												 System.out.println("tem:"+tem[1]);
												 String [] son_text=tem[1].split("&");
												 if(son_text==null||son_text.length<2){
													 continue;
												 }
												 System.out.println("son_text:"+son_text[1]);
												 String [] ch_text=son_text[1].split("|");
												 System.out.println(n+":"+"ch_text:"+ch_text[n]);
												 if(n<ch_text.length){
													 str.append(ch_text[n]);
												 }
											 }
										 }
										 str.append(text_unit).append("  ");
									 }
									 
								 }
								 */
							 }
							 value=str.toString();
						 }
					}
					pmi.setValue(value);
				}
			}
			list_return.add(pm);
		}
		return list_return;
	}

	/**
	 * 删除模板
	 */
	public String delete_template(DocTemplate template) {
		if(template!=null&&template.getTemplateId()!=null&&template.getTemplateId().trim().length()>0){
			reportMapper.delete_template_item(template);
			reportMapper.delete_template_band(template);
			reportMapper.delete_template(template);
			return "Y";
		}else {
			return "N";
		}
	}

	/**
	 * 修改模板状态
	 */
	public String update_template_valid(DocTemplate template) {
		if(template!=null&&template.getTemplateId()!=null&&template.getTemplateId().trim().length()>0){
			reportMapper.update_template_valid(template);
			return "Y";
		}else {
			return "N";
		}	
	}

	/**
	 * 删除数据
	 */
	@Transactional(value = "transactionManager")
	public HashMap<String, Object> delte_data(DocRecord record,boolean isNurseRecord,String deptCode) {
		HashMap<String, Object> maps = new HashMap<String, Object>();
		if(null==record || StringUtils.isEmpty(record.getRecordId())){
			maps.put("rslt", "N");
			return maps;
		}
		//删除前取出信息，用于更新打印页码
		final DocRecord docRecord = reportMapper.getDocRecordByID(record.getRecordId());
		record.setDate_list(docRecord.getDate_list());
		record.setTime_list(docRecord.getTime_list());
		record.setTemplate_id(docRecord.getTemplate_id());
		//先判断一下操作权限，再执行后续动作
		//1、如果不是本人创建的记录，不允许删除
		//2、是否满足操作权限
		if(!record.getCreate_person().equals(docRecord.getCreate_person())){
			maps.put("rslt", "N");
			return maps;
		}

		//有24小时转抄
		if(isNurseRecord && (orderService.has24InAndOutOrder(record.getInpatient_no(), null, null)
				|| orderService.has24UrineOrder(record.getInpatient_no(), null, null))){
			DocRecordDataStatistic docRecordDataStatistic = processDeleteDocReportStatistic(record,deptCode);
			maps.put("docStatistic", docRecordDataStatistic);

		}

		reportMapper.delete_data_item(record.getRecordId());
		reportMapper.delete_data(record.getRecordId());

		//异步更新打印信息
		mnisThreadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				updatePageNoForDocRecord(docRecord);
			}
		});

		//记录操作行为
		if(DocPrintConstants.bRecordAction) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("DocRecord", record);
			paramMap.put("boolean", isNurseRecord);
			paramMap.put("String", deptCode);
			String paramStr = GsonUtils.toJson(paramMap);
			recordAction(paramStr, record.getCreate_person());
		}

		return maps;
	}

	/**
	 * 获取护理记录分类
	 */
	public List<DocType> getDocType(DocType docType){
		return reportMapper.getDocType(docType);
	}

	public BaseListVo getTrees(String queryType, String inpatient_no,String dept_code,int docQueryType) {
		BaseListVo vo = new BaseListVo();
		vo.setRslt(ResultCst.SUCCESS);
		List<LeftTreeVo> vos = null;
		Map<String, String> map = new HashMap<String, String>();
		LOGGER.info("inpatient_no:"+inpatient_no+"queryType:"+queryType+"dept_code:"+dept_code);
		map.put("pat_id", inpatient_no);
		map.put("type", queryType);
		map.put("dept_code", dept_code);
		try {
			//vos = reportMapper.getTrees(map);
			vos=new ArrayList();
			//改造原来组建树形菜单的方法(先把所有数据取出来,避免数据库性能问题)
			List <DocTreeType> list=reportMapper.getDataForTree(map);
			
			if(DocReportConstants.ReportQueryType.HISTORY.getIndex() == docQueryType){
				list = reportMapper.getDataForHistoryTree(map);
			}
			//出院转科的病人，只获取已有记录的文书
			if(DocReportConstants.ReportQueryType.TRANSFER.getIndex() == docQueryType){
				list = reportMapper.getDataOfTransferPatient(map);
			}
			if(list!=null&&list.size()>0)
			{
				for(int i=0,l=list.size();i<l;i++)
				{
					DocTreeType temp_data=list.get(i);//一条文书分类数据
					
					//第一层结构（文书分类）
					LeftTreeVo leftTreeVo=new LeftTreeVo();
					leftTreeVo.setPat_id(inpatient_no);//设置病人ID
					leftTreeVo.setType(queryType);//查询入口,PC&NDA
					leftTreeVo.setType_name(temp_data.getType_name());//文书分类名称
					leftTreeVo.setType_id(temp_data.getType_id());//文书分类ID
					leftTreeVo.setShow_type(temp_data.getShow_type());
					//组建第二层结构(模板数据)
					List <DcoTreeTemplate> template_list=temp_data.getTemplate_list();
					if(template_list!=null&&template_list.size()>0)
					{
						List<TemplateVo> data = new ArrayList<TemplateVo>(); //定义一个装着模板的集合
						for(int m=0,n=template_list.size();m<n;m++)
						{
							DcoTreeTemplate template_data=template_list.get(m);
							TemplateVo template_vo=new TemplateVo();
							
							//封装文书模板
							template_vo.setPat_id(inpatient_no);
							
							//如果是评估单且有记录的情况，直接显示记录ID
							if(DocReportConstants.TEMPLATE_TYPE_FIXED.equals(leftTreeVo.getShow_type()))
							{
								List<DocTreeRecord> record_list=template_data.getRecord_list();
								if(record_list!=null&&record_list.size()>0){
									//一个人只有一份评估单记录
									template_vo.setTemplate_name(template_data.getTemplate_name());
									template_vo.setRecord_id(record_list.get(0).getRecord_id());
								}else{
									template_vo.setRecord_id(null);
								}
							}else{
								template_vo.setRecord_id(null);
							}
							
							//如果是nda端进来，去掉打印菜单
							if("NDA".equals(queryType)){
								if(template_data.getTemplate_id().contains("print"))
								{
									continue;
								}
							}
							template_vo.setTemplate_id(template_data.getTemplate_id());//模板ID
							template_vo.setTemplate_name(template_data.getTemplate_name());	//模板名称	
							template_vo.setReport_type(template_data.getReport_type());//模板分类
							template_vo.setType(queryType);
							
							//护理记录（每天都有记录，需要根据日期和时间展示相关数据）
							if(DocReportConstants.TEMPLATE_TYPE_LIST.equals(leftTreeVo.getShow_type()))
							{
								List<DocTreeRecord> record_list=template_data.getRecord_list();
								if(record_list!=null&&record_list.size()>0)
								{
									//用来封装日期的集合
									List<TimeTreeVo> date_list=new ArrayList<TimeTreeVo>();
									Set<String> dateSet = new HashSet<String>();
									for(int j=0,k=record_list.size();j<k;j++){
										DocTreeRecord record_data=record_list.get(j);
										
										TimeTreeVo timeTreeVo=new TimeTreeVo();
										timeTreeVo.setRecord_id(record_data.getRecord_id());
										String date=record_data.getDate_list();//设置日期
										//NDA端直接显示日期和时间
										if("NDA".equals(queryType)){
											//如果有时间，则追加时间到日期后面
											if(record_data.getTime_list()!=null&&record_data.getTime_list().trim().length()>0){
												String time=record_data.getTime_list();
												if(time!=null&&time.length()>4){
													time=time.substring(0,5);
												}
												date=date+" "+time;
											}
											timeTreeVo.setData_value(date);
											date_list.add(timeTreeVo);
										}else{//PC端
											if(record_data.getTime_list()==null||record_data.getTime_list().trim().length()==0){
												timeTreeVo.setData_value(date);
												timeTreeVo.setPat_id(inpatient_no);
												timeTreeVo.setTemplate_id(template_data.getTemplate_id());
												if(!dateSet.contains(date)){
													date_list.add(timeTreeVo);
													dateSet.add(date);
												}
											}else{
												//如果是第一条记录
												if(j==0){
													List<TimeTreeVo> date_list_son=new ArrayList<TimeTreeVo>();
													TimeTreeVo timeTreeVo_son=new TimeTreeVo();
													timeTreeVo_son.setRecord_id(record_data.getRecord_id());
													String time=record_data.getTime_list();
													if(time!=null&&time.length()>4){
														time=time.substring(0,5);
													}
													timeTreeVo_son.setData_value(time);
													date_list_son.add(timeTreeVo_son);
													timeTreeVo.setData_value(record_data.getDate_list());
													timeTreeVo.setPat_id(inpatient_no);
													timeTreeVo.setTemplate_id(template_data.getTemplate_id());
													timeTreeVo.setData(date_list_son);
													date_list.add(timeTreeVo);
												}else{
													//从第二条记录起,需与上一条记录比较,如果日期相同,则追加到相应节点下
													String date_next=record_data.getDate_list();
													if(date_next==null){
														date_next="";
													}
													DocTreeRecord record_data_pre=record_list.get(j-1);//取上一条记录
													//如果跟上一条记录的日期相等,则追加到起节点之下
													if(date_next.equals(record_data_pre.getDate_list())){
														TimeTreeVo timeTreeVo2=new TimeTreeVo();
														timeTreeVo2.setRecord_id(record_data.getRecord_id());
														String time=record_data.getTime_list();
														if(time!=null&&time.length()>4){
															time=time.substring(0,5);
														}
														timeTreeVo2.setData_value(time);
														timeTreeVo2.setPat_id(inpatient_no);
														timeTreeVo2.setTemplate_id(template_data.getTemplate_id());
														int t=date_list.size();
														TimeTreeVo t_vo=date_list.get(t-1);//获取最新的一条,追加到后面
														List <TimeTreeVo> temp=t_vo.getData();
														if(temp!=null){
															temp.add(timeTreeVo2);
														}
														t_vo.setData(temp);
														date_list.set(t-1, t_vo);
													}else{//与上一条的日期不相等，直接作为子选项
														List<TimeTreeVo> date_list_son=new ArrayList<TimeTreeVo>();
														TimeTreeVo timeTreeVo_son=new TimeTreeVo();
														timeTreeVo_son.setRecord_id(record_data.getRecord_id());
														String time=record_data.getTime_list();
														if(time!=null&&time.length()>4){
															time=time.substring(0,5);
														}
														timeTreeVo_son.setData_value(time);
														date_list_son.add(timeTreeVo_son);
														timeTreeVo.setData_value(record_data.getDate_list());
														timeTreeVo.setPat_id(inpatient_no);
														timeTreeVo.setTemplate_id(template_data.getTemplate_id());
														timeTreeVo.setData(date_list_son);
														date_list.add(timeTreeVo);
													}
												}
											}
										}
									}
									template_vo.setData(date_list);
								}								
							}							
							data.add(template_vo);
						}
						leftTreeVo.setData(data);
					}

					//对于出院转科病人，还需要获取护理记录单对应的打印模板
					if((DocReportConstants.ReportQueryType.TRANSFER.getIndex() == docQueryType
							|| DocReportConstants.ReportQueryType.HISTORY.getIndex() == docQueryType)
							&& DocReportConstants.TEMPLATE_TYPE_LIST.equals(leftTreeVo.getShow_type())) {
						AddPrintTemplateInTransferPatTree(leftTreeVo, inpatient_no, queryType);
					}

					vos.add(leftTreeVo);
				}
			}
		} catch (Exception e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("数据查询错误！");
			LOGGER.error(e.getMessage());
		}
		vo.setData(vos);
		return vo;
	}

	/**
	 * 获取出院转科病人的护记打印模板，并添加到结构树中
	 * @param leftTreeVo
	 * @param inpatient_no
	 * @param queryType
	 */
	private void AddPrintTemplateInTransferPatTree(LeftTreeVo leftTreeVo, String inpatient_no, String queryType){
		//1.获取护理记录的模板ID
		List<TemplateVo> templateVos = leftTreeVo.getData();
		List<String> templateIDs = new ArrayList<String>();
		for(TemplateVo templateVo : templateVos){
			if(DocReportConstants.REPORT_TYPE_HLJL.equals(templateVo.getReport_type())) {
				templateIDs.add(templateVo.getTemplate_id());
			}
		}
		//2、根据护记模板ID获取对应的打印模板
		if(0<templateIDs.size()) {
			List<DocTemplate> printTemplates = reportMapper.getPrintTemplateByID(templateIDs);
			//3、组织成树形结构
			if (0 < printTemplates.size()) {
				for (DocTemplate printTemplate : printTemplates) {
					TemplateVo tempVo = new TemplateVo();
					tempVo.setPat_id(inpatient_no);
					tempVo.setTemplate_id(printTemplate.getTemplateId());
					tempVo.setTemplate_name(printTemplate.getTemplateShowName());
					tempVo.setType(queryType);
					tempVo.setReport_type(printTemplate.getReport_type());

					//4、添加到模板结构树中
					templateVos.add(tempVo);
				}
			}
		}
	}

	/**
	 * 加载并处理数据
	 */
	public BaseMapVo getTempDatas(DocRecord docRecord,String isprint,String startDate,String endDate,boolean isHistory,String deptCode) {
		BaseMapVo vo = new BaseMapVo();
		DocDataRecord record = new DocDataRecord();
		//护理文书的打印要单独做
		if ("Y".equals(isprint) && (docRecord.getTemplate_id().contains("print"))) {
			//打印护理记录单的id格式为: print_xxxx(对应模板ID)
			String[] strTempID = docRecord.getTemplate_id().split("_");
			if (2 > strTempID.length) {
				//打印模板ID的格式不对
				vo.setMsg("打印模板ID的格式不对！");
				vo.setRslt(ResultCst.FAILURE);
				return vo;
			} else {
				String template_id = strTempID[1];
				docRecord.setTemplate_id(template_id);
			}
		}
		record.setInpatient_no(docRecord.getInpatient_no());//住院流水号
		record.setTemplate_id(docRecord.getTemplate_id());//模板ID
		record.setRecord_id(docRecord.getRecordId());//
		record.setModify_time(docRecord.getDate_list());
		record.setStartDate(startDate);
		record.setEndDate(endDate);
		try {
			//先获取所有的数据

			List<DocDataRecord> list = reportMapper.loadAllData(record);
			if (list.isEmpty() && isHistory) {
				//获取历史数据
				list = reportMapper.loadAllHistoryData(record);
			}
			List<DocRecord> tmps = new ArrayList<DocRecord>();
			String show_type = reportMapper.getShowType();
			if (list != null && list.size() > 0) {
				DocRecord d_empty = new DocRecord();

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				d_empty.setNow_time(df.format(new Date()));//将系统时间传给前端
				//设置病人号码，模板，打印方式等数据
				d_empty.setInpatient_no(docRecord.getInpatient_no());//病人编号
				d_empty.setTemplate_id(docRecord.getTemplate_id());//模板编号
				d_empty.setRecordId(docRecord.getRecordId());//记录ID
				d_empty.setPrint_show(show_type);//打印时展示的方式	
				d_empty.setCreate_person(list.get(0).getCreate_person());
				d_empty.setApprove_status(list.get(0).getApprove_status());
				String indate = getInDate(docRecord.getInpatient_no());
				//isprint为y时是打印进来
				if ("Y".equals(isprint)) {
					d_empty.setData_list_print(deal_list_println(docRecord.getTemplate_id(), docRecord.getInpatient_no(), indate, isHistory));
				} else {
//					d_empty.setData_list(deal_list(list));//处理护理记录单数据
					d_empty.setData_list(dealHlList(list,docRecord.getPrint_show()));
				}

				d_empty.setData_item_list(deal_item(list, docRecord.getInpatient_no()));//处理评估单数据
				d_empty.setDyna_list(deal_dyna(list));//处理动态列表数据
				//如果是护理记录单，需要统计前一天的总量
				if ("1".equals(docRecord.getPrint_show())) {

					//获取统计时间点，可能每个医院都不同
					Properties prop = PropertiesUtils.getProperties(ReportConstants.REPORT_PORP_PATH);
					String count_time = "";//出入量统计时间
					String count_time_afternoon = "";//下午统计时间
					try {
						count_time = prop.getProperty("count_time").trim();
						count_time_afternoon = prop.getProperty("count_time_afternoon");
						d_empty.setCount_time(count_time);
						d_empty.setCount_time_afternoon(count_time_afternoon);
					} catch (Exception e) {
						e.printStackTrace();
					}

					//有24小时出入量才统计
					//if ((orderService.has24InAndOutOrder(record.getInpatient_no(), null, null)
							//|| orderService.has24UrineOrder(record.getInpatient_no(), null, null))) {
						if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
							startDate = docRecord.getDate_list();
							endDate = DateUtil.format(DateUtils.addDays(DateUtil.parse(docRecord.getDate_list()), 1), com.lachesis.mnis.core.util.DateUtil.DateFormat.YMD);
						}
						List<DocRecordDataStatistic> docRecordDataStatistics = reportMapper
								.getDocReprotDataAllStatistic(docRecord.getInpatient_no(), deptCode, startDate, endDate,docRecord.getTemplate_id());
						docRecordDataStatistics = showDocRecordDataStatistics(docRecordDataStatistics, count_time, count_time_afternoon, indate);
						vo.addData("docStatistic", docRecordDataStatistics);
					//}
				}
				//前端打印时需要所有的诊断信息，但是NDA又不愿改动接口，只好单独做一个了
				if (d_empty != null && d_empty.getData_item_list() != null && d_empty.getData_item_list().size() > 0) {
					d_empty.setData_item(d_empty.getData_item_list().get(d_empty.getData_item_list().size() - 1).getList_item());
				}
				if (d_empty.getData_item() == null) {
					d_empty.setData_item(new ArrayList());
				}
				tmps.add(d_empty);
				vo.addData("list", tmps);
			}
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setRslt(ResultCst.FAILURE);
		}
		//数据返回
		return vo;
	}

	/**
	 * 计算护理记录单的入量和出量
	 * @param docRecord
	 * @return
	 */
	public DocReportCountInOut getBeforeDate(DocRecord docRecord,String count_time,boolean isHistory){
		DocReportCountInOut inout=new DocReportCountInOut();
		try{
			//选中单条记录时不统计总量
			if(docRecord.getRecordId()==null||docRecord.getRecordId().trim().length()==0){
				//如果选中模板，展示的记录中也无需统计前一天的总量
				if(docRecord.getDate_list()!=null&&docRecord.getDate_list().trim().length()>0){
					//只用选中日期时，计算护理记录单里面入量和出量的总量
					Map<String,String> map=new HashMap();
					map.put("template_id", docRecord.getTemplate_id());
					map.put("inpatient_no", docRecord.getInpatient_no());
					map.put("date_list", docRecord.getDate_list());
					map.put("count_time", count_time);
					//计算入量
					map.put("in_out", "'inTake'");
					List<RecordDataItem> list_before=reportMapper.get_before_data(map);
					if(isHistory && list_before.isEmpty()){
						list_before = reportMapper.get_before_history_data(map);
					}
					if(list_before!=null&&list_before.size()>0)
					{
						double inTake=0;//入量
						for(int lb=0;lb<list_before.size();lb++)
						{
							RecordDataItem it=list_before.get(lb);
							if(BodySignConstants.INTAKE.equals(it.getTemplate_item_id())){
								String intake=it.getRecord_value();
								if(intake==null||"".equals(intake.trim())){
									intake="0";
								}
								inTake+=Double.parseDouble(intake);
							}
							
						}
						inout.setId("inTake");
						inout.setName("入量");
						inout.setValue(inTake+"");
					}else{
						inout.setId("inTake");
						inout.setName("入量");
						inout.setValue("");					
					}
					
					//计算出量
					map.put("in_out", "'out_name','outTake'");
					list_before=reportMapper.get_before_data(map);
					if(isHistory && list_before.isEmpty()){
						list_before = reportMapper.get_before_history_data(map);
					}
					if(list_before!=null&&list_before.size()>0)
					{
						Map<String,Double> map_temp=new HashMap();
						
						List<DocReportCountInOut> out_list=new ArrayList();
						
						int ll=list_before.size();
						//出量名称和出量必须成对出现，不然不予以计算
						if(ll%2!=0)
						{
							DocReportCountInOut tt=new DocReportCountInOut();
							tt.setId("");
							tt.setName("出量名称和出量必须成对出现");
							tt.setValue("");
							out_list.add(tt);				
						}
						for(int lb=0;lb<ll;lb++)
						{
							RecordDataItem t=list_before.get(lb);
							//入量名称
							if(lb%2==0)
							{
								
								double out_take=Double.parseDouble(list_before.get(lb+1).getRecord_value());
								//如果之前有保存值
								if(map_temp.get(t.getRecord_value())!=null)
								{
									//获取之前保存的出量
									double outtake=(double) map_temp.get(t.getRecord_value());
									outtake+=out_take;
									map_temp.put(t.getRecord_value(), outtake);
								}else{
									//键值对 键为出量代码，值为出量
									map_temp.put(t.getRecord_value(), out_take);
								}
							}
						}
						for(String key : map_temp.keySet()) {
							Map queryMap=new HashMap();
							queryMap.put("metadata_code", key);
							String metadata_name=reportMapper.getNameBycode(queryMap);
							DocReportCountInOut tt=new DocReportCountInOut();
							tt.setId(key);
							tt.setName(metadata_name);
							tt.setValue(map_temp.get(key)+"");
							out_list.add(tt);
						}
						inout.setOut_list(out_list);
					}				
					
				}
			}			
		}catch(Exception e){
			e.printStackTrace();
			inout.setId("inTake");
			inout.setName("入量");
			inout.setValue("");	
		}finally{
			return inout;
		}
	}
	
	/**
	 * 处理打印的数据。
	 * @param template_id
	 * @param inpatient_no
	 * @return
	 */
	public List<String []> deal_list_println(String template_id,String inpatient_no,String indate,boolean isHistory)
	{
		String [] itemname=TemplateCache.getMetadataCodeByTemplateID(template_id);
		StringBuffer strbu=new StringBuffer();
		if(itemname==null||itemname.length==0){
			HashMap<String,String> map=new HashMap();
			map.put("template_id", template_id);
			List<String> temp=reportMapper.get_item_list(map);
			
			//需要加上床号和科室，表头展示时用到
			itemname=new String[temp.size()+2];
			for(int i=0;i<temp.size();i++)
			{
				itemname[i]=temp.get(i);
			}
			//床号
			itemname[temp.size()]=DocReportConstants.BED_NO;
			//科室
			itemname[temp.size()+1]=DocReportConstants.DEPT_NAME;
			
			TemplateCache.addMetadataInMap(template_id, itemname);
		}
		
		for(int i=0;i<itemname.length;i++)
		{
			if(i!=(itemname.length-1)){
				strbu.append("'").append(itemname[i]).append("',");
			}else{
				strbu.append("'").append(itemname[i]).append("'");
			}			
		}
		HashMap<String,String> map=new HashMap();
		map.put("template_id", template_id);		
		map.put("inpatient_no", inpatient_no);
		map.put("item_name", strbu.toString());
		List<DocReportPrintData> data_list=reportMapper.queryDataForPrint(map);
		if(isHistory && data_list.isEmpty()){
			data_list=reportMapper.queryHistoryDataForPrint(map);
		}
		List <String[]> return_list=new ArrayList();
		
		//所有日期集合
		Map <String,String> date_map=new HashMap();
		//统计时间
		String morning="";
		String afternoon="";

		Properties prop = PropertiesUtils.getProperties(ReportConstants.REPORT_PORP_PATH); 
        String count_time="";//出入量统计时间
        String count_time_afternoon="";//下午统计时间
        try {   
        	morning=" "+prop.getProperty("count_time").trim(); 
        	afternoon=" "+prop.getProperty("count_time_afternoon");
        } catch (Exception e) {   
            e.printStackTrace();   
        }		
		
		//下面是按照日期分组，先把所有的日期列举出来
		for(int i=0;i<data_list.size();i++)
		{
			DocReportPrintData data=data_list.get(i);
			List<DocReportPrintDataDetail> detail_list=data.getData_list();
			for(int j=0;j<detail_list.size();j++)
			{
				DocReportPrintDataDetail detail=detail_list.get(j);
				if(DocReportConstants.DATE_LIST.equals(detail.getTemplate_item_id()))
				{
					date_map.put(detail.getRecord_value()+morning, "N");
					if(afternoon!=null&&afternoon.trim().length()>0)
					{
						date_map.put(detail.getRecord_value()+afternoon, "N");
					}
				}
			}
		}
		
		//重新设置数据格式和打印
		for(int i=0;i<data_list.size();i++)
		{
			DocReportPrintData data=data_list.get(i);
			List<DocReportPrintDataDetail> detail_list=data.getData_list();
			String [] data_return=new String[itemname.length];
			String date="";
			String time="";			
			for(int j=0;j<detail_list.size();j++)
			{
				DocReportPrintDataDetail detail=detail_list.get(j);
				for(int m=0;m<itemname.length;m++)
				{
					//当数据的键与模板中的键相同时，写入数组
					if(itemname[m].equals(detail.getTemplate_item_id()))
					{
						if(DocReportConstants.APPROVE_PERSON.equals(itemname[m])
								|| DocReportConstants.CREATE_PERSON.equals(itemname[m]))
						{
							if(detail.getRecord_value()!=null&&detail.getRecord_value().indexOf("-")!=-1){
								data_return[m]=detail.getRecord_value().substring(detail.getRecord_value().indexOf("-")+1, detail.getRecord_value().length());
							}else{
								data_return[m]=detail.getRecord_value();
							}
							
						}else{
							data_return[m]=detail.getRecord_value();
							//对于SWT类型的数据，未选择显示为空，选择的显示对号
							//严格来说应该按照metadata对应的datatype来判断，但是这样处理太麻烦
							//简单起见使用Y/N进行判断，不过要和前端严格约定。
							if("N".equals(data_return[m])){
								data_return[m] = "";
							}else if("Y".equals(data_return[m])){
								data_return[m] = "√";
							}
						}
						if(DocReportConstants.DATE_LIST.equals(itemname[m])){
							date=detail.getRecord_value();
						}
						if(DocReportConstants.TIME_LIST.equals(itemname[m])){
							time=detail.getRecord_value();
						}						
					}
				}
			}
			if(i>0)
			{
				//还没有递归到最后一条
				if(i!=(data_list.size()-1))
				{
					
					return_list.add(data_return);
											
					//取下一条数据
					DocReportPrintData data2=data_list.get(i+1);
					List<DocReportPrintDataDetail> detail_list2=data2.getData_list();
					
					String date2="";
					String time2="";
					for(int j=0;j<detail_list2.size();j++)
					{
						if(DocReportConstants.DATE_LIST.equals(detail_list2.get(j).getTemplate_item_id())){
							date2=detail_list2.get(j).getRecord_value();
						}
						if(DocReportConstants.TIME_LIST.equals(detail_list2.get(j).getTemplate_item_id())){
							time2=detail_list2.get(j).getRecord_value();
						}
					}
					
					String counttime=date+morning;
					String datetime=date2+" "+time2;
					
					//当前记录的前一天
					String date_before=ReportUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd");
					//当前记录的后一天
					String date_after=ReportUtil.getSpecifiedDayAfter(date, "yyyy-MM-dd");
					
					//下一条记录时间比早上统计的时间大
					if(
							(ReportUtil.compare_date(counttime+":00",datetime+":00")==1)&&"N".equals(date_map.get(date+morning))
							&&(ReportUtil.compare_date(counttime+":00",(date+" "+time)+":00")!=1)
						
					)
					{
						String [] str=countInOutByTime(data_list, date_before+" "+morning, date+" "+morning,date+"上午 统计 ",indate, morning, afternoon);
						if(!"".equals(str[0]))
						{
							return_list.add(str);
						}
						
						date_map.put(date+morning, "Y");
					}
					counttime=date+afternoon;
					datetime=date2+" "+time2;
					//下一条记录时间和下午统计的时间大
					if(
							(ReportUtil.compare_date(counttime+":00",datetime+":00")==1)&&"N".equals(date_map.get(date+afternoon))
							&&(ReportUtil.compare_date(counttime+":00",(date+" "+time)+":00")!=1)
					)		
							
					{
						
						String [] str=countInOutByTime(data_list, date+morning, date+afternoon,date+"下午 统计 ",indate, morning, afternoon);
						if(!"".equals(str[0]))
						{
							return_list.add(str);
						}
						date_map.put(date+afternoon, "Y");
					}	

					//日期不相同
					if(!date.equals(date2))
					{
						//时间相隔大于一天（就是有一些日期没有填写数据），下一天算早上结量
						if(ReportUtil.getDistanceTimes(date+" 00:00:00",date2+" 00:00:00")>24)
						{
							String start_time=date+morning;
							String end_time=date_after+" "+morning;
							String [] str=countInOutByTime(data_list, start_time, end_time, end_time.substring(0,10)+"上午 统计 ",indate, morning, afternoon);
							if(!"".equals(str[0]))
							{
								return_list.add(str);
							}
							//设置下一天早上已经统计的标识
							date_map.put(end_time.substring(0,10)+morning,"Y");	
						}							
					}						
										
					
					//日期不相同
					if(!date.equals(date2))
					{	
						//当前记录时间，比早上统计的时间大
						if((ReportUtil.compare_date(date+" "+time+":00",date+morning+":00")==-1)&&"N".equals(date_map.get(date+afternoon)))
						{
							String start_time=date+morning;
							String end_time=date+afternoon;
							String [] str=countInOutByTime(data_list, start_time, end_time, date+"下午 统计 ",indate, morning, afternoon);
							if(!"".equals(str[0]))
							{
								return_list.add(str);
							}
							//设置下一天早上已经统计的标识
							date_map.put(date+afternoon,"Y");		
						}

						//下一条记录时间，大于上午统计的时间
						if(ReportUtil.compare_date(datetime+":00",date2+morning+":00")==-1)
						{
							String start_time=(date_before=ReportUtil.getSpecifiedDayBefore(date2, "yyyy-MM-dd"))+morning;
							String end_time=date2+morning;
							String [] str=countInOutByTime(data_list, start_time, end_time, date2+"上午 统计 ",indate, morning, afternoon);
							if(!"".equals(str[0]))
							{
								return_list.add(str);
							}
							//设置下一天早上已经统计的标识
							date_map.put(date2+morning,"Y");
						}

						//下一条记录时间，大于下午统计的时间
						if(ReportUtil.compare_date(datetime+":00",date2+afternoon+":00")==-1)
						{
							String start_time=date2+morning;
							String end_time=date2+morning;
							String [] str=countInOutByTime(data_list, start_time, end_time, date2+"下午 统计 ",indate, morning, afternoon);
							if(!"".equals(str[0]))
							{
								return_list.add(str);
							}
							//设置下一天下午已经统计的标识
							date_map.put(date2+afternoon,"Y");
						}							
						
					}						
					
				}
				else
				//最后一条记录
				{
					return_list.add(data_return);

					//早上统计的时间
					String morningcounttime=date+""+morning+":00";
					//下午统计的时间
					String afternooncounttime=date+""+afternoon+":00";
					//数据记录的时间
					String nowtime=date+" "+time+":00";
							
					//开始统计的时间
					String start_time="";
					String end_time="";

					//以下代码供调试用
//					int com_morning = ReportUtil.compare_date(nowtime,morningcounttime);
//					int com_afternoon = ReportUtil.compare_date(nowtime, afternooncounttime);
//					String ret_moring = date_map.get(date+morning);
//					String ret_afternoon = date_map.get(date+afternoon);

					//bug, 最后一条记录处理当天内统计，因此要比较在早上统计时间内，还是在下午统计时间内
					// 处理逻辑基于这样一种约定：如果最后一条以上记录处理得当，则对应的统计都已准确无误，
					// 那么该代码块的主要功能就是处理最后一条记录的统计归属问题。by renjunming, 2016-04-25
					String beforedate=ReportUtil.getSpecifiedDayBefore(morningcounttime+":00", "yyyy-MM-dd HH:mm:ss");
					if(ReportUtil.compare_date(nowtime,morningcounttime)>=0 && ("N".equals(date_map.get(date+morning))))
					{
						start_time=beforedate;
						end_time=morningcounttime;
						String [] str=countInOutByTime(data_list, start_time, end_time,date+"上午 统计 ",indate, morning, afternoon);
						if(!"".equals(str[0]))
						{
							return_list.add(str);
						}
						date_map.put(date+morning, "Y");							
					}
					if(ReportUtil.compare_date(nowtime,afternooncounttime)>=0 && ("N".equals(date_map.get(date+afternoon))))
					{
						start_time=morningcounttime;
						end_time=afternooncounttime;
						String [] str=countInOutByTime(data_list, start_time, end_time,date+"下午 统计 ",indate, morning, afternoon);
						if(!"".equals(str[0]))
						{
							return_list.add(str);
						}
						date_map.put(date+afternoon, "Y");							
					}
					//对于大于当天下午统计时间的情况，记录为第二天早上统计量
					if(ReportUtil.compare_date(nowtime,afternooncounttime)==-1){
						start_time=date+morning;
						String date_after=ReportUtil.getSpecifiedDayAfter(date, "yyyy-MM-dd");
						end_time=date_after+" "+morning;
						String [] str=countInOutByTime(data_list, start_time, end_time, end_time.substring(0,10)+"上午 统计 ",indate, morning, afternoon);
						if(!"".equals(str[0]))
						{
							return_list.add(str);
						}
						//设置下一天早上已经统计的标识
						date_map.put(end_time.substring(0,10)+morning,"Y");
					}
					
//
//						String type="上午 统计 ";
//						//小于上午统计的时间
//						if(ReportUtil.compare_date(nowtime,morningcounttime)==1)
//						{
//							//获取前一天7:00,统计到今天七点
//							start_time=ReportUtil.getSpecifiedDayBefore(morningcounttime, "yyyy-MM-dd HH:mm:ss");							
//							end_time=morningcounttime;
//						}else{
//							//从早上7点统计到下午三点
//							start_time=morningcounttime;
//							end_time=afternooncounttime;
//							type="下午 统计 ";
//						}
//						String [] str=countInOutByTime(data_list, start_time, end_time,date+type,indate, morning, afternoon);
//						return_list.add(str);
				}
			}else{
				//这里是第一条记录
				return_list.add(data_return);
				//如果不是只有一条记录
				if(data_list.size()>1)
				{
					DocReportPrintData data2=data_list.get(1);
					List<DocReportPrintDataDetail> detail_list2=data2.getData_list();
					String date2="";
					String time2="";
					for(int j=0;j<detail_list2.size();j++)
					{
						if(DocReportConstants.DATE_LIST.equals(detail_list2.get(j).getTemplate_item_id())){
							date2=detail_list2.get(j).getRecord_value();
						}
						if(DocReportConstants.TIME_LIST.equals(detail_list2.get(j).getTemplate_item_id())){
							time2=detail_list2.get(j).getRecord_value();
						}
					}
					String counttime=date+morning;
					String datetime=date2+" "+time2;
					
					String intake="";
					String outtake="";
					String outname="";
					
					String datetemp="";
					String timetemp="";
					
					//获得第一天的出入量
					DocReportPrintData data3=data_list.get(0);
					List<DocReportPrintDataDetail> detail_list3=data3.getData_list();
					for(int j=0;j<detail_list3.size();j++)
					{
						if(DocReportConstants.DATE_LIST.equals(detail_list3.get(j).getTemplate_item_id())){
							datetemp=detail_list3.get(j).getRecord_value();
							
						}
						if(DocReportConstants.TIME_LIST.equals(detail_list3.get(j).getTemplate_item_id())){
							timetemp=detail_list3.get(j).getRecord_value();
						}
						if("inTake".equals(detail_list3.get(j).getTemplate_item_id())){
							intake=detail_list3.get(j).getRecord_value();
						}
						if("outTake".equals(detail_list3.get(j).getTemplate_item_id())){
							outtake=detail_list3.get(j).getRecord_value();
						}
						if("out_name".equals(detail_list3.get(j).getTemplate_item_id())){
							outname=detail_list3.get(j).getRecord_value();
						}
					}	
					
					//大于下午统计的时间
					if(ReportUtil.compare_date((date+afternoon)+":00",(datetemp+" "+timetemp)+":00")==1)
					{
						continue;
					}
					
					//下一条记录时间比早上统计的时间大 并且当前记录时间小于早上统计时间，插入一条上午的统计记录
					if((ReportUtil.compare_date(counttime+":00",datetime+":00")==1)&&(ReportUtil.compare_date((datetemp+" "+timetemp)+":00",counttime+":00")==1))
					{	
						String [] counttem=new String[3];
						if(intake==null||"".equals(intake))
						{
							intake="0";
						}
						counttem[0]=" "+morning+" 统计 入量："+intake;
						if(outname!=null&&(!"".equals(outname)))
						{
							counttem[0]=counttem[0]+";出量："+outname+":"+outtake;
						}else{
							counttem[0]=counttem[0]+";出量：无";
						}
						
						if(ReportUtil.getDistanceTimes(counttime, indate+":00")>24)
						{
							counttem[0]=counttem[0]+" (24h)";
						}else{
							counttem[0]=counttem[0]+" ("+ReportUtil.getDistanceTimes(counttime, indate+":00")+"h)";
						}
						return_list.add(counttem);
						date_map.put(date+morning, "Y");						
					}
					
					//写入一条下午的统计记录
					counttime=date+afternoon;
					if(ReportUtil.compare_date(counttime+":00",datetime+":00")==1)
					{
						String [] counttem=new String[3];
						if(intake==null||"".equals(intake))
						{
							intake="0";
						}
						counttem[0]=" "+afternoon+" 统计 入量："+intake;
						if(outname!=null&&(!"".equals(outname)))
						{
							counttem[0]=counttem[0]+";出量："+outname+":"+outtake;
						}else{
							counttem[0]=counttem[0]+";出量：无";
						}
						if(ReportUtil.getDistanceTimes(counttime, indate+":00")>8)
						{
							counttem[0]=counttem[0]+" (8h)";
						}else{
							counttem[0]=counttem[0]+" ("+ReportUtil.getDistanceTimes(counttime, indate+":00")+"h)";
						}						
						return_list.add(counttem);
						date_map.put(date+afternoon, "Y");						
					}					
				}
			}

		}
		return return_list;
	}
	
	/**
	 * 根据时间段统计出入量
	 * @param data_list 
	 * @param start_time
	 * @param end_time
	 * @param l
	 * @return
	 */
	public String [] countInOutByTime(List<DocReportPrintData> data_list,String start_time,String end_time,String type,String indate,String morning,String afternoon){
		String [] str=new String [3];
		//入量总和
		double in=0;
		//组装出量
		StringBuffer out=new StringBuffer();
		//出量分类统计
		Map<String,Double> outmap=new HashMap();
		for(int m=0;m<data_list.size();m++)
		{
			DocReportPrintData data3=data_list.get(m);
			List<DocReportPrintDataDetail> detail_list3=data3.getData_list();
			
			String date_temp="";
			String time_temp="";
			
			String intake="";
			String outname="";
			String outtake="";
			for(int n=0;n<detail_list3.size();n++)
			{
				DocReportPrintDataDetail detail=detail_list3.get(n);
				if(DocReportConstants.DATE_LIST.equals(detail.getTemplate_item_id()))
				{
					date_temp=detail.getRecord_value();
				}
				if(DocReportConstants.TIME_LIST.equals(detail.getTemplate_item_id()))
				{
					time_temp=detail.getRecord_value();
				}
				if(BodySignConstants.INTAKE.equals(detail.getTemplate_item_id()))
				{
					if(detail.getRecord_value()!=null&&detail.getRecord_value().trim().length()>0)
					{
						intake=detail.getRecord_value();
					}
				}
				if(DocReportConstants.OUT_TAKE.equals(detail.getTemplate_item_id()))
				{
					if(detail.getRecord_value()!=null&&detail.getRecord_value().trim().length()>0)
					{
						outtake=detail.getRecord_value();
					}
				}
				if(DocReportConstants.OUT_NAME.equals(detail.getTemplate_item_id()))
				{
					if(detail.getRecord_value()!=null&&detail.getRecord_value().trim().length()>0)
					{
						outname=detail.getRecord_value();
					}
				}									
			}
			
			//大于等于开始时间小于等于结束时间
			if(
				(ReportUtil.compare_date(start_time+":00", date_temp+" "+time_temp+":00")==1)
				&&
				(ReportUtil.compare_date(date_temp+" "+time_temp+":00",end_time+":00")!=-1)
			 )
			{	
				
				if(intake!=null&&!"".equals(intake.trim()))
				{
					in+=Double.parseDouble(intake);
				}
				if(outname!=null&&outname.trim().length()>0&&outtake!=null&&outtake.trim().length()>0)
				{
					//根据名字获取出量分类
					Double temp=outmap.get(outname);
					
					//如果没有，重新设置一下
					if(temp==null||temp==0){
						outmap.put(outname, Double.valueOf(outtake));
					}else{
						//如果之前有数据，则取出来累加
						temp=outmap.get(outname)+Double.parseDouble(outtake);
						//再重新设置进去
						outmap.put(outname, Double.valueOf(temp));
					}
				}									
			}
		}
		//循环出所有的出量
		for(String key:outmap.keySet())
		{
			out.append(key).append(outmap.get(key)).append("； ");
		}
		if(out==null||out.length()==0){
			out=new StringBuffer("无");
		}
		String info=type+"入量："+in+"；出量："+out;
		System.out.println(start_time+":"+end_time+":"+in);
		//入院时间与统计时间相隔多少小时
		long l = 0;
		if(indate!=null){
			l = ReportUtil.getDistanceTimes(indate+":00", end_time+":00");
		}
		
		//下午统计
		if(type.indexOf("下午")!=-1){
			info=info.replace("下午", " "+afternoon);
			if(l<8)
			{
				info=info+" ("+l+"h)";
			}else{
				info=info+" (8h)";
			}
		}else if(type.indexOf("上午")!=-1){
			info=info.replace("上午", " "+morning);
			if(l<8)
			{
				info=info+" ("+l+"h)";
			}else{
				info=info+" (24h)";
			}			
		}
		
		System.err.println("========in:" + in + ";out:" + out.toString());
		//无出入量不统计显示
		if(in==0&&"无".equals(out.toString()))
		{
			str[0]="";
		}else{
			str[0]=info;
		}
		return str;
	}
	
	/**
	 * 处理护理记录单的数据
	 * @param item_list
	 * @return
	 */
	public List<RecordDataList> deal_list(List <DocDataRecord> list){
		List<RecordDataList> list_result=new ArrayList<RecordDataList>();
		boolean flag=true;

		RecordDataList headerRecord = null;
		for(int i=0,l=list.size();i<l;i++)
		{	
			DocDataRecord d_data = list.get(i);
			//所有的数据，先查询出来再组装
			List<DocDataRecordItem> item_list = d_data.getList();
			
			//护理记录数据集合,用来放一条护理记录
			RecordDataList recordDataList = null;
			//处理护理记录单列表的数据
			if(item_list!=null&&item_list.size()>0)
			{	
				recordDataList=new RecordDataList();
				//单条记录的流水号
				DocDataRecordItem tt=item_list.get(0);
				recordDataList.setRecord_id(tt.getRecord_id());
				recordDataList.setIsHeader(d_data.getIsHeader());
				List <RecordDataListDetail> list_detail=new ArrayList();
				HashMap<String,String> map=new HashMap();
				
				//一般护理记录中需要统计入量和出量，如果两者都为空，强行出入一个空的键值对
				boolean in_out=true;
				
				//单条记录的所有选项和数据值
				RecordDataListDetail detail=null;
				//循环出所有的键值对
				for(int j=0,ll=item_list.size();j<ll;j++)
				{
					DocDataRecordItem item_data=item_list.get(j);
					
					detail=new RecordDataListDetail();
					//只有等于0的才是护理记录的数据，不然是评估单数据
					if("0".equals(item_data.getParent_id()))
					{
						if(//TITLE为动态列表表头信息
							(!"TITLE_ONE".equals(item_data.getTemplate_item_id()))
							&&(!"TITLE_TWO".equals(item_data.getTemplate_item_id()))
							&&(!"TITLE_THREE".equals(item_data.getTemplate_item_id()))
							&&(!"TITLE_FOUR".equals(item_data.getTemplate_item_id()))
							&&(!"TITLE_FIVE".equals(item_data.getTemplate_item_id()))
						)
						{
							detail.setData_key(item_data.getTemplate_item_id());
							detail.setData_value(item_data.getRecord_value());
							//设置动态表头选项
							if(
								"DATA_ONE".equals(item_data.getTemplate_item_id())
								||"DATA_TWO".equals(item_data.getTemplate_item_id())
								||"DATA_THREE".equals(item_data.getTemplate_item_id())
								||"DATA_FOUR".equals(item_data.getTemplate_item_id())
								||"DATA_FIVE".equals(item_data.getTemplate_item_id())
							)
							{//把所有的选项查询出来
								map.put("data_key", item_data.getTemplate_item_id());
								detail.setList_select(reportMapper.list_select(map));
							}else
							{
								detail.setList_select(null);
							}
							flag=false;
							list_detail.add(detail);//设置键值对数据项
							
							if("inTake".equals(detail.getData_key())||"outTake".equals(detail.getData_key()))
							{
								in_out=false;
							}

						}
					}
				}
				//如果没有入量和出量，强行加入入量和出量，不然前端统计总量报错
				if(in_out)
				{
					detail=new RecordDataListDetail();
					detail.setData_key("inTake");
					detail.setData_value("");
					list_detail.add(detail);
				}
				recordDataList.setApprove_status(d_data.getApprove_status());
				recordDataList.setCreate_person(d_data.getCreate_person());
				recordDataList.setList_detail(list_detail);//存放记录集合，一条记录的所有键值对
			}
			if("1".equals(recordDataList.getIsHeader())) {
				headerRecord = new RecordDataList();
				headerRecord.setRecord_id(recordDataList.getRecord_id());
				headerRecord.setIsHeader(recordDataList.getIsHeader());
				headerRecord.setApprove_status(recordDataList.getApprove_status());
				headerRecord.setCreate_person(recordDataList.getCreate_person());
				headerRecord.setList_detail(recordDataList.getList_detail());
				continue;
			}
			list_result.add(recordDataList);
		}
		//把表头数据放在最后，方便前端进行获取
		if(null != headerRecord) {
			list_result.add(headerRecord);
		}

		if(flag){//没有数据时，显示一个空的东东
			list_result=new ArrayList();
		}
		return list_result;
	}

	/**
	 * 处理护理记录单的数据
	 * @param item_list
	 * @return
	 */
	private List<RecordDataList> dealHlList(List <DocDataRecord> list,String docType){
		List<RecordDataList> list_result = new ArrayList<RecordDataList>();

		//当天的总入量
		Map<String,BigDecimal> dayCountMap = new HashMap<String,BigDecimal>();
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		Date compleDate = null;
		Date lastDate = null;
		for(int i=0;i<list.size();i++){
			
			//当条记录
			DocDataRecord d_data = list.get(i);

			//获取第一天的前一天的出入量累计
			if(i==0 && "1".equals(docType)){
				dayCountMap = getInOutAccumulationForSpecifiedTime(d_data.getInpatient_no(),d_data.getTemplate_id(),
												null, DateUtil.addDate(d_data.getDateList(), -1),null);
			}
			
			//护理记录数据集合,用来放一条护理记录
			RecordDataList recordData = new RecordDataList();
			recordData.setRecord_id(d_data.getRecord_id());
			recordData.setApprove_status(d_data.getApprove_status());//审核状态
			recordData.setCreate_person(d_data.getCreate_person());//创建人
			
			//所有的数据，先查询出来再组装
			List<DocDataRecordItem> item_list = d_data.getList();
			//处理护理记录单列表的数据
			if(item_list!=null && item_list.size()>0){
				Map<String,Object> itemMap = new HashMap<String,Object>();
				for(DocDataRecordItem item : item_list){
					itemMap.put(item.getTemplate_item_id(), item.getRecord_value());
				}
				//获取记录时间
				if(null != itemMap.get("DATE_LIST") && null != itemMap.get("TIME_LIST")){
					String dateTime = itemMap.get("DATE_LIST") +" "+itemMap.get("TIME_LIST")+":00";
					try {
						Date nowTime = df.parse(dateTime);
						//如果比对时间为空，那么设置比对时间
						if(compleDate == null){
							//当天的07:00:00
							Calendar cal = Calendar.getInstance();
							cal.setTime(nowTime);
							int hour = cal.get(Calendar.HOUR_OF_DAY);
							if(hour>7){
								//如果已经超过7点，那么起始比对时间为第二天
								cal.add(Calendar.DATE, 1);
								dayCountMap.clear();
							}
							cal.set(Calendar.HOUR_OF_DAY, 7);
							cal.set(Calendar.MINUTE, 0);
							cal.set(Calendar.SECOND, 0);
							compleDate = cal.getTime();
							lastDate = nowTime;
						}
						//1、如果第一条的时间大于当天7点，那么比对时间+1天。2、如果当天时间大于比对时间，并且上一条小于等于比对时间，那么比对时间+1天
						if(nowTime.compareTo(compleDate)>0&&lastDate.compareTo(compleDate)<=0){
							//当天的07:00:00
							Calendar cal = Calendar.getInstance();
							cal.setTime(compleDate);
							cal.add(Calendar.DATE, 1);//加一天
							compleDate = cal.getTime();
							//清空上一天的数据
							dayCountMap.clear();
						}
						lastDate = nowTime;
					} catch (ParseException e) {
						LOGGER.error("",e);
					}
				}
				
				//单条记录的流水号
				List <RecordDataListDetail> list_detail = new ArrayList<RecordDataListDetail>();
				
				//一般护理记录中需要统计入量和出量，如果两者都为空，强行出入一个空的键值对
				boolean in_out = true;
				
				//循环出所有的键值对
				for(int j=0;j<item_list.size();j++){
					DocDataRecordItem item_data = item_list.get(j);
					String template_item_id = item_data.getTemplate_item_id();
					String recordValue = item_data.getRecord_value();
					//单条记录的所有选项和数据值
					RecordDataListDetail detail=new RecordDataListDetail();
					//只有等于0的才是护理记录的数据，不然是评估单数据
					if("0".equals(item_data.getParent_id())){
						//TITLE为动态列表表头信息
						if(!"TITLE_ONE".equals(template_item_id)&&!"TITLE_TWO".equals(template_item_id)
							&&!"TITLE_THREE".equals(template_item_id)&&!"TITLE_FOUR".equals(template_item_id)
							&&!"TITLE_FIVE".equals(template_item_id)){
							
							detail.setData_key(template_item_id);
							detail.setData_value(recordValue);
							
							//设置动态表头选项
							if("DATA_ONE".equals(template_item_id)||"DATA_TWO".equals(template_item_id)
								||"DATA_THREE".equals(template_item_id)||"DATA_FOUR".equals(template_item_id)
								||"DATA_FIVE".equals(template_item_id)){
								//把所有的选项查询出来
								HashMap<String,String> map = new HashMap<String,String>();
								map.put("data_key", template_item_id);
								detail.setList_select(reportMapper.list_select(map));
							}
							
							if("inTake".equals(detail.getData_key())){
								in_out = false;
								//统计总入量
								BigDecimal dayInTake = dayCountMap.get("inTake");
								
								if(!StringUtils.isEmpty(recordValue)){
									//如果为空，那么设置为0
									dayInTake = dayInTake ==null?BigDecimal.ZERO:dayInTake;
									dayInTake = dayInTake.add(new BigDecimal(recordValue));
									dayCountMap.put("inTake", dayInTake);
									detail.setView_value(StringUtil.getStringValue(new Double(recordValue)) +"/"+StringUtil.getStringValue(dayInTake.doubleValue()));
								}
							}
							if("outTake".equals(detail.getData_key())){
								in_out = false;
								String key = (String) itemMap.get("out_name");
								key = key == null ? "outTake" : ("outTake" +"-"+ key);
								//统计总出量
								BigDecimal dayOutTake = dayCountMap.get(key);
								
								if(!StringUtils.isEmpty(recordValue)){
									//如果为空，那么设置为0
									dayOutTake = dayOutTake ==null?BigDecimal.ZERO:dayOutTake;
									dayOutTake = dayOutTake.add(new BigDecimal(recordValue));
									dayCountMap.put(key, dayOutTake);
									detail.setView_value(StringUtil.getStringValue(new Double(recordValue)) +"/"+StringUtil.getStringValue(dayOutTake.doubleValue()));
									//detail.setView_value(recordValue+"/"+dayOutTake);
								}
							}
							list_detail.add(detail);//设置键值对数据项
						}
					}
				}
				//如果没有入量和出量，强行加入入量和出量，不然前端统计总量报错
				/*if(in_out){
					RecordDataListDetail detail=new RecordDataListDetail();
					detail.setData_key("inTake");
					detail.setData_value("");
					list_detail.add(detail);
				}*/
				recordData.setList_detail(list_detail);//存放记录集合，一条记录的所有键值对
			}
			//如果存在明细item项，那么当条添加
			if(recordData.getList_detail() !=null && !recordData.getList_detail().isEmpty()){
				list_result.add(recordData);
			}
		}
		return list_result;
	}

	/**
	 * 为指定的时间点获取出入量累计数据
	 * 1、累计值从前一个24h统计节点开始计算，直到到该条指定的记录或者时间
	 * 2、如果仅指定了日期，时间和记录ID为空，则计算指定日期当天，从第一个24h统计节点到当天结束（24:00）的累计值
	 * 3、如果同时指定了日期时间和记录ID，则计算从上一个24h统计节点开始到该条记录为止的累计值
	 * @param patID 患者住院号
	 * @param templateID 护记模板ID
	 * @param recordID 指定的记录ID，可为空
	 * @param date 指定的日期
	 * @param time 指定的时间
     * @return 出入量累计数据集合，入量的key以“inTake”表示，出量的key以“outTake-出量名称的metadata”表示
     */
    private Map<String,BigDecimal> getInOutAccumulationForSpecifiedTime(String patID, String templateID, String recordID,
																		String date, String time) {
		Map<String, BigDecimal> inOutCountInfo = new HashMap<>();
		if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID) || StringUtils.isEmpty(date)){
			return inOutCountInfo;
		}
		date = DateUtil.format(DateUtil.parse(date, DateUtil.DateFormat.YMD), DateUtil.DateFormat.YMD);
		if(null==date){
			return inOutCountInfo;
		}
		String endDate = null;
		if (time != null) {
			time = DateUtil.format(DateUtil.parse(time.substring(0,8), DateUtil.DateFormat.HMS), DateUtil.DateFormat.HMS);
			if(null==time){
				return inOutCountInfo;
			}
			Date dateTime = DateUtil.parse(time.substring(0, 5), DateUtil.DateFormat.HM);
			Date inoutTime = DateUtil.parse(DocPrintConstants.inOutTimeMorning, DateUtil.DateFormat.HM);
			if(null!=dateTime && null!=inoutTime && !dateTime.after(inoutTime)){
				endDate = date;
				date = DateUtil.addDate(date, -1);
			}
		}

		Map<String,Object> parm = new HashMap<String,Object>();
		parm.put("patId", patID);
		parm.put("templateId", templateID);
		parm.put("recordID", recordID);
		parm.put("dateList", date);
		parm.put("endDate", endDate);
		parm.put("timeList", time);
		//不要被下面函数的名字所迷惑，它其实获取的是指定时间点的出入量累计信息
		List<Map<String,Object>> countList=reportMapper.getBackDayInOut7(parm);
		if(countList != null){
            for(int j=0;j<countList.size();j++){
                Map<String,Object> inoutMap = countList.get(j);
                inOutCountInfo.put((String) inoutMap.get("templateItemId"), (BigDecimal) inoutMap.get("recordValue"));
            }
        }
		return inOutCountInfo;
	}

	/**
	 * 处理评估单数据
	 * @param list
	 * @return
	 */
	public List<RecordDataItemList> deal_item(List <DocDataRecord> list,String pat_id){
		
//		//created by qingzhi.liu 2016.04.15  护理记录单循环读取患者诊断和主诉数据
//		String inDiag = null;
//		String appeal=null;
//		if(list!=null
//				&&!StringUtils.isEmpty(pat_id)){//  主诉、入院诊断只取一次，不需要每条记录都查询
//			Patient p=patientService.getPatientByPatId(pat_id);
//			if(null!=p
//					&&!StringUtils.isEmpty(p.getInDiag())){
//				inDiag = p.getInDiag();
//			}
//
//			if(null!=p
//					&&!StringUtils.isEmpty(p.getAppeal())){
//				appeal = p.getAppeal();
//			}
//		}
//		//end by qingzhi.liu 2016.04.15  护理记录单循环读取患者诊断和主诉数据

		Map<String,String> infoMap = new HashMap<String, String>();
		List<InpatientInfo> patInfo = getBaseInfo(pat_id);
		//把数据格式转换为一个map，这样后续取值的时候不用再循环遍历了
		for(InpatientInfo info:patInfo) {
			infoMap.put(info.getKey(), info.getValue());
		}

		String inDiag = infoMap.get("PTNT_DIAGNOSE");
		String appeal = infoMap.get("DE04_01_119_00");
		
		
		List<RecordDataItemList> list_result=new ArrayList();
		for(int i=0,l=list.size();i<l;i++)
		{
			DocDataRecord d_data=list.get(i);
			List<DocDataRecordItem> item_list=d_data.getList();
			if(item_list!=null&&item_list.size()>0)
			{
				List<RecordDataItem> data_item= new ArrayList();
				RecordDataItemList recordDataItemList=new RecordDataItemList();
				recordDataItemList.setRecord_id(d_data.getRecord_id());//记录流水号
				recordDataItemList.setIsHeader(d_data.getIsHeader());
				recordDataItemList.setApprove_status(d_data.getApprove_status());//审签状态						
				
				RecordDataItem recordDataItem=null;
				for(int m=0,ll=item_list.size();m<ll;m++)
				{
					DocDataRecordItem temp_data=item_list.get(m);							
					recordDataItem=new RecordDataItem();
					//第一级节点的父节点为空
					if(temp_data.getParent_id()==null)
					{
						recordDataItem.setParent_id(null);
						recordDataItem.setRecord_id(temp_data.getRecord_id());
						recordDataItem.setRecord_item_id(temp_data.getRecord_item_id());
						recordDataItem.setRecord_value(temp_data.getRecord_value());
						recordDataItem.setTemplate_item_id(temp_data.getTemplate_item_id());
						//需要获取最新的诊断,主诉
						if(DocReportConstants.PAT_DIAGNOSE.equals(temp_data.getTemplate_item_id())
								||DocReportConstants.PAT_COMPLAINT.equals(temp_data.getTemplate_item_id()))
						{
							//Patient p=patientService.getPatientByPatId(pat_id);
							if(DocReportConstants.PAT_DIAGNOSE.equals(temp_data.getTemplate_item_id()))
							{//诊断
								if(!StringUtils.isEmpty(inDiag)){
									recordDataItem.setRecord_value(inDiag);
								}
//								//三院要求：诊断信息如果有多条，只显示前三条；
//								recordDataItem.setRecord_value(getPartOfDiagnoseInfo(recordDataItem.getRecord_value(),
//																					DocReportConstants.DIAGNOSE_NUM));
								//出院在诊断和主诉没有国
							}else if(DocReportConstants.PAT_COMPLAINT.equals(temp_data.getTemplate_item_id())){//主诉
								//if(p.getAppeal()!=null&&p.getAppeal().trim().length()!=0){
									recordDataItem.setRecord_value(appeal);
								//}
							}
							
						}

						//bug9395, 如果没有患者信息，则填充之
						if(null != infoMap && StringUtils.isEmpty(recordDataItem.getRecord_value())){
							recordDataItem.setRecord_value(infoMap.get(recordDataItem.getTemplate_item_id()));
						}
						//TODO 三院要求：入院日期只要日期，不要时间
						/*if(DocReportConstants.ADMISSION_TIME.equals(recordDataItem.getTemplate_item_id())){
							Date inDate = DateUtil.parse(recordDataItem.getRecord_value().substring(0,10), DateUtil.DateFormat.YMD);
							recordDataItem.setRecord_value(DateUtil.format(inDate, DateUtil.DateFormat.YMD));
						}*/

						recordDataItem.setList_item(new ArrayList());//
						data_item.add(recordDataItem);
					}
					else//父节点不为空，则把数据拼接为父节点的一个子节点
					{
						//递归处理相关数据
						data_item=deal_data(data_item,temp_data);
					}
					recordDataItemList.setApprove_status(d_data.getApprove_status());//审核状态
					recordDataItemList.setList_item(data_item);
				}
				list_result.add(recordDataItemList);
			}		
		}		
		return list_result;
	}
	
	/**
	 * 处理动态列表数据
	 * @param list
	 * @return
	 */
	public List<RecordDataListDetail> deal_dyna(List <DocDataRecord> list){
		List<RecordDataListDetail> list_result=new ArrayList();
		if(list!=null&&list.size()>0)
		{
		
			//暂时先定三个动态列
			boolean title1=false;
			boolean title2=false;
			boolean title3=false;			
			
			for(int l=0;l<list.size();l++)
			{
				//处理动态列表
				DocDataRecord dyna_record=list.get(l);//获取最新的一条记录
				List<DocDataRecordItem> dyna_item_list=dyna_record.getList();
				List<RecordDataListDetail> dyna_list=new ArrayList();
				RecordDataListDetail detail=null;
				
				if(dyna_item_list!=null&&dyna_item_list.size()>0)
				{
					HashMap<String,String> map=new HashMap();
					for(int n=0,ll=dyna_item_list.size();n<ll;n++){
						DocDataRecordItem temp_data=dyna_item_list.get(n);
						if(
						("TITLE_ONE".equals(temp_data.getTemplate_item_id())&&!title1)
						&&(!"null".equals(temp_data.getRecord_value()))
						&&(temp_data.getRecord_value()!=null&&temp_data.getRecord_value().trim().length()>0)
						){
							title1=true;
							detail=new RecordDataListDetail();
							detail.setData_key(temp_data.getTemplate_item_id());
							detail.setData_value(temp_data.getRecord_value());
							detail.setList_select(null);
							list_result.add(detail);
						}

						if(
						("TITLE_TWO".equals(temp_data.getTemplate_item_id())&&!title2
								&&(!"null".equals(temp_data.getRecord_value()))
						)&&(temp_data.getRecord_value()!=null&&temp_data.getRecord_value().trim().length()>0)
						){
							title2=true;
							detail=new RecordDataListDetail();
							detail.setData_key(temp_data.getTemplate_item_id());
							detail.setData_value(temp_data.getRecord_value());
							detail.setList_select(null);
							list_result.add(detail);
						}
						
						if(
						(
						"TITLE_THREE".equals(temp_data.getTemplate_item_id())&&!title3
						&&(!"null".equals(temp_data.getRecord_value()))
						)&&(temp_data.getRecord_value()!=null&&temp_data.getRecord_value().trim().length()>0)
						){
							title3=true;
							detail=new RecordDataListDetail();
							detail.setData_key(temp_data.getTemplate_item_id());
							detail.setData_value(temp_data.getRecord_value());
							detail.setList_select(null);
							list_result.add(detail);
						}		
						if(title1&&title2&&title3)
						{
							l=list.size()+10;
						}
						
					}
				}				
			}		
		}		
		return list_result;
	}
	
	/**
	 * 递归处理记录
	 * @param data_item
	 * @return
	 */
	public List<RecordDataItem> deal_data(List<RecordDataItem> list,DocDataRecordItem data_item){
		if(list!=null&&list.size()>0)
		{
			for(int i=0,l=list.size();i<l;i++)
			{
				RecordDataItem temp_data=list.get(i);
				//追加字节点
				if(data_item.getParent_id().equals(temp_data.getRecord_item_id())){
					RecordDataItem t=new RecordDataItem();
					t.setParent_id(data_item.getParent_id());
					t.setRecord_id(data_item.getRecord_item_id());
					t.setRecord_item_id(data_item.getRecord_item_id());
					t.setRecord_value(data_item.getRecord_value());
					t.setTemplate_item_id(data_item.getTemplate_item_id());
					t.setList_item(new ArrayList());
					
					List<RecordDataItem> list_item=temp_data.getList_item();
					list_item.add(t);
					temp_data.setList_item(list_item);
					list.set(i, temp_data);
				}else{
					List<RecordDataItem> list_item=temp_data.getList_item();
					deal_data(list_item,data_item);
				}
			}
		}
		return list;
	}
    
	/**
	 * 加载模板数据
	 */
	public BaseListVo getTempDatas2(DocRecord docRecord) {
		BaseListVo vo = new BaseListVo();
		vo.setRslt(ResultCst.SUCCESS);
		List<DocRecord> tmps = new ArrayList<DocRecord>();
		DocRecord doc = null;
		try {
			doc = reportMapper.getTempDatas(docRecord);
			//前端打印时需要所有的诊断信息，但是NDA又不愿改动接口，只好单独做一个了
			if(doc!=null&&doc.getData_item_list()!=null&&doc.getData_item_list().size()>0){
				doc.setData_item(doc.getData_item_list().get(doc.getData_item_list().size()-1).getList_item());
			}
			if(doc.getData_item()==null){
				doc.setData_item(new ArrayList());
			}
			tmps.add(doc);
			
			if(null==doc.getData_item()
					||doc.getData_item().size()<1){  //
				//查询模板是否包涵生命体征、专科评估等元素
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("template_id", docRecord.getTemplate_id());
				map.put("type", "1");
				int isHaven = reportMapper.getSpecials(map);  //获取特殊元素
				
				List<SpecialVo> specList = null;
				//判断是否有生命体征元素
				if(isHaven>0){  
					//调用生命体征接口
					specList = getSpecials(docRecord.getInpatient_no());
				}
				
				map.put("type", "2");
				isHaven = reportMapper.getSpecials(map);  //获取特殊元素
				//判断是否有专科评估元素
				if(isHaven>0){
					//调用专科评估
				}
				
				doc.setSpecList(specList);
			}
			vo.setData(tmps);
			vo.setRslt(ResultCst.SUCCESS);			
			
		} catch (Exception e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("数据查询错误！");
			LOGGER.error(e.getMessage());
		}
		return vo;
	}

	/**
	 * 获取特殊数据源  体征、专科评估
	 * 调用pda录入体征数据
	 * @return
	 */
	public List<SpecialVo> getSpecials(String pat_id){
		List<SpecialVo> specList =bodySignService.getFirstBodySigns(pat_id);
		return specList;
		
	}

	/**
	 * 动态查询数据源选项
	 */
	public BaseListVo getDynaItemData(String checkbox_code,String type){
		DynaMetadata m=new DynaMetadata();
		m.setCheckbox_code(checkbox_code);
		BaseListVo vo = new BaseListVo();
		try{
			List <DynaMetadata> list=reportMapper.getDynaItemData(m);
			
			//为了与前端的命名统一，把名字转换一下
			if(DocReportConstants.DATA_TYPE_OPT.equals(type)){
				if(list!=null&&list.size()>0){
					List <DynaMetadata2> list2=new ArrayList();
					DynaMetadata2 m2=null;
					for(int i=0;i<list.size();i++){
						m2=new DynaMetadata2();
						m2.setCode(list.get(i).getCheckbox_code());
						m2.setValue(list.get(i).getCheckbox_name());
						m2.setOption_ord(list.get(i).getCheckbox_ord());
						list2.add(m2);
					}
					vo.setData(list2);
				}
			}else{
				vo.setData(reportMapper.getDynaItemData(m));
			}
			vo.setRslt(ResultCst.SUCCESS);
		}catch(Exception e){
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("查询错误");
		}
		return vo;
	}
	 
	/**
	 * 获取所有科室，用于上传模板
	 */
	public BaseListVo queryComWard(){
		BaseListVo vo = new BaseListVo();
		try{
			vo.setData(reportMapper.queryComWard());
			vo.setRslt(ResultCst.SUCCESS);
		}catch(Exception e){
			vo.setRslt(ResultCst.FAILURE);
		}
		return vo;
	}
	
	/**
	 * 审核
	 */
	@Transactional(value = "transactionManager")
	public BaseListVo approve(DocApprove data){
		BaseListVo vo = new BaseListVo();
		try{
			
			String record_id="";
			if(data.getList()!=null&&data.getList().size()>0)
			{
				for(int i=0;i<data.getList().size();i++)
				{
					record_id+="'"+data.getList().get(i).getRecord_id()+"',";	
				}
				record_id=record_id.substring(0,record_id.lastIndexOf(","));
			}
			/*Map map=new HashMap();
			map.put("record_id", record_id);
			map.put("approve_person", data.getApprove_person());
			if(reportMapper.isSamePerson(map)>0){
				vo.setRslt(ResultCst.FAILURE);
				vo.setMsg("审核人与责任护士不能为同一个人");
				return vo;
			}*/
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			data.setNow_time(df.format(new Date()));
			reportMapper.approve(data);
			reportMapper.approve_person(data);
			reportMapper.approve_time(data);
			vo.setRslt(ResultCst.SUCCESS);
		}catch(Exception e){
			vo.setRslt(ResultCst.FAILURE);
			e.printStackTrace();
		}finally{
			return vo;
		}
	}	
	
	/**
	 * 拷贝图片
	 * @param s
	 * @param t
	 */
	 public void copyFile(String source, String to) {

	        FileInputStream fi = null;

	        FileOutputStream fo = null;

	        FileChannel in = null;

	        FileChannel out = null;

	        try {
	        	System.out.println("source:"+source);
	            fi = new FileInputStream(new File(source));

	            fo = new FileOutputStream(new File(to));
	            in = fi.getChannel();//得到对应的文件通道

	            out = fo.getChannel();//得到对应的文件通道

	            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

	        } catch (IOException e) {

	            e.printStackTrace();

	        } finally {

	            try {
	            	if(fi!=null){
	            		fi.close();
	            	}
	                if(in!=null){
	                	in.close();
	                }
	                if(fo!=null){
	                	fo.close();
	                }
	                if(out!=null){
	                	out.close();
	                }
	            } catch (IOException e) {

	                e.printStackTrace();

	            }

	        }

	    }

	/**
	 * 转抄文书记录的数据有效性校验
	 * @param d
	 * @throws MnisException
	 */
	private void checkRecordValidation(DocRecord d) throws MnisException {
		if(d.getCreateTime()==null) {
			throw new MnisException("文书记录创建时间为空");
		}
		if(StringUtils.isEmpty(d.getCreate_person())){
			throw new MnisException("文书记录创建人为空");
		}
		if(StringUtils.isEmpty(d.getInpatient_no())){
			throw new MnisException("文书记录病人编号为空");
		}
		String listdate = d.getDate_list();
		if(StringUtils.isEmpty(listdate)){
			throw new MnisException("文书记录日期为空");
		}
		//校验日期是否合法
		try{
			Date date = DateUtil.parse(listdate, DateUtil.DateFormat.YMD);
			String formatDate = DateUtil.format(date, DateUtil.DateFormat.YMD);
			if(null == date	|| null == formatDate || !DateUtil.isValidDate(formatDate)) {
				throw new MnisException("文书记录日期不合法");
			}
			else{
				d.setDate_list(formatDate);
			}
		}catch (Exception e){
			throw new MnisException("文书记录时间不合法");
		}

		String listtime = d.getTime_list();
		if(StringUtils.isEmpty(listtime)){
			throw new MnisException("文书记录时间为空");
		}
		if(listtime.length()==5){
			listtime += ":00";
		}
		try{
			Date time = DateUtil.parse(listtime, DateUtil.DateFormat.HMS);
			String formattime = DateUtil.format(time, DateUtil.DateFormat.HMS);
			if(null == time || null == formattime || !DateUtil.isValidTime(formattime)) {
				throw new MnisException("文书记录时间不合法");
			}
			else {
				d.setTime_list(formattime);
			}
		}catch (Exception e) {
			throw new MnisException("文书记录时间不合法");
		}

		if(d.getData_item()==null||d.getData_item().size()==0){
			throw new MnisException("文书记录数据为空");
		}
	}

	/**
	 * 获取默认护理记录单模板ID
	 * @param patID
	 * @param deptID
	 * @return 模板ID
	 * @throws MnisException
	 */
	private String getDefaultTemplateID(String patID, String deptID) throws MnisException {
		//新增默认护理记录单功能，查询用户或科室的默认护理记录单作为模板ID
		//by renjunming, 2016-05-18
		String template_id;
		if(StringUtils.isEmpty(patID) && StringUtils.isEmpty(deptID)){
			throw new MnisException("病人编号和部门编号都为空");
		}else {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("patID", patID);
			paramMap.put("deptCode", deptID);
			List<PatientDefaultTemplate> listTemp = reportMapper.getPatientDefaultTemplate(paramMap);
			if(0<listTemp.size()){
				//如果存在通常只有一条记录
				template_id = listTemp.get(0).getTemplateID();
			} else{
				//如果患者不存在默认护理记录单，则使用科室默认模板
				paramMap.put("patID", deptID);
				listTemp = reportMapper.getPatientDefaultTemplate(paramMap);
				if(0<listTemp.size()){
					template_id = listTemp.get(0).getTemplateID();
				}else{
					//没有默认模板，查询科室的第一个护记模板
					template_id = reportMapper.getTemplateIdByDeptcode(deptID);
					if(StringUtils.isEmpty(template_id)){
						throw new MnisException("无法获取到模板ID");
					}
				}
			}
		}

		return template_id;
	}

	/**
	 * 保存前预处理转抄的文书记录
	 * @param d
	 */
	private void ProcessCopyRecordBeforeSave(DocRecord d){
		List<RecordDataItem> itemList = d.getData_item();
		//1、设置护理记录单数据的parent_id为0，因为后面会据此判断……这是个坑……
		for(RecordDataItem listItem : itemList){
			listItem.setParent_id("0");
		}
		//2、文书转抄信息中添加必要信息
		//设置记录日期
		RecordDataItem item  = new RecordDataItem();
		item.setTemplate_item_id(DocReportConstants.DATE_LIST);
		String date = DateUtil.format(DateUtil.parse(d.getDate_list(), DateUtil.DateFormat.YMD), DateUtil.DateFormat.YMD);
		item.setRecord_value(date);
		item.setParent_id("0");
		itemList.add(item);
		//设置记录时间
		item  = new RecordDataItem();
		item.setTemplate_item_id(DocReportConstants.TIME_LIST);
		String time = DateUtil.format(DateUtil.parse(d.getTime_list(), DateUtil.DateFormat.HMS), DateUtil.DateFormat.HM);
		item.setRecord_value(time);
		item.setParent_id("0");
		itemList.add(item);
		//设置签名
		item  = new RecordDataItem();
		item.setTemplate_item_id(DocReportConstants.CREATE_PERSON);
		item.setRecord_value(d.getCreate_person());
		item.setParent_id("0");
		itemList.add(item);
	}

	/**
	 * 体征，医嘱与文书接口
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String save_mnis_info(DocRecord d) throws MnisException {
		//为了防止客户端执行医嘱时重复请求多次造成多条重复记录的情况，
		//转抄数据在处理之前先判断一下是否已经有记录；
		//by renjunming, 2016-05-19
		String orderCode = d.getOrder_code();
		if(!StringUtils.isEmpty(orderCode)){
			//是执行医嘱转抄过来的记录
			Integer recordCount = reportMapper.getRecordCountByOrdercode(orderCode);
			if(0<recordCount) {
				throw new MnisException("重复的医嘱执行记录");
			}
		}

		//转抄记录的数据校验
		checkRecordValidation(d);

		//记录日志
		LOGGER.info("转抄记录，病人："+d.getInpatient_no()+
				"记录时间："+d.getDate_list()+" "+d.getTime_list()+
				"创建人："+d.getCreate_person()+
				"创建时间："+d.getCreateTime());

		//如果未指定模板ID，需要获取默认模板
		if(StringUtils.isEmpty(d.getTemplate_id())){
			d.setTemplate_id(getDefaultTemplateID(d.getInpatient_no(), d.getDept_code()));
		}

		//相同时间点的不同记录，可以合并成一条
		if(MergeRecords(d)){
			return "Y";
		}

		//不能合并的，直接保存
		ProcessCopyRecordBeforeSave(d);
		try{
			saveData(d, true, d.getDept_code(), true);
		}catch(Exception e){
			throw new MnisException(e.getMessage());
		}
		return "Y";
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED )
	public void saveMnisInfo(DocReportEventEntity docReportEventEntity) {
		//先记录一下调用行为
		if(DocPrintConstants.bRecordAction) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("DocReportEventEntity", docReportEventEntity);
			String paramStr = GsonUtils.toJson(paramMap);
			String createPerson = docReportEventEntity.getCreate_person();
			recordAction(paramStr, createPerson);
		}

		DocRecordFailInfo info = new DocRecordFailInfo();
		DocRecord record = new DocRecord();
		try {
			record = copyDocReportEventToDocRecord(docReportEventEntity);
			record.setIsHeader("0");//文书记录信息，非表头
			save_mnis_info(record);
			return;//转抄成功，直接返回
		} catch (MnisException e) {
			info.setReason(e.getMessage());
		} catch (Exception e) {
			info.setReason(e.getMessage());
		}
		//设置转抄记录信息
		info.setPatID(record.getInpatient_no());
		info.setDeptCode(record.getDept_code());
		info.setCreatePerson(record.getCreate_person());
		info.setDateList(record.getDate_list());
		info.setTimeList(record.getTime_list());
		info.setBarcode(record.getOrder_code());
		//保存到数据库
		if (StringUtils.isEmpty(info.getReason())) {
			info.setReason("Unknown");
		}
		reportMapper.saveFailRecordInfo(info);
	}


	/**
	 * 合并转抄记录
	 * @param  record 文书记录
	 * @return false 未合并; true 已合并
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean MergeRecords(DocRecord record){
		boolean result = false;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("date_list", record.getDate_list());
		map.put("time_list", record.getTime_list());
		map.put("template_id", record.getTemplate_id());
		map.put("inpatient_no", record.getInpatient_no());
		List<String> recordIDs=reportMapper.getRecordIDsByTime(map);
		//该时间点有相同的记录
		if(null!=recordIDs && 0<recordIDs.size()) {
			List<RecordDataItem> dataItems = record.getData_item();
			List<String> itemIDs = new ArrayList<String>();
			for (RecordDataItem item : dataItems) {
				itemIDs.add(item.getTemplate_item_id());
			}
			Map<String,Object> recordMap=new HashMap<String,Object>();
			recordMap.put("itemIDs", itemIDs);
			for (String recordID : recordIDs) {
				recordMap.put("recordID", recordID);
				int count = reportMapper.getSameItemCountInRecord(recordMap);
				//没有找到相同的元素，说明可以合并
				if (count < 1) {
					boolean inout = false;
					final DocRecord docRecord = reportMapper.getDocRecordByID(recordID);
					List<DocDataRecordItem> docDataRecordItems = reportMapper.getDocDataRecordItems(recordID);
					record.setRecordId(recordID);
					for(RecordDataItem item : dataItems) {
						//插入数据
						item.setRecord_id(recordID);
						item.setParent_id("0");
						saveRecordItem(item);

						//判断是否需要统计出入量
						if(ReportConstants.IN_TAKE.equals(item.getTemplate_item_id())
								|| ReportConstants.OUT_TAKE.equals(item.getTemplate_item_id())){
							inout = true;
						}
					}

					//处理出入量统计信息
					Map<String, Object> maps = new HashMap<String, Object>();
					if(inout && hasInOutOrderForPatient(record.getInpatient_no())){
						//注意：这里的recod信息是不全的，下面函数处理时会重新去查询信息
						processDocReportStatistic(record,record.getDept_code(),record.getTemplate_id(),record.getDate_list(),
													record.getTime_list(),docDataRecordItems);

					String[] times = getStatisticTiems(record.getTime_list());
					maps.put(ReportConstants.TIME_LIST, times[0]);
					maps.put("docStatistic", showDocRecordDataStatistic(
							getDocReprotDataStatistic(record.getInpatient_no(), record.getDept_code(), record.getTemplate_id(),
														record.getDate_list(), times[0]),
							times[1],times[2],null));
					}

					//异步处理打印信息：计算行高和页码
					try {
						dealPrintInfoForRecordAndStatictics(record, (DocRecordDataStatistic) maps.get("docStatistic"));
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.error(e.getMessage(), "");
					}

					result = true;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 将事件监听实体转为护理文书实体
	 * @param docReportEventEntity
	 * @return
	 */
	private DocRecord copyDocReportEventToDocRecord(DocReportEventEntity docReportEventEntity){
		if(null == docReportEventEntity 
				|| null == docReportEventEntity.getData_item()
				|| docReportEventEntity.getData_item().size() == 0){
			return null;
		}
		DocRecord docRecord = new DocRecord();
		docRecord.setCreate_person(docReportEventEntity.getCreate_person());
		docRecord.setCreateTime(docReportEventEntity.getCreateTime());
		docRecord.setInpatient_no(docReportEventEntity.getInpatient_no());
		docRecord.setDept_code(docReportEventEntity.getDept_code());
		docRecord.setOrder_code(docReportEventEntity.getBarcode());
		docRecord.setDate_list(docReportEventEntity.getDate_list());
		docRecord.setTime_list(docReportEventEntity.getTime_list());
		List<RecordDataItem> recordDataItems = new ArrayList<RecordDataItem>();
		for (DocReportEventItemEntity docReportEventItemEntity : docReportEventEntity.getData_item()) {
			RecordDataItem recordDataItem = new RecordDataItem();
			
			String value=docReportEventItemEntity.getRecord_value();
			//出量名称需要转换
			if(DocReportConstants.OUT_NAME.equals(docReportEventItemEntity.getTemplate_item_id()))
			{
				Properties prop = PropertiesUtils.getProperties(ReportConstants.REPORT_TRANS_PORP_PATH); 
		        try {   
		        	value= prop.getProperty(value);
		        	if(value==null||value.trim().length()==0)
		        	{
		        		value=docReportEventItemEntity.getRecord_value();
		        	}
		        } catch (Exception e) {   
		            e.printStackTrace();   
		        }					
			}
			recordDataItem.setTemplate_item_id(docReportEventItemEntity.getTemplate_item_id());
			recordDataItem.setRecord_value(value);
			recordDataItems.add(recordDataItem);
		}
		docRecord.setData_item(recordDataItems);
		return docRecord;
		
	}
	
	/**
	 * 高责医生，护士长查房记录
	 * @param inpatient_no
	 * @param create_person
	 */
	public String nurseRounds(String type,String inpatient_no,String create_person,
							  String date_list,String time_list, String template_id){
		DocRecord d=new DocRecord();
		 d.setCreateTime(new Date());
		 d.setCreate_person(create_person);
		 d.setDate_list(date_list);
		 d.setTime_list(time_list);
		 d.setInpatient_no(inpatient_no);
		 List<RecordDataItem> t_list=new ArrayList<RecordDataItem>();
		 RecordDataItem tt=new RecordDataItem();
		tt.setTemplate_item_id(DocReportConstants.REMARK);//入量
		 if("1".equals(type)){
			 tt.setRecord_value("高责查房");
		 }else{
			 tt.setRecord_value("护士长查房");
		 }
		 
		 t_list.add(tt);

		//没有护理记录单时不需要处理
		 if(template_id!=null&&template_id.trim().length()>0){
			 d.setTemplate_id(template_id);
		 }
		 d.setData_item(t_list);
		 String return_flag="";
		 try{
			 return_flag=save_mnis_info(d);
		 }catch (MnisException e){
			 return_flag = "N";
			 e.printStackTrace();
		 }
		 catch(Exception e){
			 return_flag="N";
			 e.printStackTrace();
		 }
		 return return_flag;
	}
	
	/**
	 * 处理一些特殊的文书，如需要在床位栏显示防跌，防压疮，需要频次提醒等
	 * @param record
	 */
	public void deal_pat_info_stat(DocRecord record){
		try {
			
			Map<String,String> map=new HashMap();
			map.put("template_id", record.getTemplate_id());
			List<DocConfig> list_config= get_docConfig(map);
			//如果没有配置相关信息，则直接返回。
			if(list_config==null||list_config.size()==0)
			{
				return ;
			}			
			
			String date_list=null;//日期
			String time_list=null;//时间
			//获取文书记录的日期和时间
			if(record!=null&&record.getData_list()!=null&&record.getData_list().size()>0){
				RecordDataList dl=record.getData_list().get(0);
				List <RecordDataListDetail> detail=dl.getList_detail();
				if(detail!=null&&detail.size()>0){
					for(int i=0;i<detail.size();i++){
						if("DATE_LIST".equals(detail.get(i).getData_key())){
							date_list=detail.get(i).getData_value();
						}
						if("TIME_LIST".equals(detail.get(i).getData_key())){
							time_list=detail.get(i).getData_value();
						}
					}
				}
			}
			
			Map<String,String> map_time=new HashMap();
			if(time_list==null||time_list.trim().length()==0)
			{
				time_list=date_list;
			}
			else
			{
				time_list=date_list+" "+time_list;
			}
			map_time.put("time_list", time_list);
			map_time.put("template_id", record.getTemplate_id());
			map_time.put("inpatient_no", record.getInpatient_no());
			//如果有大于该时间段的记录，则无需处理
			if(reportMapper.queryReportByTime(map_time)>0)
			{
				return ;
			}
			if(null!=record.getData_list()){
				for(Iterator<RecordDataList> iterator=record.getData_list().iterator();iterator.hasNext();){
					RecordDataList bean=iterator.next();
					bean.setRecord_id(record.getRecordId());
					List<RecordDataListDetail> list_detail=bean.getList_detail();
					if(list_detail!=null&&list_detail.size()>0){
						RecordDataListDetail detail=null;
						//递归文书每一个元素
						for(int i=0,l=list_detail.size();i<l;i++){
							detail=list_detail.get(i);
							String key=detail.getData_key();
							String value=detail.getData_value();
							
							boolean frequency_flag=true;//频次提醒标识
							boolean show_flag=true;//床位栏显示标识
							
							for(int c=0;c<list_config.size();c++)
							{
								
								DocConfig dc=list_config.get(c);
								//与配置信息的字段值相同
								if(key.equals(dc.getRef_column()))
								{
									if(value!=null&&value.trim().length()>0)
									{
										int tri_min=dc.getTri_grade_min();
										int tri_max=dc.getTri_grade_max();
										//在床位栏展示
										if("0".equals(dc.getConfig_type())&&show_flag)
										{
											//设置标识，下次不用再二次循环  是否在床位栏展示，对于一种类型的文书，应该只有一条数据
											show_flag=false;
											
											String val="0";//无需显示
											//如果值大于或等于触发条件
											if(tri_max>=Integer.parseInt(value)&&Integer.parseInt(value)>=tri_min)
											{
												val="1";//需要显示相关标识(防跌，防压疮)
											}
											Map<String,String> map_parm=new HashMap();
											map_parm.put("pat_id", "'"+record.getInpatient_no()+"'");
											map_parm.put("column", key);
											map_parm.put("val","'"+val+"'");
											int num=reportMapper.updatePatInfoStat(map_parm);
											//没有数据，则插入一条
											if(num==0){
												reportMapper.insertPatInfoStat(map_parm);
											}
										}	
										//频次提醒
										if("1".equals(dc.getConfig_type())&&frequency_flag)
										{
											//判断是否在配置范围内
											if(tri_max>=Integer.parseInt(value)&&Integer.parseInt(value)>=tri_min)
											{
												Calendar calendar_start = Calendar.getInstance();
												calendar_start.set(Calendar.HOUR_OF_DAY, calendar_start.get(Calendar.HOUR_OF_DAY) + tri_min);
												Date date_start=calendar_start.getTime();//开始时间
												
												Calendar calendar_end = Calendar.getInstance();
												calendar_end.set(Calendar.HOUR_OF_DAY, calendar_end.get(Calendar.HOUR_OF_DAY) + tri_max);
												Date date_end  = calendar_end.getTime();//结束时间
												
												String tri_content=dc.getTri_content();//提示的内容
												frequency_flag=false;
												
											}											
										}
									}
									//床位栏展示和频次列表都有的时候,返回
									if(!frequency_flag&&!show_flag)
									{
										return;
									}
								}
							}
						}
					}

				}			
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
	
	/**
	 * 获取护理文书配置信息
	 * @param doc_type
	 * @return
	 */
	public List<DocConfig> get_docConfig(Map<String,String> map){
		return reportMapper.get_docConfig(map);
	}
	
	/**
	 * 更新防跌，防压疮标志 
	 * @param map
	 */
	public void updatePatInfoStat(Map<String,String> map){
		reportMapper.updatePatInfoStat(map);
	}
	
	/**
	 * 计算24小时出入量和8小时出入量
	 * @param data_list 原数据
	 * @param morning_time 早上统计的时间
	 * @param afternoon_time 下午统计的时间
	 * @param record_id 记录id（如果有记录的id，说明是单点一条记录进来，不需要再计算）
	 * @param date_time 记录时间（如果时间，说明是点击一天的时间进来，则计算前一天的的总量）
	 * @return
	 */
	public List<RecordDataList> dealInOutTake(List<RecordDataList> data_list,DocRecord docRecord,String morning_time,String afternoon_time)throws Exception {
		//如果有id则表示只有一条记录，直接返回原来数据，不需要统计
		if(docRecord.getRecordId()!=null&&docRecord.getRecordId().trim().length()>0)
		{
			return data_list;
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<RecordDataList> list_result=new ArrayList();
		
		//如果有日期，需要计算前一天的数据
		if(docRecord.getDate_list()!=null&&docRecord.getDate_list().trim().length()>0)
		{
			RecordDataList recordDataList=null;
	
			//递归处理数据
			int data_length=data_list.size();
			for(int i=0;i<data_length;i++)
			{
				RecordDataList rd_list=data_list.get(i);
				List<RecordDataListDetail> detail_list=rd_list.getList_detail();
				
				//该集合里面，第一条记录是日期，第二条记录是时间
				String date=detail_list.get(0).getData_value();
				String time=detail_list.get(1).getData_value();
							
				//获取当前日期的前一天(格式为年月日)
			    String date_before=new ReportUtil().getSpecifiedDayBefore(date,"yyyy-mm-dd");
				try
				{				   
				    //上午统计的时间（三院为三点）
				    String morning=date+" "+morning_time;
				    String show_str=getDataBefore(docRecord.getTemplate_id(),docRecord.getInpatient_no(),date_before+" "+morning_time,morning);
					
				    //如果只有一条记录
				    if(data_length==1)
					{
						int date_before_after=new ReportUtil().compare_date(date+" "+morning_time, date+" "+time);
						//如果上午统计时间小于当前记录的时间，在前面插入一条记录
						if(date_before_after==1)
						{
							//插入一条上午统计的记录
							recordDataList=setRecordData(date,morning_time,show_str.toString());
					    	list_result.add(recordDataList);
					    	
					    	//放回非统计记录
					    	list_result.add(data_list.get(i));	
						}
						else
						{	
							date_before_after=new ReportUtil().compare_date(date+" "+afternoon_time, date+" "+time);
							//如果下午统计时间小于当前时间，在前面插入一条记录
							if(date_before_after==1)
							{
								//插入一条上午统计的记录
								recordDataList=setRecordData(date,afternoon_time,show_str.toString());
						    	list_result.add(recordDataList);		
						    	//放回非统计记录
						    	list_result.add(data_list.get(i));	
						    	
							}else{
						    	//放回非统计记录，在后面插入一条记录
						    	list_result.add(data_list.get(i));	
								
								show_str=getDataBefore(docRecord.getTemplate_id(),docRecord.getInpatient_no(),date_before,(date+""+time));
								recordDataList=setRecordData(date,afternoon_time,show_str.toString());
						    	list_result.add(recordDataList);
							}
						}
					}else{
					    //如果是第一条，则判断一下是否大于统计时间
						if(i==0)
						{
							int date_before_after=new ReportUtil().compare_date(date+" "+morning_time, date+" "+time);
							/**
							 * 第一条，只判断统计时间是否小于当前数据记录的时间（上午时间），小于上午的时间，也必然小于下午的时间
							 */
							if(date_before_after==1)
							{
								//插入一条上午统计的记录
								recordDataList=setRecordData(date,morning_time,show_str.toString());
						    	list_result.add(recordDataList);
						    	
						    	//放回非统计记录
						    	list_result.add(data_list.get(i));	
							}
							
						}
						//最后一条数据
						else if(i==(data_length-1))
						{
							int date_before_after=new ReportUtil().compare_date(date+" "+morning_time, date+" "+time);
							//如果上午统计时间小于当前记录的时间，在前面插入一条记录
							if(date_before_after==1)
							{
								//插入一条上午统计的记录
								recordDataList=setRecordData(date,morning_time,show_str.toString());
						    	list_result.add(recordDataList);
						    	
						    	//放回非统计记录
						    	list_result.add(data_list.get(i));	
							}
							else
							{	
								date_before_after=new ReportUtil().compare_date(date+" "+afternoon_time, date+" "+time);
								//如果下午统计时间小于当前时间，在前面插入一条记录
								if(date_before_after==1)
								{
									//插入一条上午统计的记录
									recordDataList=setRecordData(date,afternoon_time,show_str.toString());
							    	list_result.add(recordDataList);		
							    	//放回非统计记录
							    	list_result.add(data_list.get(i));	
							    	
								}else{
							    	//放回非统计记录，在后面插入一条记录
							    	list_result.add(data_list.get(i));	
									
									show_str=getDataBefore(docRecord.getTemplate_id(),docRecord.getInpatient_no(),date_before,(date+""+time));
									recordDataList=setRecordData(date,afternoon_time,show_str.toString());
							    	list_result.add(recordDataList);
								}
							}
						}
						//不是第一条，也不是最后一条，那么就取后一条数据进行判断
						else
						{
							rd_list=data_list.get(i+1);
							detail_list=rd_list.getList_detail();
							//该集合里面，第一条记录是日期，第二条记录是时间
							date=detail_list.get(0).getData_value();
							time=detail_list.get(1).getData_value();
							//获取当前日期的前一天(格式为年月日)
						    date_before=new ReportUtil().getSpecifiedDayBefore(date,"yyyy-mm-dd");	

							int date_before_after=new ReportUtil().compare_date(date+" "+morning_time, date+" "+time);
							//如果上午统计时间小于当前记录的时间，在前面插入一条记录
							if(date_before_after==1)
							{
								//插入一条上午统计的记录
								recordDataList=setRecordData(date,morning_time,show_str.toString());
						    	list_result.add(recordDataList);
						    	
						    	//放回非统计记录
						    	list_result.add(data_list.get(i));	
							}else{
								date_before_after=new ReportUtil().compare_date(date+" "+afternoon_time, date+" "+time);
								//如果下午统计时间小于当前时间，在前面插入一条记录
								if(date_before_after==1)
								{
									//插入一条上午统计的记录
									recordDataList=setRecordData(date,afternoon_time,show_str.toString());
							    	list_result.add(recordDataList);		
							    	//放回非统计记录
							    	list_result.add(data_list.get(i));	
							    	
								}								
							}
						}			
					}

				}
				catch (Exception e)
				{
				}				
				
			}
		}
		//如果没有记录id，也没有时间，那么久是点击模板的时间进来的，需要分开不同的方法统计
		else
		{
			if(data_list!=null&&data_list.size()>0)
			{
				
				//所有的日期集合
				List <String> date_list=new ArrayList();
				StringBuffer dates=new StringBuffer();
				for(int i=0;i<data_list.size();i++)
				{
					RecordDataList rd_list=data_list.get(i);
					List<RecordDataListDetail> detail_list=rd_list.getList_detail();
					String date=detail_list.get(0).getData_value();
					//如果已分组的日期，将不放入集合中
					if(dates.indexOf(date)==-1)
					{
						date_list.add(date);
					}
					dates.append(date);
				}
				
				RecordDataList recordDataList=null;
				
				Date data_date=null;
				String count_date="";
				double intake=0;
				double outtake=0;
				for(int i=0;i<data_list.size();i++)
				{
					recordDataList=new RecordDataList();
					//第一条记录不需要处理
					if(i!=0)
					{
						RecordDataList rd_list=data_list.get(i);
						List<RecordDataListDetail> detail_list=rd_list.getList_detail();
						//该集合里面，第一条记录是日期，第二条记录是时间
						String date=detail_list.get(0).getData_value();
						String time=detail_list.get(1).getData_value();
						if(date_list.size()>0)
						{
							count_date=date_list.get(0);
						}
						//早上统计的时间
						Date morningdate = df.parse(count_date+" "+morning_time+":00");
						data_date=df.parse(date+" "+time+":00");
						long l = (data_date.getTime() - morningdate.getTime())/(1000 * 60 * 60 );
						//如果是一天之内的数据，累加
						if(l<=24)
						{
							for(int j=0;j<detail_list.size();j++)
							{
								RecordDataListDetail detail=detail_list.get(j);
								if("inTake".equals(detail.getData_key()))
								{
									if(detail.getData_value()!=null&&detail.getData_value().trim().length()>0)
									{
										intake+=Double.parseDouble(detail.getData_value());
									}
									
								}
								if("outTake".equals(detail.getData_key()))
								{
									if(detail.getData_value()!=null&&detail.getData_value().trim().length()>0)
									{
										outtake+=Double.parseDouble(detail.getData_value());
									}
								}
							}
						}
						
						morningdate = df.parse(count_date+" "+morning_time+":00");
						
						//大于7点时，在之前插入一条统计的记录
						if(l>24&&(count_date.equals(date)))
						{
					    	List <RecordDataListDetail> list_detail=new ArrayList();
					    	
					    	//放入入量
					    	RecordDataListDetail rld=new RecordDataListDetail();
					    	rld.setData_key("inTake");
					    	rld.setData_value(intake+"");
					    	
					    	list_detail.add(rld);

					    	//放入出量
					    	rld=new RecordDataListDetail();
					    	rld.setData_key("outTake");
					    	rld.setData_value(outtake+"");				    	
					    	
					    	list_detail.add(rld);
					    	
					    	recordDataList.setList_detail(list_detail);
					    	recordDataList.setRecord_id("0");//统计的数据，id都设置为0
					    	
					    	list_result.add(recordDataList);	
					    	
					    	//把出量和入量清零
					    	intake=0;
					    	outtake=0;
					    	
							//统计过的日期，先删除
							if(date_list.size()>0)
							{
								date_list.remove(0);
							}
						}
					}
					//第一条，直接放回原来的集合中
					list_result.add(data_list.get(i));	
					
					
				}
			}
		}
		return list_result;
	}		
	
	/**
	 * 根据时间段统计出入量
	 * @param template_id
	 * @param inpatient_no
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public String getDataBefore(String template_id,String inpatient_no,String start_time,String end_time) throws Exception{
		String str="";
		Map map=new HashMap();
		map.put("template_id", template_id);
		map.put("inpatient_no", inpatient_no);
		map.put("start_time", start_time);
	    map.put("end_time", end_time);
	    map.put("in_out", "'inTake'");
	    double inTake=0;//入量
		List<RecordDataItem> list_before=reportMapper.get_inout_data_bytime(map);
		if(list_before!=null&&list_before.size()>0)
		{
			for(int lb=0;lb<list_before.size();lb++)
			{
				RecordDataItem it=list_before.get(lb);
				if("inTake".equals(it.getTemplate_item_id())){
					String intake2=it.getRecord_value();
					if(intake2==null||"".equals(intake2.trim())){
						intake2="0";
					}
					inTake+=Double.parseDouble(intake2);
				}
			}

		}	
		//计算出量
		map.put("in_out", "'out_name','outTake'");
		list_before=reportMapper.get_inout_data_bytime(map);
		//出量分类统计展示
		StringBuffer show_str=new StringBuffer();
		if(list_before!=null&&list_before.size()>0)
		{
			Map<String,Double> map_temp=new HashMap();
			int ll=list_before.size();
			//出量名称和出量必须成对出现，不然不予以计算
			if(ll%2!=0)
			{
				show_str.append("出量名称和出量必须成对出现，请先补齐数据再统计");		
			}
			for(int lb=0;lb<ll;lb++)
			{
				RecordDataItem t=list_before.get(lb);
				//入量名称
				if(lb%2==0)
				{
					double out_take=Double.parseDouble(list_before.get(lb+1).getRecord_value());
					//如果之前有保存值
					if(map_temp.get(t.getRecord_value())!=null)
					{
						//获取之前保存的出量
						double outtake=(double) map_temp.get(t.getRecord_value());
						outtake+=out_take;
						map_temp.put(t.getRecord_value(), outtake);
					}else{
						//键值对 键为出量代码，值为出量
						map_temp.put(t.getRecord_value(), out_take);
					}
				}
			}
			for(String key : map_temp.keySet()) {
				Map queryMap=new HashMap();
				queryMap.put("metadata_code", key);
				String metadata_name=reportMapper.getNameBycode(queryMap);
				DocReportCountInOut tt=new DocReportCountInOut();
				show_str.append(metadata_name)
				.append(":")
				.append(map_temp.get(key)+";");
			}
		}	
		str="总入量："+inTake+";   出量分类统计："+show_str.toString();
		return str;
	}
	
	/**
	 * 设置一条记录
	 * @param date
	 * @param time
	 * @param show_str
	 * @return
	 */
	public RecordDataList setRecordData(String date,String time,String show_str){
		RecordDataList rdl=new RecordDataList();
		rdl.setApprove_status("N");
		rdl.setCreate_person("统计");
    	List <RecordDataListDetail> list_detail=new ArrayList();
    	//日期
    	RecordDataListDetail rld=new RecordDataListDetail();
    	rld.setData_key("DATE_LIST");
    	rld.setData_value(date);
    	
    	list_detail.add(rld);
    	//时间
    	rld=new RecordDataListDetail();
    	rld.setData_key("TIME_LIST");
    	rld.setData_value(time);
    	
    	list_detail.add(rld);
    	
    	//放入出入量统计
    	rld=new RecordDataListDetail();
    	rld.setData_key("inTake");
    	rld.setData_value(show_str+"");
    	
    	list_detail.add(rld);
    	rdl.setList_detail(list_detail);
    	rdl.setRecord_id("0");//统计的数据，id都设置为0	
		return rdl;
	}


	/**
	 * 护理文书数据转移到历史表
	 * 出院超过9天的病人
	 */
	@Override
	@Transactional(value = "transactionManager")
	public void reportDataBak() {
		//备份主表数据（转到历史表）
		reportMapper.report_data_bak();
		//备份从表数据（转到历史表）
		reportMapper.report_data_item_bak();
		//删除从表数据
		reportMapper.delete_report_data_item();
		//删除主表数据
		reportMapper.delete_report_data();
	}


	@Override
	public List<DocDataReportSync> getDocDataReportSync(String startDate,
			String endDate, String patId,List<String> outNames) {
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			Date date = new Date();
			startDate = DateUtil.format(date,com.lachesis.mnis.core.util.DateUtil.DateFormat.YMD) + MnisConstants.EMPTY + MnisConstants.DOC_REPORT_STATISTIC_TIME;
			endDate = DateUtil.format(DateUtil.setNextDayToDate(date),com.lachesis.mnis.core.util.DateUtil.DateFormat.YMD) + 
					MnisConstants.EMPTY + MnisConstants.DOC_REPORT_STATISTIC_TIME;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("patId", patId);
		if(null == outNames){
			return reportMapper.getDocDataReportInSync(params);
		}else{
			params.put("outNames", outNames);
			return reportMapper.getDocDataReportOutSync(params);
		}
		
	}


	@Override
	public void docDataReportSync(List<DocDataReportSync> docDataReportSyncs,
			String recordDay,Date fullDateTime)
			throws Exception {
		List<BodySignRecord> bodySignRecords = new ArrayList<BodySignRecord>();
		//已存在的患者,用于合并体征
		Map<String, Object> existPatIdMap = new HashMap<String, Object>();
		
		//入量
		for (DocDataReportSync docDataReportSync : docDataReportSyncs) {
			
			if(StringUtils.isBlank(docDataReportSync.getValue())
					|| Double.valueOf(docDataReportSync.getValue()) == 0d){
				continue;
			}
			
			if(existPatIdMap.containsKey(docDataReportSync.getPatId())){
				addBodySignItemToRecord(docDataReportSync, bodySignRecords.get(
						(Integer)existPatIdMap.get(docDataReportSync.getPatId())));
			}else{
				BodySignRecord bodySignRecord = new BodySignRecord();
				//基本信息
				bodySignRecord.setPatientId(docDataReportSync.getPatId());
				bodySignRecord.setRecordTime(MnisConstants.ZERO_HMS_TIME);
				bodySignRecord.setRecordDay(recordDay);
				bodySignRecord.setFullDateTime(fullDateTime);
				bodySignRecord.setDeptCode(docDataReportSync.getDeptCode());
				addBodySignItemToRecord(docDataReportSync, bodySignRecord);
				
				bodySignRecords.add(bodySignRecord);
				existPatIdMap.put(docDataReportSync.getPatId(), (bodySignRecords.size()-1));
			}
		}
		//生命体征
		
		if(!bodySignRecords.isEmpty())
			bodySignService.batchBodySignRecord(bodySignRecords,true,null,false);
	}
	
	/**
	 * 生成bodysignitem
	 * @param docDataReportSync
	 * @param record
	 * @return
	 */
	private BodySignRecord addBodySignItemToRecord(DocDataReportSync docDataReportSync,BodySignRecord record){
		//子项信息
		BodySignItem bodySignItem = new BodySignItem();
		
		String itemCode = MnisConstants.EMPTY;
		String itemName = MnisConstants.EMPTY;
		String measureNoteCode = MnisConstants.EMPTY;
		String measureNoteName = MnisConstants.EMPTY;
		if(BodySignConstants.INTAKE.equals(docDataReportSync.getValueType())){
			itemCode = BodySignConstants.TOTAL_INPUT;
			itemName = "总入液量";
		}else{
			itemCode = BodySignConstants.URINE;
			itemName = "尿量";
		}
		measureNoteCode = BodySignConstants.BODYSIGN_ITEM_MOREN;
		measureNoteName = "默认";
		
		bodySignItem.setItemUnit(MnisConstants.INFUSION_DOSAGE_UNIT);
		bodySignItem.setUnit(MnisConstants.INFUSION_DOSAGE_UNIT);
		bodySignItem.setItemCode(itemCode);
		bodySignItem.setItemName(itemName);
		bodySignItem.setMeasureNoteCode(measureNoteCode);
		bodySignItem.setMeasureNoteName(measureNoteName);
		bodySignItem.setItemValue(docDataReportSync.getValue());
			
		record.addToBodySignItems(bodySignItem);
		return record;
	}

	/**
	 * 预处理待保存的文书数据，添加必要的保存字段
	 * @param record
	 */
	private void preprocessRecordWithSaverequiredItem(DocRecord record) throws MnisException {
		//1、获取文书模板信息
		DocTemplate docTemplate = new DocTemplate();
		docTemplate.setTemplateId(record.getTemplate_id());
		String patientNo = record.getInpatient_no();
		List<DocTemplate> templ_list = getTemplateList(docTemplate, patientNo,false);
		if(0<templ_list.size()) {
			docTemplate = templ_list.get(0);
			//只处理评估单类文书的情况，列表型文书不保存必要字段，这些值直接从患者信息中获取
			DocType docType = reportMapper.getDocTypeByID(docTemplate.getDocType());
			String showType = docType.getShowType();
			if(DocReportConstants.TEMPLATE_TYPE_FIXED.equals(showType)){
				//2、获取必须要保存的字段信息
				List<DocBand> band_list = docTemplate.getBands();
				List<RecordDataItem> itemRequiredList = new ArrayList<RecordDataItem>();
				for(DocBand docBand: band_list){
					List<DocTemplateItem> item_list = docBand.getItems();
					for(DocTemplateItem item: item_list){
						if(item.isSaveRequired()) {
							RecordDataItem recordItem = new RecordDataItem();
							recordItem.setRecord_id(record.getRecordId());
							recordItem.setTemplate_item_id(item.getItemId());
							itemRequiredList.add(recordItem);
						}
					}
				}

				//3、把这些字段合并到文书记录
				List<RecordDataItem> recordItem_list = record.getData_item();
				List<RecordDataList> recordList_list = record.getData_list();
				for(RecordDataItem itemRequired: itemRequiredList){
					int index = 0;
					//3.1、插入到data_item
					for(index = 0; index<recordItem_list.size();index++){
						if(itemRequired.getTemplate_item_id().equals(
								recordItem_list.get(index).getTemplate_item_id())){
							//包含字段
							break;
						}
					}
					if(0<recordItem_list.size() && recordItem_list.size() <= index) {
						//不包含该字段
						recordItem_list.add(itemRequired);
					}
					//3.2、插入到data_list
					//目前NDA端不处理fixed类型的文书，因此data_list没有处理的必要，
					//如果后续有相应的需求变更，可以打开以下代码
//						for(index = 0; index<recordList_list.size();index++){
//							List<RecordDataListDetail> detailList = recordList_list.get(index).getList_detail();
//							for(int detailIndex = 0; detailIndex<detailList.size();detailIndex++){
//								if(itemRequired.getTemplate_item_id().equals(
//										detailList.get(detailIndex).getData_key())){
//									//包含字段
//									break;
//								}
//							}
//							if(detailList.size() <= index && 0<detailList.size()) {
//								//不包含该字段
//								RecordDataListDetail detailItemRequired = new RecordDataListDetail();
//								detailItemRequired.setRecord_id(recordList_list.get(index).getRecord_id());
//								detailItemRequired.setData_key(itemRequired.getTemplate_item_id());
//								detailList.add(detailItemRequired);
//							}
//						}
				}
			}
		}else {
			throw new MnisException("获取不到文书记录的模板信息！");
		}
	}

	/**
	 * 封装一下文书记录条目的保存，方便进行预处理
	 * @param recordItem
	 */
	private void saveRecordItem(RecordDataItem recordItem){
		//bug#7000，文书记录中如果值为空，统一保存为“”，不允许“null”，防止前端显示出现问题。
		if(StringUtil.hasValue(recordItem.getRecord_value()) && recordItem.getRecord_value().toLowerCase().contains("null")){
			recordItem.setRecord_value("");
		}
		reportMapper.saveRecordItem(recordItem);
	}

	/**
	 * 设置患者默认护理记录单
	 * @param patID
	 * @param templateID
	 * @param deptCode
	 */
	@Override
	public void setPatientDefaultTemplate(String patID, String templateID, String deptCode){
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("patID", patID);
		paramMap.put("templateID", templateID);
		paramMap.put("deptCode", deptCode);

		List<PatientDefaultTemplate> listTemp = new ArrayList<PatientDefaultTemplate>();
		listTemp = reportMapper.getPatientDefaultTemplate(paramMap);
		if(0<listTemp.size()){
			//已经存在记录，更新之
			reportMapper.updatePatientDefaultTemplate(paramMap);
		}else {
			//不存在记录，新创建一条
			reportMapper.insertPatientDefaultTemplate(paramMap);
		}
	}

	/**
	 * 设置文书打印页码数
	 * @param patID
	 * @param templateID
	 * @param printNum
	 * @param createPerson
	 */
	@Override
	public void setReportPrintNum(String patID, String templateID, String printNum, String createPerson){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("patID", patID);
		paramMap.put("templateID", templateID);
		paramMap.put("printNum", printNum);
		paramMap.put("createPerson", createPerson);

		Integer num = reportMapper.getReportPrintNum(paramMap);
		if(null != num && 0<num.intValue()) {
			//已经存在记录，更新之
			Date modifyDate = new Date();
			paramMap.put("createTime", modifyDate);
			reportMapper.updateReportPrintNum(paramMap);
		}else {
			//不存在记录，新创建一条
			reportMapper.insertReportPrintNum(paramMap);
		}

	}

	/**
	 * 获取文书打印页码数
	 * @param patID
	 * @param templateID
	 */
	@Override
	public int getReportPrintNum(String patID, String templateID){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("patID", patID);
		paramMap.put("templateID", templateID);
		Integer num = reportMapper.getReportPrintNum(paramMap);
		if(null == num){
			return 0;
		}

		return num.intValue();
	}
	@Override
	public DocRecordDataStatistic getDocReprotDataStatistic(String patId,
			String deptCode,String templateID, String staticDate, String staticTime) {
		if(StringUtils.isBlank(patId)){
			LOGGER.debug("docReprotServiceImpl getDocReprotDataStatistic patId is null");
			return null;
		}
		DocRecordDataStatistic statistic =  new DocRecordDataStatistic();
		List<DocRecordStatistic> tic = reportMapper.getDocReprotDataStatistic(patId, deptCode, staticDate, staticTime,templateID);
		statistic.setDocRecordStatistics(tic);
		if(tic!=null && !tic.isEmpty()){
			statistic.setStaticDate(tic.get(0).getStaticDate());
		}
		return statistic;
	}
	
	/**
	 * 处理统计展现形式
	 * @param docRecordDataStatistics
	 * @return
	 */
	private List<DocRecordDataStatistic> showDocRecordDataStatistics(List<DocRecordDataStatistic> docRecordDataStatistics,
			String beforeCountTime,String afterCountTime,String inDateTime){
		if(null == docRecordDataStatistics || docRecordDataStatistics.isEmpty()){
			return docRecordDataStatistics;
		}
		
		
		for (DocRecordDataStatistic docRecordDataStatistic : docRecordDataStatistics) {
			List<DocRecordStatistic> docRecordStatistics =docRecordDataStatistic.getDocRecordStatistics();
			if(docRecordStatistics == null || docRecordStatistics.isEmpty()){
				continue;
			}
			
			//上午统计记录
			DocRecordStatistic beforeDocRecordStatistic = getDocReprotStatisticByCountTime(docRecordStatistics,true, beforeCountTime,afterCountTime,inDateTime);
			//下午统计记录
			DocRecordStatistic afterDocRecordStatistic = getDocReprotStatisticByCountTime(docRecordStatistics,false, beforeCountTime,afterCountTime,inDateTime);
			
			List<DocRecordStatistic> showDocRecordStatistics = new ArrayList<DocRecordStatistic>();
			if(null != beforeDocRecordStatistic){
				showDocRecordStatistics.add(beforeDocRecordStatistic);
			}
			if(null != afterDocRecordStatistic){
				showDocRecordStatistics.add(afterDocRecordStatistic);
			}
			
			
			docRecordDataStatistic.setDocRecordStatistics(showDocRecordStatistics);
		}
		return docRecordDataStatistics;
	}
	
	/**
	 * 处理统计展现形式
	 * @param docRecordDataStatistics
	 * @return
	 */
	private DocRecordDataStatistic showDocRecordDataStatistic(
			DocRecordDataStatistic docRecordDataStatistic,
			String beforeCountTime, String afterCountTime, String inDateTime) {
		if (null == docRecordDataStatistic) {
			return docRecordDataStatistic;
		}

		List<DocRecordStatistic> docRecordStatistics = docRecordDataStatistic
				.getDocRecordStatistics();
		if (docRecordStatistics == null || docRecordStatistics.isEmpty()) {
			return null;
		}

		// 上午统计记录
		DocRecordStatistic beforeDocRecordStatistic = getDocReprotStatisticByCountTime(
				docRecordStatistics, true, beforeCountTime, afterCountTime,
				inDateTime);
		// 下午统计记录
		DocRecordStatistic afterDocRecordStatistic = getDocReprotStatisticByCountTime(
				docRecordStatistics, false, beforeCountTime, afterCountTime,
				inDateTime);

		List<DocRecordStatistic> showDocRecordStatistics = new ArrayList<DocRecordStatistic>();
		if (null != beforeDocRecordStatistic) {
			showDocRecordStatistics.add(beforeDocRecordStatistic);
		}
		if (null != afterDocRecordStatistic) {
			showDocRecordStatistics.add(afterDocRecordStatistic);
		}

		docRecordDataStatistic.setDocRecordStatistics(showDocRecordStatistics);
		return docRecordDataStatistic;
	}
	
	/**
	 * 根据统计时间点获取统计信息
	 * @param docRecordStatistics
	 * @param countTime
	 * @return
	 */
	private DocRecordStatistic getDocReprotStatisticByCountTime(List<DocRecordStatistic> docRecordStatistics,
			boolean isBefore,String beforeTime,String afterTime,String inDateTime){
		if(null == docRecordStatistics || docRecordStatistics.isEmpty()){
			return null;
		}
		//入量统计值
		StringBuffer inSb = new StringBuffer();
		//出量统计值
		StringBuffer outSb = new StringBuffer();
		
		String staticDate = StringUtils.EMPTY;
		String countTime = isBefore ? beforeTime:afterTime;
		DocRecordStatistic showDocRecordStatistic = new DocRecordStatistic();
		for (DocRecordStatistic docRecordStatistic : docRecordStatistics) {
			if(StringUtils.isBlank(staticDate))
				staticDate = docRecordStatistic.getStaticDate();
			//上午
			if (docRecordStatistic.getStaticTime().equals(countTime)
					&& !"0".equals(docRecordStatistic.getStaticValue())) {
				//先保存下页码
				showDocRecordStatistic.setPageNo(docRecordStatistic.getPageNo());
				//入量
				if(docRecordStatistic.getStaticType().equals(ReportConstants.IN_TAKE)){
					inSb.append("总入量:").append(docRecordStatistic.getStaticValue()).append("ml");
				}else {
					if(StringUtils.isBlank(outSb.toString())){
						outSb.append("总出量:");
					}
					outSb.append(docRecordStatistic.getStaticItemTypeName())
					.append(":").append(docRecordStatistic.getStaticValue()).append("   ");
				}
			}
		}
		
		StringBuffer sBuffer = new StringBuffer();
		if(StringUtils.isBlank(inSb.toString()) && StringUtils.isBlank(outSb.toString())){
			return null;
		}else{
			//先把其他相关数据传递一下
			showDocRecordStatistic.setPatId(docRecordStatistics.get(0).getPatId());
			showDocRecordStatistic.setTemplateID(docRecordStatistics.get(0).getTemplateID());

			showDocRecordStatistic.setStaticTime(countTime);
			//staticDate , inDate,countTime
			String hourDiff ="";
			//String hourDiff = processDocReportStatisticHourDiff( isBefore, staticDate,inDateTime, beforeTime, afterTime);
			if(isBefore){
				hourDiff ="24h";
			}else{
				hourDiff ="8h";
			}
			
			showDocRecordStatistic.setHourDiff(hourDiff);
			if(StringUtils.isBlank(inSb.toString())){
				showDocRecordStatistic.setStaticValue(outSb.toString());
			}else{
				showDocRecordStatistic.setStaticValue(inSb.append(";").append(outSb).toString());
			}
		}
		
		return showDocRecordStatistic;
	}
	
	/**
	 * 获取文书表头数据
	 * @param patID
	 * @param templateID
	 * @param isPrint
	 * @return
	 * @throws Exception
	 */
	@Override
	public DocRecord getDocHeaderDate(String patID, String templateID, String isPrint) throws Exception{
		DocRecord headerData = null;
		//处理打印模板的ID
		if("Y".equals(isPrint)&&(templateID.contains("print")))
		{
			//打印护理记录单的id格式为: print_xxxx(对应模板ID)
			String[] strTempID = templateID.split("_");
			if(2>strTempID.length){
				//打印模板ID的格式不对
				throw new MnisException("打印模板ID的格式不对！");
			}else{
				templateID = strTempID[1];
			}
		}

		//获取记录数据
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("patID", patID);
		paramMap.put("templateID", templateID);
		List<DocDataRecord> record = reportMapper.getDocHeaderRecord(paramMap);

		//填入返回的数据结构
		if(null != record && 0 < record.size()) {
			headerData = new DocRecord();
			headerData.setIsHeader("1");
			headerData.setInpatient_no(patID);
			headerData.setTemplate_id(templateID);
			String show_type = reportMapper.getShowType();
			headerData.setPrint_show(show_type);
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			headerData.setNow_time(df.format(new Date()));

			DocDataRecord dataRecord = record.get(0);
			String recordID = dataRecord.getRecord_id();
			headerData.setRecordId(recordID);
			headerData.setCreate_person(dataRecord.getCreate_person());
			headerData.setApprove_status(dataRecord.getApprove_status());
			//这里的时间是timestamp类型的字符串
			if(!StringUtils.isEmpty(dataRecord.getCreate_time())){
				headerData.setCreateTime(java.sql.Timestamp.valueOf(dataRecord.getCreate_time()));
			}
			if(!StringUtils.isEmpty(dataRecord.getModify_time())){
				headerData.setModify_time(java.sql.Timestamp.valueOf(dataRecord.getModify_time()));
			}

			//转换数据结构：DocDataRecordItem -> RecordDataItem
			//文书这块儿的数据结构太乱了，不然这里完全可以抽象出一个通用的处理方法
			List<RecordDataItem> itemList = new ArrayList<RecordDataItem>();
			RecordDataItem destItem = null;
			Map<String, String> infoMap = null;
			for(DocDataRecordItem sourItem : dataRecord.getList()) {
				 destItem = new RecordDataItem();
				//如果数据库中保存的信息为空，则需要获取一下患者的实时信息
				if(StringUtils.isEmpty(sourItem.getRecord_value())
						//诊断信息需要使用最新数据，不直接采用文书数据库中的存储值
						||DocReportConstants.PAT_DIAGNOSE.equals(sourItem.getTemplate_item_id())){
					if(null == infoMap){
						infoMap = new HashMap<String, String>();
						List<InpatientInfo> patInfo = getBaseInfo(patID);
						//把数据格式转换为一个map，这样后续取值的时候不用再循环遍历了
						for(InpatientInfo info:patInfo) {
							infoMap.put(info.getKey(), info.getValue());
						}
					}
					sourItem.setRecord_value(infoMap.get(sourItem.getTemplate_item_id()));
//					if(DocReportConstants.PAT_DIAGNOSE.equals(sourItem.getTemplate_item_id())) {
//						//三院要求：诊断信息如果有多条，只显示前三条；
//						sourItem.setRecord_value(getPartOfDiagnoseInfo(sourItem.getRecord_value(), DocReportConstants.DIAGNOSE_NUM));
//					}
				}

				String parentID = sourItem.getParent_id();
				if(StringUtils.isEmpty(parentID) || "0".equals(parentID)){
					//顶层节点
					destItem.setRecord_item_id(sourItem.getRecord_item_id());
					destItem.setTemplate_item_id(sourItem.getTemplate_item_id());
					destItem.setRecord_value(sourItem.getRecord_value());
					destItem.setParent_id(parentID);
					destItem.setRecord_id(recordID);
					destItem.setList_item(new ArrayList<RecordDataItem>());
	
					itemList.add(destItem);
				}else {
					//子节点
					deal_data(itemList, sourItem);
				}

			}

			headerData.setData_item(itemList);
		}
		return headerData;
	}

	/**
	 * 诊断信息提取前面部分指定的个数
	 * @param diagInfo 完整的诊断信息
	 * @param num 指定的个数
     * @return 前面部分的诊断信息字符串
     */
    private String getPartOfDiagnoseInfo(String diagInfo, int num) {
		if(StringUtils.isEmpty(diagInfo)){
			return null;
		}
		String[] diagStrCh = diagInfo.split("；");//中文
		String[] diagStrEn = diagInfo.split(";");//英文
		String[] diagStr = diagStrCh.length > diagStrEn.length ? diagStrCh : diagStrEn;//取较大一个
		if (num < diagStr.length) {
			StringBuilder builder = new StringBuilder();
			for (int j = 0; j < num; j++) {
                builder.append(diagStr[j]);
            }
			return builder.toString();
		}
		return diagInfo;
	}

	/**
	 * 获取入院时间
	 * @param list
	 * @return
	 */
	private String getInDate(String patId){
		Patient p = patientService.getPatientByPatId(patId);
		String inDate = null;
		if(p != null){
			Date date = p.getInDate();
			if(date != null){
				inDate = DateUtil.format(date, com.lachesis.mnis.core.util.DateUtil.DateFormat.FULL);
			}
		}
		return inDate;
	}
	
	/**
	 * 计算统计时间差
	 * @param isBefore
	 * @param staticDate
	 * @param inDateTime
	 * @param beforeTime
	 * @param afterTime
	 * @return
	 */
	private String processDocReportStatisticHourDiff(boolean isBefore,String staticDate,
			String inDateTime,String beforeTime,String afterTime){
		
		if(StringUtils.isBlank(inDateTime)){
			return null;
		}
		String hourDiff = StringUtils.EMPTY;
		
		int afterHour = DateUtil.parse(afterTime).getHours();
		int beforeHour = DateUtil.parse(beforeTime).getHours();
		String[] inDateTimes = inDateTime.split(" ");
		Date inDate = DateUtil.parse(inDateTimes[0]);
		Date stDate = DateUtil.parse(staticDate);
		int inDateHour = DateUtil.parse(inDateTimes[1]).getHours();
		
		
		//日期是否相同
		if(stDate == inDate){
			if(isBefore){
				hourDiff = (beforeHour-inDateHour)+ReportConstants.HOUR;
			}else{
				if(beforeHour >= inDateHour){
					hourDiff = (afterHour-beforeHour) + ReportConstants.HOUR;
				}else{
					hourDiff = (afterHour-inDateHour) + ReportConstants.HOUR;
				}
			}
		}else if (DateUtil.diffDate(inDate, stDate) == 1){//相隔一天
			if(isBefore){
				if(beforeHour >= inDateHour){
					hourDiff = ReportConstants.HOUR_24;
				}else{
					hourDiff = (24-inDateHour+beforeHour) + ReportConstants.HOUR;
				}
			}else{
				hourDiff = (afterHour-beforeHour) + ReportConstants.HOUR;
			}
		}else{
			if(isBefore){
				hourDiff = ReportConstants.HOUR_24;
			}else{
				hourDiff = (afterHour-beforeHour) + ReportConstants.HOUR;
			}
		}
		
		return hourDiff;
		
	}

	/**
	 * 清除模板及元数据缓存
	 */
	@Override
	public void clearDocTemplateCache() throws Exception{
		//先清除模板数据，后清除元数据
		TemplateCache.clearAllTemplateMap();
		TemplateCache.clearAllMetadataMap();
	}
	
	/**
	 * 获取时间统计时间：当前时间，开始统计时间，下午统计时间
	 * @param timeList
	 * @return
	 */
	private String[] getStatisticTiems(String timeList) {
		// 3.根据当天时间判断区间
		Properties prop = PropertiesUtils
				.getProperties(ReportConstants.PROP_REPORT_NAME);
		String count_time = prop.getProperty(ReportConstants.COUNT_TIME).trim();
		// 出入量统计时间
		String count_time_afternoon = prop.getProperty(
				ReportConstants.COUNT_TIME_AFTERNOON).trim();// 下午统计时间
		// 计算出统计时间点
		String time = ReportUtil.caclTimeRegion(timeList, count_time,
				count_time_afternoon);

		String[] times = new String[3];
		times[0] = time;
		times[1] = count_time;
		times[2] = count_time_afternoon;
		return times;

	}
	
	/**
	 * 计算文书统计
	 * @param newTakeValue		:新输入值
	 * @param oldTakeValue		:老值
	 * @param staticTakeValue	:已有统计值
	 * @return
	 */
	private String caluStaticValue(String newTakeValue,String staticTakeValue,boolean isAdd ){
		double d_newTakeValue = ReportUtil.StringToDouble(newTakeValue);
		double d_staticTakeValue = ReportUtil.StringToDouble(staticTakeValue);
		if(isAdd){
			return String.valueOf(d_newTakeValue + d_staticTakeValue);
		}else{
			return String.valueOf(d_staticTakeValue - d_newTakeValue);
		}
		
	}
	
	/**
	 * 根据子项值,获取统计的值和类型
	 * @param docRecordStatistics
	 * @return
	 */
	private String[] getStaticValues(List<DocDataRecordItem> oldDocDataRecordItems){
		String oldInTakeValue = StringUtils.EMPTY; //入量值
		String oldOutTakeValue = StringUtils.EMPTY;//出量值
		String oldOuTakeType = StringUtils.EMPTY; 	//出量类型
		String oldDateList = StringUtils.EMPTY;		//日期
		String oldTimeList = StringUtils.EMPTY;		//时间
		String oldInDate = StringUtils.EMPTY;		//入院时间
		for (DocDataRecordItem docDataRecordItem : oldDocDataRecordItems) {
			if(ReportConstants.IN_TAKE.equals(docDataRecordItem.getTemplate_item_id())){
				oldInTakeValue = docDataRecordItem.getRecord_value();
			}else if(ReportConstants.OUT_TAKE.equals(docDataRecordItem.getTemplate_item_id())){
				oldOutTakeValue = docDataRecordItem.getRecord_value();
				
			}else if(ReportConstants.OUT_NAME.equals(docDataRecordItem.getTemplate_item_id())){
				oldOuTakeType = docDataRecordItem.getRecord_value();
			}else if(ReportConstants.DATE_LIST.equals(docDataRecordItem.getTemplate_item_id())){
				oldDateList = docDataRecordItem.getRecord_value();
			}else if(ReportConstants.TIME_LIST.equals(docDataRecordItem.getTemplate_item_id())){
				oldTimeList = docDataRecordItem.getRecord_value();
			}else if(ReportConstants.BE_HOSPITALIZED_TIME.equals(docDataRecordItem.getTemplate_item_id())) {
				oldInDate = docDataRecordItem.getRecord_value();
			}
		}
		
		//返回数组值
		String[] values = new String[6];
		values[0] = oldInTakeValue;
		values[1] = oldOutTakeValue;
		values[2] = oldOuTakeType;
		values[3] = oldDateList;
		values[4] = oldTimeList;
		values[5] = oldInDate;
		//待保存数据
		return values;
	}
	
	/**
	 * 统计偏移
	 * @param oldDocRecordStatistics
	 * @param newDocRecordStatistics
	 */
	private String[] deviationStaticValues(List<DocDataRecordItem> oldDocDataRecordItems,
			List<DocDataRecordItem> newDocDataRecordItems){
		//历史数据
		String[] oldValues = getStaticValues(oldDocDataRecordItems);
		String[] newValues = getStaticValues(newDocDataRecordItems);
		
		
		//计算偏移
		String[] values = new String[3];
		values[0] = String.valueOf(ReportUtil.StringToDouble(newValues[0]) 
				- ReportUtil.StringToDouble(oldValues[0]));
		values[1] = String.valueOf(ReportUtil.StringToDouble(newValues[1]) 
				- ReportUtil.StringToDouble(oldValues[1]));
		values[2] = newValues[2];
		//待保存数据
		return values;
	}
	
	/**
	 * 给出统计时间
	 * @param region
	 * @param dateList
	 * @return
	 */
	private String getStaticDate(int region,String dateList){
		//判断时间区间
		
		//保存日期
		String saveDateList = StringUtils.EMPTY;
		if( 0 == region)//0-7
		{
			saveDateList = dateList;
		}else{//15-07
			saveDateList = DateUtil.format(DateUtils.addDays(DateUtil.parse(dateList), 1), com.lachesis.mnis.core.util.DateUtil.DateFormat.YMD);
		}
		return saveDateList;
	}
	
	/**
	 * 获取统计配置的信息
	 * @return
	 */
	private String[] getStatisticProp(){
		Properties prop = PropertiesUtils
				.getProperties(ReportConstants.PROP_REPORT_NAME);
		String count_time = prop.getProperty(ReportConstants.COUNT_TIME).trim();
		// 出入量统计时间
		String count_time_afternoon = prop.getProperty(
				ReportConstants.COUNT_TIME_AFTERNOON).trim();// 下午统计时间
		
		String[] statisticProps = new String[2];
		statisticProps[0] = StringUtils.isBlank(count_time) ? ReportConstants.SEVER_TIME : count_time;
		statisticProps[1] = StringUtils.isBlank(count_time_afternoon) ? ReportConstants.FIFTEEN_TIME : count_time_afternoon; 
		return statisticProps;
	}

	/**
	 * 记录模块操作行为信息
	 * @param paramStr	调用接口的参数（建议统一使用Map<类型，对象>的方式存放参数，然后转字符串）
	 * @param createPerson	创建人
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void recordAction(String paramStr, String createPerson){
		//0、参数检查及处理
		if(StringUtils.isEmpty(createPerson)){
			createPerson = "unknown";//防止参数为空导致保存失败
		}
		//1、获取调用者信息
		StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
		final String interfaceName = traceElements[2].getMethodName();
		//String className = traceElements[1].getClassName();//可以尝试获取模块名称
		try{
			int len = 1500;
			while(paramStr.length()>len){
				//2、保存记录
				ActionRecord record = new ActionRecord();
				record.setModuleName("DOC");
				record.setInterfaceName(interfaceName);
				record.setParamStr(paramStr.substring(0,len));
				record.setCreatePerson(createPerson);
				actionMapper.saveActionRecord(record);
				paramStr = paramStr.substring(len-1,paramStr.length());
			}
			if(!StringUtils.isEmpty(paramStr)){
				ActionRecord record = new ActionRecord();
				record.setModuleName("DOC");
				record.setInterfaceName(interfaceName);
				record.setParamStr(paramStr);
				record.setCreatePerson(createPerson);
				actionMapper.saveActionRecord(record);
			}
		}catch (Exception e){
			LOGGER.error(e.toString(),"");
		}

//		//使用异步线程记录操作信息
//		final String param = paramStr;
//		final String creater = createPerson;
//		mnisThreadPoolTaskExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				//2、保存记录
//				ActionRecord record = new ActionRecord();
//				record.setModuleName("DOC");
//				record.setInterfaceName(interfaceName);
//				record.setParamStr(param);
//				record.setCreatePerson(creater);
//				actionMapper.saveActionRecord(record);
//			}
//		});

		//调试使用
//		String interfacenm = null;
//		List<ActionRecord> records = actionMapper.getRecordsByModuleAndInterface("DOC", interfacenm);
//		String recordsStr = GsonUtils.toJson(records);
	}

	/**
	 * 根据模板ID获取模板数据
	 * @param templateID
	 * @return
	 */
	private DocTemplate getTemplateByID(String templateID){
		if(StringUtils.isEmpty(templateID)){
			return null;
		}
		//先从缓存获取，如果没有从数据库中查询
		DocTemplate template=(DocTemplate) TemplateCache.getTemplateByID(templateID);
		if(null == template) {
			template = new DocTemplate();
			template.setTemplateId(templateID);
			List<DocTemplate> list = reportMapper.getTemplateList(template);
			if (null==list || 0>=list.size()) {
				return null;
			} else {
				template = list.get(0);
				TemplateCache.addTemplateInMap(templateID, template);
			}
		}

		return template;
	}

	/**
	 * 
	 * 计算数据区域的高度
	 * @param templateID
	 * @return
	 */
	private int getDetailBandHightInListTemplate(String templateID){
		if(StringUtils.isEmpty(templateID)){
			return 0;
		}
		int detailHight = 0;
		//获取打印模板信息
		DocTemplate template = getTemplateByID(templateID);
		if(null == template) {
			return 0;
		}else {
			//1、获取纸张打印区域的总高度
			DocPrintPaper paper = DocPrintPaper.createPrintPaper(DocPrintPaper.PrintPaperType.A4, DocPrintConstants.printResolution);
			//获取横向还是纵向打印,并且获取计算的打印区域的高度
			if(DocReportConstants.TEMPLATE_ORIENTATION_LAND.equals(template.getOrientation())){
				detailHight = paper.getHight4LandPrint();
			}else{
				detailHight = paper.getHight4PortPrint();
			}
			//2、获取非Detail区域的高度和
			int otherHight = 0;
			for(DocBand band : template.getBands()){
				if(!DocReportConstants.TEMPLATE_BAND_NAME_DETAIL.equals(band.getBandName())){
					otherHight += band.getHeight();
				}
			}
			if(0 >= otherHight){
				return 0;
			}
			//3、相减得到Detial区域的打印高度
			if(detailHight<otherHight){
				return 0;
			}else {
				detailHight = detailHight- otherHight;
			}
		}

		return detailHight;
	}

	/**
	 * 计算文书记录的打印页码，同时更新所有受影响的记录页码
	 * @param record
	 * @throws MnisException
	 */
	private void updatePageNoForDocRecord(DocRecord record) throws MnisException{
		//0、验证参数数据有效性
		if(StringUtils.isEmpty(record.getInpatient_no()) || StringUtils.isEmpty(record.getTemplate_id())
				|| StringUtils.isEmpty(record.getRecordId())) {
			throw new MnisException("数据缺失，无法更新页码。");
		}else if(record.getTemplate_id().contains("print") || "1".equals(record.getIsHeader())) {
			throw new MnisException("文书记录不适用于更新页码。");
		}else if(0 >= record.getRowHight()) {
			throw new MnisException("文书记录的行高数据错误。");
		}
		//1、获取模板数据，包括detail区域的总高度和单行记录的行高
		int detailHight = getDetailBandHightInListTemplate(record.getTemplate_id());
		//获取一行的高度
		int oneRowHight = DocPrintUtil.getOneRowHightForDocPrint();
		if(0>=detailHight || 0>= oneRowHight){
			throw new MnisException("模板数据错误。");
		}
		//2、确定重算的起始页面
		int pageNo = record.getPageNo();
		//查询当前记录的上一条记录
		List<DocRecord> recordList = reportMapper.getOneRecordBeforTheRecord(record);
		if (null==recordList || recordList.isEmpty()) {
			//如果不存在上一条记录，说明是该模板的第一条记录，设置页码为1
			pageNo = 1;
		} else {
			int beforeNo = recordList.get(0).getPageNo();//上一条记录的页码
			if(0 >= pageNo) {
				//取前一条记录的页码进行计算
				pageNo = beforeNo;
			}else {
				//当前记录页码，跟前一条记录比较，取较小的一个
				pageNo = pageNo<beforeNo? pageNo:beforeNo;
			}

		}
		//先把暂定的页码设置到数据库中，这样后面就能根据页码正确的取出数据，且按时间排序
		record.setPageNo(pageNo);
		reportMapper.updatePrintInfo4Record(record.getRecordId(), record.getRowHight(), pageNo);

		//3、重算起始页面后的所有记录
		reCountAndUpdateRecordsPrintInfoForPatient(record.getInpatient_no(), record.getTemplate_id(),pageNo,0,false);

	}

	/**
	 * 获取模板detail区域组件的单行高度（方法为：任取一个组件的高度作为单行高度）
	 * 以下参数二选一传入：
	 * @param templateID      模板ID
	 * @param templateItemMap detail区域组件集合
	 * @return 单行高度值，单位：像素
	 */
	private int getOneRowHightInDetailBand(final String templateID, Map<String, DocTemplateItem> templateItemMap){
		if(StringUtils.isEmpty(templateID) && (null==templateItemMap || 0>=templateItemMap.size())){
			return 0;
		}
		//如果模板组件集合为空，就根据模板ID重新获取一下
		if(null==templateItemMap || 0>=templateItemMap.size()){
			templateItemMap = getItemsInBandOfListTemplate(templateID,DocReportConstants.TEMPLATE_BAND_NAME_DETAIL);
			if(0 >= templateItemMap.size()){
				return 0;
			}
		}
		//任取一个组件高度作为单行的高度
		int oneRowHight = (new ArrayList<DocTemplateItem>(templateItemMap.values())).get(0).getHeight();

		return oneRowHight;
	}
	
	/**
	 * 把指定集合中的文书记录行高和页码更新到数据库中
	 * @param recordList 记录集合，使用通配符指定其类型
	 */
	private void updatePrintInfoForRecords(List<?> recordList){
		if(null==recordList || 0>=recordList.size()){
			return;
		}
		Object record = recordList.get(0);
		if(record instanceof DocRecord){
			for(DocRecord updateRecord : (List<DocRecord>)recordList){
				reportMapper.updatePrintInfo4Record(updateRecord.getRecordId(), updateRecord.getRowHight(),
						updateRecord.getPageNo());
			}
		}else if(record instanceof DocReportPrintData){
			for(DocReportPrintData updateRecord : (List<DocReportPrintData>)recordList){
				if(isInOutStatisticRecord(updateRecord)) {
					reportMapper.updatePrintInfo4StatisticRecord(updateRecord);
				}else {
					reportMapper.updatePrintInfo4Record(updateRecord.getRecord_id(), updateRecord.getRowHight(),
							updateRecord.getPageNo());
				}
			}
		}
	}

	private void countPageNoForRecords(List<DocReportPrintData> records, final int printHight, final int startPageNo,
									   final int oneRowHight, Map<String, DocReportPrintData> updateMap){
		if(null == records || 0>=records.size() || 0>=printHight || 0>=startPageNo || 0>=oneRowHight){
			return;
		}

		int hightCount = 0;
		int newPageNo = startPageNo;
		//注意该遍历的前提是：列表中的所有记录按时间排序
		for (DocReportPrintData record : records) {
			//该记录是否需要更新打印信息
			boolean update = false;
			//计算本条记录的页码
			int rowHight = record.getRowHight();
			if(0>=rowHight) {
				//如果行高为0，则重新计算
				if(isInOutStatisticRecord(record)){
					rowHight = oneRowHight;
				}else {
					rowHight = countRowHightForRecord(record);
				}
				record.setRowHight(rowHight);
				update = true;
			}
			hightCount += rowHight;
			if(hightCount > printHight){
				hightCount = rowHight;
				newPageNo +=1;
			}
			if(record.getPageNo() != newPageNo){
				record.setPageNo(newPageNo);
				update = true;
			}
			if(update){
				if(isInOutStatisticRecord(record)) {
					String strID = record.getDateList() + record.getTimeList();
					updateMap.put(strID, record);
				}else {
					updateMap.put(record.getRecord_id(), record);
				}
			}
		}
	}

	/**
	 * 为指定的文书记录集合计算打印页码
	 * 约束条件：1、参数指定的文书记录集合是按日期时间升序排列的；
	 * 			2、记录集合必须是整页开始的记录，不能是任意记录集合
	 * 			3、根据第一条记录的出入量标志，决定是否先插入一行统计信息，因此集合第一条记录的出入量标识必须是准确的。
	 * @param recordList  指定的文书记录集合, 主要使用到的record属性有：ID，时间，行高，页码，出入量标志。
	 * @param printHight  打印区域的高度值，单位：像素
	 * @param startPageNo 起始打印页码
	 * @param oneRowHight 模板中单行记录的行高，用于插入出入量统计记录
	 * @return 行高/打印页码/首行标记有变动的文书集合
	 */
//	private List<DocRecord> countPageNoForRecords(List<DocRecord> recordList, final int printHight,
//												  final int startPageNo, final int oneRowHight){
//		List<DocRecord> updateList = new ArrayList<DocRecord>();
//		if(null == recordList || 0>=recordList.size() || 0>=printHight || 0>=startPageNo || 0>=oneRowHight){
//			return updateList;
//		}
//
//		int hightCount = 0;
//		//判断第一条记录前是否需要先插入一条出入量统计记录
//		if(StringUtil.hasValue(recordList.get(0).getInoutInsert())
//				&& startPageNo == Integer.parseInt(recordList.get(0).getInoutInsert())){
//			hightCount += oneRowHight;
//		}
//		int newPageNo = startPageNo;
//		DocRecord lastRecord = recordList.get(0);
//		final boolean hasInOutOrder = hasInOutOrderForPatient(recordList.get(0).getInpatient_no());
//		//注意该遍历的前提是：列表中的所有记录按时间排序
//		for (DocRecord docRecord : recordList) {
//			//该记录是否需要更新打印信息
//			boolean update = false;
//			//判断是否需要插入出入量记录，如果是，默认插入一行的高度
//			if(hasInOutOrder && isInOutRecordNeeded(lastRecord, docRecord)){
//				hightCount += oneRowHight;
//				if(hightCount > printHight){
//					hightCount = oneRowHight;
//					newPageNo +=1;
//				}
//				if(!Integer.toString(newPageNo).equals(docRecord.getInoutInsert())){
//					docRecord.setInoutInsert(Integer.toString(newPageNo));
//					update = true;
//				}
//			}
//			//计算本条记录的页码
//			int rowHight = docRecord.getRowHight();
//			if(0>=rowHight) {
//				//如果行高为0，则重新计算
//				rowHight = countRowHightForRecord(docRecord);
//				docRecord.setRowHight(rowHight);
//				update = true;
//			}
//			hightCount += rowHight;
//			if(hightCount > printHight){
//				hightCount = rowHight;
//				newPageNo +=1;
//			}
//			if(docRecord.getPageNo() != newPageNo){
//				docRecord.setPageNo(newPageNo);
//				update = true;
//			}
//			if(update){
//				updateList.add(docRecord);
//			}
//			//记录本条记录，进行下一轮循环
//			lastRecord = docRecord;
//		}
//		return updateList;
//	}

	/**
	 * 判断两条记录之间是否需要插入一条出入量统计量信息
	 * 需要用到的记录中信息包括：记录中的日期，记录中的时间。
	 * @param lastRecord 第一条记录
	 * @param thisRecord 第二条记录
	 * @return 需要插入返回真，否者返回假。
	 */
	private boolean isInOutRecordNeeded(DocRecord lastRecord, DocRecord thisRecord){
		if(null==lastRecord || null==thisRecord){
			return false;
		}
		boolean isInOut = false;
		Date inOutTimeMorning = DateUtil.parse(DocPrintConstants.inOutTimeMorning, DateUtil.DateFormat.HM);
		Date inOutTimeAfternoon = DateUtil.parse(DocPrintConstants.inOutTimeAfternoon, DateUtil.DateFormat.HM);
		Date lastDate = DateUtil.parse(lastRecord.getDate_list(), DateUtil.DateFormat.YMD);
		Date thisDate = DateUtil.parse(thisRecord.getDate_list(), DateUtil.DateFormat.YMD);
		Date lastTime = DateUtil.parse(lastRecord.getTime_list().substring(0,5), DateUtil.DateFormat.HM);
		Date thisTime = DateUtil.parse(thisRecord.getTime_list().substring(0,5), DateUtil.DateFormat.HM);
		if(null==lastDate || null==thisDate || null==lastTime || null==thisTime
				|| null==inOutTimeMorning || null==inOutTimeAfternoon){
			return isInOut;
		}
		if(lastDate.equals(thisDate)){
			//同一天，只有跨越了统计时间点的情况才需要插入出入量记录
			if( ((lastTime.before(inOutTimeMorning) || lastTime.equals(inOutTimeMorning)) && thisTime.after(inOutTimeMorning))
					|| ((lastTime.before(inOutTimeAfternoon) || lastTime.equals(inOutTimeAfternoon)) && thisTime.after(inOutTimeAfternoon)) ){
				isInOut = true;
			}
		}else if(lastDate.before(thisDate)){
			//跨天，只有当天inOutTimeAfternoon到次日inOutTimeMorning之间是不需要统计的，其他情况肯定需要统计
			if(thisDate.equals(DateUtils.addDays(lastDate, 1)) && lastTime.after(inOutTimeAfternoon) && thisTime.before(inOutTimeMorning)){
				isInOut = false;
			}else {
				isInOut = true;
			}
		}

		return isInOut;
	}

	private boolean isInOutRecordNeeded(final DocReportPrintData lastRecord, final DocReportPrintData thisRecord){
		DocRecord lastDocRecord = DocReportPrintData2DocRecord(lastRecord);
		DocRecord thisDocRecord = DocReportPrintData2DocRecord(thisRecord);
		if(null==lastDocRecord || null==thisDocRecord){
			return false;
		}

		return isInOutRecordNeeded(lastDocRecord, thisDocRecord);
	}

	/**
	 * 判断患者是否有出入量医嘱，包括：1、24小时出入量医嘱；2、24小时尿量医嘱；后续可能会增加，to be continued.
	 * @param patID
	 * @return
	 */
	private boolean hasInOutOrderForPatient(String patID){
		return (orderService.has24InAndOutOrder(patID, null, null)
				|| orderService.has24UrineOrder(patID, null, null));
	}


	/**
	 * 计算文书记录打印所需要的行高
	 * @param record 文书记录，主要使用的数据有：记录ID，模板ID，行高
	 * @return
	 * @throws MnisException
	 */
	private int countRowHightForRecord(DocRecord record) throws MnisException{
		//0、验证数据有效性
		if(StringUtils.isEmpty(record.getTemplate_id()) || record.getTemplate_id().contains("print")) {
			throw new MnisException("模板信息错误。");
		}
		//1、获取模板组件信息
		Map<String, DocTemplateItem> templateItemInfo = getItemsInBandOfListTemplate(record.getTemplate_id(),
															DocReportConstants.TEMPLATE_BAND_NAME_DETAIL);
		if(0 >= templateItemInfo.size()){
			return 0;
		}
		//2、获取文书记录的打印数据（主要针对OPT等选项要使用元数据）
		DocReportPrintData printData = reportMapper.getPrintDataForRecord(record.getRecordId(), new ArrayList<String>(templateItemInfo.keySet()));
		if(null == printData) {
			throw new MnisException("未获取到记录数据。");
		}
		//如果是护记数据，需要先累计出入量
		if(isReportTypeHLJL(record.getTemplate_id())){
			ArrayList<DocReportPrintData> recordList = new ArrayList<DocReportPrintData>();
			recordList.add(printData);
			accumulateInOutDataForRecords(recordList);
		}
		//3、计算打印数据所需要的打印高度
		int rowHight = DocPrintUtil.countRowHightForRecord(printData, templateItemInfo);
		if(0<rowHight && record.getRowHight()!=rowHight){
			record.setRowHight(rowHight);
		}

		return rowHight;
	}

	private int countRowHightForRecord(DocReportPrintData record) throws MnisException{
		//0、验证数据有效性
		if(StringUtils.isEmpty(record.getTemplateID()) || record.getTemplateID().contains("print")) {
			throw new MnisException("模板信息错误。");
		}
		//1、获取模板组件信息
		Map<String, DocTemplateItem> templateItemInfo = getItemsInBandOfListTemplate(record.getTemplateID(),
				DocReportConstants.TEMPLATE_BAND_NAME_DETAIL);
		if(0 >= templateItemInfo.size()){
			throw new MnisException("模板信息错误。");
		}
		//2、计算打印数据所需要的打印高度
		int rowHight = DocPrintUtil.countRowHightForRecord(record, templateItemInfo);
		if(0<rowHight && record.getRowHight()!=rowHight){
			record.setRowHight(rowHight);
		}

		return rowHight;
	}

	/**
	 * 获取LIST类型模板中指定Band的所有组件信息的有序集合
	 * 约束条件：从数据库中获取到的模板组件信息是以item_index升序排列的
	 * @param templateID 模板ID
	 * @param bandName	 指定Band区域
	 * @return 组件信息的有序集合，以组件名称为key，组件的item_index信息作为排序依据
	 */
	private Map<String, DocTemplateItem> getItemsInBandOfListTemplate(String templateID, String bandName){
		Map<String, DocTemplateItem> templateItemInfo = new LinkedHashMap<String, DocTemplateItem>();
		if(StringUtils.isEmpty(templateID) || templateID.contains("print") || StringUtils.isEmpty(bandName)){
			return templateItemInfo;
		}
		DocTemplate template = getTemplateByID(templateID);
		if (null==template) {
			return templateItemInfo;
		}
		DocType docType = reportMapper.getDocTypeByID(template.getDocType());
		String showType = docType.getShowType();
		if (!DocReportConstants.TEMPLATE_TYPE_LIST.equals(showType)) {
			return templateItemInfo;
		}
		for (DocBand band : template.getBands()) {
			if (band.getBandName().contains(bandName)) {
				List<DocTemplateItem> templateItemList = band.getItems();
				for (DocTemplateItem item : templateItemList) {
					templateItemInfo.put(item.getItem_name(), item);
				}
				break;
			}
		}

		return templateItemInfo;
	}

	private void reCountAndUpdateRecordsPrintInfoForPatient(final String patID, final String templateID, final int startPage,
															final int endPage, boolean recountHight) throws MnisException{
		//0、检查参数有效性
		if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID)){
			throw new MnisException("参数为空值。");
		}else if(templateID.contains("print")) {
			throw new MnisException("模板ID错误。");
		}
		if(0!=startPage && 0!=endPage && endPage<startPage) {
			throw new MnisException("指定页码错误。");
		}
		//1、获取模板组件信息
		Map<String, DocTemplateItem> templateItemInfo = getItemsInBandOfListTemplate(templateID, DocReportConstants.TEMPLATE_BAND_NAME_DETAIL);
		if(null==templateItemInfo || templateItemInfo.isEmpty()){
			throw new MnisException("获取模板数据错误");
		}
		//2、获取该患者指定页面文书记录的打印数据
		int numType = 0;//不区分单双面
		boolean isHistory = false;
		List<DocReportPrintData> docRecords = getDocReportPrintDatas(patID, templateID,startPage,endPage,numType,
																	isHistory, templateItemInfo);
		if(null==docRecords || 0>=docRecords.size()) {
			return;
		}
		//如果是护理记录单，需要计算出入量的累计值
		if(isReportTypeHLJL(templateID)){
			accumulateInOutDataForRecords(docRecords);
		}

		//3、根据要求重新计算每条文书记录的行高
		Map<String, DocReportPrintData> updateMap = new HashMap<String, DocReportPrintData>();
		if(recountHight){
			for(DocReportPrintData docRecord : docRecords) {
				int rawHight = docRecord.getRowHight();
				//重算每条记录的行高，如果有变化，则保存到变化集合中
				int newHight = DocPrintUtil.countRowHightForRecord(docRecord, templateItemInfo);
				if(rawHight != newHight){
					docRecord.setRowHight(newHight);
					updateMap.put(docRecord.getRecord_id(), docRecord);

				}
			}
		}

		//4、获取该患者指定模板下所有的出入量统计记录
		List<DocReportPrintData> statisticRecords = getStatisticPrintRecordsForPatient(patID, templateID, startPage, endPage, numType);
		//5、合并文书记录与出入量记录，按时间排序
		List<DocReportPrintData> records  = mergeDocRecordAndStaticRecords(docRecords, statisticRecords);

		//6、获取打印区域高度
		int printHight = getDetailBandHightInListTemplate(templateID);
		int oneRowHight = DocPrintUtil.getOneRowHightForDocPrint();
		if(0>=printHight || 0>=oneRowHight) {
			throw new MnisException("获取模板数据错误。");
		}

		///7、重新计算打印页码
		int startPageNo = 1;
		if(1<startPage){
			startPageNo = startPage;
		}
		countPageNoForRecords(records, printHight, startPageNo, oneRowHight, updateMap);

		//8、更新所有记录的行高和页码
		updatePrintInfoForRecords(new ArrayList<DocReportPrintData>(updateMap.values()));
	}

	/**
	 * 为文书记录中的出入量数据添加累计值信息
	 * 约束条件：1、文书记录按照日期进行排序，相同日期的记录按照recordID排序
	 * 			2、出入量累计以DocPrintConstants.inOutTimeMorning指定的时间为节点
	 * @param records 文书记录集合
     */
    private void accumulateInOutDataForRecords(List<DocReportPrintData> records){
		if(null==records || 0>=records.size()){
			return;
		}
		//获取出入量初始累计信息
		DocReportPrintData firstRecord = records.get(0);
		Map<String,BigDecimal> inOutCount = getInOutAccumulationForSpecifiedTime(firstRecord.getPatID(),firstRecord.getTemplateID(),
													firstRecord.getRecord_id(),firstRecord.getDateList(),firstRecord.getTimeList());
		DocReportPrintData record = null;
		String inOutValue = null;
		String outName = null;
		boolean newDayFlag = true;
		if(DateUtil.after(firstRecord.getTimeList().substring(0,5), DocPrintConstants.inOutTimeMorning,
				DateUtil.DateFormat.HM)){
			//如果第一条记录的时间未到早上统计时间，认为是新的一天
			//如果已经过了早上统计时间，则不算新的一天，仍沿用已有统计数据
			newDayFlag = false;
		}
		String lastDate = firstRecord.getDateList();
		BigDecimal countInfo = null;
		//循环累计出入量
		for (int j = 0; j < records.size(); j++) {
			record = records.get(j);
			if(DateUtil.before(lastDate, record.getDateList(), DateUtil.DateFormat.YMD)){
				//新的一天
				newDayFlag = true;
				lastDate = record.getDateList();
			}
			//如果是新一天的累计开始，则清空出入量累计信息
			if(newDayFlag && DateUtil.after(record.getTimeList().substring(0,5), DocPrintConstants.inOutTimeMorning,
											DateUtil.DateFormat.HM)){
				inOutCount.clear();
				newDayFlag = false;
			}
			for (DocReportPrintDataDetail detail : record.getData_list()) {
				if(DocReportConstants.IN_TAKE.equals(detail.getTemplate_item_id())
						&&!StringUtils.isEmpty(detail.getRecord_value())){
					countInfo = inOutCount.get(DocReportConstants.IN_TAKE);
					countInfo = countInfo==null? BigDecimal.ZERO:countInfo;
					countInfo = countInfo.add(new BigDecimal(detail.getRecord_value()));
					inOutCount.put(DocReportConstants.IN_TAKE, countInfo);
					inOutValue = Integer.toString((new BigDecimal(detail.getRecord_value())).intValue()) + "/"
													+ Integer.toString(countInfo.intValue());
					detail.setRecord_value(inOutValue);
				}else if(DocReportConstants.OUT_TAKE.equals(detail.getTemplate_item_id())
						&& !StringUtils.isEmpty(detail.getRecord_value())){
					outName = "outTake-" + record.getDetailDataByTemplateItemID(DocReportConstants.OUT_NAME).getRaw_value();
					countInfo = inOutCount.get(outName);
					countInfo = countInfo==null? BigDecimal.ZERO:countInfo;
					countInfo = countInfo.add(new BigDecimal(detail.getRecord_value()));
					inOutCount.put(outName, countInfo);
					inOutValue = Integer.toString((new BigDecimal(detail.getRecord_value())).intValue()) + "/"
													+ Integer.toString(countInfo.intValue());
					detail.setRecord_value(inOutValue);
				}
			}
		}
	}

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
	@Override
	public List<String[]> loadPrintDataInListTemplateForPatient(final String patID, String templateID, int startPage,
										  						int endPage, int numType, String startDate,
																String endDate, final boolean isHistory)throws MnisException{
		//0、校验参数有效性
		if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID)) {
			throw new MnisException("患者或模板信息为空。");
		}
		if(templateID.contains("print")){
			templateID = getTemplateIDByPrintID(templateID);
			if(null==templateID) {
				throw new MnisException("模板信息错误。");
			}
		}
		//调整页码参数
		if(0>startPage){
			startPage = 0;
		}
		if(0>endPage){
			endPage = 0;
		}
		if(0<startPage && 0<endPage && startPage>endPage) {
			//交换页码
			int tempInt = startPage;
			startPage = endPage;
			endPage = tempInt;
		}
		//根据指定日期获取页码范围
		Date sDate = DateUtil.parse(startDate, DateUtil.DateFormat.YMD);
		Date eDate = DateUtil.parse(endDate, DateUtil.DateFormat.YMD);
		if(null!=sDate && null!=eDate && sDate.after(eDate)){
			//交换日期
			Date tempDate = sDate;
			sDate = eDate;
			eDate = tempDate;
		}
		startDate =  DateUtil.format(sDate, DateUtil.DateFormat.YMD);
		endDate = DateUtil.format(eDate, DateUtil.DateFormat.YMD);
		if (StringUtil.hasValue(startDate) || StringUtil.hasValue(endDate)) {
			Integer[] datePage = reportMapper.getPrintPageNoForSpecifiedDate(patID, templateID, startDate, endDate);
			/*NOTE，页码设定规则：
			  1、优先使用用户设定页码；
			  2、未设定页码情况下使用指定日期对应的打印页码
			  3、如果同时指定了页码和日期，日期对应的页码不会覆盖指定的页码*/
			if(null != datePage && 0<datePage.length
					&& 0>=startPage && 0>=endPage){
				startPage = datePage[0];
				endPage = datePage[datePage.length - 1];
			}
		}

		//1、检查患者文书数据页码有效性
		int invalidCount = 0;
		if(isHistory) {
			invalidCount = reportMapper.getRecordCountWithInvalidPageNoInHistory(patID, templateID) +
							reportMapper.getStatisticCountWithInvalidPageNo(patID, templateID);
		}else {
			invalidCount = reportMapper.getRecordCountWithInvalidPageNo(patID, templateID) +
							reportMapper.getStatisticCountWithInvalidPageNo(patID, templateID);
		}
		if(0<invalidCount){
			//存在无效页码的文书记录，则重新计算该患者的所有文书记录
			reCountAndUpdateRecordsPrintInfoForPatient(patID, templateID,0,0,true);
		}

		//获取模板数据
		Map<String, DocTemplateItem> templateItemInfo = getItemsInBandOfListTemplate(templateID, DocReportConstants.TEMPLATE_BAND_NAME_DETAIL);
		if(null==templateItemInfo || 0>=templateItemInfo.size()){
			throw new MnisException("获取模板数据错误");
		}
		//2、获取患者所有文书记录的打印数据
		//由于涉及到出入量累计，需要取出指定页码内所有的记录，计算过出入量后再筛选出奇偶页
		int numTypeAll = 0;
		List<DocReportPrintData> records = getDocReportPrintDatas(patID, templateID, startPage, endPage, numTypeAll,
																isHistory, templateItemInfo);
		if(null==records || 0>=records.size()) {
			throw new MnisException("获取文书记录为空。");
		}
		//如果是护理记录，需要对每条记录累计出入量数据
		if(isReportTypeHLJL(templateID)){
			accumulateInOutDataForRecords(records);
		}
		//挑选出指定页码类型的记录
		getNumTypeRecords(records, numType);

		//获取患者所有出入量统计信息
		List<DocReportPrintData> statisticRecords = getStatisticPrintRecordsForPatient(patID, templateID, startPage, endPage, numType);

		//合并记录数据和出入量统计数据
		List<DocReportPrintData> printRecords = mergeDocRecordAndStaticRecords(records, statisticRecords);

		//3、将数据组织成字符串数组
		List<String[]> printStrList = getDocPrintStringFromRecordData(printRecords, templateItemInfo);

		//根据奇偶页打印方式处理打印顺序
		List<String[]> printData = dealPageOrderWithNumType(printStrList, numType);

		return printData;
	}

	/**
	 * 根据文书模板ID判断是否护理记录单
	 * @param templateID 模板编号
	 * @return 护记返回true，否则返回fals
     */
    private boolean isReportTypeHLJL(String templateID) {
		if(StringUtils.isEmpty(templateID)){
			return false;
		}
		DocTemplate template = getTemplateByID(templateID);
		if(null==template){
			return false;
		}
		return DocReportConstants.REPORT_TYPE_HLJL.equals(template.getReport_type());
	}

	/**
	 * 根据奇偶页打印方式调整打印记录字符串的顺序
	 * 约束条件：输入的打印字符串集合已经按照页码从小到大进行排序
	 * @param printStrList 打印记录的字符串集合
	 * @param numType 奇偶页打印方式
     * @return 顺序调整后的字符串集合
     */
    private List<String[]> dealPageOrderWithNumType(List<String[]> printStrList, int numType){
		if (printStrList == null || printStrList.isEmpty()) {
			return printStrList;
		}
		//非偶数页打印不需要进行处理
		if(2!=numType){
			return printStrList;
		}

		ArrayList<String[]> orderedStrList = new ArrayList<>();
		String oldPageNo = printStrList.get(0)[printStrList.get(0).length-1];
		ArrayList<String[]> pageStrs = new ArrayList<String[]>();
		for (String[] printStr : printStrList) {
			String pageNo = printStr[printStr.length - 1];
			if(!pageNo.equals(oldPageNo)){
				//新的一页，把上页的数据放进集合
				orderedStrList.addAll(0, pageStrs);
				pageStrs = new ArrayList<String[]>();
			}
			pageStrs.add(printStr);
			oldPageNo = pageNo;
		}
		//最后一页放进集合
		orderedStrList.addAll(0, pageStrs);

		return orderedStrList;
	}

	/**
	 * 在文书记录集合中获取指定奇偶页码类型的记录
	 * @param records 记录集合
	 * @param numType 指定页码类型
     */
    private void getNumTypeRecords(List<DocReportPrintData>records, int numType){
		if(null==records || 0>=records.size()){
			return;
		}
		if(0>=numType || 2<numType){
			return;
		}
		numType = numType%2;
		for (int i = 0; i < records.size(); i++) {
			if(numType != records.get(i).getPageNo()%2){
				records.remove(i);
				i--;
			}
		}
	}

	/**
	 * 把文书记录的数据转换为字符串以供前端显示
	 * 约束条件：1、文书记录如果包含出入量，则出入量记录标志要符合isInOutStatisticRecord函数中的约束
	 * 			2、模板信息是Item的有序集合，以item_index升序排列
	 * @param printRecords 文书记录数据集合，可以包含出入量数据
	 * @param templateItemInfo 对应的模板信息
     * @return 文书记录数据的字符串数组集合，一条数据对应一个数组
     */
    private List<String[]> getDocPrintStringFromRecordData(List<DocReportPrintData> printRecords, Map<String, DocTemplateItem> templateItemInfo) {
		List<String[]> printDataList = new ArrayList<String[]>();
		if(null==printRecords || 0>=printRecords.size() || null==templateItemInfo
				|| 0>=templateItemInfo.size()){
			return printDataList;
		}
		//字符串数组长度 = detail区域组件个数 + 2(行高和页码)
		final int strLength = templateItemInfo.size() + 2;
		//以上获取到的templateItemInfo是以item_index进行排序的，因此可以取第一条作为基址序号
		final int baseIndex = (new ArrayList<>(templateItemInfo.values())).get(0).getIndex();
		for(DocReportPrintData record : printRecords) {
			if(null==record.getData_list() || 0>=record.getData_list().size()){
				continue;
			}
			if(isInOutStatisticRecord(record)){
				//出入量统计信息
				String[] inOutStr = new String[3];
				//NOTE：此处字符串的组织形式是按前端要求进行处理的，但是个人感觉更合理的做法是把基本数据元素传给前端
				//前端按照具体的需求去组织显示形式，这样可以既可以应对不同客户端的不同显示需求，也可以防止由于需求变更迫使后端改动代码
				inOutStr[0] = record.getDateList() + " " + record.getTimeList() + " " + "统计" + " " + record.getData_list().get(0).getRecord_value()
								+ " " + record.getData_list().get(0).getRaw_value();
				inOutStr[1] = Integer.toString(record.getRowHight());
				inOutStr[2] = Integer.toString(record.getPageNo());
				printDataList.add(inOutStr);
			}else {
				String[] dataStr = new String[strLength];
				dataStr[strLength - 1] = Integer.toString(record.getPageNo());
				dataStr[strLength - 2] = Integer.toString(record.getRowHight());
				for(DocReportPrintDataDetail listData : record.getData_list()) {
					try{
						dataStr[templateItemInfo.get(listData.getTemplate_item_id()).getIndex()-baseIndex] = DocPrintUtil.getItemContentForPrint(listData);
					}catch (Exception e){
						//do nothing, 数据填充过程中如果有异常（空指针，下标越界等等），则忽略并继续取下一个值
					}
				}
				for(int i=0;i<strLength;i++) {
					//空值元素填充为空字符串
					if(null == dataStr[i])
						dataStr[i] = "";
				}
				printDataList.add(dataStr);
			}
		}
		return printDataList;
	}

	private List<DocReportPrintData> getDocReportPrintDatas(String patID, String templateID, int startPage,
															int endPage, int numType, boolean isHistory,
															Map<String, DocTemplateItem> templateItemInfo) {

		List<DocReportPrintData> records = new ArrayList<DocReportPrintData>();
		if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID)){
			return records;
		}
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("patID",patID);
		paramMap.put("templateID", templateID);
		paramMap.put("startPage", startPage);
		paramMap.put("endPage", endPage);
		paramMap.put("numType", numType);
		paramMap.put("itemList", new ArrayList<String>(templateItemInfo.keySet()));
		records = reportMapper.getRecordsPrintDataForPatient(paramMap);
		if(isHistory && (null==records || 0>=records.size())){
			//历史文书
			records = reportMapper.getRecordsPrintDataForPatientInHistory(paramMap);
		}
		return records;
	}

	/**
	 * 判断文书记录是否为出入量统计信息
	 * 判断依据：	记录ID是否“InOutStatistics”。该依据后续根据需要可能会有变化，因此独立成一个函数。
	 * @param record
	 * @return
	 */
	private boolean isInOutStatisticRecord(DocReportPrintData record){
		return "InOutStatistics".equals(record.getRecord_id());
	}

	/**
	 * 把出入量数据合并到文书记录中形成一个新的合集，按时间排序
	 * @param docRecords 文书记录
	 * @param statisticRecords 出入量信息，以文书记录形式
	 * @return
	 */
	private List<DocReportPrintData> mergeDocRecordAndStaticRecords(final List<DocReportPrintData> docRecords,
																	final List<DocReportPrintData> statisticRecords){
		List<DocReportPrintData> records = new ArrayList<DocReportPrintData>();
		if(null==docRecords || 0>=docRecords.size()){
			return records;
		}else if(null==statisticRecords || 0>=statisticRecords.size()){
			return docRecords;
		}
		int totalSize = docRecords.size() + statisticRecords.size();
		int docIndex=0;
		int statIndex=0;
		for(int index=0; index<totalSize; index++){
			if(docRecords.get(docIndex).after(statisticRecords.get(statIndex))){
				records.add(statisticRecords.get(statIndex));
				statIndex++;
				if(statIndex>=statisticRecords.size()) {
					records.addAll(docRecords.subList(docIndex, docRecords.size()));
					break;
				}
			}else {
				records.add(docRecords.get(docIndex));
				docIndex++;
				if(docIndex >= docRecords.size()) {
					records.addAll(statisticRecords.subList(statIndex, statisticRecords.size()));
					break;
				}
			}
		}

		return records;
	}

	private List<DocReportPrintData> getStatisticPrintRecordsForPatient(final String patID, final String templateID,
																		final int startPage, final int endPage,
																		final int numType) {
		List<DocReportPrintData> statisticRecords = new ArrayList<DocReportPrintData>();
		if (!StringUtil.hasValue(patID) || !StringUtil.hasValue(templateID) || startPage>endPage) {
			return statisticRecords;
		}
		//获取统计信息
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("patID", patID);
		paramMap.put("templateID", templateID);
		paramMap.put("startPage", startPage);
		paramMap.put("endPage", endPage);
		paramMap.put("numType", numType);
		List<DocRecordDataStatistic> statisticList = reportMapper.getStatisticDataByPageNo(paramMap);
		statisticList = showDocRecordDataStatistics(statisticList,DocPrintConstants.inOutTimeMorning,
				DocPrintConstants.inOutTimeAfternoon, null);
		//组织成DocReportPrintData形式
		int oneRowHight = DocPrintUtil.getOneRowHightForDocPrint();
		for(DocRecordDataStatistic statisticData : statisticList){
			for(DocRecordStatistic data : statisticData.getDocRecordStatistics()){
				DocReportPrintDataDetail recordDetail = new DocReportPrintDataDetail();
				recordDetail.setTemplate_item_id("InOutStatistics");
				recordDetail.setRecord_value(data.getStaticRawValue());
				recordDetail.setRaw_value("(" + data.getHourDiff() +")");
				DocReportPrintData statisticRecord = new DocReportPrintData();
				statisticRecord.setData_list(new ArrayList<DocReportPrintDataDetail>());
				statisticRecord.getData_list().add(recordDetail);
				//NOTE：出入量统计信息组织成的文书记录，ID设置为InOutStatistics，方便后续判断
				statisticRecord.setRecord_id("InOutStatistics");
				statisticRecord.setPatID(patID);
				statisticRecord.setTemplateID(templateID);
				statisticRecord.setDateList(statisticData.getStaticDate());
				statisticRecord.setTimeList(data.getStaticTime());
				statisticRecord.setRowHight(oneRowHight);
				statisticRecord.setPageNo(data.getPageNo());
				statisticRecords.add(statisticRecord);
			}
		}
		return statisticRecords;
	}

	private DocRecordStatistic getStatisticInfoByRecordDateTime(final Map<String, Map<String, DocRecordStatistic>> statisticMap,
																final String strDate, final String strTime){
		if(null==statisticMap || 0>=statisticMap.size() || StringUtils.isEmpty(strDate) || StringUtils.isEmpty(strTime)){
			return null;
		}
		Date date = DateUtil.parse(strDate, DateUtil.DateFormat.YMD);
		Date time = DateUtil.parse(strTime.substring(0,5), DateUtil.DateFormat.HM);
		Date inOutTimeMorning = DateUtil.parse(DocPrintConstants.inOutTimeMorning, DateUtil.DateFormat.YMD);
		Date inOutTimeAfternoon = DateUtil.parse(DocPrintConstants.inOutTimeAfternoon, DateUtil.DateFormat.HM);
		String newDate = strDate;
		String newTime = strTime;
		if(null==date || null==time || null==inOutTimeMorning || null==inOutTimeAfternoon){
			return null;
		}
		if(time.before(inOutTimeMorning) || time.equals(inOutTimeMorning)){
			newTime = DocPrintConstants.inOutTimeMorning;
		}else if(time.before(inOutTimeAfternoon) || time.equals(inOutTimeAfternoon)){
			newTime = DocPrintConstants.inOutTimeAfternoon;
		}else {
			newDate = DateUtil.format(DateUtils.addDays(date, 1), DateUtil.DateFormat.YMD);
			newTime = DocPrintConstants.inOutTimeMorning;
		}

		return statisticMap.get(newDate).get(newTime);
	}


	private Map<String, Map<String, DocRecordStatistic>> getStatisticMapForPatient(final String patID, final String deptCode,
																				   final String templateID,final String startDate,
																				   final String endDate,final String admissionTime) {
		Map<String, Map<String, DocRecordStatistic>> statisticMap = new HashMap<String, Map<String, DocRecordStatistic>>();
		if (StringUtils.isEmpty(patID) || StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(startDate)
				|| StringUtils.isEmpty(endDate)) {
			return statisticMap;
		}
		//获取所有统计信息
		List<DocRecordDataStatistic> docRecordDataStatistics = reportMapper
				.getDocReprotDataAllStatistic(patID, deptCode, startDate, endDate,templateID);
		docRecordDataStatistics = showDocRecordDataStatistics(docRecordDataStatistics, DocPrintConstants.inOutTimeMorning,
				DocPrintConstants.inOutTimeAfternoon, null);
		//根据日期时间组织成MAP集合
		for(DocRecordDataStatistic record : docRecordDataStatistics){
			Map<String, DocRecordStatistic> dataMap = new HashMap<String, DocRecordStatistic>();
			for(DocRecordStatistic data : record.getDocRecordStatistics()) {
				dataMap.put(data.getStaticTime(), data);
			}
			statisticMap.put(record.getStaticDate(), dataMap);
		}

		return statisticMap;
	}

	private DocRecord DocReportPrintData2DocRecord(final DocReportPrintData printRecord){
		if(null == printRecord){
			return null;
		}
		DocRecord record = new DocRecord();
		record.setRecordId(printRecord.getRecord_id());
		record.setDate_list(printRecord.getDateList());
		record.setTime_list(printRecord.getTimeList());
		record.setRowHight(printRecord.getRowHight());
		record.setPageNo(printRecord.getPageNo());

		return record;
	}

	private DocReportPrintData DocRecord2DocReportPrintData(final DocRecord record){
		if(null == record){
			return null;
		}
		DocReportPrintData printRecord = new DocReportPrintData();
		printRecord.setRecord_id(record.getRecordId());
		printRecord.setDateList(record.getDate_list());
		printRecord.setTimeList(record.getTime_list());
		printRecord.setRowHight(record.getRowHight());
		printRecord.setPageNo(record.getPageNo());

		return printRecord;
	}

	/**
	 * 文书记录的操作权限判定
	 * @param record 文书记录信息
	 * @return
     */
    private void checkPermissionBeforRecordOperation(DocRecord record){
//		if(null==record || StringUtils.isEmpty(record.getDate_list()) || StringUtils.isEmpty(record.getTime_list())
//				|| StringUtils.isEmpty(record.getTemplate_id())){
//			return false;
//		}
//		//1、开放时间前的数据不允许修改
//		Date date = DateUtil.parse(record.getDate_list() + " " + record.getTime_list());
//		Calendar theDate = Calendar.getInstance();
//		if(null==date){
//			return false;
//		}else{
//			theDate.add(Calendar.HOUR_OF_DAY, 0-DocReportConstants.RECORD_OPEN_INTERVAL);
//			if(date.before(theDate.getTime())){
//				return false;
//			}
//		}
//		//2、非本科室人员不允许编辑和删除记录
//		String[] str = record.getCreate_person().split("-");
//		if(0 >= str.length){
//			return false;
//		}
//		String deptCode = ((DocTemplate)TemplateCache.getTemplateByID(record.getTemplate_id())).getDeptCode();
//		if(!deptCode.equals(identityService.queryUserByCode(str[0]).getDeptCode()) && !"0".equals(deptCode)){
//			return false;
//		}

		//已经审核过的记录不允许编辑
		if(!StringUtils.isEmpty(record.getRecordId())){
			DocRecord origRecord = reportMapper.getDocRecordByID(record.getRecordId());
			if(null!=origRecord && null!=origRecord.getApprove_status()
					&& "Y".equals(origRecord.getApprove_status().toUpperCase())){
				throw new MnisException("已审核的护记，不允许在保存！");
			}
		}
	}

	/**
	 * 记录用户的打印信息
	 * @param printInfo 打印信息记录
	 * @throws MnisException
     */
    @Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void recordPatPrintInfo(PrintInfoRecord printInfo) throws MnisException{
		if (printInfo == null) {
			throw new MnisException("打印信息为空。");
		}
		switch (DocReportConstants.PatPrintType.getIndexByName(printInfo.getPrintType())){
			case 0://打印文书
				saveDocPrintInfo(printInfo);
				break;
			case 1://打印体温单
				saveTempraturePrintInfo(printInfo);
				break;
			default:
				throw new MnisException("非法打印类型。");
		}

	}

	private void saveDocPrintInfo(PrintInfoRecord printInfo){
		if (printInfo == null) {
			return;
		}
		int startNo = 0, endNo = 0;
		try {
			startNo = Integer.parseInt(printInfo.getPrintStart());
			endNo = Integer.parseInt(printInfo.getPrintEnd());
		}catch (Exception e){
			return;
		}
		if(startNo>endNo && 0<endNo){
			return;
		}
		if(0 >= startNo){
			//为0表示从头开始打印
			startNo = 1;
		}
		if(0 >= endNo){
			//为0表示打印到结束为止
			//NOTE:这里是从文书记录表中取最大值，因此不能处理历史记录打印的情况
			//根据文书功能的设定，患者出院或转科时就应该把对应的文书打印出来，不希望用户到历史数据进行打印
			endNo = reportMapper.getMaxPageNoOfPatDocRecords(printInfo.getPatID(), printInfo.getTemplateID());
			if(0>=endNo){
				LOGGER.error("文书页码为空导致打印信息记录失败："
						+ printInfo.getCreatePerson() + printInfo.getPatID()+ printInfo.getTemplateID()
						+ printInfo.getPrintStart() + printInfo.getPrintEnd());
			}
		}
		for(int i=startNo; i<=endNo; i++){
			printInfo.setPrintStart(Integer.toString(i));//使用起始字段保存打印信息
			reportMapper.savePatPrintInfo(printInfo);
		}
	}

	private void saveTempraturePrintInfo(PrintInfoRecord printInfo){
		if (printInfo == null) {
			return;
		}
		Date startDate = DateUtil.parse(printInfo.getPrintStart());
		Date endDate = DateUtil.parse(printInfo.getPrintEnd());
		//体温单打印必须要指定起始和终止日期
		if(null==startDate || null==endDate || startDate.after(endDate)){
			return;
		}
		//起始和终止日期相差天数
		int dayCnt = DateUtil.diffDate(startDate,endDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		//起始日期的前一天作为起始，方便后续每次+1计算日期
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		for(int i=0; i<=dayCnt; i++){
			//从起始日期开始，每次加一天
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			Date newDate = calendar.getTime();
			printInfo.setPrintStart(DateUtil.format(calendar.getTime(), DateUtil.DateFormat.YMD));
			reportMapper.savePatPrintInfo(printInfo);
		}

	}

	/**
	 * 更新文书记录对应的出入量信息到生命体征
	 * @param record 当前操作的文书记录
	 * @param oldRecordInfo 操作前的文书记录信息
	 * @throws MnisException 当前文书信息缺失
     */
    private void updatedStatisticToTempratureSheet(DocRecord record, DocRecord oldRecordInfo,
												   List<DocDataRecordItem> oldItems)throws MnisException{
		if (record == null || StringUtils.isEmpty(record.getDate_list())
				|| StringUtils.isEmpty(record.getTime_list())) {
			throw new MnisException("文书记录信息缺失。");
		}
		final String patID = record.getInpatient_no();
		final String deptCode = ((DocTemplate)TemplateCache.getTemplateByID(record.getTemplate_id())).getDeptCode();
		final String templateID = record.getTemplate_id();
		if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(templateID)){
			//参数信息不全，无法获取到出入量数据
			throw new MnisException("文书记录信息缺失。");
		}
		ArrayList<BodySignRecord> records = new ArrayList<>();
		final String statisticDate = getStatisticDateForSpecifiedTime(record.getDate_list(), record.getTime_list());
		//如果原有记录存在，且出入量归属到不同的统计日期，则需要更新原有的出入量信息到体温单
		if (oldRecordInfo != null && !StringUtils.isEmpty(oldRecordInfo.getDate_list())
				&& !StringUtils.isEmpty(oldRecordInfo.getTime_list())) {
			String oldStatisticDate = getStatisticDateForSpecifiedTime(oldRecordInfo.getDate_list(), oldRecordInfo.getTime_list());
			if(null!=oldStatisticDate && !oldStatisticDate.equals(statisticDate)){
				//获取统计量信息
				DocReportPrintData statisticData = getRecordStatisticForBodySign(patID, deptCode, oldStatisticDate, templateID);
				if (statisticData != null) {
					//如果记录变更导致原有的某些出入量值被删除，那么体温单上对应的记录要清零
					//暂时不需要，因为出入量统计保留了0值。留待以后需要时处理
					//addDeletedStatisticDataForBodysign(statisticData.getData_list(), oldItems);
					//出入量统计信息更新到体温单前一天去
					statisticData.setDateList(DateUtil.addDate(oldStatisticDate, -1));
					records.add(docRecord2BodysignRecord(statisticData, "0000-statistic"));
				}
			}

		}
		//获取本条记录对应的出入量信息
		DocReportPrintData statisticData = getRecordStatisticForBodySign(patID, deptCode, statisticDate, templateID);
		if (statisticData != null) {
//			addDeletedStatisticDataForBodysign(statisticData.getData_list(), oldItems);
			statisticData.setDateList(DateUtil.addDate(statisticDate, -1));
			records.add(docRecord2BodysignRecord(statisticData, "0000-statistic"));
		}

		//更新生命体征信息到数据库表
		for (BodySignRecord bodysignRecord : records) {
			bodySignService.updateBodySignRecordValue(bodysignRecord);
		}
	}

	/**
	 * 在统计数据中加入被删除掉的出入量信息，对应统计值设置为0
	 * @param statisticData 统计信息
	 * @param oldItems 文书记录数据
     */
    private void addDeletedStatisticDataForBodysign(List<DocReportPrintDataDetail> statisticData, List<DocDataRecordItem> oldItems){
		if (statisticData == null || null==oldItems) {
			return;
		}
		//获取统计数据中的项目信息
		HashMap<String, DocReportPrintDataDetail> itemMap = new HashMap<>();
		for (DocReportPrintDataDetail detail : statisticData) {
			itemMap.put(detail.getTemplate_item_id(), detail);
		}
		//遍历原有文书记录数据，找出被删除掉的出入量
		for (DocDataRecordItem oldItem : oldItems) {
			String itemName = null;
			if(DocReportConstants.IN_TAKE.equals(oldItem.getTemplate_item_id())
					&&StringUtils.isEmpty(oldItem.getRecord_value())){
				itemName = DocReportConstants.INOUT_ITEM_TO_BODYSIGN.get(DocReportConstants.INOUT_METADATA_TOTALINPUT);
			}
			if(DocReportConstants.OUT_NAME.equals(oldItem.getTemplate_item_id())
					&&StringUtils.isEmpty(oldItem.getRecord_value())){
				itemName = DocReportConstants.INOUT_ITEM_TO_BODYSIGN.get(oldItem.getRecord_value());
			}
			if (!StringUtils.isEmpty(itemName) && null==itemMap.get(itemName)) {
				//原有记录的出入量信息在新的统计数据中不存在，说明被删除掉了
				//那么现在把这个信息加上，值设为零
				DocReportPrintDataDetail addDetail = new DocReportPrintDataDetail();
				addDetail.setTemplate_item_id(itemName);
				addDetail.setRecord_value(Integer.toString(0));
				statisticData.add(addDetail);
			}
		}
	}

	/**
	 * 计算指定时间所归属的24H出入量统计日期
	 * @param date 指定日期字符串，YMD格式
	 * @param time 指定时间，HM格式
     * @return 统计日期，YMD格式
     */
    private String getStatisticDateForSpecifiedTime(String date, String time) {
		date = DateUtil.format(DateUtil.parse(date.substring(0,10), DateUtil.DateFormat.YMD), DateUtil.DateFormat.YMD);
		time = DateUtil.format(DateUtil.parse(time.substring(0,5), DateUtil.DateFormat.HM), DateUtil.DateFormat.HM);
		if(StringUtils.isEmpty(date)){
			return null;
		}
		if(StringUtils.isEmpty(time)){
			return date;
		}
		String newDate = date;
		if(DateUtil.after(time.substring(0,5), DocPrintConstants.inOutTimeMorning, DateUtil.DateFormat.HM)){
            //过了24h统计节点的出入量要更新到第二天的统计数据中去
            newDate = DateUtil.addDate(date, 1);
        }
		return newDate;
	}

	/**
	 * 更新文书记录中体温最大值的的生命体征信息到数据库体征表
	 * 1、体温最大值从操作文书记录所在的时间段内取
	 * 2、操作文书记录体温值为该时段内最大时更新到生命体征
	 * @param record 操作的文书记录信息
     */
    private void updatedBodySignToTempratureSheet(DocRecord record) throws MnisException{
		if (record == null || StringUtils.isEmpty(record.getDate_list())
				|| StringUtils.isEmpty(record.getTime_list())) {
			throw new MnisException("文书记录信息缺失。");
		}

		//获取记录时间对应到体温单的时间区间
		String[] timeInterval = identityService.getTempratureTimeIntervalOfSpecifiedTime(record.getDate_list(),
				record.getTime_list());
		if(null==timeInterval || 4>timeInterval.length){
			throw new MnisException("体温单时间信息获取失败。");
		}
		//获取时间区间对应的体温单时间节点
		String[] inputTime = identityService.getTemperatureInputTimeByTimeInterval(timeInterval);
		if (inputTime == null || 2>inputTime.length) {
			throw new MnisException("体温单时间信息获取失败。");
		}

		final String patID = record.getInpatient_no();
		final String templateID = record.getTemplate_id();
		//保存文书的时候，前端居然连科室编号都没传进来？这坑是有多深……
		final String deptCode = ((DocTemplate)TemplateCache.getTemplateByID(record.getTemplate_id())).getDeptCode();
		//检查该时间段内是否已经有降温处理
		List<BodySignRecord> excludRecords = bodySignService.getBodySignRecordsWithCoolingProcess(patID,
																	deptCode, inputTime[0], inputTime[1]);
		List<BodySignRecord> records = new ArrayList<BodySignRecord>();
		if(null==excludRecords || 0>=excludRecords.size()){
			//获取时间段内体温最大值的记录信息
			DocRecord docRecord = getRecordWithMaxTempratureInTimeInterval(patID, templateID, timeInterval);
			if(null!= docRecord){
				DocReportPrintData theRecord = reportMapper.getPrintDataForRecord(docRecord.getRecordId(),
																	DocReportConstants.BODYSIGN_NAME_LIST);
				//把记录的时间设置为所归属的体温单时间
				theRecord.setDateList(inputTime[0]);
				theRecord.setTimeList(inputTime[1]);
				//转换文书记录数据到生命体征数据结构
				records.add(docRecord2BodysignRecord(theRecord, record.getCreate_person()));
			}
		}

		//更新生命体征信息到数据库表
		for (BodySignRecord bodysignRecord : records) {
			bodySignService.updateBodySignRecordValue(bodysignRecord);
		}
	}

	/**
	 * 获取指定时间段内，具有体温记录最大值的文书记录信息
	 * @param patID 患者住院号
	 * @param templateID 模板编号
	 * @param timeInterval 指定时间段，字符串数组，包换元素：0，起始日期；1，起始时间；2、结束日期；3、结束时间
     * @return 文书记录信息，不包含记录数据
     */
    private DocRecord getRecordWithMaxTempratureInTimeInterval(String patID, String templateID, String[] timeInterval) {
		if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID)
				|| null==timeInterval || 0>=timeInterval.length){
			return null;
		}
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("patID", patID);
		paramMap.put("templateID", templateID);
		paramMap.put("startDate", timeInterval[0]);
		paramMap.put("startTime", timeInterval[1]);
		paramMap.put("endDate", timeInterval[2]);
		paramMap.put("endTime", timeInterval[3]);
		return reportMapper.getRecordWithMaxTempratureInTimeInterval(paramMap);
	}

	private BodySignRecord docRecord2BodysignRecord(DocReportPrintData docRecord, String createPerson){
		if(null==docRecord || StringUtils.isEmpty(docRecord.getPatID())
				|| StringUtils.isEmpty(createPerson)
				|| StringUtils.isEmpty(docRecord.getDateList())
				|| StringUtils.isEmpty(docRecord.getTimeList())){
			return null;
		}
		String[] operator = createPerson.split("-");
		if(2>operator.length){
			//文书记录操作用户的信息格式不对
			return null;
		}
		BodySignRecord record = new BodySignRecord();
		//设置患者信息
		Patient patient = patientService.getPatientByPatId(docRecord.getPatID());
		record.setPatientId(patient.getPatId());
		record.setInHospNo(patient.getInHospNo());
		record.setPatientName(patient.getName());
		record.setDeptCode(patient.getDeptCode());
		record.setBedCode(patient.getBedCode());
		//设置时间信息
		//NOTE:创建/修改的时间/用户都设置成当前时间/用户，方便后续创建/修改记录时使用
		Date date = DateUtil.parse(docRecord.getDateList() + " " + docRecord.getTimeList(), DateUtil.DateFormat.YMDHM);
		record.setFullDateTime(date);
		record.setRecordDay(docRecord.getDateList());
		record.setRecordTime(docRecord.getTimeList());
		record.setFirstDate(new Date());
		record.setModifyTime(new Date());
		//设置用户信息
		record.setRecordNurseCode(operator[0]);
		record.setRecordNurseName(operator[1]);
		record.setModifyNurseCode(operator[0]);
		record.setModifyNurseName(operator[1]);
		//设置生命体征信息
		//NOTE:如果文书模板组件名称与生命体征所使用的itemcode不一致，需要进行转换
		List<BodySignItem> bdsnList = new ArrayList<BodySignItem>();
		for (DocReportPrintDataDetail dataDetail : docRecord.getData_list()) {
			BodySignItem bodySignItem = new BodySignItem();
			//模板组件名称作为itemCode
			bodySignItem.setItemCode(dataDetail.getTemplate_item_id());
			bodySignItem.setItemValue(dataDetail.getRecord_value());
			bodySignItem.setMeasureNoteCode(dataDetail.getTemplate_item_id());
			bodySignItem.setPatId(record.getPatientId());
			bodySignItem.setRecordDate(record.getFullDateTime());
			bdsnList.add(bodySignItem);
		}
		record.setBodySignItemList(bdsnList);

		return record;
	}

	/**
	 * 获取指定时间段内，具有体温记录最大值的文书记录数据（主要为生命体征数据，以及出入量）
	 * @param patID 患者住院号
	 * @param deptCode 病区编号
	 * @param date 指定日期
	 * @param time 指定时间
	 * @return 文书记录数据，以生命体征数据结构的形式组织，元素个数固定为体温录入点的个数
	 * NOTE:感觉这个功能放在生命体征服务中去做更合适一些，可是这样的话又会引入体征服务对文书包的依赖，
	 * 		造成循环依赖；希望后续重构的时候可以考虑一下。
	 */
	@Override
	public List<BodySignRecordVo> getDocRecordInfoWithMaxTemprature(String patID, String deptCode, String date, String time){
		List<BodySignRecordVo> recordsVo=new ArrayList<BodySignRecordVo>();
		if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(date)){
			return  recordsVo;
		}
		//获取降温处理的记录
		List<BodySignRecord> excludRecords = bodySignService.getBodySignRecordsWithCoolingProcess(patID, deptCode, date, time);
		//获取需要查询的时间
		List<String> timeList = new ArrayList<String>();
		if(!StringUtils.isEmpty(time)){
			if(excludRecords.isEmpty()){
				//指定的时间内没有降温处理的记录
				timeList.add(time);
			}
		}else {
			//未指定时间，在当天的时间点中去除进行降温处理的时间点
			timeList = new ArrayList<String>(Arrays.asList(identityService.getTempratureInputTimeArray()));
			for (BodySignRecord record : excludRecords) {
				String excludTime = DateUtil.format(record.getFullDateTime(), DateUtil.DateFormat.HM);
				if(StringUtils.isEmpty(excludTime)){
					continue;
				}
				for (int i=0; i<timeList.size(); i++) {
					if(excludTime.equals(timeList.get(i))){
						timeList.remove(i);
						break;
					}
				}
			}
		}
		List<BodySignRecord> records = new ArrayList<BodySignRecord>();
		//获取文书记录需要指定具体的模板
		String templateID = null;
		try {
			templateID = getDefaultTemplateID(patID, deptCode);
			//获取从前一天到当天24h统计节点的出入量数据
			
			String newDate = DateUtil.addDate(date, 1);
			DocReportPrintData statisticData = getRecordStatisticForBodySign(patID, deptCode, newDate, templateID);
			
			if (statisticData != null) {
				records.add(docRecord2BodysignRecord(statisticData, "0000-statistic"));//出入量统计数据没有创建人，暂且用"0000-statistic"代替
			}
			//获取指定区段的文书记录信息
			for (String theTime : timeList) {
				String[] timeInterval = identityService.getTempratureTimeIntervalOfSpecifiedTime(date, theTime);
				DocRecord docRecord = getRecordWithMaxTempratureInTimeInterval(patID, templateID, timeInterval);
				if (docRecord == null) {
					//没有相关记录，继续下一个时间段
					continue;
				}
				DocReportPrintData recordData = reportMapper.getPrintDataForRecord(docRecord.getRecordId(), DocReportConstants.BODYSIGN_NAME_LIST);
				if (recordData != null) {
					//文书记录转为生命体征数据格式
					String[] inputTime = identityService.getTemperatureInputTimeByTimeInterval(timeInterval);
					//设置为生命体征时间节点
					recordData.setDateList(inputTime[0]);
					recordData.setTimeList(inputTime[1]);
					records.add(docRecord2BodysignRecord(recordData, docRecord.getCreate_person()));
				}
			}
		} catch (Exception e) {
			//无法获取到该科室的模板信息等异常，后续使用空的文书记录
			e.printStackTrace();
		}

		recordsVo = bodySignService.getBodySignRecordVosFromBodySignRecords(records);

		return recordsVo;
	}

	private DocReportPrintData getRecordStatisticForBodySign(String patID, String deptCode, String date, String templateID) {
		if (StringUtils.isEmpty(patID) || StringUtils.isEmpty(deptCode)
				|| StringUtils.isEmpty(date) || StringUtils.isEmpty(templateID)) {
			return null;
		}
		if (null == DateUtil.parse(date, DateUtil.DateFormat.YMD)) {
			return null;
		}

		DocRecordDataStatistic dataStatistic = getDocReprotDataStatistic(patID, deptCode, templateID, date, DocPrintConstants.inOutTimeMorning);
		if (dataStatistic == null) {
			return null;
		} else {
			//组织成文书记录的形式
			DocReportPrintData record = new DocReportPrintData();
			record.setPatID(patID);
			record.setTemplateID(templateID);
			record.setDateList(date);
			record.setTimeList(DocReportConstants.ZERO_HM_TIME);//根据体征那边的规则，出入量统计数据时间设置为零点
			ArrayList<DocReportPrintDataDetail> detailList = new ArrayList<DocReportPrintDataDetail>();
			int outCnt = 0;
			//NOTE:以下循环处理的约束条件是，出入量按照值从大到小进行排序
			for (DocRecordStatistic statisticData : dataStatistic.getDocRecordStatistics()) {
				if (DocReportConstants.INOUT_ITEM_TO_BODYSIGN.get(statisticData.getStaticItemType()) != null) {
					//取入量统计和尿量
					DocReportPrintDataDetail detail = new DocReportPrintDataDetail();
//					detail.setTemplate_item_id(DocReportConstants.INOUT_ITEM_TO_BODYSIGN.get(statisticData.getStaticItemType()));
					detail.setTemplate_item_id(statisticData.getStaticItemType());
					detail.setRecord_value(statisticData.getStaticValue());
					detailList.add(detail);
				}else if (MnisConstants.DOC2BODYSIGN_OTHEROUT_COUNT > outCnt){
					//取出量数据中统计值最大的前三个
					DocReportPrintDataDetail detail = new DocReportPrintDataDetail();
//					detail.setTemplate_item_id(DocReportConstants.OUT_ITEMNAME_IN_BODYSIGN[outCnt++]
//							+ "-" +statisticData.getStaticItemType());
					detail.setTemplate_item_id(statisticData.getStaticItemType());
					detail.setRecord_value(statisticData.getStaticValue());
					detailList.add(detail);
					outCnt++;
				}
			}

			//加入大便次数统计信息
			//需要使用前一天的日期进行查询，基于这样的事实：
			//24h出入量节点的信息实质上是使用前一天数据计算的结果
			String yesterday = DateUtil.addDate(date, -1);
			Integer stoolCnt = reportMapper.getRecordStoolCountForSpecifiedDate(patID, templateID, yesterday);
			DocReportPrintDataDetail stoolDetail = new DocReportPrintDataDetail();
			stoolDetail.setTemplate_item_id(MnisConstants.DOC_OUT_NAME_STOOL);//大便项目名称，TODO
			stoolDetail.setRecord_value(Integer.toString(stoolCnt));
			detailList.add(stoolDetail);

			record.setData_list(detailList);

			return record;
		}
	}

	public void saveDocReportUserPermission(DocUserPermission permission)throws MnisException{
		if (permission == null) {
			return;
		}
		if(StringUtils.isEmpty(permission.getUserCode())
				|| StringUtils.isEmpty(permission.getTemplateID())){
			throw new MnisException("权限信息不全。");
		}
		Date sDate = DateUtil.parse(permission.getStartTime(), DateUtil.DateFormat.YMDHM);
		Date eDate = DateUtil.parse(permission.getEndTime(), DateUtil.DateFormat.YMDHM);
		if(null==sDate || null==eDate){
			throw new MnisException("时间信息格式不对。");
		}else if(sDate.after(eDate)) {
			Date tempDate = sDate;
			sDate = eDate;
			eDate = tempDate;
		}else {
			permission.setStartTime(DateUtil.format(sDate, DateUtil.DateFormat.YMDHM));
			permission.setEndTime(DateUtil.format(eDate, DateUtil.DateFormat.YMDHM));
		}
		//NOTE:是否应该有对授权人权限的检查？
		reportMapper.saveDocReportUserPermission(permission);
	}

	/**
	 * 获取用户在指定患者文书模板下的权限
	 * @param userCode 用户编号
	 * @param deptCode 科室编号
	 * @param patID 患者住院号
	 * @param templateID 模板ID
	 * @return 用户权限标识，无权限为0
	 */
	private Integer getDocReportUserPermission(final String userCode, final String deptCode,
											   final String patID, final String templateID){
		if(StringUtils.isEmpty(userCode) || StringUtils.isEmpty(deptCode)
				|| StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID)){
			return 0;
		}
		DocUserPermission permission = new DocUserPermission();
		permission.setUserCode(userCode);
		permission.setDeptCode(deptCode);
		permission.setPatID(patID);
		permission.setTemplateID(templateID);
		List<DocUserPermission> userPermissions = reportMapper.getDocReportUserPermission(permission);
		if(0<userPermissions.size()){
			return 1;
		}else {
			return 0;
		}
	}

	/**
	 * 获取用户在指定患者文书模板下的编辑权限
	 * @param userCode 用户编号
	 * @param deptCode 科室编号
	 * @param patID 患者住院号
	 * @param templateID 模板ID
	 * @return 用户权限标识，无权限为0
	 */
	public Integer getDocReportUserEditPermission(final String userCode, final String deptCode,
											   final String patID, final String templateID){
		//默认允许编辑
		Integer permission = 1;
		if(StringUtils.isEmpty(patID)){
			return 0;
		}
		//在院患者的文书允许编辑
		Patient patient = patientService.getPatientByPatId(patID);
		if(null != patient && null==patient.getOutDate()){
			permission = 1;
			return permission;
		}
		//如果患者已经出院超过规定期限，则默认不允许编辑
		//除非授权表中有相应的授权信息
		Date nowDate = new Date();
		if(nowDate.after(DateUtils.addDays(patient.getOutDate(), DocReportConstants.RECORD_OPEN_INTERVAL))){
			permission = getDocReportUserPermission(userCode,deptCode,patID,templateID);
		}
		return permission;
	}

}