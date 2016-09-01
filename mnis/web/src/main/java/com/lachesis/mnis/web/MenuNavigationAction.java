package com.lachesis.mnis.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.DocTreeService;
import com.lachesis.mnis.core.doctree.entity.ComNavTree;

@Controller
public class MenuNavigationAction {

	static final Logger LOGGER = LoggerFactory.getLogger(MenuNavigationAction.class);
	
	@Autowired
	private DocTreeService docTreeService;
	
	@RequestMapping(value = "/toliquor")
	public String toLiquor() {
		return "/nur/liquor";
	}
	
	@RequestMapping(value = "/topersral")
	public String toPersral() {
		return "/nur/persral";
	}

	@RequestMapping(value = "/toinfusion")
	public String toInfusion() {
		return "/nur/infusion";
	}
	
	@RequestMapping(value = "/toward")
	public String toWard() {
		return "/nur/ward";
	}
	
	@RequestMapping(value = "/patientMenuAction/getMenus")
	public @ResponseBody
	List<EasyUiTree> getMenus(String patientId, String hospitalNo,
			String departmentId) {
		//从资源获取所有菜单,并转换成树结构
		List<EasyUiTree> rslt = testResourceList(patientId, hospitalNo);
		return rslt;
	}
	
	private List<EasyUiTree> testResourceList(String patientId,
			String hospitalNo) {
		
		List<ComNavTree> list = docTreeService.getComNavTrees();
		List<EasyUiTree> rslt = new ArrayList<EasyUiTree>();
		if(null!=list
				&&list.size()>0){
			EasyUiTree t = null;
			for (ComNavTree comNavTree : list) {
				t = new EasyUiTree();
				//父节点
				if(comNavTree.getParent_id()==0){
					t.setId(comNavTree.getFieldtype());
					t.setText(comNavTree.getShowname());
					t.setChildren(getEasyUiTrees(list, comNavTree.getId()));
					rslt.add(t);
				}
			}
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("load resource tree for test.");
		}
		return rslt;
	}
	
	/**
	 * 递归方法
	 * @param list
	 * @return
	 */
	private List<EasyUiTree> getEasyUiTrees(List<ComNavTree> list,int id){
		List<EasyUiTree> rslt = null;
		EasyUiTree eaTree=null;
		for (ComNavTree c : list) {
			if(c.getParent_id()!=0){
				if(id==c.getParent_id()){
					if(rslt==null){
						rslt = new ArrayList<EasyUiTree>();
					}
					
					eaTree = new EasyUiTree();
					eaTree.setId(c.getFieldtype());
					eaTree.setText(c.getShowname());
					
					List<EasyUiTree> childrens = getEasyUiTrees(list, c.getId());
					if(childrens!=null
							&&childrens.size()>0){
						eaTree.setChildren(childrens);
					}
					rslt.add(eaTree);
				}
			}
		}
		
		return rslt;
	}
	
	public class EasyUiTree{
		private String id;
		private String text;
		private String state;
		private String checked;
		private Map<String,Object> attributes;
		private List<EasyUiTree> children;
		
		public EasyUiTree(){
			attributes = new HashMap<String,Object>();
			children = new ArrayList<EasyUiTree>();
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getChecked() {
			return checked;
		}

		public void setChecked(String checked) {
			this.checked = checked;
		}

		public Map<String, Object> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}

		public List<EasyUiTree> getChildren() {
			return children;
		}

		public void setChildren(List<EasyUiTree> children) {
			this.children = children;
		}
		
		public void addChild(EasyUiTree node){
			this.children.add(node);
		}

	}
}
