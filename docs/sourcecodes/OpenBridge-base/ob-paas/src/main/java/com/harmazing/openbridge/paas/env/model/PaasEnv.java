package com.harmazing.openbridge.paas.env.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.harmazing.framework.common.model.IBaseModel;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.openbridge.paas.nginx.vo.NginxConfVo;
import com.harmazing.openbridge.paas.util.EnvMarkUtil;

@SuppressWarnings("serial")
public class PaasEnv implements IBaseModel {
	private String envId;
	private String envName;
	private String envType;
	private String projectId;
	private String businessType;
	private String businessId;
	
	private String envMark;
	
	private String envMarkName;
	
	private List<NginxConfVo> paasNginxConfs = new ArrayList<NginxConfVo>();
	/**
	 * 创建者
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date creationTime;

	private List<PaasEnvResource> resources;

	public List<PaasEnvResource> getResources() {
		return resources;
	}

	public void setResources(List<PaasEnvResource> resources) {
		this.resources = resources;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public List<NginxConfVo> getPaasNginxConfs() {
		return paasNginxConfs;
	}

	public void setPaasNginxConfs(List<NginxConfVo> paasNginxConfs) {
		this.paasNginxConfs = paasNginxConfs;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getEnvMark() {
		return envMark;
	}

	public void setEnvMark(String envMark) {
		this.envMark = envMark;
	}

	public String getEnvMarkName() {
		if(StringUtils.isEmpty(envMark)){
			return null;
		}
		return EnvMarkUtil.get(envMark);
	}

	public void setEnvMarkName(String envMarkName) {
		this.envMarkName = envMarkName;
	}
	
	
	
}
