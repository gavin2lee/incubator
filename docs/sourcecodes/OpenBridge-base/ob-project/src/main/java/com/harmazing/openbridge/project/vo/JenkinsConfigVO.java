package com.harmazing.openbridge.project.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.harmazing.framework.common.model.BaseModel;

/**
* @ClassName: JenkinsConfigVO 
* @Description: Jenkins配置属性 VO
* @author weiyujia
* @date 2016年4月7日 上午11:01:03 
*
 */
public class JenkinsConfigVO extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 项目类型 api / app
	 */
	private String sourceFromType;
	
	/**
	 * appName
	 */
	private String appName;
	/**
	 * 在必要的时候并发构建
	 */
	private String concurrentBuild;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 关闭构建
	 */
	private String disabled;
	/**
	 * maven 指令
	 */
	private String goals;
	/**
	 * invoke ant
	 */
	private String hudsonTasksAnt;
	/**
	 * Execute Windows batch command
	 */
	private String hudsonTasksBatchFile;
	/**
	 *  execute shell
	 */
	private String hudsonTasksShell;
	/**
	 * svn remote
	 */
	private String remote;
	/**
	 * rootPOM
	 */
	private String rootPOM;
	
	private String runPostStepsIfResult;
	
	private String type ;
	
	private String fileBelongModel;
	
	private String displayName;
	
	private String local;
	
	/**
	 * 
	 */
	private String postBuildUrl;
	
	private String numToKeep;
	
	public String getNumToKeep() {
		return numToKeep;
	}
	public void setNumToKeep(String numToKeep) {
		this.numToKeep = numToKeep;
	}
	public String getPostBuildUrl() {
		return postBuildUrl;
	}
	public void setPostBuildUrl(String postBuildUrl) {
		this.postBuildUrl = postBuildUrl;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getFileBelongModel() {
		return fileBelongModel;
	}
	public void setFileBelongModel(String fileBelongModel) {
		try {
			this.fileBelongModel =  URLDecoder.decode(fileBelongModel, "utf-8");
		} catch (UnsupportedEncodingException e) {
			this.fileBelongModel = fileBelongModel;
		}
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRunPostStepsIfResult() {
		return runPostStepsIfResult;
	}
	public void setRunPostStepsIfResult(String runPostStepsIfResult) {
		this.runPostStepsIfResult = runPostStepsIfResult;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getConcurrentBuild() {
		return concurrentBuild;
	}
	public void setConcurrentBuild(String concurrentBuild) {
		this.concurrentBuild = concurrentBuild;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getGoals() {
		return goals;
	}
	public void setGoals(String goals) {
		try {
			this.goals =  URLDecoder.decode(goals, "utf-8");
		} catch (UnsupportedEncodingException e) {
			this.goals = goals;
		}
	}
	public String getHudsonTasksAnt() {
		return hudsonTasksAnt;
	}
	public void setHudsonTasksAnt(String hudsonTasksAnt) {
		this.hudsonTasksAnt = hudsonTasksAnt;
	}
	public String getHudsonTasksBatchFile() {
		return hudsonTasksBatchFile;
	}
	public void setHudsonTasksBatchFile(String hudsonTasksBatchFile) {
		try {
			this.hudsonTasksBatchFile =  URLDecoder.decode(hudsonTasksBatchFile, "utf-8");
		} catch (UnsupportedEncodingException e) {
			this.hudsonTasksBatchFile = hudsonTasksBatchFile;
		}
	}
	public String getHudsonTasksShell() {
		return hudsonTasksShell;
	}
	public void setHudsonTasksShell(String hudsonTasksShell) {
		try {
			this.hudsonTasksShell =  URLDecoder.decode(hudsonTasksShell, "utf-8");
		} catch (UnsupportedEncodingException e) {
			this.hudsonTasksShell = hudsonTasksShell;
		}
	}
	public String getRemote() {
		return remote;
	}
	public void setRemote(String remote) {
		try {
			this.remote =  URLDecoder.decode(remote, "utf-8");
		} catch (UnsupportedEncodingException e) {
			this.remote = remote;
		}
	}
	public String getRootPOM() {
		return rootPOM;
	}
	public void setRootPOM(String rootPOM) {
		try {
			this.rootPOM =  URLDecoder.decode(rootPOM, "utf-8");
		} catch (UnsupportedEncodingException e) {
			this.rootPOM = rootPOM;
		}
	}
	public String getSourceFromType() {
		return sourceFromType;
	}
	public void setSourceFromType(String sourceFromType) {
		this.sourceFromType = sourceFromType;
	}
	
}
