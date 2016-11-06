package com.harmazing.openbridge.paas.deploy.model;

import java.util.ArrayList;
import java.util.List;

public class PaasProjectProbes {
	
	private String type;
	
	private Long timeoutSeconds;
	
	private Long initialDelaySeconds;
	
	private List<PaasProjectProbesHandler> handlers = new ArrayList<PaasProjectProbesHandler>();

	public String getType() {
		return type;
	}

	public Long getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public Long getInitialDelaySeconds() {
		return initialDelaySeconds;
	}

	public List<PaasProjectProbesHandler> getHandlers() {
		return handlers;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTimeoutSeconds(Long timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}

	public void setInitialDelaySeconds(Long initialDelaySeconds) {
		this.initialDelaySeconds = initialDelaySeconds;
	}

	public void setHandlers(List<PaasProjectProbesHandler> handlers) {
		this.handlers = handlers;
	}
	
	
	
	

}
