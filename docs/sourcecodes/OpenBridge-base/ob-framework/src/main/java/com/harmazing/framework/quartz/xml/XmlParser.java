package com.harmazing.framework.quartz.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XmlParser {

	private final Log logger = LogFactory.getLog(this.getClass());

	public List<JobConfig> loadXmlDefinitions(InputStream input) {
		List<JobConfig> configs = new ArrayList<JobConfig>();
		try {
			Document doc = buildDocument(input);
			Element root = doc.getDocumentElement();
			List<Element> nl = DomUtils.getChildElementsByTagName(root, "job");
			for (int i = 0; i < nl.size(); i++) {
				Element job = nl.get(i);
				configs.add(parse(job));
			}
		} catch (Throwable ex) {
			logger.error("配置jobs文件解析过程中出错," + ex.getMessage());
		} finally {
			try {
				input.close();
			} catch (Exception e) {
			}
		}
		return configs;
	}

	private Document buildDocument(InputStream stream)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder parser = dbf.newDocumentBuilder();
		return parser.parse(stream);
	}

	public JobConfig parse(Element paramElement) {
		JobConfig config = new JobConfig();
		Element item = paramElement;
		String jobName = item.getAttribute("name");
		String jobService = item.getAttribute("service");
		String cronExpression = item.getAttribute("cronExpression");
		String jobType = item.getAttribute("type");
		String parameter = item.getAttribute("parameter");
		String onStartupRun = item.getAttribute("onStartupRun");
		config.setCronExpression(cronExpression);
		config.setJobName(jobName);
		config.setJobService(jobService);
		config.setJobType(jobType);
		config.setParameter(parameter);
		config.setOnStartupRun(onStartupRun);
		return config;
	}
}
