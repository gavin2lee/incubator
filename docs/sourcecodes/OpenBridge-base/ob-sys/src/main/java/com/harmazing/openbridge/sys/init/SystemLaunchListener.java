package com.harmazing.openbridge.sys.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.LogUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.web.SystemEvent;

@Service
public class SystemLaunchListener implements ApplicationListener<SystemEvent> {
	@Autowired
	private ILaunchSystemService launchSystemService;

	/**
	 * SpringBean加载完成，进行系统初始化
	 */
	@Override
	public void onApplicationEvent(SystemEvent event) {
		if (event.getType().equals(SystemEvent.STARTUP)) {
			String hasInitial = ConfigUtil.getConfigString("sys.initCheck");
			if (StringUtil.isNull(hasInitial) || hasInitial.equals("N")) {
				try {
					launchSystemService.launchSystem();
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error("系统启动初始化失败", e);
				}
			}
		}
	}
}
