package com.lachesis.mnisqm.module.qualityForm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.util.JSONUtils;

import org.apache.jackrabbit.commons.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityFormDetailMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityFormMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityIndexItemMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityIndexMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityIssueManageMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityModelDetailMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityModelMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityPlanMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityResultDetailMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityResultMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityTaskMapper;
import com.lachesis.mnisqm.module.qualityForm.dao.QualityTeamMapper;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityForm;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityFormDetail;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIndex;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIndexItem;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityInfo;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIssueManage;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityModel;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityModelDetail;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityModelTrans;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityPlan;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityResult;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityResultDetail;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityTask;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityTeam;
import com.lachesis.mnisqm.module.qualityForm.service.IQualityFormService;
import com.lachesis.mnisqm.module.remote.qualityForm.dao.QualityInfoMapper;
import com.lachesis.mnisqm.module.user.dao.ComDeptMapperExt;

@Service
public class QualityFormServiceImpl implements IQualityFormService {

	@Autowired
	private QualityFormDetailMapper qualityFormDetailMapper;
	@Autowired
	private QualityFormMapper qualityFormMapper;
	@Autowired
	private QualityTaskMapper qualityTaskMapper;
	@Autowired
	private QualityResultMapper qualityResultMapper;
	@Autowired
	private QualityResultDetailMapper qualityResultDetailMapper;
	@Autowired
	private QualityIssueManageMapper qualityIssueManageMapper;
	@Autowired
	private ComDeptMapperExt comDeptMapperExt;
	@Autowired
	private QualityInfoMapper qualityInfoMapper;
	@Autowired
	private QualityTeamMapper qualityTeamMapper;
	@Autowired
	private QualityIndexItemMapper qualityIndexItemMapper;
	@Autowired
	private QualityModelMapper qualityModelMapper;
	@Autowired
	private QualityModelDetailMapper qualityModelDetailMapper;
	@Autowired
	private QualityPlanMapper qualityPlanMapper;
	@Autowired
	private QualityIndexMapper qualityIndexMapper;

	@Override
	public int insertQualityForm(QualityForm qualityForm) {
		List<QualityFormDetail> list = qualityForm.getQualityFormDetailList();
		qualityFormMapper.insert(qualityForm);
		if (null != list && list.size() != 0) {
			for (QualityFormDetail qualityFormDetail : list) {
				qualityFormDetail.setFormCode(qualityForm.getFormCode());
				qualityFormDetail.setStatus(MnisQmConstants.STATUS_YX);
				qualityFormDetail
						.setCreatePerson(qualityForm.getCreatePerson());
				qualityFormDetail.setCreateTime(new Date());
				qualityFormDetailMapper.insert(qualityFormDetail);
			}
		}
		return 0;
	}

	@Override
	public int updateQualityForm(QualityForm qualityForm) {
		List<QualityFormDetail> list = qualityForm.getQualityFormDetailList();
		qualityFormMapper.updateByPrimaryKey(qualityForm);
		if (null != list && list.size() != 0) {
			for (QualityFormDetail qualityFormDetail : list) {
				// 新增的质量检查明细没有sqlid
				if (null == qualityFormDetail.getSeqId()
						|| 0 == qualityFormDetail.getSeqId()) {
					qualityFormDetail.setFormCode(qualityForm.getFormCode());
					qualityFormDetail.setStatus(MnisQmConstants.STATUS_YX);
					qualityFormDetail.setCreatePerson(qualityForm
							.getCreatePerson());
					qualityFormDetail.setCreateTime(new Date());
					qualityFormDetailMapper.insert(qualityFormDetail);
				} else {
					qualityFormDetail.setUpdatePerson(qualityForm
							.getUpdatePerson());// 更新人
					qualityFormDetail.setUpdateTime(new Date());// 更新时间
					qualityFormDetailMapper
							.updateByPrimaryKey(qualityFormDetail);
				}
			}
		}
		return 0;
	}

	@Override
	public void deleteQualityForm(QualityForm qualityForm) {
		if (null == qualityForm.getSeqId()) {
			throw new CommRuntimeException("seqId不允许为空！");
		}
		qualityForm.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
		qualityFormMapper.updateForDelete(qualityForm);
	}

	@Override
	public void insertQualityFormDetail(QualityFormDetail qualityFormDetail) {
		qualityFormDetailMapper.insert(qualityFormDetail);
	}

	@Override
	public int updateQualityFormDetail(QualityFormDetail qualityFormDetail) {
		return qualityFormDetailMapper.updateByPrimaryKey(qualityFormDetail);
	}

	@Override
	public void deleteQualityFormDetail(QualityFormDetail qualityFormDetail) {
		if (null == qualityFormDetail.getSeqId()) {
			throw new CommRuntimeException("seqId不允许为空！");
		}
		qualityFormDetail.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
		qualityFormDetailMapper.updateForDelete(qualityFormDetail);
	}

	@Override
	public int insertQualityTask(QualityTask qualityTask) {
		return qualityTaskMapper.insert(qualityTask);
	}

	@Override
	public int updateQualityTask(QualityTask qualityTask) {
		return qualityTaskMapper.updateByPrimaryKey(qualityTask);
	}

	@Override
	public void deleteQualityTask(QualityTask qualityTask) {
		if (null == qualityTask.getSeqId()) {
			throw new CommRuntimeException("seqId不允许为空！");
		}
		qualityTask.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
		qualityTaskMapper.updateForDelete(qualityTask);
	}

	@Override
	public List<QualityForm> queryQualityFormByDeptCodeAndType(String type) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("type", type);
		return qualityFormMapper.queryByDeptCodeAndType(conditionMap);
	}

	@Override
	public List<QualityTask> queryQTRByTimeAndTypeAndDeptCode(String date,
			String deptCode, String type) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("date", date + "%");
		if (null == deptCode) {
			conditionMap.put("deptCode", deptCode);
		} else {
			conditionMap.put("deptCode", "%" + deptCode + "%");
		}
		conditionMap.put("type", type);
		List<QualityTask> list = qualityTaskMapper
				.queryByTimeAndTypeAndDeptCode(conditionMap);
		String[] temp;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			temp = list.get(i).getDeptCode().split(",");
			sb.setLength(0);
			for (String string : temp) {
				sb.append(comDeptMapperExt.selectDeptByCode(string)
						.getDeptName() + ",");
			}
			if (sb.length() != 0) {
				list.get(i).setDeptNames(
						sb.toString().substring(0, sb.length() - 1));
			}
		}
		return list;
	}

	@Override
	public int insertQualityResult(QualityResult qualityResult) {
		List<QualityResultDetail> list = qualityResult
				.getQualityResultDetailList();
		qualityResultMapper.insert(qualityResult);
		if (null != list && list.size() != 0) {
			for (QualityResultDetail qualityResultDetail : list) {
				qualityResultDetail
						.setResultCode(qualityResult.getResultCode());
				qualityResultDetail.setStatus(MnisQmConstants.STATUS_YX);
				qualityResultDetail.setCreatePerson(qualityResult
						.getCreatePerson());
				qualityResultDetail.setCreateTime(new Date());
				qualityResultDetailMapper.insert(qualityResultDetail);
			}
		}
		return 0;
	}

	@Override
	public int updateQualityResult(QualityResult qualityResult) {
		List<QualityResultDetail> list = qualityResult
				.getQualityResultDetailList();
		qualityResultMapper.updateByPrimaryKey(qualityResult);
		if (null != list && list.size() != 0) {
			for (QualityResultDetail qualityResultDetail : list) {
				// 新增的质量检查明细没有sqlid
				if (null == qualityResultDetail.getSeqId()
						|| 0 == qualityResultDetail.getSeqId()) {
					qualityResultDetail.setResultCode(qualityResult
							.getResultCode());
					qualityResultDetail.setStatus(MnisQmConstants.STATUS_YX);
					qualityResultDetail.setCreatePerson(qualityResult
							.getCreatePerson());
					qualityResultDetail.setCreateTime(new Date());
					qualityResultDetailMapper.insert(qualityResultDetail);
				} else {
					qualityResultDetail.setUpdatePerson(qualityResult
							.getUpdatePerson());
					qualityResultDetail.setUpdateTime(qualityResult
							.getUpdateTime());
					qualityResultDetailMapper
							.updateByPrimaryKey(qualityResultDetail);
				}
			}
		}
		return 0;
	}

	@Override
	public void deleteQualityResult(QualityResult qualityResult) {
		if (null == qualityResult.getSeqId()) {
			throw new CommRuntimeException("seqId不允许为空！");
		}
		qualityResult.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
		qualityResultMapper.updateForDelete(qualityResult);
	}

	@Override
	public int insertQualityResultDetail(QualityResultDetail qualityResultDetail) {
		return qualityResultDetailMapper.insert(qualityResultDetail);
	}

	@Override
	public int updateQualityResultDetail(QualityResultDetail qualityResultDetail) {
		return qualityResultDetailMapper
				.updateByPrimaryKey(qualityResultDetail);
	}

	@Override
	public void deleteQualityResultDetail(
			QualityResultDetail qualityResultDetail) {
		if (null == qualityResultDetail.getSeqId()) {
			throw new CommRuntimeException("seqId不允许为空！");
		}
		qualityResultDetail.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
		qualityResultDetailMapper.updateForDelete(qualityResultDetail);
	}

	@Override
	public int insertQualityIssueManage(QualityIssueManage qualityIssueManage) {
		return qualityIssueManageMapper.insert(qualityIssueManage);
	}

	@Override
	public int updateQualityIssueManage(QualityIssueManage qualityIssueManage) {
		return qualityIssueManageMapper.updateByPrimaryKey(qualityIssueManage);
	}

	@Override
	public void deleteQualityIssueManage(String seqId) {
		qualityIssueManageMapper.deleteByPrimaryKey(Long.parseLong(seqId));
	}

	@Override
	public QualityIssueManage getQualityIssueManageByResultCode(
			String resultCode) {
		return qualityIssueManageMapper.getByresultCode(resultCode);
	}

	@Override
	public List<QualityIssueManage> queryQIMByTimeAndTypeAndDeptCode(
			String date, String deptCode, String type) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("date", date + "%");
		conditionMap.put("deptCode", deptCode);
		conditionMap.put("type", type);
		return qualityIssueManageMapper
				.queryByTimeAndTypeAndDeptCode(conditionMap);
	}

	@Override
	public List<QualityResult> queryQRByTimeAndFormNameAndDeptCode(String date,
			String deptCode) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("date", date + "%");
		conditionMap.put("deptCode", deptCode);
		return qualityResultMapper
				.queryByTimeAndFormNameAndDeptCode(conditionMap);
	}

	@Override
	public List<QualityForm> queryAllQualityForm() {
		return qualityFormMapper.selectAll();
	}

	@Override
	public List<QualityResult> queryAllQualityResult() {
		return qualityResultMapper.selectAll();
	}

	@Override
	public List<QualityInfo> selectAllQualityInfo() {
		return qualityInfoMapper.selectAll();
	}

	@Override
	@Transactional
	public void saveQualityTeam(QualityTeam qualityTeam) {
		qualityTeam.setStatus(MnisQmConstants.STATUS_YX);
		List<QualityTeam> test = null;
		if(qualityTeam.getTeamCode()!=null){
			test = qualityTeamMapper.selectByTeamCode(
					qualityTeam.getTeamCode(), null);
		}
		if (test == null || test.size() == 0) {
			String code = CodeUtils.getSysInvokeId();//新建小组时teamCode
			List<String> userList = qualityTeam.getUserList();
			for (String s : userList) {
				qualityTeam.setUserCode(s);
				qualityTeam.setTeamCode(code);
				qualityTeamMapper.insert(qualityTeam);
			}
		} else {
			List<String> preList = new ArrayList<String>();
			for(QualityTeam q : test){
				preList.add(q.getUserCode());//更改前小组成员
			}
			List<String> userList = qualityTeam.getUserList();
			for (String s : userList) {
				if(preList.contains(s)){
					qualityTeam.setUserCode(s);
					qualityTeamMapper.update(qualityTeam);//更新名字和组长
					preList.remove(s);//要保存的小组成员如果存在，移除，保留要删除的
				}else{
					qualityTeam.setUserCode(s);//如果不存在，就是新增小组成员
					qualityTeamMapper.insert(qualityTeam);
				}
			}
			for(String s: preList){
				if(s!=null){
					qualityTeam.setUserCode(s);
					deletQualityTeamByPrimaryKey(qualityTeam);//删除要删除的成员
				}
			}
		}
	}

	@Override
	public List<QualityTeam> selectQualityTeamByTeamName(String teamName) {
		List<QualityTeam> list = null;
		List<QualityTeam> teamCodeList = qualityTeamMapper.selectUniqueTeamCode();// 找到所有队
		if (teamCodeList != null && teamCodeList.size()!=0) {
			list = new ArrayList<QualityTeam>();//
			// 每一组一个对象插入list
			for (QualityTeam q : teamCodeList) {
				QualityTeam qualityTeam = new QualityTeam();
				if(q!=null){
					List<QualityTeam> team = qualityTeamMapper.selectByTeamCode(
							q.getTeamCode(), null);// 找到队编号对应小组
					ArrayList<String> userList = new ArrayList<String>();//userList用来保存本组成员
					for (QualityTeam q2 : team) {
						userList.add(q2.getUserCode());// 成员名放入列表
					}
					qualityTeam.setUserList(userList);
					qualityTeam.setTeamName(team.get(0).getTeamName());
					qualityTeam.setTeamLeader(team.get(0).getTeamLeader());
					qualityTeam.setTeamCode(q.getTeamCode());
					list.add(qualityTeam);// 一组放入列表
				}
			}
		}
		return list;
	}

	@Override
	public void deletQualityTeamByPrimaryKey(QualityTeam qualityTeam) {
		String userCode = qualityTeam.getUserCode();
		if(userCode!=null && !userCode.equals("")){
			List<QualityTask> list = qualityTaskMapper.queryByHandUserName(userCode);
			if(list!=null && list.size()!=0){
				throw new CommRuntimeException("此组员有未完成任务，不能删除");
			}
			qualityTeamMapper.deleteByPrimaryKey(qualityTeam);
		}else{
			List<QualityTeam> teamList = qualityTeamMapper.selectByTeamCode(qualityTeam.getTeamCode(), null);
			for(QualityTeam q : teamList){
				qualityTeam.setUserCode(q.getUserCode());
				deletQualityTeamByPrimaryKey(qualityTeam);
			}
			
		}
	}

	@Override
	public void saveQualityIndexItem(QualityIndexItem qualityIndexItem) {
		if (qualityIndexItem.getSeqId() == null) {
			qualityIndexItem.setStatus(MnisQmConstants.STATUS_YX);
			qualityIndexItemMapper.insert(qualityIndexItem);
		} else {
			qualityIndexItemMapper.update(qualityIndexItem);
		}
	}

	@Override
	public List<QualityIndexItem> selectQualityIndexItem(Map<String, Object> map) {
		return qualityIndexItemMapper.select(map);
	}

	@Override
	public void deleteQualityIndexItem(Long seqId) {
		qualityIndexItemMapper.deleteByUpdate(seqId);
	}

	@Override
	public Long saveQualityModel(QualityModel qualityModel) {
		
		String createPerson = qualityModel.getCreatePerson();
		List<QualityFormDetail> detailList = qualityModel.getQualityFormDetailList();
		if(detailList==null || detailList.isEmpty()){
			//如果是保存项目，则模板不保存
			if (qualityModel.getSeqId() == null) {
				qualityModel.setStatus(MnisQmConstants.STATUS_YX);
				qualityModel.setIsUsed("1");
				qualityModelMapper.insert(qualityModel);
			} else {
				qualityModelMapper.update(qualityModel);
			}
		}else{
			List<QualityForm> formList = qualityModel.getQualityFormList();//调查类型列表
			String formCode = null;
			if (formList != null && formList.size()!=0) {
				for (QualityForm qualityForm : formList) {
					if(qualityForm.getSeqId()==null){
						qualityForm.setStatus(MnisQmConstants.STATUS_YX);
						qualityForm.setCreatePerson(createPerson);
						qualityForm.setUpdatePerson(createPerson);
						if(StringUtils.isEmpty(qualityForm.getParentType())){
							qualityForm.setParentType(formCode);
						}
						qualityForm.setFormCode(CodeUtils.getSysInvokeId());
						qualityFormMapper.insert(qualityForm);
					}else{
						qualityForm.setUpdatePerson(createPerson);
						qualityFormMapper.updateByPrimaryKey(qualityForm);
					}
					//下一跳数据的父类型
					formCode = qualityForm.getFormCode();
				}
			}
			
			if(detailList!=null && detailList.size()!=0){
				for(QualityFormDetail detail : detailList){
					if(detail.getSeqId()==null){
						detail.setStatus(MnisQmConstants.STATUS_YX);
						detail.setCreatePerson(createPerson);
						detail.setUpdatePerson(createPerson);
						if(StringUtils.isEmpty(detail.getFormCode())){
							detail.setFormCode(formCode);
						}
						qualityFormDetailMapper.insert(detail);
						//录入关联关系
						QualityModelDetail qualityModelDetail = new QualityModelDetail();
						qualityModelDetail.setModelId(qualityModel.getSeqId());
						qualityModelDetail.setDetailId(detail.getSeqId());
						qualityModelDetailMapper.insert(qualityModelDetail);
					}else{
						if(StringUtils.isEmpty(detail.getFormCode())){
							detail.setFormCode(formCode);
						}
						detail.setUpdatePerson(createPerson);
						detail.setStatus(MnisQmConstants.STATUS_YX);
						detail.setUpdatePerson(createPerson);
						qualityFormDetailMapper.updateByPrimaryKey(detail);
					}
				}
			}
		}
		
		return qualityModel.getSeqId();
	}

	@Override
	public List<QualityModel> selectQualityModel(QualityModel qualityModel) {
		List<QualityModel> modelList = qualityModelMapper.selectByCode(qualityModel);
		if(qualityModel.getSeqId() != null && modelList!=null && 
				!modelList.isEmpty()){
			QualityModel model = modelList.get(0);
			//查询子项
			QualityModelDetail pDetail = new QualityModelDetail();
			pDetail.setModelId(qualityModel.getSeqId());
			List<QualityFormDetail> details = qualityModelDetailMapper.selectModelById(pDetail);
			List<QualityForm> formList = new ArrayList<QualityForm>();
			if(details!=null){
				//通过项目查询类型
				Set<String> formSet = new HashSet<String>();
				for(QualityFormDetail dtl :details){
					String code = dtl.getFormCode();
					if(!formSet.contains(code)){
						QualityForm pform = qualityModelDetailMapper.selectFormByCode(dtl);
						if(pform!=null){
							formList.add(pform);
							formSet.add(code);
						}
						if(pform!=null&&!formSet.contains(pform.getParentType())){
							QualityForm form = qualityModelDetailMapper.selectFormByPCode(pform);
							if(form != null){
								formList.add(form);
								formSet.add(form.getFormCode());
							}
						}
					}
				}
			}
			
			model.setQualityFormDetailList(details);
			model.setQualityFormList(formList);
		}
		//计算总分
		for(QualityModel m : modelList){
			if(m.getTotalScore()==null){
				m.setTotalScore(0);
			}
			QualityModelDetail modelDetail = new QualityModelDetail();
			modelDetail.setModelId(m.getSeqId());
			List<QualityFormDetail> detailList = qualityModelDetailMapper.selectModelById(modelDetail);
			if(detailList!=null && detailList.size()!=0){
				for(QualityFormDetail det : detailList){
					m.setTotalScore(m.getTotalScore()+det.getPoints());
				}
			}
		}
		return modelList;
	}

	@Override
	public void deleteQualityModel(Long seqId) {
		qualityModelMapper.delete(seqId);
		QualityModelDetail qualityModelDetail = new QualityModelDetail();
		qualityModelDetail.setModelId(seqId);
		qualityModelDetailMapper.delete(qualityModelDetail);//删除中间表
	}

	@Override
	public List<QualityPlan> selectQualityPlan(QualityPlan qualityPlan) {
		List<QualityPlan> planList = qualityPlanMapper.select(qualityPlan);
		Long planId = null;
		if(planList!=null && planList.size()!=0){
			for(QualityPlan plan : planList){
				planId = plan.getSeqId();
				List<QualityTask> taskList = qualityTaskMapper.queryByPlanId(planId);
				plan.setQualityTaskList(taskList);
			}
		}
		return planList;
	}

	@Override
	public Long saveQualityPlan(QualityPlan qualityPlan) {
//		String createPerson = qualityPlan.getCreatePerson();
		if(qualityPlan.getModelId()!=null){
			QualityModel qualityModel = new QualityModel();
			qualityModel.setSeqId(qualityPlan.getModelId());
			qualityPlan.setModelName(qualityModelMapper.selectByCode(qualityModel).get(0).getModelName());
		}
		if(qualityPlan.getTeamCode()!=null){
			String teamName = qualityTeamMapper.selectByTeamCode(qualityPlan.getTeamCode(), null).get(0).getTeamName();
			qualityPlan.setTeamName(teamName);
		}
		if(qualityPlan.getSeqId()==null){
			qualityPlan.setStatus(MnisQmConstants.STATUS_YX);
			qualityPlanMapper.insert(qualityPlan);
		}else{
			qualityPlanMapper.update(qualityPlan);
		}
//		List<QualityTask> taskList = qualityPlan.getQualityTaskList();
//		if(taskList!=null && taskList.size()!=0){
//			for(QualityTask task : taskList){
//				if(task.getSeqId()==null){
//					task.setStatus(MnisQmConstants.STATUS_YX);
//					task.setCreatePerson(createPerson);
//					task.setUpdatePerson(createPerson);
//					task.setPlanId(qualityPlan.getSeqId());
//					qualityTaskMapper.insert(task);
//				}else{
//					task.setPlanId(qualityPlan.getSeqId());
//					qualityTaskMapper.updateByPrimaryKey(task);
//				}
//			}
//		}
		return qualityPlan.getSeqId();
	}

	@Override
	public void deleteQualityPlan(Long seqId) {
		qualityPlanMapper.delete(seqId);
	}

	@Override
	public List<QualityIndex> selectQualityIndex(QualityIndex qualityIndex) {
		return qualityIndexMapper.select(qualityIndex);
	}

	@Override
	public void saveQualityIndex(QualityIndex qualityIndex) {
		if(qualityIndex.getSeqId()==null){
			qualityIndex.setStatus(MnisQmConstants.STATUS_YX);
			qualityIndexMapper.insert(qualityIndex);
		}else{
			qualityIndexMapper.update(qualityIndex);
		}
	}

	@Override
	public void deleteQualityIndex(Long seqId) {
		qualityIndexMapper.delete(seqId);
	}

	@Override
	public void savetModelDetail(QualityModelTrans qualityModelTrans) {
		QualityForm qualityForm = null;
		QualityFormDetail qualityFormDetail = null;
		QualityForm parentForm = null;
		if(qualityModelTrans.getTypeId()!=null){
			qualityForm = qualityFormMapper.selectByPrimaryKey(qualityModelTrans.getTypeId());
			qualityForm.setFormName(qualityModelTrans.getTypeName());//设置子类型名
			qualityFormMapper.updateByPrimaryKey(qualityForm);//修改子类型
		}else{
			qualityForm = new QualityForm();
			qualityForm.setFormName(qualityModelTrans.getTypeName());
			qualityFormMapper.insert(qualityForm);//插入新子类名
		}
		if(qualityModelTrans.getBigTypeId()!=null){
			parentForm = qualityFormMapper.selectByPrimaryKey(qualityModelTrans.getBigTypeId());
			parentForm.setFormName(qualityModelTrans.getTypeName());//设置父类名
			qualityFormMapper.updateByPrimaryKey(parentForm);
		}else{
			parentForm = new QualityForm();
			parentForm.setFormName(qualityModelTrans.getTypeName());//设置父类名
			qualityFormMapper.insert(parentForm);
		}
		if(qualityModelTrans.getDetailId()!=null){
			qualityFormDetail=qualityFormDetailMapper.selectByPrimaryKey(qualityModelTrans.getDetailId());
			qualityFormDetail.setDetailName(qualityModelTrans.getDetailName());
			qualityFormDetail.setDescrip(qualityModelTrans.getDescrip());
			qualityFormDetail.setPoints(qualityModelTrans.getPoints());
			qualityFormDetail.setStantard(qualityModelTrans.getStandard());
			qualityFormDetailMapper.updateByPrimaryKey(qualityFormDetail);
		}else{
			qualityFormDetail =  new QualityFormDetail();
			qualityFormDetail.setDetailName(qualityModelTrans.getDetailName());
			qualityFormDetail.setDescrip(qualityModelTrans.getDescrip());
			qualityFormDetail.setPoints(qualityModelTrans.getPoints());
			qualityFormDetail.setStantard(qualityModelTrans.getStandard());
			qualityFormDetailMapper.insert(qualityFormDetail);
		}
		
	}

	@Override
	public QualityModel copyQualityModel(Long seqId, String userCode) {
		QualityModel qualityModel = new QualityModel();
		if(seqId==null){
			qualityModel.setModelName("");
			qualityModel.setCreatePerson(userCode);
			qualityModel.setUpdatePerson(userCode);
			qualityModel.setIsUsed("1");
			qualityModel.setStatus(MnisQmConstants.STATUS_YX);
			qualityModel.setSeqId(qualityModelMapper.insert(qualityModel));//新增模板
			return qualityModel;//返回模板
		}
		qualityModel.setSeqId(seqId);
		List<QualityModel> modelList = selectQualityModel(qualityModel);
		
		QualityModel copiedModel = null;
		if(modelList!=null && modelList.size()!=0){
			copiedModel =  modelList.get(0);
		}
		copiedModel.setCreatePerson(userCode);
		copiedModel.setUpdatePerson(userCode);
		copiedModel.setSeqId(null);
		qualityModelMapper.insert(copiedModel);//新增复制的模板，返回模板id
		String oldFormCode;//旧类型编号
		List<QualityForm> formList = copiedModel.getQualityFormList();
		List<QualityFormDetail> detailList = copiedModel.getQualityFormDetailList();
		Set<String> parentCodes = new HashSet<String>();
		for(QualityForm form : formList){
			parentCodes.add(form.getParentType());//获得所有父类
		}
		Iterator<String> iter = parentCodes.iterator();
		Map<String, String> newParentCodes = new HashMap<String, String>();
		while(iter.hasNext()){
			newParentCodes.put(iter.next(),CodeUtils.getSysInvokeId());//新的父类code对应老的父类code
		}
		for(QualityForm form : formList){
			form.setSeqId(null);
			oldFormCode = form.getFormCode();
			String oldParentCode = form.getParentType();
			String newCode = CodeUtils.getSysInvokeId();//新类型编号
			if(parentCodes.contains(oldFormCode)){//如果是父类
				form.setFormCode(newParentCodes.get(oldFormCode));
			}else{//如果是子类
				form.setFormCode(newCode);
				form.setParentType(newParentCodes.get(oldParentCode));
			}
			qualityFormMapper.insert(form);//新增复制的类型表
			//插入类型后关联formCode
			for(QualityFormDetail detail :detailList ){
				if(oldFormCode.equals(detail.getFormCode())){
					detail.setSeqId(null);
					detail.setStatus(MnisQmConstants.STATUS_YX);
					if(parentCodes.contains(oldFormCode)){//如果是父类
						detail.setFormCode(newParentCodes.get(oldFormCode));
					}else{
						detail.setFormCode(newCode);
					}
					qualityFormDetailMapper.insert(detail);//新增复制项目，返回项目Id
					QualityModelDetail qmd = new QualityModelDetail();
					qmd.setModelId(copiedModel.getSeqId());
					qmd.setDetailId(detail.getSeqId());
					qualityModelDetailMapper.insert(qmd);//插入项目、模板关联
				}
			}
		}
		return copiedModel;//返回复制模板
	}
}
