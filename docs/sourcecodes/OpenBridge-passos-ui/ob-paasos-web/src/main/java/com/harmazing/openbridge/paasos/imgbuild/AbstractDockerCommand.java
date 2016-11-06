package com.harmazing.openbridge.paasos.imgbuild;

import io.fabric8.docker.client.DefaultDockerClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.openbridge.paas.async.AsyncCallback;
import com.harmazing.openbridge.paas.async.AsyncCommand;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paasos.imgbuild.log.IPaaSBuildLogService;
import com.harmazing.openbridge.paasos.imgbuild.service.IPaasImageService;
import com.harmazing.openbridge.paasos.utils.DockerUtil;

public abstract class AbstractDockerCommand implements AsyncCommand,
		AsyncCallback {
	protected static final Log logger = LogFactory
			.getLog(AbstractDockerCommand.class);

	protected IPaasImageService geImageService() {
		return SpringUtil.getBean(IPaasImageService.class);
	}
	protected static IPaaSBuildLogService getBuildLogService() {
		IPaaSBuildLogService paaSBuildLogService = SpringUtil.getBean(IPaaSBuildLogService.class);
		return paaSBuildLogService;
	}
	protected DefaultDockerClient getDefaultDockerClient() {
		return DockerUtil.getDefaultDockerClient();
	}

	@Override
	public void execute(AsyncTask buildTask) {
		execute((BuildTask) buildTask);
	}

	public abstract void execute(BuildTask buildTask);
}
