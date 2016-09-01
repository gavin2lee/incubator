package com.anyi.report.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.anyi.report.service.DocReportService;

@Component
public class ReportDataBakTask {

	@Autowired
	private DocReportService docReportService;	
	
	@Scheduled(cron="${doc.bak.time}")
	public void reportDataBak(){
		docReportService.reportDataBak();
	}
}
