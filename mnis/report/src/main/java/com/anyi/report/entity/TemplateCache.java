package com.anyi.report.entity;

import com.anyi.report.db.ReportMapper;
import com.anyi.report.entity.DocTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class TemplateCache {

	private static HashMap<String, Object> templateMap=new HashMap<String, Object>();//用于存放模板，不用每次都获取
	
	//public static List<DocConfig> list_docConfig=new ArrayList<DocConfig>();//文书配置信息，床位展示或频次提醒
	
	private static HashMap<String ,String[]> metadatacodeMap=new HashMap<String, String[]>();//护理文书（一般护理所有的列表节点，用于打印）

//	@Autowired
//	private ReportMapper reportMapper;

	//创建私有构造器，不允许该类进行实例化，仅做数据缓存容器使用
	private TemplateCache() {
		throw new AssertionError();
	}

	public static synchronized void addTemplateInMap(String templateID, Object template) {
		templateMap.put(templateID, template);
	}

	public static synchronized void addMetadataInMap(String templateID,String[] strList) {
		metadatacodeMap.put(templateID, strList);
	}

	public static synchronized void clearTemplateInMap(String templateID) {
		templateMap.remove(templateID);
	}

	public static synchronized void clearMetadataInMap(String templateID) {
		metadatacodeMap.remove(templateID);
	}

	public static synchronized void clearAllTemplateMap(){
		templateMap.clear();
	}

	public static synchronized void clearAllMetadataMap(){
		metadatacodeMap.clear();
	}

	/**
	 * 根据模板ID获取模板数据
	 * @param templateID
	 * @return
	 */
	public static Object getTemplateByID(String templateID){
		return templateMap.get(templateID);
	}

	/**
	 * 获取模板中组件对应的metadatacode
	 * @param templateID
	 * @return
	 */
	public static String[] getMetadataCodeByTemplateID(String templateID) {
		return metadatacodeMap.get(templateID);
	}

//使用静态类无法对reportmapper进行依赖注入，后续考虑。
//	public DocTemplate getTemplateByID(String templateID){
//		if(StringUtils.isEmpty(templateID)){
//			return null;
//		}
//		//先从缓存获取，如果没有从数据库中查询
//		DocTemplate template=(DocTemplate) templateMap.get(templateID);
//		if(null == template) {
//			template = new DocTemplate();
//			template.setTemplateId(templateID);
//			List<DocTemplate> list = reportMapper.getTemplateList(template);
//			if (null==list || 0>=list.size()) {
//				return null;
//			} else {
//				template = list.get(0);
//				addTemplateInMap(templateID, template);
//			}
//		}
//
//		return template;
//	}
}
