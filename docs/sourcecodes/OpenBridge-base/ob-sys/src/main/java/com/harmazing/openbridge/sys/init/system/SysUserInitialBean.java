package com.harmazing.openbridge.sys.init.system;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.openbridge.sys.init.ILaunchSystemBean;
import com.harmazing.openbridge.sys.init.InitContext;
import com.harmazing.openbridge.sys.user.dao.SysUserMapper;
import com.harmazing.openbridge.sys.user.model.SysUser;

/**
 * 初始化系统用户：系统管理员和svn管理员
 * @author taoshuangxi
 *
 */
@Service("SysUserInitialBean")
public class SysUserInitialBean implements ILaunchSystemBean {
	@Autowired
	private SysUserMapper userMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void onFirstStartup(InitContext context) {
		SysUser admin = userMapper.getUserByLoginName("admin");
		if(admin==null){
			admin =new SysUser();
			admin.setUserId("181f1ab4e4a6baac5f9158b265767ebc");
			admin.setUserName("系统管理员");
			admin.setLoginName("admin");
			admin.setLoginPassword("123456");
			admin.setRoles("Administrator");
			admin.setActivate(true);
			admin.setCreateTime(new Date());
			userMapper.create(admin);
		}
		SysUser svn = userMapper.getUserByLoginName("svn");
		if(svn==null){
			svn = new SysUser();
			svn.setUserId("af04a1aa0e00f476768e6030957aaaee");
			svn.setUserName("svn");
			svn.setLoginName("svn");
			svn.setLoginPassword("svn");
			svn.setActivate(true);
			svn.setCreateTime(new Date());
			svn.setSysUser(true);
			userMapper.create(svn);
		}
	}
}
