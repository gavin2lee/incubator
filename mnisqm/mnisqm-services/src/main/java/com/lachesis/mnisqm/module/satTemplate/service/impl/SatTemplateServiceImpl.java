package com.lachesis.mnisqm.module.satTemplate.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.satTemplate.dao.SatOptionMapper;
import com.lachesis.mnisqm.module.satTemplate.dao.SatResultDetailMapper;
import com.lachesis.mnisqm.module.satTemplate.dao.SatResultMapper;
import com.lachesis.mnisqm.module.satTemplate.dao.SatTemplateDetailMapper;
import com.lachesis.mnisqm.module.satTemplate.dao.SatTemplateMapper;
import com.lachesis.mnisqm.module.satTemplate.domain.SatOption;
import com.lachesis.mnisqm.module.satTemplate.domain.SatResult;
import com.lachesis.mnisqm.module.satTemplate.domain.SatResultDetail;
import com.lachesis.mnisqm.module.satTemplate.domain.SatTemplate;
import com.lachesis.mnisqm.module.satTemplate.domain.SatTemplateDetail;
import com.lachesis.mnisqm.module.satTemplate.service.ISatTemplateService;

@Service
public class SatTemplateServiceImpl implements ISatTemplateService {
	
	@Autowired
	SatTemplateMapper satTemplateMapper;
	@Autowired
	SatTemplateDetailMapper satTemplateDetailMapper;
	@Autowired
	SatOptionMapper satOptionMapper;
	@Autowired
	SatResultMapper satResultMapper;
	@Autowired
	SatResultDetailMapper satResultDetailMapper;

	@Override
	public int insertSatTemplate(SatTemplate satTemplate) {
		satTemplate.setFormCode(CodeUtils.getSysInvokeId());
		List<SatTemplateDetail> satTempDetailList = satTemplate.getSatTempDetailList();
		List<SatOption> satOptionList = null;
		if(null != satTempDetailList && satTempDetailList.size() != 0){
			for (SatTemplateDetail satTemplateDetail : satTempDetailList) {
				satTemplateDetail.setFormCode(satTemplate.getFormCode());
				satTemplateDetail.setDetailCode(CodeUtils.getSysInvokeId());
				satTemplateDetail.setCreatePerson(satTemplate.getCreatePerson());
				satTemplateDetail.setCreateTime(new Date());
				satOptionList = satTemplateDetail.getSatOptionList();
				if(null != satOptionList && satOptionList.size() != 0){
					for (SatOption satOption : satOptionList) {
						satOption.setDetailCode(satTemplateDetail.getDetailCode());
						satOption.setOptionCode(CodeUtils.getSysInvokeId());
						satOption.setCreateTime(new Date());
						satOption.setCreatePerson(satTemplate.getCreatePerson());
						satOptionMapper.insert(satOption);
					}					
				}
				satTemplateDetailMapper.insert(satTemplateDetail);
			}
		}
		return satTemplateMapper.insert(satTemplate);
	}

	@Override
	public int updateSatTemplate(SatTemplate satTemplate) {
		List<SatTemplateDetail> satTempDetailList = satTemplate.getSatTempDetailList();
		List<SatOption> satOptionList = null;
		if(null != satTempDetailList && satTempDetailList.size() != 0){
			for (SatTemplateDetail satTemplateDetail : satTempDetailList) {
				if(StringUtils.isNotEmpty(satTemplateDetail.getDetailCode())){ //有detailCode，则更新
					satTemplateDetailMapper.updateByPrimaryKey(satTemplateDetail);
					satOptionList = satTemplateDetail.getSatOptionList();
					if(null != satOptionList && satOptionList.size() != 0){
						for (SatOption satOption : satOptionList) {
							if(StringUtils.isNotEmpty(satOption.getDetailCode())){//有detailCode，则更新
								satOptionMapper.updateByPrimaryKey(satOption);
							}else{
								satOption.setDetailCode(satTemplateDetail.getDetailCode());
								satOption.setOptionCode(CodeUtils.getSysInvokeId());
								satOptionMapper.insert(satOption);
							}
						}					
					}
				}else{
					satTemplateDetail.setDetailCode(CodeUtils.getSysInvokeId());
					satOptionList = satTemplateDetail.getSatOptionList();
					if(null != satOptionList && satOptionList.size() != 0){
						for (SatOption satOption : satOptionList) {
							satOption.setDetailCode(satTemplateDetail.getDetailCode());
							satOption.setOptionCode(CodeUtils.getSysInvokeId());
							satOptionMapper.insert(satOption);
						}					
					}
					satTemplateDetailMapper.insert(satTemplateDetail);
				}
			}
		}
		return satTemplateMapper.updateByPrimaryKey(satTemplate);
	}

	@Override
	public void deleteSatTemplate(Long seqId) {
		satTemplateMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public int insertSatTemplateDetail(SatTemplateDetail satTemplateDetail) {
		satTemplateDetail.setDetailCode(CodeUtils.getSysInvokeId());
		return satTemplateDetailMapper.insert(satTemplateDetail);
	}

	@Override
	public int updateSatTemplateDetail(SatTemplateDetail satTemplateDetail) {
		return satTemplateDetailMapper.updateByPrimaryKey(satTemplateDetail);
	}

	@Override
	public void deleteSatTemplateDetail(Long seqId) {
		satTemplateDetailMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public int insertSatOption(SatOption satOption) {
		satOption.setOptionCode(CodeUtils.getSysInvokeId());
		return satOptionMapper.insert(satOption);
	}

	@Override
	public int updateSatOption(SatOption satOption) {
		return satOptionMapper.updateByPrimaryKey(satOption);
	}

	@Override
	public void deleteSatOption(Long seqId) {
		satOptionMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public void updateSatTemplateForDelete(SatTemplate satTemplate) {
		satTemplate.setStatus(MnisQmConstants.STATUS_WX);
		satTemplateMapper.updateForDelete(satTemplate);
	}

	@Override
	public List<SatTemplate> querySatTemplateByFormType(String formType, String userType) {
		Map<String, Object> conditionMap = new HashMap<String, Object>(); 
		conditionMap.put("formType", formType);
		conditionMap.put("userType", Integer.parseInt(userType));
		return satTemplateMapper.queryByFormTypeAndUserType(conditionMap);
	}
	
	@Override
	public List<SatTemplate> queryAllSatTemplateCodeAndFormName() {
		return satTemplateMapper.queryAllCodeAndFormName();
	}
	
	@Override
	public SatTemplate getSatTemplateByFormCode(String formCode) {
		return satTemplateMapper.getByFormCode(formCode);
	}

	@Override
	public int insertSatResult(SatResult satResult) {
		satResult.setResultCode(CodeUtils.getSysInvokeId());
		List<SatResultDetail> satResultDetailList = satResult.getSatResultDetailList();
		if(null != satResultDetailList && satResultDetailList.size() != 0){
			for (SatResultDetail satResultDetail : satResultDetailList) {
				satResultDetail.setResultCode(satResult.getResultCode());
				satResultDetail.setCreatePerson(satResult.getCreatePerson());
				satResultDetail.setCreateTime(new Date());
				satResultDetailMapper.insert(satResultDetail);
			}
		}
		return satResultMapper.insert(satResult);
	}

	@Override
	public int updateSatResult(SatResult satResult) {
		List<SatResultDetail> satResultDetailList = satResult.getSatResultDetailList();
		if(null != satResultDetailList && satResultDetailList.size() != 0){
			for (SatResultDetail satResultDetail : satResultDetailList) {
				satResultDetailMapper.updateByPrimaryKey(satResultDetail);
			}
		}
		return satResultMapper.updateByPrimaryKey(satResult);
	}

	@Override
	public void deleteSatResult(Long seqId) {
		satResultMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public int insertSatResultDetail(SatResultDetail satResultDetail) {
		return satResultDetailMapper.insert(satResultDetail);
	}

	@Override
	public int updateSatResultDetail(SatResultDetail satResultDetail) {
		return satResultDetailMapper.updateByPrimaryKey(satResultDetail);
	}

	@Override
	public void deleteSatResultDetail(Long seqId) {
		satResultDetailMapper.deleteByPrimaryKey(seqId);
	}

	@Override
	public void updateSatResultForDelete(SatResult satResult) {
		satResult.setStatus(MnisQmConstants.STATUS_WX);
		satResultMapper.updateForDelete(satResult);
	}

	@Override
	public List<SatResult> queryByDateAndFormTypeAndPatient(String yearAndMonth,String beginTime, String endTime , String formType, String patientName, String deptCode, String userType){
		Map<String, Object> conditionMap = new HashMap<String, Object>(); 
		if(null != yearAndMonth){
			conditionMap.put("yearAndMonth", yearAndMonth+"%");
		}else{
			conditionMap.put("yearAndMonth", yearAndMonth);
		}
		if(null != beginTime){
			conditionMap.put("beginTime", beginTime + " 00:00:00");
			conditionMap.put("endTime", endTime + " 23:59:59");
		}else{
			conditionMap.put("beginTime", beginTime);
			conditionMap.put("endTime", endTime);
		}
		conditionMap.put("formType", (StringUtils.isEmpty(formType))?null:formType);
		conditionMap.put("patientName", patientName);
		conditionMap.put("deptCode", (StringUtils.isEmpty(deptCode))?null:deptCode);
		conditionMap.put("userType", userType);
		return satResultMapper.queryByDateOrFormTypeOrPatientOrDeptCode(conditionMap);
	}

	@Override
	public SatTemplate getByTemplateName(String templateName) {
		return satTemplateMapper.getByTemplateName(templateName);
	}
}
