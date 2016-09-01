package com.anyi.report.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.anyi.report.service.DocReportService;
import com.lachesis.mnis.core.event.DocReportEvent;
import com.lachesis.mnis.core.event.MnisEventListener;
/**
 * 体征或医嘱 转抄至护理文书 事件监听
 * @author ThinkPad
 *
 */
@Component
public class DocReportEventListener implements MnisEventListener<DocReportEvent> {
	
	@Autowired
	private DocReportService docReportService;
	@Override
	public void onApplicationEvent(DocReportEvent event) {
		docReportService.saveMnisInfo(event.getDocReportEventEntity());
	}

}
