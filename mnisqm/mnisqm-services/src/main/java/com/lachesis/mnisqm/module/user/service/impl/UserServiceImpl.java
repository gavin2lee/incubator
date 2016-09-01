package com.lachesis.mnisqm.module.user.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.constants.MnisQmConstants.SysDicConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.DateUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.remote.user.dao.AllocateMapper;
import com.lachesis.mnisqm.module.remote.user.dao.BusyDeptInfoMapper;
import com.lachesis.mnisqm.module.remote.user.dao.DeptAllocateApplyMapper;
import com.lachesis.mnisqm.module.remote.user.dao.DeptBedInfoMapper;
import com.lachesis.mnisqm.module.remote.user.dao.DeptHumanResourceInfoMapper;
import com.lachesis.mnisqm.module.remote.user.dao.DeptSchedulInfoMapper;
import com.lachesis.mnisqm.module.remote.user.dao.JuniorAskForLeaveMapper;
import com.lachesis.mnisqm.module.remote.user.dao.SeniorSearchMapper;
import com.lachesis.mnisqm.module.remote.user.dao.TotalInfoMapper;
import com.lachesis.mnisqm.module.system.dao.SysUserMapperExt;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.system.service.ICacheService;
import com.lachesis.mnisqm.module.user.dao.ComDeptMapper;
import com.lachesis.mnisqm.module.user.dao.ComDeptMapperExt;
import com.lachesis.mnisqm.module.user.dao.ComDeptUserMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserChangeMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserEducationMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserEducationMapperExt;
import com.lachesis.mnisqm.module.user.dao.ComUserFamilyMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserFamilyMapperExt;
import com.lachesis.mnisqm.module.user.dao.ComUserLearningMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserLearningMapperExt;
import com.lachesis.mnisqm.module.user.dao.ComUserMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserMapperExt;
import com.lachesis.mnisqm.module.user.dao.ComUserNursingMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserNursingMapperExt;
import com.lachesis.mnisqm.module.user.dao.ComUserPositionMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserPositionMapperExt;
import com.lachesis.mnisqm.module.user.dao.ComUserTrainingMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserTrainingMapperExt;
import com.lachesis.mnisqm.module.user.domain.Allocate;
import com.lachesis.mnisqm.module.user.domain.BusyDeptInfo;
import com.lachesis.mnisqm.module.user.domain.ComDept;
import com.lachesis.mnisqm.module.user.domain.ComDeptUser;
import com.lachesis.mnisqm.module.user.domain.ComUser;
import com.lachesis.mnisqm.module.user.domain.ComUserChange;
import com.lachesis.mnisqm.module.user.domain.ComUserEducation;
import com.lachesis.mnisqm.module.user.domain.ComUserFamily;
import com.lachesis.mnisqm.module.user.domain.ComUserLearning;
import com.lachesis.mnisqm.module.user.domain.ComUserNursing;
import com.lachesis.mnisqm.module.user.domain.ComUserPosition;
import com.lachesis.mnisqm.module.user.domain.ComUserStatistics;
import com.lachesis.mnisqm.module.user.domain.ComUserTraining;
import com.lachesis.mnisqm.module.user.domain.DeptAllocateApply;
import com.lachesis.mnisqm.module.user.domain.DeptBedInfo;
import com.lachesis.mnisqm.module.user.domain.DeptHumanResourceInfo;
import com.lachesis.mnisqm.module.user.domain.DeptSchedulInfo;
import com.lachesis.mnisqm.module.user.domain.JuniorAskForLeave;
import com.lachesis.mnisqm.module.user.domain.SeniorSearch;
import com.lachesis.mnisqm.module.user.domain.Statistics;
import com.lachesis.mnisqm.module.user.domain.TotalInfo;
import com.lachesis.mnisqm.module.user.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private ICacheService cacheService;
	
	@Autowired
	private ComUserMapper comUserMapper;

	@Autowired
	private ComUserMapperExt comUserMapperExt;

	@Autowired
	private SysUserMapperExt userMapperExt;

	@Autowired
	private ComDeptMapper comDeptMapper;

	@Autowired
	private ComDeptMapperExt comDeptMapperExt;
	
	@Autowired
	private ComUserEducationMapper comUserEducationMapper;

	@Autowired
	private ComUserEducationMapper comUserEducationMaper;

	@Autowired
	private ComUserTrainingMapper comUserTrainingMapper;

	@Autowired
	private ComUserLearningMapper comUserLearningMapper;

	@Autowired
	private ComUserFamilyMapper comUserFamilyMapper;

	@Autowired
	private ComUserNursingMapper comUserNursingMapper;

	@Autowired
	private ComUserPositionMapper comUserPositionMapper;

	@Autowired
	private ComUserPositionMapperExt comUserPositionMapperExt;
	
	@Autowired
	private ComUserNursingMapperExt comUserNursingMapperExt;

	@Autowired
	private ComUserEducationMapperExt comUserEducationMaperExt;

	@Autowired
	private ComUserTrainingMapperExt comUserTrainingMapperExt;

	@Autowired
	private ComUserLearningMapperExt comUserLearningMapperExt;

	@Autowired
	private ComUserFamilyMapperExt comUserFamilyMapperExt;
	
	@Autowired
	private JuniorAskForLeaveMapper juniorAskForLeaveMapper;
	
	
	@Autowired
	private ComDeptUserMapper comDeptUserMapper;
	
	@Autowired
	private TotalInfoMapper totalInfoMapper;
	
	@Autowired
	private BusyDeptInfoMapper busyDeptInfoMapper;
	
	@Autowired
	private DeptHumanResourceInfoMapper deptHumanResourceInfoMapper;
	
	@Autowired
	private DeptSchedulInfoMapper deptSchedulInfoMapper;
	
	@Autowired
	private DeptBedInfoMapper deptBedInfoMapper;
	
	@Autowired
	private DeptAllocateApplyMapper deptAllocateApplyMapper;
	
	@Autowired
	private AllocateMapper allocateMapper;
	
	@Autowired
	private SeniorSearchMapper seniorSearchMapper;
	
	@Autowired
	private ComUserChangeMapper comUserChangeMapper;

	@Override
	public List<ComUser> getUserList(String depotCode) {
		// 整合参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("deptCode", depotCode);
		// 获取用户列表
		return comUserMapperExt.selectUserByDepot(param);
	}

	@Override
	public SysUser getUserByLoginName(String loginName){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("loginName", loginName);
		return userMapperExt.selectSysUserByName(param);
	}

	@Override
	public List<ComDept> getAllDepts(){
		return comDeptMapper.selectAll();
	}

	@Override
	public Map<String, Object> getUserDepts(String deptCode,String sysData){
		//先考虑简单情况，只获取直属部门信息
		Map<String, Object> userDeptMap = new HashMap<String, Object>();
		ComDept userDept = comDeptMapperExt.selectDeptByCode(deptCode);
		if(null != userDept){
			userDeptMap.put("deptCode", userDept.getDeptCode());
			userDeptMap.put("deptName", userDept.getDeptName());
			if(!MnisQmConstants.SYS_DATA_1.equals(sysData)){
				userDeptMap.put("sysData", sysData);
			}

			List<ComDept> belongedDepts = comDeptMapperExt.selectSubDeptsByCode(userDeptMap);
			//获取用户列表
			List<ComUser> users = getUserList(null);
			Map<String,List<Map<String,Object>>> userMaps = new HashMap<String,List<Map<String,Object>>>();
			for(ComUser user :users){
				String dept = user.getDeptCode();
				List<Map<String,Object>> ls = userMaps.get(dept);
				if(ls == null){
					ls = new ArrayList<Map<String,Object>>();
				}
				Map<String,Object> userMap = new HashMap<String,Object>();
				userMap.put("userCode", user.getUserCode());
				userMap.put("userName", user.getUserName());
				ls.add(userMap);
				userMaps.put(dept, ls);
			}
			userDeptMap.put("user", userMaps.get(userDept.getDeptCode()));
			if(null != belongedDepts){
				List<Map<String,Object>> deptList = new ArrayList<Map<String, Object>>();
				for(ComDept dept : belongedDepts){
					Map<String, Object> deptMap= new HashMap<String, Object>();
					deptMap.put("deptCode", dept.getDeptCode());
					deptMap.put("deptName", dept.getDeptName());
					deptMap.put("user", userMaps.get(dept.getDeptCode()));
					deptList.add(deptMap);
				}
				userDeptMap.put("belongedDepts", deptList);
			}else{
				userDeptMap.put("belongedDepts", null);
			}
		}

		return userDeptMap;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int inserObj(Object obj) {
		int refV = 0;
		if(obj instanceof ComUserEducation){
			/*
			 * 教育信息
			 */
			ComUserEducation education = (ComUserEducation) obj;
			education.setStatus(MnisQmConstants.STATUS_YX);//有效标志
			refV=comUserEducationMapper.insert(education);
		}else if(obj instanceof ComUserFamily){
			/*
			 * 家庭信息
			 */
			ComUserFamily family = (ComUserFamily) obj;
			family.setStatus(MnisQmConstants.STATUS_YX);//有效标志
			refV=comUserFamilyMapper.insert(family);
		}else if(obj instanceof ComUserLearning){
			ComUserLearning learning = (ComUserLearning) obj;
			learning.setStatus(MnisQmConstants.STATUS_YX);//有效标志
			refV=comUserLearningMapper.insert(learning);
		}else if(obj instanceof ComUserTraining){
			ComUserTraining training = (ComUserTraining) obj;
			training.setStatus(MnisQmConstants.STATUS_YX);//有效标志
			refV=comUserTrainingMapper.insert(training);
		}else if(obj instanceof ComUser){
			/*
			 * 用户基本信息
			 */
			ComUser baseInfo = (ComUser)obj;
			baseInfo.setStatus(MnisQmConstants.STATUS_YX);//有效标志
			refV=comUserMapper.insert(baseInfo);
			
			if(refV>0
					&&!StringUtils.isEmpty(baseInfo.getDeptCode())){
				ComDeptUser comDeptUser = new ComDeptUser();
				comDeptUser.setDeptCode(baseInfo.getDeptCode());
				comDeptUser.setUserCode(baseInfo.getUserCode());
				comDeptUser.setCreatePerson(baseInfo.getCreatePerson());
				comDeptUser.setUpdatePerson(baseInfo.getUpdatePerson());
				comDeptUser.setStatus(MnisQmConstants.STATUS_YX);//有效标志
				refV=comDeptUserMapper.insert(comDeptUser);
			}
		}else if(obj instanceof ComUserPosition){
			ComUserPosition positionInfo = (ComUserPosition)obj;
			positionInfo.setStatus(MnisQmConstants.STATUS_YX);//有效标志
			refV=comUserPositionMapper.insert(positionInfo);
		}else if(obj instanceof ComUserNursing){
			ComUserNursing nursingInfo = (ComUserNursing) obj;
			nursingInfo.setStatus(MnisQmConstants.STATUS_YX);//有效标志
			refV= comUserNursingMapper.insert(nursingInfo);
		}
		return refV;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteUserInfo(Object obj) {
		/**
		 * 用户相关信息删除
		 * 对于删除，不允许进行物理删除
		 */
		if(obj instanceof ComUserEducation){
			ComUserEducation comUserEducation  = (ComUserEducation) obj;
			comUserEducationMapper.deleteByPrimaryKey(comUserEducation);
		}else if(obj instanceof ComUserFamily){
			ComUserFamily comUserFamily = (ComUserFamily) obj;
			comUserFamilyMapper.deleteByPrimaryKey(comUserFamily);
		}else if(obj instanceof ComUserLearning){
			ComUserLearning comUserLearning = (ComUserLearning) obj;
			comUserLearningMapper.deleteByPrimaryKey(comUserLearning);
		}else if(obj instanceof ComUserTraining){
			ComUserTraining comUserTraining = (ComUserTraining) obj;
			comUserTrainingMapper.deleteByPrimaryKey(comUserTraining);
		}else if(obj instanceof ComUser){
			ComUser user = (ComUser)obj;
			comUserMapper.deleteByPrimaryKey(user);
		}else if(obj instanceof ComUserPosition){
			ComUserPosition position = (ComUserPosition)obj;
			comUserPositionMapper.deleteByPrimaryKey(position);
		}else if(obj instanceof ComUserNursing){
			ComUserNursing nursing = (ComUserNursing) obj;
			 comUserNursingMapper.deleteByPrimaryKey(nursing);
		}
	}

	@Override
	public Map<String, Object> getUserDetailInfo(String userCode,String hisCode){
		Map<String, Object> userInfo = new HashMap<String, Object>();
		ComUser user = null;
		if(!StringUtils.isEmpty(userCode)){
			user = comUserMapperExt.selectUserByCode(userCode);
		}else{
			user = comUserMapperExt.selectUserByHisCode(hisCode);
		}
		if(null != user){
			user.setPapersTypeName(cacheService.getSysDicValue(SysDicConstants.papersType, user.getPapersType()));
			user.setPracticeTypeName(cacheService.getSysDicValue(SysDicConstants.practiceType, user.getPracticeType()));
			user.setPolitacalStatusName(cacheService.getSysDicValue(SysDicConstants.politacalStatus, user.getPolitacalStatus()));
			user.setNatureName(cacheService.getSysDicValue(SysDicConstants.nature, user.getNature()));
			user.setGenderName(cacheService.getSysDicValue(SysDicConstants.gender, user.getGender()));
			userInfo.put("comUser", user);
			userCode = user.getUserCode();//员工编号
			userInfo.put("comUserPosition", comUserPositionMapperExt.selectByUsercode(userCode));
			userInfo.put("comUserNursing", comUserNursingMapperExt.selectByUsercode(userCode));
			userInfo.put("comUserEducation", comUserEducationMaperExt.selectByUsercode(userCode));
			userInfo.put("comUserTraining", comUserTrainingMapperExt.selectByUsercode(userCode));
			userInfo.put("comUserLearning", comUserLearningMapperExt.selectByUsercode(userCode));
			userInfo.put("comUserFamily", comUserFamilyMapperExt.selectByUsercode(userCode));
		}
		return userInfo;
	}

	@Override
	public int saveUserInfo(Object userInfo, String infoType) {
		switch(infoType){
			case "userInfo":
				ComUser baseInfo = (ComUser)userInfo;
				return comUserMapper.insert(baseInfo);
			case "positionInfo":
				ComUserPosition positionInfo = (ComUserPosition)userInfo;
				return comUserPositionMapper.insert(positionInfo);
			case "nursingInfo":
				ComUserNursing nursingInfo = (ComUserNursing) userInfo;
				return comUserNursingMapper.insert(nursingInfo);
			case "educationInfo":
				ComUserEducation educationInfo = (ComUserEducation) userInfo;
				return comUserEducationMaper.insert(educationInfo);
			case "trainingInfo":
				ComUserTraining trainingInfo = (ComUserTraining) userInfo;
				return comUserTrainingMapper.insert(trainingInfo);
			case "learningInfo":
				ComUserLearning learningInfo = (ComUserLearning) userInfo;
				return comUserLearningMapper.insert(learningInfo);
			case "familyInfo":
				ComUserFamily familyInfo = (ComUserFamily) userInfo;
				return comUserFamilyMapper.insert(familyInfo);
			default:
				return 0;
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComUser(ComUser user){
		if(null == user.getSeqId()){
			ComUser comUser = comUserMapperExt.selectUserByHisCode(user.getHisCode());
			if(null != comUser){
				throw new CommRuntimeException("员工编号为:"+user.getHisCode() +"的员工已经存在!");
			}
			if(user.getBirthday()!=null){
				try{
					DateUtils.parseDate(user.getBirthday(), DateUtils.YMD);
				}catch (Exception e) {
					throw new CommRuntimeException("生日格式错误,正确格式:yyyy-MM-dd");
				}
			}
			//如果科室为空，那么指定一个默认科室
			if(StringUtils.isEmpty(user.getDeptCode())){
				user.setDeptCode(MnisQmConstants.defaultDept);
			}
			//新增
			user.setStatus(MnisQmConstants.STATUS_YX);//状态
			user.setUserCode(CodeUtils.getUserCode());//员工编号
			comUserMapper.insert(user);
			//如果部门编号不为空，那么把该员工加入该部门
			if(!StringUtils.isEmpty(user.getDeptCode())){
				ComDeptUser deptUser = new ComDeptUser();
				deptUser.setDeptCode(user.getDeptCode());//部门编号
				deptUser.setUserCode(user.getUserCode());//员工编号
				deptUser.setCreatePerson(user.getCreatePerson());//创建人
				deptUser.setUpdatePerson(user.getUpdatePerson());//更新人
				deptUser.setStatus(MnisQmConstants.STATUS_YX);//有效状态
				comDeptUserMapper.insert(deptUser);
			}
			
		}else{
			//修改
			user.setStatus(MnisQmConstants.STATUS_YX);//状态
			comUserMapper.updateByPrimaryKey(user);
		}
	}
	
	/**
	 * 职务变更
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComUserPosition(ComUserPosition positions){
		if(null == positions){
			return;
		}
		if(null == positions.getSeqId()){
			//新增
			positions.setStatus(MnisQmConstants.STATUS_YX);//状态
			comUserPositionMapper.insert(positions);
			//更新继续信息表
			ComUser user = new ComUser();
			user.setUserCode(positions.getUserCode());//员工编号
			comUserMapper.updateDuty(user);
		}else{
			positions.setStatus(MnisQmConstants.STATUS_YX);//状态
			comUserPositionMapper.updateByPrimaryKey(positions);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComUserEducation(List<ComUserEducation> edus){
		if(null == edus){
			return;
		}
		for(ComUserEducation edu : edus){
			if(null == edu.getSeqId()){
				//新增
				edu.setStatus(MnisQmConstants.STATUS_YX);//状态
				comUserEducationMaper.insert(edu);
			}else{
				edu.setStatus(MnisQmConstants.STATUS_YX);//状态
				comUserEducationMaper.updateByPrimaryKey(edu);
			}
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComUserFamily(List<ComUserFamily> familys){
		if(null == familys){
			return;
		}
		for(ComUserFamily family : familys){
			if(null == family.getSeqId()){
				//新增
				family.setStatus(MnisQmConstants.STATUS_YX);//状态
				comUserFamilyMapper.insert(family);
			}else{
				family.setStatus(MnisQmConstants.STATUS_YX);//状态
				comUserFamilyMapper.updateByPrimaryKey(family);
			}
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComUserLearning(List<ComUserLearning> leas){
		if(null == leas){
			return;
		}
		for(ComUserLearning lea : leas){
			if(null == lea.getSeqId()){
				//新增
				lea.setStatus(MnisQmConstants.STATUS_YX);//状态
				comUserLearningMapper.insert(lea);
			}else{
				lea.setStatus(MnisQmConstants.STATUS_YX);//状态
				comUserLearningMapper.updateByPrimaryKey(lea);
			}
		}
	}
	
	/**
	 * 职称变更
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComUserNursing(ComUserNursing nurs){
		if(null == nurs){
			return;
		}
		if(null == nurs.getSeqId()){
			//新增
			nurs.setStatus(MnisQmConstants.STATUS_YX);//状态
			comUserNursingMapper.insert(nurs);
			//更新职称
			ComUser user = new ComUser();
			user.setUserCode(nurs.getUserCode());//员工编号
			comUserMapper.updateTechnicalPost(user);
		}else{
			nurs.setStatus(MnisQmConstants.STATUS_YX);//状态
			comUserNursingMapper.updateByPrimaryKey(nurs);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComUserTraining(List<ComUserTraining> tras){
		if(null == tras){
			return;
		}
		for(ComUserTraining tra : tras){
			if(null == tra.getSeqId()){
				//新增
				tra.setStatus(MnisQmConstants.STATUS_YX);//状态
				comUserTrainingMapper.insert(tra);
			}else{
				tra.setStatus(MnisQmConstants.STATUS_YX);//状态
				comUserTrainingMapper.updateByPrimaryKey(tra);
			}
		}
	}

	@Override
	public List<String> queryAllDeptCode() {
		return comDeptMapper.selectAllDeptCode();
	}

	@Override   
	public TotalInfo getMainInfo() {
		return totalInfoMapper.selectAll().get(0);
//		return totalInfoMapper.selectMainInfo();
	}

	@Override
	public List<BusyDeptInfo> selectAllBusyDeptInfo() {
		return busyDeptInfoMapper.selectAll();
	}

	@Override
	public List<DeptHumanResourceInfo> queryDeptHumanResourceInfo() {
		return deptHumanResourceInfoMapper.selectAll();
//		return deptHumanResourceInfoMapper.queryDeptHumanResourceInfo();
	}

	@Override
	public List<DeptSchedulInfo> queryDeptSchedulInfo() {
//		Map<String, Object> conditionMap = new HashMap<String, Object>();
//		List<DeptSchedulInfo> list = deptSchedulInfoMapper.queryDeptSchedulInfo(conditionMap);
//		return list;
		return deptSchedulInfoMapper.selectAll();
	}

	@Override
	public List<DeptBedInfo> selectAllDeptBedInfo(String deptCode) {
//		List<DeptBedInfo> list = deptBedInfoMapper.queryDeptManageBedInfo(deptCode);
//		for (DeptBedInfo deptBedInfo : list) {
//			List<String> nameList = deptBedInfoMapper.queryDutyNames(deptCode);
//			for (String temp : nameList) {
//				deptBedInfo.setDutyNurse(deptBedInfo.getDutyNurse() + temp + " ");
//			}
//		}
//		return list;
		return deptBedInfoMapper.selectAll();
	}

	@Override
	public void saveComUserDept(ComDeptUser comDeptUser) {
		ComDeptUser c = comDeptUserMapper.selectByUserCode(comDeptUser.getUserCode());
		if(c==null){
			comDeptUser.setCreateTime(new Date());
			comDeptUser.setUpdateTime(new Date());
			comDeptUserMapper.insert(comDeptUser);
		}else{
			comDeptUser.setSeqId(c.getSeqId());
			comDeptUser.setUpdateTime(new Date());
			ComUserChange comUserChange = new ComUserChange();
			comUserChange.setUserCode(comDeptUser.getUserCode());
			comUserChange.setChangeType(MnisQmConstants.DeptChange);//设置类型为科室变更
			comUserChange.setForeName(c.getDeptCode());//变更前科室
			comUserChange.setRearName(comDeptUser.getDeptCode());//变更后科室
			SimpleDateFormat myformat = new SimpleDateFormat("YYYY-MM-dd");
			comUserChange.setChangeTime(myformat.format(new Date()));//更改时间
			comUserChange.setStatus(MnisQmConstants.STATUS_YX);
			comUserChange.setCreatePerson(comDeptUser.getUpdatePerson());
			comUserChange.setUpdatePerson(comDeptUser.getUpdatePerson());
			saveComUserChange(comUserChange);//科室变更信息保存到人事变更表
			comDeptUserMapper.updateByPrimaryUser(comDeptUser);
		}
	}

	@Override
	public void deleteComUserDept(ComDeptUser comDeptUser) {
		ComDeptUser deptUser = comDeptUserMapper.selectByUserCode(comDeptUser.getUserCode());
		deptUser.setStatus(MnisQmConstants.STATUS_WX);
		deptUser.setUpdatePerson(comDeptUser.getUpdatePerson());
		deptUser.setUpdateTime(new Date());
		//comDeptUserMapper.updateByPrimaryKey(deptUser);
	}

	@Override
	public int saveComDept(ComDept comDept) {
		if(comDept.getSeqId()==null){
			comDept.setCreateTime(new Date());
			comDept.setUpdateTime(new Date());
			return comDeptMapper.insert(comDept);
		}else{
			comDept.setUpdateTime(new Date());
			return comDeptMapper.updateByPrimaryKey(comDept);
		}
	}

	@Override
	public void deleteComDeptByPrimaryKey(ComDept comDept) {
		ComDept dept = comDeptMapper.selectByPrimaryKey((comDept.getSeqId()));
		dept.setStatus(MnisQmConstants.STATUS_WX);
		dept.setUpdatePerson(comDept.getUpdatePerson());
		dept.setUpdateTime(new Date());
		comDeptMapper.updateByPrimaryKey(dept);
		
	}

	@Override
	public List<DeptAllocateApply> selectAllDeptAllocateApply() {
		return deptAllocateApplyMapper.selectAll();
	}

	@Override
	public List<Allocate> queryAllAllocate() {
		return allocateMapper.selectAll();
	}

	@Override
	public List<SeniorSearch> queryAllSeniorSearch(String dateTime, String deptCode, String hierarchy,
			String specialQualifications, String collegeExperience, String nightShiftFlag) {
		/*Map<String,String> conditionMap = new HashMap<String,String>();
		conditionMap.put("dateTime", dateTime);
		conditionMap.put("deptCode", deptCode);
		conditionMap.put("hierarchy", hierarchy);
		conditionMap.put("specialQualifications", specialQualifications);
		conditionMap.put("collegeExperience", collegeExperience);
		conditionMap.put("nightShiftFlag", nightShiftFlag);
		seniorSearchMapper.queryAllSeniorSearch(conditionMap);*/
		return seniorSearchMapper.selectAll();
	}

	@Override
	public List<ComUserChange> selectAllComUserChange(String userCode,String changeType) {
		ComUserChange change = new ComUserChange();
		change.setUserCode(userCode);
		change.setChangeType(changeType);
		return comUserChangeMapper.selectAll(change);
	}

	@Override
	@Transactional
	public void saveComUserChange(ComUserChange comUserChange) {
		if(comUserChange.getSeqId()==null){
			comUserChange.setStatus(MnisQmConstants.STATUS_YX);
			comUserChangeMapper.insert(comUserChange);
			//更新当前的临床层级，和带教级别
			if(MnisQmConstants.Clinical.equals(comUserChange.getChangeType())){
				ComUser user = new ComUser();
				user.setUserCode(comUserChange.getUserCode());
				user.setTeachLevel(comUserChange.getRearName());
				comUserMapper.updateTeachLevel(user);
			}else if(MnisQmConstants.TeachLevel.equals(comUserChange.getChangeType())){
				ComUser user = new ComUser();
				user.setUserCode(comUserChange.getUserCode());
				user.setTeachLevel(comUserChange.getRearName());
				comUserMapper.updateTeachLevel(user);
			}else if(MnisQmConstants.DeptChange.equals(comUserChange.getChangeType())){}
		}else{
			comUserChangeMapper.update(comUserChange);
		}
		
	}

	@Override
	@Transactional
	public void updateUserType(ComUser user) {
		comUserMapper.updateUserType(user.getUserType(), user.getUserCode());
		//离职改变科室
		if(MnisQmConstants.userType_04.equals(user.getUserType())){
			comDeptUserMapper.updateForLeave(user);
		}
	}

	@Override
	public List<ComUser> selectUserListByNameCodeDept(String userCode,
			String userName, String deptCode, String gender, String userType) {
		return comUserMapperExt.selectUserByCodeNameDept(userCode, userName, deptCode, gender, userType);
	}
	
	/**
	 * 查询离职员工信息
	 * @param deptCode
	 * @return
	 */
	@Override
	public List<ComUser> selectDimissionUserList(String deptCode){
		ComDept dept = new ComDept();
		dept.setFatherDept(deptCode);
		dept.setDeptCode(MnisQmConstants.lvealDept);
		ComDept leaveDept = comDeptMapper.selectLeaveDept(dept);
		List<ComUser> rsUsers= null;
		if(leaveDept != null){
			rsUsers = comUserMapperExt.selectUserByCodeNameDept(null,null,leaveDept.getDeptCode(),null,null);
		}
		return rsUsers;
	}
	
	@Override
	public List<JuniorAskForLeave> queryAllJuniorLeaveInfo(){
		return juniorAskForLeaveMapper.selectAll();
	}
	
	@Override
	public ComUserStatistics queryUserStatistics(String deptCode){
		ComDept dept = new ComDept();
		dept.setDeptCode(deptCode);
		ComUserStatistics statistics = new ComUserStatistics();
		//查询职务
		List<Statistics> duty = comDeptMapper.selectPositionCount(dept);
		statistics.setDuty(duty);
		//查询职称
		List<Statistics> technicalPost = comDeptMapper.selectTechnicalPostCount(dept);
		statistics.setTechnicalPost(technicalPost);
		//查询临床层级
		List<Statistics> clinical = comDeptMapper.selectClinicalCount(dept);
		statistics.setClinical(clinical);
		//查询临床层级
		List<Statistics> teachLevel = comDeptMapper.selectTeachingCount(dept);
		statistics.setTeachLevel(teachLevel);
		//查询学历统计
		List<Statistics> education = comDeptMapper.selectEducation(dept);
		statistics.setEducation(education);
		//查询性别统计
		List<Statistics> gender = comDeptMapper.selectGender(dept);
		statistics.setGender(gender);
		//查询性别统计
		List<Statistics> userType = comDeptMapper.selectUserType(dept);
		statistics.setUserType(userType);
		return statistics;
	}

	@Override
	@Transactional
	public void saveDeptNurseHeader(ComDept comDept) {
		comDeptMapper.saveDeptNurseHeader(comDept);
	}

}
