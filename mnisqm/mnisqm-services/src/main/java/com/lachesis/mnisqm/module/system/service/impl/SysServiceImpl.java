package com.lachesis.mnisqm.module.system.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.system.dao.ClassFieldMapMapper;
import com.lachesis.mnisqm.module.system.dao.SysConfigMapper;
import com.lachesis.mnisqm.module.system.dao.SysDateMapper;
import com.lachesis.mnisqm.module.system.dao.SysFunctionMapperExt;
import com.lachesis.mnisqm.module.system.dao.SysRoleFunMapper;
import com.lachesis.mnisqm.module.system.dao.SysRoleMapper;
import com.lachesis.mnisqm.module.system.dao.SysUserMapper;
import com.lachesis.mnisqm.module.system.dao.SysUserMapperExt;
import com.lachesis.mnisqm.module.system.dao.SysUserRoleMapper;
import com.lachesis.mnisqm.module.system.dao.SysUserRoleMapperExt;
import com.lachesis.mnisqm.module.system.domain.ClassFieldMap;
import com.lachesis.mnisqm.module.system.domain.SysConfig;
import com.lachesis.mnisqm.module.system.domain.SysDate;
import com.lachesis.mnisqm.module.system.domain.SysFunction;
import com.lachesis.mnisqm.module.system.domain.SysRole;
import com.lachesis.mnisqm.module.system.domain.SysRoleFun;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.system.domain.SysUserRole;
import com.lachesis.mnisqm.module.system.service.ISysService;

@Service
public class SysServiceImpl implements ISysService{
	
	@Autowired
	private SysFunctionMapperExt functionMapper;
	
	@Autowired
	private SysUserMapperExt userMapperExt;

	@Autowired
	private SysUserRoleMapperExt userRoleMapper;
	
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private SysRoleFunMapper sysRoleFunMapper;

	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private SysDateMapper sysDateMapper;
	
	@Autowired
	private ClassFieldMapMapper classFieldMapMapper;
	
	@Autowired
	private SysConfigMapper sysConfigMapper;
	
	
	
	public SysUser checkLogin(String loginName,String password){
		//查询系统用户信息
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("loginName", loginName);
		SysUser user = userMapperExt.selectSysUserByName(param);
		if(null == user){
			throw new CommRuntimeException("用户名不存在!");
		}
		//密码处理
		if(StringUtils.isEmpty(password)){
			//空密码
			if(!StringUtils.isEmpty(user.getPassword())){
				throw new CommRuntimeException("密码错误!");
			}
		}else{
			if(!password.equals(user.getPassword())){
				throw new CommRuntimeException("密码错误!");
			}
		}
		return user;
	}

	@Override
	public List<Object> getFunctionByRole(String roleCode){
		//数据查询
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("roleCode", roleCode);
		List<SysFunction> funs = functionMapper.selectFunByRole(param);
		List<Object> rs = getFuns(funs);
			
		//数据返回
		return rs;
	}
	
	@Override
	public List<Object> getNotHaveRole(String roleCode){
		//数据查询
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("roleCode", roleCode);
		List<SysFunction> funs = functionMapper.selectNotHaveRole(param);
		List<Object> rs = getFuns(funs);
			
		//数据返回
		return rs;
	}

	@SuppressWarnings("unchecked")
	private List<Object> getFuns(List<SysFunction> funs) {
		//数据处理
		Map<String,Object> funMaps = new HashMap<String,Object>();
		
		List<String> indexs = new ArrayList<String>();
		
		if(null != funs){
			for(SysFunction func : funs){
				String fatherCode = func.getFatherFunCode();
				String funCode = func.getFunCode();
				Map<String,Object> funMap = new HashMap<String,Object>();
				funMap.put("funCode", funCode);
				funMap.put("funName", func.getFunName());
				funMap.put("funUrl", func.getFunUrl());
				funMap.put("icoName", func.getIcoName());
				
				if(StringUtils.isEmpty(fatherCode)){
					funMap.put("child", new ArrayList<Map<String,String>>());
					funMaps.put(funCode,funMap);
					indexs.add(funCode);
				}else{
					Map<String,Object> fatherMap = (Map<String, Object>) funMaps.get(fatherCode);
					if(fatherMap!=null){
						List<Map<String,Object>> lst = (List<Map<String, Object>>) fatherMap.get("child");
						lst.add(funMap);
						fatherMap.put("child", lst);
					}
				}
			}
		}
		
		//排序
		List<Object> rs = new ArrayList<Object>();
		for(String s : indexs){
			rs.add(funMaps.get(s));
		}
		return rs;
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveSysUserRole(SysUserRole userRole){
		//保存用户角色的使用状态
		userRoleMapper.updateSysUserRole(userRole);
	}
	
	@Override
	public SysUserRole getUserRoleByUserCode(Long userId){
		//数据查询-通过员工编号查询当前使用的角色
		Map<String,Object> parm= new HashMap<String,Object>();
		parm.put("userId", userId);
		SysUserRole userRole= userRoleMapper.selectUserRoleByUserCode(parm);
		//如果不存在正在已经使用的用户角色，那么查询一个未使用的
		if(null == userRole){
			userRole = userRoleMapper.selectNoUseUserRole(parm);
		}
		return userRole;
	}
	
	@Override
	public SysUserRole getUserRoleByRoleCode(String roleCode){
		Map<String,Object> parm= new HashMap<String,Object>();
		parm.put("roleCode", roleCode);
		return userRoleMapper.selectUserRoleByUserCode(parm);
	}

	@Override
	public List<SysRole> getUserSysRoles(String userCode) {
		List<SysRole> roles = null;
		if(StringUtils.isEmpty(userCode)){
			roles = sysRoleMapper.getAllRoles();
		}else{
			roles = sysRoleMapper.getUserSysRoles(userCode);
		}
		return roles;
	}

	@Override
	public int insertSysRole(SysRole record) {
		return sysRoleMapper.insert(record);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertSysUserRole(List<SysUserRole> sysUserRoles) {
		for (SysUserRole sysUserRole : sysUserRoles) {
			sysUserRoleMapper.insert(sysUserRole);
		}
	}

	@Override
	public int updateSysUserRole(SysUserRole sysUserRole) {
		return sysUserRoleMapper.updateByPrimaryKey(sysUserRole);
	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveSysRoleFun(List<SysRoleFun> sysRoleFuns, SysRole sysRole) {
		sysRole.setStatus(MnisQmConstants.STATUS_YX);
		SysRole role = sysRoleMapper.selectRoleByCode(sysRole);
		//系统角色不允许修改
		if(MnisQmConstants.STATUS_XT.equals(role.getStatus())){
			throw new CommRuntimeException("管理员角色不允许修改！");
		}
		sysRoleMapper.updateByPrimaryKey(sysRole);  //修改角色数据
		sysRoleFunMapper.deleteByRoleFun(sysRole.getRoleCode());
		if(sysRoleFuns!=null){
			for (SysRoleFun sysRoleFun : sysRoleFuns) {
				sysRoleFunMapper.insert(sysRoleFun);
			}
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveSysUser(SysUser sysUser) {
		int refV = 0;
		if(sysUser!=null
				&& null!=sysUser.getSeqId()){
			sysUserMapper.updateByPrimaryKey(sysUser);
		}else{
			SysUser user = sysUserMapper.selectSysUserByCode(sysUser);
			if(null != user){
				throw new CommRuntimeException("当前员工已经是用户，不允许再次添加！");
			}
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("loginName", sysUser.getLoginName());
			user = userMapperExt.selectSysUserByName(param);
			if(null != user){
				throw new CommRuntimeException("登录账号已经存在，请换一个登录名！");
			}
			
			sysUserMapper.insert(sysUser);
		}
		return refV;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteSysUser(SysUser sysUser) {
		if(null == sysUser.getSeqId()){
			throw new CommRuntimeException("seqId不允许为空！");
		}
		sysUser.setStatus(MnisQmConstants.STATUS_WX);//无效状态
		sysUserMapper.updateSysUserForDelete(sysUser);
	}

	@Override
	public List<SysUser> getSysUsers(String deptCode, String queryKey) {
		return sysUserMapper.getSysUsers(queryKey, deptCode);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateSysRole(SysRole record) {
		return sysRoleMapper.updateByPrimaryKey(record);  //修改角色数据;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteSysRole(SysRole role){
		SysRole sysRole = sysRoleMapper.selectRoleByCode(role);
		if(sysRole != null){
			if(MnisQmConstants.STATUS_XT.equals(sysRole.getStatus())){
				throw new CommRuntimeException("系统角色不允许删除！");
			}
			sysRoleMapper.updateForDelete(role);
		}
		//是否缓存
		sysRole = null;
		role = null;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveSysRole(SysRole role){
		role.setRoleCode(CodeUtils.getRoleCode());
		role.setCreatePerson("123");
		role.setUpdatePerson("123");
		role.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		sysRoleMapper.insert(role);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteUserRole(SysUserRole userRole){
		userRole.setStatus(MnisQmConstants.STATUS_WX);//无效
		sysUserRoleMapper.updateForDelete(userRole);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void creatSysDate(String year){
		
		//已存在的年份记录，不允许重新生成
		List<SysDate> sysDateList = sysDateMapper.getByDateUseLike(year+"%");
		if(sysDateList.size() != 0){
			return;
		}
		
		sysDateList = creatNewSysDate(year);
		for (SysDate sysDate : sysDateList) {
			sysDateMapper.insert(sysDate);
		}
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateSysDate(SysDate sysDate){
		return sysDateMapper.updateByPrimaryKey(sysDate);
	}
	
	@Override
	public List<SysDate> querySysDate(String year, String month){
		if(year==null && month==null){
			return sysDateMapper.getByDateUseLike(null);
		}
		String time = year + "-" + ((month.length() == 1) ? ("0"+month) : month);
		return sysDateMapper.getByDateUseLike(time+"%");
	}
	
	/**
	 * 生成默认一年内的日期数据
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 */
	public List<SysDate> creatNewSysDate(String year){
		Calendar star = Calendar.getInstance();//年初
        Calendar end = Calendar.getInstance();//年末
        Date nowDate = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//格式化日期
        try {
			star.setTime(sdf.parse(year + "-01-01"));
			star.setFirstDayOfWeek(Calendar.MONDAY);//设置每星期第一天是周一
			end.setTime(sdf.parse(year + "-12-31"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        String tempWeek;
        SysDate sysDate;
        List<SysDate> sysDateList = new ArrayList<SysDate>();
        for (; star.compareTo(end) <= 0; star.add(Calendar.DAY_OF_MONTH, 1)) {
        	sysDate = new SysDate();
        	tempWeek=String.valueOf(star.get(Calendar.DAY_OF_WEEK)-1); 
        	if("0".equals(tempWeek)){
        		tempWeek = "7";
        		sysDate.setIsWeekend("1");
            }
        	sysDate.setDate(sdf.format(star.getTime()));
        	sysDate.setMonth((star.get(Calendar.MONTH)+1)+"");
        	sysDate.setWeeks(star.get(Calendar.WEEK_OF_YEAR));
        	sysDate.setWeekDay(tempWeek);
        	sysDate.setCreateTime(nowDate);
        	sysDateList.add(sysDate);
        }
		return sysDateList;
	}

	@Override
	public List<ClassFieldMap> queryClassFieldMapByClass(String className) {
		List<ClassFieldMap> classFieldMaps=new ArrayList<ClassFieldMap>();
		
		return null;
	}
	
	@Override
	public SysConfig getSysConfigByConfigId(String configId) {
		return sysConfigMapper.getByConfigId(configId);
	}

	@Override
	public int updateSysConfig(SysConfig record) {
		return sysConfigMapper.updateByPrimaryKey(record);
	}
}