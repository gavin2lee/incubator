package com.lachesis.mnisqm.module.satTemplate.service;

import java.util.List;

import com.lachesis.mnisqm.module.satTemplate.domain.SatOption;
import com.lachesis.mnisqm.module.satTemplate.domain.SatResult;
import com.lachesis.mnisqm.module.satTemplate.domain.SatResultDetail;
import com.lachesis.mnisqm.module.satTemplate.domain.SatTemplate;
import com.lachesis.mnisqm.module.satTemplate.domain.SatTemplateDetail;

public interface ISatTemplateService {

	public int insertSatTemplate(SatTemplate satTemplate);
	
	public int updateSatTemplate(SatTemplate satTemplate);
	
	public void deleteSatTemplate(Long seqId);
	
	public int insertSatTemplateDetail(SatTemplateDetail satTemplateDetail);
	
	public int updateSatTemplateDetail(SatTemplateDetail satTemplateDetail);
	
	public void deleteSatTemplateDetail(Long seqId);
	
	public int insertSatOption(SatOption satOption);
	
	public int updateSatOption(SatOption satOption);
	
	public void deleteSatOption(Long seqId);
	
	public int insertSatResult(SatResult satResult);
	
	public int updateSatResult(SatResult satResult);
	
	public void deleteSatResult(Long seqId);
	
	public int insertSatResultDetail(SatResultDetail satResultDetail);
	
	public int updateSatResultDetail(SatResultDetail satResultDetail);
	
	public void deleteSatResultDetail(Long seqId);
	
	public void updateSatTemplateForDelete(SatTemplate satTemplate);
	
	public void updateSatResultForDelete(SatResult satResult);
	
	public SatTemplate getByTemplateName(String templateName);
	
	/**
	 * 根据表单类型查找满意度模板
	 * @param formType 表单类型
	 * @param userType 使用类型 1：患者；2：护士
	 * @return
	 */
	public List<SatTemplate> querySatTemplateByFormType(String formType, String userType);
	
	/**
	 * 根据表单编号查找满意度模板
	 * @param formCode 表单编号
	 * @return
	 */
	public SatTemplate getSatTemplateByFormCode(String formCode);
	
	/**获取所有表单编号和表单名
	 * @return
	 */
	public List<SatTemplate> queryAllSatTemplateCodeAndFormName();
	
	/**
	 * 根据时间、表单类型、患者姓名查询患者调查结果
	 * @param yearAndMonth
	 * @param formType
	 * @param patientName
	 * @return
	 */
	public List<SatResult> queryByDateAndFormTypeAndPatient(String yearAndMonth,String beginTime, String endTime , String formType, String patientName, String deptCode, String userType);
}
