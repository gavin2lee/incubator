package com.lachesis.mnisqm.module.profSkillDoc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.module.profSkillDoc.dao.AcademicConferenceMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.AssessmentMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.AwardPunishmentInfoMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.ContinuingEducationMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.CprAclsMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.EducationRecordInspectionMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.NurseHirarchicalRegistrationMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.PersonalAssessmentMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.PositionStatementMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.PreJobTrainingMapper;
import com.lachesis.mnisqm.module.profSkillDoc.dao.SpecialistNursesExperienceMapper;
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

@Service
public class ProfSkillDocServiceImpl implements IProfSkillDocService {

	@Autowired
	private AcademicConferenceMapper academicConferenceMapper;

	@Autowired
	private AssessmentMapper assessmentMapper;

	@Autowired
	private AwardPunishmentInfoMapper awardPunishmentInfoMapper;

	@Autowired
	private ContinuingEducationMapper continuingEducationMapper;

	@Autowired
	private CprAclsMapper cprAclsMapper;

	@Autowired
	private EducationRecordInspectionMapper educationRecordInspectionMapper;

	@Autowired
	private NurseHirarchicalRegistrationMapper nurseHirarchicalRegistrationMapper;

	@Autowired
	private PersonalAssessmentMapper personalAssessmentMapper;

	@Autowired
	private PositionStatementMapper positionStatementMapper;

	@Autowired
	private PreJobTrainingMapper preJobTrainingMapper;

	@Autowired
	private SpecialistNursesExperienceMapper specialistNursesExperienceMapper;

	@Override
	public List<AcademicConference> getAcademicConferences(
			AcademicConference academicConference) {
		// 模糊查询
		if (academicConference.getUserName() != null) {
			academicConference.setUserName("%"
					+ academicConference.getUserName() + "%");
		}
		if (academicConference.getMeetingName() != null) {
			academicConference.setMeetingName("%"
					+ academicConference.getMeetingName() + "%");
		}
		if (academicConference.getMeetingSite() != null) {
			academicConference.setMeetingSite("%"
					+ academicConference.getMeetingSite() + "%");
		}
		if (academicConference.getHostUnit() != null) {
			academicConference.setHostUnit("%"
					+ academicConference.getHostUnit() + "%");
		}
		if (academicConference.getAuditor() != null) {
			academicConference.setAuditor("%" + academicConference.getAuditor()
					+ "%");
		}
		ArrayList<AcademicConference> list = (ArrayList<AcademicConference>) academicConferenceMapper.select(academicConference);
		for(AcademicConference a : list){
			a.setMeetingDate(a.getMeetingDate().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveAcademicConference(AcademicConference academicConference) {
		if (academicConference.getSeqId() != null) {
			academicConferenceMapper.update(academicConference);
		} else {
			academicConferenceMapper.insert(academicConference);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAcademicConference(AcademicConference academicConference) {
		academicConference.setStatus(MnisQmConstants.STATUS_WX);
		academicConferenceMapper.deleteByPrimaryKey(academicConference);
	}

	@Override
	public List<Assessment> getAssessment(Assessment assessment) {
		// 模糊查询
		if (assessment.getUserName() != null) {
			assessment.setUserName("%" + assessment.getUserName() + "%");
		}
		if (assessment.getAssessmentContent() != null) {
			assessment.setAssessmentContent("%"
					+ assessment.getAssessmentContent() + "%");
		}
		if (assessment.getAssessmentResult() != null) {
			assessment.setAssessmentResult("%"
					+ assessment.getAssessmentResult() + "%");
		}
		ArrayList<Assessment> list = (ArrayList<Assessment>) assessmentMapper.select(assessment);
		for(Assessment a : list){
			a.setAssessmentTime(a.getAssessmentTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveAssessment(Assessment assessment) {
		if (assessment.getSeqId() != null) {
			assessmentMapper.update(assessment);
		} else {
			assessmentMapper.insert(assessment);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAssessment(Assessment assessment) {
		assessment.setStatus(MnisQmConstants.STATUS_WX);
		assessmentMapper.deleteByPrimaryKey(assessment);
	}

	@Override
	public List<AwardPunishmentInfo> getAwardPunishmentInfo(
			AwardPunishmentInfo awardPunishmentInfo) {
		if (awardPunishmentInfo.getUserName() != null) {
			awardPunishmentInfo.setUserName("%"
					+ awardPunishmentInfo.getUserName() + "%");
		}
		if (awardPunishmentInfo.getWardCode() != null) {
			awardPunishmentInfo.setWardCode("%"
					+ awardPunishmentInfo.getWardCode() + "%");
		}
		if (awardPunishmentInfo.getReason() != null) {
			awardPunishmentInfo.setReason("%" + awardPunishmentInfo.getReason()
					+ "%");
		}
		if (awardPunishmentInfo.getAuditor() != null) {
			awardPunishmentInfo.setAuditor("%"
					+ awardPunishmentInfo.getAuditor() + "%");
		}
		ArrayList<AwardPunishmentInfo> list = (ArrayList<AwardPunishmentInfo>) awardPunishmentInfoMapper.select(awardPunishmentInfo);
		for(AwardPunishmentInfo a : list){
			a.setApTime(a.getApTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveAwardPunishmentInfo(AwardPunishmentInfo awardPunishmentInfo) {
		if (awardPunishmentInfo.getSeqId() != null) {
			awardPunishmentInfoMapper.update(awardPunishmentInfo);
		} else {
			awardPunishmentInfoMapper.insert(awardPunishmentInfo);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAwardPunishmentInfo(
			AwardPunishmentInfo awardPunishmentInfo) {
		awardPunishmentInfo.setStatus(MnisQmConstants.STATUS_WX);
		awardPunishmentInfoMapper.deleteByPrimaryKey(awardPunishmentInfo);
	}

	@Override
	public List<ContinuingEducation> getContinuingEducation(
			ContinuingEducation continuingEducation) {
		// 模糊查询
		if (continuingEducation.getUserName() != null) {
			continuingEducation.setUserName("%"
					+ continuingEducation.getUserName() + "%");
		}
		if (continuingEducation.getEducationName() != null) {
			continuingEducation.setEducationName("%"
					+ continuingEducation.getEducationName() + "%");
		}
		if (continuingEducation.getAuditor() != null) {
			continuingEducation.setAuditor("%"
					+ continuingEducation.getAuditor() + "%");
		}
		ArrayList<ContinuingEducation> list = (ArrayList<ContinuingEducation>) continuingEducationMapper.select(continuingEducation);
		for(ContinuingEducation a : list){
			a.setBeginTime(a.getBeginTime().substring(0, 10));
			a.setEndTime(a.getEndTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveContinuingEducation(ContinuingEducation continuingEducation) {
		if (continuingEducation.getSeqId() != null) {
			continuingEducationMapper.update(continuingEducation);
		} else {
			continuingEducationMapper.insert(continuingEducation);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteContinuingEducation(
			ContinuingEducation continuingEducation) {
		continuingEducation.setStatus(MnisQmConstants.STATUS_WX);
		continuingEducationMapper.deleteByPrimaryKey(continuingEducation);
	}

	@Override
	public List<CprAcls> getCprAcls(CprAcls cprAcls) {
		// 模糊查询
		if (cprAcls.getUserName() != null) {
			cprAcls.setUserName("%" + cprAcls.getUserName() + "%");
		}
		if (cprAcls.getTrainName() != null) {
			cprAcls.setTrainName("%" + cprAcls.getTrainName() + "%");
		}
		if (cprAcls.getAssessmentContent() != null) {
			cprAcls.setAssessmentContent("%" + cprAcls.getAssessmentContent()
					+ "%");
		}
		if (cprAcls.getAuditor() != null) {
			cprAcls.setAuditor("%" + cprAcls.getAuditor() + "%");
		}
		ArrayList<CprAcls> list = (ArrayList<CprAcls>) cprAclsMapper.select(cprAcls);
		for(CprAcls a : list){
			a.setTrainTime(a.getTrainTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveCprAcls(CprAcls cprAcls) {
		if (cprAcls.getSeqId() != null) {
			cprAclsMapper.update(cprAcls);
		} else {
			cprAclsMapper.insert(cprAcls);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteCprAcls(CprAcls cprAcls) {
		cprAcls.setStatus(MnisQmConstants.STATUS_WX);
		cprAclsMapper.deleteByPrimaryKey(cprAcls);
	}

	@Override
	public List<EducationRecordInspection> getEducationRecordInspection(
			EducationRecordInspection educationRecordInspection) {
		// 模糊查询
		if (educationRecordInspection.getUserName() != null) {
			educationRecordInspection.setUserName("%"
					+ educationRecordInspection.getUserName() + "%");
		}
		if (educationRecordInspection.getTrainName() != null) {
			educationRecordInspection.setTrainName("%"
					+ educationRecordInspection.getTrainName() + "%");
		}
		if (educationRecordInspection.getAuditor() != null) {
			educationRecordInspection.setAuditor("%"
					+ educationRecordInspection.getAuditor() + "%");
		}
		ArrayList<EducationRecordInspection> list = (ArrayList<EducationRecordInspection>) educationRecordInspectionMapper.select(educationRecordInspection);
		for(EducationRecordInspection a : list){
			a.setBeginTime(a.getBeginTime().substring(0, 10));
			a.setEndTime(a.getEndTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveEducationRecordInspection(
			EducationRecordInspection educationRecordInspection) {
		if (educationRecordInspection.getSeqId() != null) {
			educationRecordInspectionMapper.update(educationRecordInspection);
		} else {
			educationRecordInspectionMapper.insert(educationRecordInspection);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteEducationRecordInspection(
			EducationRecordInspection educationRecordInspection) {
		educationRecordInspection.setStatus(MnisQmConstants.STATUS_WX);
		educationRecordInspectionMapper
				.deleteByPrimaryKey(educationRecordInspection);
	}

	@Override
	public List<NurseHirarchicalRegistration> getNurseHirarchicalRegistration(
			NurseHirarchicalRegistration nurseHirarchicalRegistration) {
		if (nurseHirarchicalRegistration.getUserName() != null) {
			nurseHirarchicalRegistration.setUserName("%"
					+ nurseHirarchicalRegistration.getUserName() + "%");
		}
		if (nurseHirarchicalRegistration.getAuditor() != null) {
			nurseHirarchicalRegistration.setAuditor("%"
					+ nurseHirarchicalRegistration.getAuditor() + "%");
		}
		ArrayList<NurseHirarchicalRegistration> list = (ArrayList<NurseHirarchicalRegistration>) nurseHirarchicalRegistrationMapper.select(nurseHirarchicalRegistration);
		for(NurseHirarchicalRegistration a : list){
			a.setBeginTime(a.getBeginTime().substring(0, 10));
			a.setEndTime(a.getEndTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveNurseHirarchicalRegistration(
			NurseHirarchicalRegistration nurseHirarchicalRegistration) {
		if (nurseHirarchicalRegistration.getSeqId() != null) {
			nurseHirarchicalRegistrationMapper
					.update(nurseHirarchicalRegistration);
		} else {
			nurseHirarchicalRegistrationMapper
					.insert(nurseHirarchicalRegistration);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteNurseHirarchicalRegistration(
			NurseHirarchicalRegistration nurseHirarchicalRegistration) {
		nurseHirarchicalRegistration.setStatus(MnisQmConstants.STATUS_WX);
		nurseHirarchicalRegistrationMapper
				.deleteByPrimaryKey(nurseHirarchicalRegistration);
	}

	@Override
	public List<PersonalAssessment> getPersonalAssessment(
			PersonalAssessment personalAssessment) {
		if (personalAssessment.getUserName() != null) {
			personalAssessment.setUserName("%"
					+ personalAssessment.getUserName() + "%");
		}
		if (personalAssessment.getAssessmentItem() != null) {
			personalAssessment.setAssessmentItem("%"
					+ personalAssessment.getAssessmentItem() + "%");
		}
		if (personalAssessment.getAuditor() != null) {
			personalAssessment.setAuditor("%" + personalAssessment.getAuditor()
					+ "%");
		}
		ArrayList<PersonalAssessment> list = (ArrayList<PersonalAssessment>) personalAssessmentMapper.select(personalAssessment);
		for(PersonalAssessment a : list){
			a.setAssessmentTime(a.getAssessmentTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void savePersonalAssessment(PersonalAssessment personalAssessment) {
		if (personalAssessment.getSeqId() != null) {
			personalAssessmentMapper.update(personalAssessment);
		} else {
			personalAssessmentMapper.insert(personalAssessment);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deletePersonalAssessment(PersonalAssessment personalAssessment) {
		personalAssessment.setStatus(MnisQmConstants.STATUS_WX);
		personalAssessmentMapper.deleteByPrimaryKey(personalAssessment);
	}

	@Override
	public List<PositionStatement> getPositionStatement(
			PositionStatement positionStatement) {
		// 模糊查询
		if (positionStatement.getUserName() != null) {
			positionStatement.setUserName("%" + positionStatement.getUserName()
					+ "%");
		}
		if (positionStatement.getPositionName() != null) {
			positionStatement.setPositionName("%"
					+ positionStatement.getPositionName() + "%");
		}
		if (positionStatement.getJobSummary() != null) {
			positionStatement.setJobSummary("%"
					+ positionStatement.getJobSummary() + "%");
		}
		if (positionStatement.getLeaderMemberRelation() != null) {
			positionStatement.setLeaderMemberRelation("%"
					+ positionStatement.getLeaderMemberRelation() + "%");
		}
		if (positionStatement.getResponsibilities() != null) {
			positionStatement.setResponsibilities("%"
					+ positionStatement.getResponsibilities() + "%");
		}
		if (positionStatement.getJobStandard() != null) {
			positionStatement.setJobStandard("%"
					+ positionStatement.getJobStandard() + "%");
		}
		ArrayList<PositionStatement> list = (ArrayList<PositionStatement>) positionStatementMapper.select(positionStatement);
		for(PositionStatement a : list){
			a.setBeginTime(a.getBeginTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void savePositionStatement(PositionStatement positionStatement) {
		if (positionStatement.getSeqId() != null) {
			positionStatementMapper.update(positionStatement);
		} else {
			positionStatementMapper.insert(positionStatement);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deletePositionStatement(PositionStatement positionStatement) {
		positionStatement.setStatus(MnisQmConstants.STATUS_WX);
		positionStatementMapper.deleteByPrimaryKey(positionStatement);
	}

	@Override
	public List<PreJobTraining> getPreJobTraining(PreJobTraining preJobTraining) {
		if (preJobTraining.getUserName() != null) {
			preJobTraining
					.setUserName("%" + preJobTraining.getUserName() + "%");
		}
		if (preJobTraining.getTrainName() != null) {
			preJobTraining.setTrainName("%" + preJobTraining.getTrainName()
					+ "%");
		}
		if (preJobTraining.getCertificateQualification() != null) {
			preJobTraining.setCertificateQualification("%"
					+ preJobTraining.getCertificateQualification() + "%");
		}
		if (preJobTraining.getAuditor() != null) {
			preJobTraining.setAuditor("%" + preJobTraining.getAuditor() + "%");
		}
		ArrayList<PreJobTraining> list = (ArrayList<PreJobTraining>) preJobTrainingMapper.select(preJobTraining);
		for(PreJobTraining a : list){
			a.setBeginTime(a.getBeginTime().substring(0, 10));
			a.setEndTime(a.getEndTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void savePreJobTraining(PreJobTraining preJobTraining) {
		if (preJobTraining.getSeqId() != null) {
			preJobTrainingMapper.update(preJobTraining);
		} else {
			preJobTrainingMapper.insert(preJobTraining);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deletePreJobTraining(PreJobTraining preJobTraining) {
		preJobTraining.setStatus(MnisQmConstants.STATUS_WX);
		preJobTrainingMapper.deleteByPrimaryKey(preJobTraining);
	}

	@Override
	public List<SpecialistNursesExperience> getSpecialistNursesExperience(
			SpecialistNursesExperience specialistNursesExperience) {
		if (specialistNursesExperience.getUserName() != null) {
			specialistNursesExperience.setUserName("%"
					+ specialistNursesExperience.getUserName() + "%");
		}
		if (specialistNursesExperience.getWardCode() != null) {
			specialistNursesExperience.setWardCode("%"
					+ specialistNursesExperience.getWardCode() + "%");
		}
		if (specialistNursesExperience.getJuniorCollege() != null) {
			specialistNursesExperience.setJuniorCollege("%"
					+ specialistNursesExperience.getJuniorCollege() + "%");
		}
		if (specialistNursesExperience.getAuditor() != null) {
			specialistNursesExperience.setAuditor("%"
					+ specialistNursesExperience.getAuditor() + "%");
		}
		ArrayList<SpecialistNursesExperience> list = (ArrayList<SpecialistNursesExperience>) specialistNursesExperienceMapper.select(specialistNursesExperience);
		for(SpecialistNursesExperience a : list){
			a.setBeginTime(a.getBeginTime().substring(0, 10));
			a.setEndTime(a.getEndTime().substring(0, 10));
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveSpecialistNursesExperience(
			SpecialistNursesExperience specialistNursesExperience) {
		if (specialistNursesExperience.getSeqId() != null) {
			specialistNursesExperienceMapper.update(specialistNursesExperience);
		} else {
			specialistNursesExperienceMapper.insert(specialistNursesExperience);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteSpecialistNursesExperience(
			SpecialistNursesExperience specialistNursesExperience) {
		specialistNursesExperience.setStatus(MnisQmConstants.STATUS_WX);
		specialistNursesExperienceMapper
				.deleteByPrimaryKey(specialistNursesExperience);
	}

}
