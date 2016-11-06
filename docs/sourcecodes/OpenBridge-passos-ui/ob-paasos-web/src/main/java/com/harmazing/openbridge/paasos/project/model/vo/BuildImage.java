/**
 * Project Name:ob-paasos-web
 * File Name:BuildImage.java
 * Package Name:com.harmazing.openbridge.paasos.project.model.vo
 * Date:2016年4月28日上午10:41:03
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.model.vo;

import java.io.Serializable;
import java.util.*;

/**
 * ClassName:BuildImage <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月28日 上午10:41:03 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class BuildImage implements Serializable{
	
	private String imageName;
	
	private List<BuildImagePort> ports = new ArrayList<BuildImagePort>();

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public List<BuildImagePort> getPorts() {
		return ports;
	}

	public void setPorts(List<BuildImagePort> ports) {
		this.ports = ports;
	}
	
	
	

}

