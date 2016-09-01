package com.lachesis.mnis.core.identity.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
import com.lachesis.mnis.core.identity.repository.IdentityRepository;
import com.lachesis.mnis.core.mybatis.mapper.IdentityMapper;

@Repository("identityRepository")
public class IdentityRepositoryImpl implements IdentityRepository {

	@Autowired
	private IdentityMapper identityMapper;
	
	@Override
	public UserInformation getUserByCode(String code) {
		return identityMapper.getUserByCode(code);
	}
	
	@Override
	public UserInformation getUserByLoginName(String loginName,String deptCode) {
		return identityMapper.getUserByLoginName(loginName,deptCode);
	}
	
	@Override
	public List<SysDept> getDeptments() {
		return identityMapper.getDeptments();
	}
	
	@Override
	public List<SysDept> getDeptmentsByCode(String nurseCode,String deptCode) {
		return identityMapper.getDeptmentsByCode(nurseCode,deptCode);
	}

	@Override
	public List<ConfigBean> getSystemConfig() {
		return identityMapper.getSystemConfig();
	}
	
	@Override
	public List<Dict> getDiagList() {
		return identityMapper.getDiagList();
	}
	
	@Override
	public List<Dict> getDrugList() {
		return identityMapper.getDrugList();
	}

	@Override
	public String getFingerSecKeyForUserCode(String userCode) {
		return identityMapper.getFingerSecKeyByUserCode(userCode);
	}
	
	@Override
	public List<UserFinger> getFingerByDeptCodeAndDate(String deptCode, Date refDate) {
		return identityMapper.getFingerByDeptCodeAndDate(deptCode, refDate);
	}

	@Override
	public int saveUserFinger(UserFinger userFinger) {
		return identityMapper.saveUserFinger(userFinger);
	}
	
	/**
	 * 获取生命体征配置信息
	 * @return
	 */
	@Override
	public List<BodySignConfig> getBodysignConfig(){
		return identityMapper.getBodysignConfig();
	}

	@Override
	public List<SysMenu> getSysMenusByCode(String code) {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		return identityMapper.getSysMenusByCode(params);
	}
	
	@Override
	public void resetData(){
		//数据重置
		identityMapper.resetData();
	}

	@Override
	public List<LoginCardInfo> getLoginCardInfos(String deptCode,
			String nurseCode, String nurseName) {
		return identityMapper.getLoginCardInfos(deptCode, nurseCode, nurseName);
	}

	@Override
	public List<LoginCardManager> getLoginCardManagers(String deptCode,
			String nurseCode, String nurseName) {
		return identityMapper.getLoginCardManagers(deptCode, nurseCode, nurseName);
	}

	@Override
	public int insertLoginCardInfo(LoginCardInfo loginCardInfo) {
		return identityMapper.insertLoginCardInfo(loginCardInfo);
	}

	@Override
	public int invalidLoginCardInfo(LoginCardInfo loginCardInfo) {
		return identityMapper.invalidLoginCardInfo(loginCardInfo);
	}

	@Override
	public String getLoginCardStatus(String loginCardCode) {
		return identityMapper.getLoginCardStatus(loginCardCode);
	}

	@Override
	public Integer getDefaultDeptCount(String nurseCode) {
		return identityMapper.getDefaultDeptCount(nurseCode);
	}

	@Override
	public int insertDefaultDept(String nurseCode, String deptCode) {
		return identityMapper.insertDefaultDept(nurseCode, deptCode);
	}

	@Override
	public int updateDefaultDept(String nurseCode, String deptCode) {
		return identityMapper.updateDefaultDept(nurseCode, deptCode);
	}

	@Override
	public int insertSysUser(SysUser sysUser) {
		return identityMapper.insertSysUser(sysUser);
	}
}
