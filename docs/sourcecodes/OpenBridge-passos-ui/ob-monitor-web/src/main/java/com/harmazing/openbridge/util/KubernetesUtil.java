package com.harmazing.openbridge.util;

import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.harmazing.framework.util.ConfigUtil;

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
	
	public static NodeList getNodes(){
		NodeList nl = getKubernetesClient().nodes().list();
		if(nl==null || nl.getItems()==null || nl.getItems().size()==0){
			return null;
		}
		return nl;
	}

	


}
