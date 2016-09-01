package com.lachesis.mnisqm.module.profSkillDoc.service;

import java.util.List;

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

public interface IProfSkillDocService {

	/**获得学术会议列表
	 * @param academicConference
	 * @return
	 */
	List<AcademicConference> getAcademicConferences(AcademicConference academicConference);
	
	/**保存学术会议
	 * @param academicConference
	 */
	void saveAcademicConference(AcademicConference academicConference);
	
	/**删除学术会议
	 * @param seqId
	 */
	void deleteAcademicConference(AcademicConference academicConference);
	
	/**获得评估考核
	 * @param assessment
	 * @return
	 */
	List<Assessment> getAssessment(Assessment assessment);
	
	/**保存评估考核
	 * @param assessment
	 */
	void saveAssessment(Assessment assessment);
	
	/**删除评估考核
	 * @param seqId
	 */
	void deleteAssessment(Assessment assessment);
	
	/**获得奖励惩罚情况
	 * @param awardPunishmentInfo
	 * @return
	 */
	List<AwardPunishmentInfo> getAwardPunishmentInfo(AwardPunishmentInfo awardPunishmentInfo);
	
	/**保存奖励惩罚情况
	 * @param awardPunishmentInfo
	 */
	void saveAwardPunishmentInfo(AwardPunishmentInfo awardPunishmentInfo);
	
	/**删除奖励惩罚情况
	 * @param seqId
	 */
	void deleteAwardPunishmentInfo(AwardPunishmentInfo awardPunishmentInfo);
	
	/**获得继续教育列表
	 * @param continuingEducation
	 * @return
	 */
	List<ContinuingEducation> getContinuingEducation(ContinuingEducation continuingEducation);
	
	/**保存继续教育
	 * @param continuingEducation
	 */
	void saveContinuingEducation(ContinuingEducation continuingEducation);
	
	/**删除继续教育
	 * @param seqId
	 */
	void deleteContinuingEducation(ContinuingEducation continuingEducation);
	
	/**获得CPR/ACLS证书列表
	 * @param cprAcls
	 * @return
	 */
	List<CprAcls> getCprAcls(CprAcls cprAcls);
	
	/**保存CPR/ACLS证书
	 * @param cprAcls
	 */
	void saveCprAcls(CprAcls cprAcls);
	
	/**删除CPR/ACLS证书
	 * @param seqId
	 */
	void deleteCprAcls(CprAcls cprAcls);
	
	/**获得教育记录与考核列表
	 * @param educationRecordInspection
	 * @return
	 */
	List<EducationRecordInspection> getEducationRecordInspection(EducationRecordInspection educationRecordInspection);
	
	/**保存教育记录与考核
	 * @param educationRecordInspection
	 */
	void saveEducationRecordInspection(EducationRecordInspection educationRecordInspection);
	
	/**删除教育记录与考核
	 * @param seqId
	 */
	void deleteEducationRecordInspection(EducationRecordInspection educationRecordInspection);
	
	/**获得护士分层分级登记列表
	 * @param nurseHirarchicalRegistration
	 * @return
	 */
	List<NurseHirarchicalRegistration> getNurseHirarchicalRegistration(NurseHirarchicalRegistration nurseHirarchicalRegistration);
	
	/**保存护士分层分级登记
	 * @param nurseHirarchicalRegistration
	 */
	void saveNurseHirarchicalRegistration(NurseHirarchicalRegistration nurseHirarchicalRegistration);
	
	/**删除护士分层分级登记
	 * @param seqId
	 */
	void deleteNurseHirarchicalRegistration(NurseHirarchicalRegistration nurseHirarchicalRegistration);
	
	/**获得个人考核结果列表
	 * @param personalAssessment
	 * @return
	 */
	List<PersonalAssessment> getPersonalAssessment(PersonalAssessment personalAssessment);
	
	/**保存个人考核结果
	 * @param personalAssessment
	 */
	void savePersonalAssessment(PersonalAssessment personalAssessment);
	
	/**删除个人考核结果
	 * @param seqId
	 */
	void deletePersonalAssessment(PersonalAssessment personalAssessment);
	
	/**获得岗位职责列表
	 * @param positionStatement
	 * @return
	 */
	List<PositionStatement> getPositionStatement(PositionStatement positionStatement);
	
	/**保存岗位职责
	 * @param positionStatement
	 */
	void savePositionStatement(PositionStatement positionStatement);
	
	/**删除岗位职责
	 * @param seqId
	 */
	void deletePositionStatement(PositionStatement positionStatement);
	
	/**获得岗前培训列表
	 * @param preJobTraining
	 * @return
	 */
	List<PreJobTraining> getPreJobTraining(PreJobTraining preJobTraining);
	
	/**保存岗前培训
	 * @param preJobTraining
	 */
	void savePreJobTraining(PreJobTraining preJobTraining);
	
	/**删除岗前培训
	 * @param seqId
	 */
	void deletePreJobTraining(PreJobTraining preJobTraining);
	
	/**获得专科护士经历列表
	 * @param specialistNursesExperience
	 * @return
	 */
	List<SpecialistNursesExperience> getSpecialistNursesExperience(SpecialistNursesExperience specialistNursesExperience);
	
	/**保存专科护士经历
	 * @param specialistNursesExperience
	 */
	void saveSpecialistNursesExperience(SpecialistNursesExperience specialistNursesExperience);
	
	/**删除专科护士经历
	 * @param seqId
	 */
	void deleteSpecialistNursesExperience(SpecialistNursesExperience specialistNursesExperience);
	
}
