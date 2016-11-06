package com.harmazing.framework.util;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public abstract class ResourceUtil {
	private static PathResourcePatternResolver resourcePatternResolver = new PathResourcePatternResolver();

	public static Resource[] getResources(String pattern) throws IOException {
		return resourcePatternResolver.getResources(pattern);
	}

	public static Resource[] getResources(Resource rootPattern,
			String subPattern) throws IOException {
		return resourcePatternResolver
				.getRootResources(rootPattern, subPattern);
	}

	public static class PathResourcePatternResolver extends
			PathMatchingResourcePatternResolver {
		public Resource[] getRootResources(Resource rootPattern,
				String subPattern) throws IOException {
			Set<Resource> result = new LinkedHashSet<Resource>(16);
			Resource rootDirResource = resolveRootDirResource(rootPattern);
			if (isJarResource(rootDirResource)) {
				result.addAll(doFindPathMatchingJarResources(rootDirResource,
						subPattern));
			} else {
				result.addAll(doFindPathMatchingFileResources(rootDirResource,
						subPattern));
			}
			return result.toArray(new Resource[result.size()]);
		}
	}
}
