package com.harmazing.openbridge.sys.user.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.openbridge.sys.user.dao.SysDepartMapper;
import com.harmazing.openbridge.sys.user.dao.SysUserDepartmentMapper;
import com.harmazing.openbridge.sys.user.model.SysDepart;
import com.harmazing.openbridge.sys.user.service.ISysDepartService;
import com.harmazing.framework.util.StringUtil;

@Service
public class SysDepartService implements ISysDepartService {
	@Autowired
	private SysDepartMapper departMapper;
	@Autowired
	private SysUserDepartmentMapper userDepartMapper;

	/**
	 * 获取组织结构树形结构的字符串
	 * 
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public String getLevelStrutString() {
		List<SysDepart> rootDeparts = getDepartFullTree(null);
		StringBuffer sb = new StringBuffer();
		if (rootDeparts != null && rootDeparts.size() > 0) {
			for (SysDepart root : rootDeparts) {
				buildTreeString(sb, root);
			}
		}
		return sb.toString();
	}

	//构建html字符串
	private void buildTreeString(StringBuffer sb, SysDepart depart) {
		if (depart.getParentId().trim().equals("")) {
			sb.append("<li class='li_one'><p>");
		} else {
			sb.append("<li><p>");
		}
		List<SysDepart> subList = depart.getSubDepart();
		sb.append("<span class='pull-right'>");
		if (subList != null && subList.size() > 0) {
			sb.append("<i class='fa fa-angle-down' style='cursor: pointer;'></i>");
		}

		sb.append("</span><label style='cursor: pointer;'departId='"
				+ depart.getDeptId() + "'class='cglabel'>"
				+ depart.getDeptName() + "</label></p>");
		if (subList != null && subList.size() > 0) {
			sb.append("<ul class='subnav_li'>");
			for (SysDepart item : subList) {
				buildTreeString(sb, item);
			}
			sb.append("</ul>");
		}
		sb.append("</li>");
	}

	
	//构建所有部门的树形结构 排除掉某个部门及其子部门
	private List<SysDepart> getDepartFullTree(SysDepart excludeDepart){
		List<SysDepart> allDeparts = new ArrayList<SysDepart>();
		allDeparts.addAll(departMapper.getAllDeparts());
		List<SysDepart> rootDeparts = new ArrayList<SysDepart>();
		if(allDeparts.size()>0){
			for(SysDepart depart : allDeparts){
				if(StringUtil.isNull(depart.getParentId())&&!rootDeparts.contains(depart)){
					if(excludeDepart==null || !excludeDepart.getDeptId().equals(depart.getDeptId())){
						depart.setLevel(1);
						rootDeparts.add(depart);
					}
				}
			}
			allDeparts.removeAll(rootDeparts);
		}
		
		if ( rootDeparts.size() > 0) {
			for (SysDepart root : rootDeparts) {
				if(root!=null){
					setDepartSubTree(root,allDeparts,excludeDepart);
				}
			}
		}
		return rootDeparts;
	}
	
	/**
	 * 递归建立部门之间的树形结构排除掉某个部门及其子部门
	 * 
	 * @param parentDepart
	 * @return
	 */
	private void setDepartSubTree(SysDepart parentDepart,List<SysDepart> allDepart, SysDepart excludeDepart) {
		List<SysDepart> subDepart = new ArrayList<SysDepart>();
		if (allDepart!=null && allDepart.size()>0){
			for(SysDepart depart : allDepart){
				if(depart!=null && depart.getParentId().equals(parentDepart.getDeptId())){
					if(excludeDepart==null || !depart.getDeptId().equals(excludeDepart.getDeptId())){
						depart.setLevel(parentDepart.getLevel() + 1);
						subDepart.add(depart);
					}
				}
			}
			allDepart.removeAll(subDepart);
		}
		
		if (subDepart.size() > 0) {
			parentDepart.setSubDepart(subDepart);
			for (SysDepart depart : subDepart) {
				if(depart!=null){
					setDepartSubTree(depart,allDepart,excludeDepart);
				}
			}
		}
	}
	
	/**
	 * 获取单个组织结构的详细信息
	 * 
	 * @param deptId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public SysDepart getDepartById(String deptId) {
		return departMapper.getDepartById(deptId);
	}
	 
	/**
	 * 删除组织结构
	 * 
	 * @param deptId
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteDepartById(String deptId) {
		List<SysDepart> subDepart = departMapper.getDepartByParentId(deptId);
		if(subDepart!=null && subDepart.size()>0)
			return -1;
		departMapper.deleteDepartById(deptId);
		userDepartMapper.delUserDepartByDeptId(deptId);
		return 1;
	}

	
	/**
	 * 新增或修改组织结构
	 * 
	 * @param depart
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int saveOrUpdateDepart(SysDepart depart) {
			SysDepart currentDepart = departMapper.getDepartByNameAndParentId(depart.getDeptName(), depart.getParentId());
		if(depart.getDeptId().equals("")){
			//新增部门
			if(currentDepart!=null){
				return -1;
			}
			String departId = StringUtil.getUUID();
			depart.setDeptId(departId);
			String parentId = depart.getParentId();
			if(StringUtil.isNull(parentId)){
				//当前部门是顶级部门，设置其HID为自己的departId
				depart.setHierarchyId(departId);
			}else{
				//当前部门是子部门，设置其HID为其父亲的HID加上自已的departId
				SysDepart parentDepart = departMapper.getDepartById(parentId);
				String parentHId = parentDepart.getHierarchyId();
				if(StringUtil.isNull(parentHId)){
					parentHId = parentDepart.getDeptId();
				}
				depart.setHierarchyId(parentHId+","+departId);
			}
			departMapper.addDepart(depart);
		}else{
			//修改部门
			if(currentDepart!=null && !currentDepart.getDeptId().equals(depart.getDeptId())){
				return -1;
			}
			String departId = depart.getDeptId();
			SysDepart originDepart = departMapper.getDepartById(departId);
			String originHID = originDepart.getHierarchyId();
			String newHID="";
			if(StringUtil.isNull(depart.getParentId())){
				newHID=departId;
			}else{
				SysDepart parentDepart = departMapper.getDepartById(depart.getParentId());
				String parentHID = parentDepart.getHierarchyId();
				if(StringUtil.isNull(parentHID)){
					parentHID= parentDepart.getDeptId();
				}
				newHID=parentHID+","+departId;
			}
				
			departMapper.updateDepart(depart);
			if(StringUtil.isNotNull(originHID)&&!originHID.equals(newHID)){
				departMapper.updateDepartHierarchyId(newHID, originHID);
			}
		}
		return 1;
	}
	
	/**
	 * 构建父级部门的下拉框
	 */

	@Override
	@Transactional(readOnly=true)
	public String getDepartOptionString(SysDepart currentDepart) {
		List<SysDepart> departList=getDepartFullTree(currentDepart);
	
		StringBuffer sb = new StringBuffer();
		if(departList !=null && departList.size()>0){
			for(SysDepart depart: departList){
				buildOptionString(sb, depart, currentDepart);
			}
		}
		return sb.toString();
	}
	
	private String getLevelBlank(int level) {
		if (level == 0) {
			level = 1;
		}
		int count = level - 1;
		String blank = "";
		for (int i = 0; i < count; i++) {
			blank += "&nbsp;&nbsp;";	
		}
		return blank;
	}
	
	private void buildOptionString(StringBuffer sb, SysDepart depart, SysDepart subDepart){
		int level = depart.getLevel();
		String blanks = getLevelBlank(level);
		sb.append("<option value='" + depart.getDeptId() + "'");
		if (subDepart!=null && depart.getDeptId().equals(subDepart.getParentId())) {
				sb.append(" selected ");
		}
		sb.append(">");
		sb.append(blanks);
		sb.append(depart.getDeptName());
		sb.append("</option>");
		if(depart.getSubDepart()!=null){
			for(SysDepart childDepart : depart.getSubDepart())
				buildOptionString(sb, childDepart,subDepart);
		}
	}

	
	/**
	 * 获取当前部门的子部门
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SysDepart> getSubDepartById(String departId) {
		return departMapper.getDepartByParentId(departId);
	}

	@Override
	@Transactional(readOnly=true)
	public String getUserDepartOptionString(String id) {
		List<SysDepart> departList = this.getDepartFullTree(null);
		StringBuffer sb = new StringBuffer();
		if(departList!=null && departList.size()>0){
			for(SysDepart depart: departList){
				buildOptionString(sb,depart,id);
			}
		}
		return sb.toString();
	}
	
	private void buildOptionString(StringBuffer sb, SysDepart depart, String departId){
		int level = depart.getLevel();
		String blanks = getLevelBlank(level);
		sb.append("<option value='" + depart.getDeptId() + "'");
		if (departId!=null && depart.getDeptId().equals(departId)) {
				sb.append(" selected ");
		}
		sb.append(">");
		sb.append(blanks);
		sb.append(depart.getDeptName());
		sb.append("</option>");
		if(depart.getSubDepart()!=null){
			for(SysDepart childDepart : depart.getSubDepart())
				buildOptionString(sb, childDepart,departId);
		}
	}
	

}
