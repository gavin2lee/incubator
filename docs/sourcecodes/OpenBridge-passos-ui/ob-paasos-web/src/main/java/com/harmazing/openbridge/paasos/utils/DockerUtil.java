package com.harmazing.openbridge.paasos.utils;

import io.fabric8.docker.client.Config;
import io.fabric8.docker.client.ConfigBuilder;
import io.fabric8.docker.client.DefaultDockerClient;

import com.harmazing.framework.util.ConfigUtil;

public class DockerUtil {

	public static DefaultDockerClient getDefaultDockerClient() {

		String dockerRegistry = ConfigUtil.getConfigString("paasos.build.url");

		Config config = new ConfigBuilder().withDockerUrl(
				"http://" + dockerRegistry).build();
		try {
			DefaultDockerClient defaultDockerClient = new DefaultDockerClient(
					config);
			return defaultDockerClient;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
