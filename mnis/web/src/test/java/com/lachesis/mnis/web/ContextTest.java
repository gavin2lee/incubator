package com.lachesis.mnis.web;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class ContextTest {
	@Test
	public void test() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		Assert.assertNotNull(wac);
	}
}
