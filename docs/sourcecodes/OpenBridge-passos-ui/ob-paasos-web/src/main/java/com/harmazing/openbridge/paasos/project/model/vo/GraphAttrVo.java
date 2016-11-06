/**
 * Project Name:ob-paasos-web
 * File Name:GraphAttrVo.java
 * Package Name:com.harmazing.openbridge.paasos.project.model.vo
 * Date:2016年5月18日下午3:44:55
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.model.vo;

import java.util.*;

/**
 * ClassName:GraphAttrVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月18日 下午3:44:55 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class GraphAttrVo {
	
	private String id;
	
	private String name;
	
	private Map<String,String> lines = new TreeMap<String, String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getLines() {
		return lines;
	}

	public void setLines(Map<String, String> lines) {
		this.lines = lines;
	}
	
	

}

