package com.harmazing.openbridge.paasos.utils;

import io.fabric8.kubernetes.api.model.EventList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.dsl.PrettyLoggable;
import io.fabric8.kubernetes.client.dsl.internal.PodOperationsImpl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.squareup.okhttp.OkHttpClient;

public class KubernetesUtil {
	// protected static String K8_NAMESPACE="";

	public static KubernetesClient getKubernetesClient() {
		String K8_MASTERURL = ConfigUtil
				.getConfigString("paasos.k8s.apiserver");
		String K8_USERNAME = ConfigUtil.getConfigString("paasos.k8s.username");
		String K8_PASSWORD = ConfigUtil.getConfigString("paasos.k8s.password");
		ConfigBuilder builder = new ConfigBuilder().withMasterUrl(K8_MASTERURL);
		if (StringUtils.hasText(K8_USERNAME)) {
			builder.withUsername(K8_USERNAME);
			builder.withPassword(K8_PASSWORD);
		} else {
			builder.withTrustCerts(true);
		}
		builder.withRequestTimeout(10000);
		Config config = builder.build();
		return new DefaultKubernetesClient(config);

	}

	public static PodList getPods(String tenant, Map<String, String> labels) {
		PodList pl = null;
		if (StringUtils.hasText(tenant)) {
			pl = getKubernetesClient().pods().inNamespace(tenant)
					.withLabels(labels).list();
		} else {
			pl = getKubernetesClient().pods().inAnyNamespace()
					.withLabels(labels).list();
		}

		return pl;
	}
	

	public static PodList getPods(String tenant, String key, String... values) {
		PodList pl = null;
		if (StringUtils.hasText(tenant)) {
			pl = getKubernetesClient().pods().inNamespace(tenant)
					.withLabelIn(key, values).list();
		} else {
			pl = getKubernetesClient().pods().inAnyNamespace()
					.withLabelIn(key, values).list();
		}

		return pl;
	}

	public static String getPodLogs(String tenant, String podName) {
		PodOperationsImpl oi = (PodOperationsImpl) getKubernetesClient().pods().inNamespace(tenant).withName(podName);
		PrettyLoggable<String, LogWatch> noi = oi.tailingLines(4000);
		return noi.getLog();
	}

	public static EventList getPodEventLogs(String tenant, String podName) {
		EventList el;
		if (StringUtil.isNotNull(tenant)) {
			el = getKubernetesClient().events().inNamespace(tenant)
					.withField("involvedObject.name", podName).list();
		} else {
			el = getKubernetesClient().events().inAnyNamespace()
					.withField("involvedObject.name", podName).list();
		}
		return el;
	}

	public static EventList getPodEventLogs(String podName) {
		EventList el = getKubernetesClient().events().inAnyNamespace()
				.withField("involvedObject.name", podName).list();
		return el;
	}

	public static String getWebSSH(String namespace, String podName,
			String containerName) {
		return getWebSSH(namespace, podName, containerName, "sh");
	}

	public static String getWebSSH(String namespace, String podName,
			String containerName, String bash) {
		String webSSH = ConfigUtil.getConfigString("paasos.webssh.url");
		String webssh = webSSH + "@" + namespace + "," + podName + ","
				+ containerName + "," + bash + "@/";
		return webssh;
	}
}
