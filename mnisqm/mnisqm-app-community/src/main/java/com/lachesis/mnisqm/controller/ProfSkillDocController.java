package com.lachesis.mnisqm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.common.WebContextUtils;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.module.profSkillDoc.domain.AcademicConference;
import com.lachesis.mnisqm.module.profSkillDoc.domain.Assessment;
import com.lachesis.mnisqm.module.profSkillDoc.domain.AwardPunishmentInfo;
import com.lachesis.mnisqm.module.profSkillDoc.domain.ContinuingEducation;
import com.lachesis.mnisqm.module.profSkillDoc.domain.CprAcls;
import com.lachesis.mnisqm.module.profSkillDoc.domain.EducationRecordInspection;
import com.lachesis.mnisqm.module.profSkillDoc.domain.NurseHirarchicalRegistration;
import com.lachesis.mnisqm.module.profSkillDoc.domain.PersonalAssessment;
import com.lachesis.mnisqm.module.profSkillDoc.domain.PositionStatement;
import com.lachesis.mnisqm.module.profSkillDoc.domain.PreJobTraining;
import com.lachesis.mnisqm.module.profSkillDoc.domain.SpecialistNursesExperience;
import com.lachesis.mnisqm.module.profSkillDoc.service.IProfSkillDocService;
import com.lachesis.mnisqm.module.system.domain.SysUser;

@Controller
@RequestMapping(value = "/ProfSkillDoc")
public class ProfSkillDocController {

	@Autowired
	public IProfSkillDocService service;

	/**
	 * 保存学术会议、学习班登记
	 * 
	 * @param academicConference
	 * @return
	 */
	@RequestMapping(value = "/saveAcademicConference")
	public @ResponseBody
	BaseDataVo saveAcademicConference(
			@RequestBody AcademicConference academicConference) {
		BaseDataVo vo = new BaseDataVo();
		if ((academicConference != null)
				&& (academicConference.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((academicConference != null)
				&& (academicConference.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((academicConference != null)
				&& (academicConference.getWardCode() == null)) {
			throw new CommRuntimeException("病区不能为空!");
		}
		if ((academicConference != null)
				&& (academicConference.getMeetingName() == null)) {
			throw new CommRuntimeException("会议名称不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		academicConference.setCreatePerson(user.getUserCode());
		academicConference.setUpdatePerson(user.getUserCode());
		service.saveAcademicConference(academicConference);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得学术会议、学习班登记
	 * 
	 * @param academicConference
	 * @return
	 */
	@RequestMapping(value = "/getAcademicConferences")
	public @ResponseBody
	BaseDataVo getAcademicConferences(
			@RequestBody AcademicConference academicConference) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getAcademicConferences(academicConference));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除学术会议、学习班登记
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deleteAcademicConference")
	public @ResponseBody
	BaseDataVo deleteAcademicConference(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		AcademicConference a = new AcademicConference();

		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deleteAcademicConference(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存评估考核
	 * 
	 * @param assessment
	 * @return
	 */
	@RequestMapping(value = "/saveAssessment")
	public @ResponseBody
	BaseDataVo saveAssessment(@RequestBody Assessment assessment) {
		BaseDataVo vo = new BaseDataVo();
		if ((assessment != null) && (assessment.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((assessment != null) && (assessment.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((assessment != null) && (assessment.getAssessmentContent() == null)) {
			throw new CommRuntimeException("审核内容不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		assessment.setCreatePerson(user.getUserCode());
		assessment.setUpdatePerson(user.getUserCode());
		service.saveAssessment(assessment);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得评估考核
	 * 
	 * @param assessment
	 * @return
	 */
	@RequestMapping(value = "/getAssessment")
	public @ResponseBody
	BaseDataVo getAssessment(@RequestBody Assessment assessment) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getAssessment(assessment));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除评估考核
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deleteAssessment")
	public @ResponseBody
	BaseDataVo deleteAssessment(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		Assessment a = new Assessment();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deleteAssessment(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存奖励惩罚情况
	 * 
	 * @param awardPunishmentInfo
	 * @return
	 */
	@RequestMapping(value = "/saveAwardPunishmentInfo")
	public @ResponseBody
	BaseDataVo saveAwardPunishmentInfo(
			@RequestBody AwardPunishmentInfo awardPunishmentInfo) {
		BaseDataVo vo = new BaseDataVo();
		if ((awardPunishmentInfo != null)
				&& (awardPunishmentInfo.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((awardPunishmentInfo != null)
				&& (awardPunishmentInfo.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((awardPunishmentInfo != null)
				&& (awardPunishmentInfo.getWardCode() == null)) {
			throw new CommRuntimeException("病区不能为空!");
		}
		if ((awardPunishmentInfo != null)
				&& (awardPunishmentInfo.getApType() == null)) {
			throw new CommRuntimeException("奖励或惩罚类型不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		awardPunishmentInfo.setCreatePerson(user.getUserCode());
		awardPunishmentInfo.setUpdatePerson(user.getUserCode());
		service.saveAwardPunishmentInfo(awardPunishmentInfo);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得奖励惩罚情况
	 * 
	 * @param awardPunishmentInfo
	 * @return
	 */
	@RequestMapping(value = "/getAwardPunishmentInfo")
	public @ResponseBody
	BaseDataVo getAwardPunishmentInfo(
			@RequestBody AwardPunishmentInfo awardPunishmentInfo) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getAwardPunishmentInfo(awardPunishmentInfo));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除奖励惩罚情况
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deleteAwardPunishmentInfo")
	public @ResponseBody
	BaseDataVo deleteAwardPunishmentInfo(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		AwardPunishmentInfo a = new AwardPunishmentInfo();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deleteAwardPunishmentInfo(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存继续教育
	 * 
	 * @param continuingEducation
	 * @return
	 */
	@RequestMapping(value = "/saveContinuingEducation")
	public @ResponseBody
	BaseDataVo saveContinuingEducation(
			@RequestBody ContinuingEducation continuingEducation) {
		BaseDataVo vo = new BaseDataVo();
		if ((continuingEducation != null)
				&& (continuingEducation.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((continuingEducation != null)
				&& (continuingEducation.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((continuingEducation != null)
				&& (continuingEducation.getEducationName() == null)) {
			throw new CommRuntimeException("教育名称不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		continuingEducation.setCreatePerson(user.getUserCode());
		continuingEducation.setUpdatePerson(user.getUserCode());
		service.saveContinuingEducation(continuingEducation);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得继续教育
	 * 
	 * @param continuingEducation
	 * @return
	 */
	@RequestMapping(value = "/getContinuingEducation")
	public @ResponseBody
	BaseDataVo ContinuingEducation(
			@RequestBody ContinuingEducation continuingEducation) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getContinuingEducation(continuingEducation));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除继续教育
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deleteContinuingEducation")
	public @ResponseBody
	BaseDataVo deleteContinuingEducation(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		ContinuingEducation a = new ContinuingEducation();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deleteContinuingEducation(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存CPR/ACLS证书
	 * 
	 * @param cprAcls
	 * @return
	 */
	@RequestMapping(value = "/saveCprAcls")
	public @ResponseBody
	BaseDataVo saveCprAcls(@RequestBody CprAcls cprAcls) {
		BaseDataVo vo = new BaseDataVo();
		if ((cprAcls != null) && (cprAcls.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((cprAcls != null) && (cprAcls.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((cprAcls != null) && (cprAcls.getTrainName() == null)) {
			throw new CommRuntimeException("培训名称不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		cprAcls.setCreatePerson(user.getUserCode());
		cprAcls.setUpdatePerson(user.getUserCode());
		service.saveCprAcls(cprAcls);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得CPR/ACLS证书
	 * 
	 * @param cprAcls
	 * @return
	 */
	@RequestMapping(value = "/getCprAcls")
	public @ResponseBody
	BaseDataVo getCprAcls(@RequestBody CprAcls cprAcls) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getCprAcls(cprAcls));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除CPR/ACLS证书
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deleteCprAcls")
	public @ResponseBody
	BaseDataVo deleteCprAcls(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		CprAcls a = new CprAcls();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deleteCprAcls(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存教育记录与考核
	 * 
	 * @param educationRecordInspection
	 * @return
	 */
	@RequestMapping(value = "/saveEducationRecordInspection")
	public @ResponseBody
	BaseDataVo saveEducationRecordInspection(
			@RequestBody EducationRecordInspection educationRecordInspection) {
		BaseDataVo vo = new BaseDataVo();
		if ((educationRecordInspection != null)
				&& (educationRecordInspection.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((educationRecordInspection != null)
				&& (educationRecordInspection.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((educationRecordInspection != null)
				&& (educationRecordInspection.getTrainName() == null)) {
			throw new CommRuntimeException("培训名称不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		educationRecordInspection.setCreatePerson(user.getUserCode());
		educationRecordInspection.setUpdatePerson(user.getUserCode());
		service.saveEducationRecordInspection(educationRecordInspection);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得教育记录与考核
	 * 
	 * @param educationRecordInspection
	 * @return
	 */
	@RequestMapping(value = "/getEducationRecordInspection")
	public @ResponseBody
	BaseDataVo getEducationRecordInspection(
			@RequestBody EducationRecordInspection educationRecordInspection) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service
				.getEducationRecordInspection(educationRecordInspection));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除教育记录与考核
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deleteEducationRecordInspection")
	public @ResponseBody
	BaseDataVo deleteEducationRecordInspection(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		EducationRecordInspection a = new EducationRecordInspection();

		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deleteEducationRecordInspection(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存护士分层分级登记
	 * 
	 * @param nurseHirarchicalRegistration
	 * @return
	 */
	@RequestMapping(value = "/saveNurseHirarchicalRegistration")
	public @ResponseBody
	BaseDataVo saveNurseHirarchicalRegistration(
			@RequestBody NurseHirarchicalRegistration nurseHirarchicalRegistration) {
		BaseDataVo vo = new BaseDataVo();
		if ((nurseHirarchicalRegistration != null)
				&& (nurseHirarchicalRegistration.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((nurseHirarchicalRegistration != null)
				&& (nurseHirarchicalRegistration.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((nurseHirarchicalRegistration != null)
				&& (nurseHirarchicalRegistration.getWardCode() == null)) {
			throw new CommRuntimeException("病区不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		nurseHirarchicalRegistration.setCreatePerson(user.getUserCode());
		nurseHirarchicalRegistration.setUpdatePerson(user.getUserCode());
		service.saveNurseHirarchicalRegistration(nurseHirarchicalRegistration);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得护士分层分级登记
	 * 
	 * @param nurseHirarchicalRegistration
	 * @return
	 */
	@RequestMapping(value = "/getNurseHirarchicalRegistration")
	public @ResponseBody
	BaseDataVo getNurseHirarchicalRegistrations(
			@RequestBody NurseHirarchicalRegistration nurseHirarchicalRegistration) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service
				.getNurseHirarchicalRegistration(nurseHirarchicalRegistration));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除护士分层分级登记
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deleteNurseHirarchicalRegistration")
	public @ResponseBody
	BaseDataVo deleteNurseHirarchicalRegistration(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		NurseHirarchicalRegistration a = new NurseHirarchicalRegistration();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deleteNurseHirarchicalRegistration(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存个人考核结果
	 * 
	 * @param personalAssessment
	 * @return
	 */
	@RequestMapping(value = "/savePersonalAssessment")
	public @ResponseBody
	BaseDataVo savePersonalAssessment(
			@RequestBody PersonalAssessment personalAssessment) {
		BaseDataVo vo = new BaseDataVo();
		if ((personalAssessment != null)
				&& (personalAssessment.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((personalAssessment != null)
				&& (personalAssessment.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((personalAssessment != null)
				&& (personalAssessment.getWardCode() == null)) {
			throw new CommRuntimeException("病区不能为空!");
		}
		if ((personalAssessment != null)
				&& (personalAssessment.getAssessmentItem() == null)) {
			throw new CommRuntimeException("考核项目不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		personalAssessment.setCreatePerson(user.getUserCode());
		personalAssessment.setUpdatePerson(user.getUserCode());
		service.savePersonalAssessment(personalAssessment);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得个人考核结果
	 * 
	 * @param personalAssessment
	 * @return
	 */
	@RequestMapping(value = "/getPersonalAssessment")
	public @ResponseBody
	BaseDataVo getPersonalAssessments(
			@RequestBody PersonalAssessment personalAssessment) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getPersonalAssessment(personalAssessment));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除个人考核结果
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deletePersonalAssessment")
	public @ResponseBody
	BaseDataVo deletePersonalAssessment(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		PersonalAssessment a = new PersonalAssessment();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deletePersonalAssessment(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存岗位职责
	 * 
	 * @param positionStatement
	 * @return
	 */
	@RequestMapping(value = "/savePositionStatement")
	public @ResponseBody
	BaseDataVo savePositionStatement(
			@RequestBody PositionStatement positionStatement) {
		BaseDataVo vo = new BaseDataVo();
		if ((positionStatement != null)
				&& (positionStatement.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((positionStatement != null)
				&& (positionStatement.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((positionStatement != null)
				&& (positionStatement.getPositionName() == null)) {
			throw new CommRuntimeException("岗位名称不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		positionStatement.setCreatePerson(user.getUserCode());
		positionStatement.setUpdatePerson(user.getUserCode());
		service.savePositionStatement(positionStatement);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得岗位职责
	 * 
	 * @param positionStatement
	 * @return
	 */
	@RequestMapping(value = "/getPositionStatement")
	public @ResponseBody
	BaseDataVo getPositionStatements(
			@RequestBody PositionStatement positionStatement) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getPositionStatement(positionStatement));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除岗位职责
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deletePositionStatement")
	public @ResponseBody
	BaseDataVo deletePositionStatement(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		PositionStatement a = new PositionStatement();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deletePositionStatement(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存岗前培训
	 * 
	 * @param preJobTraining
	 * @return
	 */
	@RequestMapping(value = "/savePreJobTraining")
	public @ResponseBody
	BaseDataVo savePreJobTraining(@RequestBody PreJobTraining preJobTraining) {
		BaseDataVo vo = new BaseDataVo();
		if ((preJobTraining != null) && (preJobTraining.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((preJobTraining != null) && (preJobTraining.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((preJobTraining != null) && (preJobTraining.getTrainName() == null)) {
			throw new CommRuntimeException("培训名称不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		preJobTraining.setCreatePerson(user.getUserCode());
		preJobTraining.setUpdatePerson(user.getUserCode());
		service.savePreJobTraining(preJobTraining);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得岗前培训
	 * 
	 * @param preJobTraining
	 * @return
	 */
	@RequestMapping(value = "/getPreJobTraining")
	public @ResponseBody
	BaseDataVo getPreJobTrainings(@RequestBody PreJobTraining preJobTraining) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getPreJobTraining(preJobTraining));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除岗前培训
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deletePreJobTraining")
	public @ResponseBody
	BaseDataVo deletePreJobTraining(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		PreJobTraining a = new PreJobTraining();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deletePreJobTraining(a);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存专科护士经历
	 * 
	 * @param specialistNursesExperience
	 * @return
	 */
	@RequestMapping(value = "/saveSpecialistNursesExperience")
	public @ResponseBody
	BaseDataVo saveSpecialistNursesExperience(
			@RequestBody SpecialistNursesExperience specialistNursesExperience) {
		BaseDataVo vo = new BaseDataVo();
		if ((specialistNursesExperience != null)
				&& (specialistNursesExperience.getUserName() == null)) {
			throw new CommRuntimeException("姓名不能为空!");
		}
		if ((specialistNursesExperience != null)
				&& (specialistNursesExperience.getUserCode() == null)) {
			throw new CommRuntimeException("工号不能为空!");
		}
		if ((specialistNursesExperience != null)
				&& (specialistNursesExperience.getWardCode() == null)) {
			throw new CommRuntimeException("病区不能为空!");
		}
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		specialistNursesExperience.setCreatePerson(user.getUserCode());
		specialistNursesExperience.setUpdatePerson(user.getUserCode());
		service.saveSpecialistNursesExperience(specialistNursesExperience);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得专科护士经历
	 * 
	 * @param specialistNursesExperience
	 * @return
	 */
	@RequestMapping(value = "/getSpecialistNursesExperience")
	public @ResponseBody
	BaseDataVo getSpecialistNursesExperiences(
			@RequestBody SpecialistNursesExperience specialistNursesExperience) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service
				.getSpecialistNursesExperience(specialistNursesExperience));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除专科护士经历
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "/deleteSpecialistNursesExperience")
	public @ResponseBody
	BaseDataVo deleteSpecialistNursesExperience(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		SpecialistNursesExperience a = new SpecialistNursesExperience();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		a.setUpdatePerson(user.getUserCode());
		a.setSeqId(seqId);
		service.deleteSpecialistNursesExperience(a);
		vo.setCode(Constants.Success);
		return vo;
	}

}
