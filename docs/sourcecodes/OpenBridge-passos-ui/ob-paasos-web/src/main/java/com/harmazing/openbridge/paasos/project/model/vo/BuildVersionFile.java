/**
 * Project Name:ob-paasos-web
 * File Name:BuildVersion.java
 * Package Name:com.harmazing.openbridge.paasos.project.model.vo
 * Date:2016年4月20日上午10:54:09
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.model.vo;

import java.io.Serializable;
import java.util.*;

/**
 * ClassName:BuildVersion <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月20日 上午10:54:09 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class BuildVersionFile  implements Serializable{
	
	private String fileName;
	
	private String filePath;
	
	private String buildNo;
	
	

	public BuildVersionFile() {
		super();
	}

	public BuildVersionFile(String fileName, String filePath) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}
	
	
}

