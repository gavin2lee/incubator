package com.lachesis.mnis.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.anyi.report.constants.ReportConstants;
import com.anyi.report.entity.DocApprove;
import com.anyi.report.entity.DocDataReportSync;
import com.anyi.report.entity.DocRecord;
import com.anyi.report.entity.DocTemplate;
import com.anyi.report.entity.DocType;
import com.anyi.report.entity.DocUserPermission;
import com.anyi.report.entity.Metadata;
import com.anyi.report.entity.MetadataOption;
import com.anyi.report.entity.PrintInfoRecord;
import com.anyi.report.entity.PrintModelItem;
import com.anyi.report.entity.PrintModelList;
import com.anyi.report.entity.PrintModelListDetail;
import com.anyi.report.entity.PrintModelListDetailOrd;
import com.anyi.report.entity.PrintModelMain;
import com.anyi.report.json.BaseListVo;
import com.anyi.report.json.BaseMapVo;
import com.anyi.report.json.BaseVo;
import com.anyi.report.json.ResultCst;
import com.anyi.report.service.DocReportService;
import com.anyi.report.service.DocServiceForMetadata;
import com.anyi.report.vo.QueryVo;
import com.google.gson.Gson;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecordVo;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseDataVo;

@Controller
@RequestMapping("/nur/doc")
public class ReportAction {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportAction.class);
	@Autowired
	private DocReportService docReportService;
	
	//跳转到文书首页
	@RequestMapping(value = "/toDocMain")
	public String toDocMain(){
		return "/doc_report/docMain";
	}
	
	//跳转到上传模板的页面
	@RequestMapping(value = "/toUpLoadTemplate")
	public String toUpLoadTemplate(){
		return "/doc_report/toUpLoadTemplate";
	}
	
	@Autowired
	private DocServiceForMetadata docServiceForMeta;
	@RequestMapping(value = "/printpdf")
	public String printpdf(Model model,String template_id,String inpatient_no) {
		// 报表数据源
		PrintModelMain pmm=new PrintModelMain();
		pmm.setTemplate_id(template_id);
		pmm.setInpatient_no(inpatient_no);
		List<PrintModelMain> records = docReportService.getDataForPrint(pmm);
		List <Map> data_list=new ArrayList();
		for (Iterator<PrintModelMain> iterator = records.iterator(); iterator.hasNext();) 
		{
			PrintModelMain record = iterator.next();
			List<PrintModelItem> items = record.getData_item();
			for (Iterator iterator2 = items.iterator(); iterator2.hasNext();) {
				PrintModelItem paramBean = (PrintModelItem) iterator2.next();
				if(paramBean.getValue()==null||"null".equals(paramBean.getValue())){
					model.addAttribute(paramBean.getItem_id(), "");
				}else{
					model.addAttribute(paramBean.getItem_id(), paramBean.getValue());
				}
			}
			List <PrintModelList> datalist=record.getData_list();
			if(datalist!=null&&datalist.size()>0)
			{
				Map<String, Object> p=null;
				for(int k=0;k<datalist.size();k++)
				{	p=new HashMap<String, Object>();
					List <PrintModelListDetail> detail_list=datalist.get(k).getList_detail();
					int l=detail_list.size();
					
					String data_key="";
					String data_value="";
					String data_type="";
					PrintModelListDetail detail=null;
					
					for(int i=0;i<l;i++)
					{	
						detail=detail_list.get(i);
						data_key=detail.getData_key();
						data_value=detail.getData_value();
						if(data_value==null){
							data_value="";
						}
						data_type=detail.getData_type();
						//是选择框时，打印显示其序号
						if("SEL".equals(data_type)||("MEL".equals(data_type))){//多选或单选时，显示序号
							List <PrintModelListDetailOrd> list_ord=detail.getList_ord(); 
							if(list_ord!=null&&list_ord.size()>0){
								PrintModelListDetailOrd mord=null;
								String ord="";
								for(int o=0;o<list_ord.size();o++){
									mord=list_ord.get(o);
									if(data_value.indexOf(mord.getMetadata_code())!=-1){
										ord+=mord.getOrd()+",";
									}
								}
								if(ord.length()>0){
									ord=ord.substring(0,ord.lastIndexOf(","));
									data_value=ord;
								}
							}
						} 
						
						if("SWT".equals(data_type)){//开关用勾表
							if("Y".equals(data_value)){
								data_value="√";
							}else{
								data_value="";
							}
							
						}
						//动态列的输入框,下拉菜单显示对于的名称
						if("OPT".equals(detail.getItem_type())&&(
								"DATA_ONE".equals(data_key)
								||"DATA_TWO".equals(data_key)
								||"DATA_THREE".equals(data_key)
								||"DATA_FOUR".equals(data_key)
								||"DATA_FIVE".equals(data_key)
								))
						{
							data_value=detail.getMetadata_name();
						}
						//复选框(单选或多选),显示排序号
						if(("SEL".equals(detail.getItem_type())||"MSEL".equals(detail.getItem_type()))&&(
								"DATA_ONE".equals(data_key)
								||"DATA_TWO".equals(data_key)
								||"DATA_THREE".equals(data_key)
								||"DATA_FOUR".equals(data_key)
								||"DATA_FIVE".equals(data_key)
								))
						{
							data_value=detail.getOrd();
						}
						p.put(data_key, data_value);
						
					}
					data_list.add(p);//保存最后一行数据	
				}
				
			}else{
				Map<String, Object> p=new HashMap<String, Object>();
				p.put("test","");
				data_list.add(p);
			}
		}
		
		JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(data_list);
		// 动态指定报表模板url
		//String path=records.get(0).getTemplate_file_name();
		//path="/"+path.substring(path.indexOf("WEB-INF"), path.length());
		model.addAttribute("url", "D:/workspace_eclipse/web/src/main/webapp/WEB-INF/jasper/111.jasper");
		model.addAttribute("format", "pdf"); // 报表格式
		model.addAttribute("jrMainDataSource", data);
		return "reportView"; // 对应jasper-views.xml中的bean id
	}

	/**
	 * 根据主记录id
	 * @param record_id
	 * @param isHisotry:历史数据
	 * @return
	 */
	@RequestMapping(value = "/loadData")
	public @ResponseBody BaseMapVo loadData(String record_id,
			String template_id, String data_value, String inpatient_no,
			String report_type, String isprint, String startDate, String endDate,boolean isHistory) {
		DocRecord d = new DocRecord();
		d.setRecordId(record_id);
		d.setTemplate_id(template_id);
		d.setInpatient_no(inpatient_no);
		d.setPrint_show(report_type);// 模板分类
		if (data_value != null && data_value.trim().length() > 0) {
			if (data_value.indexOf(":") != -1) {// 日期后面有时间
				String[] temp = data_value.split(" ");
				d.setDate_list(temp[0]);
				d.setTime_list(temp[1]);
			} else {
				d.setDate_list(data_value);
				d.setTime_list(null);
			}
		}
		// List<DocRecord> tmps = docReportService.getTemplateData(d);
		/*
		 * BaseListVo vo=new BaseListVo(); vo.setData(tmps);
		 * vo.setRslt(ResultCst.SUCCESS); return vo;
		 */
		String deptCode = WebContextUtils.getSessionUserInfo().getDeptCode();
		return docReportService.getTempDatas(d, isprint, startDate, endDate,isHistory,deptCode);
	}
	
	@RequestMapping(value = "/loadTempDatas")
	public @ResponseBody BaseListVo loadTempDatas(String requestStr){
		BaseListVo listVo = new BaseListVo();
		if(requestStr==null
				||"".equals(requestStr)){
			listVo.setRslt(ResultCst.FAILURE);
			listVo.setMsg("请求数据不能为空！");
			return null;
		}
		
		QueryVo queryVo = GsonUtils.fromJson(requestStr,QueryVo.class);

		return null;
	}


	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public  @ResponseBody BaseVo reportP(@RequestParam MultipartFile temFile,@RequestParam String fName,
										 @RequestParam String dept_code,@RequestParam String doc_type,
										 String report_type,String templateId, Model model, String showOrder,
										 String bDefaultTemplate) {
		BaseVo vo=new BaseVo();
		// 动态指定报表模板url
		if (temFile != null) {
			String fileName = temFile.getOriginalFilename();
			if (fileName.endsWith("jasper")) {
				try {
					vo.setRslt("0");
					boolean bDefaultTempl = false;
					if("on".equals(bDefaultTemplate)){
						bDefaultTempl = true;
					}
					int intShowOrder = 0;
					if(!StringUtils.isEmpty(showOrder)) {
						intShowOrder = (int) Integer.decode(showOrder);
					}
					docReportService.addTemplate(fileName, temFile,dept_code,fName,doc_type,report_type,
							templateId, intShowOrder, bDefaultTempl);
				} catch (Exception e) {
					e.printStackTrace();
					vo.setRslt("1");
				}
			}

		}

		return vo;
	}

//	@RequestMapping(value = "/list")
//	public @ResponseBody BaseListVo reportList(String template_id,String inpatient_no,boolean isHistory) {
//		BaseListVo vo = new BaseListVo();
//		DocTemplate template=new DocTemplate();
//		template.setTemplateId(template_id);
//		List<DocTemplate> tmps = docReportService.getTemplateList(template,inpatient_no,isHistory);
//		vo.setData(tmps);
//		vo.setRslt(ResultCst.SUCCESS);
//		return vo;
//	}

	@RequestMapping(value = "/list")
	public @ResponseBody BaseMapVo reportList(String userCode, String deptCode, String template_id,
											   String inpatient_no, boolean isHistory) {
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isEmpty(template_id) || StringUtils.isEmpty(inpatient_no)){
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("传入参数信息缺失。");
		}else {
			DocTemplate template=new DocTemplate();
			template.setTemplateId(template_id);
			List<DocTemplate> tmps = docReportService.getTemplateList(template,inpatient_no,isHistory);
			vo.addData("template", tmps);
			//设置模板权限信息
			if(!StringUtils.isEmpty(userCode)){
				vo.addData("permission", docReportService.getDocReportUserEditPermission(userCode, deptCode, inpatient_no, template_id));
			}
			vo.setRslt(ResultCst.SUCCESS);
		}

		return vo;
	}
	
	@RequestMapping(value = "/getTree")
	public @ResponseBody BaseListVo getTree(@RequestParam String dept_code,@RequestParam String valid,@RequestParam String inpatient_no,int docQueryType) {
		return docReportService.getTrees(valid, inpatient_no,dept_code, docQueryType);
	}	
	
	
	@RequestMapping(value = "/saveData")
	public @ResponseBody BaseMapVo saveData(String recordStr,boolean isNurseRecord) {
		
		LOGGER.error("saveData: recordStr->" + recordStr);
		BaseMapVo vo=new BaseMapVo();
		try{
			//校验数据是否为空
			if(StringUtils.isEmpty(recordStr)){
				throw new MnisException("数据为空！");
			}
			//数据处理
			DocRecord record=null;
			try{
				record = new Gson().fromJson(recordStr, DocRecord.class);
			}catch (Exception e) {
				throw new MnisException("数据格式错误！");
			}
			String deptCode = WebContextUtils.getSessionUserInfo().getDeptCode();
			HashMap<String, Object> maps = docReportService.saveData(record,isNurseRecord,deptCode, false);
			
			if(maps.get(ReportConstants.IS_STATISTIC) != null && StringUtils.isBlank(record.getIsHeader())){
				vo.addData("docStatistic", maps.get("docStatistic"));
			} 
			
			vo.setRslt(ResultCst.SUCCESS);
			vo.setMsg(String.valueOf(maps.get(ReportConstants.RECORD_ID)!= null 
					? maps.get(ReportConstants.RECORD_ID):""));// 返回记录的ID
			//释放缓存
			recordStr = null;
			record = null;
		}catch (MnisException e) {
			//已知异常处理
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
			LOGGER.error(e.toString(),e);
		}catch (Exception e) {
			//未知异常处理
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
			LOGGER.error(e.toString(),e);
		}
		return vo;
	}
	
	@RequestMapping(value = "/nurseRounds")
	public @ResponseBody BaseVo nurseRounds(String type,String inpatient_no,String create_person,
											String date_list,String time_list, String template_id) {
		
		BaseVo vo=new BaseVo();
		try {
			String flag=docReportService.nurseRounds(type, inpatient_no, create_person, date_list,
													time_list, template_id);
			if("Y".equals(flag)){//返回记录的ID
				vo.setRslt("0");
			}else{
				vo.setRslt("1");
			}
		} catch (Exception ex) {
			vo.setRslt("1");
		}
		return vo;
	}	
	
	@RequestMapping(value = "/delete_data")
	public  @ResponseBody BaseMapVo delete_data(String record_id,String patId,boolean isNurseRecord, String delete_person){
		BaseMapVo vo=new BaseMapVo();
		if(StringUtils.isEmpty(record_id) || StringUtils.isEmpty(patId) || StringUtils.isEmpty(delete_person)){
			vo.setRslt("1");
			vo.setMsg("操作相关信息不全。");
			return vo;
		}
		//解码操作者信息
		String operator = null;
		try {
			operator = URLDecoder.decode(delete_person, MnisConstants.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			vo.setRslt("1");
			vo.setMsg("传入参数错误。");
			return vo;
		}

		DocRecord docRecord=new DocRecord();
		docRecord.setRecordId(record_id);
		docRecord.setInpatient_no(patId);
		docRecord.setCreate_person(operator);
		try{
			String deptCode = WebContextUtils.getSessionUserInfo().getDeptCode();
			HashMap<String, Object> maps = docReportService.delte_data(docRecord,isNurseRecord,deptCode);
			
			if(maps != null && maps.get("docStatistic") != null){
				//返回统计信息
				vo.addData("docStatistic",maps.get("docStatistic"));
			}
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
		}
		return vo;
	}

	/**
	 * 删除模板
	 * @param templateId
	 * @return
	 */
	@RequestMapping(value = "/delete_template", method = RequestMethod.POST)
	public @ResponseBody BaseVo delete_template(@RequestParam String templateId) {
		DocTemplate template=new DocTemplate();
		template.setTemplateId(templateId);
		BaseVo vo=new BaseVo();
		try{
			docReportService.delete_template(template);
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setMsg("1");
		}
		return vo;
	}	

	/**
	 * 将模板置为无效
	 * @param templateId
	 * @return
	 */
	@RequestMapping(value = "/update_template_valid", method = RequestMethod.POST)
	public @ResponseBody BaseVo update_template_valid(@RequestParam String templateId) {
		DocTemplate template=new DocTemplate();
		template.setTemplateId(templateId);
		BaseVo vo=new BaseVo();
		try{
			docReportService.update_template_valid(template);
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
		}
		return vo;
	}		
	
	@RequestMapping(value="/getDynaItemData")
	public @ResponseBody BaseListVo getDynaItemData(@RequestParam String checkbox_code,String data_type){
		return docReportService.getDynaItemData(checkbox_code,data_type);
	}
	
	/**
	 * 查询元数据
	 * @param metadata_name
	 * @param metadata_simple_name
	 * @param metadata_name_pinyin
	 * @param metadata_code
	 * @return
	 */
	@RequestMapping(value = "/queryMetadata")
	public @ResponseBody BaseListVo metadataList(@RequestParam String metadata_name,
			@RequestParam String metadata_simple_name,
			@RequestParam String metadata_name_pinyin,
			@RequestParam String metadata_code
			) {
		Metadata m=new Metadata();
		m.setMetadata_name(metadata_name);
		m.setMetadata_simple_name(metadata_simple_name);
		m.setMetadata_name_pinyin(metadata_name_pinyin);
		m.setMetadata_code(metadata_code);
		List<Metadata> tmps = docServiceForMeta.queryMetadata(m);
		BaseListVo vo = new BaseListVo();
		vo.setData(tmps);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}	
	
	/**
	 * 新增元数据
	 * @param metadata
	 * @return
	 */
	@RequestMapping(value="/addMetadada")
	public @ResponseBody BaseVo addMetadada(@ModelAttribute Metadata metadata){
		BaseVo vo=new BaseVo();
		try{
			if(metadata==null){
				vo.setRslt("1");
			}else{
				docServiceForMeta.addMetadada(metadata);
				vo.setRslt("0");
			}
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
		}
		return vo;
	}
	
	/**
	 * 更新元数据
	 * @param metadata
	 * @return
	 */
	@RequestMapping(value="/updateMetadata")
	public @ResponseBody  BaseVo updateMetadata( Metadata metadata){
		BaseVo vo=new BaseVo();
		if(metadata==null||metadata.getMetadata_code()==null||metadata.getMetadata_code().trim().length()==0){
			vo.setRslt("1");
			return vo;
		}
		try{
			docServiceForMeta.updateMetadata(metadata);
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
		}
		return vo;
	}
	
	/**
	 * 删除元数据
	 * @param metadata_code
	 * @return
	 */
	@RequestMapping(value="/deletaMetadata")
	public @ResponseBody BaseVo deletaMetadata(@RequestParam String metadata_code){
		BaseVo vo=new BaseVo();
		if(metadata_code==null||metadata_code.trim().length()==0){
			vo.setRslt("1");
			return vo;
		}
		try{
			Metadata metadata=new Metadata();
			metadata.setMetadata_code(metadata_code);
			docServiceForMeta.deletaMetadata(metadata);
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
		}
		return vo;
	}
	
	@RequestMapping(value="/addMetadataOption")
	public @ResponseBody BaseVo addMetadataOption( MetadataOption option){
		BaseVo vo=new BaseVo();
		if(option==null||option.getMetadata_code()==null||option.getMetadata_code().trim().length()==0){
			vo.setRslt("1");
			return vo;
		}
		try{
			docServiceForMeta.addMetadataOption(option);
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
		}
		return vo;
	}
	
	/**
	 * 删除元数据的子元素
	 * @param oprion_id
	 * @return
	 */
	@RequestMapping(value="/deleteMetadataOption")
	public @ResponseBody BaseVo deleteMetadataOption(@RequestParam String oprion_id){
		BaseVo vo=new BaseVo();
		if(oprion_id==null||oprion_id.trim().length()==0){
			vo.setRslt("1");
			return vo;
		}
		try{
			MetadataOption option=new MetadataOption();
			option.setOprion_id(oprion_id);
			docServiceForMeta.deleteMetadataOption(option);
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
		}
		return vo;
	}
	
	@RequestMapping(value="/updateMetadataOption")
	public @ResponseBody BaseVo updateMetadataOption( MetadataOption option){
		BaseVo vo=new BaseVo();
		if(option==null){
			vo.setRslt("1");
			return vo;
		}
		try{
			docServiceForMeta.updateMetadataOption(option);
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
		}
		return vo;
	}
	
	@RequestMapping(value="/queryMetadataOption")
	public BaseListVo queryMetadataOption(@RequestParam String metadata_code,@RequestParam String parent_id){
		BaseListVo vo =null; 
		try{
			MetadataOption m=new MetadataOption();
			if(metadata_code!=null&&metadata_code.trim().length()>0){
				m.setMetadata_code(metadata_code);
			}else if(parent_id!=null&&parent_id.trim().length()>0){
				m.setParent_id(parent_id);
			}else{
				vo=new BaseListVo();
			}
			if(vo==null){
				vo=new BaseListVo();
				m.setMetadata_code(metadata_code);
				m.setParent_id(parent_id);
				List<MetadataOption> tmps = docServiceForMeta.queryMetadataOption(m);
				
				vo.setData(tmps);
				vo.setRslt(ResultCst.SUCCESS);					
			}else{
				vo.setData(null);
				vo.setRslt(ResultCst.FAILURE);	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return vo;		
	}
	
	@RequestMapping(value="/isExitSameCode")
	public @ResponseBody BaseVo isExitSameCode(@RequestParam String metadata_code){
		BaseVo vo=new BaseVo();
		try{
			docServiceForMeta.isExitSameCode(metadata_code);
			vo.setRslt("0");
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt("1");
			vo.setMsg("有重复值");
		}
		return vo;
	}
	
	@RequestMapping(value="/getDocType")
	public @ResponseBody BaseListVo getDocType(){
		BaseListVo vo= new BaseListVo();
		DocType docType=new DocType();
		try{
			List <DocType> list=docReportService.getDocType(docType);
			vo.setData(list);
			vo.setRslt(ResultCst.SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
			vo.setRslt(ResultCst.FAILURE);
		}
		return vo;
	}
	
	/**
	 * 获取所有科室，用于上传
	 * @return
	 */
	@RequestMapping(value="/queryComWard")
	public @ResponseBody BaseListVo queryComWard(){
		return docReportService.queryComWard();
	}
	
	@RequestMapping(value = "/approve")
	public @ResponseBody BaseListVo approve(String json) {
		DocApprove data=null;
		try{
			data=new Gson().fromJson(json, DocApprove.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return docReportService.approve(data);
	}	
	
	@RequestMapping(value="/docDataReportSync")
	@ResponseBody
	public BaseMapVo docDataReportSync(String patId,String startDate,String endDate){
		BaseMapVo vo = new BaseMapVo();
		
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			vo.setMsg("同步失败,请传入时间区间!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		
		//根据患者指定时间同步出入量数据
		try {
			List<DocDataReportSync> docDataReportSyncs = docReportService.getDocDataReportSync(startDate, endDate, patId, null);
			if( null != docDataReportSyncs && !docDataReportSyncs.isEmpty())
				docReportService.docDataReportSync(docDataReportSyncs,MnisConstants.DOC_REPORT_STATISTIC_TIME,
						DateUtil.parse(endDate, DateFormat.YMD));
			vo.setMsg("同步成功");
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			vo.setMsg("同步失败");
			vo.setRslt(ResultCst.FAILURE);
		}
		return vo;
	}

	/**
	 * 设置患者默认护理记录单
	 * @param patID
	 * @param templateID
	 * @param deptCode
	 * @return
	 */
	@RequestMapping(value = "/setPatDefaultTempl")
	@ResponseBody
	public BaseDataVo setPatientDefaultTemplate(String patID, String templateID, String deptCode){
		BaseDataVo dataVo = new BaseDataVo();
		try{
			if(!StringUtil.hasValue(patID) || !StringUtil.hasValue(templateID)){
				throw new MnisException("病人编号和模板ID不能为空。");
			}

			docReportService.setPatientDefaultTemplate(patID, templateID, deptCode);
			dataVo.setRslt(ResultCst.SUCCESS);
			dataVo.setMsg("设置成功。");
		}catch (MnisException e) {
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}catch (Exception e) {
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg("设置患者默认护理记录单失败。");
		}

		return dataVo;
	}

	/**
	 * 设置文书打印页码数，以实现续页打印功能
	 * @param patID
	 * @param templateID
	 * @param printNum
	 * @param createPerson
	 * @return 成功/失败
	 */
	@RequestMapping(value = "/setReportPrintNum")
	@ResponseBody
	public BaseDataVo setReportPrintNum(String patID, String templateID, String printNum, String createPerson){
		BaseDataVo dataVo = new BaseDataVo();
		try{
			if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID) || StringUtils.isEmpty(printNum)
					|| StringUtils.isEmpty(createPerson)) {
				throw new MnisException("传入信息字段有空值。");
			}
			docReportService.setReportPrintNum(patID, templateID, printNum, createPerson);
			dataVo.setRslt(ResultCst.SUCCESS);
		}catch (MnisException e){
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}catch (Exception e) {
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}
		return dataVo;
	}

	/**
	 * 获取文书打印页码数
	 * @param patID
	 * @param templateID
	 * @return 打印页码
	 */
	@RequestMapping(value = "/getReportPrintNum")
	@ResponseBody
	public BaseDataVo getReportPrintNum(String patID, String templateID){
		BaseDataVo dataVo = new BaseDataVo();
		try{
			if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID)) {
				throw new MnisException("患者或模板ID为空值。");
			}
			int num = docReportService.getReportPrintNum(patID, templateID);
			dataVo.setData(num);
			dataVo.setRslt(ResultCst.SUCCESS);
		}catch (MnisException e){
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}catch (Exception e) {
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}
		return dataVo;
	}

	/**
	 * 获取列表类型文书的表头数据
	 * @param patID
	 * @param templateID
	 * @param isPrint
	 * @return
	 */
	@RequestMapping(value = "/getDocHeaderData")
	@ResponseBody
	public BaseDataVo getDocHeaderData(String patID, String templateID, String isPrint){
		BaseDataVo dataVo = new BaseDataVo();
		try{
			if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID)) {
				throw new MnisException("患者或模板ID为空值。");
			}
			DocRecord headerData = docReportService.getDocHeaderDate(patID, templateID, isPrint);
			dataVo.setData(headerData);
			dataVo.setRslt(ResultCst.SUCCESS);
		}catch (MnisException e){
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}catch (Exception e) {
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}
		return dataVo;
	}

	/**
	 * 清除模板和元数据的缓存数据
	 * @return
	 */
	@RequestMapping(value = "/clearDocTemplateCache")
	@ResponseBody
	public BaseDataVo clearDocTemplateCache(){
		BaseDataVo dataVo = new BaseDataVo();
		try{
			docReportService.clearDocTemplateCache();
			dataVo.setRslt(ResultCst.SUCCESS);
			dataVo.setMsg("成功清除模板及元数据的缓存数据。");
		}catch (Exception e) {
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}
		return dataVo;
	}

	/**
	 * 智能打印功能获取文书的打印数据
	 * @param patID      患者住院号
	 * @param templateID 模板ID
	 * @param startPage  打印的起始页码，为0表示从头开始
	 * @param endPage    打印的结束页码，为0表示到结尾
	 * @param numType    打印页码类型，0 正常，1 奇数页打印，2 偶数页打印
	 * @param startDate  指定起始日期，打印该日期区间所在页的所有记录
	 * @param endDate    指定截止日期
	 * @param isHistory  是否历史文书
     * @return 打印信息集合，文书记录数据以字符串数组的形式放在“printData”对应的value中
     */
    @RequestMapping(value = "/loadPrintData")
	@ResponseBody
	public BaseMapVo loadPrintDataInListTemplateForPatient(String patID, String templateID, Integer startPage,
														   Integer endPage, Integer numType, String startDate,
														   String endDate, boolean isHistory){
		BaseMapVo mapVo = new BaseMapVo();
		try{
			if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(templateID)) {
				throw new MnisException("患者或模板信息为空。");
			}
			if(null==startPage || 0>startPage){
				startPage = 0;
			}
			if(null==endPage || 0>endPage) {
				endPage = 0;
			}
			List<String[]> printData = docReportService.loadPrintDataInListTemplateForPatient(patID, templateID, startPage,
																			endPage, numType, startDate, endDate, isHistory);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("patID", patID);
			resultMap.put("templateID", templateID);
			resultMap.put("printData", printData);
			mapVo.setData(resultMap);
			mapVo.setRslt(ResultCst.SUCCESS);
		}catch (MnisException e) {
			mapVo.setRslt(ResultCst.FAILURE);
			mapVo.setMsg(e.getMessage());
		}catch (Exception e){
			mapVo.setRslt(ResultCst.FAILURE);
			mapVo.setMsg(e.getMessage());
		}

		return mapVo;
	}

	/**
	 * 记录用户的打印信息
	 * @param printInfo 打印信息记录
	 * @throws MnisException
	 */
	@RequestMapping(value = "/recordPatPrintInfo")
	@ResponseBody
	public BaseDataVo recordPatPrintInfo(String printInfo){
		BaseDataVo dataVo = new BaseDataVo();
		try {
			if (printInfo == null) {
				throw new MnisException("传入参数为空。");
			}
			PrintInfoRecord printInfoRecord = null;
			try {
				printInfoRecord = GsonUtils.fromJson(printInfo, PrintInfoRecord.class);
			}catch (Exception e){
				throw new MnisException("无法获取参数信息。");
			}
			if(StringUtils.isEmpty(printInfoRecord.getPatID())
					|| StringUtils.isEmpty(printInfoRecord.getCreatePerson())
					|| StringUtils.isEmpty(printInfoRecord.getPrintType())
					|| StringUtils.isEmpty(printInfoRecord.getPrintStart())){
				throw new MnisException("打印信息不全。");
			}
			docReportService.recordPatPrintInfo(printInfoRecord);
			dataVo.setRslt(ResultCst.SUCCESS);
		}catch (Exception e){
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}

		return dataVo;
	}

	/**
	 * 获取指定时间段内，具有体温记录最大值的文书记录数据（主要为生命体征数据，以及出入量）
	 * @param patID 患者住院号
	 * @param deptCode 病区编号
	 * @param date 指定日期
	 * @param time 指定时间
	 * @return 文书记录数据，以生命体征数据结构的形式组织
	 */
	@RequestMapping(value = "/getDocRecordInfoWithMaxTemprature")
	@ResponseBody
	public BaseDataVo getDocRecordInfoWithMaxTemprature(String patID, String deptCode, String date, String time){
		BaseDataVo dataVo = new BaseDataVo();
		try {
			if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(date)){
				throw new MnisException("参数信息不全。");
			}
			List<BodySignRecordVo> records = docReportService.getDocRecordInfoWithMaxTemprature(patID, deptCode, date, time);
			if(records.isEmpty()){
				throw new MnisException("数据获取异常。");
			}
			dataVo.setData(records);
			dataVo.setRslt(ResultCst.SUCCESS);
		}catch (Exception e){
			dataVo.setMsg(e.getMessage());
			dataVo.setRslt(ResultCst.FAILURE);
		}

		return dataVo;
	}

	/**
	 * 设置用户对文书数据的编辑权限
	 * @param permission 权限信息
	 * @return 设置结果
     */
    @RequestMapping(value = "/saveDocPermission")
	@ResponseBody
	public BaseDataVo saveDocReportUserPermission(String permission) {
		BaseDataVo dataVo = new BaseDataVo();
		try {
			if (permission == null) {
				throw new MnisException("传入参数为空。");
			}
			DocUserPermission userPermission = null;
			try {
				userPermission = GsonUtils.fromJson(permission, DocUserPermission.class);
			} catch (Exception e) {
				throw new MnisException("Json解析失败。");
			}
			if(StringUtils.isEmpty(userPermission.getUserCode()) || StringUtils.isEmpty(userPermission.getTemplateID())
					|| StringUtils.isEmpty(userPermission.getPatID() ) || StringUtils.isEmpty(userPermission.getEndTime())
					|| StringUtils.isEmpty(userPermission.getStartTime())){
				throw new MnisException("参数信息不全。");
			}
			docReportService.saveDocReportUserPermission(userPermission);
			dataVo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}
	return dataVo;
	}

}
