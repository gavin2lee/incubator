package com.lachesis.mnis.core;

import com.lachesis.mnis.core.identity.entity.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/***
 * 
 * 用户相关：用户信息、指纹相关、系统配置及系统字典相关等
 *
 * @author yuliang.xu
 * @date 2015年6月9日 上午10:03:18 
 *
 */
public interface IdentityService {

	/**
	 * 根据员工账号/员工编号 查询员工信息
	 * 
	 * @param code
	 * @return
	 */
	UserInformation queryUserByCode(String code);

	/**
	 * 根据用户登录名查询用户信息
	 * @param loginID 登录名
	 * @param deptCode 科室编号，可以为空
	 * @return 用户信息
	 */
	UserInformation queryUserByLoginName(String loginID, String deptCode);

	/**
	 * 使用账号名/密码/密钥 验证 用户 
	 * 
	 * @param accountName
	 * @param passwd
	 * @return
	 */
	UserInformation verifyUser(String accountName, String password,String deptCode,boolean isDefault);

    /**
     * 获取所有科室
     * @return
     */
    List<SysDept> queryDeptments();
    
    public List<SysDept> getDeptmentsByCode(String nurseCode,String deptCode);

    /**
     * 获取诊断列表
     * @return
     */
    List<Dict> queryDiagList();
    
    /**
     * 获取药物列表
     * @return
     */
    List<Dict> queryDrugList();
    
    
    /***
     * 查询系统配置
     * @param userCode
     * @param deptCode
     * @return
     */
    Map<String, Map<String, String>> queryConfig(String userCode, String deptCode);
    
	/**
	 * 根据科室Code获取用户指纹列表
	 * @param deptCode           科室编码
	 * @param refDate				选择所有在参考时间后修改过的
	 * @return
	 */
    List<UserFinger> queryFingerByDeptCodeAndDate(String deptCode, Date refDate); 
	
	/**
	 * 保存指纹（如果我1112字节特征的先转换为1024字节）
	 * @param userCode
	 * @param deptCode
	 * @param feature		1112字节或1024字节的指纹特征
	 * @param adminId
	 * @return
	 */
	int saveUserFinger(String userCode, String deptCode, String feature, String adminId);
	
	/**
	 * 获取医院名称
	 */
	String getHospitalName();
	
	/**
	 * 获取瓶签类型
	 *
	 */
	boolean isVeritalLabelBarcode();
	/**
	 * 登陆是否加密
	 *
	 */
	boolean isEncrypt();
	/**
	 * 配液管理医嘱类型(0:临嘱，1：长嘱，2：全部)
	 * @return
	 */
	String getLiquorOrderType();
	/**
	 * 获取交班本时间点
	 * @return
	 */
	int[] getNurseShiftTimes();
	
	/**
	 * 是否关闭输液监视
	 * @return
	 */
	boolean isOpenIs();
	/**
	 * 医嘱,生命体征是否同步到文书
	 * @return
	 */
	boolean isSyncDocReport();

	/**
	 * 执行医嘱是否只有在包含出入量信息的情况下才同步到文书
	 * @return
     */
    boolean isOrderInOutCopy();
	/**
	 * 是否需要his数据同步
	 * @return
	 */
	boolean isSyncHis();
	/**
	 * his数据同步的ip和端口
	 * @return
	 */
	String getSyncHisIp();
	
	/**
	 * 是否启用交班功能
	 * @return
	 */
	//boolean isNurseShift();
	/***
	 * 通过配置项目编号获取系统配置
	 * 如： bodysign.display.rule=new
	 * 	new: display the latest data
	 *		first:  display the earliest data
	 * 	high:  dispaly the highest data
	 * @param configureCode
	 * @return
	 */
	String getConfigure(String configureCode);
	/**
	 * 获取皮试过滤药物code
	 * @return
	 */
	List<String> getSkinTestDrugCodes();
	/**
	 * 生命体征数据选择策略
	 * @return
	 */
	List<String> getBodySignDataStrategy();
	
	/**
	 * 获取菜单信息
	 * @param code
	 * @return
	 */
	List<SysMenu> getSysMenus();
	
	/**
	 * 数据重置
	 */
	void resetData();
	
	void init();
	
	/**
	 * 获取登陆牌信息界面
	 * @param deptCode
	 * @param nurseCode
	 * @param nurseName
	 * @return
	 */
	List<LoginCardInfo> getLoginCardInfos(String deptCode,String nurseCode,String nurseName) throws Exception;
	/**
	 * 获取登陆牌信息打印界面
	 * @param deptCode
	 * @param nurseCode
	 * @param nurseName
	 * @return
	 */
	List<LoginCardManager> getLoginCardManagers(String deptCode,String nurseCode,String nurseName) throws Exception;
	/**
	 * 登陆牌信息插入
	 * @param loginCardInfo
	 * @return
	 */
	int insertLoginCardInfo(LoginCardInfo loginCardInfo) throws Exception;
	/**
	 * 批量登陆牌信息插入
	 * @param loginCardInfo
	 * @return
	 */
	int batchInsertLoginCardInfo(List<LoginCardInfo> loginCardInfos) throws Exception;
	/**
	 * 登陆牌信息作废
	 * @param params
	 * @return
	 */
	int invalidLoginCardInfo(LoginCardInfo loginCardInfo) throws Exception;
	/**
	 * 登陆牌信息作废--批量
	 * @param params
	 * @return
	 */
	int batchInvalidLoginCardInfos(List<LoginCardInfo> loginCardInfos) throws Exception;
	/**
	 * 登陆牌核对
	 * @param loginCardCode
	 * @return
	 */
	UserInformation verfyLoginCardCode(String loginCardCode);
	/**
	 * 保存默认科室
	 * @param nurseCode
	 * @param deptCode
	 * @return
	 */
	int saveDefaultDept(String nurseCode,String deptCode) throws Exception;

	/**
	 * 获取体温单数据录入的时间节点
	 * @return 以时间节点为元素的字符数组
     */
    public String[] getTempratureInputTimeArray();

	/**
	 * 获取指定时间点所归属的体温单时间段
	 * @param date      指定日期
	 * @param time      指定时间
	 * @return 返回时间段字符串数组，共包含四个元素：0，起始日期；1，起始时间；2、结束日期；3、结束时间
	 */
	public String[] getTempratureTimeIntervalOfSpecifiedTime(String date, String time);

	/**
	 * 根据输入的时间区间，获取对应的体温单时间节点
	 * @param timeInterval 时间区间，共包含四个元素：0，起始日期；1，起始时间；2、结束日期；3、结束时间
	 * @return 体温单的记录时间节点，字符串数组，包含两个元素：0、日期，1、时间
	 */
	public String[] getTemperatureInputTimeByTimeInterval(String[] timeInterval);
	
	/**
	 * 新增用户
	 * @param sysUser
	 * @return
	 */
	int insertSysUser(SysUser sysUser);
}
