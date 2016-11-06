/**
 * Project Name:ob-paasos-web
 * File Name:PaasOsImage.java
 * Package Name:com.harmazing.openbridge.paasos.project.model
 * Date:2016年4月26日上午11:18:11
 * Copyright (c) 2016
 *
 */

package com.harmazing.openbridge.paasos.imgbuild.model;

import java.io.Serializable;

/**
 * ClassName:PaasOsImage <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年4月26日 上午11:18:11 <br/>
 * 
 * @author dengxq
 * @version
 * @since JDK 1.6
 * @see
 */
public class PaasOsImage implements Serializable {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = -4461478616449692416L;
	private String imageId;
	private String imageType;
	private String imageName;
	private String imageVersion;
	private String ports;
	private String command;
	private String args;
	private String workdir;
	private String dockerFile;
	private String buildType;
	private String buildFile;
	private String buildStatus;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageVersion() {
		return imageVersion;
	}

	public void setImageVersion(String imageVersion) {
		this.imageVersion = imageVersion;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getWorkdir() {
		return workdir;
	}

	public void setWorkdir(String workdir) {
		this.workdir = workdir;
	}

	public String getDockerFile() {
		return dockerFile;
	}

	public void setDockerFile(String dockerFile) {
		this.dockerFile = dockerFile;
	}

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}

	public String getBuildFile() {
		return buildFile;
	}

	public void setBuildFile(String buildFile) {
		this.buildFile = buildFile;
	}

	public String getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(String buildStatus) {
		this.buildStatus = buildStatus;
	}

}
