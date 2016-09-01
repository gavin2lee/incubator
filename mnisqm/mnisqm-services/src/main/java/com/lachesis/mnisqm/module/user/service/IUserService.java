package com.lachesis.mnisqm.module.user.service;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.system.domain.SysUser;
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
import com.lachesis.mnisqm.module.user.domain.TotalInfo;


public interface IUserService {
		
	/**
	 * 获取用户列表
	 * @return
	 */
	public List<ComUser> getUserList(String depotCode) ;

	/**
	 * 根据登录名获取用户信息
	 * @param loginName
	 * @return
	 */
	public SysUser getUserByLoginName(String loginName);

	/**
	 * 获取系统内所用部门信息
	 * @return
	 */
	public List<ComDept> getAllDepts();
	
	/**
	 * 获取所有部门code
	 * @return
	 */
	public List<String> queryAllDeptCode();

	/**
	 * 获取用户下属的所有部门信息
	 * @param userCode
	 * @param deptCode
	 * @return
	 */
	public Map<String, Object> getUserDepts(String deptCode,String sysData);

	/**
	 * 获取用户的详细信息，包括职位信息和教育信息等
	 * @param userCode
	 * @return
	 */
	public Map<String, Object> getUserDetailInfo(String userCode,String hisCode);

	/**
	 * 保存员工信息
	 * @param userInfo 员工详细信息
	 * @param infoType 信息类型，例如职位信息，教育背景等
	 * @return 成功返回1，失败返回0
	 */
	public int saveUserInfo(Object userInfo, String infoType);
	
	/**
	 * 数据新增
	 * @param obj  新增数据
	 * @return
	 */
	public int inserObj(Object obj);
	
	/**
	 * 用户数据删除
	 * @param obj
	 * @return
	 */
	public void deleteUserInfo(Object obj);
	
	/**
	 * 保存用户基本信息
	 * @param user
	 */
	public void saveComUser(ComUser user);
	
	/**
	 * 保存职位信息
	 * @param positions
	 */
	public void saveComUserPosition(ComUserPosition positions);
	
	/**
	 * 保存学历信息
	 * @param edus
	 */
	public void saveComUserEducation(List<ComUserEducation> edus);
	
	/**
	 * 保存家庭信息
	 * @param familys
	 */
	public void saveComUserFamily(List<ComUserFamily> familys);
	
	/**
	 * 保存教育信息
	 * @param leas
	 */
	public void saveComUserLearning(List<ComUserLearning> leas);
	
	/**
	 * 保存护理信息
	 * @param nurs
	 */
	public void saveComUserNursing(ComUserNursing nurs);
	
	/**
	 * 保存培训信息
	 * @param tras
	 */
	public void saveComUserTraining(List<ComUserTraining> tras);
	
	/**
	 * 获取人力资源主界面的统计信息
	 * @return
	 */
	TotalInfo getMainInfo();
	
	/**
	 * 获取繁忙病区信息
	 * @return
	 */
	List<BusyDeptInfo> selectAllBusyDeptInfo();
	
	/**
	 * 获取科室人力资源分布信息
	 * @return
	 */
	List<DeptHumanResourceInfo> queryDeptHumanResourceInfo();
	
	/**
	 * 获取科室排班情况信息
	 * @return
	 */
	List<DeptSchedulInfo> queryDeptSchedulInfo();
	
	/**
	 * 获取科室管床情况信息
	 * @return
	 */
	List<DeptBedInfo> selectAllDeptBedInfo(String deptCode);
	
	/**
	 * 保存用户-部门信息
	 * @param comDeptUser
	 * @return
	 */
	public void saveComUserDept(ComDeptUser comDeptUser);

	/**
	 * 删除用户-部门信息
	 * @param comDeptUser
	 */
	public void deleteComUserDept(ComDeptUser comDeptUser);

	/**
	 * 保存部门信息
	 * 
	 * @param comDept
	 * @return 成功返回1，失败返回0
	 */
	public int saveComDept(ComDept comDept);

	/**
	 * 根据主键删除部门信息
	 * 
	 * @param seqId
	 */
	public void deleteComDeptByPrimaryKey(ComDept comDept);
	
	/**
	 * 获取科室调配申请信息
	 * @return
	 */
	List<DeptAllocateApply> selectAllDeptAllocateApply();
	
	/**
	 * 获取已调配信息
	 * @return
	 */
	List<Allocate> queryAllAllocate();
	
	/**
	 * 获取调配所有结果
	 * @return
	 */
	List<SeniorSearch> queryAllSeniorSearch(String dateTime, String deptCode, String hierarchy,
			String specialQualifications, String collegeExperience, String nightShiftFlag);
	
	/**
	 * 获取所有人事变更
	 * @return
	 */
	List<ComUserChange> selectAllComUserChange(String userCode,String changeType);
	
	/**
	 * 保存人事变更
	 * @param comUserChange
	 */
	public void saveComUserChange(ComUserChange comUserChange);
	
	/**
	 * 离职-〉改变userType
	 * @param userType
	 * @param userCode
	 */
	public void updateUserType(ComUser user);
	
	/**
	 * 根据员工编号，姓名，部门编号查询用户信息
	 * @param userCode
	 * @param userName
	 * @param deptCode
	 * @return
	 */
	public List<ComUser> selectUserListByNameCodeDept(String userCode, String userName, String deptCode, String gender, String userType);
	
	/**
	 * 获取所有专科人员请假信息
	 * @return
	 */
	List<JuniorAskForLeave> queryAllJuniorLeaveInfo();
	
	/**
	 * 查询离职员工信息
	 * @param deptCode
	 * @return
	 */
	public List<ComUser> selectDimissionUserList(String deptCode);
	
	/**
	 * 获取部门的统计信息
	 * @param deptCode
	 * @return
	 */
	public ComUserStatistics queryUserStatistics(String deptCode);
	
	/**
	 * 修改科护长
	 * @param deptCode
	 * @param deptNurseHeader
	 */
	public void saveDeptNurseHeader(ComDept comDept);
	
}
