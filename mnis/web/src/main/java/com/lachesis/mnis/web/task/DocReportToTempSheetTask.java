package com.lachesis.mnis.web.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anyi.report.entity.DocDataReportSync;
import com.anyi.report.service.DocReportService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Component
public class DocReportToTempSheetTask {

	@Autowired
	private DocReportService docReportService;
	
	private static Logger LOGGER = LoggerFactory.getLogger(DocReportToTempSheetTask.class);
	
	public void synDocReport(){
		Date date = new Date();
		
		String patId = null;
		
		LOGGER.debug("DocReportToTempSheetTask:start syn docreport data to tempsheet");
		
		String recordDay = DateUtil.format(date,DateFormat.YMD);
		Date fullDateTime = DateUtil.parse(recordDay,DateFormat.YMD);
 		
		String startDate = DateUtil.format(DateUtils.addDays(date,-1),com.lachesis.mnis.core.util.DateUtil.DateFormat.YMD) 
				+ MnisConstants.EMPTY + MnisConstants.DOC_REPORT_STATISTIC_TIME;
		String endDate = recordDay + MnisConstants.EMPTY + MnisConstants.DOC_REPORT_STATISTIC_TIME;
		
		List<String> outNames = new ArrayList<String>();
		//尿量
		outNames.add("out_name_01");
		
		List<DocDataReportSync> docDataReportSyncs = new ArrayList<DocDataReportSync>();
		try {
			//入量统计
			List<DocDataReportSync> docDataReportInSyncs = docReportService.getDocDataReportSync(startDate, endDate, patId ,null);
			//出量统计
//			docDataReportOutSyncs = docReportService.getDocDataReportSync(startDate, endDate, patId ,outNames);
			docDataReportSyncs.addAll(docDataReportInSyncs);
//			docDataReportSyncs.addAll(docDataReportOutSyncs);
			if( null != docDataReportSyncs && !docDataReportSyncs.isEmpty())
				docReportService.docDataReportSync(docDataReportSyncs, recordDay, fullDateTime);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
