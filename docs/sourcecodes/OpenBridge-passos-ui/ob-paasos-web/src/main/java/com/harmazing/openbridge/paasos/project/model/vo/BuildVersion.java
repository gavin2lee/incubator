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
public class BuildVersion implements Serializable{
	
	//版本ID
	private String versionId;
	
	//版本编号
	private String versionName;
	
	private List<BuildVersionFile> files = new ArrayList<BuildVersionFile>();
	
	private BuildImage imageInfo;

	public BuildVersion() {
		super();
	}

	public BuildVersion(String versionId, String versionName,
			List<BuildVersionFile> files) {
		super();
		this.versionId = versionId;
		this.versionName = versionName;
		this.files = files;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public List<BuildVersionFile> getFiles() {
		return files;
	}

	public void setFiles(List<BuildVersionFile> files) {
		this.files = files;
	}

	public BuildImage getImageInfo() {
		return imageInfo;
	}

	public void setImageInfo(BuildImage imageInfo) {
		this.imageInfo = imageInfo;
	}
	
	

}

