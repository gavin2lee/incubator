package com.harmazing.openbridge;

import com.harmazing.springboot.SampleTomcatApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleTomcatApplication.class)
@WebIntegrationTest({"server.port:0"})
@DirtiesContext
public class WebTestConfig {
	public Log log = LogFactory.getLog(this.getClass());
}
