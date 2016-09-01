package com.lachesis.mnis.core.identity.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.lachesis.mnis.core.bodysign.entity.BodySignConfig;
import com.lachesis.mnis.core.identity.entity.ConfigBean;
import com.lachesis.mnis.core.identity.entity.Dict;
import com.lachesis.mnis.core.identity.entity.LoginCardInfo;
import com.lachesis.mnis.core.identity.entity.LoginCardManager;
import com.lachesis.mnis.core.identity.entity.SysMenu;
import com.lachesis.mnis.core.identity.entity.SysUser;
import com.lachesis.mnis.core.identity.entity.UserFinger;
import com.lachesis.mnis.core.identity.entity.SysDept;
import com.lachesis.mnis.core.identity.entity.UserInformation;

/***
 * 
 * 用户相关的数据访问层
 *
 * @author yuliang.xu
 * @date 2015年6月9日 上午10:58:06 
 *
 */
public interface IdentityRepository {

	/**
	 * 根据员工登录账号/员工编号查询员工信息
	 * 
	 * @param emplCode
	 * @return
	 */
	UserInformation getUserByCode(String code);
	/**
	 * 根据登录账号获取信息
	 * @param loginName
	 * @return
	 */
	UserInformation getUserByLoginName(String loginName,String deptCode);

    /**
     * 获取所有科室
     * @return
     */
    List<SysDept> getDeptments();
    
	List<SysDept> getDeptmentsByCode(String nurseCode,String deptCode);
    
    /**
     * 获取诊断列表
     * @return
     */
    List<Dict> getDiagList();
    
    /**
     * 获取药物列表
     * @return
     */
    List<Dict> getDrugList();
    
	/***
	 * 查询系统配置
	 * @return
	 */
	List<ConfigBean> getSystemConfig();
	
	/**
	 * 根据用户编号查询secKey
	 * @param userCode
	 * @return
	 */
	String getFingerSecKeyForUserCode(String userCode);
	
	/**
	 * 根据科室Code获取用户指纹列表
	 * @param deptCode           科室编码
	 * @param refDate				选择所有在参考时间后修改过的
	 * @return
	 */
	List<UserFinger> getFingerByDeptCodeAndDate(String deptCode, Date refDate);
	
	/**
	 * 保存上传的用户指纹信息
	 * @param userFinger
	 * @return
	 */
	int saveUserFinger(UserFinger userFinger);
	
	/**
	 * 获取生命体征配置信息
	 * @return
	 */
	public List<BodySignConfig> getBodysignConfig();
	/**
	 * 根据code获取菜单信息
	 * @param code
	 * @return
	 */
	List<SysMenu> getSysMenusByCode(String code);
	
	/**
	 * 执行数据重置
	 */
	public void resetData();

	/**
	 * 获取登陆牌信息界面
	 * @param deptCode
	 * @param nurseCode
	 * @param nurseName
	 * @return
	 */
	List<LoginCardInfo> getLoginCardInfos(String deptCode,String nurseCode,String nurseName);
	/**
	 * 获取登陆牌信息打印界面
	 * @param deptCode
	 * @param nurseCode
	 * @param nurseName
	 * @return
	 */
	List<LoginCardManager> getLoginCardManagers(String deptCode,String nurseCode,String nurseName);
	/**
	 * 根据登陆牌获取状态
	 * @param loginCardCode
	 * @return
	 */
	String getLoginCardStatus(String loginCardCode);
	/**
	 * 登陆牌信息插入
	 * @param loginCardInfo
	 * @return
	 */
	int insertLoginCardInfo(LoginCardInfo loginCardInfo);
	/**
	 * 登陆牌信息作废
	 * @param params
	 * @return
	 */
	int invalidLoginCardInfo(LoginCardInfo loginCardInfo);
	/**
	 * 判断默认科室是否存在
	 * @param nurseCode
	 * @return
	 */
	Integer getDefaultDeptCount(String nurseCode);
	/**
	 * 插入默认科室
	 * @param nurseCode
	 * @param deptCode
	 * @return
	 */
	int insertDefaultDept(String nurseCode,String deptCode);
	/**
	 * 修改默认科室
	 * @param nurseCode
	 * @param deptCode
	 * @return
	 */
	int updateDefaultDept(String nurseCode,String deptCode);
	
	/**
	 * 新增用户
	 * @param sysUser
	 * @return
	 */
	int insertSysUser(SysUser sysUser);
}
