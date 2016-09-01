package com.lachesis.mnis.core.identity;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.bodysign.BodySignConstants;
import com.lachesis.mnis.core.bodysign.entity.BodySignConfig;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.*;
import com.lachesis.mnis.core.identity.repository.IdentityRepository;
import com.lachesis.mnis.core.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("identityService")
public class IdentityServiceImpl implements IdentityService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(IdentityServiceImpl.class);

	@Autowired
	private IdentityRepository identityRepository;

	/**
	 * 缓存sys_config中的系统配置信息 map 第一个String 存放的为code, 第二个String 存放的为value
	 */
	static Map<String, String> SYSTEM_CONFIGS = new HashMap<>();

	/**
	 * 缓存sys_config中的科室配置信息 map 第一个String 存放的为deptment code, 第二个String
	 * 存放的为ConfigBean
	 */
	static Map<String, List<ConfigBean>> DEPTMENT_CONFIGS = new HashMap<>();

	/**
	 * 缓存sys_config中的用户配置信息 map 第一个String 存放的为user code, 第二个String
	 * 存放的为ConfigBean
	 **/
	static Map<String, List<ConfigBean>> USER_CONFIGS = new HashMap<>();
	
	static Map<String, List<SysMenu>> MENU_CONFIG = new HashMap<String, List<SysMenu>>();
	/**
	 * 仅仅PC端数据
	 */
	static Map<String, String> PC_SYSTEM_CONFIGS = new HashMap<String, String>();
	
	/***
	 * 从数据库中初始化系统配置
	 */
	@PostConstruct
	@Override
	public void init() {
		List<ConfigBean> configList = identityRepository.getSystemConfig();
		for (ConfigBean config : configList) {
			switch (config.getType()) {
			case D:
				if (DEPTMENT_CONFIGS.containsKey(config.getOwner())) {
					DEPTMENT_CONFIGS.get(config.getOwner()).add(config);
				} else {
					List<ConfigBean> list = new ArrayList<>(configList.size());
					list.add(config);
					DEPTMENT_CONFIGS.put(config.getOwner(), list);
				}
				break;
			case U:
				if (USER_CONFIGS.containsKey(config.getOwner())) {
					USER_CONFIGS.get(config.getOwner()).add(config);
				} else {
					List<ConfigBean> list = new ArrayList<>(configList.size());
					list.add(config);
					USER_CONFIGS.put(config.getOwner(), list);
				}
				break;
			case S:
				SYSTEM_CONFIGS.put(config.getCode(), config.getName());
				break;
			default :
				//仅PC端本地缓存数据(LS)
				PC_SYSTEM_CONFIGS.put(config.getCode(), config.getName());
				break;
			}
		}
		
		List<SysMenu> sysMenus = getSysMenus();
		MENU_CONFIG.put("menu_config", sysMenus);
		
		/**
		 * 把数据放到缓存类，其他地方使用 不需要再@Autowired private IdentityService identityService;注册
		 */
		SuperCacheUtil.setSYSTEM_CONFIGS(SYSTEM_CONFIGS);
		
		SuperCacheUtil.setMENU_CONFIG(MENU_CONFIG);
		
		List<BodySignConfig> bodysignCfgs = identityRepository.getBodysignConfig();
		if(null != bodysignCfgs){
			for(BodySignConfig cfg : bodysignCfgs){
				SuperCacheUtil.BODY_SIGN_CONFIGS.put(cfg.getValue(), cfg);
			}
		}
	}

	@Override
	public UserInformation queryUserByCode(String code) {
		return identityRepository.getUserByCode(code);
	}

	/**
	 * 根据用户登录名查询用户信息
	 * @param loginID 登录名
	 * @param deptCode 科室编号，可以为空
     * @return 用户信息
     */
    @Override
	public UserInformation queryUserByLoginName(String loginID, String deptCode){
		if(StringUtils.isEmpty(loginID)){
			return null;
		}
		return identityRepository.getUserByLoginName(loginID, deptCode);
	}

	/**
	 * 根据登录类型和传入参数进行登录和会话管理
	 * 
	 * @param accountName
	 *            用户名
	 * @param passwd
	 *            用户密码
	 * @param loginType
	 *            登录类型
	 * @return
	 */
	public UserInformation verifyUser(String loginId, String password,String deptCode,boolean isDefault) {
		if (StringUtils.isBlank(loginId)) {
			return null;
		}
		UserInformation userAccount = identityRepository
				.getUserByLoginName(loginId.toUpperCase(),deptCode);
		if (userAccount == null) {
			return null;
		}
		
		// 密码校验:先加密再比较
		try {
			String encryptPwd = MnisConstants.EMPTY;
			boolean isEncrypt = isEncrypt();
			if (isEncrypt) {  //三院加密  东软
				encryptPwd = EncryptPasswordUtil.encrypt(password);
			} else {  //md5加密  胸科
				
				String encryptType = SYSTEM_CONFIGS
						.get(MnisConstants.SYS_CONFIG_ENCRYPT_TYPE);
				switch (encryptType) {
				case "2":  //沈阳胸科 md5解密  厦门置业
					encryptPwd = MD5Util.string2MD5(password);
					break;
				case "3":  //内江资中人民医院 成都先达
					encryptPwd = ZiZhongEncryptUtil.Encrypt(password);
					break;
				default:
					encryptPwd = password;
					break;
				}
				
				//
			}
			if (!StringUtils.isBlank(userAccount.getUser().getPassword())
					&& encryptPwd != null
					&& !StringUtils.equals(userAccount.getUser().getPassword(),
							encryptPwd)) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

		if(StringUtils.isNotBlank(deptCode)){
			List<SysDept> depts = identityRepository.getDeptmentsByCode(userAccount.getCode(), null);
			userAccount.setDeptList(depts);
		}
		//保存默认科室
		if(isDefault){
			saveDefaultDept(userAccount.getCode(), deptCode);
		}
		
		return userAccount;
	}

	@Override
	public List<SysDept> queryDeptments() {
		return identityRepository.getDeptments();
	}
	
	@Override
	public List<SysDept> getDeptmentsByCode(String nurseCode,String deptCode) {
		if(StringUtils.isBlank(nurseCode) && StringUtils.isBlank(deptCode)){
			return null;
		}
		return identityRepository.getDeptmentsByCode(nurseCode,deptCode);
	}

	@Override
	public List<Dict> queryDiagList() {
		return identityRepository.getDiagList();
	}

	@Override
	public List<Dict> queryDrugList() {
		return identityRepository.getDrugList();
	}

	@Override
	public Map<String, Map<String, String>> queryConfig(String userCode,
			String deptCode) {
		// 从缓存中取，设计需要考虑，这里设置是否过于复杂
		Map<String, Map<String, String>> configMap = new HashMap<>(3);
		configMap.put("system", SYSTEM_CONFIGS);
		
		configMap.put("pcSystem", PC_SYSTEM_CONFIGS);

		// deptament level
		configMap.put("dept",
				generateConfigDate(DEPTMENT_CONFIGS.get(deptCode)));

		// user level
		configMap.put("user", generateConfigDate(USER_CONFIGS.get(deptCode)));
		
		return configMap;
	}

	private Map<String, String> generateConfigDate(List<ConfigBean> list) {
		if (list == null || list.size() == 0) {
			return null;
		}

		Map<String, String> map = new HashMap<>(list.size());
		for (ConfigBean config : list) {
			map.put(config.getCode(), config.getName());
		}
		return map;
	}

	@Override
	public List<UserFinger> queryFingerByDeptCodeAndDate(String deptCode,
			Date refDate) {
		List<UserFinger> fpList = identityRepository
				.getFingerByDeptCodeAndDate(deptCode, refDate);
		for (UserFinger fpe : fpList) {
			// 原因：保存到NDA的时间是没有毫秒的,不清除，NDA每次都会新增一个指纹
			Date createDate = DateUtil.clearMilliSecond(fpe.getCreateDate());
			if (refDate == null || DateUtil.before(refDate, createDate)) {
				fpe.setModifyType(UserFinger.MODIFY_TYPE_CREATE);
			} else {
				// 修改时间已在参考时间之后
				fpe.setModifyType(UserFinger.MODIFY_TYPE_UPDATE);
			}
		}
		return fpList;
	}

	@Transactional
	public int saveUserFinger(String userCode, String deptCode, String feature,
			String adminId) {
		UserFinger userFinger = new UserFinger();
		userFinger.setUserCode(userCode);
		userFinger.setDeptCode(deptCode);
		userFinger.setFeature(feature);

		// Get unique secret key for a user
		String secretKey = identityRepository
				.getFingerSecKeyForUserCode(userCode);
		// 尚不存在该用户的密钥则生成之
		if (secretKey == null) {
			secretKey = StringUtil.getUUID();
		}
		userFinger.setSecretKey(secretKey);

		identityRepository.saveUserFinger(userFinger);
		return userFinger.getId();
	}

	@Override
	public String getHospitalName() {
		return SYSTEM_CONFIGS.get(MnisConstants.SYS_CONFIG_HOSPITAL_NAME);
	}

	@Override
	public String getConfigure(String configureCode) {
		return SYSTEM_CONFIGS.get(configureCode);
	}

	@Override
	public boolean isVeritalLabelBarcode() {
		String labelBarcodeType = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_CONFIG_LABEL_BARCODE_TYPE);
		return (StringUtils.isNotBlank(labelBarcodeType) && labelBarcodeType
				.equals("1")) ? true : false;
	}

	@Override
	public boolean isEncrypt() {
		String encryptType = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_CONFIG_ENCRYPT_TYPE);
		return (StringUtils.isNotBlank(encryptType) && encryptType.equals("1")) ? true
				: false;
	}
	

	@Override
	public String getLiquorOrderType() {
		String liquorOrderType = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_LIQUOR_ORDER_TYPE);
		return (StringUtils.isNotBlank(liquorOrderType) && !"2"
				.equals(liquorOrderType)) ? liquorOrderType : null;
	}

	@Override
	public int[] getNurseShiftTimes() {
		String nurseShiftTime = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_NURSE_SHIFT_TIME);
		String[] nurseShiftTimeStrs = null;
		int[] nurseShiftTimes = null;
		if (StringUtils.isBlank(nurseShiftTime))
			nurseShiftTimes = null;
		else {
			nurseShiftTimeStrs = nurseShiftTime.split(",");
			nurseShiftTimes = new int[nurseShiftTimeStrs.length];
			System.arraycopy(nurseShiftTimeStrs, 0, nurseShiftTimes, 0,
					nurseShiftTimeStrs.length);
		}
		return nurseShiftTimes;
	}

	@Override
	public boolean isOpenIs() {
		String isOpenIS = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_ISOPEN_IS);
		return (StringUtils.isNotBlank(isOpenIS) && isOpenIS.equals("1")) ? true
				: false;
	}
	
	@Override
	public boolean isSyncDocReport() {
		String isSyncDocReport = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_IS_SYNC_DOC_REPORT);
		return (StringUtils.isNotBlank(isSyncDocReport) && isSyncDocReport.equals("1")) ? true
				: false;
	}

	@Override
	public boolean isOrderInOutCopy() {
		String isOrderInOutCopy = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_IS_SYNC_ORDER_WITH_INOUT);
		return (StringUtils.isNotBlank(isOrderInOutCopy) && isOrderInOutCopy.equals("1")) ? true
				: false;
	}

	@Override
	public List<String> getSkinTestDrugCodes() {
		String skinTestDrugCodes = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_SKIN_TEST_DRUG_CODES);
		if(skinTestDrugCodes == null){
			return null;
		}
		return Arrays.asList(skinTestDrugCodes.split(MnisConstants.COMMA));
	}
	
	@Override
	public List<String> getBodySignDataStrategy(){
		List<String> bodySignDataStrategys = new ArrayList<String>();
		String bodySignDataStrategyStr = SYSTEM_CONFIGS
				.get(MnisConstants.SYS_BODY_SIGN_DATA_STRATEGY);
		if(bodySignDataStrategyStr == null){
			return BodySignConstants.BODY_SIGN_DATA_DEFAULT_STRATEGY;
		}
		Collections.addAll(bodySignDataStrategys, bodySignDataStrategyStr.split(MnisConstants.COMMA));
		return bodySignDataStrategys;
	}

	@Override
	public List<SysMenu> getSysMenus() {
		// 获取父菜单项
		List<SysMenu> parentSysMenus = identityRepository
				.getSysMenusByCode(null);

		for (SysMenu sysMenu : parentSysMenus) {
			String parentCode = sysMenu.getCode();
			List<SysMenu> sysMenus = identityRepository
					.getSysMenusByCode(parentCode);
			sysMenu.setChildren(sysMenus);
		}

		return parentSysMenus;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void resetData(){
		//数据重置
		identityRepository.resetData();
	}

	@Override
	public List<LoginCardInfo> getLoginCardInfos(String deptCode,
			String nurseCode, String nurseName) {
		if (StringUtils.isBlank(deptCode)) {
			LOGGER.error("IdentityServiceImpl getLoginCardInfos deptCode is null");
			throw new MnisException("部门为空!");
		}
		
		if(StringUtils.isBlank(nurseCode)){
			nurseCode = null;
		}
		
		if(StringUtils.isBlank(nurseName)){
			nurseName = null;
		}
		return identityRepository.getLoginCardInfos(deptCode, nurseCode, nurseName);
	}

	@Override
	public List<LoginCardManager> getLoginCardManagers(String deptCode,
			String nurseCode, String nurseName) {
		if (StringUtils.isBlank(deptCode)) {
			LOGGER.error("IdentityServiceImpl getLoginCardManagers deptCode is null");
			throw new MnisException("部门为空!");
		}
		
		if(StringUtils.isBlank(nurseCode)){
			nurseCode = null;
		}
		
		if(StringUtils.isBlank(nurseName)){
			nurseName = null;
		}
		return identityRepository.getLoginCardManagers(deptCode, nurseCode, nurseName);
	}

	@Override
	public int insertLoginCardInfo(LoginCardInfo loginCardInfo) {
		if(null == loginCardInfo){
			LOGGER.error("IdentityServiceImpl insertLoginCardInfo loginCardInfo is null");
			throw new MnisException("参数为空!");
		}
		
		if(null == loginCardInfo.getOperaDate()){
			loginCardInfo.setOperaDate(new Date());
		}
		//作废已有登陆牌
		invalidLoginCardInfo(loginCardInfo);
		
		
		/**
		 * 登陆牌有效天数：配置，默认30天
		 */
		int amount = SuperCacheUtil.getSYSTEM_CONFIGS().get("validLoginCardDays") == null ? 30 :
			Integer.valueOf(SuperCacheUtil.getSYSTEM_CONFIGS().get("validLoginCardDays"));
		loginCardInfo.setStartDate(loginCardInfo.getOperaDate());
		loginCardInfo.setStartDate(DateUtils.addDays(loginCardInfo.getStartDate(), amount));
		
		
		return identityRepository.insertLoginCardInfo(loginCardInfo);
	}
	
	@Override
	public int batchInsertLoginCardInfo(List<LoginCardInfo> loginCardInfos) {
		if(null == loginCardInfos || loginCardInfos.isEmpty()){
			LOGGER.error("IdentityServiceImpl insertLoginCardInfo loginCardInfo is null");
			return 0;
		}
		int count = 0;
		for (LoginCardInfo loginCardInfo : loginCardInfos) {
			count = insertLoginCardInfo(loginCardInfo);
		}
		return count;
	}

	@Override
	public int invalidLoginCardInfo(LoginCardInfo loginCardInfo) {
		
		if(null == loginCardInfo || 
				(StringUtils.isBlank(loginCardInfo.getNurseCode()) 
						&& StringUtils.isBlank(loginCardInfo.getLoginCardCode()))){
			LOGGER.error("IdentityServiceImpl invalidLoginCardInfo nurseCode is null and logincardCode is null");
			return 0;
		}
		
		if(loginCardInfo.getOperaDate() == null){
			loginCardInfo.setOperaDate(new Date());
		}
		return identityRepository.invalidLoginCardInfo(loginCardInfo);
	}

	@Override
	public UserInformation verfyLoginCardCode(String loginCardCode) {
		if(StringUtils.isBlank(loginCardCode)){
			LOGGER.error("IdentityServiceImpl verfyLoginCardCode loginCardCode is null!");
			throw new AlertException("登陆牌信息为空!");
		}
		
		//判断登陆牌状态(deptcode与nurseCode与status集合,"-"隔开)
		String NurseStatus = identityRepository.getLoginCardStatus(loginCardCode);
		if(StringUtils.isBlank(NurseStatus)){
			LOGGER.debug("IdentityServiceImpl verfyLoginCardCode status is null:登陆牌信息不存在!");
			throw new AlertException("登陆牌信息不存在!");
		}
		String[] nurseStatusArray = NurseStatus.split(MnisConstants.LINE);
		UserInformation userInformation = null;
		switch (Integer.valueOf(nurseStatusArray[2])) {
		case 1:
			LOGGER.debug("IdentityServiceImpl verfyLoginCardCode status=1:登陆牌作废!");
			throw new AlertException("登陆牌作废!");
		case 2:
			LOGGER.debug("IdentityServiceImpl verfyLoginCardCode status=2:登陆牌过期!");
			throw new AlertException("登陆牌过期!");
		default:
			String deptCode = nurseStatusArray[0];
			String nurseCode = nurseStatusArray[1];
			
			userInformation = queryUserByCode(nurseCode);
			if(StringUtils.isNotBlank(deptCode) && null != userInformation){
				List<SysDept> depts = identityRepository.getDeptmentsByCode(userInformation.getCode(), null);
				userInformation.setDeptList(depts);
			}
			break;
		}
		
		return userInformation;
	}

	@Override
	public int saveDefaultDept(String nurseCode, String deptCode) {
		if(StringUtils.isBlank(nurseCode) || StringUtils.isBlank(deptCode)){
			LOGGER.error("identityserviceimpl saveDefaultDept exist null: nurseCode:" + nurseCode + ";deptCode:" + deptCode);
			throw new AlertException("科室或用户编号为空!");
		}
		Integer count = identityRepository.getDefaultDeptCount(nurseCode) ;
		int operCount = 0;
		if(null != count && count > 0){
			//更新
			operCount = identityRepository.updateDefaultDept(nurseCode, deptCode);
		}else{
			operCount = identityRepository.insertDefaultDept(nurseCode, deptCode);
		}
		
		return operCount;
	}

	@Override
	public int batchInvalidLoginCardInfos(List<LoginCardInfo> loginCardInfos)
			throws Exception {
		if(null == loginCardInfos || loginCardInfos.isEmpty()){
			LOGGER.error("IdentityServiceImpl batchInvalidLoginCardInfos loginCardInfos is null");
			return 0;
		}
		int operCount = 0;
		for (LoginCardInfo loginCardInfo : loginCardInfos) {
			operCount = invalidLoginCardInfo(loginCardInfo);
		}
		
		return operCount;
	}

	@Override
	public boolean isSyncHis() {
		String isSyncHisStr = SuperCacheUtil.getSYSTEM_CONFIGS().get("isSyncHis");
		
		if(StringUtils.isBlank(isSyncHisStr)){
			return false;
		}else{
			if("1".equals(isSyncHisStr)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getSyncHisIp() {
		return SuperCacheUtil.getSYSTEM_CONFIGS().get("syncHisIp");
	}

	/**
	 * 获取体温单数据录入的时间节点
	 * @return 以时间节点为元素的字符数组
     */
    public String[] getTempratureInputTimeArray(){
		String tempInputTimeSelector = SYSTEM_CONFIGS.get(MnisConstants.SYS_CONFIG_TMPRT_TIME);
		return tempInputTimeSelector.split(",");
	}

	/**
	 * 根据输入的时间区间，获取对应的体温单时间节点
	 * @param timeInterval 时间区间，共包含四个元素：0，起始日期；1，起始时间；2、结束日期；3、结束时间
	 * @return 体温单的记录时间节点，字符串数组，包含两个元素：0、日期，1、时间
     */
    public String[] getTemperatureInputTimeByTimeInterval(String[] timeInterval){
		if(null==timeInterval || 0>=timeInterval.length){
			return new String[0];
		}
		//由于计算时间区间采用的是向前偏移，所以计算时间节点的话直接拿起始时间加上偏移量就好了
		Date inputTime = DateUtil.parse(timeInterval[0] + " " + timeInterval[1].substring(0, 5), DateUtil.DateFormat.YMDHM);
		if (inputTime == null) {
			return new String[0];
		}
		inputTime = DateUtils.addHours(inputTime, IdentityConstants.TEMPERATURE_TIME_OFFSET);

		String[] dateTime = new String[2];
		dateTime[0] = DateUtil.format(inputTime, DateUtil.DateFormat.YMD);
		dateTime[1] = DateUtil.format(inputTime, DateUtil.DateFormat.HM);

		return dateTime;
	}

	/**
	 * 根据体温单的具体情况，分割记录的时间区间
	 * NOTE:考虑到不同的跨天情况，对时间节点进行了排序，
	 * 		仅表示当天内的顺序时间节点，不保证与数据库中的节点顺序相对应
	 * @return 体温单分割区间的时间节点(格式：HM)，字符串数组，可能为空
     */
    private String[] getTemperatureTimeInterval(){
		//约束条件：获取到数组中的时间，1、格式为HM，2、按照从小到大排序
		String[] timeArray = getTempratureInputTimeArray();
		if(null==timeArray || 0>=timeArray.length){
			return new String[0];
		}
		//根据医院的具体情况确定区间边界，例如深圳三院规定为时间节点的前后两个小时
		for (int i = 0; i < timeArray.length; i++) {
			//采用向前偏移的计算方法
			Date time = DateUtil.parse(timeArray[i].substring(0, 5), DateUtil.DateFormat.HM);
			if (time == null) {
				//时间格式不合法
				return new String[0];
			}
			timeArray[i] = DateUtil.format(DateUtils.addHours(time, 0-IdentityConstants.TEMPERATURE_TIME_OFFSET),
											DateUtil.DateFormat.HM);
		}
		String[] timeInterval = new String[timeArray.length];
		if(DateUtil.after(timeArray[0].substring(0,5), timeArray[1].substring(0,5), DateUtil.DateFormat.HM)){
			//第一个时间节点跨天的情况，把第一个时间节点挪到最后一个就好了
			timeInterval[timeInterval.length-1] = timeArray[0];
			System.arraycopy(timeArray,1, timeInterval,0, timeArray.length-1);
		}else {
			//没有跨天，直接使用计算后的结果
			timeInterval = timeArray;
		}
		//确保一天之内时间区间没有交叉
		for (int j = 0; j < (timeInterval.length-1); j++) {
			if(DateUtil.after(timeInterval[j].substring(0,5), timeInterval[j+1].substring(0,5), DateUtil.DateFormat.HM)){
				//时间区间有交叉，说明计算结果有误，返回空数组
				return new String[0];
			}
		}
		return timeInterval;
	}

	/**
	 * 获取指定时间点所归属的体温单时间段
	 * @param date      指定日期
	 * @param time      指定时间
	 * @return 返回时间段字符串数组（可能为空），共包含四个元素：0，起始日期；1，起始时间；2、结束日期；3、结束时间
	 */
	public String[] getTempratureTimeIntervalOfSpecifiedTime(String date, String time){
		if(StringUtils.isEmpty(date) || StringUtils.isEmpty(time)){
			return new String[0];
		}
		Date theTime = DateUtil.parse(time.substring(0,5), DateUtil.DateFormat.HM);
		if (theTime == null) {
			return new String[0];
		}
		//获取体温单一天内时间的分割区间
		String[] timeArray = getTemperatureTimeInterval();
		if (timeArray==null || 0>=timeArray.length) {
			return new String[0];
		}
		try {
			String[] timeInterval = new String[4];
			for (int i = 0; i < timeArray.length; i++) {
				Date temprtTime = DateUtil.parse(timeArray[i], DateUtil.DateFormat.HM);
				if (temprtTime == null) {
					//获取到的日期节点不合法，继续计算可能会产生错误结果，直接返回好了
					return new String[0];
				}
				if(!theTime.before(temprtTime)){
					if(i >= (timeArray.length-1)){
						//传入时间晚于最后一个时间节点，最后一个节点作为起始时间
						//第一个节点作为结束时间，同时日期取后一天的日期
						timeInterval[0] = date;
						timeInterval[1] = timeArray[i];
						timeInterval[2] = DateUtil.addDate(date, 1);
						timeInterval[3] = timeArray[0];
						break;
					}
				}else {
					//获得截止时间
					timeInterval[2] = date;
					timeInterval[3] = timeArray[i];
					if(0 <= (i-1)){
						//前一个节点作为起始时间
						timeInterval[0] = date;
						timeInterval[1] = timeArray[i - 1];
					}else {
						//截止时间是第一个节点的情况，取最后一个节点作为起始时间
						//日期要取前一天的日期
						timeInterval[0] = DateUtil.addDate(date, -1);
						timeInterval[1] = timeArray[timeArray.length - 1];
					}
					break;
				}
			}
			return timeInterval;
		} catch (Exception e) {
			e.printStackTrace();
			return new String[0];
		}
	}

	@Override
	public int insertSysUser(SysUser sysUser) {
		return identityRepository.insertSysUser(sysUser);
	}
}