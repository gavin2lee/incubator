package com.harmazing.openbridge.paas.architecture.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.harmazing.framework.util.StringUtil;

public class GraphVo {
	
	private String name;
	
//	private String code;
	
//	private String url;
	
	private String id;
	
	
	private List<GraphVo> children = new ArrayList<GraphVo>();
	
	//protocol 协议
	//url IP:端口 或者 域名
	//error : 1 访问不通  2 缺少配置
	private Map<String,Object> property = new HashMap<String,Object>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}
//
//	public String getUrl() {
//		return url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}

	public List<GraphVo> getChildren() {
		return children;
	}

	public void setChildren(List<GraphVo> children) {
		this.children = children;
	}

	public Map<String, Object> getProperty() {
		return property;
	}

	public void setProperty(Map<String, Object> property) {
		this.property = property;
	}

	public String getId() {
		if(StringUtil.isEmpty(id)){
			return StringUtil.getUUID();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	
}
