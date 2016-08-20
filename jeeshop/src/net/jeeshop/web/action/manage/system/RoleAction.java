package net.jeeshop.web.action.manage.system;

import net.jeeshop.core.BaseAction;
import net.jeeshop.core.ManageContainer;
import net.jeeshop.core.Services;
import net.jeeshop.core.exception.NotThisMethod;
import net.jeeshop.core.system.bean.Role;
import net.jeeshop.core.system.bean.User;
import net.jeeshop.services.manage.system.impl.MenuService;
import net.jeeshop.services.manage.system.impl.RoleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.common.comm.ServiceClient.Request;

/**
 * 角色action
 * @author huangf
 *
 */
public class RoleAction extends BaseAction<Role> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(RoleAction.class);
	private RoleService roleService;
	private MenuService menuService;

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	private Role role = new Role();

	/**
	 * 添加角色
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		Role r = new Role();
		r.setRole_name(getRequest().getParameter("roleName"));
		r.setId(getRequest().getParameter("id"));
		r.setRole_desc(getRequest().getParameter("roleDesc"));
		r.setRole_dbPrivilege(getRequest().getParameter("role_dbPrivilege"));
		r.setPrivileges(getRequest().getParameter("privileges"));
		r.setStatus(getRequest().getParameter("status"));
		if(r.getRole_name()==null || r.getRole_name().trim().equals("")){
			getResponse().getWriter().print("0");
			return null;
		}else{
			roleService.editRole(r,getRequest().getParameter("insertOrUpdate"));
		}
		
		getResponse().getWriter().write("1");
		return null;
	}
	
	@Override
	public String deletes() throws Exception {
		throw new NotThisMethod(ManageContainer.not_this_method);
	}

	/**
	 * 批量删除角色和角色下的所有权限
	 * @return
	 * @throws Exception
	 */
//	public String delet() throws Exception {
//		logger.error("role.delete...");
//		throw new NotThisMethod(ManageContainer.not_this_method);
////		if(getIds()!=null && getIds().length>0){
////			for(int i=0;i<getIds().length;i++){
////				if(getIds()[i].equals("1")){
////					throw new Exception("超级管理员不可删除！");
////				}
////			}
////			roleService.deletes(getIds());
////		}
////		return selectList();
//	}

	// getter setter
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public Role getE() {
		// TODO Auto-generated method stub
		return this.role;
	}

	@Override
	public Services<Role> getServer() {
		// TODO Auto-generated method stub
		return this.roleService;
	}

	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		String id = getRequest().getParameter("id");
		if (id==null || id.trim().equals("")){
			role.clear();
			role.setInsertOrUpdate("1");
		}
		else{
			role.clear();
			role.setId(id);
			role = roleService.selectOne(role);
			if (role == null) {
				role = new Role();
			}
			role.setInsertOrUpdate("2");
		}
		
		if(e==null){
			e = new Role();
		}
	}

	@Override
	public void insertAfter(Role e) {
		e.clear();
	}
	@Override
	protected void selectListAfter() {
		pager.setPagerUrl("role!selectList.action");
	}
	
	/**
	 * 只能是admin才具有编辑其他用户权限的功能
	 */
	@Override
	public String update() throws Exception {
		User user = (User)getRequest().getSession().getAttribute(ManageContainer.manage_session_user_info);
		if(!user.getUsername().equals("admin")){
			throw new NullPointerException(ManageContainer.RoleAction_update_error);
		}
		return super.update();
	}
}
