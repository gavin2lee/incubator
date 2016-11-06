package com.harmazing.openbridge.sys.init.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.ResourceUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.framework.util.XmlUtil;
import com.harmazing.openbridge.sys.config.dao.SysConfigMapper;
import com.harmazing.openbridge.sys.init.ILaunchSystemBean;
import com.harmazing.openbridge.sys.init.ILaunchSystemService;
import com.harmazing.openbridge.sys.init.InitContext;

@Service
public class LaunchSystemServiceImpl implements ILaunchSystemService {
	private final Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private SysConfigMapper sysConfigMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void launchSystem() throws RuntimeException {
		try {
			InitContext context = new InitContext();
			systemInit(context);
		} catch (Exception e) {
			throw new RuntimeException("系统初始化出错。", e);
		}
	}

	/**
	 * 执行系统初始化
	 * 
	 * @throws Exception
	 */
	private void systemInit(InitContext context) throws Exception {
		List<ILaunchSystemBean> initBeans = getInitResource();
		for (ILaunchSystemBean initBean : initBeans) {
			initBean.onFirstStartup(context);
		}
		String hasInitial = ConfigUtil.getConfigString("sys.initCheck");
		if (StringUtil.isNull(hasInitial)) {
			sysConfigMapper.saveSysCoreConfig("sys.initCheck", "Y");
		} else {
			sysConfigMapper.updateSysCoreConfig("sys.initCheck", "Y");
		}
	}

	/**
	 * 读取配置文件获取系统初始化配置信息
	 * 
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws Exception
	 */
	private List<ILaunchSystemBean> getInitResource() throws Exception {
		List<ILaunchSystemBean> initBeansConfiged = new ArrayList<ILaunchSystemBean>();
		Resource[] r = ResourceUtil.getResources("classpath:config/init.xml");
		InputStream input = r[0].getInputStream();
		// 记录需要加载的bean的名称，防止重复加载
		Map<String, String> initBeanNames = new HashMap<String, String>();
		Document doc = XmlUtil.buildDocument(input);
		List<Element> initBeanElements = XmlUtil.getChildElementsByTagName(
				doc.getDocumentElement(), "initBean");
		Collections.sort(initBeanElements, new Comparator<Element>() {
			@Override
			public int compare(Element beanEle1, Element beanEle2) {
				Integer eleOneOrder;
				try {
					eleOneOrder = Integer.parseInt(beanEle1
							.getAttribute("order"));
				} catch (NumberFormatException e) {
					eleOneOrder = 999999;
				}
				Integer eleTwoOrder;
				try {
					eleTwoOrder = Integer.parseInt(beanEle2
							.getAttribute("order"));
				} catch (NumberFormatException e) {
					eleTwoOrder = 999999;
				}
				return eleOneOrder - eleTwoOrder;
			}
		});
		for (int i = 0; i < initBeanElements.size(); i++) {
			Element e = initBeanElements.get(i);
			String beanName = e.getAttribute("beanName");

			if (!initBeanNames.containsKey(beanName)) {
				initBeanNames.put(beanName, beanName);
				ILaunchSystemBean sysInitBean = (ILaunchSystemBean) WebUtil
						.getApplicationContext().getBean(beanName);
				initBeansConfiged.add(sysInitBean);
			}
		}
		return initBeansConfiged;
	}
}
