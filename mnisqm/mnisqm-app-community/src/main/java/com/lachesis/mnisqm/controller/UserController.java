package com.lachesis.mnisqm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.BaseMapVo;
import com.lachesis.mnisqm.common.WebContextUtils;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.DateUtils;
import com.lachesis.mnisqm.core.utils.GsonUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.user.domain.ComDept;
import com.lachesis.mnisqm.module.user.domain.ComDeptUser;
import com.lachesis.mnisqm.module.user.domain.ComUser;
import com.lachesis.mnisqm.module.user.domain.ComUserChange;
import com.lachesis.mnisqm.module.user.domain.ComUserEducation;
import com.lachesis.mnisqm.module.user.domain.ComUserFamily;
import com.lachesis.mnisqm.module.user.domain.ComUserLearning;
import com.lachesis.mnisqm.module.user.domain.ComUserNursing;
import com.lachesis.mnisqm.module.user.domain.ComUserPosition;
import com.lachesis.mnisqm.module.user.domain.ComUserTraining;
import com.lachesis.mnisqm.module.user.service.IUserService;
import com.lachesis.mnisqm.vo.user.ComUserInfoInVo;

/**
 * 用户模块
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	private static final String comUser = "comUser";// 基本信息
	private static final String comUserEducation = "comUserEducation";// 学历信息
	private static final String comUserFamily = "comUserFamily";// 家庭信息
	private static final String comUserLearning = "comUserLearning";// 教育信息
	private static final String comUserTraining = "comUserTraining";// 员工培训信息
	private static final String comUserPosition = "comUserPosition";// 职位信息
	private static final String comUserNursing = "comUserNursing";// 护理信息

	@Autowired
	public IUserService service;

	/**
	 * 根据用户编号,姓名,部门编号获取用户的详细信息，包括职称信息和教育信息等
	 * 
	 * @param userCode
	 * @return
	 */
	@RequestMapping(value = "/getUserDetailInfo")
	@ResponseBody
	public BaseDataVo getUserDetailInfo(String userCode, String hisCode) {
		BaseDataVo outVo = new BaseDataVo();
		if (StringUtils.isEmpty(userCode) && StringUtils.isEmpty(hisCode)) {
			throw new CommRuntimeException("员工编号为空！");
		} else {
			// 获取员工详细信息
			outVo.setData(service.getUserDetailInfo(userCode, hisCode));
		}

		return outVo;
	}

	/**
	 * 获取员工列表,通过部门编号
	 * 
	 * @param depotCode
	 *            :部门编号
	 * @return
	 */
	@RequestMapping(value = "/getUserList")
	public @ResponseBody
	BaseMapVo getUserList(String userCode, String userName, String deptCode, String userType, String gender) {
		//System.out.println(GsonUtils.toJson(WebContextUtils.getSessionUserInfo()));
		BaseMapVo outVo = new BaseMapVo();
		try {
			List<ComUser> users = service.selectUserListByNameCodeDept(
					userCode, userName, deptCode, gender, userType);
			// 数据返回
			outVo.addData("lst", users);
			outVo.setCode(Constants.Success);
		} catch (CommRuntimeException e) {
			outVo.setCode(Constants.FailureCode);
			outVo.setMsg(e.getMessage());
		} catch (Exception e) {
			outVo.setCode(Constants.FailureCode);
			outVo.setMsg(e.getMessage());
		}
		System.out.println(GsonUtils.toJson(outVo));
		return outVo;
	}
	
	/**
	 * 获取员工列表,通过部门编号
	 * @param depotCode:部门编号
	 * @return
	 */
	@RequestMapping(value = "/getDimissionUserList")
	public @ResponseBody
	BaseMapVo getDimissionUserList(String deptCode) {
		BaseMapVo outVo = new BaseMapVo();
		try {
			List<ComUser> users = service.selectDimissionUserList(deptCode);
			// 数据返回
			outVo.addData("lst", users);
			outVo.setCode(Constants.Success);
		} catch (CommRuntimeException e) {
			outVo.setCode(Constants.FailureCode);
			outVo.setMsg(e.getMessage());
		} catch (Exception e) {
			outVo.setCode(Constants.FailureCode);
			outVo.setMsg(e.getMessage());
		}
		System.out.println(GsonUtils.toJson(outVo));
		return outVo;
	}
	
	/**
	 * 获取系统所有部门列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getAllDepts")
	public @ResponseBody
	BaseDataVo getAllDepts() {
		BaseDataVo outVo = new BaseDataVo();
		try {
			outVo.setData(service.getAllDepts());
			outVo.setCode(Constants.Success);
		} catch (CommRuntimeException e) {
			outVo.setCode(Constants.FailureCode);
			outVo.setMsg(e.getMessage());
			// e.printStackTrace();
		} catch (Exception e) {
			outVo.setCode(Constants.FailureCode);
			outVo.setMsg(e.getMessage());
			// e.printStackTrace();
		}

		return outVo;
	}

	/**
	 * 获取用户所属部门的所有下属部门
	 * 
	 * @param deptCode
	 * @return
	 */
	@RequestMapping(value = "/getUserDepts")
	public @ResponseBody
	BaseDataVo getUserDepts(String deptCode,String getSysDept) {
		BaseDataVo outVo = new BaseDataVo();

		if (StringUtils.isEmpty(deptCode)) {
			throw new RuntimeException("当前部门编号为空！");
		}
		// 数据查询
		outVo.setData(service.getUserDepts(deptCode,getSysDept));

		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 数据删除
	 * 
	 * @param jsonStr
	 * @return
	 */
	@RequestMapping(value = "/deleteUserInfo")
	public @ResponseBody
	BaseDataVo deleteUserInfo(@RequestBody ComUserInfoInVo invo) {
		BaseDataVo outVo = new BaseDataVo();
		if (invo == null) {
			throw new CommRuntimeException("数据为空!");
		}
		if (invo.getDelSeqId() == null) {
			throw new CommRuntimeException("删除数据的ID不允许为空！");
		}
		if (StringUtils.isEmpty(invo.getDataType())) {
			throw new CommRuntimeException("删除数据的数据类型为空!");
		}
		String dataType = invo.getDataType();
		SysUser sessionUser = WebContextUtils.getSessionUserInfo();
		switch (dataType) {
		case comUser:// 员工基本信息
			ComUser user = new ComUser();
			user.setSeqId(invo.getDelSeqId());
			user.setUpdatePerson(sessionUser.getUserCode());
			user.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
			service.deleteUserInfo(user);
			break;
		case comUserEducation:
			ComUserEducation edu = new ComUserEducation();
			edu.setSeqId(invo.getDelSeqId());
			edu.setUpdatePerson(sessionUser.getUserCode());
			edu.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
			service.deleteUserInfo(edu);
			break;
		case comUserFamily:
			// 家庭信息校验
			ComUserFamily family = new ComUserFamily();
			family.setSeqId(invo.getDelSeqId());
			family.setUpdatePerson(sessionUser.getUserCode());
			family.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
			service.deleteUserInfo(family);
			break;
		case comUserLearning:
			// 信息校验
			ComUserLearning lea = new ComUserLearning();
			lea.setSeqId(invo.getDelSeqId());
			lea.setUpdatePerson(sessionUser.getUserCode());
			lea.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
			service.deleteUserInfo(lea);
			break;
		case comUserNursing:
			// 信息校验
			ComUserNursing nur = new ComUserNursing();
			nur.setSeqId(invo.getDelSeqId());
			nur.setUpdatePerson(sessionUser.getUserCode());
			nur.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
			service.deleteUserInfo(nur);
			break;
		case comUserPosition:
			// 信息校验
			ComUserPosition po = new ComUserPosition();
			po.setSeqId(invo.getDelSeqId());
			po.setUpdatePerson(sessionUser.getUserCode());
			po.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
			service.deleteUserInfo(po);
			break;
		case comUserTraining:
			// 信息校验
			ComUserTraining tra = new ComUserTraining();
			tra.setSeqId(invo.getDelSeqId());
			tra.setUpdatePerson(sessionUser.getUserCode());
			tra.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
			service.deleteUserInfo(tra);
			break;
		}

		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	@RequestMapping("/saveComUser")
	public @ResponseBody
	BaseDataVo saveComUser(@RequestBody ComUserInfoInVo invo) {
		BaseDataVo outVo = new BaseDataVo();
		System.out.println(GsonUtils.toJson(invo));
		String dataType = invo.getDataType();
		SysUser sessionUser = WebContextUtils.getSessionUserInfo();
		switch (dataType) {
		case comUser:// 员工基本信息
			ComUser user = invo.getComUser();
			user.setCreatePerson(sessionUser.getUserCode());
			user.setUpdatePerson(sessionUser.getUserCode());
			if(StringUtils.isEmpty(user.getUserType())){
				throw new CommRuntimeException("员工类型不允许为空!");
			}
			service.saveComUser(user);
			break;
		case comUserEducation:
			List<ComUserEducation> edus = invo.getComUserEducation();
			if (null == edus) {
				throw new CommRuntimeException("学历信息为空!");
			}
			for (ComUserEducation edu : edus) {
				if (StringUtils.isEmpty(edu.getUserCode())) {
					throw new CommRuntimeException("员工编号不允许为空！");
				}
				if (StringUtils.isEmpty(edu.getEducationType())) {
					throw new CommRuntimeException("代课类型不允许为空！");
				}
				if (StringUtils.isEmpty(edu.getCourseTopics())) {
					throw new CommRuntimeException("课程专题允许为空！");
				}
				edu.setCreatePerson(sessionUser.getUserCode());
				edu.setUpdatePerson(sessionUser.getUserCode());
			}
			service.saveComUserEducation(edus);
			break;
		case comUserFamily:
			// 家庭信息校验
			List<ComUserFamily> familys = invo.getComUserFamily();
			if (null == familys) {
				throw new CommRuntimeException("家庭信息为空!");
			}
			for (ComUserFamily family : familys) {
				if (StringUtils.isEmpty(family.getUserCode())) {
					throw new CommRuntimeException("员工编号不允许为空！");
				}
				family.setCreatePerson(sessionUser.getUserCode());// 创建人
				family.setUpdatePerson(sessionUser.getUserCode());// 更新人
			}
			service.saveComUserFamily(familys);
			break;
		case comUserLearning:
			// 信息校验
			List<ComUserLearning> leas = invo.getComUserLearning();
			if (null == leas) {
				throw new CommRuntimeException("学历信息为空!");
			}
			for (ComUserLearning lea : leas) {
				if (StringUtils.isEmpty(lea.getUserCode())) {
					throw new CommRuntimeException("员工编号不允许为空！");
				}
				lea.setCreatePerson(sessionUser.getUserCode());// 创建人
				lea.setUpdatePerson(sessionUser.getUserCode());// 更新人
			}
			service.saveComUserLearning(leas);
			break;
		case comUserNursing:
			// 职称
			ComUserNursing nur = invo.getComUserNursing();
			if (null == nur) {
				throw new CommRuntimeException("护理信息为空!");
			}
			if (StringUtils.isEmpty(nur.getUserCode())) {
				throw new CommRuntimeException("员工编号不允许为空！");
			}
			nur.setCreatePerson(sessionUser.getUserCode());// 创建人
			nur.setUpdatePerson(sessionUser.getUserCode());// 更新人
			service.saveComUserNursing(nur);
			break;
		case comUserPosition:
			// 职务
			ComUserPosition po = invo.getComUserPosition();
			if (null == po) {
				throw new CommRuntimeException("职位信息为空!");
			}
			if (StringUtils.isEmpty(po.getUserCode())) {
				throw new CommRuntimeException("员工编号不允许为空！");
			}
			po.setCreatePerson(sessionUser.getUserCode());// 创建人
			po.setUpdatePerson(sessionUser.getUserCode());// 更新人
			service.saveComUserPosition(po);
			break;
		case comUserTraining:
			// 信息校验
			List<ComUserTraining> tras = invo.getComUserTraining();
			if (null == tras) {
				throw new CommRuntimeException("继续教育信息为空!");
			}
			for (ComUserTraining tra : tras) {
				if (StringUtils.isEmpty(tra.getUserCode())) {
					throw new CommRuntimeException("员工编号不允许为空！");
				}
				tra.setCreatePerson(sessionUser.getUserCode());// 创建人
				tra.setUpdatePerson(sessionUser.getUserCode());// 更新人
			}
			service.saveComUserTraining(tras);
			break;
		}
		// 缓存清理
		invo = null;

		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取人力资源主界面的统计信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryTotalInfo")
	public @ResponseBody
	BaseDataVo queryTotalInfo() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.getMainInfo());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取繁忙病区信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryBusyDeptInfo")
	public @ResponseBody
	BaseDataVo queryBusyDeptInfo() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.selectAllBusyDeptInfo());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取科室人力资源分布信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryDeptHumanResourceInfo")
	public @ResponseBody
	BaseDataVo queryDeptHumanResourceInfo() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.queryDeptHumanResourceInfo());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取科室排班情况信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryDeptSchedulInfo")
	public @ResponseBody
	BaseDataVo queryDeptSchedulInfo() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.queryDeptSchedulInfo());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取科室管床情况信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryDeptBedInfo")
	public @ResponseBody
	BaseDataVo queryDeptBedInfo(String deptCode) {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.selectAllDeptBedInfo(deptCode));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取科室调配申请信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryAllDeptAllocateApply")
	public @ResponseBody
	BaseDataVo queryAllDeptAllocateApply() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.selectAllDeptAllocateApply());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取已调配信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryAllAllocate")
	public @ResponseBody
	BaseDataVo queryAllAllocate() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.queryAllAllocate());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取调配所有结果
	 * @param dateTime 当前时间
	 * @param deptCode 部门code
	 * @param hierarchy 层级
	 * @param specialQualifications 特殊资质
	 * @param collegeExperience 专科经历
	 * @param nightShiftFlag 夜班准入
	 * @return
	 */
	@RequestMapping(value = "/queryAllSeniorSearch")
	public @ResponseBody
	BaseDataVo queryAllSeniorSearch(String dateTime, String deptCode, String hierarchy,
			String specialQualifications, String collegeExperience, String nightShiftFlag) {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.queryAllSeniorSearch(dateTime, deptCode, hierarchy, specialQualifications, collegeExperience, nightShiftFlag));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 保存部门信息
	 * 
	 * @param comDept
	 * @return
	 */
	@RequestMapping("/saveDept")
	public @ResponseBody
	BaseDataVo saveDept(@RequestBody ComDept comDept) {
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		comDept.setCreatePerson(user.getUserCode());
		comDept.setUpdatePerson(user.getUserCode());
		comDept.setStatus(MnisQmConstants.STATUS_YX);
		service.saveComDept(comDept);
		vo.setCode(Constants.Success);
		return vo;

	}

	/**
	 * 根据Id删除部门
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping("/deleteDept")
	public @ResponseBody
	BaseDataVo deleteDeptByPrimaryKey(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		ComDept comDept = new ComDept();
		SysUser user = WebContextUtils.getSessionUserInfo();
		comDept.setUpdatePerson(user.getUserCode());
		comDept.setSeqId(seqId);
		service.deleteComDeptByPrimaryKey(comDept);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存用户部门信息
	 * 
	 * @param comDeptUser
	 * @return
	 */
	@RequestMapping("/saveUserDept")
	public @ResponseBody
	BaseDataVo saveDeptUser(@RequestBody ComDeptUser comDeptUser) {
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		comDeptUser.setCreatePerson(user.getUserCode());
		comDeptUser.setUpdatePerson(user.getUserCode());
		comDeptUser.setStatus(MnisQmConstants.STATUS_YX);
		service.saveComUserDept(comDeptUser);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 根据id删除用户部门表信息
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping("/deleteUserDept")
	public @ResponseBody
	BaseDataVo deleteDeptUser(String userCode) {
		BaseDataVo vo = new BaseDataVo();
		ComDeptUser comDeptUser = new ComDeptUser();
		SysUser user = WebContextUtils.getSessionUserInfo();
		comDeptUser.setUpdatePerson(user.getUserCode());
		comDeptUser.setUserCode(userCode);
		service.deleteComUserDept(comDeptUser);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获得所有人事信息变更
	 * 
	 * @return
	 */
	@RequestMapping("/getAllComUserChange")
	public @ResponseBody
	BaseDataVo getAllComUserChange(String userCode,String changeType) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.selectAllComUserChange(userCode,changeType));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存人事信息变更
	 * 
	 * @param comUserChange
	 * @return
	 */
	@RequestMapping("/saveComUserChange")
	public @ResponseBody
	BaseDataVo saveComUserChange(@RequestBody ComUserChange comUserChange) {
		BaseDataVo vo = new BaseDataVo();
		if (comUserChange.getUserCode() == null) {
			throw new CommRuntimeException("用户不能为空！");
		}
		if (comUserChange.getChangeType() == null) {
			throw new CommRuntimeException("变更内型不能为空！");
		}
		if (comUserChange.getForeName() == null) {
			throw new CommRuntimeException("变更前内容不能为空！");
		}
		if (comUserChange.getRearName() == null) {
			throw new CommRuntimeException("变更后内容不能为空！");
		}
		if (comUserChange.getChangeTime() == null) {
			throw new CommRuntimeException("变更时间不能为空！");
		}else{
			try{
				String date = DateUtils.format(DateUtils.parseDate(comUserChange.getChangeTime(), DateUtils.YMD), DateUtils.YMD);
				comUserChange.setChangeTime(date);
			}catch (Exception e) {
				throw new CommRuntimeException("变更日期格式不正确!");
			}
		}
		SysUser user = WebContextUtils.getSessionUserInfo();
		comUserChange.setCreatePerson(user.getUserCode());
		comUserChange.setUpdatePerson(user.getUserCode());
		service.saveComUserChange(comUserChange);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 离职（改变用户类型）
	 * 
	 * @param userType
	 * @param userCode
	 * @return
	 */
	@RequestMapping("/changeUserType")
	public @ResponseBody
	BaseDataVo changeUserType(@RequestBody ComUser user) {
		BaseDataVo vo = new BaseDataVo();
		service.updateUserType(user);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获取所有专科人员请假信息
	 * @return
	 */
	@RequestMapping("/queryAllJuniorLeaveInfo")
	public @ResponseBody
	BaseDataVo queryAllJuniorLeaveInfo() {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.queryAllJuniorLeaveInfo());
		vo.setCode(Constants.Success);
		return vo;
	}
	
	/**
	 * 获取科室下员工统计数据
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getUserStatistics")
	public @ResponseBody BaseDataVo getUserStatistics(String deptCode){
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.queryUserStatistics(deptCode));
		vo.setCode(Constants.Success);
		return vo;
	}
	
	/**
	 * 保存科护长
	 * @param deptCode
	 * @param deptNurseHeader
	 * @return
	 */
	@RequestMapping("/saveDeptNurseHeader")
	public @ResponseBody BaseDataVo saveDeptNurseHeader(@RequestBody ComDept comDept){
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		comDept.setUpdatePerson(user.getUserCode());
		service.saveDeptNurseHeader(comDept);
		vo.setCode(Constants.Success);
		return vo;
	}
}