package com.lachesis.mnis.core.old.doc;


public class DepartmentTemplate extends BaseEntity {	
        
	/** 
	 * @Fields serialVersionUID : TODO
	 */ 
	private static final long serialVersionUID = 1L;

	private String deptRefid;
        
	private String nodeType;
        
	private String nodeName;
        
	private String nodeParentRefid;
        
	private Integer ord;
        
	private String numberType;
        
	private String templateRefid;
	
	private String templateType;
        
	private String url;
                            		
	public String getDeptRefid() {
		return deptRefid;
	}

	public void setDeptRefid(String deptRefid) {
		this.deptRefid = deptRefid;
	}
		
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
		
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
		
	public String getNodeParentRefid() {
		return nodeParentRefid;
	}

	public void setNodeParentRefid(String nodeParentRefid) {
		this.nodeParentRefid = nodeParentRefid;
	}
		
	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}
		
	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}
		
	public String getTemplateRefid() {
		return templateRefid;
	}

	public void setTemplateRefid(String templateRefid) {
		this.templateRefid = templateRefid;
	}
		
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
							
}
